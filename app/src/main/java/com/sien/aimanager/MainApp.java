package com.sien.aimanager;

import android.content.Intent;
import android.os.Handler;

import com.sien.aimanager.services.MonitorServices;
import com.sien.lib.baseapp.BaseApplication;
import com.sien.lib.databmob.config.DatappConfig;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

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

        initBmobConfig();
    }

    private void initBmobConfig(){
        BmobConfig config =new BmobConfig.Builder(this)
            //设置appkey
            .setApplicationId(DatappConfig.BMOB_APPID)
            //请求超时时间（单位为秒）：默认15s
            .setConnectTimeout(30)
            //文件分片上传时每片的大小（单位字节），默认512*1024
            .setUploadBlockSize(1024*1024)
            //文件的过期时间(单位为秒)：默认1800s
            .setFileExpiration(2500)
            .build();
        Bmob.initialize(config);
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
                startService(new Intent(MainApp.this, MonitorServices.class));
            }
        },200);
    }
}
