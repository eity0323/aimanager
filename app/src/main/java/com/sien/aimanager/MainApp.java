package com.sien.aimanager;

import android.content.Intent;
import android.os.Handler;

import com.sien.aimanager.services.MonitorServices;
import com.sien.lib.baseapp.BaseApplication;

/**
 * @author sien
 * @date 2017/2/5
 * @descript
 */
public class MainApp extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        //启动监听服务 并 延时触发创建检测机制
        startMonitorServiceAndDelayInitialCheckMechanism();
    }

    /**
     * 触发创建检测机制
     */
    private void startMonitorServiceAndDelayInitialCheckMechanism(){
        //启动监听定时创建检查机制(可放到监听服务启动时触发)
        Handler tempHandler = new Handler();
        tempHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                GeneratePeroidAimUtils.checkGenerateMechanism();
                startService(new Intent(MainApp.this, MonitorServices.class));
            }
        },200);
    }
}
