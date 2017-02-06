package com.sien.lib.datapp.cache;

import com.sien.lib.datapp.beans.BaseResult;

/**
 * @author sien
 * @date 2016/11/1
 * @descript 缓存操作回调接口
 */
public interface CacheDataStorageCallBack {
    public void complete(BaseResult result);
}
