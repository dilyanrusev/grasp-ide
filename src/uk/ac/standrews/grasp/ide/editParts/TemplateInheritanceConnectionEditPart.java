package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.gef.editparts.AbstractConnectionEditPart;

public class TemplateInheritanceConnectionEditPart extends
		AbstractConnectionEditPart {
	
	public TemplateInheritanceConnectionEditPart(TemplateInheritanceConnection model) {
		setModel(model);
	}

	@Override
	protected void createEditPolicies() {

	}

}
