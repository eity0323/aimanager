package com.sien.aimanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sien.aimanager.services.MonitorServices;

/**
 * @author sien
 * @date 2017/2/9
 * @descript 开机检测机制
 */
public class DeviceBoostReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //校验监听服务是否开启，如未开启，则开启服务
        Intent service = new Intent(context, MonitorServices.class);
        context.startService(service);

        //检查触发创建机制(可放到监听服务启动时触发)
//        GeneratePeroidAimUtils.checkGenerateMechanism(context);
    }
}
