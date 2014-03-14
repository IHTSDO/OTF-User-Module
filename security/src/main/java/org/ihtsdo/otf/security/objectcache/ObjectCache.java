package org.ihtsdo.otf.security.objectcache;

import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * An object cache for statically managing shared objects.
 * @author Adam Flinton
 *
 */
public enum ObjectCache {
    /** The Instance. **/
	INSTANCE;
	/** The Cache hashtable. **/
	private ConcurrentHashMap<String, Object> cache;
	/** The logger. **/
	private static final Logger LOG = 
	    Logger.getLogger(ObjectCache.class.getName());
/**
 * The constructor.
 */
	private ObjectCache() {
		cache = new ConcurrentHashMap<String, Object>();
	}
/**
 * Retrieves the object from the cache using a key.
 * @param key The key
 * @return The object indicated by the key. If nothing found then null.
 */
	public Object get(final String key) {
		Object obj = null;
		try {
			if (cache.containsKey(key)) {
				synchronized (this) { // make thread safe
					obj = cache.get(key);
				}
			}
		} catch (Exception excep) {
			LOG.log(Level.SEVERE , "Error in ObjectCache.get key = "
			    + key , excep);
		}
		return obj;
	}
/**
 * Adds an object into the cache.
 * @param key The Key value used to retrieve the object.
 * @param obj The object to be stored.
 * @return A boolean which indicates if the operation was a success.
 */
	public boolean put(final String key, final Object obj) {
		boolean isok = false;
		try {
			synchronized (this) { // make thread safe
				cache.put(key, obj);
				isok = true;
			}

		} catch (Exception excep) {
			LOG.log(Level.SEVERE , "Error in ObjectCache.put key = "
			    + key + "Object = " + obj , excep);
			isok = false;
		}

		return isok;
	}

	/**
	 * Clear the cache.
	 */
	public void clear() {
		cache.clear();
	}

	/**
	 * 
	 * @return java.util.Enumeration
	 */
	public Enumeration<Object> elements() {
		return cache.elements();
	}

	/**
	 * Remove a specific object from the cache.
	 * 
	 * @param key
	 *            The filename of the object.
	 */
	public void remove(final Object key) {
		cache.remove(key);
	}

	/**
	 * Insert the method's description here. Creation date: (14/02/02 12:54:30)
	 * 
	 * @return int
	 */
	public int size() {
		return cache.size();
	}

	/**
	 * Insert the method's description here. Creation date: (22/07/2002
	 * 16:22:14)
	 */
	public void debugOC() {

		for (Entry<String, Object> entry : cache.entrySet()) {
			LOG.severe("OC Key = " + entry.getKey());
			LOG.severe("OC Value = " + entry.getValue());
		}

	}

	/**
	 * Insert the method's description here. Creation date: (14/02/02 12:53:12)
	 * 
	 * @return java.util.Enumeration
	 */
	public Enumeration<String> keys() {
		return cache.keys();
	}

}
