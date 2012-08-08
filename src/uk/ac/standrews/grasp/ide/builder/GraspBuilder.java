package uk.ac.standrews.grasp.ide.builder;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import uk.ac.standrews.grasp.ide.GraspPlugin;
import uk.ac.standrews.grasp.ide.Log;
import uk.ac.standrews.grasp.ide.compiler.CompilationError;
import uk.ac.standrews.grasp.ide.compiler.CompilationOptions;
import uk.ac.standrews.grasp.ide.compiler.CompilationResult;
import uk.ac.standrews.grasp.ide.compiler.Compilers;
import uk.ac.standrews.grasp.ide.compiler.ICompiler;

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
		ICompiler compiler = Compilers.getCurrent();
		
		CompilationOptions options = 
				new CompilationOptions()
				.setXmlFile(getXmlFileFor(file))
				.setProgressMonitor(progress);
		CompilationResult res = compiler.compile(file, options);
		if (!res.isSuccessful()) {
			for (CompilationError err: res.getErrors()) {
				createProblemMarker(file, err);
			}
		}
				
	}
	
	private IFile getXmlFileFor(IFile graspFile) {
		IPath path = graspFile.getProjectRelativePath();
		path = path.addFileExtension("xml");
		return graspFile.getProject().getFile(path);
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
	
	private void createProblemMarker(IFile file, CompilationError error) {
		try {
			IMarker marker = file.createMarker(GraspPlugin.ID_PROBLEM_MARKER);
			marker.setAttribute(IMarker.MESSAGE, error.getMessage());
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			int lineNumber = error.getLine();			
			marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
			
			int linePos = getLinePositionInFile(file, lineNumber);
			if (linePos != -1) {				
				marker.setAttribute(IMarker.CHAR_START, linePos + error.getColumn());
				marker.setAttribute(IMarker.CHAR_END, linePos + error.getColumnEnd());	
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
