package com.sien.lib.datapp.network.action;


import android.content.Context;

import com.sien.lib.datapp.beans.AimItemVO;
import com.sien.lib.datapp.beans.AimTypeVO;
import com.sien.lib.datapp.beans.UserInfoVO;
import com.sien.lib.datapp.control.CPThreadPoolManager;
import com.sien.lib.datapp.db.helper.AimItemDBHelper;
import com.sien.lib.datapp.db.helper.AimTypeDBHelper;
import com.sien.lib.datapp.db.helper.UserInfoDBHelper;
import com.sien.lib.datapp.events.DatappEvent;
import com.sien.lib.datapp.utils.CPLogUtil;
import com.sien.lib.datapp.utils.EventPostUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author sien
 * @date 2017/2/4
 * @descript 数据库action
 */
public class MainDatabaseAction {
    //----------------------------------------------------------------------------------------------初始化系统数据（默认目标类型）
    public static void createInitialAimType(final Context context){
        CPThreadPoolManager.newInstance().addExecuteTask(new Runnable() {

            @Override
            public void run() {
                AimTypeDBHelper.createInitialAimType(context);
            }
        });

    }
    //----------------------------------------------------------------------------------------------查询
    /**
     * 请求目标分类数据(all)
     */
    public static void requestAimTypeDatas(final Context context){
        if (context == null) {
            CPLogUtil.logDebug("requestAimTypeDatas context can not be null");
            EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_FAIL_OHTERERROR, null));
            return;
        }

        CPThreadPoolManager.newInstance().addExecuteTask(new Runnable() {
            @Override
            public void run() {
                List<AimTypeVO> list = AimTypeDBHelper.requestAimTypeDatasSync(context);

                CPLogUtil.logDebug("requestAimTypeDatas Result " + list.size() );

                EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_SUCCESS, list));
            }
        });
    }

    /**
     * 根据id查目标分类
     * @param context
     * @param aimTypeId
     */
    public static void requestAimTypeDatasById(final Context context, final Long aimTypeId){
        if (context == null) {
            CPLogUtil.logDebug("requestAimTypeDatas context can not be null");
            EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_FAIL_OHTERERROR, null));
            return;
        }

        CPThreadPoolManager.newInstance().addExecuteTask(new Runnable() {
            @Override
            public void run() {
                AimTypeVO aimTypeVO = AimTypeDBHelper.requestAimTypeDatasByIdSync(context,aimTypeId);

                List<AimTypeVO> list = new ArrayList<AimTypeVO>();
                list.add(aimTypeVO);
                CPLogUtil.logDebug("requestAimTypeDatas Result " + list.size() );

                EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_SUCCESS, list));
            }
        });
    }

    /**
     * 请求目标分类数据(固定分类)
     */
    public static void requestAimTypeFixedDatas(final Context context){
        if (context == null) {
            CPLogUtil.logDebug("requestAimTypeFixedDatas context can not be null");
            EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_FAIL_OHTERERROR, null));
            return;
        }

        CPThreadPoolManager.newInstance().addExecuteTask(new Runnable() {
            @Override
            public void run() {
                List<AimTypeVO> list = AimTypeDBHelper.requestAimTypeFixedDatasSync(context);

                CPLogUtil.logDebug("requestAimTypeFixedDatas Result " + list.size() );

                EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_SUCCESS, list));
            }
        });
    }


    /**
     * 请求目标分类数据(自动创建分类)
     */
    public static void requestAimTypeAutoDatas(final Context context){
        if (context == null) {
            CPLogUtil.logDebug("requestAimTypeAutoDatas context can not be null");
            EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_FAIL_OHTERERROR, null));
            return;
        }

        CPThreadPoolManager.newInstance().addExecuteTask(new Runnable() {
            @Override
            public void run() {
                List<AimTypeVO> list = AimTypeDBHelper.requestAimTypeAutoDatasSync(context);

                CPLogUtil.logDebug("requestAimTypeAutoDatas Result " + list.size() );

                EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_SUCCESS, list));
            }
        });
    }
    /**
     * 根据周期请求目标分类数据(固定分类)
     * @param context
     * @param period
     * @return
     */
    public static void requestAimTypeFixedDatas(final Context context, final int period){
        if (context == null) {
            CPLogUtil.logDebug("requestAimTypeFixedDatas context can not be null");
            EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_FAIL_OHTERERROR, null));
            return;
        }

        CPThreadPoolManager.newInstance().addExecuteTask(new Runnable() {
            @Override
            public void run() {
                List<AimTypeVO> list = AimTypeDBHelper.requestAimTypeFixedDatasSync(context,period);

                CPLogUtil.logDebug("requestAimTypeFixedDatas Result " + list.size() );

                EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_SUCCESS, list));
            }
        });
    }

    /**
     * 根据日期查询目标分类
     * @param context
     */
    public static void requestAimTypeAutoByDate(final Context context, final Date date){
        if (context == null) {
            CPLogUtil.logDebug("requestAimObjectByDate context can not be null");
            EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_FAIL_OHTERERROR, null));
            return;
        }

        CPThreadPoolManager.newInstance().addExecuteTask(new Runnable() {
            @Override
            public void run() {
                List<AimTypeVO> list = AimTypeDBHelper.requestAimTypeAutoByDate(context,date);

                CPLogUtil.logDebug("requestAimObjectByDate Result " + list.size() );

                EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_SUCCESS, list));
            }
        });
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
                List<AimItemVO> list = AimItemDBHelper.requestAimItemDataSync(context);

                CPLogUtil.logDebug("requestAimItemDatas Result " + list.size() );

                EventPostUtil.post(new DatappEvent.AimItemEvent(DatappEvent.STATUS_SUCCESS, list));
            }
        });
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
                List<AimItemVO> list = AimItemDBHelper.requestAimItemDataSync(context,aimTypeId);

                CPLogUtil.logDebug("requestAimItemDatas Result " + list.size() );

                EventPostUtil.post(new DatappEvent.AimItemEvent(DatappEvent.STATUS_SUCCESS, list));
            }
        });
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
                List<UserInfoVO> list = UserInfoDBHelper.requestUserInfoDataSync(context,userName);

                CPLogUtil.logDebug("requestAimItemDatas Result " + list.size() );

                EventPostUtil.post(new DatappEvent.UserInfoEvent(DatappEvent.STATUS_SUCCESS, list));
            }
        });
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
                List<UserInfoVO> list = UserInfoDBHelper.requestUserInfoDataSync(context);

                CPLogUtil.logDebug("requestAimItemDatas Result " + list.size() );

                EventPostUtil.post(new DatappEvent.UserInfoEvent(DatappEvent.STATUS_SUCCESS, list));
            }
        });
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
                AimItemDBHelper.deleteAimItemByIdSync(context,aimItemId);

                EventPostUtil.post(new DatappEvent.deleteAimItemEvent(DatappEvent.STATUS_SUCCESS, aimItemId));
            }
        });
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
                AimItemDBHelper.deleteAimItemsByTypeIdSync(context,aimItemTypeId);

                EventPostUtil.post(new DatappEvent.deleteAimItemEvent(DatappEvent.STATUS_SUCCESS, aimItemTypeId));
            }
        });
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
                AimTypeDBHelper.deleteAimTypeByIdSync(context,aimTypeId);

                EventPostUtil.post(new DatappEvent.deleteAimTypeEvent(DatappEvent.STATUS_SUCCESS, aimTypeId));
            }
        });
    }

    //----------------------------------------------------------------------------------------------添加 or 修改

    /**
     * 新增 or 修改目标分类
     * @param context
     * @param aimTypeVO
     */
    public static void insertOrReplaceAimType(final Context context,final AimTypeVO aimTypeVO){
        if (context == null) {
            CPLogUtil.logDebug("deleteAimTypeById context can not be null");
            EventPostUtil.post(new DatappEvent.insertAimTypeEvent(DatappEvent.STATUS_FAIL_OHTERERROR, aimTypeVO));
            return;
        }

        CPThreadPoolManager.newInstance().addExecuteTask(new Runnable() {
            @Override
            public void run() {
                AimTypeDBHelper.insertOrReplaceAimTypeSync(context,aimTypeVO);

                EventPostUtil.post(new DatappEvent.insertAimTypeEvent(DatappEvent.STATUS_SUCCESS, aimTypeVO));
            }
        });
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
                AimTypeDBHelper.insertOrReplaceAimTypeListSync(context,aimTypeVOList);

                EventPostUtil.post(new DatappEvent.insertAimTypeEvent(DatappEvent.STATUS_SUCCESS, aimTypeVOList));
            }
        });
    }

    /**
     * 更新目标分类完成状态
     * @param context
     * @param aimTypeId
     * @param percent
     */
    public static void updateAimTypeStatus(final Context context, final Long aimTypeId, final float percent){
        if (context == null) {
            CPLogUtil.logDebug("updateAimTypeStatus context can not be null");
            EventPostUtil.post(new DatappEvent.updateAimTypeStatusEvent(DatappEvent.STATUS_FAIL_OHTERERROR, aimTypeId));
            return;
        }

        CPThreadPoolManager.newInstance().addExecuteTask(new Runnable() {
            @Override
            public void run() {
                AimTypeDBHelper.updateAimTypeStatusSync(context,aimTypeId,percent);

                EventPostUtil.post(new DatappEvent.updateAimTypeStatusEvent(DatappEvent.STATUS_SUCCESS, aimTypeId));
            }
        });

    }

    /**
     * 新增 or 修改目标项
     * @param context
     * @param aimTypeVO
     */
    public static void insertOrReplaceAimItem(final Context context,final AimItemVO aimTypeVO){
        if (context == null) {
            CPLogUtil.logDebug("deleteAimTypeById context can not be null");
            EventPostUtil.post(new DatappEvent.insertAimTypeEvent(DatappEvent.STATUS_FAIL_OHTERERROR, aimTypeVO));
            return;
        }

        CPThreadPoolManager.newInstance().addExecuteTask(new Runnable() {
            @Override
            public void run() {
                AimItemDBHelper.insertOrReplaceAimItemSync(context,aimTypeVO);

                EventPostUtil.post(new DatappEvent.insertAimTypeEvent(DatappEvent.STATUS_SUCCESS, aimTypeVO));
            }
        });
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
                AimItemDBHelper.insertOrReplaceAimItemListSync(context,aimItemVOList);

                EventPostUtil.post(new DatappEvent.insertAimTypeEvent(DatappEvent.STATUS_SUCCESS, aimItemVOList));
            }
        });
    }

    /**
     * 新增 or 修改用户
     * @param context
     * @param userInfoVO
     */
    public static void insertOrReplaceUserInfo(final Context context,final UserInfoVO userInfoVO){
        if (context == null) {
            CPLogUtil.logDebug("deleteAimTypeById context can not be null");
            EventPostUtil.post(new DatappEvent.insertUserInfoEvent(DatappEvent.STATUS_FAIL_OHTERERROR, userInfoVO));
            return;
        }

        CPThreadPoolManager.newInstance().addExecuteTask(new Runnable() {
            @Override
            public void run() {
                UserInfoDBHelper.insertOrReplaceUserInfoSync(context,userInfoVO);

                EventPostUtil.post(new DatappEvent.insertUserInfoEvent(DatappEvent.STATUS_SUCCESS, userInfoVO));
            }
        });
    }

}


