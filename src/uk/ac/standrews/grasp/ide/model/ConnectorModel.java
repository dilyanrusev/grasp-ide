package uk.ac.standrews.grasp.ide.model;

/**
 * Represents Grasp connectors
 * @author Dilyan Rusev
 *
 */
public class ConnectorModel extends InstantiableModel {
	/**
	 * Construct a connector that is a copy of another connector
	 * @param other Source
	 * @param parent Parent elemnet
	 */
	public ConnectorModel(ConnectorModel other, FirstClassModel parent) {
		super(other, parent);
	}
	
	/**
	 * Construct new connector
	 * @param parent Parent element
	 */
	public ConnectorModel(FirstClassModel parent) {
		super(ElementType.CONNECTOR, parent);
	}
}
