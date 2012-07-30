package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.gef.editparts.AbstractConnectionEditPart;

import uk.ac.standrews.grasp.ide.model.LinkModel;

public class LinkConnectionEditPart extends AbstractConnectionEditPart {
	
	public LinkConnectionEditPart(LinkModel model) {
		setModel(model);
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub

	}

}
