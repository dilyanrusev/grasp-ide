package uk.ac.standrews.grasp.ide.editors;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.EventObject;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.actions.PrintAction;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.gef.ui.actions.SaveAction;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;

import uk.ac.standrews.grasp.ide.Log;
import uk.ac.standrews.grasp.ide.editParts.GraspEditPartFactory;
import uk.ac.standrews.grasp.ide.model.ArchitectureModel;
import uk.ac.standrews.grasp.ide.model.ElementType;
import uk.ac.standrews.grasp.ide.model.GraspFile;
import uk.ac.standrews.grasp.ide.model.GraspFileChangedEvent;
import uk.ac.standrews.grasp.ide.model.GraspModel;
import uk.ac.standrews.grasp.ide.model.IGraspFileChangedListener;
import uk.ac.standrews.grasp.ide.model.ModelToSourceSerializer;

public class GraspDesigner extends GraphicalEditorWithFlyoutPalette 
		implements IGraspFileChangedListener {	
	private ArchitectureModel model;
	
	public GraspDesigner() {
		setEditDomain(new DefaultEditDomain(this));		
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);	
		GraspFile.addChangeListener(this);		
	}
	
	@Override
	public void commandStackChanged(EventObject event) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(event);
	}
	
	@Override
	protected PaletteViewerProvider createPaletteViewerProvider() {
		return new PaletteViewerProvider(getEditDomain()) {
			protected void configurePaletteViewer(PaletteViewer viewer) {
				super.configurePaletteViewer(viewer);
				// create a drag source listener for this palette viewer
				// together with an appropriate transfer drop target listener,
				// this will enable
				// model element creation by dragging a
				// CombinatedTemplateCreationEntries
				// from the palette into the editor
				// @see ShapesEditor#createTransferDropTargetListener()
				viewer.addDragSourceListener(new TemplateTransferDragSourceListener(
						viewer));
			}
		};
	}

	private ArchitectureModel getModel() {
		if (model == null) {
			IFileEditorInput fileInput = (IFileEditorInput) getEditorInput();
			model = GraspModel.INSTANCE.ensureFileStats(fileInput.getFile()).getArchitecture();
		}
		return model;
	}
	
	@Override
	public void dispose() {
		GraspFile.removeChangeListener(this);		
		super.dispose();
	}

	@Override
	protected PaletteRoot getPaletteRoot() {
		return DesignerPalette.getDefault();
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		IFile file = ((IFileEditorInput) getEditorInput()).getFile();
		try {
			String charsetName = file.getCharset(true);
			ModelToSourceSerializer serializer = new ModelToSourceSerializer();
			InputStream data = serializer.serializeToStream(getModel(), Charset.forName(charsetName));
			file.setContents(data, true, true, monitor);
			getCommandStack().markSaveLocation();
		} catch (CoreException e) {
			Log.error("Cannot save graphical designer", e);
		}
	}

	@Override
	protected void configureGraphicalViewer() {		
		super.configureGraphicalViewer();
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new GraspEditPartFactory());
		viewer.setRootEditPart(new ScalableFreeformRootEditPart());		
	}
	
	@Override
	protected void initializeGraphicalViewer() {		
		super.initializeGraphicalViewer();		
		getGraphicalViewer().setContents(getModel());		
		getGraphicalViewer().addDropTargetListener(
				new TemplateTransferDropTargetListener(getGraphicalViewer()) {
					protected CreationFactory getFactory(Object template) {
						return new DesignerPalette.Factory((ElementType) template);
					};
				}
		);
		//getEditDomain().addViewer(getGraphicalViewer());
	}	
	
	@Override
	public void fileChanged(GraspFileChangedEvent event) {
		if (event.getKind().contains(GraspFileChangedEvent.Kind.ArchiectureRefreshed)
				&& event.getSource().getFile().equals(((IFileEditorInput) getEditorInput()).getFile())) {		
			model = event.getSource().getArchitecture();			
			bindViewer(model);
		}
	}
	
	private void bindViewer(final ArchitectureModel theModel) {		
		Display.getDefault().asyncExec(new Runnable() {			
			@Override
			public void run() {
				getGraphicalViewer().setContents(theModel);				
			}
		});
	}

	@Override
	protected void createActions() {		
		ActionRegistry registry = getActionRegistry();
		IAction action;

		action = new UndoAction(this);
		registry.registerAction(action);
		getStackActions().add(action.getId());

		action = new RedoAction(this);
		registry.registerAction(action);
		getStackActions().add(action.getId());	

		action = new DeleteAction((IWorkbenchPart) this);		
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new SaveAction(this);
		registry.registerAction(action);
		getPropertyActions().add(action.getId());

		registry.registerAction(new PrintAction(this));	
	}
	
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {		
		IEditorPart activeEditor = getSite().getPage().getActiveEditor();
		if (activeEditor == this ||
				(activeEditor instanceof GraspEditor
						&& ((GraspEditor) activeEditor).getActiveEditor() == this)) {
			updateActions(getSelectionActions());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected List<String> getStackActions() {		
		return super.getStackActions();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected List<String> getSelectionActions() {		
		return super.getSelectionActions();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected List<String> getPropertyActions() {		
		return super.getPropertyActions();
	}	
	
	/**
	 * Invoke GEF's delete action
	 */
	public void deleteSelection() {
		System.out.println(getGraphicalViewer().getSelection());		
		IAction ac = getActionRegistry().getAction(ActionFactory.DELETE.getId());
		ac.run();
	}
	
	/**
	 * Invoke GEF's undo action
	 */
	public void performUndo() {
		getActionRegistry().getAction(ActionFactory.UNDO.getId()).run();
	}
	
	/**
	 * Invoke GEF's redo action
	 */
	public void performRedo() {
		getActionRegistry().getAction(ActionFactory.REDO.getId()).run();
	}
}
