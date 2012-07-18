package uk.ac.standrews.grasp.ide.model;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ObservableList<E> extends AbstractList<E> implements IObservableCollection<E> {
	private List<E> store;
	private List<ICollectionChangedListener<E>> listeners =
			new ArrayList<ICollectionChangedListener<E>>();
	
	public ObservableList() {
		store = new ArrayList<E>();
	}
	
	public ObservableList(Collection<? extends E> other) {
		store = new ArrayList<E>(other);
	}

	@Override
	public void addCollectionChangeListener(
			ICollectionChangedListener<E> listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeCollectionChangeListener(
			ICollectionChangedListener<E> listener) {
		listeners.remove(listener);		
	}

	@Override
	public void fireCollectionChanged(CollectionChangedEvent<E> event) {
		for (ICollectionChangedListener<E> listener: listeners) {
			listener.collectionChanged(event);
		}
	}

	@Override
	public E get(int i) {
		return store.get(i);
	}

	@Override
	public int size() {
		return store.size();
	}
	
	@Override
	public E set(int index, E element) {
		E old = store.set(index, element);
		fireCollectionChanged(CollectionChangedEvent.forReplace(this, old, element));
		return old;
	}
	
	@Override
	public void add(int index, E element) {
		store.add(index, element);
		fireCollectionChanged(CollectionChangedEvent.forItemAdded(this, element));
	}
	
	@Override
	public E remove(int index) {
		E removed = store.remove(index);
		fireCollectionChanged(CollectionChangedEvent.forItemRemoved(this, removed));
		return removed;
	}
}
