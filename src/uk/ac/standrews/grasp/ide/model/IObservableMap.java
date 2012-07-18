package uk.ac.standrews.grasp.ide.model;

import java.util.Map;

public interface IObservableMap<K, V> extends Map<K, V> {
	void addMapChangedListener(IMapChangedListener<K, V> listener);
	void removeMapChangedListener(IMapChangedListener<K, V> listener);
	void fireMapChanged(MapChangedEvent<K, V> event);
}
