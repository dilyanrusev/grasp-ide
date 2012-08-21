package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.draw2d.IFigure;

import uk.ac.standrews.grasp.ide.figures.RequiresFigure;
import uk.ac.standrews.grasp.ide.model.FirstClassModel;
import uk.ac.standrews.grasp.ide.model.RequiresModel;

/**
 * Edit part for the requires interface of a component/connector
 * @author Dilyan Rusev
 *
 */
public class RequiresEditPart extends AbstractElementNodeEditPart<RequiresModel> {

	/**
	 * Construct a new edit part and bind it to a model
	 * @param model Model to bind to
	 */
	public RequiresEditPart(RequiresModel model) {
		super(model);		
	}

	@Override
	protected boolean isModelChildSupported(FirstClassModel child) {		
		return false;
	}

	@Override
	protected IFigure createFigure() {
		return new RequiresFigure();
	}

}
