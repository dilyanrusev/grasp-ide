package uk.ac.standrews.grasp.ide.model;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Provides an observable implementation of <code>Set</code> based on <code>TreeSet</code>  
 * @author Dilyan Rusev
 *
 * @param <E> Type of the element managed by this collection
 */
public class ObservableSet<E> extends AbstractSet<E> implements IObservableCollection<E>, SortedSet<E> {
	private final List<ICollectionChangedListener<E>> changeListeners =
			new ArrayList<ICollectionChangedListener<E>>();		
	private SortedSet<E> store;
	
	/**
	 * Constructs new set with natural sorting
	 */
	public ObservableSet() {
		store = new TreeSet<E>();		
	}
	
	/**
	 * Constructs a new set with custom sorting
	 * @param comparator Comparator for custom sorting
	 */
	public ObservableSet(Comparator<? super E> comparator) {
		store = new TreeSet<E>(comparator);
	}	
	
	/**
	 * Constructs a new set by copying the identical items of another collection
	 * @param other Collection to copy into the newly created set
	 */
	public ObservableSet(Collection<? extends E> other) {
		store = new TreeSet<E>(other);
	}

	@Override
	public void addCollectionChangeListener(
			ICollectionChangedListener<E> listener) {
		if (!changeListeners.contains(listener)) {
			changeListeners.add(listener);
		}
	}

	@Override
	public void removeCollectionChangeListener(
			ICollectionChangedListener<E> listener) {
		changeListeners.remove(listener);		
	}

	@Override
	public void fireCollectionChanged(CollectionChangedEvent<E> event) {
		for (ICollectionChangedListener<E> listener: changeListeners) {
			listener.collectionChanged(event);
		}
	}

	@Override
	public Iterator<E> iterator() {
		return new ObservableSetIterator(store.iterator());
	}

	@Override
	public int size() {
		return store.size();
	}
	
	@Override
	public boolean add(E e) {
		boolean added = store.add(e);
		if (added) {
			fireCollectionChanged(CollectionChangedEvent.forItemAdded(this, e));
		}
		return added;
	}
	
	/**
	 * Resorts the collection
	 */
	public void resort() {
		SortedSet<E> copy = new TreeSet<E>();
		for (E child: store) {
			copy.add(child);
		}
		store = copy;
	}
	
	@Override
	public Comparator<? super E> comparator() {
		return store.comparator();
	}

	@Override
	public SortedSet<E> subSet(E fromElement, E toElement) {
		return store.subSet(fromElement, toElement);
	}

	@Override
	public SortedSet<E> headSet(E toElement) {
		return store.headSet(toElement);
	}

	@Override
	public SortedSet<E> tailSet(E fromElement) {
		return store.tailSet(fromElement);
	}

	@Override
	public E first() {
		return store.first();
	}

	@Override
	public E last() {
		return store.last();
	}
	
	private class ObservableSetIterator implements Iterator<E> {
		private final Iterator<E> hostIterator;
		private E lastExtractedValue;
		
		public ObservableSetIterator(Iterator<E> hostIterator) {
			this.hostIterator = hostIterator;
		}
		
		@Override
		public boolean hasNext() {
			return hostIterator.hasNext();
		}

		@Override
		public E next() {
			lastExtractedValue = hostIterator.next();
			return lastExtractedValue;
		}

		@Override
		public void remove() {
			hostIterator.remove();
			fireCollectionChanged(CollectionChangedEvent.forItemRemoved(ObservableSet.this, lastExtractedValue));
		}		
	}
}
