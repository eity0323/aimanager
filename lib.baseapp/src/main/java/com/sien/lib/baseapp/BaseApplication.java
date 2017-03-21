package com.sien.lib.baseapp;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.sien.lib.baseapp.config.CPConfiguration;
import com.sien.lib.databmob.control.CPSharedPreferenceManager;

import java.util.concurrent.Executors;

/**
 * @author sien
 * @date 2016/9/28
 * @descript 基类application
 */
public class BaseApplication extends Application{
    private static BaseApplication mainApp;

    @Override
    public void onCreate() {
        super.onCreate();

        mainApp = this;

        initImageLoader(getApplicationContext());

//        CacheDataStorage.getInstance().init(this);
    }

    public void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.

        int memClass = ((android.app.ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        memClass = memClass > 32 ? 32 : memClass;
        final int cacheSize = 1024 * 1024 * memClass / 8;

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);

        config.threadPriority(Thread.NORM_PRIORITY - 2)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 MiB
                .taskExecutor(Executors.newFixedThreadPool(3))
                .denyCacheImageMultipleSizesInMemory()
                // 开启十个线程下载图片，默认是3个
                // .threadPoolSize(3)
                // 设置线程池大小，默认为3
                // .denyCacheImageMultipleSizesInMemory()
                // 禁止缓存多份不同大小的图片
                // .threadPriority(Thread.NORM_PRIORITY - 1)
                // 线程优先级，默认
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .memoryCache(new LRULimitedMemoryCache(cacheSize))
                .diskCache(new UnlimitedDiskCache(CPConfiguration.getImageLoaderCacheDirectory(context)));
                // 缓存策略
                // 默认已设为1/8
                // .memoryCacheSize(cacheSize)
                // .memoryCacheSizePercentage(13)//设置最大内存缓存大小
//                .writeDebugLogs();// 输出Debug信息，释放版本的时候，不需要这句

        // Initialize ImageLoader with configuration.
        if (ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().clearMemoryCache();
            ImageLoader.getInstance().destroy();
        }
        ImageLoader.getInstance().init(config.build());
    }

    public static void setSharePerfence(String key, String value) {
        if (null != value) {
            CPSharedPreferenceManager.getInstance(mainApp).saveData(key, value);
        }
    }
    public static String getSharePerfence(String key) {
        String value = CPSharedPreferenceManager.getInstance(mainApp).getData(key);
        if (TextUtils.isEmpty(value)) {
            return "";
        }
        return value;
    }
}
