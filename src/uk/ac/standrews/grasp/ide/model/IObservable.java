package uk.ac.standrews.grasp.ide.model;

/**
 * Provides a contract for observable elements
 * @author Dilyan Rusev
 *
 */
public interface IObservable {
	
	/**
	 * Adds a listener if it is not null. If the listener is already part of the collection,
	 * it will not be added again.
	 * @param listener Object to receive notifications when an object has changed its state
	 */
	void addElementChangedListener(IElementChangedListener listener);
	
	/**
	 * Remove a listener. Unregistered listeners and <code>null</code> are ignored
	 * @param listener Previously registered listener
	 */
	void removeElementChangedListener(IElementChangedListener listener);
	
	/**
	 * Notify all registered listeners that this object has been modified
	 * @param propertyNames Optional list of the properties that changed the object's state
	 */
	void fireElementChanged(String... propertyNames);
}
