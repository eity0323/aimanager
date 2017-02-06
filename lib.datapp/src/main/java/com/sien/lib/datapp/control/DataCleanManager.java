package com.sien.lib.datapp.control;

import android.content.Context;
import android.text.TextUtils;

import com.sien.lib.datapp.utils.CPDeviceUtil;

import java.io.File;

/**
 * @author sien
 * @date 2016/10/20
 * @descript 主要功能有清除内/外缓存，清除数据库，清除sharedPreference，清除files和清除自定义目录
 */

public class DataCleanManager {

    /** * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * * @param context */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /** * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * * @param context */
    public static void cleanDatabases(Context context) {
        deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/databases"));
    }

    /**
     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) * * @param
     * context
     */
    public static void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/shared_prefs"));
    }

    /** * 按名字清除本应用数据库 * * @param context * @param dbName */
    public static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }

    /** * 清除/data/data/com.xxx.xxx/files下的内容 * * @param context */
    public static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache) * * @param
     * context
     */
    public static void cleanExternalCache(Context context) {
        if (CPDeviceUtil.hasSdcard()) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /**
     * * 清除外部[type]下的内容(/mnt/sdcard/Android/data/com.xxx.xxx/files/[type]) * * @param
     * context
     */
    public static void cleanExternalFile(Context context,String type) {
        if (CPDeviceUtil.hasSdcard()) {
            deleteFilesByDirectory(context.getExternalFilesDir(type));
        }
    }

    /** * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * * @param filePath */
    public static void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }

    /** * 清除本应用所有的数据 * * @param context * @param filepath */
    public static void cleanApplicationData(Context context, String... filepath) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanDatabases(context);
        cleanSharedPreference(context);
        cleanFiles(context);
        for (String filePath : filepath) {
            cleanCustomCache(filePath);
        }
    }

    /** * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    /**
     * 递归删除指定文件名文件和文件夹
     * @param filePath
     */
    public static void deleteFielOrDirectoryByPath(String filePath){
        if (!TextUtils.isEmpty(filePath)){
            File file = new File(filePath);
            deleteFileOrDirectoryByFile(file);
        }
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file
     * 要删除的根目录
     */
    public static void deleteFileOrDirectoryByFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    deleteFileOrDirectoryByFile(f);
                }
                file.delete();
            }
        }
    }

}
