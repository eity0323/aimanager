package com.sien.lib.datapp.cache;

/**
 * @author sien
 * @date 2016/11/15
 * @descript 通用的读取缓存数据的接口
 */
public class BaseCacheAction {
    /**
     * 通用的读取缓存数据的接口(读取bean对象缓存)
     * @param key
     * @param callBack
     */
    public static void requestCommonCacheData(String key,CacheDataStorageCallBack callBack){
        CacheDataStorage.getInstance().readCacheFromStorage(key,callBack);
    }

    /**
     * 通用的读取缓存数据的接口(读取json字符串缓存)
     * @param key
     * @return
     */
    public static void requestCommonCacheJsonData(String key,CacheDataStorageStringCallBack callBack){
       CacheDataStorage.getInstance().readCacheJsonStringFromStorage(key,callBack);
    }
}
