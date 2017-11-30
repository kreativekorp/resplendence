/*
 * Copyright &copy; 2007-2009 Rebecca G. Bettencourt / Kreative Software
 * <p>
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * <a href="http://www.mozilla.org/MPL/">http://www.mozilla.org/MPL/</a>
 * <p>
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 * <p>
 * Alternatively, the contents of this file may be used under the terms
 * of the GNU Lesser General Public License (the "LGPL License"), in which
 * case the provisions of LGPL License are applicable instead of those
 * above. If you wish to allow use of your version of this file only
 * under the terms of the LGPL License and not to allow others to use
 * your version of this file under the MPL, indicate your decision by
 * deleting the provisions above and replace them with the notice and
 * other provisions required by the LGPL License. If you do not delete
 * the provisions above, a recipient may use your version of this file
 * under either the MPL or the LGPL License.
 * @since KJL 1.0
 * @author Rebecca G. Bettencourt, Kreative Software
 */

package com.kreative.util;

import java.io.*;
import java.util.*;

/**
 * The <code>VectorMap</code> class implements a <code>Map</code> which keeps
 * its keys in the order in which they were added. This is done by keeping
 * two parallel <code>Vector</code>s, one containing keys and one containing
 * values.
 * <p>
 * <code>VectorMap</code> fully conforms to the <code>Map</code> interface,
 * including behavior of <code>Collection</code>s returned by the
 * <code>entrySet()</code>, <code>keySet()</code>, and <code>values()</code>
 * methods, and <code>Iterator</code>s returned by said <code>Collection</code>s.
 * @since KJL 1.0
 * @author Rebecca G. Bettencourt, Kreative Software
 * @param <K> the type of each key
 * @param <V> the type of each value
 */
public class VectorMap<K,V> implements Map<K,V>, Cloneable, Serializable {
	private static final long serialVersionUID = 2394877l;
	protected List<K> keys;
	protected List<V> values;
	
	/**
	 * Constructs an empty <code>VectorMap</code>.
	 */
	public VectorMap() {
		keys = new Vector<K>();
		values = new Vector<V>();
	}
	
	/**
	 * Constructs a new <code>VectorMap</code> with the same mappings
	 * as the specified <code>Map</code>.
	 * @param m the map whose mappings are to be placed in this map.
	 * @throws NullPointerException if the specified map is null.
	 */
	public VectorMap(Map<? extends K,? extends V> m) {
		keys = new Vector<K>();
		values = new Vector<V>();
		Iterator<? extends K> it = m.keySet().iterator();
		while (it.hasNext()) {
			K key = it.next();
			V value = m.get(key);
			int i = keys.indexOf(key);
			if (i < 0) {
				keys.add(key);
				values.add(value);
			} else {
				values.set(i, value);
			}
		}
	}
	
	/**
	 * Removes all mappings from this map.
	 */
	public void clear() {
		keys.clear();
		values.clear();
	}
	
	/**
	 * Returns <code>true</code> if this map contains a mapping for the specified key.
	 * @param key The key whose presence in this map is to be tested
	 * @return <code>true</code> if this map contains a mapping for the specified key.
	 */
	public boolean containsKey(Object key) {
		return keys.contains(key);
	}
	
	/**
	 * Returns <code>true</code> if this map maps one or more keys to the specified value.
	 * @param value value whose presence in this map is to be tested.
	 * @return <code>true</code> if this map maps one or more keys to the specified value.
	 */
	public boolean containsValue(Object value) {
		return values.contains(value);
	}

	/**
	 * Returns a collection view of the mappings contained in this map. Each element in
	 * the returned collection is a <code>Map.Entry</code>. The collection is backed
	 * by the map, so changes to the map are reflected in the collection, and vice-versa.
	 * The collection supports element removal, which removes the corresponding mapping
	 * from the map, via the <code>Iterator.remove</code>, <code>Set.remove</code>,
	 * <code>removeAll</code>, <code>retainAll</code>, and <code>clear</code>
	 * operations. It does not support the <code>add</code> or <code>addAll</code> operations.
	 * @return a collection view of the mappings contained in this map.
	 */
	@SuppressWarnings("unchecked")
	public Set<Map.Entry<K,V>> entrySet() {
		return new Set<Map.Entry<K,V>>() {
			public boolean add(java.util.Map.Entry<K,V> o) {
				throw new UnsupportedOperationException();
			}
			public boolean addAll(Collection<? extends Map.Entry<K,V>> c) {
				throw new UnsupportedOperationException();
			}
			public void clear() {
				keys.clear();
				values.clear();
			}
			public boolean contains(Object o) {
				if (o instanceof Map.Entry) {
					Map.Entry<?,?> me = (Map.Entry<?,?>)o;
					Object ko = me.getKey();
					Object vo = me.getValue();
					int i = keys.indexOf(ko);
					if (i >= 0) return values.get(i).equals(vo);
				}
				return false;
			}
			public boolean containsAll(Collection<?> c) {
				Iterator<?> it = c.iterator();
				while (it.hasNext()) if (!contains(it.next())) return false;
				return true;
			}
			public boolean isEmpty() {
				return keys.isEmpty() || values.isEmpty();
			}
			public Iterator<Map.Entry<K,V>> iterator() {
				return new Iterator<Map.Entry<K,V>>() {
					int idx = 0;
					public boolean hasNext() {
						return (idx < keys.size());
					}
					public Map.Entry<K,V> next() {
						Entry e = new Entry(keys.get(idx),values.get(idx));
						idx++; return e;
					}
					public void remove() {
						idx--;
						keys.remove(idx);
						values.remove(idx);
					}
				};
			}
			public boolean remove(Object o) {
				if (o instanceof Map.Entry) {
					Map.Entry<?,?> me = (Map.Entry<?,?>)o;
					Object ko = me.getKey();
					Object vo = me.getValue();
					int i = keys.indexOf(ko);
					if (i >= 0 && values.get(i).equals(vo)) {
						keys.remove(i);
						values.remove(i);
						return true;
					}
				}
				return false;
			}
			public boolean removeAll(Collection<?> c) {
				boolean res = false;
				Iterator<?> it = c.iterator();
				while (it.hasNext()) res = res || remove(it.next());
				return res;
			}
			public boolean retainAll(Collection<?> c) {
				boolean res = false;
				for (int i=keys.size()-1; i>=0; i--) {
					if (!c.contains(new Entry(keys.get(i),values.get(i)))) {
						keys.remove(i);
						values.remove(i);
						res = true;
					}
				}
				return res;
			}
			public int size() {
				return keys.size();
			}
			public Object[] toArray() {
				Object[] a = new Object[keys.size()];
				for (int i=keys.size()-1; i>=0; i--) {
					a[i] = new Entry(keys.get(i),values.get(i));
				}
				return a;
			}
			public <T> T[] toArray(T[] arr) {
				Object[] a = new Object[keys.size()];
				for (int i=keys.size()-1; i>=0; i--) {
					a[i] = new Entry(keys.get(i),values.get(i));
				}
				return (T[])a;
			}
		};
	}
	
	/**
	 * Returns the value to which the specified key is mapped in this map, or
	 * <code>null</code> if the map contains no mapping for this key. A return
	 * value of <code>null</code> does not <i>necessarily</i> indicate that
	 * the map contains no mapping for the key; it is also possible that the
	 * map explicitly maps the key to <code>null</code>. The <code>containsKey</code>
	 * method may be used to distinguish these two cases.
	 * @param key the key whose associated value is to be returned.
	 * @return the value to which this map maps the specified key, or <code>null</code> if the map contains no mapping for this key.
	 */
	public V get(Object key) {
		int i = keys.indexOf(key);
		if (i < 0) return null;
		else return values.get(i);
	}
	
	/**
	 * Returns the index of the specified key in this <code>VectorMap</code>, or
	 * -1 if the map contains no mapping for this key.
	 * @param key the key whose index is to be returned.
	 * @return the index of the specified key in this <code>VectorMap</code>, or -1 if the map contains no mapping for this key.
	 */
	public int indexOfKey(K key) {
		return keys.indexOf(key);
	}
	
	/**
	 * Returns the first index of the specified value in this <code>VectorMap</code>,
	 * or -1 if the map contains no keys mapping to the specified value.
	 * @param value the value whose index is to be returned.
	 * @return the first index of the specified value in this <code>VectorMap</code>, or -1 if the map contains no keys mapping to the specified value.
	 */
	public int indexOfValue(V value) {
		return values.indexOf(value);
	}
	
	/**
	 * Returns <code>true</code> if this map contains no key-value mappings.
	 * @return <code>true</code> if this map contains no key-value mappings.
	 */
	public boolean isEmpty() {
		return keys.isEmpty() || values.isEmpty();
	}
	
	/**
	 * Returns a set view of the keys contained in this map. The set is backed
	 * by the map, so changes to the map are reflected in the set, and vice-versa.
	 * The set supports element removal, which removes the corresponding mapping
	 * from the map, via the <code>Iterator.remove</code>, <code>Set.remove</code>,
	 * <code>removeAll</code>, <code>retainAll</code>, and <code>clear</code>
	 * operations. It does not support the <code>add</code> or <code>addAll</code> operations.
	 * @return a set view of the keys contained in this map.
	 */
	public Set<K> keySet() {
		return new Set<K>() {
			public boolean add(K o) {
				throw new UnsupportedOperationException();
			}
			public boolean addAll(Collection<? extends K> c) {
				throw new UnsupportedOperationException();
			}
			public void clear() {
				keys.clear();
				values.clear();
			}
			public boolean contains(Object o) {
				return keys.contains(o);
			}
			public boolean containsAll(Collection<?> c) {
				return keys.containsAll(c);
			}
			public boolean isEmpty() {
				return keys.isEmpty() || values.isEmpty();
			}
			public Iterator<K> iterator() {
				return new Iterator<K>() {
					int idx = 0;
					public boolean hasNext() {
						return (idx < keys.size());
					}
					public K next() {
						return keys.get(idx++);
					}
					public void remove() {
						idx--;
						keys.remove(idx);
						values.remove(idx);
					}
				};
			}
			public boolean remove(Object o) {
				int i = keys.indexOf(o);
				if (i < 0) return false;
				else {
					keys.remove(i);
					values.remove(i);
					return true;
				}
			}
			public boolean removeAll(Collection<?> c) {
				boolean res = false;
				Iterator<?> it = c.iterator();
				while (it.hasNext()) {
					int i = keys.indexOf(it.next());
					if (i >= 0) {
						keys.remove(i);
						values.remove(i);
						res = true;
					}
				}
				return res;
			}
			public boolean retainAll(Collection<?> c) {
				boolean res = false;
				for (int i=keys.size()-1; i>=0; i--) {
					if (!c.contains(keys.get(i))) {
						keys.remove(i);
						values.remove(i);
						res = true;
					}
				}
				return res;
			}
			public int size() {
				return keys.size();
			}
			public Object[] toArray() {
				return keys.toArray();
			}
			public <T> T[] toArray(T[] a) {
				return keys.toArray(a);
			}
		};
	}

	/**
	 * Returns the last index of the specified value in this <code>VectorMap</code>,
	 * or -1 if the map contains no keys mapping to the specified value.
	 * @param value the value whose index is to be returned.
	 * @return the last index of the specified value in this <code>VectorMap</code>, or -1 if the map contains no keys mapping to the specified value.
	 */
	public int lastIndexOfValue(V value) {
		return values.lastIndexOf(value);
	}
	
	/**
	 * Associates the specified value with the specified key in this map. If the map
	 * previously contained a mapping for this key, the old value is replaced.
	 * @param key key with which the specified value is to be associated.
	 * @param value value to be associated with the specified key.
	 * @return previous value associated with specified key, or <code>null</code> if there was no mapping for key. A <code>null</code> return can also indicate that the map previously associated <code>null</code> with the specified key.
	 */
	public V put(K key, V value) {
		int i = keys.indexOf(key);
		if (i < 0) {
			keys.add(key);
			values.add(value);
			return null;
		} else {
			return values.set(i, value);
		}
	}
	
	/**
	 * Associates the specified value with the specified key in this map. If the map
	 * previously contained a mapping for this key, the old value is replaced.
	 * If this <code>VectorMap</code> did not previously contain a mapping for this
	 * key, the key and value are inserted at the specified index.
	 * @param index index at which to insert the mapping if the map did not previously contain a mapping for this key.
	 * @param key key with which the specified value is to be associated.
	 * @param value value to be associated with the specified key.
	 * @return previous value associated with specified key, or <code>null</code> if there was no mapping for key. A <code>null</code> return can also indicate that the map previously associated <code>null</code> with the specified key.
	 */
	public V put(int index, K key, V value) {
		int i = keys.indexOf(key);
		if (i < 0) {
			keys.add(index, key);
			values.add(index, value);
			return null;
		} else {
			return values.set(i, value);
		}
	}
	
	/**
	 * Copies all of the mappings from the specified map to this map. These
	 * mappings will replace any mappings that this map had for any of the
	 * keys currently in the specified map.
	 * @param t mappings to be stored in this map.
	 * @throws NullPointerException if the specified map is null.
	 */
	public void putAll(Map<? extends K, ? extends V> t) {
		Iterator<? extends K> it = t.keySet().iterator();
		while (it.hasNext()) {
			K key = it.next();
			V value = t.get(key);
			int i = keys.indexOf(key);
			if (i < 0) {
				keys.add(key);
				values.add(value);
			} else {
				values.set(i, value);
			}
		}
	}
	
	/**
	 * Removes the mapping for this key from this map if present.
	 * @param key key whose mapping is to be removed from the map.
	 * @return previous value associated with specified key, or <code>null</code> if there was no mapping for key. A <code>null</code> return can also indicate that the map previously associated <code>null</code> with the specified key.
	 */
	public V remove(Object key) {
		int i = keys.indexOf(key);
		if (i < 0) {
			return null;
		} else {
			keys.remove(i);
			return values.remove(i);
		}
	}
	
	/**
	 * Returns the number of key-value mappings in this map.
	 * @return the number of key-value mappings in this map.
	 */
	public int size() {
		return keys.size();
	}

	/**
	 * Returns a collection view of the values contained in this map. The collection is backed
	 * by the map, so changes to the map are reflected in the collection, and vice-versa.
	 * The collection supports element removal, which removes the corresponding mapping
	 * from the map, via the <code>Iterator.remove</code>, <code>Set.remove</code>,
	 * <code>removeAll</code>, <code>retainAll</code>, and <code>clear</code>
	 * operations. It does not support the <code>add</code> or <code>addAll</code> operations.
	 * @return a collection view of the values contained in this map.
	 */
	public Collection<V> values() {
		return new Collection<V>() {
			public boolean add(V arg0) {
				throw new UnsupportedOperationException();
			}
			public boolean addAll(Collection<? extends V> arg0) {
				throw new UnsupportedOperationException();
			}
			public void clear() {
				keys.clear();
				values.clear();
			}
			public boolean contains(Object o) {
				return values.contains(o);
			}
			public boolean containsAll(Collection<?> c) {
				return values.containsAll(c);
			}
			public boolean isEmpty() {
				return keys.isEmpty() || values.isEmpty();
			}
			public Iterator<V> iterator() {
				return new Iterator<V>() {
					int idx = 0;
					public boolean hasNext() {
						return (idx < values.size());
					}
					public V next() {
						return values.get(idx++);
					}
					public void remove() {
						idx--;
						keys.remove(idx);
						values.remove(idx);
					}
				};
			}
			public boolean remove(Object o) {
				int i = values.indexOf(o);
				if (i < 0) return false;
				else {
					keys.remove(i);
					values.remove(i);
					return true;
				}
			}
			public boolean removeAll(Collection<?> c) {
				boolean res = false;
				Iterator<?> it = c.iterator();
				while (it.hasNext()) {
					int i = values.indexOf(it.next());
					if (i >= 0) {
						keys.remove(i);
						values.remove(i);
						res = true;
					}
				}
				return res;
			}
			public boolean retainAll(Collection<?> c) {
				boolean res = false;
				for (int i=values.size()-1; i>=0; i--) {
					if (!c.contains(values.get(i))) {
						keys.remove(i);
						values.remove(i);
						res = true;
					}
				}
				return res;
			}
			public int size() {
				return values.size();
			}
			public Object[] toArray() {
				return values.toArray();
			}
			public <T> T[] toArray(T[] a) {
				return values.toArray(a);
			}
		};
	}
	
	private class Entry implements Map.Entry<K,V> {
		private K key;
		private V value;
		public Entry(K key, V value) {
			this.key = key;
			this.value = value;
		}
		public K getKey() {
			return key;
		}
		public V getValue() {
			return value;
		}
		public V setValue(V value) {
			int i = keys.indexOf(key);
			if (i < 0) {
				keys.add(key);
				values.add(value);
			} else {
				values.set(i,value);
			}
			return this.value = value;
		}
	}
}
