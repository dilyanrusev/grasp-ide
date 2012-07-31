package uk.ac.standrews.grasp.ide.model;

import org.eclipse.core.runtime.Assert;

/**
 * Provides event information for element change listeners
 * @author Dilyan Rusev
 * @see IElementChangedListener
 * @see IObservable
 */
public class ElementChangedEvent {
	private final IObservable source;
	private final String[] propertyNames;
	
	/**
	 * Constructs a new state change event data
	 * @param source Object that changed state. May not be <code>null</code>
	 * @param propertyNames Optional list of the property names that provoked the change
	 */
	public ElementChangedEvent(IObservable source, String... propertyNames) {
		Assert.isNotNull(source);
		this.source = source;
		this.propertyNames = propertyNames;
	}
	
	/**
	 * Retrieve the object that changed state
	 * @return Object that has changed its state
	 */
	public final IObservable getSource() {
		return source;
	}
	
	/**
	 * Retrieve the list of properties that changed the state
	 * @return List of properties that change this object's state
	 */
	public final String[] getPropertyNames() {
		return propertyNames.clone();
	}
}
