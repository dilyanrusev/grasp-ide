package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.gef.EditPolicy;

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
	
	/**
	 * Construct a new part and set its model
	 * @param model Part model
	 */
	public AbstractElementNodeEditPart(TModel model) {
		super(model);
	}	
	
	@Override
	protected void refreshVisuals() {		
		super.refreshVisuals();
		if (getFigure() instanceof IFirstClassFigure) {
			IFirstClassFigure figure = (IFirstClassFigure) getFigure();
			figure.setHeaderText(getHeaderText());
			figure.setTooltipText(getTooltipText());
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
