package uk.ac.standrews.grasp.ide.views;


import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.ViewPart;

import uk.ac.standrews.grasp.ide.editParts.GraspEditPartFactory;

public class GefView extends ViewPart implements IActiveEditorChangedListener {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "uk.ac.standrews.grasp.ide.views.GefView";
	
	private ScrollingGraphicalViewer graphicalViewer;
	private ActiveEditorChangeWatcher activeEditorChangeWatcher;

	public GefView() {		
	}
	
	@Override
	public void createPartControl(Composite parent) {		
		ScalableFreeformRootEditPart rootEditPart = new ScalableFreeformRootEditPart();
		graphicalViewer = new ScrollingGraphicalViewer();
		graphicalViewer.createControl(parent);
		graphicalViewer.setRootEditPart(rootEditPart);
		graphicalViewer.setEditPartFactory(new GraspEditPartFactory());
		graphicalViewer.getControl().setVisible(false);
		
	
		IEditorPart activeEditor = getSite().getWorkbenchWindow().getActivePage().getActiveEditor();
		if (activeEditor != null) {
			activeEditorChanged(new ActiveEditorChangedEvent(activeEditor));
		}
		activeEditorChangeWatcher = new ActiveEditorChangeWatcher(getSite().getWorkbenchWindow().getWorkbench());
	}
	
	@Override
	public void dispose() {
		activeEditorChangeWatcher.dispose();
		super.dispose();
	}

	@Override
	public void setFocus() {
		graphicalViewer.getControl().setFocus();
	}

	@Override
	public void activeEditorChanged(ActiveEditorChangedEvent event) {		
		graphicalViewer.getControl().setVisible(true);
		graphicalViewer.setContents(event.getEditorArchitecture());		
	}
}