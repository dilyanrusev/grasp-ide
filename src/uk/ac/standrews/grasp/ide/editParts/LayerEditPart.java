package uk.ac.standrews.grasp.ide.editParts;

import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;

import uk.ac.standrews.grasp.ide.figures.LayerFigure;
import uk.ac.standrews.grasp.ide.model.ConnectionModel;
import uk.ac.standrews.grasp.ide.model.ElementType;
import uk.ac.standrews.grasp.ide.model.FirstClassModel;
import uk.ac.standrews.grasp.ide.model.LayerModel;

public class LayerEditPart extends AbstractElementNodeEditPart<LayerModel> 
	implements NodeEditPart {	

	public LayerEditPart(LayerModel model) {
		super(model);				
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

	@Override
	protected List<ConnectionModel> getModelSourceConnections() {
		return getElement().getSourceConnections();		
	}
	
	@Override
	protected List<ConnectionModel> getModelTargetConnections() {
		return getElement().getTargetConnections();	
	}
	
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connection) {
		return new ChopboxAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connection) {
		return new ChopboxAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return new ChopboxAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return new ChopboxAnchor(getFigure());
	}	
	
}
