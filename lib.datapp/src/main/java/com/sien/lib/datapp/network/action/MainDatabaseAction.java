package com.sien.lib.datapp.network.action;


import android.content.Context;

import com.sien.lib.datapp.R;
import com.sien.lib.datapp.beans.AimItemVO;
import com.sien.lib.datapp.beans.AimTypeVO;
import com.sien.lib.datapp.control.CPSharedPreferenceManager;
import com.sien.lib.datapp.control.CPThreadPoolManager;
import com.sien.lib.datapp.db.AimItemVODao;
import com.sien.lib.datapp.db.AimTypeVODao;
import com.sien.lib.datapp.db.DBManager;
import com.sien.lib.datapp.events.DatappEvent;
import com.sien.lib.datapp.utils.CPLogUtil;
import com.sien.lib.datapp.utils.EventPostUtil;

import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Date;
import java.util.List;

/**
 * @author sien
 * @date 2017/2/4
 * @descript 数据库action
 */
public class MainDatabaseAction {
    //----------------------------------------------------------------------------------------------初始化系统数据（默认目标类型）
    public static void createInitialAimType(Context context){
        CPLogUtil.logDebug("start to generate default data");

        //every day
        AimTypeVO aimTypeVO = new AimTypeVO();
        aimTypeVO.setCustomed(false);
        aimTypeVO.setDesc("1天完成的目标");
        aimTypeVO.setFinishPercent(0);
        aimTypeVO.setFinishStatus(AimTypeVO.STATUS_UNDO);
        aimTypeVO.setPeriod(AimTypeVO.PERIOD_DAY);
        aimTypeVO.setModifyTime(new Date());
        aimTypeVO.setPriority(AimTypeVO.PRIORITY_FIVE);
        aimTypeVO.setRecyclable(true);
        aimTypeVO.setTargetPeriod(AimTypeVO.PERIOD_DAY);
        aimTypeVO.setCover("drawable://" + R.mipmap.icon_everyday);
        aimTypeVO.setTypeName("每天");

        insertOrReplaceAimType(context,aimTypeVO);

        //every week
        aimTypeVO = new AimTypeVO();
        aimTypeVO.setCustomed(false);
        aimTypeVO.setDesc("1周完成的目标");
        aimTypeVO.setFinishPercent(0);
        aimTypeVO.setFinishStatus(AimTypeVO.STATUS_UNDO);
        aimTypeVO.setPeriod(AimTypeVO.PERIOD_WEEK);
        aimTypeVO.setModifyTime(new Date());
        aimTypeVO.setPriority(AimTypeVO.PRIORITY_FIVE);
        aimTypeVO.setRecyclable(true);
        aimTypeVO.setTargetPeriod(AimTypeVO.PERIOD_WEEK);
        aimTypeVO.setCover("drawable://" + R.mipmap.icon_everyweek);
        aimTypeVO.setTypeName("每周");

        insertOrReplaceAimType(context,aimTypeVO);

        //every month
        aimTypeVO = new AimTypeVO();
        aimTypeVO.setCustomed(false);
        aimTypeVO.setDesc("1个月完成的目标");
        aimTypeVO.setFinishPercent(0);
        aimTypeVO.setFinishStatus(AimTypeVO.STATUS_UNDO);
        aimTypeVO.setPeriod(AimTypeVO.PERIOD_MONTH);
        aimTypeVO.setModifyTime(new Date());
        aimTypeVO.setPriority(AimTypeVO.PRIORITY_FIVE);
        aimTypeVO.setRecyclable(true);
        aimTypeVO.setTargetPeriod(AimTypeVO.PERIOD_MONTH);
        aimTypeVO.setCover("drawable://" + R.mipmap.icon_everyday);
        aimTypeVO.setTypeName("每月");

        insertOrReplaceAimType(context,aimTypeVO);

        //every season
        aimTypeVO = new AimTypeVO();
        aimTypeVO.setCustomed(false);
        aimTypeVO.setDesc("1一个季度完成的目标");
        aimTypeVO.setFinishPercent(0);
        aimTypeVO.setFinishStatus(AimTypeVO.STATUS_UNDO);
        aimTypeVO.setPeriod(AimTypeVO.PERIOD_SEASON);
        aimTypeVO.setModifyTime(new Date());
        aimTypeVO.setPriority(AimTypeVO.PRIORITY_FIVE);
        aimTypeVO.setRecyclable(true);
        aimTypeVO.setTargetPeriod(AimTypeVO.PERIOD_SEASON);
        aimTypeVO.setCover("drawable://" + R.mipmap.icon_everyweek);
        aimTypeVO.setTypeName("每季");

        insertOrReplaceAimType(context,aimTypeVO);

        //every half year
        aimTypeVO = new AimTypeVO();
        aimTypeVO.setCustomed(false);
        aimTypeVO.setDesc("半年完成的目标");
        aimTypeVO.setFinishPercent(0);
        aimTypeVO.setFinishStatus(AimTypeVO.STATUS_UNDO);
        aimTypeVO.setPeriod(AimTypeVO.PERIOD_HALF_YEAR);
        aimTypeVO.setModifyTime(new Date());
        aimTypeVO.setPriority(AimTypeVO.PRIORITY_FIVE);
        aimTypeVO.setRecyclable(true);
        aimTypeVO.setTargetPeriod(AimTypeVO.PERIOD_HALF_YEAR);
        aimTypeVO.setCover("drawable://" + R.mipmap.icon_everyday);
        aimTypeVO.setTypeName("半年");

        insertOrReplaceAimType(context,aimTypeVO);

        //every year
        aimTypeVO = new AimTypeVO();
        aimTypeVO.setCustomed(false);
        aimTypeVO.setDesc("1年完成的目标");
        aimTypeVO.setFinishPercent(0);
        aimTypeVO.setFinishStatus(AimTypeVO.STATUS_UNDO);
        aimTypeVO.setPeriod(AimTypeVO.PERIOD_YEAR);
        aimTypeVO.setModifyTime(new Date());
        aimTypeVO.setPriority(AimTypeVO.PRIORITY_FIVE);
        aimTypeVO.setRecyclable(true);
        aimTypeVO.setTargetPeriod(AimTypeVO.PERIOD_YEAR);
        aimTypeVO.setCover("drawable://" + R.mipmap.icon_everyweek);
        aimTypeVO.setTypeName("每年");

        insertOrReplaceAimType(context,aimTypeVO);

        CPSharedPreferenceManager.getInstance(context).saveData(CPSharedPreferenceManager.SETTINGS_DEFAULT_DATA,"true");

        CPLogUtil.logDebug("end to generate default data");
    }
    //----------------------------------------------------------------------------------------------查询
    /**
     * 请求目标分类数据
     */
    public static void requestAimTypeDatas(final Context context){
        if (context == null) {
            CPLogUtil.logDebug("requestCustomTabDatas context can not be null");
            EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_FAIL_OHTERERROR, null));
            return;
        }

        CPThreadPoolManager.newInstance().addExecuteTask(new Runnable() {
            @Override
            public void run() {
                List<AimTypeVO> list = requestAimTypeDatasSync(context);

                CPLogUtil.logDebug("requestCustomTabDatas Result " + list.size() );

                EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_SUCCESS, list));
            }
        });
    }

    private static List<AimTypeVO> requestAimTypeDatasSync(Context context){
        AimTypeVODao dao = DBManager.getInstance(context).getDaoSession().getAimTypeVODao();
        QueryBuilder<AimTypeVO> qb = dao.queryBuilder();
        List<AimTypeVO> list = qb.list();
        return list;
    }

    /**
     * 请求目标记录数据
     */
    public static void requestAimItemDatas(final Context context){
        if (context == null) {
            CPLogUtil.logDebug("requestAimItemDatas context can not be null");
            EventPostUtil.post(new DatappEvent.AimItemEvent(DatappEvent.STATUS_FAIL_OHTERERROR, null));
            return;
        }

        CPThreadPoolManager.newInstance().addExecuteTask(new Runnable() {
            @Override
            public void run() {
                List<AimItemVO> list = requestAimItemDataSync(context);

                CPLogUtil.logDebug("requestAimItemDatas Result " + list.size() );

                EventPostUtil.post(new DatappEvent.AimItemEvent(DatappEvent.STATUS_SUCCESS, list));
            }
        });
    }

    private static List<AimItemVO> requestAimItemDataSync(Context context){
        AimItemVODao dao = DBManager.getInstance(context).getDaoSession().getAimItemVODao();
        QueryBuilder<AimItemVO> qb = dao.queryBuilder();
        List<AimItemVO> list = qb.list();
        return list;
    }


    /**
     * 请求目标记录数据
     * @param context
     * @param aimTypeId 分类id
     */
    public static void requestAimItemDatas(final Context context,final Long aimTypeId){
        if (context == null) {
            CPLogUtil.logDebug("requestAimItemDatas context can not be null");
            EventPostUtil.post(new DatappEvent.AimItemEvent(DatappEvent.STATUS_FAIL_OHTERERROR, null));
            return;
        }

        CPThreadPoolManager.newInstance().addExecuteTask(new Runnable() {
            @Override
            public void run() {
                List<AimItemVO> list = requestAimItemDataSync(context,aimTypeId);

                CPLogUtil.logDebug("requestAimItemDatas Result " + list.size() );

                EventPostUtil.post(new DatappEvent.AimItemEvent(DatappEvent.STATUS_SUCCESS, list));
            }
        });
    }

    private static List<AimItemVO> requestAimItemDataSync(Context context,Long aimTypeId){
        AimItemVODao dao = DBManager.getInstance(context).getDaoSession().getAimItemVODao();
        QueryBuilder<AimItemVO> qb = dao.queryBuilder();
        qb.where(AimItemVODao.Properties.AimTypeId.eq(aimTypeId));
        List<AimItemVO> list = qb.list();
        return list;
    }

    //----------------------------------------------------------------------------------------------删除
    /**
     * 删除记录item
     * @param context
     * @param aimItemId
     */
    public static void deleteAimItemById(final Context context,final Long aimItemId){
        if (context == null) {
            CPLogUtil.logDebug("deleteAimItemById context can not be null");
            EventPostUtil.post(new DatappEvent.deleteAimItemEvent(DatappEvent.STATUS_FAIL_OHTERERROR, aimItemId));
            return;
        }
        if (aimItemId == null) {
            CPLogUtil.logDebug("deleteAimItemById aimItemId can not be null");
            EventPostUtil.post(new DatappEvent.deleteAimItemEvent(DatappEvent.STATUS_FAIL_OHTERERROR, aimItemId));
            return;
        }

        CPThreadPoolManager.newInstance().addExecuteTask(new Runnable() {

            @Override
            public void run() {
                deleteAimItemByIdSync(context,aimItemId);

                EventPostUtil.post(new DatappEvent.deleteAimItemEvent(DatappEvent.STATUS_SUCCESS, aimItemId));
            }
        });
    }

    private static void deleteAimItemByIdSync(Context context,Long aimTypeId){
        AimItemVODao dao = DBManager.getInstance(context).getDaoSession().getAimItemVODao();
        dao.deleteByKey(aimTypeId);
    }

    /**
     * 根据分类删除记录item
     * @param context
     * @param aimItemTypeId
     */
    public static void deleteAimItemsByTypeId(final Context context,final Long aimItemTypeId){
        if (context == null) {
            CPLogUtil.logDebug("deleteAimItemsByTypeId context can not be null");
            EventPostUtil.post(new DatappEvent.deleteAimItemEvent(DatappEvent.STATUS_FAIL_OHTERERROR, aimItemTypeId));
            return;
        }
        if (aimItemTypeId == null) {
            CPLogUtil.logDebug("deleteAimItemsByTypeId aimItemId can not be null");
            EventPostUtil.post(new DatappEvent.deleteAimItemEvent(DatappEvent.STATUS_FAIL_OHTERERROR, aimItemTypeId));
            return;
        }

        CPThreadPoolManager.newInstance().addExecuteTask(new Runnable() {

            @Override
            public void run() {
                deleteAimItemsByTypeIdSync(context,aimItemTypeId);

                EventPostUtil.post(new DatappEvent.deleteAimItemEvent(DatappEvent.STATUS_SUCCESS, aimItemTypeId));
            }
        });
    }

    private static void deleteAimItemsByTypeIdSync(Context context,Long aimTypeId){
        AimItemVODao dao = DBManager.getInstance(context).getDaoSession().getAimItemVODao();
        QueryBuilder queryBuilder = dao.queryBuilder().where(AimItemVODao.Properties.AimTypeId.eq(aimTypeId));;
        DeleteQuery deleteQuery = queryBuilder.buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
    }

    /**
     * 删除分类
     * @param context
     * @param aimTypeId
     */
    public static void deleteAimTypeById(final Context context,final Long aimTypeId){
        if (context == null) {
            CPLogUtil.logDebug("deleteAimTypeById context can not be null");
            EventPostUtil.post(new DatappEvent.deleteAimTypeEvent(DatappEvent.STATUS_FAIL_OHTERERROR, aimTypeId));
            return;
        }
        if (aimTypeId == null) {
            CPLogUtil.logDebug("deleteAimTypeById aimTypeId can not be null");
            EventPostUtil.post(new DatappEvent.deleteAimTypeEvent(DatappEvent.STATUS_FAIL_OHTERERROR, aimTypeId));
            return;
        }

        CPThreadPoolManager.newInstance().addExecuteTask(new Runnable() {

            @Override
            public void run() {
                deleteAimTypeByIdSync(context,aimTypeId);

                EventPostUtil.post(new DatappEvent.deleteAimTypeEvent(DatappEvent.STATUS_SUCCESS, aimTypeId));
            }
        });
    }

    private static void deleteAimTypeByIdSync(Context context,Long aimTypeId){
        //删除目标类型，注：系统默认类型不可删除
//        AimItemVODao dao =  DBManager.getInstance(context).getDaoSession().getAimItemVODao();
//        dao.deleteByKey(aimTypeId);

        AimTypeVODao dao = DBManager.getInstance(context).getDaoSession().getAimTypeVODao();
        QueryBuilder queryBuilder = dao.queryBuilder().where(AimTypeVODao.Properties.Id.eq(aimTypeId),AimTypeVODao.Properties.Customed.eq(true));
        DeleteQuery deleteQuery = queryBuilder.buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
    }

    //----------------------------------------------------------------------------------------------添加 or 修改
    public static void insertOrReplaceAimType(final Context context,final AimTypeVO aimTypeVO){
        if (context == null) {
            CPLogUtil.logDebug("deleteAimTypeById context can not be null");
            EventPostUtil.post(new DatappEvent.insertAimTypeEvent(DatappEvent.STATUS_FAIL_OHTERERROR, aimTypeVO));
            return;
        }

        CPThreadPoolManager.newInstance().addExecuteTask(new Runnable() {
            @Override
            public void run() {
                insertOrReplaceAimTypeSync(context,aimTypeVO);

                EventPostUtil.post(new DatappEvent.insertAimTypeEvent(DatappEvent.STATUS_SUCCESS, aimTypeVO));
            }
        });
    }
    private static void insertOrReplaceAimTypeSync(Context context,AimTypeVO aimTypeVO){
        AimTypeVODao dao =  DBManager.getInstance(context).getDaoSession().getAimTypeVODao();
        dao.insertOrReplace(aimTypeVO);
    }

    public static void insertOrReplaceAimItem(final Context context,final AimItemVO aimTypeVO){
        if (context == null) {
            CPLogUtil.logDebug("deleteAimTypeById context can not be null");
            EventPostUtil.post(new DatappEvent.insertAimTypeEvent(DatappEvent.STATUS_FAIL_OHTERERROR, aimTypeVO));
            return;
        }

        CPThreadPoolManager.newInstance().addExecuteTask(new Runnable() {
            @Override
            public void run() {
                insertOrReplaceAimItemSync(context,aimTypeVO);

                EventPostUtil.post(new DatappEvent.insertAimTypeEvent(DatappEvent.STATUS_SUCCESS, aimTypeVO));
            }
        });
    }
    private static void insertOrReplaceAimItemSync(Context context,AimItemVO aimTypeVO){
        AimItemVODao dao =  DBManager.getInstance(context).getDaoSession().getAimItemVODao();
        dao.insertOrReplace(aimTypeVO);
    }
}


