package uk.ac.standrews.grasp.ide.model;

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

import uk.ac.standrews.grasp.ide.GraspPlugin;
import uk.ac.standrews.grasp.ide.Log;
import uk.ac.standrews.grasp.ide.editors.completion.GraspScanner;

public final class GraspModel {
	public static final GraspModel INSTANCE = new GraspModel();
	
	private IResourceChangeListener resourceChangeListener;
	private Map<IFile, GraspScanner> scannedFiles;
	private IResourceDeltaVisitor visitor;
	
	private GraspModel() { 
		scannedFiles = new HashMap<IFile, GraspScanner>();
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
				IContentDescription desc = file.getContentDescription();
				if (desc == null || !GraspPlugin.ID_GRASP_CONTENT_TYPE.equals(desc.getContentType().getId())) return true;				
				
				switch (delta.getKind()) {
				case IResourceDelta.ADDED:
				case IResourceDelta.CHANGED:
					GraspScanner scanner = new GraspScanner();
					scanner.parse(new InputStreamReader(file.getContents()));
					GraspModel.INSTANCE.scannedFiles.put(file, scanner);
					break;
				case IResourceDelta.REMOVED:
					GraspModel.INSTANCE.scannedFiles.remove(file);					
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
	
	public void init() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceChangeListener, IResourceChangeEvent.POST_CHANGE);
	}
	
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceChangeListener);
	}

	
}
