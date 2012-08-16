package uk.ac.standrews.grasp.ide.model;

/**
 * Represents Grasp property
 * @author Dilyan Rusev
 *
 */
public class PropertyModel extends EvaluableModel {
	
	/**
	 * Construct a copy of another property
	 * @param other Property to copy
	 * @param parent Parent element
	 */
	public PropertyModel(PropertyModel other, FirstClassModel parent) {
		super(other, parent);
	}
	
	/**
	 * Construct a new property
	 * @param parent Parent element
	 */
	public PropertyModel(FirstClassModel parent) {
		super(ElementType.PROPERTY, parent);
	}
}
