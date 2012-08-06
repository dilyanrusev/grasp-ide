package uk.ac.standrews.grasp.ide.model;

public class ConnectorModel extends InstantiableModel {
	public ConnectorModel(ConnectorModel other, FirstClassModel parent) {
		super(other, parent);
	}
	
	public ConnectorModel(FirstClassModel parent) {
		super(ElementType.CONNECTOR, parent);
	}
}
