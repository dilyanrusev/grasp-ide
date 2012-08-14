package uk.ac.standrews.grasp.ide.editParts;

import uk.ac.standrews.grasp.ide.figures.IFirstClassFigure;
import uk.ac.standrews.grasp.ide.model.FirstClassModel;

public abstract class AbstractElementNodeEditPart<TModel extends FirstClassModel> 
		extends AbstractElementEditPart<TModel> {
	
	public AbstractElementNodeEditPart(TModel model) {
		super(model);
	}	
	
	@Override
	protected void refreshVisuals() {		
		super.refreshVisuals();
		if (getFigure() instanceof IFirstClassFigure) {
			IFirstClassFigure figure = (IFirstClassFigure) getFigure();
			figure.setHeaderText(getDisplayName(getElement()));
			figure.setTooltipText(getElement().getClass().getSimpleName());
		}
	}
}
