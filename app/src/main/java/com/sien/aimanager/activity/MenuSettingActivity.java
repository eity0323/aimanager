package com.sien.aimanager.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sien.aimanager.R;
import com.sien.aimanager.adapter.MenuSettingAdapter;
import com.sien.aimanager.config.AppConfig;
import com.sien.aimanager.control.UpdateManager;
import com.sien.aimanager.model.IMenuSettingViewModel;
import com.sien.aimanager.presenter.MenuSettingPresenter;
import com.sien.lib.baseapp.BaseApplication;
import com.sien.lib.baseapp.activity.CPBaseBoostActivity;
import com.sien.lib.baseapp.adapter.CPBaseRecyclerAdapter;
import com.sien.lib.baseapp.widgets.recyclerview.CPDividerItemDecoration;
import com.sien.lib.datapp.config.DatappConfig;
import com.sien.lib.datapp.control.CPSharedPreferenceManager;
import com.sien.lib.datapp.control.DataCleanManager;
import com.sien.lib.datapp.utils.CPDeviceUtil;
import com.sien.lib.datapp.utils.CPFileUtils;
import com.sien.lib.datapp.utils.CPNetworkUtil;
import com.sien.lib.datapp.utils.CPStringUtil;

/**
 * Name: MenuSettingActivity
 * Author: sien
 * Email:
 * Comment: //应用设置页
 * Date: 2016-07-25 17:13
 */
public class MenuSettingActivity extends CPBaseBoostActivity implements IMenuSettingViewModel {

    private MenuSettingPresenter presenter;

    private RecyclerView recyclerView;
    private MenuSettingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_setting);

    }

    @Override
    public void initViews() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("配置");
        }

        recyclerView = findView(R.id.settingList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        CPDividerItemDecoration itemDecoration = new CPDividerItemDecoration(this,CPDividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public void initial() {
        super.initial();

        presenter = getPresenter();

        adapter = new MenuSettingAdapter(recyclerView,presenter.getDatasource());
        adapter.setOnItemClickListener(new CPBaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object data, int position) {
                if (position == 0){
                    cleanCacheConfirmPanel();
                }else if (position == 1){
                    setDefaultRequestSource();
                }else if (position == 2){
                    checkAndPrepareUpgrade();
                }else if (position == 3){
                    changeEnvironment();
                }else if (position == 4){
                    switchLog();
                }else if (position == 5){
                    //密码
                }else if (position == 6){
                    go2AboutActivity();
                }
            }
        });
        recyclerView.setAdapter(adapter);

        showContentLayout();
    }

    private void go2AboutActivity(){
        startActivity(new Intent(this,AboutActivity.class));
    }

    /*切换打印日志*/
    private void switchLog(){
        DatappConfig.IS_DEV = !DatappConfig.IS_DEV;

        presenter.changeLogStatus();
        refreshAdapterDisplay();
    }

    /*刷新显示*/
    private void refreshAdapterDisplay(){
        adapter.refresh(presenter.getDatasource());
        adapter.notifyDataSetChanged();
    }

    /*切换网络环境*/
    private void changeEnvironment(){
        Intent intent = new Intent(this,RequestSourceActivity.class);
        intent.putExtra("entryModel",2);
        startActivityForResult(intent,AppConfig.REQUEST_CODE_ENVIRONMENT);
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

        presenter.changeCacheData();
        refreshAdapterDisplay();
    }

    /*设置默认请求方式*/
    private void setDefaultRequestSource(){
        startActivityForResult(new Intent(this,RequestSourceActivity.class),AppConfig.REQUEST_CODE_DATE_SOURCE);
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
        if (requestCode == AppConfig.REQUEST_CODE_ENVIRONMENT && resultCode == AppConfig.REQUEST_CODE_ENVIRONMENT){
            presenter.changeNetworkEnv();
            presenter.changeCacheData();
            refreshAdapterDisplay();
        }else if (requestCode == AppConfig.REQUEST_CODE_DATE_SOURCE && resultCode == AppConfig.REQUEST_CODE_DATE_SOURCE){
            presenter.changeRequestSource();
            refreshAdapterDisplay();
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
