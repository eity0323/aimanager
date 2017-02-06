package com.sien.lib.datapp.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.math.BigDecimal;

/**
 * @author sien
 * @date 2016/12/16
 * @descript 文件工具类
 */
public class CPFileUtils {
    /**
     * 应用数据根目录路径 （不推荐使用）
     * /mnt/sdcard
     * @param context
     * @return
     * 应用卸载不能删除数据，需要用户手动触发删除操作(DataCleanManager.deleteFielOrDirectoryByPath or DataCleanManager.deleteFileOrDirectoryByFile)
     */
    public static String getAppRootStorageFilePath(Context context){
        Context appContext = context.getApplicationContext();
        String dir = null;
        if (CPDeviceUtil.hasSdcard()){
            dir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + CPDeviceUtil.getPackageName(appContext);
        }else {
            dir = appContext.getCacheDir().getAbsolutePath() + File.separator + CPDeviceUtil.getPackageName(appContext);
        }
        return dir;
    }

    public static File getAppRootStorageDirectory(Context context){
        String dir = getAppRootStorageFilePath(context);
        File file = new File(dir);
        return file;
    }

    /**
     * 应用缓存根目录路径  (默认)
     * /mnt/sdcard/Android/data/[app package]/cache
     * @param context
     * @return
     * 应用卸载自动删除数据
     */
    public static String getAppCacheRootFilePath(Context context){
        Context appContext = context.getApplicationContext();
        String dir = null;
        if (CPDeviceUtil.hasSdcard()){
            if(appContext.getExternalCacheDir() != null) {
                dir = appContext.getExternalCacheDir().getAbsolutePath();
            }else {
                dir = Environment.getExternalStorageDirectory().getAbsolutePath();
            }
        }else {
            dir = appContext.getCacheDir().getAbsolutePath();
        }
        return dir;
    }

    /**
     * 应用数据根目录
     * /mnt/sdcard/Android/data/[app package]/cache
     * @param context
     * @return
     */
    public static File getAppCacheRootDirectory(Context context){
        String dir = getAppCacheRootFilePath(context);
        File file = new File(dir);
        return file;
    }

    /**
     * 内部应用临时目录
     * /data/data/[app package]/files
     * @param context
     * @return
     */
    public static File getInnerCacheDirectory(Context context){
        Context appContext = context.getApplicationContext();
        return appContext.getCacheDir();
    }


    /**
     * 内部临时缓存路径
     * /data/data/[app package]/cache
     * @return
     */
    public static String getInnerCacheFilePath(Context context){
        Context appContext = context.getApplicationContext();
        String dir = appContext.getCacheDir().getAbsolutePath();
        return dir;
    }

    /**
     * 内部临时文件路径
     * /data/data/[app package]/files
     * @return
     */
    public static String getInnerTempFilePath(Context context){
        Context appContext = context.getApplicationContext();
        String dir = appContext.getFilesDir().getAbsolutePath();
        return dir;
    }

    //-----------------------------------------------------------------------------------------------
    /**
     * 计算缓存大小
     * @param context
     * @return
     * @throws Exception
     */
    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    // 获取文件
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }
}
