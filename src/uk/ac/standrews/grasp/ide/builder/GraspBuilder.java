package uk.ac.standrews.grasp.ide.builder;

import grasp.lang.Compiler;
import grasp.lang.IArchitecture;
import grasp.lang.ICompiler;

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
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import shared.error.IError;
import shared.error.IErrorReport;
import shared.io.ISource;
import shared.logging.ILogger;
import shared.logging.ILogger.Level;
import uk.ac.standrews.grasp.ide.GraspPlugin;
import uk.ac.standrews.grasp.ide.Log;

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
			// TODO: hard-coded task number
			buildIndividualFile(file, SubMonitor.convert(monitor), buildXml);
		}
	}
	
	private void buildIndividualFile(IFile file, SubMonitor progress, boolean buildXml) {	
		// TODO: use progress monitor
		ISource source = new GraspSourceFile(file);
		ILogger logger = new GraspCompilationLogger().initialize(file.getName(), Level.ERROR, false);
		ICompiler compiler = new Compiler();
		
		try {		
			IArchitecture graph = compiler.compile(source, logger);
			
			IErrorReport errorReport = compiler.getErrors();
			
			for (IError error: errorReport.getErrors()) {
				createProblemMarker(file, error);
			}
			
			if (!errorReport.isAny() && graph != null && buildXml) {
				// TODO: Build XML
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
					performBuild(delta.getResource(), monitor, false);
				}
				return true;
			}
		});
	}
	
	private void createProblemMarker(IFile file, IError error) {
		try {
			IMarker marker = file.createMarker(GraspPlugin.ID_MARKER);
			marker.setAttribute(IMarker.MESSAGE, error.getMessage());
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			int lineNumber = error.getLine();
			if (lineNumber == -1) {
				lineNumber = 1;
			}
			marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
			// TODO: Set both IMarker.CHAR_START and IMarker.CHAR_END
			//marker.setAttribute(IMarker.CHAR_START, error.getColumn());
			
		} catch (CoreException e) {
			Log.error(e);
		}
	}
	
	private void deleteMarkers(IFile file) {
		try {
			file.deleteMarkers(GraspPlugin.ID_MARKER, false, IResource.DEPTH_ZERO);
		} catch (CoreException ce) {
			Log.error(ce);
		}
	}
}
