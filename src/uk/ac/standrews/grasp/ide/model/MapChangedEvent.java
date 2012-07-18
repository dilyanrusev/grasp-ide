package uk.ac.standrews.grasp.ide.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.runtime.Assert;

public class MapChangedEvent<K, V> {
	private final K key;
	private final Collection<V> removed;
	private final Collection<V> added;
	private final IObservableMap<K, V> source;
	private final boolean clear;
	
	public MapChangedEvent(IObservableMap<K, V> source, K key, 
			Collection<V> added, Collection<V> removed) {
		Assert.isNotNull(source);
		Assert.isNotNull(added);
		Assert.isNotNull(removed);
		this.source = source;
		this.key = key;
		this.added = added;
		this.removed = removed;
		this.clear = false;
	}
	
	private MapChangedEvent(IObservableMap<K,V> source) {
		Assert.isNotNull(source);
		this.source = source;
		this.key = null;
		this.added = Collections.emptyList();
		this.removed = Collections.emptyList();
		this.clear = true;
	}

	public static <K, V> MapChangedEvent<K, V> forClear(
			IObservableMap<K, V> source) {
		return new MapChangedEvent<K, V>(source);
	}
	
	public static <K, V> MapChangedEvent<K, V> forItemAdded(
			IObservableMap<K, V> source, K key, V value) {
		Collection<V> added = new ArrayList<V>(1);
		added.add(value);
		Collection<V> removed = Collections.emptyList();
		return new MapChangedEvent<K, V>(source, key, added, removed);
	}
	
	public static <K, V> MapChangedEvent<K, V> forItemsRemoved(
			IObservableMap<K, V> source, K key, Collection<V> removed) {
		Collection<V> added = Collections.emptyList();
		return new MapChangedEvent<K, V>(source, key, added, removed);
	}
	
	public K getKey() {
		return key;
	}

	public Collection<V> getRemoved() {
		return removed;
	}

	public Collection<V> getAdded() {
		return added;
	}

	public IObservableMap<K, V> getSource() {
		return source;
	}	
	
	public boolean isCleared() {
		return clear;
	}
}