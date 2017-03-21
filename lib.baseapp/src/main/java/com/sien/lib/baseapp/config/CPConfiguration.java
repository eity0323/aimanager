package com.sien.lib.baseapp.config;

import android.content.Context;

import com.sien.lib.databmob.utils.CPFileUtils;

import java.io.File;

/**
 * @author sien
 * @date 2017/1/22
 * @descript 基类配置文件
 */
public class CPConfiguration {
    //public
    public static boolean USING_PASSWORD = false;//是否开启密码

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
