package uk.ac.standrews.grasp.ide.views;


import org.eclipse.core.resources.IFile;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;

import uk.ac.standrews.grasp.ide.editParts.ArchitectureEditPart;
import uk.ac.standrews.grasp.ide.editParts.GraspEditPartFactory;
import uk.ac.standrews.grasp.ide.model.ArchitectureModel;
import uk.ac.standrews.grasp.ide.model.GraspFile;
import uk.ac.standrews.grasp.ide.model.GraspFileChangedEvent;
import uk.ac.standrews.grasp.ide.model.GraspFileChangedEvent.Kind;
import uk.ac.standrews.grasp.ide.model.IGraspFileChangedListener;

public class GefView 
	extends ViewPart 
	implements IActiveEditorChangedListener, IGraspFileChangedListener {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "uk.ac.standrews.grasp.ide.views.GefView";
	
	private ScrollingGraphicalViewer graphicalViewer;
	private ActiveEditorChangeWatcher activeEditorChangeWatcher;
	private IFile lastActiveFile;

	public GefView() {		
	}
	
	@Override
	public void createPartControl(Composite parent) {		
		ScalableFreeformRootEditPart rootEditPart = new ScalableFreeformRootEditPart();
		graphicalViewer = new ScrollingGraphicalViewer();
		graphicalViewer.createControl(parent);
		graphicalViewer.setRootEditPart(rootEditPart);
		graphicalViewer.setEditPartFactory(new GraspEditPartFactory());
		
		activeEditorChangeWatcher = new ActiveEditorChangeWatcher();
		activeEditorChangeWatcher.addEditorVisibilityListener(this);
		activeEditorChangeWatcher.start(getSite().getWorkbenchWindow().getWorkbench());
		
		GraspFile.addChangeListener(this);
	}
	
	@Override
	public void dispose() {
		GraspFile.removeChangeListener(this);
		activeEditorChangeWatcher.dispose();
		super.dispose();
	}

	@Override
	public void setFocus() {
		graphicalViewer.getControl().setFocus();
	}

	@Override
	public void activeEditorChanged(ActiveEditorChangedEvent event) {
		IFile currentFile = getCurrentFile();
		IFile eventFile = event.getEditorArchitecture().getFile();
		if (currentFile == null || !currentFile.equals(eventFile) || !currentFile.equals(lastActiveFile)) {
			System.out.println("Active architecture: " + event.getEditorArchitecture());
			bindView(event.getEditorArchitecture());
			lastActiveFile = currentFile;
		}
	}

	@Override
	public void fileChanged(GraspFileChangedEvent event) {
		if (event.getKind().contains(Kind.ArchiectureRefreshed)) {
			if (event.getSource().getFile().equals(getCurrentFile())) {
				bindView(event.getSource().getArchitecture());
				System.out.println("Refreshed from " + getCurrentFile());
			}
		}
	}
	
	private void bindView(final ArchitectureModel model) {
		if (Display.getCurrent() != null) {
			graphicalViewer.setContents(model);
		} else {			
			Display.getDefault().asyncExec(new Runnable() {				
				@Override
				public void run() {
					graphicalViewer.setContents(model);				
				}
			});
		}
	}
	
	private IFile getCurrentFile() {
		if (graphicalViewer != null && graphicalViewer.getContents() instanceof ArchitectureEditPart) {
			IFile currentFile = ((ArchitectureModel) graphicalViewer.getContents().getModel()).getFile();
			return currentFile;
		}
		return null;
	}
}