package uk.ac.standrews.grasp.ide.builder;

import grasp.lang.Compiler;
import grasp.lang.IArchitecture;
import grasp.lang.ICompiler;

import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

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
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import shared.error.IError;
import shared.io.FileSource;
import shared.io.ISource;
import shared.logging.ILogger;
import uk.ac.standrews.grasp.ide.Log;

/**
 * Incremental build for Grasp
 * @author Dilyan Rusev
 *
 */
public class GraspBuilder extends IncrementalProjectBuilder implements IGraspBuilder {	
	public static final String BUILDER_ID = "uk.ac.standrews.grasp.ide.graspBuilder";
	public static final String MARKER_TYPE = "uk.ac.standrews.grasp.ide.graspProblem";
	
	private final IResourceVisitor resourceVisitor;
	private final IResourceDeltaVisitor resourceDeltaVisitor;
	
	public GraspBuilder() {
		GraspBuilderResourceVisitor impl = new GraspBuilderResourceVisitor(this);
		this.resourceDeltaVisitor = impl;
		this.resourceVisitor = impl;
	}
		
	
	
	private void deleteMarkers(IFile file) {
		try {
			file.deleteMarkers(MARKER_TYPE, false, IResource.DEPTH_ZERO);
		} catch (CoreException ce) {
			Log.error(ce);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.internal.events.InternalBuilder#build(int,
	 *      java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {
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

	@Override
	public void performBuild(IResource resource) {
		if (resource instanceof IFile && resource.getName().endsWith(".grasp")) {
			IFile file = (IFile) resource;
			deleteMarkers(file);
			buildIndividualFile(file);
		}
	}
	
	private void buildIndividualFile(IFile file) {
		ISource source = new GraspSourceFile(file);
		ILogger logger = new GraspCompilationLogger(file, MARKER_TYPE);
		ICompiler compiler = new Compiler();
		IArchitecture graph = compiler.compile(source, logger);
		
//		for (IError error: compiler.getErrors().getErrors()) {
//			error.getLine()
//		}
		
		if (compiler.getErrors().isAny() || graph == null) {
			logger.print("%s failed to compile due to erors", source);
		} else {
			logger.print("%s compiled successfully", source);
		}
	}

	private void fullBuild(final IProgressMonitor monitor)
			throws CoreException {
		try {
			getProject().accept(resourceVisitor);
		} catch (CoreException e) {
			Log.error(e);
		}
	}

	private void incrementalBuild(IResourceDelta delta,
			IProgressMonitor monitor) throws CoreException {
		delta.accept(resourceDeltaVisitor);
	}
}
