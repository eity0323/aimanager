package com.sien.lib.databmob.cache.memory;

import android.util.LruCache;

/**
 * @author sien
 * @description lrucache 内存缓存管理类
 * eg:
 * 		LruCacheManager.getInstance.put(“testString”, mEt_string_input.getText().toString());
 * 		LruCacheManager.getInstance.get(“testString”);
 **/
public class LruCacheManager {

	/**
	 * lruCache
	 */
	private LruCache<String, Object> lruCache;
	/**
	 * instance
	 */
	private static LruCacheManager instance;
	/**
	 * cache size
	 */
	private final int CACHE_SIZE = 50;

	/**
	 *  LruCacheManager constructor
	 */
	private LruCacheManager() {
		this.lruCache = new LruCache<String, Object>(CACHE_SIZE);
	}
	
	/**
	 *  LruCacheManager constructor
	 */

	/**
	 * get the LruCacheManager instance, it is the singleton
	 * 
	 * @return LruCacheManager
	 */
	public static LruCacheManager getInstance() {
		if (instance == null) {
			synchronized (LruCacheManager.class) {
				if (instance == null) {
					instance = new LruCacheManager();
				}
			}
		}
		return instance;
	}

	/**
	 * put
	 * @param key String
	 * @param value Object
	 */
	public void put(String key, Object value) {
		lruCache.put(key, value);
	}

	/**
	 * get
	 * @param key String
	 * @return Object
	 */
	public Object get(String key) {
		return lruCache.get(key);
	}

	/**
	 * remove 
	 * @param key String
	 * @return Object
	 */
	public Object remove(String key) {
		return lruCache.remove(key);
	}

	/**
	 * evictAll 清除数据
	 */
	public void evictAll() {
		lruCache.evictAll();
	}

	/**
	 * maxSize
	 * @return int
	 */
	public int maxSize() {
		return lruCache.maxSize();
	}

	/**
	 * size
	 * @return int
	 */
	public int size() {
		return lruCache.size();
	}

}
