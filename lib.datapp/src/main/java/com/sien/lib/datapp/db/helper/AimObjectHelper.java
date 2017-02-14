package com.sien.lib.datapp.db.helper;

import android.content.Context;

import com.sien.lib.datapp.beans.AimObjectVO;
import com.sien.lib.datapp.db.AimObjectVODao;
import com.sien.lib.datapp.db.DBManager;

import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * @author sien
 * @date 2017/2/13
 * @descript 目标分类（自动创建项）数据库管理类
 */
public class AimObjectHelper {
    /**
     * 请求目标分类数据（自动创建 or 不可循环创建的目标分类）
     */
    public static List<AimObjectVO> requestAimObjectDatasSync(Context context){
        AimObjectVODao dao = DBManager.getInstance(context).getDaoSession().getAimObjectVODao();
        QueryBuilder<AimObjectVO> qb = dao.queryBuilder();
        qb.where(AimObjectVODao.Properties.Recyclable.eq(false));
        List<AimObjectVO> list = qb.list();
        return list;
    }

    /**
     * 请求目标分类数据(固定分类)
     */
    public static List<AimObjectVO> requestAimTypeFixedDatasSync(Context context){
        AimObjectVODao dao = DBManager.getInstance(context).getDaoSession().getAimObjectVODao();
        QueryBuilder<AimObjectVO> qb = dao.queryBuilder();
        qb.where(AimObjectVODao.Properties.Recyclable.eq(true));
        List<AimObjectVO> list = qb.list();
        return list;
    }

    /**
     * 根据周期请求目标分类数据(固定分类)
     * @param context
     * @param period
     * @return
     */
    public static List<AimObjectVO> requestAimTypeFixedDatasSync(Context context,int period){
        AimObjectVODao dao = DBManager.getInstance(context).getDaoSession().getAimObjectVODao();
        QueryBuilder<AimObjectVO> qb = dao.queryBuilder();
        qb.where(AimObjectVODao.Properties.Recyclable.eq(true),AimObjectVODao.Properties.Period.eq(period));
        List<AimObjectVO> list = qb.list();
        return list;
    }

    /**
     * 删除分类
     * @param context
     * @param aimTypeId
     */
    public static void deleteAimTypeByIdSync(Context context,Long aimTypeId){
        //删除目标类型，注：系统默认类型不可删除
//        AimItemVODao dao =  DBManager.getInstance(context).getDaoSession().getAimItemVODao();
//        dao.deleteByKey(aimTypeId);

        AimObjectVODao dao = DBManager.getInstance(context).getDaoSession().getAimObjectVODao();
        QueryBuilder queryBuilder = dao.queryBuilder().where(AimObjectVODao.Properties.Id.eq(aimTypeId),AimObjectVODao.Properties.Customed.eq(true));
        DeleteQuery deleteQuery = queryBuilder.buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
    }

    /**
     * 新增 or 修改目标分类
     * @param context
     * @param aimObjectVO
     */
    public static void insertOrReplaceAimTypeSync(Context context,AimObjectVO aimObjectVO){
        AimObjectVODao dao =  DBManager.getInstance(context).getDaoSession().getAimObjectVODao();
        dao.insertOrReplace(aimObjectVO);
    }

    public static void insertOrReplaceAimTypeListSync(Context context,final List<AimObjectVO> aimObjectVOList){
        if(aimObjectVOList == null || aimObjectVOList.isEmpty()){
            return;
        }
        final AimObjectVODao dao =  DBManager.getInstance(context).getDaoSession().getAimObjectVODao();
        dao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for(int i= 0; i<aimObjectVOList.size(); i++){
                    AimObjectVO note = aimObjectVOList.get(i);
                    dao.insertOrReplace(note);
                }
            }
        });
    }
}
