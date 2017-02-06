package com.sien.aimanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sien.lib.baseapp.events.BaseAppEvents;
import com.sien.lib.datapp.utils.CPNetworkUtil;
import com.sien.lib.datapp.utils.EventPostUtil;

/**
 * @author sien
 * @date 2016/12/15
 * @descript 监听网络状态变化
 */
public class ConnectionChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive( Context context, Intent intent ) {
        boolean netstart = CPNetworkUtil.isNetworkAvailable(context);
        if (!netstart){
            EventPostUtil.post(new BaseAppEvents.NetworkChangeEvent(BaseAppEvents.STATUS_FAIL_NETERROR,-1));
        }else {
            EventPostUtil.post(new BaseAppEvents.NetworkChangeEvent(BaseAppEvents.STATUS_SUCCESS,CPNetworkUtil.getNetworkType(context)));
        }
    }
}
