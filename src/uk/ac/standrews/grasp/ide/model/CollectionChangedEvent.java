package uk.ac.standrews.grasp.ide.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.runtime.Assert;

public class CollectionChangedEvent<E> {
	private final IObservableCollection<E> collection;
	private final Collection<E> added;
	private final Collection<E> removed;
	
	public CollectionChangedEvent(IObservableCollection<E> collection,
			Collection<E> added, Collection<E> removed) {
		Assert.isNotNull(collection);
		Assert.isNotNull(added);
		Assert.isNotNull(removed);
		this.collection = collection;
		this.added = added;
		this.removed = removed;
	}
	
	public static <E> CollectionChangedEvent<E> forItemAdded(
			IObservableCollection<E> collection, E item) {
		Collection<E> added = new ArrayList<E>();
		added.add(item);
		Collection<E> removed = Collections.emptyList();
		return new CollectionChangedEvent<E>(collection, added, removed);
	}
	
	public static <E> CollectionChangedEvent<E> forItemRemoved(
			IObservableCollection<E> collection, E item) {
		Collection<E> removed = new ArrayList<E>();
		removed.add(item);
		Collection<E> added = Collections.emptyList();
		return new CollectionChangedEvent<E>(collection, added, removed);
	}
	
	public static <E> CollectionChangedEvent<E> forItemsAdded(
			IObservableCollection<E> collection, Collection<E> added) {
		Collection<E> removed = Collections.emptyList();
		return new CollectionChangedEvent<E>(collection, added, removed);
	}
	
	public static <E> CollectionChangedEvent<E> forItemsRemoved(
			IObservableCollection<E> collection, Collection<E> removed) {
		Collection<E> added = Collections.emptyList();
		return new CollectionChangedEvent<E>(collection, added, removed);
	}
	
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
	
	public Collection<E> getAdded() {
		return added;
	}
	
	public Collection<E> getRemoved() {
		return removed;
	}
	
	public IObservableCollection<E> getSource() {
		return collection;
	}
}
