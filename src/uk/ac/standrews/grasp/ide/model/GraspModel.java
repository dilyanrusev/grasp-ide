package uk.ac.standrews.grasp.ide.model;

import grasp.lang.IElement;
import grasp.lang.ISyntaxTree;
import grasp.lang.Parser;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.content.IContentDescription;

import shared.io.ISource;
import uk.ac.standrews.grasp.ide.GraspPlugin;
import uk.ac.standrews.grasp.ide.Log;
import uk.ac.standrews.grasp.ide.builder.GraspSourceFile;
import uk.ac.standrews.grasp.ide.builder.NullLogger;
import uk.ac.standrews.grasp.ide.editors.completion.GraspScanner;

public final class GraspModel {
	public static final GraspModel INSTANCE = new GraspModel();
	
	private IResourceChangeListener resourceChangeListener;
	private Map<IFile, GraspScanner> scannedFiles;
	private Map<IFile, ISyntaxTree> parsedFiles;
	private IResourceDeltaVisitor visitor;
	
	private GraspModel() { 
		scannedFiles = new HashMap<IFile, GraspScanner>();
		parsedFiles = new HashMap<IFile, ISyntaxTree>();
		resourceChangeListener = new IResourceChangeListener() {			
			@Override
			public void resourceChanged(IResourceChangeEvent event) {
				Assert.isTrue(event.getType() == IResourceChangeEvent.POST_CHANGE);
				try {
					event.getDelta().accept(visitor);
				} catch (CoreException e) {
					Log.error(e);
				}				
			}
		};
		visitor = new IResourceDeltaVisitor() {			
			@Override
			public boolean visit(IResourceDelta delta) throws CoreException {
				IResource res = delta.getResource();
				if (!(res instanceof IFile)) return true;				
				IFile file = (IFile)res;								
				
				switch (delta.getKind()) {
				case IResourceDelta.ADDED:
				case IResourceDelta.CHANGED:
					IContentDescription desc = file.getContentDescription();
					if (desc == null 
							|| !GraspPlugin.ID_GRASP_CONTENT_TYPE.equals(desc.getContentType().getId())) {
						return true;
					}
					
					GraspScanner scanner = new GraspScanner();
					scanner.parse(new InputStreamReader(file.getContents()));
					GraspModel.INSTANCE.scannedFiles.put(file, scanner);
					Parser parser = new Parser();
					ISource source = new GraspSourceFile(file);
					ISyntaxTree tree = parser.parse(source, NullLogger.INSTANCE);
					GraspModel.INSTANCE.parsedFiles.put(file, tree);
					break;
				case IResourceDelta.REMOVED:
					GraspModel.INSTANCE.scannedFiles.remove(file);	
					GraspModel.INSTANCE.parsedFiles.remove(file);
					break;
				}
				return true;
			}
		};
	}
	
	public GraspScanner getScannerForFile(IFile file) {
		return scannedFiles.get(file);
	}
	
	public void setScannerForFile(IFile file, GraspScanner scanner) {
		scannedFiles.put(file, scanner);		
	}
	
	public static IElement getObservableFor(IElement graspElement) {
		// TODO: implement		
		return graspElement;
	}
	
	public ISyntaxTree getSyntaxTreeForFile(IFile file) {
		return parsedFiles.get(file);
	}
	
	public void setFileSyntaxTree(IFile file, ISyntaxTree tree) {
		parsedFiles.put(file, tree);
	}
	
	public void init() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceChangeListener, IResourceChangeEvent.POST_CHANGE);
	}
	
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceChangeListener);
	}

	
}
