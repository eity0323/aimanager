package com.sien.aimanager.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.sien.aimanager.R;
import com.sien.aimanager.activity.MainAimObjectActivity;
import com.sien.aimanager.control.GeneratePeroidAimUtils;
import com.sien.lib.databmob.utils.CPLogUtil;

/**
 * @author sien
 * @date 2017/2/9
 * @descript 定时器触发机制
 */
public class MonitorAlarmReceiver extends BroadcastReceiver {
    private final String CHECK_ACTION = "com.sien.aimanager.check";
    private final String REMIND_ACTION = "com.sien.aimanager.remind";
    private final int mId = 170327;

    @Override
    public void onReceive(Context context, Intent intent) {
        CPLogUtil.logDebug("MonitorAlarmReceiver-------------->");
        if (CHECK_ACTION.equals(intent.getAction())) {
            //检查触发创建机制
            GeneratePeroidAimUtils.checkGenerateMechanism(context);
        }else if (REMIND_ACTION.equals(intent.getAction())){
            remindOperation(context);
        }
    }

    private void remindOperation(Context context){
//        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        Notification n = new Notification();
//        n.flags = Notification.FLAG_AUTO_CANCEL;
//
//        Intent i = new Intent(context, MainAimObjectActivity.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
//        //PendingIntent
//        PendingIntent contentIntent = PendingIntent.getActivity(
//                context,
//                R.string.app_name,
//                i,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//        n.contentIntent = contentIntent;
//        n.tickerText = "记得完成今天的任务";
//        n.category = "目标管理";
//        nm.notify(R.string.app_name, n);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.icon_share)
                        .setContentTitle("目标管理")
                        .setContentText("记得完成今天的任务");
        Intent resultIntent = new Intent(context, MainAimObjectActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainAimObjectActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(mId, mBuilder.build());
    }
}
