package com.sien.lib.datapp.network.action;

/**
 * @author sien
 * @date 2016/10/27
 * @descript 接口参数
 */
public class RequestApiConfig {
    public final static String ACTION_VERSIONCHECK = "https://api.bmob.cn/1/classes/apkversion";//版本检测
    public final static String ACTION_FEEDBACK = "https://api.bmob.cn/1/classes/feedback";//反馈
    public final static String ACTION_UPLOADFILE = " https://api.bmob.cn/2/files/fileName";//上传图片

    public final static String ACTION_AIM_ITEM = "https://api.bmob.cn/1/classes/aimtype";
    public final static String ACTION_AIM_TYPE = "https://api.bmob.cn/1/classes/aimitem";
    public final static String ACTION_AIM_TYPE_BYID = "https://api.bmob.cn/1/classes/aimtype/{id}";
}
