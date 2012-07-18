package uk.ac.standrews.grasp.ide.model;

public interface IMapChangedListener<K, V> {
	void mapChanged(MapChangedEvent<K, V> event);
}