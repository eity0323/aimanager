package com.sien.lib.baseapp.config;

import android.content.Context;

import com.sien.lib.datapp.utils.CPFileUtils;

import java.io.File;

/**
 * @author sien
 * @date 2017/1/22
 * @descript 基类配置文件
 */
public class CPConfiguration {
    //public
    public static final String REQUEST_MODEL_KEY = "request_model_key";//数据请求方式
    public static final String ENVIRONMENT_KEY = "environment_key";//网络环境

    //private
    /**
     * 是否使用插件框架
     */
    private static boolean usingPluginFramework = true;

    public static boolean isUsingPluginFramework() {
        return usingPluginFramework;
    }

    public static void setUsingPluginFramework(boolean usingPluginFramework) {
        CPConfiguration.usingPluginFramework = usingPluginFramework;
    }


    //method
    /**
     * 应用数据imageloader缓存目录
     * @param context
     * @return
     */
    public static File getImageLoaderCacheDirectory(Context context){
        String dir = CPFileUtils.getAppCacheRootFilePath(context) + File.separator + "UIL";
        File file = new File(dir);
        return file;
    }
}
