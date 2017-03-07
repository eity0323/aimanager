package com.sien.aimanager.config;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.sien.lib.baseapp.BaseApplication;
import com.sien.lib.datapp.utils.CPDateUtil;

import java.util.Date;

/**
 * 程序配置信息获取类
 * 
 * @author sien
 * 
 */
public class AppConfig {
	public final static int REQUEST_CODE_SELECTCOVER = 2071;//选择封面图片
	public final static int REQUEST_CODE_ENVIRONMENT= 2081;//切换网络环境
	public final static int REQUEST_CODE_DATE_SOURCE = 2082;//切换数据来源
	public final static int REQUEST_CODE_NEW_AIMTYPE = 2281;//新增目标分类
	public final static int REQUEST_CODE_EDIT_AIMTYPE = 2282;//编辑目标分类
	public final static int RESULT_CODE_OK = 200;//页面回传码ok


	public static final String GESTURE_PASSWORD = "GesturePassword";

	/**
	 * 程序应用版本升级信息
	 */
	public static final String PRESH_CONFIG_UPGRADE_MIN_SUPPORTED_VERSION = "config_upgrade_min_supported_version";
	public static final String PRESH_CONFIG_UPGRADE_VERSION_NAME = "config_upgrade_versionname";
	public static final String PRESH_CONFIG_UPGRADE_DESC = "config_upgrade_desc";
	public static final String PRESH_CONFIG_UPGRADE_APK_DOWNLOAD_URL = "config_upgrade_apk_download_url";

	public static String getUpgradeMinSupportedVersionName() {
		return getConfigDataItem(PRESH_CONFIG_UPGRADE_MIN_SUPPORTED_VERSION);
	}

	public static String getUpgradeVersionName() {
		return getConfigDataItem(PRESH_CONFIG_UPGRADE_VERSION_NAME);
	}

	public static String getUpgradeDesc() {
		return getConfigDataItem(PRESH_CONFIG_UPGRADE_DESC);
	}

	public static String getUpgradeApkDownloadUrl() {
		return getConfigDataItem(PRESH_CONFIG_UPGRADE_APK_DOWNLOAD_URL);
	}

	private static String getConfigDataItem(String key){
		return BaseApplication.getSharePerfence(key);
	}

	/**
	 * 格式化自动创建分类名称
	 * @param oldName
	 * @param date
     * @return
     */
	public static String formatGenerateTypeName(String oldName,Date date){
		String val = oldName;
		if (date != null){
			val += "(" + CPDateUtil.getDateToString(date, "MM-dd") + ")";
		}
		return val;
	}

	/**
	 * 根据名称获取资源id
	 * @param context
	 * @param source
     * @return
     */
	public static int getResIdByString(Context context,String source){
		int resId = View.NO_ID;
		if (context != null && !TextUtils.isEmpty(source)){
			resId = context.getResources().getIdentifier(source,"mipmap",context.getPackageName());
		}
		return resId;
	}

	/**
	 * 是否是第一次使用
	 */
	public static final String PRESH_KEY_FIRST_USE = "press_key_first_use";

	/**
	 * 是否有更新版本
	 */
	public static final String PRESH_KEY_NEW_UPGRADE = "has_new_upgrade";
	/**
	 * 是否为强制更新
	 */
	public static final String PRESH_KEY_UPGRADE_FORCED = "upgrade_forced";

	public static String launcherActivityModel = "AimTypeListActivity";//首页模式

}
