package com.sien.lib.datapp.network.action;


import android.content.Context;

import com.sien.lib.datapp.R;
import com.sien.lib.datapp.beans.AimItemVO;
import com.sien.lib.datapp.beans.AimTypeVO;
import com.sien.lib.datapp.beans.UserInfoVO;
import com.sien.lib.datapp.control.CPSharedPreferenceManager;
import com.sien.lib.datapp.control.CPThreadPoolManager;
import com.sien.lib.datapp.db.AimItemVODao;
import com.sien.lib.datapp.db.AimTypeVODao;
import com.sien.lib.datapp.db.DBManager;
import com.sien.lib.datapp.db.UserInfoVODao;
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
        aimTypeVO.setStartTime(new Date());
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
        aimTypeVO.setStartTime(new Date());
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
        aimTypeVO.setStartTime(new Date());
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
        aimTypeVO.setStartTime(new Date());
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
        aimTypeVO.setStartTime(new Date());
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
        aimTypeVO.setStartTime(new Date());
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
     * 请求目标分类数据(all)
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

    public static List<AimTypeVO> requestAimTypeDatasSync(Context context){
        AimTypeVODao dao = DBManager.getInstance(context).getDaoSession().getAimTypeVODao();
        QueryBuilder<AimTypeVO> qb = dao.queryBuilder();
        List<AimTypeVO> list = qb.list();
        return list;
    }

    /**
     * 请求目标分类数据(固定分类)
     */
    public static void requestAimTypeFixedDatas(final Context context){
        if (context == null) {
            CPLogUtil.logDebug("requestCustomTabDatas context can not be null");
            EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_FAIL_OHTERERROR, null));
            return;
        }

        CPThreadPoolManager.newInstance().addExecuteTask(new Runnable() {
            @Override
            public void run() {
                List<AimTypeVO> list = requestAimTypeFixedDatasSync(context);

                CPLogUtil.logDebug("requestCustomTabDatas Result " + list.size() );

                EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_SUCCESS, list));
            }
        });
    }

    public static List<AimTypeVO> requestAimTypeFixedDatasSync(Context context){
        AimTypeVODao dao = DBManager.getInstance(context).getDaoSession().getAimTypeVODao();
        QueryBuilder<AimTypeVO> qb = dao.queryBuilder();
        qb.where(AimTypeVODao.Properties.Recyclable.eq(true));
        List<AimTypeVO> list = qb.list();
        return list;
    }

    /**
     * 根据周期请求目标分类数据(固定分类)
     * @param context
     * @param period
     * @return
     */
    public static void requestAimTypeFixedDatas(final Context context, final int period){
        if (context == null) {
            CPLogUtil.logDebug("requestCustomTabDatas context can not be null");
            EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_FAIL_OHTERERROR, null));
            return;
        }

        CPThreadPoolManager.newInstance().addExecuteTask(new Runnable() {
            @Override
            public void run() {
                List<AimTypeVO> list = requestAimTypeFixedDatasSync(context,period);

                CPLogUtil.logDebug("requestCustomTabDatas Result " + list.size() );

                EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_SUCCESS, list));
            }
        });
    }
    public static List<AimTypeVO> requestAimTypeFixedDatasSync(Context context,int period){
        AimTypeVODao dao = DBManager.getInstance(context).getDaoSession().getAimTypeVODao();
        QueryBuilder<AimTypeVO> qb = dao.queryBuilder();
        qb.where(AimTypeVODao.Properties.Recyclable.eq(true),AimTypeVODao.Properties.Period.eq(period));
        List<AimTypeVO> list = qb.list();
        return list;
    }

    /**
     * 请求目标分类数据（自动创建 or 不可循环创建的目标分类）
     */
    public static void requestAimObjectDatas(final Context context){
        if (context == null) {
            CPLogUtil.logDebug("requestCustomTabDatas context can not be null");
            EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_FAIL_OHTERERROR, null));
            return;
        }

        CPThreadPoolManager.newInstance().addExecuteTask(new Runnable() {
            @Override
            public void run() {
                List<AimTypeVO> list = requestAimObjectDatasSync(context);

                CPLogUtil.logDebug("requestCustomTabDatas Result " + list.size() );

                EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_SUCCESS, list));
            }
        });
    }

    private static List<AimTypeVO> requestAimObjectDatasSync(Context context){
        AimTypeVODao dao = DBManager.getInstance(context).getDaoSession().getAimTypeVODao();
        QueryBuilder<AimTypeVO> qb = dao.queryBuilder();
        qb.where(AimTypeVODao.Properties.Recyclable.eq(false));
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

    public static List<AimItemVO> requestAimItemDataSync(Context context,Long aimTypeId){
        AimItemVODao dao = DBManager.getInstance(context).getDaoSession().getAimItemVODao();
        QueryBuilder<AimItemVO> qb = dao.queryBuilder();
        qb.where(AimItemVODao.Properties.AimTypeId.eq(aimTypeId));
        List<AimItemVO> list = qb.list();
        return list;
    }

    /**
     * 获取分类下的目标项记录数
     * @param context
     * @param aimTypeId
     * @return
     */
    public static long requestAimItemCountSync(Context context,Long aimTypeId){
        AimItemVODao dao = DBManager.getInstance(context).getDaoSession().getAimItemVODao();
        QueryBuilder<AimItemVO> qb = dao.queryBuilder();
        qb.where(AimItemVODao.Properties.AimTypeId.eq(aimTypeId));
        return qb.buildCount().count();
    }

    /**
     * 查询用户信息
     * @param context
     * @param userName
     */
    public static void requestUserInfoDatas(final Context context,final String userName){
        if (context == null) {
            CPLogUtil.logDebug("requestUserInfoDatas context can not be null");
            EventPostUtil.post(new DatappEvent.AimItemEvent(DatappEvent.STATUS_FAIL_OHTERERROR, null));
            return;
        }
        CPThreadPoolManager.newInstance().addExecuteTask(new Runnable() {
            @Override
            public void run() {
                List<UserInfoVO> list = requestUserInfoDataSync(context,userName);

                CPLogUtil.logDebug("requestAimItemDatas Result " + list.size() );

                EventPostUtil.post(new DatappEvent.UserInfoEvent(DatappEvent.STATUS_SUCCESS, list));
            }
        });
    }

    private static List<UserInfoVO> requestUserInfoDataSync(Context context,String userName){
        UserInfoVODao dao = DBManager.getInstance(context).getDaoSession().getUserInfoVODao();
        QueryBuilder<UserInfoVO> qb = dao.queryBuilder();
        qb.where(UserInfoVODao.Properties.UserName.eq(userName));
        List<UserInfoVO> list = qb.list();
        return list;
    }

    public static void requestUserInfoDatas(final Context context){
        if (context == null) {
            CPLogUtil.logDebug("requestUserInfoDatas context can not be null");
            EventPostUtil.post(new DatappEvent.AimItemEvent(DatappEvent.STATUS_FAIL_OHTERERROR, null));
            return;
        }
        CPThreadPoolManager.newInstance().addExecuteTask(new Runnable() {
            @Override
            public void run() {
                List<UserInfoVO> list = requestUserInfoDataSync(context);

                CPLogUtil.logDebug("requestAimItemDatas Result " + list.size() );

                EventPostUtil.post(new DatappEvent.UserInfoEvent(DatappEvent.STATUS_SUCCESS, list));
            }
        });
    }
    private static List<UserInfoVO> requestUserInfoDataSync(Context context){
        UserInfoVODao dao = DBManager.getInstance(context).getDaoSession().getUserInfoVODao();
        QueryBuilder<UserInfoVO> qb = dao.queryBuilder();
        List<UserInfoVO> list = qb.list();
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
    public static void insertOrReplaceAimTypeSync(Context context,AimTypeVO aimTypeVO){
        AimTypeVODao dao =  DBManager.getInstance(context).getDaoSession().getAimTypeVODao();
        dao.insertOrReplace(aimTypeVO);
    }

    public static void insertOrReplaceAimTypeList(final Context context,final List<AimTypeVO> aimTypeVOList){
        if (context == null) {
            CPLogUtil.logDebug("deleteAimTypeById context can not be null");
            EventPostUtil.post(new DatappEvent.insertAimTypeEvent(DatappEvent.STATUS_FAIL_OHTERERROR, aimTypeVOList));
            return;
        }

        CPThreadPoolManager.newInstance().addExecuteTask(new Runnable() {
            @Override
            public void run() {
                insertOrReplaceAimTypeListSync(context,aimTypeVOList);

                EventPostUtil.post(new DatappEvent.insertAimTypeEvent(DatappEvent.STATUS_SUCCESS, aimTypeVOList));
            }
        });
    }
    private static void insertOrReplaceAimTypeListSync(Context context,final List<AimTypeVO> aimTypeVOList){
        if(aimTypeVOList == null || aimTypeVOList.isEmpty()){
            return;
        }
        final AimTypeVODao dao =  DBManager.getInstance(context).getDaoSession().getAimTypeVODao();
        dao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for(int i= 0; i<aimTypeVOList.size(); i++){
                    AimTypeVO note = aimTypeVOList.get(i);
                    dao.insertOrReplace(note);
                }
            }
        });
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
    public static void insertOrReplaceAimItemSync(Context context,AimItemVO aimTypeVO){
        AimItemVODao dao =  DBManager.getInstance(context).getDaoSession().getAimItemVODao();
        dao.insertOrReplace(aimTypeVO);
    }

    public static void insertOrReplaceAimItemList(final Context context,final List<AimItemVO> aimItemVOList){
        if (context == null) {
            CPLogUtil.logDebug("deleteAimTypeById context can not be null");
            EventPostUtil.post(new DatappEvent.insertAimTypeEvent(DatappEvent.STATUS_FAIL_OHTERERROR, aimItemVOList));
            return;
        }

        CPThreadPoolManager.newInstance().addExecuteTask(new Runnable() {
            @Override
            public void run() {
                insertOrReplaceAimItemListSync(context,aimItemVOList);

                EventPostUtil.post(new DatappEvent.insertAimTypeEvent(DatappEvent.STATUS_SUCCESS, aimItemVOList));
            }
        });
    }
    private static void insertOrReplaceAimItemListSync(Context context,final List<AimItemVO> aimItemVOList){
        if(aimItemVOList == null || aimItemVOList.isEmpty()){
            return;
        }
        final AimItemVODao dao =  DBManager.getInstance(context).getDaoSession().getAimItemVODao();
        dao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for(int i= 0; i<aimItemVOList.size(); i++){
                    AimItemVO note = aimItemVOList.get(i);
                    dao.insertOrReplace(note);
                }
            }
        });
    }

    public static void insertOrReplaceUserInfo(final Context context,final UserInfoVO userInfoVO){
        if (context == null) {
            CPLogUtil.logDebug("deleteAimTypeById context can not be null");
            EventPostUtil.post(new DatappEvent.insertUserInfoEvent(DatappEvent.STATUS_FAIL_OHTERERROR, userInfoVO));
            return;
        }

        CPThreadPoolManager.newInstance().addExecuteTask(new Runnable() {
            @Override
            public void run() {
                insertOrReplaceUserInfoSync(context,userInfoVO);

                EventPostUtil.post(new DatappEvent.insertUserInfoEvent(DatappEvent.STATUS_SUCCESS, userInfoVO));
            }
        });
    }
    private static void insertOrReplaceUserInfoSync(Context context,UserInfoVO userInfoVO){
        UserInfoVODao dao =  DBManager.getInstance(context).getDaoSession().getUserInfoVODao();
        dao.insertOrReplace(userInfoVO);
    }
}


