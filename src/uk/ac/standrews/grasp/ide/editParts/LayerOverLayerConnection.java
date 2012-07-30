package uk.ac.standrews.grasp.ide.editParts;

import uk.ac.standrews.grasp.ide.model.LayerModel;

public class LayerOverLayerConnection {
	private LayerModel source;
	private LayerModel target;
	
	public LayerOverLayerConnection(LayerModel source, LayerModel target) {
		this.source = source;
		this.target = target;
	}
	
	public LayerModel getSource() {
		return source;
	}
	
	public LayerModel getTarget() {
		return target;
	}
}
