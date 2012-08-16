package uk.ac.standrews.grasp.ide.model;

/**
 * Represents Grasp system element
 * @author Dilyan Rusev
 *
 */
public class SystemModel extends BecauseModel {
	
	/**
	 * Construct a copy of another system element
	 * @param other System element to copy
	 * @param parent Parent element
	 */
	public SystemModel(SystemModel other, ArchitectureModel parent) {
		super(other, parent);
	}
	
	/**
	 * Create a new system element
	 * @param parent Parent element
	 */
	public SystemModel(ArchitectureModel parent) {
		super(ElementType.SYSTEM, parent);
	}
	
}
