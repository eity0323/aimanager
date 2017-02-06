package com.sien.lib.baseapp.utils;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.sien.lib.baseapp.R;

/**
 * @author sien
 * @date 2016/10/11
 * @descript ImageLoader显示配置工具类
 */
public class CPDisplayImageOptionsUtils {
    private static DisplayImageOptions displayImageOptions;

    /**
     * 缓存配置
     * @return
     */
    public static DisplayImageOptions getImageLoaderCachedDisplayOptions(){
        if (displayImageOptions == null){
            displayImageOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true) // default
                    .cacheOnDisk(true) // default
                    .considerExifParams(true) // default
                    .imageScaleType(ImageScaleType.NONE) // default
                    .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                    .build();
        }

        return displayImageOptions;
    }

    /**
     * 不缓存配置
     * @return
     */
    public static DisplayImageOptions getImageLoaderScaledDisplayOptions(){
//        EXACTLY :图像将完全按比例缩小的目标大小
//        EXACTLY_STRETCHED:图片会缩放到目标大小完全
//        IN_SAMPLE_INT:图像将被二次采样的整数倍
//        IN_SAMPLE_POWER_OF_2:图片将降低2倍，直到下一减少步骤，使图像更小的目标大小
//        NONE:图片不会调整
        if (displayImageOptions == null){
            displayImageOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(false) // default
                    .cacheOnDisk(false) // default
                    .considerExifParams(true) // default
                    .imageScaleType(ImageScaleType.NONE) // default
                    .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                    .build();
        }

        return displayImageOptions;
    }

    /**
     * 缓存圆角头像配置（默认失败占位图）
     * @return
     */
    public static DisplayImageOptions getImageLoaderCachedDisplayOptionsWithAvatarImage(){
        if (displayImageOptions == null){
            displayImageOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true) // default
                    .cacheOnDisk(true) // default
                    .considerExifParams(true) // default
                    .imageScaleType(ImageScaleType.NONE) // default
                    .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                    .showImageForEmptyUri(R.drawable.place_holder_gray_oval)    //url爲空會显示该图片，自己放在drawable里面的
                    .showImageOnFail(R.drawable.place_holder_gray_oval)// 加载失败显示的图片
                    .displayer(new RoundedBitmapDisplayer(10))
                    .build();
        }

        return displayImageOptions;
    }

    /**
     * 缓存文件配置（默认失败占位图）
     * @return
     */
    public static DisplayImageOptions getImageLoaderCachedDisplayOptionsWithFileImage(){
        if (displayImageOptions == null){
            displayImageOptions = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.placeholder)    //url爲空會显示该图片，自己放在drawable里面的
                    .showImageOnFail(R.drawable.placeholder)// 加载失败显示的图片
                    .cacheInMemory(true) // default
                    .cacheOnDisk(true) // default
                    .considerExifParams(true) // default
                    .imageScaleType(ImageScaleType.NONE) // default
                    .bitmapConfig(Bitmap.Config.ARGB_8888) // default  RGB_565
                    .considerExifParams(false)
                    .build();
        }

        return displayImageOptions;
    }
}
