package uk.ac.standrews.grasp.ide.model;

/**
 * Contract for classes that want to listen to collection changes
 * @author Dilyan Rusev
 *
 * @param <E> Type of the collection element
 */
public interface ICollectionChangedListener<E> {
	/**
	 * Notification that a change to the observable collection was made
	 * @param event Information about what has been changed
	 */
	void collectionChanged(CollectionChangedEvent<E> event);
}
