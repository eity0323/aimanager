package com.sien.lib.datapp.db.helper;

import java.util.Calendar;
import java.util.Date;

/**
 * @author sien
 * @date 2017/2/15
 * @descript 数据库日期工具类
 */
public class DBDateHelper {
    /**
     * 获取当天开始的日期
     * @param date
     * @return
     */
    public static Date getDayStartMillisecond(Date date){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);
        long time = calendar1.getTime().getTime();
        return new Date(time);
    }

    /**
     * 获取当天结束的日期
     * @param date
     * @return
     */
    public static Date getDayEndMillisecond(Date date){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        calendar1.set(Calendar.HOUR_OF_DAY, 23);
        calendar1.set(Calendar.MINUTE, 59);
        calendar1.set(Calendar.SECOND, 59);
        calendar1.set(Calendar.MILLISECOND, 0);
        long time = calendar1.getTime().getTime();
        return new Date(time);
    }
}
