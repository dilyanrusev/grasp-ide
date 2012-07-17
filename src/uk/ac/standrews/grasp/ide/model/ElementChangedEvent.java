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
	private final String propertyName;
	
	/**
	 * Constructs a new state change event data
	 * @param source Object that changed state. May not be <code>null</code>
	 * @param propertyName Optional name of the property that provoked the change
	 */
	public ElementChangedEvent(IObservable source, String propertyName) {
		Assert.isNotNull(source);
		this.source = source;
		this.propertyName = propertyName;
	}
	
	/**
	 * Retrieve the object that changed state
	 * @return Object that has changed its state
	 */
	public final IObservable getSource() {
		return source;
	}
	
	/**
	 * Retrieve the name of the property that changed the state of the object
	 * @return Name of a property
	 */
	public final String getPropertyName() {
		return propertyName;
	}
}
