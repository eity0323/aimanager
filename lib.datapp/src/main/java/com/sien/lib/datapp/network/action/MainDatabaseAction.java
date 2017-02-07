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
        aimTypeVO.setDesc("targets for every day");
        aimTypeVO.setFinishPercent(0);
        aimTypeVO.setFinishStatus(0);
        aimTypeVO.setPeriod(1);
        aimTypeVO.setModifyTime(new Date());
        aimTypeVO.setPriority(5);
        aimTypeVO.setRecyclable(true);
        aimTypeVO.setTargetPeriod(1);
        aimTypeVO.setCover("drawable://" + R.mipmap.icon_everyday);
        aimTypeVO.setTypeName("Every day");

        insertAimType(context,aimTypeVO);

        //every week
        aimTypeVO = new AimTypeVO();
        aimTypeVO.setCustomed(false);
        aimTypeVO.setDesc("targets for every week");
        aimTypeVO.setFinishPercent(0);
        aimTypeVO.setFinishStatus(0);
        aimTypeVO.setPeriod(7);
        aimTypeVO.setModifyTime(new Date());
        aimTypeVO.setPriority(5);
        aimTypeVO.setRecyclable(true);
        aimTypeVO.setTargetPeriod(7);
        aimTypeVO.setCover("drawable://" + R.mipmap.icon_everyweek);
        aimTypeVO.setTypeName("Every week");

        insertAimType(context,aimTypeVO);

        //every month
        aimTypeVO = new AimTypeVO();
        aimTypeVO.setCustomed(false);
        aimTypeVO.setDesc("targets for every month");
        aimTypeVO.setFinishPercent(0);
        aimTypeVO.setFinishStatus(0);
        aimTypeVO.setPeriod(30);
        aimTypeVO.setModifyTime(new Date());
        aimTypeVO.setPriority(5);
        aimTypeVO.setRecyclable(true);
        aimTypeVO.setTargetPeriod(30);
        aimTypeVO.setCover("drawable://" + R.mipmap.icon_everyday);
        aimTypeVO.setTypeName("Every month");

        insertAimType(context,aimTypeVO);

        //every season
        aimTypeVO = new AimTypeVO();
        aimTypeVO.setCustomed(false);
        aimTypeVO.setDesc("targets for every season");
        aimTypeVO.setFinishPercent(0);
        aimTypeVO.setFinishStatus(0);
        aimTypeVO.setPeriod(90);
        aimTypeVO.setModifyTime(new Date());
        aimTypeVO.setPriority(5);
        aimTypeVO.setRecyclable(true);
        aimTypeVO.setTargetPeriod(90);
        aimTypeVO.setCover("drawable://" + R.mipmap.icon_everyweek);
        aimTypeVO.setTypeName("Every season");

        insertAimType(context,aimTypeVO);

        //every half year
        aimTypeVO = new AimTypeVO();
        aimTypeVO.setCustomed(false);
        aimTypeVO.setDesc("targets for every half year");
        aimTypeVO.setFinishPercent(0);
        aimTypeVO.setFinishStatus(0);
        aimTypeVO.setPeriod(180);
        aimTypeVO.setModifyTime(new Date());
        aimTypeVO.setPriority(5);
        aimTypeVO.setRecyclable(true);
        aimTypeVO.setTargetPeriod(180);
        aimTypeVO.setCover("drawable://" + R.mipmap.icon_everyday);
        aimTypeVO.setTypeName("Every half year");

        insertAimType(context,aimTypeVO);

        //every year
        aimTypeVO = new AimTypeVO();
        aimTypeVO.setCustomed(false);
        aimTypeVO.setDesc("targets for every year");
        aimTypeVO.setFinishPercent(0);
        aimTypeVO.setFinishStatus(0);
        aimTypeVO.setPeriod(360);
        aimTypeVO.setModifyTime(new Date());
        aimTypeVO.setPriority(5);
        aimTypeVO.setRecyclable(true);
        aimTypeVO.setTargetPeriod(360);
        aimTypeVO.setCover("drawable://" + R.mipmap.icon_everyweek);
        aimTypeVO.setTypeName("Every year");

        insertAimType(context,aimTypeVO);

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
    public static void insertAimType(final Context context,final AimTypeVO aimTypeVO){
        if (context == null) {
            CPLogUtil.logDebug("deleteAimTypeById context can not be null");
            EventPostUtil.post(new DatappEvent.insertAimTypeEvent(DatappEvent.STATUS_FAIL_OHTERERROR, aimTypeVO));
            return;
        }

        CPThreadPoolManager.newInstance().addExecuteTask(new Runnable() {
            @Override
            public void run() {
                insertAimTypeSync(context,aimTypeVO);

                EventPostUtil.post(new DatappEvent.insertAimTypeEvent(DatappEvent.STATUS_SUCCESS, aimTypeVO));
            }
        });
    }
    private static void insertAimTypeSync(Context context,AimTypeVO aimTypeVO){
        AimTypeVODao dao =  DBManager.getInstance(context).getDaoSession().getAimTypeVODao();
        dao.insertOrReplace(aimTypeVO);
    }

    public static void insertAimItem(final Context context,final AimTypeVO aimTypeVO){
        if (context == null) {
            CPLogUtil.logDebug("deleteAimTypeById context can not be null");
            EventPostUtil.post(new DatappEvent.insertAimTypeEvent(DatappEvent.STATUS_FAIL_OHTERERROR, aimTypeVO));
            return;
        }

        CPThreadPoolManager.newInstance().addExecuteTask(new Runnable() {
            @Override
            public void run() {
                insertAimItemSync(context,aimTypeVO);

                EventPostUtil.post(new DatappEvent.insertAimTypeEvent(DatappEvent.STATUS_SUCCESS, aimTypeVO));
            }
        });
    }
    private static void insertAimItemSync(Context context,AimTypeVO aimTypeVO){
        AimTypeVODao dao =  DBManager.getInstance(context).getDaoSession().getAimTypeVODao();
        dao.insertOrReplace(aimTypeVO);
    }
}


