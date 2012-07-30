package uk.ac.standrews.grasp.ide.editParts;

import grasp.lang.IElement.ElementType;
import grasp.lang.IFirstClass;
import grasp.lang.ILayer;
import grasp.lang.ISystem;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;

import uk.ac.standrews.grasp.ide.model.LayerModel;

public class LayerEditPart extends AbstractElementEditPart<LayerModel> {
	private final List<LayerOverLayerConnection> sourceOverConnections;
	private final List<LayerOverLayerConnection> targetOverConnections;

	public LayerEditPart(LayerModel model) {
		super(model);	
		sourceOverConnections = new ArrayList<LayerOverLayerConnection>(model.getOver().size());
		for (ILayer layer: model.getOver()) {
			sourceOverConnections.add(new LayerOverLayerConnection(model, (LayerModel) layer));
		}
		targetOverConnections = new ArrayList<LayerOverLayerConnection>();
		ISystem system = (ISystem) model.getArchitecture().getBodyByType(ElementType.SYSTEM);
		for (IFirstClass fc: system.getBodyByType(ElementType.LAYER)) {
			ILayer layer = (ILayer) fc;
			if (!layer.getQualifiedName().equalsIgnoreCase(model.getQualifiedName())) {
				for (ILayer over: layer.getOver()) {
					if (over.getQualifiedName().equalsIgnoreCase(model.getQualifiedName())) {
						targetOverConnections.add(new LayerOverLayerConnection((LayerModel) layer, model));
					}
				}
			}
		}
	}
	
	@Override
	protected List<LayerOverLayerConnection> getModelSourceConnections() {
		return sourceOverConnections;
	}
	
	@Override
	protected List<LayerOverLayerConnection> getModelTargetConnections() {
		return targetOverConnections;
	}
	
	@Override
	public void activate() {
		super.activate();
		
	}

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		
	}
}