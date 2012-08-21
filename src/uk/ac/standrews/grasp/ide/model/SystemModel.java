package uk.ac.standrews.grasp.ide.model;

/**
 * Represents Grasp system element
 * @author Dilyan Rusev
 *
 */
public class SystemModel extends BecauseModel {
	public static final String PROPERTY_ARCHITECTURE = "Architecture";

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
	
	@Override
	public void elementRefactored(ElementModel element, String operation,
			Object oldValue, Object newName) {		
		super.elementRefactored(element, operation, oldValue, newName);
		if (element.getType() == ElementType.ARCHITECTURE && operation == Refactor.OPERATION_RENAME) {
			fireElementChanged(PROPERTY_ARCHITECTURE);
		}
	}
}
