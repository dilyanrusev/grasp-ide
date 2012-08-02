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
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('[');
	    sb.append(source.getName()).append(" -> ");
	    sb.append(target.getName());
	    sb.append(']');
	    return sb.toString();
	}
}
