package uk.ac.standrews.grasp.ide.editParts;

import uk.ac.standrews.grasp.ide.model.ElementType;
import uk.ac.standrews.grasp.ide.model.FirstClassModel;
import uk.ac.standrews.grasp.ide.model.InstantiableModel;

public abstract class AbstractInstantiableEditPart<TModel extends InstantiableModel> extends
		AbstractElementNodeEditPart<TModel> {

	public AbstractInstantiableEditPart(TModel model) {
		super(model);		
	}	
	
	@Override
	protected boolean isModelChildSupported(FirstClassModel child) {
		return child.getType() == ElementType.PROVIDES || child.getType() == ElementType.REQUIRES;
	}
}
