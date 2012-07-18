package uk.ac.standrews.grasp.ide.builder;

import grasp.lang.Compiler;
import grasp.lang.IArchitecture;
import grasp.lang.ICompiler;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.StringWriter;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import shared.error.IError;
import shared.error.IErrorReport;
import shared.io.ISource;
import shared.logging.ILogger;
import shared.logging.ILogger.Level;
import shared.xml.DomXmlWriter;
import shared.xml.IXmlWriter;
import uk.ac.standrews.grasp.ide.GraspPlugin;
import uk.ac.standrews.grasp.ide.Log;
import uk.ac.standrews.grasp.ide.model.ArchitectureModel;
import uk.ac.standrews.grasp.ide.model.GraspModel;

/**
 * Incremental build for Grasp
 * @author Dilyan Rusev
 *
 */
public class GraspBuilder extends IncrementalProjectBuilder  {	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.internal.events.InternalBuilder#build(int,
	 *      java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IProject[] build(int kind, Map<String,String> args, IProgressMonitor monitor) throws CoreException {
		if (kind == FULL_BUILD) {
			fullBuild(monitor);
		} else {
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				fullBuild(monitor);
			} else {
				incrementalBuild(delta, monitor);
			}
		}
		return null;
	}

	private void performBuild(IResource resource, final IProgressMonitor monitor, boolean buildXml) {
		if (resource instanceof IFile && resource.getName().endsWith(".grasp")) {
			IFile file = (IFile) resource;
			deleteMarkers(file);
			buildIndividualFile(file, SubMonitor.convert(monitor), buildXml);
		}
	}
	
	private void buildIndividualFile(IFile file, SubMonitor progress, boolean buildXml) {	
		progress.setWorkRemaining(3);
		progress.setTaskName("Compiling " + file);
		ISource source = new GraspSourceFile(file);
		ILogger logger = new GraspCompilationLogger().initialize(file.getName(), Level.ERROR, false);
		ICompiler compiler = new Compiler();
		
		try {		
			IArchitecture graph = compiler.compile(source, logger);
			progress.worked(1);
			
			GraspModel.INSTANCE.ensureFileStats(file).compiled(graph, compiler.getErrors());
			
			IErrorReport errorReport = compiler.getErrors();
			for (IError error: errorReport.getErrors()) {
				createProblemMarker(file, error);
			}
			
			if (!errorReport.isAny() && graph != null && buildXml) {				
				progress.setTaskName("Building xml for " + file);
				IProject project = file.getProject();
				if (project == null) {
					Log.error("Cannot find project of " + file, null);
					return;
				}
				IFile xmlFile = project.getFile(file.getProjectRelativePath().addFileExtension("xml"));
				try {	
					StringWriter stringWriter = new StringWriter();
					BufferedWriter output = new BufferedWriter(stringWriter);
					IXmlWriter xml = new DomXmlWriter();
					graph.toXml(xml);
					xml.serialize(output);
					output.close();
					String txt = stringWriter.toString();	
					if (!xmlFile.exists()) {
						xmlFile.create(new ByteArrayInputStream(txt.getBytes("utf-8")), true, progress.newChild(1));
					}
					else {
						xmlFile.setContents(new ByteArrayInputStream(txt.getBytes("utf-8")), true, true, progress.newChild(1));
					}
					xmlFile.setDerived(true, progress.newChild(1));
				} catch (CoreException e) {
					Log.error(e);
				} catch (FileNotFoundException e) {
					Log.error(e);
				} catch (IOException e) {
					Log.error(e);
				} catch (ParserConfigurationException e) {
					Log.error(e);
				} catch (TransformerException e) {
					Log.error(e);
				} 				
			}
		} finally {
			logger.shutdown();
		}		
	}

	private void fullBuild(final IProgressMonitor monitor) throws CoreException {		
		getProject().accept(new IResourceVisitor() {				
			@Override
			public boolean visit(IResource resource) throws CoreException {
				// full build -> build XML
				performBuild(resource, monitor, true);
				return true;
			}
		});		
	}

	private void incrementalBuild(IResourceDelta delta, final IProgressMonitor monitor) throws CoreException {
		delta.accept(new IResourceDeltaVisitor() {			
			@Override
			public boolean visit(IResourceDelta delta) throws CoreException {
				// ignore remove
				int kind = delta.getKind();
				if (kind == IResourceDelta.CHANGED || kind == IResourceDelta.ADDED) {
					// syntax checking only
					performBuild(delta.getResource(), monitor, true);
				}
				return true;
			}
		});
	}
	
	private void createProblemMarker(IFile file, IError error) {
		try {
			IMarker marker = file.createMarker(GraspPlugin.ID_PROBLEM_MARKER);
			marker.setAttribute(IMarker.MESSAGE, error.getMessage());
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			int lineNumber = error.getLine();
			if (lineNumber == -1) {
				lineNumber = 1;
			}
			marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
			
			int linePos = getLinePositionInFile(file, lineNumber);
			if (linePos != -1) {								
				marker.setAttribute(IMarker.CHAR_START, linePos + error.getColumn() + 1);
				marker.setAttribute(IMarker.CHAR_END, linePos + error.getColumnEnd() + 1);	
				marker.setAttribute(IMarker.LOCATION, String.format("line %d [%d:%d]", lineNumber, error.getColumn(), error.getColumnEnd()));
			}
			
		} catch (CoreException e) {
			Log.error(e);
		}
	}
	
	private static int getLinePositionInFile(IFile file, int line) {
		LineNumberReader reader = null;
		try {
			reader = new LineNumberReader(new InputStreamReader(file.getContents()));
			reader.setLineNumber(1);
			int counter = 0;
			while (reader.read() != -1 && reader.getLineNumber() != line) {
				counter++;
			}
			return counter;
		} catch (CoreException e) {
			Log.error(e);
			return -1;
		} catch (IOException e) {
			Log.error(e);
			return -1;
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				Log.error(e);
			}
		}
	}	
	
	private void deleteMarkers(IFile file) {
		try {
			file.deleteMarkers(GraspPlugin.ID_PROBLEM_MARKER, false, IResource.DEPTH_INFINITE);
		} catch (CoreException ce) {
			Log.error(ce);
		}
	}
}
