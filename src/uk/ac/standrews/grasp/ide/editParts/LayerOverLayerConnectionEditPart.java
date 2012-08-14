package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;

import uk.ac.standrews.grasp.ide.model.ConnectionModel;

public class LayerOverLayerConnectionEditPart extends
		AbstractConnectionEditPart {	
	
	public LayerOverLayerConnectionEditPart(ConnectionModel model) {
		setModel(model);
	}
	
	@Override
	protected void createEditPolicies() {

	}
	
	@Override
	protected IFigure createFigure() {
		PolylineConnection connection = new PolylineConnection();		
		
		PolygonDecoration decoration = new PolygonDecoration();
		decoration.setTemplate(PolygonDecoration.TRIANGLE_TIP);
		
		//String str = getModel().toString();
		Label label = new Label("over");
		label.setOpaque(true);
		
		connection.setLineWidth(2);
		connection.setTargetDecoration(decoration);
		connection.add(label, new MidpointLocator(connection, 0));
		
		return connection;
	}
}
