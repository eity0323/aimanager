package com.sien.lib.datapp.network.action;

import android.content.Context;
import android.text.TextUtils;

import com.sien.lib.datapp.beans.BaseResult;
import com.sien.lib.datapp.cache.BaseRepository;
import com.sien.lib.datapp.cache.CacheDataStorage;
import com.sien.lib.datapp.cache.CacheDataStorageCallBack;
import com.sien.lib.datapp.cache.CacheDataStorageStringCallBack;
import com.sien.lib.datapp.config.DatappConfig;
import com.sien.lib.datapp.control.JsonMananger;
import com.sien.lib.datapp.events.DatappEvent;
import com.sien.lib.datapp.network.result.AimTypeResult;
import com.sien.lib.datapp.utils.CPNetworkUtil;
import com.sien.lib.datapp.utils.CPStringUtil;
import com.sien.lib.datapp.utils.EventPostUtil;

/**
 * @author sien
 * @date 2016/10/20
 * @descript 数据请求仓库（对上封装数据来源，网络、内存、缓存、数据库）
 */
public class MainRepository extends BaseRepository {
    private static final String moduleSuffix = "main";

    /**
     * 请求目标分类
     * @param context
     * @param requestSource
     */
    public static void requestAimTypeDatas(final Context context,int requestSource){
        final String key = CPStringUtil.appendIdentifier(RequestApiConfig.ACTION_AIM_TYPE,moduleSuffix);

        centralRequest(context, requestSource, new BaseRepositoryOperation() {
            @Override
            public void requestFromNetwork() {
                //仅使用网络请求时，删除缓存数据
                CacheDataStorage.getInstance().removeCacheFromStorage(key);
                MainRequestAction.requestAimTypeDatas(context,key);
            }

            @Override
            public void requestFromCache() {
                MainCacheAction.requestAimTypeDatas(key);
            }

            @Override
            public void requestFromDatabase() {
                MainDatabaseAction.requestAimTypeDatas(context);
            }

            @Override
            public void requestAuto() {
                // 将返回数据以字符串的形式缓存（之前测试过程中，发现保存对象时数据会丢失，暂时先这么处理，待优化）
                 MainCacheAction.requestCommonCacheJsonData(key, new CacheDataStorageStringCallBack(){
                     @Override
                     public void complete(String jsonResult) {
                         if (!TextUtils.isEmpty(jsonResult)){
                             try {
                                 AimTypeResult result = JsonMananger.jsonToBean(jsonResult,AimTypeResult.class);
                                 if(result != null){
                                     EventPostUtil.post(new DatappEvent.AimItemEvent(DatappEvent.STATUS_SUCCESS, result));
                                     return;
                                 }
                             }catch (Exception ex){
                                 ex.printStackTrace();
                             }
                         }
                         MainRequestAction.requestAimTypeDatas(context,key);
                     }
                 });
            }

            @Override
            public void requestBoth() {
                MainCacheAction.requestCommonCacheJsonData(key, new CacheDataStorageStringCallBack() {
                    @Override
                    public void complete(String jsonResult) {
                        if (!TextUtils.isEmpty(jsonResult)){
                            try {
                                AimTypeResult result = JsonMananger.jsonToBean(jsonResult,AimTypeResult.class);
                                if(result != null){
                                    EventPostUtil.post(new DatappEvent.AimItemEvent(DatappEvent.STATUS_SUCCESS, result));
                                    return;
                                }
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }

                        if (!CPNetworkUtil.isNetworkAvailable(context)) {
                            MainRequestAction.requestAimTypeDatas(context,key);
                        }
                    }
                });
            }
        });
    }
    public static void requestAimTypeDatas(Context context){
        requestAimTypeDatas(context, DatappConfig.DEFAULT_REQUEST_MODEL);
    }

    /**
     * 请求目标记录项
     * @param context
     * @param aimTypeId
     * @param requestSource
     */
    public static void requestAimItemDatas(final Context context,final long
            aimTypeId,int requestSource){
        final String key = CPStringUtil.appendIdentifier(RequestApiConfig.ACTION_AIM_ITEM,String.valueOf(aimTypeId),moduleSuffix);

        centralRequest(context, requestSource, new BaseRepositoryOperation() {
            @Override
            public void requestFromNetwork() {
                //仅使用网络请求时，删除缓存数据
                CacheDataStorage.getInstance().removeCacheFromStorage(key);
                MainRequestAction.requestAimItemDatas(context,aimTypeId,key);
            }

            @Override
            public void requestFromCache() {
                MainCacheAction.requestAimItemDatas(key);
            }

            @Override
            public void requestFromDatabase() {
                MainDatabaseAction.requestAimItemDatas(context,aimTypeId);
            }

            @Override
            public void requestAuto() {
                MainCacheAction.requestCommonCacheData(key, new CacheDataStorageCallBack() {
                    @Override
                    public void complete(BaseResult result) {
                        if (result != null && result.checkRequestSuccess()){
                            EventPostUtil.post(new DatappEvent.AimItemEvent(DatappEvent.STATUS_SUCCESS, result));
                        }else {
                            MainRequestAction.requestAimItemDatas(context,aimTypeId,key);
                        }
                    }
                });
            }

            @Override
            public void requestBoth() {
                MainCacheAction.requestCommonCacheData(key, new CacheDataStorageCallBack() {
                    @Override
                    public void complete(BaseResult result) {
                        if (result != null && result.checkRequestSuccess()){
                            EventPostUtil.post(new DatappEvent.AimItemEvent(DatappEvent.STATUS_SUCCESS, result));
                        }
                        if (!CPNetworkUtil.isNetworkAvailable(context)) {
                            MainRequestAction.requestAimItemDatas(context,aimTypeId,key);
                        }
                    }
                });
            }
        });
    }
    public static void requestAimItemDatas(Context context,long aimTypeId){
        requestAimItemDatas(context,aimTypeId, DatappConfig.DEFAULT_REQUEST_MODEL);
    }

}
