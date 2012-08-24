package uk.ac.standrews.grasp.ide.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.runtime.Assert;

/**
 * Contains information about the changes performed to a collection
 * @author Dilyan Rusev
 *
 * @param <E> Type of the observed element
 */
public class CollectionChangedEvent<E> {
	private final IObservableCollection<E> collection;
	private final Collection<E> added;
	private final Collection<E> removed;
	
	/**
	 * Construct new event data
	 * @param collection Source of the event. Cannot be null.
	 * @param added Elements that were added. Cannot be null.
	 * @param removed Elements that were removed. Cannot be null.
	 */
	public CollectionChangedEvent(IObservableCollection<E> collection,
			Collection<E> added, Collection<E> removed) {
		Assert.isNotNull(collection);
		Assert.isNotNull(added);
		Assert.isNotNull(removed);
		this.collection = collection;
		this.added = added;
		this.removed = removed;
	}
	
	/**
	 * Helper method for constructing an event for the case where a single item was added to a collection
	 * @param collection Source of the event
	 * @param item Item that was added
	 * @return Event that represents the case where one item was added to a collection
	 */
	public static <E> CollectionChangedEvent<E> forItemAdded(
			IObservableCollection<E> collection, E item) {
		Collection<E> added = new ArrayList<E>();
		added.add(item);
		Collection<E> removed = Collections.emptyList();
		return new CollectionChangedEvent<E>(collection, added, removed);
	}
	
	/**
	 * Helper method for constructing an event for the case where a single item was removed from a collection
	 * @param collection Source of the event
	 * @param item Item that was removed
	 * @return Event that represents the case where a single item was removed from a collection
	 */
	public static <E> CollectionChangedEvent<E> forItemRemoved(
			IObservableCollection<E> collection, E item) {
		Collection<E> removed = new ArrayList<E>();
		removed.add(item);
		Collection<E> added = Collections.emptyList();
		return new CollectionChangedEvent<E>(collection, added, removed);
	}
	
	/**
	 * Helper method for constructing an event for the case where a set of items were added to a collection
	 * @param collection Source of the event
	 * @param added Items that were added
	 * @return Event containing the items that were added
	 */
	public static <E> CollectionChangedEvent<E> forItemsAdded(
			IObservableCollection<E> collection, Collection<E> added) {
		Collection<E> removed = Collections.emptyList();
		return new CollectionChangedEvent<E>(collection, added, removed);
	}
	
	/**
	 * Helper method for constructing an event for the case where a set of items were removed from a collection
	 * @param collection Source of the event
	 * @param removed Items that were removed
	 * @return Event containing the items that were removed
	 */
	public static <E> CollectionChangedEvent<E> forItemsRemoved(
			IObservableCollection<E> collection, Collection<E> removed) {
		Collection<E> added = Collections.emptyList();
		return new CollectionChangedEvent<E>(collection, added, removed);
	}
	
	/**
	 * Helper method for constructing an event for the case where one item was removed and another inserted in its place
	 * @param collection Source of the event
	 * @param oldItem Item that was removed. Can be null.
	 * @param newItem Item that was added. Can be null.
	 * @return Event
	 */
	public static <E> CollectionChangedEvent<E> forReplace(
			IObservableCollection<E> collection, E oldItem, E newItem) {
		Collection<E> added = new ArrayList<E>();
		if (newItem != null) {
			added.add(newItem);
		}
		Collection<E> removed = new ArrayList<E>();
		if (oldItem != null) {
			removed.add(oldItem);
		}
		return new CollectionChangedEvent<E>(collection, added, removed);
	}
	
	/**
	 * Get the elements that were added to the source collection
	 * @return Added items
	 */
	public Collection<E> getAdded() {
		return added;
	}
	
	/**
	 * Get the elements that were removed from the source collection
	 * @return Removed items
	 */
	public Collection<E> getRemoved() {
		return removed;
	}
	
	/**
	 * Get the collection that was modified
	 * @return Modified collection
	 */
	public IObservableCollection<E> getSource() {
		return collection;
	}
}
