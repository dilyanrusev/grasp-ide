package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.gef.editparts.AbstractConnectionEditPart;

public class LayerOverLayerConnectionEditPart extends
		AbstractConnectionEditPart {
	
	public LayerOverLayerConnectionEditPart(LayerOverLayerConnection model) {
		setModel(model);
	}

	@Override
	protected void createEditPolicies() {

	}
}
