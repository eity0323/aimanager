package com.sien.aimanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author sien
 * @date 2016/10/12
 * @descript 短信监听
 */
public class SMSBroadcastReceiver extends BroadcastReceiver {

    private static MessageListener mMessageListener;
    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    public SMSBroadcastReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
//            String format = intent.getStringExtra("format"); sdk 23

            SmsMessage smsMessage;
            for(Object pdu:pdus) {
                smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                //SmsMessage.createFromPdu(pdu, format);  sdk 23

                String sender = smsMessage.getDisplayOriginatingAddress();
                //短信内容
                String content = smsMessage.getDisplayMessageBody();
                long date = smsMessage.getTimestampMillis();
                Date tiemDate = new Date(date);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = simpleDateFormat.format(tiemDate);

                if (mMessageListener != null) {
                    mMessageListener.onReceived(content);
                }
                abortBroadcast();
            }
        }

    }

    //回调接口
    public interface MessageListener {
        public void onReceived(String message);
    }

    public void setOnReceivedMessageListener(MessageListener messageListener) {
        this.mMessageListener = messageListener;
    }

    public void removeMessageListener(){
        mMessageListener = null;
    }
}
