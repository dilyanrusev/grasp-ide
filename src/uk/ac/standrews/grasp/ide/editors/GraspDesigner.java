package uk.ac.standrews.grasp.ide.editors;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.EventObject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;

import uk.ac.standrews.grasp.ide.Log;
import uk.ac.standrews.grasp.ide.editParts.GraspEditPartFactory;
import uk.ac.standrews.grasp.ide.model.ArchitectureModel;
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
		getSite().setSelectionProvider(getGraphicalViewer());
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
			@Override
			protected void configurePaletteViewer(PaletteViewer viewer) {				
				super.configurePaletteViewer(viewer);
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
				new TemplateTransferDropTargetListener(getGraphicalViewer()));
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

	/**
	 * Invoke GEF's delete action
	 */
	public void deleteSelection() {
		getActionRegistry().getAction(ActionFactory.DELETE.getId()).run();	
	}
	
}
