package com.sien.aimanager.control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.sien.aimanager.config.AppConfig;
import com.sien.aimanager.services.GenerateAimServices;
import com.sien.lib.datapp.utils.CPDateUtil;
import com.sien.lib.datapp.beans.AimItemVO;
import com.sien.lib.datapp.beans.AimTypeVO;
import com.sien.lib.datapp.db.helper.AimItemDBHelper;
import com.sien.lib.datapp.db.helper.AimTypeDBHelper;
import com.sien.lib.datapp.utils.CPLogUtil;
import com.sien.lib.datapp.utils.CPStringUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author sien
 * @date 2017/2/9
 * @descript 创建周期目标
 * 触发机制：
 * 1、定时触发
 * 2、开机触发
 * 3、充电触发 （ignore）
 * 4、服务开启触发
 *
 * 触发动作：
 * 1、检查上一次触发时机，在有效周期内不执行操作
 * 2、周期失效，执行创建目标操作
 */
public class GeneratePeroidAimUtils {
    private static final String monitorShareName = "aimanager_monitorshare";
    public static final long timeInterval = 10 * 1000;//24 * 60 * 60 * 1000;//1天

    /**
     * 检测是否触发创建机制
     */
    public static boolean checkGenerateMechanism(Context context){
        //天
        checkGenerateMechanismByPeriod(context,AimTypeVO.PERIOD_DAY);
        //周
        checkGenerateMechanismByPeriod(context,AimTypeVO.PERIOD_WEEK);
        //月
        checkGenerateMechanismByPeriod(context,AimTypeVO.PERIOD_MONTH);
        //季
        checkGenerateMechanismByPeriod(context,AimTypeVO.PERIOD_SEASON);
        //半年
        checkGenerateMechanismByPeriod(context,AimTypeVO.PERIOD_HALF_YEAR);
        //年
        checkGenerateMechanismByPeriod(context,AimTypeVO.PERIOD_YEAR);
        return false;
    }

    /*根据周期检查触发机制*/
    private static void checkGenerateMechanismByPeriod(Context context,int period){
        Date checkDate = getLastCheckDate(context,period);
        if (checkDate != null) {
            int diffDay = CPDateUtil.getTimeDiffDays(new Date(), checkDate);

            CPLogUtil.logDebug("checkGenerateMechanismByPeriod period:" + period + " diffDay:" + diffDay);

            Intent intent = null;
            if (diffDay >= period) {
                intent = new Intent();
                intent.setClass(context,GenerateAimServices.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("period", period);
                intent.putExtra("diff",diffDay);
            }

            if (intent != null){
                context.startService(intent);
            }
        }
    }

    /**
     * 获取上一次检测时间
     * @param context
     * @return
     */
    private static Date getLastCheckDate(Context context,int period){
        Date lastCheckDate = null;
        SharedPreferences preferences = context.getSharedPreferences(monitorShareName, Activity.MODE_PRIVATE);
        if (preferences != null){
            String key = CPStringUtil.appendIdentifier("lastCheckDate",String.valueOf(period));
            lastCheckDate = CPDateUtil.getDefaultDateByParse(preferences.getString(key,""));

            CPLogUtil.logDebug("lastCheckDate:" + lastCheckDate.toString());
        }
        return lastCheckDate;
    }

    /*设置检测时间*/
    public static void setLastCheckDate(Context context,int period,Date date){
        if (date != null){
            SharedPreferences preferences = context.getSharedPreferences(monitorShareName, Activity.MODE_PRIVATE);
            String key = CPStringUtil.appendIdentifier("lastCheckDate",String.valueOf(period));
            preferences.edit().putString(key, CPDateUtil.getDateString(date)).apply();
        }
    }

    /*初始化检查时间(仅第一次执行)*/
    public static void initLastCheckDate(Context context){
        SharedPreferences preferences = context.getSharedPreferences(monitorShareName, Activity.MODE_PRIVATE);

        Date nowDate = new Date();
        //天
        int period = AimTypeVO.PERIOD_DAY;
        String key = CPStringUtil.appendIdentifier("lastCheckDate",String.valueOf(period));
        if (preferences == null || !preferences.contains(key)) {
            GeneratePeroidAimUtils.setLastCheckDate(context, period, nowDate);
        }

        //周
        period = AimTypeVO.PERIOD_WEEK;
        key = CPStringUtil.appendIdentifier("lastCheckDate",String.valueOf(period));
        if (preferences == null || !preferences.contains(key)) {
            GeneratePeroidAimUtils.setLastCheckDate(context, period, nowDate);
        }

        //月
        period = AimTypeVO.PERIOD_MONTH;
        key = CPStringUtil.appendIdentifier("lastCheckDate",String.valueOf(period));
        if (preferences == null || !preferences.contains(key)) {
            GeneratePeroidAimUtils.setLastCheckDate(context, period, nowDate);
        }

        //季
        period = AimTypeVO.PERIOD_SEASON;
        key = CPStringUtil.appendIdentifier("lastCheckDate",String.valueOf(period));
        if (preferences == null || !preferences.contains(key)) {
            GeneratePeroidAimUtils.setLastCheckDate(context, period, nowDate);
        }

        //半年
        period = AimTypeVO.PERIOD_HALF_YEAR;
        key = CPStringUtil.appendIdentifier("lastCheckDate",String.valueOf(period));
        if (preferences == null || !preferences.contains(key)) {
            GeneratePeroidAimUtils.setLastCheckDate(context, period, nowDate);
        }

        //年
        period = AimTypeVO.PERIOD_YEAR;
        key = CPStringUtil.appendIdentifier("lastCheckDate",String.valueOf(period));
        if (preferences == null || !preferences.contains(key)) {
            GeneratePeroidAimUtils.setLastCheckDate(context, period, nowDate);
        }
    }

    /**
     * 创建周期目标
     * @param context
     * @param period 周期
     */
    public static void generatePeriodAimByPeriod(Context context,int period,int diff){
        CPLogUtil.logDebug("1、start to generate period aimType! period:" + period + " diffDay:" + diff);

        //1、查询需要创建的目标项;2、创建目标分类;3、创建目标项;4、通知界面更新
        List<AimTypeVO> dayList = AimTypeDBHelper.requestAimTypeFixedSync(context,period);
        if (dayList != null && dayList.size() > 0){
//            List<AimObjectVO> newDataList = new ArrayList<>();
            AimTypeVO aimTypeVO;

            for (int i = diff;i >= 0;i-=period) {    //相差天数 + 今天

                Date nowDate = CPDateUtil.getRelativeDate(new Date(),(-1) * i); //往前推i天

                for (AimTypeVO item : dayList) { //需要循环创建的分类数
                    //非激活状态不可自动创建
                    if (item.getActive() != null && !item.getActive().booleanValue()){
                        continue;
                    }

                    //有开始日期，且未到开始日期不创建
                    if (item.getStartTime() != null) {
                        int startDiff = CPDateUtil.getTimeDiffDays(nowDate, item.getStartTime());
                        if (startDiff < 0) continue;
                    }

                    //有结束日期，且超过结束日期不创建
                    if (item.getEndTime() != null) {
                        int endDiff = CPDateUtil.getTimeDiffDays(item.getEndTime(), nowDate);
                        if (endDiff < 0) continue;
                    }

                    //无目标项则不创建该分类对象
                    long count = AimItemDBHelper.requestAimItemCountSync(context,item.getId());
                    if (count <= 0) continue;

                    aimTypeVO = new AimTypeVO();
                    aimTypeVO.setCustomed(true);
                    aimTypeVO.setDesc(item.getDesc());
                    aimTypeVO.setFinishPercent(0);
                    aimTypeVO.setFinishStatus(AimTypeVO.STATUS_UNDO);
                    aimTypeVO.setPeriod(item.getPeriod());
                    aimTypeVO.setModifyTime(nowDate);
                    aimTypeVO.setStartTime(nowDate);
                    aimTypeVO.setPriority(AimTypeVO.PRIORITY_FIVE);
                    aimTypeVO.setRecyclable(false);
                    aimTypeVO.setPlanProject(item.getPlanProject());
                    aimTypeVO.setTargetPeriod(item.getTargetPeriod());
                    aimTypeVO.setCover(item.getCover());
                    aimTypeVO.setTypeName(AppConfig.formatGenerateTypeName(item.getTypeName(),nowDate));
                    aimTypeVO.setFirstExtra(String.valueOf(item.getId()));

//                    newDataList.add(aimTypeVO);

                    //创建目标分类
                    AimTypeDBHelper.insertOrReplaceAimTypeSync(context,aimTypeVO);

                    //创建目标项
                    if (aimTypeVO.getId() != null){
                        generateAimItemByAimType(context,item.getId(),aimTypeVO.getId(),nowDate);
                    }
                }
            }

//            //批量插入目标分类(单条记录插入，便于获取id创建目标项)
//            AimObjectHelper.insertOrReplaceAimTypeList(context,newDataList);

            //更新最后检测时间
            setLastCheckDate(context,period,new Date());

            //TODO 通知界面更新
        }
    }

    /*批量插入目标项*/
    private static void generateAimItemByAimType(Context context,Long aimTypeId,Long newAimTypeId,Date nowDate){
        CPLogUtil.logDebug("1、start to generate period aimItem! aimTypeId:" + aimTypeId + " newAimTypeId:" + newAimTypeId);

        List<AimItemVO> dayList = AimItemDBHelper.requestAimItemDataSync(context,aimTypeId);

        if (dayList != null && dayList.size() > 0) {
            List<AimItemVO> newDataList = new ArrayList<>();

            AimItemVO itemVO;
            for (AimItemVO item : dayList){
                itemVO = new AimItemVO();
                itemVO.setAimName(item.getAimName());
                itemVO.setDesc(item.getDesc());
                item.setStartTime(nowDate);
                itemVO.setModifyTime(nowDate);
                itemVO.setFinishStatus(AimItemVO.STATUS_UNDO);
                itemVO.setPriority(AimItemVO.PRIORITY_FIVE);
                itemVO.setFinishPercent(0);
                itemVO.setAimTypeId(newAimTypeId);

                newDataList.add(itemVO);
            }

            if (newDataList.size() > 0) {
                //批量插入目标项
                AimItemDBHelper.insertOrReplaceAimItemListSync(context, newDataList);
            }
        }
    }
}
