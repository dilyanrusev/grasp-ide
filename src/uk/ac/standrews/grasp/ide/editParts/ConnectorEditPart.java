package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.swt.graphics.Image;

import uk.ac.standrews.grasp.ide.model.ConnectorModel;

public class ConnectorEditPart extends
		AbstractInstantiableEditPart<ConnectorModel> {

	public ConnectorEditPart(ConnectorModel model) {
		super(model);		
	}
	
	@Override
	protected Image getIcon() {
		return IconsCache.getDefault().getConnectorIcon();
	}

}
