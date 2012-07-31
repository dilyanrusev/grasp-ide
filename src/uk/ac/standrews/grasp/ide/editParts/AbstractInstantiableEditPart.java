package uk.ac.standrews.grasp.ide.editParts;

import uk.ac.standrews.grasp.ide.model.InstantiableModel;

public abstract class AbstractInstantiableEditPart<TModel extends InstantiableModel> extends
		AbstractElementNodeEditPart<TModel> {

	public AbstractInstantiableEditPart(TModel model) {
		super(model);		
	}

	@Override
	protected void createEditPolicies() {
		
	}

}
