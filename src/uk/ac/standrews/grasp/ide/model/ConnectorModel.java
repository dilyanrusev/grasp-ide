package uk.ac.standrews.grasp.ide.model;

import grasp.lang.IConnector;
import grasp.lang.IFirstClass;

public class ConnectorModel extends InstantiableModel implements IConnector {
	public ConnectorModel(IConnector other, IFirstClass parent) {
		super(other, parent);
	}
	
	public ConnectorModel(IFirstClass parent) {
		super(ElementType.CONNECTOR, parent);
	}
}
