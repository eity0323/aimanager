package com.sien.lib.datapp.network;

import android.content.Context;

import com.sien.lib.datapp.config.DatappConfig;
import com.sien.lib.datapp.network.action.MainRequestApi;
import com.sien.lib.datapp.network.base.CPRetrofitClient;

import retrofit2.Retrofit;

/**
 * @author sien
 * @date 2016/9/28
 * @descript 业务接口client
 */
public class RestClient {
    private static String baseUrl = DatappConfig.APP_BASE_URL;

    private static MainRequestApi gitApiInterface ;

    public static MainRequestApi getClient(Context context) {
        checkNetworkEnvironment();

        if (gitApiInterface == null) {
            synchronized (RestClient.class) {
                if (gitApiInterface != null){
                    return gitApiInterface;
                }

                Retrofit client = CPRetrofitClient.getNetCacheWithCookieRetrofitClient(context,baseUrl);
                gitApiInterface = client.create(MainRequestApi.class);
            }
        }
        return gitApiInterface ;
    }

    /*检查网络环境是否变化*/
    private static void checkNetworkEnvironment(){
        if (!DatappConfig.APP_BASE_URL.equals(baseUrl)){
            baseUrl = DatappConfig.APP_BASE_URL;
            gitApiInterface = null;
        }
    }

    public static void releaseClient(){
        gitApiInterface = null;
    }
}
