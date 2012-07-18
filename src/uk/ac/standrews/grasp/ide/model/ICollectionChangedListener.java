package uk.ac.standrews.grasp.ide.model;

public interface ICollectionChangedListener<E> {
	void collectionChanged(CollectionChangedEvent<E> event);
}
