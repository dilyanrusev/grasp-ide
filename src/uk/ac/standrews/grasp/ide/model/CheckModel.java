package uk.ac.standrews.grasp.ide.model;

/**
 * Represents Grasp check statements
 * @author Dilyan Rusev
 *
 */
public class CheckModel extends EvaluableModel {
	/**
	 * Construct a copy of another element
	 * @param other Source
	 * @param parent Parent
	 */
	public CheckModel(CheckModel other, FirstClassModel parent) {
		super(other, parent);
	}
	
	/**
	 * Construct a new check
	 * @param parent Parent element
	 */
	public CheckModel(FirstClassModel parent) {
		super(ElementType.CHECK, parent);
	}	
}
