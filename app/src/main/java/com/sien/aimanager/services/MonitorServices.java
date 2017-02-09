package com.sien.aimanager.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.sien.aimanager.control.GeneratePeroidAimUtils;
import com.sien.aimanager.receiver.MonitorAlarmReceiver;

/**
 * @author sien
 * @date 2017/2/9
 * @descript 监听服务
 *
 * 用于检查是否触发创建目标机制，常驻内存
 */
public class MonitorServices extends Service {
    private AlarmManager am;   //定时触发检查机制
    private PendingIntent sender;

    private Context mcontext;

    private Binder mBinder = null;

    @Override
    public void onCreate() {
        super.onCreate();

        mcontext = this;
        mBinder = new LocalBinder();

        GeneratePeroidAimUtils.initLastCheckDate(mcontext);

        launchLocationAlarm();
    }


    @Override
    public void onDestroy() {
        if(am != null && sender != null){
            am.cancel(sender);
        }
        am = null;
        sender = null;
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        CPLogUtil.logDebug("onStartCommand-------------->");
//        GeneratePeroidAimUtils.checkGenerateMechanism(mcontext);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class LocalBinder extends Binder {
        public MonitorServices getService(){
            return MonitorServices.this;
        }
    }

    /**
     * 启动定时器
     * */
    private void launchLocationAlarm(){
        startAlarmCheckMechanism();
    }

    /**
     * 触发创建检测机制
     */
    private void startAlarmCheckMechanism(){
        am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(mcontext, MonitorAlarmReceiver.class);
        intent.setAction("com.sien.aimanager");

        sender = PendingIntent.getBroadcast(mcontext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), GeneratePeroidAimUtils.timeInterval, sender);
    }

}
