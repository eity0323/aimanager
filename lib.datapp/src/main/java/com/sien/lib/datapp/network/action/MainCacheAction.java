package com.sien.lib.datapp.network.action;

import android.text.TextUtils;

import com.sien.lib.datapp.beans.BaseResult;
import com.sien.lib.datapp.cache.BaseCacheAction;
import com.sien.lib.datapp.cache.CacheDataStorage;
import com.sien.lib.datapp.cache.CacheDataStorageCallBack;
import com.sien.lib.datapp.cache.CacheDataStorageStringCallBack;
import com.sien.lib.datapp.control.JsonMananger;
import com.sien.lib.datapp.events.DatappEvent;
import com.sien.lib.datapp.network.result.AimTypeResult;
import com.sien.lib.datapp.utils.EventPostUtil;

/**
 * @author sien
 * @date 2016/10/20
 * @descript 缓存action
 */
public class MainCacheAction extends BaseCacheAction {
    /**
     * 请求目标分类数据
     * @param key
     */
    public static void requestAimTypeDatas(String key){
        CacheDataStorage.getInstance().readCacheJsonStringFromStorage(key, new CacheDataStorageStringCallBack() {
            @Override
            public void complete(String jsonResult) {
                if (!TextUtils.isEmpty(jsonResult)){
                    try {
                        AimTypeResult result = JsonMananger.jsonToBean(jsonResult,AimTypeResult.class);
                        if(result != null){
                            EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_SUCCESS, result));
                            return;
                        }
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
                EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_FAIL_OHTERERROR, "cache error"));
            }
        });
    }

    /**
     * 请求目标记录数据
     */
    public static void requestAimItemDatas(String key){
        CacheDataStorage.getInstance().readCacheFromStorage(key, new CacheDataStorageCallBack() {
            @Override
            public void complete(BaseResult result) {
                if (result != null) {
                    EventPostUtil.post(new DatappEvent.AimItemEvent(DatappEvent.STATUS_SUCCESS, result));
                }else {
                    EventPostUtil.post(new DatappEvent.AimItemEvent(DatappEvent.STATUS_FAIL_OHTERERROR, "cache error"));
                }
            }
        });
    }

}
