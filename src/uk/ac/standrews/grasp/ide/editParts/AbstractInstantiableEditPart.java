package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.swt.graphics.Image;

import uk.ac.standrews.grasp.ide.figures.IInstantiableFigure;
import uk.ac.standrews.grasp.ide.model.ElementType;
import uk.ac.standrews.grasp.ide.model.FirstClassModel;
import uk.ac.standrews.grasp.ide.model.InstantiableModel;
import uk.ac.standrews.grasp.ide.model.ProvidesModel;
import uk.ac.standrews.grasp.ide.model.RequiresModel;

public abstract class AbstractInstantiableEditPart<TModel extends InstantiableModel> extends
		AbstractElementNodeEditPart<TModel> {

	public AbstractInstantiableEditPart(TModel model) {
		super(model);		
	}

	@Override
	protected void createEditPolicies() {
		
	}
	
	@Override
	protected void refreshVisuals() {		
		IInstantiableFigure figure = (IInstantiableFigure) getFigure();
		figure.setHeaderText(getDisplayName(getElement()));
		figure.clearBody();
		for (FirstClassModel child: getElement().getBodyByType(ElementType.PROVIDES)) {
			ProvidesModel provides = (ProvidesModel) child;
			figure.addProvides(getDisplayName(provides));
		}
		for (FirstClassModel child: getElement().getBodyByType(ElementType.REQUIRES)) {
			RequiresModel requires = (RequiresModel) child;
			figure.addRequires(getDisplayName(requires));
		}
	}
	
	@Override
	protected boolean isModelChildSupported(FirstClassModel child) {
		return false;
	}
	
	@Override
	protected Image getIcon() {	
		return null;
	}
}
