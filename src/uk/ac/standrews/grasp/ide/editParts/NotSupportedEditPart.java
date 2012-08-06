package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.swt.graphics.Image;

import uk.ac.standrews.grasp.ide.model.FirstClassModel;

public class NotSupportedEditPart extends AbstractElementNodeEditPart<FirstClassModel> {

	public NotSupportedEditPart(FirstClassModel model) {
		super(model);		
	}

	@Override
	protected Image getIcon() {		
		return null;
	}

	@Override
	protected void createEditPolicies() {	
		
	}

	@Override
	protected boolean isModelChildSupported(FirstClassModel child) {
		return false;
	}

}
