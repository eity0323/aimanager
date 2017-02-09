package com.sien.aimanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sien.aimanager.control.GeneratePeroidAimUtils;
import com.sien.lib.datapp.utils.CPLogUtil;

/**
 * @author sien
 * @date 2017/2/9
 * @descript 定时器触发机制
 */
public class MonitorAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        CPLogUtil.logDebug("MonitorAlarmReceiver-------------->");
        //检查触发创建机制
        GeneratePeroidAimUtils.checkGenerateMechanism(context);
    }
}
