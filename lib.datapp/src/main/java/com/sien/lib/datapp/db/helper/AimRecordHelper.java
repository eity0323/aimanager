package com.sien.lib.datapp.db.helper;

import android.content.Context;

import com.sien.lib.datapp.beans.AimRecordVO;
import com.sien.lib.datapp.db.AimRecordVODao;
import com.sien.lib.datapp.db.DBManager;

import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * @author sien
 * @date 2017/2/13
 * @descript 目标项（自动创建）数据库管理类
 */
public class AimRecordHelper {
    /**
     * 请求目标记录数据
     */
    public static List<AimRecordVO> requestAimRecordDataSync(Context context){
        AimRecordVODao dao = DBManager.getInstance(context).getDaoSession().getAimRecordVODao();
        QueryBuilder<AimRecordVO> qb = dao.queryBuilder();
        List<AimRecordVO> list = qb.list();
        return list;
    }


    /**
     * 请求目标记录数据
     * @param context
     * @param aimTypeId 分类id
     */
    public static List<AimRecordVO> requestAimRecordDataSync(Context context,Long aimTypeId){
        AimRecordVODao dao = DBManager.getInstance(context).getDaoSession().getAimRecordVODao();
        QueryBuilder<AimRecordVO> qb = dao.queryBuilder();
        qb.where(AimRecordVODao.Properties.AimTypeId.eq(aimTypeId));
        List<AimRecordVO> list = qb.list();
        return list;
    }

    /**
     * 获取分类下的目标项记录数
     * @param context
     * @param aimTypeId
     * @return
     */
    public static long requestAimRecordCountSync(Context context,Long aimTypeId){
        AimRecordVODao dao = DBManager.getInstance(context).getDaoSession().getAimRecordVODao();
        QueryBuilder<AimRecordVO> qb = dao.queryBuilder();
        qb.where(AimRecordVODao.Properties.AimTypeId.eq(aimTypeId));
        return qb.buildCount().count();
    }

    /**
     * 删除记录item
     * @param context
     * @param aimTypeId
     */
    public static void deleteAimRecordByIdSync(Context context,Long aimTypeId){
        AimRecordVODao dao = DBManager.getInstance(context).getDaoSession().getAimRecordVODao();
        dao.deleteByKey(aimTypeId);
    }


    /**
     * 根据分类删除记录item
     * @param context
     * @param aimTypeId
     */
    public static void deleteAimRecordByTypeIdSync(Context context,Long aimTypeId){
        AimRecordVODao dao = DBManager.getInstance(context).getDaoSession().getAimRecordVODao();
        QueryBuilder queryBuilder = dao.queryBuilder().where(AimRecordVODao.Properties.AimTypeId.eq(aimTypeId));;
        DeleteQuery deleteQuery = queryBuilder.buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
    }

    /**
     * 新增目标项
     * @param context
     * @param aimRecordVO
     */
    public static void insertOrReplaceAimRecordSync(Context context,AimRecordVO aimRecordVO){
        AimRecordVODao dao = DBManager.getInstance(context).getDaoSession().getAimRecordVODao();
        dao.insertOrReplace(aimRecordVO);
    }

    public static void insertOrReplaceAimRecordListSync(Context context,final List<AimRecordVO> aimRecordVOList){
        if(aimRecordVOList == null || aimRecordVOList.isEmpty()){
            return;
        }
        final AimRecordVODao dao = DBManager.getInstance(context).getDaoSession().getAimRecordVODao();
        dao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for(int i= 0; i<aimRecordVOList.size(); i++){
                    AimRecordVO note = aimRecordVOList.get(i);
                    dao.insertOrReplace(note);
                }
            }
        });
    }
}
