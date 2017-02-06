package com.sien.lib.baseapp.events;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * @author sien
 * @date 2016/9/26
 * @descript 插件框架or独立应用检查接口
 */
public interface IPluginOrAloneChecker {
    /**
     * 获取插件传递参数
     * @param activity
     * @return
     */
    public Uri pluginOrAloneGetUri(Activity activity);
    /**
     * 打开插件页面
     * @param uriString 插件名称
     * @param context 应用上下文
     */
    public void pluginOrAloneOpenUri(String uriString, Context context);
    public void pluginOrAloneOpenUriForResult(String uriStrig, int requestCode, Activity context);
    public void pluginOrAloneOpenUriForResult(Intent intent, int requestCode, Activity context);
    /**
     * 获取插件intent
     * @param uriString 插件名称
     * @param context 应用上下文
     */
    public Intent pluginOrAloneGetIntentOfUri(String uriString, Context context);
}
