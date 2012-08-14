package uk.ac.standrews.grasp.ide.editParts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.IFigure;

import uk.ac.standrews.grasp.ide.figures.LayerFigure;
import uk.ac.standrews.grasp.ide.model.ElementType;
import uk.ac.standrews.grasp.ide.model.FirstClassModel;
import uk.ac.standrews.grasp.ide.model.LayerModel;
import uk.ac.standrews.grasp.ide.model.SystemModel;

public class LayerEditPart extends AbstractElementNodeEditPart<LayerModel> {
	private final List<LayerOverLayerConnection> sourceOverConnections;
	private final List<LayerOverLayerConnection> targetOverConnections;

	public LayerEditPart(LayerModel model) {
		super(model);	
		sourceOverConnections = new ArrayList<LayerOverLayerConnection>(model.getOver().size());
		for (LayerModel layer: model.getOver()) {
			sourceOverConnections.add(new LayerOverLayerConnection(model, (LayerModel) layer));
		}
		targetOverConnections = new ArrayList<LayerOverLayerConnection>();
		SystemModel system = (SystemModel) model.getArchitecture().getBodyByType(ElementType.SYSTEM).iterator().next();
		for (FirstClassModel fc: system.getBodyByType(ElementType.LAYER)) {
			LayerModel layer = (LayerModel) fc;
			if (!layer.getQualifiedName().equalsIgnoreCase(model.getQualifiedName())) {
				for (LayerModel over: layer.getOver()) {
					if (over.getQualifiedName().equalsIgnoreCase(model.getQualifiedName())) {
						targetOverConnections.add(new LayerOverLayerConnection((LayerModel) layer, model));
					}
				}
			}
		}
	}
	
	@Override
	protected List<LayerOverLayerConnection> getModelSourceConnections() {
		//return sourceOverConnections;
		return Collections.emptyList();
	}
	
	@Override
	protected List<LayerOverLayerConnection> getModelTargetConnections() {
		//return targetOverConnections;
		return Collections.emptyList();
	}
	@Override
	protected void createEditPolicies() {
				
	}

	@Override
	protected boolean isModelChildSupported(FirstClassModel child) {
		return child.getType() == ElementType.COMPONENT 
				|| child.getType() == ElementType.CONNECTOR
				//|| child.getType() == ElementType.LINK
				;
	}

	@Override
	protected IFigure createFigure() {
		return new LayerFigure();
	}	
	
}
