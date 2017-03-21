package com.sien.lib.databmob.utils;

import de.greenrobot.event.EventBus;

/**
 * Name: EventPostUtil
 * Author: sien
 * Email:
 * Comment: //总线事件发出方法
 * Date: 2016-06-29 15:07
 */
public class EventPostUtil {
    public static void post(Object obj){
        EventBus.getDefault().post(obj);
    }

    public static void register(Object obj){
        EventBus.getDefault().register(obj);
    }

    public static void unregister(Object obj){
        EventBus.getDefault().unregister(obj);
    }
}
