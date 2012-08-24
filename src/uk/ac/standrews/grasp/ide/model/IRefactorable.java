package uk.ac.standrews.grasp.ide.model;


/**
 * Provides notifications to elements when a refactoring operation was performed
 * @author Dilyan Rusev
 *
 */
public interface IRefactorable {
	/**
	 * Notifies this item an item (possibly itself) has been refactored. See OPERATION_xx constants in
	 * {@link Refactor}
	 * @param operation Defines the kind of operation performed on the element
	 * @param element Element that has been renamed. Do not change it.
	 * @param oldValue Previous value
	 * @param newValue New value	 
	 */
	void elementRefactored(ElementModel element, String operation, Object oldValue, Object newValue);
}
