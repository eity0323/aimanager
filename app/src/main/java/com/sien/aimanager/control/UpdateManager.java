package com.sien.aimanager.control;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.sien.aimanager.config.AppConfig;
import com.sien.aimanager.R;
import com.sien.lib.baseapp.BaseApplication;
import com.sien.lib.baseapp.control.CPActivityManager;
import com.sien.lib.datapp.utils.CPStringUtil;
import com.sien.lib.datapp.cache.memory.LruCacheManager;
import com.sien.lib.datapp.control.BaseOkHttpFileCallBack;
import com.sien.lib.datapp.control.CPSharedPreferenceManager;
import com.sien.lib.datapp.utils.CPFileUtils;

import java.io.File;
import java.io.PrintWriter;

import retrofit2.Call;

/**
 * [版本更新管理]
 * 
 * @author sien
 * @version 1.0
 * @date 2016-10-23
 * 
 **/
public class UpdateManager {

	@SuppressWarnings("unused")
	private final String tag = UpdateManager.class.getSimpleName();
	private final String showUpdateFlag = "showUpdateFlag";

	private Context mContext;
	private String updateMsg;	//更新说明
	private String latestVersion;//最新版本号
	private String apkUrl;//下载地址
	private boolean canNotCancel = false;//是否可以取消下载提示（建议or强制升级）
	private boolean ignoreNextCheck = false;//当设置了下次再说时，首页进入的检测版本检测忽略，设置页面的版本检测强制执行升级检测

	private AlertDialog noticeDialog;//升级提示框（建议、强制升级）
	private AlertDialog downloadDialog;//下载框
	private AlertDialog versionDialog;//当前最新版本提示框
	private ProgressBar mProgress;//下载进度条

	private BaseOkHttpFileCallBack fileCallBack;

	/**
	 * @param context
	 */
	public UpdateManager(Context context, boolean ignoreNextCheck) {
		mContext = context;
		this.ignoreNextCheck = ignoreNextCheck;

		String rootPath = CPFileUtils.getAppCacheRootDirectory(context) + "/download/";
		String fileNameWithPatch = context.getPackageName().replaceAll("\\.","_");

		fileCallBack = new BaseOkHttpFileCallBack(rootPath, fileNameWithPatch) {
			@Override
			public void onProgress(float progress, long total) {
				try {
					if(mProgress != null){
						mProgress.setProgress((int) progress);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFail(Call call, Exception e) {
				if (downloadDialog != null) {
					downloadDialog.dismiss();
				}
			}

			@Override
			public void onSuccess(File response) {
				if (downloadDialog != null) {
					downloadDialog.dismiss();
				}

				if (!response.exists()) {
					return;
				}
				//安装并打开应用
//				// 判断root权限
//				if (isRoot()) {
//					// 有root权限，静默安装
//					return apkInstall(path);
//				}else {
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setDataAndType(Uri.parse("file://" + response.toString()), "application/vnd.android.package-archive");
					mContext.startActivity(i);

//				Intent intent = new Intent();
//				intent.setAction("android.intent.action.VIEW");
//				intent.addCategory("android.intent.category.DEFAULT");
//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intent.setDataAndType(Uri.fromFile(response),
//						"application/vnd.android.package-archive");
//				mContext.startActivity(intent);
//				}
			}
		};
	}

	/**
	 * 弹出提示框
	 */
	private void showNoticeDialog() {
		if (TextUtils.isEmpty(updateMsg)) {
			updateMsg = mContext.getString(R.string.more_update_version_hasnew);
		}
		if (canNotCancel) {
			// 如果是强制更新
			noticeDialog = new AlertDialog.Builder(mContext).setMessage(updateMsg).setCancelable(canNotCancel).setPositiveButton(R.string.common_update_must, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (noticeDialog != null) {
						noticeDialog.dismiss();
					}
					showDownloadDialog();
				}
			}).setNegativeButton(R.string.common_exit_app, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					cancelDownload();
					LruCacheManager.getInstance().evictAll();
					//退出页面
					CPActivityManager.getAppManager().appExit();
				}
			}).create();
			noticeDialog.show();

		} else {
			// 建议升级
			noticeDialog = new AlertDialog.Builder(mContext).setMessage(updateMsg).setCancelable(canNotCancel).setPositiveButton(R.string.common_update, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (noticeDialog != null) {
						noticeDialog.dismiss();
					}
					showDownloadDialog();
				}
			}).setNegativeButton(R.string.common_update_next, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 如果是建议更新，则保存该标志
					CPSharedPreferenceManager.getInstance(mContext).saveData(showUpdateFlag, "true");
					if (noticeDialog != null) {
						noticeDialog.dismiss();
					}
				}
			}).create();
			noticeDialog.show();
		}
	}

	/**
	 * 弹出下载dialog
	 */
	private void showDownloadDialog() {
		try {
			View view = LayoutInflater.from(mContext).inflate(R.layout.layout_upgrade, null);
			mProgress = (ProgressBar) view.findViewById(R.id.progress);
			if (canNotCancel) {
				// 清除缓存
				LruCacheManager.getInstance().evictAll();

				downloadDialog = new AlertDialog.Builder(mContext)
						.setTitle(mContext.getString(R.string.more_update_version))
						.setMessage(mContext.getString(R.string.more_update_version_downloading))
//						.setNegativeButton("取消",null)
						.setView(view)
						.setCancelable(true)
						.create();
				downloadDialog.show();
			} else {
				downloadDialog = new AlertDialog.Builder(mContext)
						.setTitle(mContext.getString(R.string.more_update_version))
						.setMessage(mContext.getString(R.string.more_update_version_downloading))
						.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								cancelDownload();
								downloadDialog.dismiss();
							}
						})
						.setView(view)
						.create();
				downloadDialog.show();
			}

			DownloadUtils.getInstance().ApkDownload(apkUrl,mContext,fileCallBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void cancelDownload(){
		//TODO 取消请求
		DownloadUtils.getInstance().cancelDownload();
	}

	public void setIgnoreNextCheck(boolean ignoreNextCheck) {
		this.ignoreNextCheck = ignoreNextCheck;
	}

	/**
	 * 自动更新
	 * 
	 */
	public void autoUpdate() {
		try {
			updateMsg = AppConfig.getUpgradeDesc();
			latestVersion = AppConfig.getUpgradeVersionName();
			apkUrl = AppConfig.getUpgradeApkDownloadUrl();

			if (TextUtils.isEmpty(apkUrl)){
				//下载地址为空
				return;
			}

			apkUrl = CPStringUtil.remediImagePath(apkUrl);

			PackageManager packageManager = mContext.getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
//			int versionCode = packInfo.versionCode;
			String versionName = packInfo.versionName;
			String minSupportedVersion = AppConfig.getUpgradeMinSupportedVersionName();

			// 判断是否需要强制升级
			canNotCancel = false;
			if (!TextUtils.isEmpty(minSupportedVersion) && CPStringUtil.boostCompare(minSupportedVersion,versionName)) {// 如果当前版本号低于最小支持的版本号，那么得强制升级
				saveLocalCacheDataItem(AppConfig.PRESH_KEY_UPGRADE_FORCED, "true");
				canNotCancel = true;
			} else {
				saveLocalCacheDataItem(AppConfig.PRESH_KEY_UPGRADE_FORCED, "false");
				canNotCancel = false;
			}

			// ignoreNextCheck=false, 如果是首页进来
			if (!ignoreNextCheck && !canNotCancel) {
				// 检查不再提示 TODO
				String flagStr = CPSharedPreferenceManager.getInstance(mContext).getData(showUpdateFlag);
				boolean showflag = "true".equals(flagStr)?true:false;
				if (showflag) {
					return;
				}
			}

			// 判断是否更新
			if (CPStringUtil.boostCompare(latestVersion,versionName)) {		//最新版比本地版大
				saveLocalCacheDataItem(AppConfig.PRESH_KEY_NEW_UPGRADE, "true");
				showNoticeDialog();
				return;
			}
			saveLocalCacheDataItem(AppConfig.PRESH_KEY_NEW_UPGRADE, "false");
			if (ignoreNextCheck) {
				String vertitle = "当前版本：V" + packInfo.versionName;
				versionDialog = new AlertDialog.Builder(mContext).setTitle(vertitle).setMessage("暂无版本更新").setPositiveButton("确定",null).setCancelable(false).create();
				versionDialog.show();
			}

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void saveLocalCacheDataItem(String key,String value){
		BaseApplication.setSharePerfence(key, value);
	}

	/** 静默安装 */
	private static boolean apkInstall(String path) {
		PrintWriter PrintWriter = null;
		Process process = null;
		try {

			process = Runtime.getRuntime().exec("su");
			PrintWriter = new PrintWriter(process.getOutputStream());
			PrintWriter.println("chmod 777 " + path);
			PrintWriter
					.println("export LD_LIBRARY_PATH=/vendor/lib:/system/lib");
			PrintWriter.println("pm install -r " + path);
			PrintWriter.flush();
			PrintWriter.close();
			int value = process.waitFor();
			return value == 0;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (process != null) {
				process.destroy();
			}
		}
		return false;
	}

	/** 判断当前设备是否已经获取到root权限 */
	private static boolean isRoot() {
		PrintWriter PrintWriter = null;
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("su");
			PrintWriter = new PrintWriter(process.getOutputStream());
			PrintWriter.flush();
			PrintWriter.close();
			int value = process.waitFor();
			return value == 0;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (process != null) {
				process.destroy();
			}
		}
		return false;
	}
}
