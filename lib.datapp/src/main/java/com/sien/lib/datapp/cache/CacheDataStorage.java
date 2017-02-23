package com.sien.lib.datapp.cache;

import android.content.Context;
import android.text.TextUtils;

import com.sien.lib.datapp.beans.BaseResult;
import com.sien.lib.datapp.cache.disk.DiskLruCacheManager;
import com.sien.lib.datapp.cache.memory.LruCacheManager;
import com.sien.lib.datapp.cache.share.ShareCacheManager;
import com.sien.lib.datapp.control.CPSharedPreferenceManager;
import com.sien.lib.datapp.control.CPThreadPoolManager;
import com.sien.lib.datapp.control.DataCleanManager;
import com.sien.lib.datapp.control.JsonMananger;
import com.sien.lib.datapp.control.ParseDataException;
import com.sien.lib.datapp.utils.CPFileUtils;

import java.io.IOException;

/**
 * @author sien
 * @date 2016/10/20
 * @descript 数据缓存仓库
 * 需要提前调用CacheDataStorage.getInstance().init(appcontext)初始化
 */
public class CacheDataStorage{
    private static DiskLruCacheManager lruCacheManager = null;
    private static ShareCacheManager shareCacheManager;
    private static Context mAppContext;

    private boolean needMemoryCache = true;//是否开启内存缓存
    private boolean needDiskCache = true;

    protected static class CacheDataStorageHolder{
        public static CacheDataStorage INSTANCE = new CacheDataStorage();
    }

    private CacheDataStorage(){}

    public void init(Context appContext){
        mAppContext = appContext.getApplicationContext();
    }

    public static CacheDataStorage getInstance(){
        return CacheDataStorageHolder.INSTANCE;
    }

    public void setNeedMemoryCache(boolean needMemoryCache) {
        this.needMemoryCache = needMemoryCache;
    }

    public void setNeedDiskCache(boolean needDiskCache) {
        this.needDiskCache = needDiskCache;
    }

    /**
     * 同步写缓存
     * @param key
     * @param value
     */
    public void writeCacheToStorageSync(String key, BaseResult value){
        checkContext();

        //cache to memory
        if (needMemoryCache) {
            LruCacheManager.getInstance().put(key, value);
        }

        //cache to disk
        if (needDiskCache) {
            try {
                lruCacheManager = new DiskLruCacheManager(mAppContext);
                lruCacheManager.put(key, value);
            } catch (IOException e) {
                e.printStackTrace();

                //cache to disk with expire time
                shareCacheManager = ShareCacheManager.get(mAppContext);
                shareCacheManager.put(key, value);
            }
        }
    }

    /**
     * 同步写缓存
     * @param key
     * @param json
     */
    public void writeCacheToStorageSync(String key,String json){
        checkContext();

        //cache to memory
        if (needMemoryCache) {
            LruCacheManager.getInstance().put(key, json);
        }

        //cache to disk
        if (needDiskCache) {
            try {
                lruCacheManager = new DiskLruCacheManager(mAppContext);
                lruCacheManager.put(key, json);
            } catch (IOException e) {
                e.printStackTrace();

                //cache to disk with expire time
                shareCacheManager = ShareCacheManager.get(mAppContext);
                shareCacheManager.put(key, json);
            }
        }
    }

    /**
     * 同步写缓存
     * @param key
     * @param data
     * @throws ParseDataException
     */
    public void writeCacheToStorageSync(String key,Object data) throws ParseDataException {
        checkContext();

        String json = JsonMananger.beanToJson(data);

        //cache to memory
        if (needMemoryCache) {
            LruCacheManager.getInstance().put(key, json);
        }

        //cache to disk
        if (needDiskCache) {
            try {
                lruCacheManager = new DiskLruCacheManager(mAppContext);
                lruCacheManager.put(key, json);
            } catch (IOException e) {
                e.printStackTrace();

                //cache to disk with expire time
                shareCacheManager = ShareCacheManager.get(mAppContext);
                shareCacheManager.put(key, json);
            }
        }
    }

    /**
     * 同步写缓存
     * @param key
     * @param data
     * @param useMemoryCache 指定缓存模式
     * @throws ParseDataException
     */
    public void writeCacheToStorageSync(String key,Object data,boolean useMemoryCache) throws ParseDataException{
        checkContext();

        String json = JsonMananger.beanToJson(data);

        //cache to memory
        if (useMemoryCache) {
            LruCacheManager.getInstance().put(key, json);
        }else {
            //cache to disk
            try {
                lruCacheManager = new DiskLruCacheManager(mAppContext);
                lruCacheManager.put(key, json);
            } catch (IOException e) {
                e.printStackTrace();

                //cache to disk with expire time
                shareCacheManager = ShareCacheManager.get(mAppContext);
                shareCacheManager.put(key, json);
            }
        }
    }

    /**
     * 异步写缓存
     * @param key
     * @param value
     */
    public void writeCacheToStorage(final String key,final Object value){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    writeCacheToStorageSync(key,value);
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        };
        CPThreadPoolManager.newInstance().addExecuteTask(r);
    }

    /**
     * 异步写缓存
     * @param key
     * @param value
     * @param needMemoryCache
     */
    public void writeCacheToStorage(final String key,final Object value,final boolean needMemoryCache){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    writeCacheToStorageSync(key,value,needMemoryCache);
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        };
        CPThreadPoolManager.newInstance().addExecuteTask(r);
    }

    /**
     * 异步写缓存
     */
    public void writeCacheToStorage(final String key,final BaseResult value,final CacheDataStorageCallBack callBack){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                writeCacheToStorageSync(key,value);
                if (callBack != null){
                    callBack.complete(null);
                }
            }
        };
        CPThreadPoolManager.newInstance().addExecuteTask(r);
    }

    /**
     * 异步写缓存
     * @param key
     * @param value
     * @param callBack
     */
    public void writeCacheToStorage(final String key,final String value,final CacheDataStorageStringCallBack callBack){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                writeCacheToStorageSync(key,value);
                if (callBack != null){
                    callBack.complete(null);
                }
            }
        };
        CPThreadPoolManager.newInstance().addExecuteTask(r);
    }

    /**
     * 同步读缓存
     * @param key
     * @return
     */
    public BaseResult readCacheFromStorageSync(String key){
        checkContext();

        BaseResult result = null;

        if (needMemoryCache) {
            result = (BaseResult) LruCacheManager.getInstance().get(key);
        }

        if (needDiskCache) {
            if (result == null) {
                if (lruCacheManager != null) {
                    String res = lruCacheManager.getAsString(key);

                    if (!TextUtils.isEmpty(res)) {
                        try {
                            result = JsonMananger.jsonToBean(res, BaseResult.class);
                            return result;

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                if (shareCacheManager != null) {
                    result = (BaseResult) shareCacheManager.getAsObject(key);
                    return result;
                }
            }
        }

        return result;
    }

    /**
     * 同步读缓存
     * @param key
     * @return
     */
    public String readCacheJsonStringFromStorageSync(String key){
        checkContext();

        String result = "";
        if (needMemoryCache) {
            if (LruCacheManager.getInstance().get(key) != null) {
                result = LruCacheManager.getInstance().get(key).toString();
            }
        }

        if (needDiskCache) {
            if (result == null) {
                if (lruCacheManager != null) {
                    result = lruCacheManager.getAsString(key);
                    return result;
                }

                if (shareCacheManager != null) {
                    result = shareCacheManager.getAsObject(key).toString();
                    return result;
                }
            }
        }

        return result;
    }

    /**
     * 同步读缓存
     * @param key
     * @param useMemoryCache 从指定缓存中读取
     * @return
     */
    public String readCacheJsonStringFromStorageSync(String key,boolean useMemoryCache){
        checkContext();

        String result = "";
        if (useMemoryCache) {
            if (LruCacheManager.getInstance().get(key) != null) {
                result = LruCacheManager.getInstance().get(key).toString();
            }
        }else {
            if (result == null) {
                if (lruCacheManager != null) {
                    result = lruCacheManager.getAsString(key);
                    return result;
                }

                if (shareCacheManager != null) {
                    result = shareCacheManager.getAsObject(key).toString();
                    return result;
                }
            }
        }

        return result;
    }

    /**
     * 同步读缓存
     * @param key
     * @param cls
     * @param <T>
     * @return
     * @throws ParseDataException
     */
    public  <T> T readCacheJsonObjectFromStorageSync(String key,Class<T> cls) throws ParseDataException{
        checkContext();

        String result = "";
        if (needMemoryCache) {
            if (LruCacheManager.getInstance().get(key) != null) {
                result = LruCacheManager.getInstance().get(key).toString();
            }
        }

        if (needDiskCache) {
            if (result == null) {
                if (lruCacheManager != null) {
                    result = lruCacheManager.getAsString(key);
                    return strig2T(result, cls);
                }

                if (shareCacheManager != null) {
                    result = shareCacheManager.getAsObject(key).toString();
                    return strig2T(result, cls);
                }
            }
        }

        return strig2T(result,cls);
    }

    /**
     * 同步读缓存
     * @param key
     * @param cls
     * @param useMemoryCache
     * @param <T>
     * @return
     * @throws ParseDataException
     */
    public  <T> T readCacheJsonObjectFromStorageSync(String key,Class<T> cls,boolean useMemoryCache) throws ParseDataException{
        checkContext();

        String result = "";
        if (useMemoryCache) {
            if (LruCacheManager.getInstance().get(key) != null) {
                result = LruCacheManager.getInstance().get(key).toString();
            }
        }else {
            if (result == null) {
                if (lruCacheManager != null) {
                    result = lruCacheManager.getAsString(key);
                    return strig2T(result, cls);
                }

                if (shareCacheManager != null) {
                    result = shareCacheManager.getAsObject(key).toString();
                    return strig2T(result, cls);
                }
            }
        }

        return strig2T(result,cls);
    }

    /**
     * 字符串转泛型
     * @param json
     * @param cls
     * @param <T>
     * @return
     * @throws ParseDataException
     */
    private  <T> T strig2T(String json,Class<T> cls) throws ParseDataException{
        if (TextUtils.isEmpty(json)){
            return null;
        }
        return JsonMananger.jsonToBean(json, cls);
    }

    /**
     * 异步读缓存
     */
    public void readCacheFromStorage(final String key,final CacheDataStorageCallBack callBack){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                BaseResult result = readCacheFromStorageSync(key);
                if (callBack != null){
                    callBack.complete(result);
                }
            }
        };
        CPThreadPoolManager.newInstance().addExecuteTask(r);
    }

    /**
     * 异步读缓存
     * @param key
     * @param callBack
     */
    public void readCacheJsonStringFromStorage(final String key,final CacheDataStorageStringCallBack callBack){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                String result = readCacheJsonStringFromStorageSync(key);
                if (callBack != null){
                    callBack.complete(result);
                }
            }
        };
        CPThreadPoolManager.newInstance().addExecuteTask(r);
    }

    /**
     * 删除缓存数据
     * @param key
     */
    public void removeCacheFromStorage(String key){
        //remove memory cache
        if (needMemoryCache) {
            LruCacheManager.getInstance().remove(key);
        }

        if (needDiskCache) {
            //remove disk cache
            if (lruCacheManager != null) {
                lruCacheManager.remove(key);
            }
            //remove file cache
            if (shareCacheManager != null) {
                shareCacheManager.remove(key);
            }
        }
    }

    /**
     * 清除应用缓存及cookie（未测试）
     */
    public void clear(){
        checkContext();

        CPSharedPreferenceManager.getInstance(mAppContext).clean();

        DataCleanManager.cleanSharedPreference(mAppContext);
        DataCleanManager.cleanExternalCache(mAppContext);
        DataCleanManager.cleanInternalCache(mAppContext);
        //删除本地缓存文件夹 /mnt/sdcard/Android/data/[app package]/cache
        DataCleanManager.deleteFielOrDirectoryByPath(CPFileUtils.getAppCacheRootFilePath(mAppContext));
    }

    public void releaseMemory(){
        LruCacheManager.getInstance().evictAll();
    }

    private void checkContext(){
        if (mAppContext == null){
            throw new IllegalArgumentException("appcontext is null,please call the CacheDataStorage.getInstance().init(appcontext) method to init it");
        }
    }
}
