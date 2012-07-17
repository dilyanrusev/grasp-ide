package uk.ac.standrews.grasp.ide.model;

/**
 * Provides a callback for when a <code>IObservable</code> changes its state
 * @author Dilyan Rusev
 *
 */
public interface IElementChangedListener {
	/**
	 * Callback that gets notified when the state of an observable object changes
	 * @param event Contains information about the object that changed state
	 */
	void elementChanged(ElementChangedEvent event);
}
