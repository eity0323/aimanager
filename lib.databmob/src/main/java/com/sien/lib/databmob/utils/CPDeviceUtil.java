package com.sien.lib.databmob.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * @author sien
 * @date 2016/10/12
 * @descript 设备工具类
 */
public class CPDeviceUtil {
    /**
     * 获取sdk版本号
     * @return
     */
    public static int getSDKVersionNumber() {
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(Build.VERSION.SDK_INT);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }

        return sdkVersion;

    }

    /**
     * 获取手机型号  eg：OPPO R9m
     * @return
     */
    public static String getMobileModel(){
        return Build.MODEL;
    }

    public static String getProduct(){
        return Build.PRODUCT;
    }

    /**
     * 获取手机系统版本号 eg:5.1
     * @return
     */
    public static String getSystemVersion(){
        return Build.VERSION.RELEASE;
    }

    /**
     *
     * @return
     */
    public static String getDeviceDesign(){
        return Build.DEVICE;
    }

    /**
     * 厂商型号
     * @return
     */
    public static String getManufactory(){
        return Build.MANUFACTURER;
    }

    /**
     * 序列码
     * @return
     */
    public static String getSerial(){
        return Build.SERIAL;
    }

    public static UUID getDeviceId(Context context){
        return new DeviceUuidFactory(context).getDeviceUuid();
    }

    /**
     * 应用唯一标识
     * @param context
     * @return
     */
    public static String getAppUniqueId(Context context){
        return Installation.id(context);
    }

    /**
     * 品牌 eg:Meizu
     * @return
     */
    public static String getBrand(){
        return Build.BRAND;
    }

    //app relate
    /**
     * 获取版本名
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "0.0";
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 检测sdcard
     * @return
     */
    public static boolean hasSdcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取包名
     * @param context
     * @return
     */
    public static String getPackageName(Context context){
        String pkName = context.getPackageName();
        return pkName;
    }

    /**
     * sd剩余空间
     * @return
     */
    public long getSDFreeSize(){
        if (CPDeviceUtil.hasSdcard()){
            return 0;
        }
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        //返回SD卡空闲大小
        //return freeBlocks * blockSize;  //单位Byte
        //return (freeBlocks * blockSize)/1024;   //单位KB
        return (freeBlocks * blockSize)/1024 /1024; //单位MB
    }

    /**
     * SD卡总容量
     * @return
     */
    public long getSDAllSize(){
        if (CPDeviceUtil.hasSdcard()){
            return 0;
        }
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //获取所有数据块数
        long allBlocks = sf.getBlockCount();
        //返回SD卡大小
        //return allBlocks * blockSize; //单位Byte
        //return (allBlocks * blockSize)/1024; //单位KB
        return (allBlocks * blockSize)/1024/1024; //单位MB
    }

    /**
     * 获取APK包签名
     * @param context
     * @param pkgName
     * @return
     * @return String
     */
    public static String getSign(Context context, String pkgName) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
        Iterator<PackageInfo> iter = apps.iterator();
        while(iter.hasNext()) {
            PackageInfo packageinfo = iter.next();
            String packageName = packageinfo.packageName;
            if (packageName.equals(pkgName)) {
                return packageinfo.signatures[0].toCharsString();
            }
        }
        return null;
    }

    /**
     * 获取电量百分比
     * @param context
     * @return
     */
    public static int getBatteryPercent(Context context){
        int level = -1; // percentage, or -1 for unknown

        Intent batteryInfoIntent = context.getApplicationContext().registerReceiver( null,new IntentFilter( Intent.ACTION_BATTERY_CHANGED ) ) ;
        if (batteryInfoIntent != null){
            int rawlevel = batteryInfoIntent.getIntExtra("level", -1);
            int scale = batteryInfoIntent.getIntExtra("scale", -1);
            if (rawlevel >= 0 && scale > 0) {
                level = (rawlevel * 100) / scale;
            }
        }
        return level;
    }

    /**
     * 获取网卡地址
     * @return 网卡地址
     */
    public static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = null;
        try {
            info = wifi.getConnectionInfo();
            return info.getMacAddress();
        } catch (Exception e) {
            CPLogUtil.logError("getLocalMacAddress", e.toString());
        }

        return null;
    }

    /**
     * 读取manifest.xml中application标签下的配置项，如果不存在，则返回空字符串
     * @param key 键名
     * @return 字符串类型配置信息
     */
    public static String getConfigString(Context context, String key) {
        String val = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            val = appInfo.metaData.getString(key);
        } catch (Exception e) {
            CPLogUtil.logError("getConfigString", e.toString());
        }
        return val;
    }

    /**
     * 读取manifest.xml中application标签下的配置项
     * @param key 键名
     * @return 整形配置信息
     */
    public static int getConfigInt(Context context, String key) {
        int val = 0;
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            val = appInfo.metaData.getInt(key);
        } catch (PackageManager.NameNotFoundException e) {
            CPLogUtil.logError("getConfigInt", e.toString());
        }
        return val;
    }

    /**
     * 读取manifest.xml中application标签下的配置项
     * @param key 键名
     * @return 布尔类型配置信息
     */
    public static boolean getConfigBoolean(Context context, String key) {
        boolean val = false;
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            val = appInfo.metaData.getBoolean(key);
        } catch (PackageManager.NameNotFoundException e) {
            CPLogUtil.logError("getConfigBoolean", e.toString());
        }
        return val;
    }


    /** 是否为中文环境 */
    public static boolean isChineseLocale(Context context) {
        try {
            Locale locale = context.getResources().getConfiguration().locale;
            if ((Locale.CHINA.equals(locale)) || (Locale.CHINESE.equals(locale)) || (Locale.SIMPLIFIED_CHINESE.equals(locale)) || (Locale.TAIWAN.equals(locale))){
                return true;
            }
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    //-----------------------------------------------------------------------------------------apk 安装
    /**
     * 先判断是否安装，已安装则启动目标应用程序，否则先安装
     * @param packageName 目标应用安装后的包名
     * @param appPath 目标应用apk安装文件所在的路径
     * @author zuolongsnail
     */
    public static void launchApp(Context context,String packageName, String appPath) {
        // 启动目标应用
        if (isInstallByread(packageName)) {
            // 获取目标应用安装包的Intent
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(
                    packageName);
            context.startActivity(intent);
        }
        // 安装目标应用
        else {
            Intent intent = new Intent();
            // 设置目标应用安装包路径
            intent.setDataAndType(Uri.fromFile(new File(appPath)),
                    "application/vnd.android.package-archive");
            context.startActivity(intent);
        }
    }

    /** 判断是否安装或覆盖安装的类型*/
    private static final int NOTINSTALL = 0;    // 未安装
    private static final int INSTALLED = 1;     // 已安装且为新版本
    private static final int OLDVERSION = 2;    // 已安装但为旧版本
    /**
     * 判断应用是否安装或者是否为最新版本
     * @param packageName 目标应用安装后的包名
     * @param versionCode 指定的应用版本号
     * @return 安装的类型
     * @author zuolongsnail
     */
    private static int isInstallByread(Context context,String packageName, int versionCode){
        // 判断是否安装
        if(new File("/data/data/" + packageName).exists()){
            // 获取系统中安装的所有应用包名集合
            List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
            for(int i=0;i<packages.size();i++){
                PackageInfo packageInfo = packages.get(i);
                // 找出指定的应用
                if(packageName.equals(packageInfo.packageName)){
                    if(packageInfo.versionCode >= versionCode){
                        return INSTALLED;
                    }else{
                        return OLDVERSION;
                    }
                }
            }
        }
        return NOTINSTALL;
    }

    /**
     * 判断是否安装目标应用
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     * @author zuolongsnail
     */
    private static boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }
}
