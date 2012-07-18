package uk.ac.standrews.grasp.ide.model;

import grasp.util.misc.IMultiMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ObservableMultiHaspMap<K, V> implements
		IObservableMap<K, V>, IMultiMap<K, V> {
	private List<IMapChangedListener<K,V>> changeListeners = new ArrayList<IMapChangedListener<K, V>>();
	private Map<K, List<V>> store;

	public ObservableMultiHaspMap() {
		store = new HashMap<K, List<V>>();
	}
	
	@Override
	public Collection<V> getAll(Object key) {
		if (store.containsKey(key)) {
			return Collections.unmodifiableCollection(store.get(key));
		} else {
			return null;
		}
	}

	@Override
	public void addMapChangedListener(IMapChangedListener<K, V> listener) {
		if (!changeListeners.contains(listener)) {
			changeListeners.add(listener);
		}
	}

	@Override
	public void removeMapChangedListener(IMapChangedListener<K, V> listener) {
		changeListeners.remove(listener);
	}

	@Override
	public void fireMapChanged(MapChangedEvent<K, V> event) {
		for (IMapChangedListener<K, V> listener: changeListeners) {
			listener.mapChanged(event);
		}
	}

	@Override
	public int size() {
		return store.size();
	}

	@Override
	public boolean isEmpty() {
		return store.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return store.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		for (List<V> values: store.values()) {
			if (values.contains(value)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public V get(Object key) {
		if (store.containsKey(key)) {
			List<V> valsForKey = store.get(key);
			if (valsForKey != null && valsForKey.size() >= 1) {
				return valsForKey.get(0);
			}
		}
		return null;
	}

	@Override
	public V put(K key, V value) {		
		if (!store.containsKey(key)) {
			store.put(key, new ArrayList<V>());
		}
		store.get(key).add(value);
		fireMapChanged(MapChangedEvent.forItemAdded(this, key, value));
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public V remove(Object key) {
		List<V> removedValues = null;
		if (store.containsKey(key)) {
			removedValues = store.get(key);
		}
		store.remove(key);
		if (removedValues != null) {
			fireMapChanged(MapChangedEvent.forItemsRemoved(this, (K)key, removedValues));
		}
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void clear() {
		store.clear();
		fireMapChanged(MapChangedEvent.forClear(this));
	}

	@Override
	public Set<K> keySet() {
		return Collections.unmodifiableSet(store.keySet());
	}

	@Override
	public Collection<V> values() {
		Collection<V> result = new ArrayList<V>();
		for (K key: store.keySet()) {
			List<V> valuesForKey = store.get(key);
			result.addAll(valuesForKey);
		}
		return Collections.unmodifiableCollection(result);
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		throw new UnsupportedOperationException();
	}

}
