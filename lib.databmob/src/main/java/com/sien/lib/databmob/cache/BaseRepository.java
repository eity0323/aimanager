package com.sien.lib.databmob.cache;

import android.content.Context;

import com.sien.lib.databmob.utils.CPNetworkUtil;

/**
 * @author sien
 * @date 2016/11/16
 * @descript 基类Repository
 */
public class BaseRepository {
    public static int REQUEST_ONLEY_NETWORK = 1;//使用网络数据
    public static int REQUEST_ONLEY_CACHE = 2;//使用缓存数据
    public static int REQUEST_ONLEY_DATABASE = 16;//使用本地数据库数据
    public static int REQUEST_AUTO = 4;//根据网络情况使用数据（默认）,过网络判断之后，进行缓存优先读数据，缓存没数据，则从网络读取
    public static int REQUEST_BOTH = 8;//先从缓存读取数据显示，然后再同步网络数据

    /**
     * 中央统一请求（请求方式判断）
     * @param context
     * @param requestSource
     * @param operation
     */
    public static void centralRequest(Context context,int requestSource,BaseRepositoryOperation operation){
        if ((requestSource & REQUEST_AUTO) == REQUEST_AUTO ){
            requestSource = checkAutoRequestSource(context);
        }

        //经过网络判断之后，进行缓存优先读数据，缓存没数据，则从网络读取
        if ( (requestSource & REQUEST_AUTO) == REQUEST_AUTO ){
           if (operation != null){
               operation.requestAuto();
           }
            return;
        }

        //firstly get data from cache then sync data from network
        if((requestSource & REQUEST_BOTH) == REQUEST_BOTH){
            if (operation != null){
                operation.requestBoth();
            }
            return;
        }

        //get data from datastorage
        if ((requestSource & REQUEST_ONLEY_CACHE) == REQUEST_ONLEY_CACHE ) {
            if (operation != null){
                operation.requestFromCache();
            }
            return;
        }

        if ((requestSource & REQUEST_ONLEY_NETWORK) == REQUEST_ONLEY_NETWORK ){
            //get data from network
            if (operation != null){
                operation.requestFromNetwork();
            }
            return;
        }

        if ((requestSource & REQUEST_ONLEY_DATABASE) == REQUEST_ONLEY_DATABASE){
            if (operation != null){
                operation.requestFromDatabase();
            }
            return;
        }
    }

    /**
     * 自动判断请求数据方式（无网络时从缓存中取数据，有网络时，优先取缓存数据，无缓存数据时从网络获取数据）
     * @param context
     * @return
     */
    private static int checkAutoRequestSource(Context context){
        int requestSource = REQUEST_AUTO;
        if (!CPNetworkUtil.isNetworkAvailable(context)){
            requestSource = REQUEST_ONLEY_CACHE;
        }
        return requestSource;
    }

    public interface BaseRepositoryOperation{
        /**
         * get data from network
         */
        public void requestFromNetwork();

        /**
         * get data from datastorage
         */
        public void requestFromCache();

        /**
         * get data from database
         */
        public void requestFromDatabase();

        /**
         * 经过网络判断之后，进行缓存优先读数据，缓存没数据，则从网络读取
         */
        public void requestAuto();

        /**
         * firstly get data from cache then sync data from network
         */
        public void requestBoth();
    }
}
