package uk.ac.standrews.grasp.ide.views;


import org.eclipse.core.resources.IFile;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import uk.ac.standrews.grasp.ide.editParts.ArchitectureEditPart;
import uk.ac.standrews.grasp.ide.editParts.GraspEditPartFactory;
import uk.ac.standrews.grasp.ide.model.ArchitectureModel;
import uk.ac.standrews.grasp.ide.model.GraspFile;
import uk.ac.standrews.grasp.ide.model.GraspFileChangedEvent;
import uk.ac.standrews.grasp.ide.model.GraspFileChangedEvent.Kind;
import uk.ac.standrews.grasp.ide.model.GraspModel;
import uk.ac.standrews.grasp.ide.model.IGraspFileChangedListener;

/**
 * Architecture preview.
 * 
 * Displays a read-only model for the Grasp architecture. Listens for changes to the currently selected editor
 * and to the current selection provider.
 * @author Dilyan Rusev
 *
 */
public class GefView 
	extends ViewPart 
	implements IActiveEditorChangedListener 
	, IGraspFileChangedListener
	, ISelectionListener {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "uk.ac.standrews.grasp.ide.views.GefView";
	
	private ScrollingGraphicalViewer graphicalViewer;
	private ActiveEditorChangeWatcher activeEditorChangeWatcher;
	private IFile lastActiveFile;

	/**
	 * Construct the Architecture Preview
	 */
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
		getSite().getWorkbenchWindow().getSelectionService()
			.addSelectionListener(IPageLayout.ID_PROJECT_EXPLORER, this);
	}
	
	@Override
	public void dispose() {
		activeEditorChangeWatcher.dispose();
		GraspFile.removeChangeListener(this);
		getSite().getWorkbenchWindow().getSelectionService()
			.removeSelectionListener(IPageLayout.ID_PROJECT_EXPLORER, this);		
		super.dispose();
	}

	@Override
	public void setFocus() {
		graphicalViewer.getControl().setFocus();
	}

	@Override
	public void activeEditorChanged(ActiveEditorChangedEvent event) {
		IFile currentFile = getCurrentFile();
		IFile eventFile =
				event.getEditorArchitecture() != null
				? event.getEditorArchitecture().getFile()
				: null;
		if (currentFile == null || !currentFile.equals(eventFile) || !currentFile.equals(lastActiveFile)) {			
			bindView(event.getEditorArchitecture());
			lastActiveFile = currentFile;
		}
	}

	@Override
	public void fileChanged(GraspFileChangedEvent event) {
		if (event.getKind().contains(Kind.ArchiectureRefreshed)) {
			if (event.getSource().getFile().equals(getCurrentFile())) {
				bindView(event.getSource().getArchitecture());			
			}
		}
	}
	
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (!(selection instanceof ITreeSelection)) return;
		ITreeSelection theSelection = (ITreeSelection) selection;
		if (theSelection.isEmpty()) return;
		Object selected = theSelection.getFirstElement();
		if (selected instanceof IFile) {
			IFile selectedFile = (IFile) selected;
			ArchitectureModel arch = GraspModel.INSTANCE.ensureFileStats(selectedFile).getArchitecture();
			if (arch != null) {
				bindView(arch);
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