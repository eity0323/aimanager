package com.sien.lib.databmob.db.helper;

import com.sien.lib.databmob.utils.CPDateUtil;

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
        Date nextDate = CPDateUtil.getRelativeDate(date,1);
        calendar1.setTime(nextDate);
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);
        long time = calendar1.getTime().getTime();
        return new Date(time);
    }
}
