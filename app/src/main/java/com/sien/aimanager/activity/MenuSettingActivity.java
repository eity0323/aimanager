package com.sien.aimanager.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.sien.aimanager.R;
import com.sien.aimanager.config.AppConfig;
import com.sien.aimanager.control.UpdateManager;
import com.sien.aimanager.model.IMenuSettingViewModel;
import com.sien.aimanager.presenter.MenuSettingPresenter;
import com.sien.lib.baseapp.BaseApplication;
import com.sien.lib.baseapp.activity.CPBaseActivity;
import com.sien.lib.datapp.utils.CPStringUtil;
import com.sien.lib.baseapp.utils.RepeatClickUtil;
import com.sien.lib.datapp.cache.BaseRepository;
import com.sien.lib.datapp.config.DatappConfig;
import com.sien.lib.datapp.control.CPSharedPreferenceManager;
import com.sien.lib.datapp.control.DataCleanManager;
import com.sien.lib.datapp.utils.CPDeviceUtil;
import com.sien.lib.datapp.utils.CPFileUtils;
import com.sien.lib.datapp.utils.CPNetworkUtil;

/**
 * Name: MenuSettingActivity
 * Author: sien
 * Email:
 * Comment: //应用设置页
 * Date: 2016-07-25 17:13
 */
public class MenuSettingActivity extends CPBaseActivity implements IMenuSettingViewModel {
    private final int ENVIRONMENT_REQUEST_CODE = 2233;
    private final int SOURCE_REQUEST_CODE = 3344;
    private Button versionBtn,envBtn,cacheBtn,requestBtn,switchLogBtn;
    private MenuSettingPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_setting);

        cacheBtn = findView(R.id.clear_cache_btn);
        cacheBtn.setOnClickListener(clickListener);

        envBtn = findView(R.id.change_environment_btn);
        requestBtn = findView(R.id.set_default_request_btn);
        requestBtn.setOnClickListener(clickListener);

        envBtn.setOnClickListener(clickListener);

        switchLogBtn = findView(R.id.switch_log_btn);
        switchLogBtn.setOnClickListener(clickListener);

        versionBtn = findView(R.id.set_new_version_btn);
        String res = String.format(getString(R.string.setting_new_version), CPDeviceUtil.getVersionName(this));
        versionBtn.setText(res);
        versionBtn.setOnClickListener(clickListener);

        displayCacheSizeText();
        displayEnvironmentText();
        displayRequestSourceText();
        displayLogText();
    }

    @Override
    public void initViews() {
        getSupportActionBar().setTitle("配置");
    }

    @Override
    public void initial() {
        super.initial();

        presenter = getPresenter();
    }

    private void displayRequestSourceText(){
        String temp = "缓存优先";
        if (DatappConfig.DEFAULT_REQUEST_MODEL == BaseRepository.REQUEST_ONLEY_CACHE){
            temp = "仅缓存";
        }else if (DatappConfig.DEFAULT_REQUEST_MODEL == BaseRepository.REQUEST_ONLEY_NETWORK){
            temp = "仅网络";
        }else if (DatappConfig.DEFAULT_REQUEST_MODEL == BaseRepository.REQUEST_BOTH) {
            temp = "先缓存再网络";
        }

        String res = String.format(getString(R.string.setting_current_request),temp);
        requestBtn.setText(res);
    }

    private void displayCacheSizeText(){
        try {
            String cacheSize = CPFileUtils.getTotalCacheSize(this);

            cacheBtn.setText(getString(R.string.setting_clear_cache) + "(" + cacheSize + ")");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void displayLogText(){
        String res = getString(R.string.setting_switch_log);
        if (DatappConfig.IS_DEV){
            res += "(开)";
        }else {
            res += "(关)";
        }
        switchLogBtn.setText(res);
    }

    /*显示网络环境版本*/
    private void displayEnvironmentText(){
        String temp = "开发环境";
        if (DatappConfig.enviromentType == DatappConfig.ENV_DEVELOP){
            temp = "测试环境";
        }else if (DatappConfig.enviromentType == DatappConfig.ENV_OFFICAL){
            temp = "正式环境";
        }

        String res = String.format(getString(R.string.setting_current_environment),temp);
        envBtn.setText(res);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vid = v.getId();

            if (vid == R.id.set_new_version_btn){
                if (!RepeatClickUtil.isRepeatClick()) {
                    checkAndPrepareUpgrade();
                }
            }else if (vid == R.id.set_default_request_btn){
                if (!RepeatClickUtil.isRepeatClick()) {
                    setDefaultRequestSource();
                }
            }else if (vid == R.id.clear_cache_btn){
                if (!RepeatClickUtil.isRepeatClick()) {
                    cleanCacheConfirmPanel();
                }
            }else if (vid == R.id.change_environment_btn){
                if (!RepeatClickUtil.isRepeatClick()) {
                    changeEnvironment();
                }
            }else if (vid == R.id.switch_log_btn){
                if (!RepeatClickUtil.isRepeatClick()){
                    switchLog();
                }
            }
        }
    };

    /*切换打印日志*/
    private void switchLog(){
        DatappConfig.IS_DEV = !DatappConfig.IS_DEV;

        displayLogText();
    }

    /*切换网络环境*/
    private void changeEnvironment(){
        Intent intent = new Intent(this,RequestSourceActivity.class);
        intent.putExtra("entryModel",2);
        startActivityForResult(intent,ENVIRONMENT_REQUEST_CODE);
    }

    /*检查并准备更新*/
    private void checkAndPrepareUpgrade(){

        boolean wifi = CPNetworkUtil.isWifiValid(this);
        if (wifi){
            //wifi情况下直接校验安装
            requestVersionCheck();
        }else {
            boolean netValide = CPNetworkUtil.isNetworkAvailable(this);
            if (!netValide){
                showToast("当前网络异常，请检查网络状况");
                return;
            }
            AlertDialog confirmUpgrade = new AlertDialog.Builder(this).setMessage("当前不在WIFI条件下，是否继续更新？").setPositiveButton("继续", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    requestVersionCheck();
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).create();
            confirmUpgrade.show();
        }
    }

    /*低电量校验*/
    private void checkLowBattery(){
        int batteryLevel = CPDeviceUtil.getBatteryPercent(this);
        if (batteryLevel < 30){
            AlertDialog confirmBattery= new AlertDialog.Builder(this).setMessage("当前电量低于30%，是否继续更新？").setPositiveButton("继续", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    checkAndPostAppUpgradeEvent();
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).create();
            confirmBattery.show();
        }else {
            //直接更新
            checkAndPostAppUpgradeEvent();
        }
    }

    /*校验版本*/
    private void requestVersionCheck(){
        if (presenter != null) {
            presenter.requestCheckVersion();
        }
    }

    private void cleanCacheConfirmPanel(){
        AlertDialog confirmClean = new AlertDialog.Builder(this).setMessage("确认清除缓存？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cleanCacheData();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create();
        confirmClean.show();
    }

    /*清楚缓存*/
    private void cleanCacheData(){
        CPSharedPreferenceManager.getInstance(getApplicationContext()).clean();
        getSharedPreferences("cookie", Context.MODE_PRIVATE).edit().clear().commit();
        DataCleanManager.cleanSharedPreference(getApplicationContext());
        DataCleanManager.cleanExternalCache(getApplicationContext());
        DataCleanManager.cleanInternalCache(getApplicationContext());
        //删除本地缓存文件夹 /mnt/sdcard/Android/data/[app package]/cache
        DataCleanManager.deleteFielOrDirectoryByPath(CPFileUtils.getAppCacheRootFilePath(getApplicationContext()));

        displayCacheSizeText();
    }

    /*设置默认请求方式*/
    private void setDefaultRequestSource(){
        startActivityForResult(new Intent(this,RequestSourceActivity.class),SOURCE_REQUEST_CODE);
    }

    /*校验并发送升级事件*/
    private void checkAndPostAppUpgradeEvent(){
        String versionName = CPDeviceUtil.getVersionName(this);
        if (CPStringUtil.boostCompare(presenter.getVersionCheckVO().getVersionNumber(),versionName)) {        //最新版比本地版大
            BaseApplication.setSharePerfence(AppConfig.PRESH_CONFIG_UPGRADE_MIN_SUPPORTED_VERSION,presenter.getVersionCheckVO().getMinUpgradeVersion());
            BaseApplication.setSharePerfence(AppConfig.PRESH_CONFIG_UPGRADE_VERSION_NAME,presenter.getVersionCheckVO().getVersionNumber());
            BaseApplication.setSharePerfence(AppConfig.PRESH_CONFIG_UPGRADE_APK_DOWNLOAD_URL,presenter.getVersionCheckVO().getFileId());
            BaseApplication.setSharePerfence(AppConfig.PRESH_CONFIG_UPGRADE_DESC,presenter.getVersionCheckVO().getRemark());
            // 版本升级
            UpdateManager updateManager = new UpdateManager(this, false);
            updateManager.setIgnoreNextCheck(true);//忽略下次再说配置
            updateManager.autoUpdate();

        }else {
            showToast("当前已是最新版");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ENVIRONMENT_REQUEST_CODE && resultCode == ENVIRONMENT_REQUEST_CODE){
            displayEnvironmentText();
            displayCacheSizeText();
        }else if (requestCode == SOURCE_REQUEST_CODE && resultCode == SOURCE_REQUEST_CODE){
            displayRequestSourceText();
        }
    }

    @Override
    public void refreshVersionCheck(boolean success) {
        if (!getActiveStatus()) return;

        if (success){
            checkLowBattery();
        }
    }

    @Override
    public MenuSettingPresenter createPresenter() {
        return new MenuSettingPresenter(this);
    }
}
