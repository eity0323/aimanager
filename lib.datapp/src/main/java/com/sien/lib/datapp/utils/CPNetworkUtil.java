package com.sien.lib.datapp.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

/**
 * @author sien
 * @date 2016/11/2
 * @descript 网络工具类
 */
public class CPNetworkUtil {
    /**
     * 是否有访问网络的权限
     * @param context 上下文
     * @return boolean true:有权限,false:无权限
     */
    public static boolean hasInternetPermission(Context context) {
        if (context != null) {
            return context.checkCallingOrSelfPermission("android.permission.INTERNET") == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    /**
     * 网络是否可用
     * @param context 上下文
     * @return boolean true:网络可用 ,false:网络不可用
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            NetworkInfo info = getActiveNetworkInfo(context);
            return (info != null) && (info.isConnected());
        }

        return false;
    }

    /**
     * WIFI是否有效
     * @param context 上下文
     * @return boolean true:WIFI可用，false:WIFI不可用
     */
    public static boolean isWifiValid(Context context) {
        if (context != null) {
            NetworkInfo info = getActiveNetworkInfo(context);
            return (info != null) && (ConnectivityManager.TYPE_WIFI == info.getType()) && (info.isConnected());
        }

        return false;
    }

    /**
     * 是否使用手机网络
     * @param context 上下文
     * @return boolean true:使用手机网络,false:不是手机网络
     */
    public static boolean isMobileNetwork(Context context) {
        if (context != null) {
            NetworkInfo info = getActiveNetworkInfo(context);

            if (info == null) {
                return false;
            }

            return (info != null) && (info.getType() == ConnectivityManager.TYPE_MOBILE) && (info.isConnected());
        }

        return false;
    }

    /**
     * 获取代表联网状态的NetWorkInfo对象
     * @param context 山下问
     * @return NetworkInfo 网络信息
     */
    public static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            return connectivity.getActiveNetworkInfo();
        } catch (Exception e) {
            CPLogUtil.logError("tsp", e.toString());
        }
        return null;
    }

    /**
     * 获取指定网络类型的NetWorkInfo对象
     * @param context 上下文
     * @param networkType 网络类型
     * @return NetworkInfo 网络对象
     */
    public static NetworkInfo getNetworkInfo(Context context, int networkType) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(networkType);
    }

    /**
     * 获取当前网络类型
     * @param context 上下文
     * @return int 网络类型
     */
    public static int getNetworkType(Context context) {
        if (context != null) {
            NetworkInfo info = getActiveNetworkInfo(context);

            return info == null ? -1 : info.getType();
        }

        return -1;
    }

    /**
     * WIFI状态
     * @param context 上下文
     * @return int WIFI状态
     */
    public static int getWifiState(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        if (wifi == null) {
            return WifiManager.WIFI_STATE_UNKNOWN;
        }

        return wifi.getWifiState();
    }

    /**
     * 获取wifi链接状态
     * @param context 上下文
     * @return DetailedState 连接状态
     */
    public static NetworkInfo.DetailedState getWifiConnectivityState(
            Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context, ConnectivityManager.TYPE_WIFI);
        return networkInfo == null ? NetworkInfo.DetailedState.FAILED
                : networkInfo.getDetailedState();
    }

    /**
     * 连接指定wifi
     * @param context 上下文
     * @param wifiSSID WIFI名称
     * @param password 密码
     * @return boolean true:连接成功，false:连接失败
     */
    public static boolean wifiConnection(Context context, String wifiSSID, String password) {
        boolean isConnection = false;
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String strQuotationSSID = "\"" + wifiSSID + "\"";

        WifiInfo wifiInfo = wifi.getConnectionInfo();
        if ((wifiInfo != null) && ((wifiSSID.equals(wifiInfo.getSSID())) || (strQuotationSSID.equals(wifiInfo.getSSID())))) {
            isConnection = true;
        } else {
            List<ScanResult> scanResults = wifi.getScanResults();
            if ((scanResults != null) && (scanResults.size() != 0)) {
                for (int nAllIndex = scanResults.size() - 1; nAllIndex >= 0; nAllIndex--) {
                    String strScanSSID = ((ScanResult)scanResults.get(nAllIndex)).SSID;
                    if ((wifiSSID.equals(strScanSSID)) || (strQuotationSSID.equals(strScanSSID))) {
                        WifiConfiguration config = new WifiConfiguration();
                        config.SSID = strQuotationSSID;
                        config.preSharedKey = ("\"" + password + "\"");
                        config.status = WifiConfiguration.Status.ENABLED;

                        int nAddWifiId = wifi.addNetwork(config);
                        isConnection = wifi.enableNetwork(nAddWifiId, false);
                        break;
                    }
                }
            }
        }

        return isConnection;
    }

    /*获取手机ip*/
    public static String getLocalIpAddress(Context context)
    {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable()) {
            WifiManager wifimanage = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);// 获取WifiManager
            // 检查wifi是否开启
            if (!wifimanage.isWifiEnabled()) {
            }
            WifiInfo wifiinfo = wifimanage.getConnectionInfo();
            // 将获取的int转为真正的ip地址,参考的网上的，修改了下
            int i = wifiinfo.getIpAddress();
            return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 24) & 0xFF);
        } else if (mobile.isAvailable()) {
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            return inetAddress.getHostAddress().toString();
                        }
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

//    /**
//     * 通过TelephonyManager判断移动网络的类型
//     * @param context
//     * @return
//     */
//    private static boolean isFastMobileNetwork(Context context) {
//        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
//        switch (telephonyManager.getNetworkType()) {
//            case TelephonyManager.NETWORK_TYPE_1xRTT:
//                return false; // ~ 50-100 kbps
//            case TelephonyManager.NETWORK_TYPE_CDMA:
//                return false; // ~ 14-64 kbps
//            case TelephonyManager.NETWORK_TYPE_EDGE:
//                return false; // ~ 50-100 kbps
//            case TelephonyManager.NETWORK_TYPE_EVDO_0:
//                return true; // ~ 400-1000 kbps
//            case TelephonyManager.NETWORK_TYPE_EVDO_A:
//                return true; // ~ 600-1400 kbps
//            case TelephonyManager.NETWORK_TYPE_GPRS:
//                return false; // ~ 100 kbps
//            case TelephonyManager.NETWORK_TYPE_HSDPA:
//                return true; // ~ 2-14 Mbps
//            case TelephonyManager.NETWORK_TYPE_HSPA:
//                return true; // ~ 700-1700 kbps
//            case TelephonyManager.NETWORK_TYPE_HSUPA:
//                return true; // ~ 1-23 Mbps
//            case TelephonyManager.NETWORK_TYPE_UMTS:
//                return true; // ~ 400-7000 kbps
//            case TelephonyManager.NETWORK_TYPE_EHRPD:
//                return true; // ~ 1-2 Mbps
//            case TelephonyManager.NETWORK_TYPE_EVDO_B:
//                return true; // ~ 5 Mbps
//            case TelephonyManager.NETWORK_TYPE_HSPAP:
//                return true; // ~ 10-20 Mbps
//            case TelephonyManager.NETWORK_TYPE_IDEN:
//                return false; // ~25 kbps
//            case TelephonyManager.NETWORK_TYPE_LTE:
//                return true; // ~ 10+ Mbps
//            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
//                return false;
//            default:
//                return false;
//        }
//    }
}
