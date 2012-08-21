package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.draw2d.IFigure;

import uk.ac.standrews.grasp.ide.figures.ProvidesFigure;
import uk.ac.standrews.grasp.ide.model.FirstClassModel;
import uk.ac.standrews.grasp.ide.model.ProvidesModel;

/**
 * Edit part for the provides interface in components and connectors
 * @author Dilyan Rusev
 *
 */
public class ProvidesEditPart extends AbstractElementNodeEditPart<ProvidesModel> {

	/**
	 * Construct a new edit part and bind it to a model
	 * @param model Model to bind to
	 */
	public ProvidesEditPart(ProvidesModel model) {
		super(model);		
	}

	@Override
	protected boolean isModelChildSupported(FirstClassModel child) {		
		return false;
	}

	@Override
	protected IFigure createFigure() {
		return new ProvidesFigure();
	}
}
