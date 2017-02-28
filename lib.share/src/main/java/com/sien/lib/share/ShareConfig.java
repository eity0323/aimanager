package com.sien.lib.share;

/**
 * Name: ShareConfig
 * Author: sien
 * Email:
 * Comment: //分享配置参数
 * Date: 2016-06-13 18:36
 */
public class ShareConfig {
    /**
     * 分享id是否加密
     */
    public static boolean isEncryptByAESForData = true;
    /**
     * 数据加密密钥
     */
    public static final String DB_SECRET_KEY = "com.sien.lib.share";

    /**
     * 分享相关
     *
     */
    public static final String APPKEY = "1b911fcf664d8";// 此字段是在ShareSDK注册的应用所对应的appkey

    /** 朋友圈 */
    public static final String APPID_CIRCLE_FRIEND = "K9cdwPG5D9V+XpuDHi92bgqHN0yVT30q";
    public static final String APPSECRET_CIRCLE_FRIEND = "tzkwAMEbYHnWim0iprnuXLtnd4+n3XV81laIQXa38V2iOx0nFj477g==";
    public static final String BYPASSAPPROVAL_CIRCLE_FRIEND = "false";//是否绕过审核
    public static final String ENABLE_CIRCLE_FRIEND = "true";
    public static final int THUMBNAIL_SIZE = 120;//分享的Url缩略图尺寸

    /** 微信好友 */

    public static final String APPID_WE_CHAT_FRIEND = "K9cdwPG5D9V+XpuDHi92bgqHN0yVT30q";
    public static final String APPSECRET_WE_CHAT_FRIEND = "tzkwAMEbYHnWim0iprnuXLtnd4+n3XV81laIQXa38V2iOx0nFj477g==";

    public static final String BYPASSAPPROVAL_WE_CHAT_FRIEND = "false";//是否绕过审核
    public static final String ENABLE_WE_CHAT_FRIEND = "true";

    public static final String SHARE_APP_TITLE = "目标管理";
}
