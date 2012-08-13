package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.draw2d.IFigure;

import uk.ac.standrews.grasp.ide.figures.ConnectorFigure;
import uk.ac.standrews.grasp.ide.model.ConnectorModel;

public class ConnectorEditPart extends
		AbstractInstantiableEditPart<ConnectorModel> {

	public ConnectorEditPart(ConnectorModel model) {
		super(model);		
	}
	
	@Override
	protected IFigure createFigure() {
		return new ConnectorFigure();
	}

}
