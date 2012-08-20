package uk.ac.standrews.grasp.ide.editParts;

import java.util.Iterator;

import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import uk.ac.standrews.grasp.ide.figures.IFirstClassFigure;
import uk.ac.standrews.grasp.ide.model.FirstClassModel;

/**
 * Base class for node edit parts
 * @author Dilyan Rusev
 *
 * @param <TModel> Type of Grasp element this node is drawing
 */
public abstract class AbstractElementNodeEditPart<TModel extends FirstClassModel> 
		extends AbstractElementEditPart<TModel> {
	private ISelectionChangedListener selectionListener;	
	private boolean selected;
	
	/**
	 * Construct a new part and set its model
	 * @param model Part model
	 */
	public AbstractElementNodeEditPart(TModel model) {
		super(model);
	}	
	
	@Override
	public void activate() {		
		super.activate();
		selected = getViewer().getSelectedEditParts().contains(this);
		selectionListener = new ISelectionChangedListener() {			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection sel = (IStructuredSelection) event.getSelection();				
				boolean wasSelected = selected;
				selected = sel.getFirstElement() == AbstractElementNodeEditPart.this;
				if (wasSelected != selected) {
					selectedChanged(selected);
				}
			}
		};
		getViewer().addSelectionChangedListener(selectionListener);
	}
	
	@Override
	public void deactivate() {
		getViewer().removeSelectionChangedListener(selectionListener);
		selectionListener = null;
		super.deactivate();
	}
	
	protected void selectedChanged(boolean isSelected) {
		refreshVisuals();
	}
	
	protected boolean isSelected() {
		return selected;
	}
	
	@Override
	protected void refreshVisuals() {		
		super.refreshVisuals();
		if (getFigure() instanceof IFirstClassFigure) {						
			IFirstClassFigure figure = (IFirstClassFigure) getFigure();
			figure.setHeaderText(getHeaderText());
			figure.setTooltipText(getTooltipText());	
			figure.setSelected(isSelected());
		}
	}
	
	/**
	 * Invoked in {@link #refreshVisuals()} to set the figure's header text
	 * @return
	 */
	protected String getHeaderText() {
		return getDisplayName(getElement());
	}
	
	/**
	 * Invoked in {@link #refreshVisuals()} to set the figure's tooltip text
	 * @return
	 */
	protected String getTooltipText() {
		return getElement().getClass().getSimpleName();
	}
	
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new GraspComponentPolicy());		
	}
}
