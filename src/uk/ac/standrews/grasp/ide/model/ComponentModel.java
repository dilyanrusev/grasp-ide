package uk.ac.standrews.grasp.ide.model;

/**
 * Represents Grasp components
 * @author Dilyan Rusev
 *
 */
public class ComponentModel extends InstantiableModel {
	
	/**
	 * Construct a component that is a copy of another component
	 * @param other Source
	 * @param parent Parent element
	 */
	public ComponentModel(ComponentModel other, FirstClassModel parent) {
		super(other, parent);
	}
	
	/**
	 * Construct new component
	 * @param parent Parent element
	 */
	public ComponentModel(FirstClassModel parent) {
		super(ElementType.COMPONENT, parent);
	}
}
