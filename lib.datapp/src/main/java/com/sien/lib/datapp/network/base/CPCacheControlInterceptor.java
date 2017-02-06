package com.sien.lib.datapp.network.base;

import android.content.Context;

import com.sien.lib.datapp.utils.CPNetworkUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author sien
 * @date 2016/10/19
 * @descript 缓存控制拦截器
 */
public class CPCacheControlInterceptor implements Interceptor {
    private Context mContext;
    public CPCacheControlInterceptor(Context context){
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if(!CPNetworkUtil.isNetworkAvailable(mContext)){
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }

        Response originalResponse = chain.proceed(request);
        if(CPNetworkUtil.isNetworkAvailable(mContext)){
            //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
            String cacheControl = request.cacheControl().toString();
            return originalResponse.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build();
        }else{
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                    .removeHeader("Pragma")
                    .build();
        }
    }
}
