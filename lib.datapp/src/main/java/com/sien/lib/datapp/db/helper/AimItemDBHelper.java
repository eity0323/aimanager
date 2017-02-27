package com.sien.lib.datapp.db.helper;

import android.content.Context;

import com.sien.lib.datapp.beans.AimItemVO;
import com.sien.lib.datapp.beans.AimTypeVO;
import com.sien.lib.datapp.db.AimItemVODao;
import com.sien.lib.datapp.db.DBManager;

import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Date;
import java.util.List;

/**
 * @author sien
 * @date 2017/2/13
 * @descript 目标项管理类（非自动创建）数据库管理类
 */
public class AimItemDBHelper {
    //----------------------------------------------------------------------------------------------初始化系统数据（默认目标项）
    public static void createInitialAimItem(Context context,Long aimTypeId,int period){
        if (period == AimTypeVO.PERIOD_DAY){
            //every day
            AimItemVO itemVO = new AimItemVO();
            itemVO.setAimName("运动半小时");
            itemVO.setDesc("运动半小时");
            itemVO.setModifyTime(new Date());
            itemVO.setFinishStatus(AimItemVO.STATUS_UNDO);
            itemVO.setPriority(AimItemVO.PRIORITY_FIVE);
            itemVO.setFinishPercent(0);
            itemVO.setAimTypeId(aimTypeId);

            insertOrReplaceAimItemSync(context,itemVO);
        }else if (period == AimTypeVO.PERIOD_MONTH){
            //every month
            AimItemVO itemVO = new AimItemVO();
            itemVO.setAimName("阅读一本书");
            itemVO.setDesc("阅读一本书");
            itemVO.setModifyTime(new Date());
            itemVO.setFinishStatus(AimItemVO.STATUS_UNDO);
            itemVO.setPriority(AimItemVO.PRIORITY_FIVE);
            itemVO.setFinishPercent(0);
            itemVO.setAimTypeId(aimTypeId);

            insertOrReplaceAimItemSync(context,itemVO);
        }
    }

    /**
     * 请求目标记录数据
     */
    public static List<AimItemVO> requestAimItemDataSync(Context context){
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
     * 删除记录item
     * @param context
     * @param aimTypeId
     */
    public static void deleteAimItemByIdSync(Context context,Long aimTypeId){
        AimItemVODao dao = DBManager.getInstance(context).getDaoSession().getAimItemVODao();
        dao.deleteByKey(aimTypeId);
    }


    /**
     * 根据分类删除记录item
     * @param context
     * @param aimTypeId
     */
    public static void deleteAimItemsByTypeIdSync(Context context,Long aimTypeId){
        AimItemVODao dao = DBManager.getInstance(context).getDaoSession().getAimItemVODao();
        QueryBuilder queryBuilder = dao.queryBuilder().where(AimItemVODao.Properties.AimTypeId.eq(aimTypeId));;
        DeleteQuery deleteQuery = queryBuilder.buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
    }

    /**
     * 新增目标项
     * @param context
     * @param aimItemVO
     */
    public static void insertOrReplaceAimItemSync(Context context,AimItemVO aimItemVO){
        AimItemVODao dao =  DBManager.getInstance(context).getDaoSession().getAimItemVODao();
        dao.insertOrReplace(aimItemVO);
    }

    public static void insertOrReplaceAimItemListSync(Context context,final List<AimItemVO> aimItemVOList){
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

}
