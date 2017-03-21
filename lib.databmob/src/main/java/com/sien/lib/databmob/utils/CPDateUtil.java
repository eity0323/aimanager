package com.sien.lib.databmob.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author sien
 * @date 2016/11/3
 * @descript 日期工具类
 */
public class CPDateUtil {
    /**
     * 格式 yyyy-MM-dd HH:mm:ss
     */
    public static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 格式 yyyyMMddHHmmss
     */
    public static final String DATE_FORMAT_1 = "yyyyMMddHHmmss";

    /**
     * 格式 yyyy-MM-dd HH:mm
     */
    public static final String DATE_FORMAT_2 = "yyyy-MM-dd HH:mm";

    /**
     * 格式 yyyy-MM-dd
     */
    public static final String DATE_FORMAT_3 = "yyyy-MM-dd";

    /**
     * 格式 yyyy年MM月dd日
     */
    public static final String DATE_FORMAT_4 = "yyyy年MM月dd日";

    /**
     * 格式 HH:mm:ss
     */
    public static final String DATE_FORMAT_5 = "HH:mm:ss";

    public static final String DATE_FORMAT_6 = "yyyyMMdd.hhmmss";

    public static final String DATE_FORMAT_7 = "yyyy/MM/dd HH:mm";

    public static final String DATE_FORMAT_8 = "yyyy/MM/dd HH:mm:ss";

    /**
     * 年月日时分秒
     */
    public static final SimpleDateFormat sdf1 = new SimpleDateFormat(DATE_FORMAT_1, Locale.getDefault());

    private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat(DATE_FORMAT_DEFAULT);
        }
    };

    /**
     *
     * getDate 获取当前时间
     *
     */
    public static Date getDate()
    {
        return new Date();
    }

    /**
     *
     * getSystemCalendar 获取当前Calendar
     */
    public static Calendar getSystemCalendar()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDate());
        return cal;
    }

    /**
     *
     * getDateString 获取字符串形式的当前时间
     *
     */
    public static String getSystemDateTimeString()
    {
        Date date = getDate();
        return threadLocal.get().format(date);
    }

    /**
     *
     * getDateString 获取日期字符串
     *
     */
    public static String getDateString(Date date)
    {
        if (date == null)
        {
            return null;
        }
        return threadLocal.get().format(date);
    }

    public static String getThisDateToString(String format)
    {
        Date date = getDate();

        if (format == null)
        {
            format = DATE_FORMAT_DEFAULT;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        return dateFormat.format(date);
    }

    /**
     *
     * getDateToString 根据指定日期格式获取对应字符串
     *
     */
    public static String getDateToString(Date date, String format)
    {
        if (date == null)
        {
            return null;
        }

        if (format == null)
        {
            format = DATE_FORMAT_DEFAULT;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        return dateFormat.format(date);

    }

    public static Date getDefaultDateByParse(String dateTime)
    {
        Date date = null;

        try
        {
            date = threadLocal.get().parse(dateTime);
        }
        catch (ParseException e)
        {
        }
        return date;
    }

    public static Date getDateByParse(String dateTime, String format)
    {
        Date date = null;

        try
        {
            SimpleDateFormat s = new SimpleDateFormat(format, Locale.getDefault());
            date = s.parse(dateTime);
        }
        catch (ParseException e)
        {

        }
        return date;
    }

    /**
     *
     * getDayOfWeek(这里用一句话描述这个方法的作用)
     * 计算今天是星期几
     */
    public static int getDayOfWeek()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate());
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取相对于今天的日期
     * @return
     */
    public static Date getRelativeDate(int num)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate());
        calendar.set(Calendar.DATE,calendar.get(Calendar.DATE) + num);
        return calendar.getTime();
    }

    /**
     * 获取相对于指定日期的天数
     * @param relDate
     * @param num
     * @return
     */
    public static Date getRelativeDate(Date relDate, int num)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(relDate);
        calendar.set(Calendar.DATE,calendar.get(Calendar.DATE) + num);
        return calendar.getTime();
    }

    /**
     * 计算时间差
     * [Summary]
     *       getTimeDiffDays 请用一句话描述这个方法的作用
     *
     */
    public static int getTimeDiffDays(Date data1, Date date2){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(data1);
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        calendar2.set(Calendar.HOUR_OF_DAY, 0);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);
        calendar2.set(Calendar.MILLISECOND, 0);

        long time1 = calendar1.getTime().getTime();
        long time2 = calendar2.getTime().getTime();

        int days = Math.abs((int)((time1 - time2)/(1000 * 60 * 60 * 24)));
        return days;
    }

    //-------------------------------------------------------------extra
    /**
     * 根据日期获取时间状态
     * @param date
     * @param format
     * @return
     */
    public static String getTimeState(Date date,String format){
        if (date == null){
            return "";
        }
        long timestamp = date.getTime();

        return getTimeState(timestamp,format);
    }

    /**
     * 根据 timestamp 生成各类时间状态串
     * @param timestamp 距1970 00:00:00 GMT的秒数
     * @param format 格式
     * @return 时间状态串(如：刚刚5分钟前)
     */
    public static String getTimeState(String timestamp, String format) {

        if (timestamp == null || "".equals(timestamp)) {
            return "";
        }

        try {
            timestamp = formatTimestamp(timestamp);
            long _timestamp = Long.parseLong(timestamp);

           return getTimeState(_timestamp,format);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String getTimeState(long timestamp,String format){
        if (System.currentTimeMillis() - timestamp < 1 * 60 * 1000) {
            return "刚刚";
        } else if (System.currentTimeMillis() - timestamp < 30 * 60 * 1000) {
            return ((System.currentTimeMillis() - timestamp) / 1000 / 60) + "分钟前";
        } else {

            Calendar now = Calendar.getInstance();
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(timestamp);

            if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR) && c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                    && c.get(Calendar.DATE) == now.get(Calendar.DATE)) {
                SimpleDateFormat sdf = new SimpleDateFormat("今天 HH:mm");
                return sdf.format(c.getTime());
            }

            if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR) && c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                    && c.get(Calendar.DATE) == now.get(Calendar.DATE) - 1) {
                SimpleDateFormat sdf = new SimpleDateFormat("昨天 HH:mm");
                return sdf.format(c.getTime());
            }else if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
                SimpleDateFormat sdf = null;
                if (format != null && !format.equalsIgnoreCase("")) {
                    sdf = new SimpleDateFormat(format);
                } else {
                    sdf = new SimpleDateFormat("M月d日 HH:mm:ss");
                }
                return sdf.format(c.getTime());
            }else {
                SimpleDateFormat sdf = null;
                if (format != null && !format.equalsIgnoreCase("")) {
                    sdf = new SimpleDateFormat(format);
                } else {
                    sdf = new SimpleDateFormat("yyyy年M月d日 HH:mm:ss");
                }
                return sdf.format(c.getTime());
            }
        }
    }

    /**
     * 对时间戳格式进行格式化，保证时间戳长度为13位
     *
     * @param timestamp
     *            时间戳
     * @return 返回为13位的时间戳
     */
    public static String formatTimestamp(String timestamp) {
        if (timestamp == null || "".equals(timestamp)) {
            return "";
        }

        String tempTimeStamp = timestamp + "00000000000000";
        StringBuffer stringBuffer = new StringBuffer(tempTimeStamp);
        return tempTimeStamp = stringBuffer.substring(0, 13);
    }

    /**
     * 获取短时间 （几天前，几年前，几小时前）
     * @param date
     * @return
     */
    public static String getShortTime(Date date) {
        String shortstring = "";
        if(date == null) return shortstring;

        long now = Calendar.getInstance().getTimeInMillis();
        long deltime = (now - date.getTime())/1000;
        if(deltime > 365*24*60*60) {
            shortstring = (int)(deltime/(365*24*60*60)) + "年前";
        } else if(deltime > 24*60*60) {
            shortstring = (int)(deltime/(24*60*60)) + "天前";
        } else if(deltime > 60*60) {
            shortstring = (int)(deltime/(60*60)) + "小时前";
        } else if(deltime > 60) {
            shortstring = (int)(deltime/(60)) + "分前";
        } else if(deltime > 1) {
            shortstring = deltime + "秒前";
        } else {
            shortstring = "1秒前";
        }
        return shortstring;
    }
}
