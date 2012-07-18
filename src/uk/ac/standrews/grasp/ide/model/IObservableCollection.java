package uk.ac.standrews.grasp.ide.model;

import java.util.Collection;

public interface IObservableCollection<E> extends Collection<E> {
	void addCollectionChangeListener(ICollectionChangedListener<E> listener);
	void removeCollectionChangeListener(ICollectionChangedListener<E> listener);
	
	void fireCollectionChanged(CollectionChangedEvent<E> event);
}
