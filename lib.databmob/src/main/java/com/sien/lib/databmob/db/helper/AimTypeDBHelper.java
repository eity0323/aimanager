package com.sien.lib.databmob.db.helper;

import android.content.Context;

import com.sien.lib.databmob.beans.AimTypeVO;
import com.sien.lib.databmob.beans.CoverIcon;
import com.sien.lib.databmob.control.CPSharedPreferenceManager;
import com.sien.lib.databmob.db.AimTypeVODao;
import com.sien.lib.databmob.db.DBManager;
import com.sien.lib.databmob.utils.CPLogUtil;

import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Date;
import java.util.List;

/**
 * @author sien
 * @date 2017/2/13
 * @descript 目标分类（非自动创建）数据库管理类
 *
 * String updateQuery = "update "+PersonDao.TABLENAME
+ " set "+PersonDao.Properties.Name.columnName + "=?"
+" where " + PersonDao.Properties.Id.columnName + "=?";

mDaoMaster.getDatabase().execSQL(updateQuery, new Object[] {"VISHAL", 10});
 */
public class AimTypeDBHelper {
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
        aimTypeVO.setPlanProject(false);
        aimTypeVO.setActive(false);
        aimTypeVO.setCover(CoverIcon.ICON_DAY.getValue());
        aimTypeVO.setTypeName("每天");

        insertOrReplaceAimTypeSync(context,aimTypeVO);

        //aim item
        AimItemDBHelper.createInitialAimItem(context,aimTypeVO.getId(),AimTypeVO.PERIOD_DAY);

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
        aimTypeVO.setPlanProject(false);
        aimTypeVO.setActive(false);
        aimTypeVO.setCover(CoverIcon.ICON_WEEK.getValue());
        aimTypeVO.setTypeName("每周");

        insertOrReplaceAimTypeSync(context,aimTypeVO);

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
        aimTypeVO.setPlanProject(false);
        aimTypeVO.setActive(false);
        aimTypeVO.setCover(CoverIcon.ICON_MONTH.getValue());
        aimTypeVO.setTypeName("每月");

        insertOrReplaceAimTypeSync(context,aimTypeVO);

        //aim item
        AimItemDBHelper.createInitialAimItem(context,aimTypeVO.getId(),AimTypeVO.PERIOD_MONTH);

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
        aimTypeVO.setPlanProject(false);
        aimTypeVO.setActive(false);
        aimTypeVO.setCover(CoverIcon.ICON_SEASON.getValue());
        aimTypeVO.setTypeName("每季");

        insertOrReplaceAimTypeSync(context,aimTypeVO);

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
        aimTypeVO.setPlanProject(false);
        aimTypeVO.setActive(false);
        aimTypeVO.setCover(CoverIcon.ICON_HALFYEAR.getValue());
        aimTypeVO.setTypeName("半年");

        insertOrReplaceAimTypeSync(context,aimTypeVO);

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
        aimTypeVO.setPlanProject(false);
        aimTypeVO.setActive(false);
        aimTypeVO.setCover(CoverIcon.ICON_YEAR.getValue());
        aimTypeVO.setTypeName("每年");

        insertOrReplaceAimTypeSync(context,aimTypeVO);

        CPSharedPreferenceManager.getInstance(context).saveData(CPSharedPreferenceManager.SETTINGS_DEFAULT_DATA,"true");

        CPLogUtil.logDebug("end to generate default data");
    }

    /**
     * 请求目标分类数据(all)
     */
    public static List<AimTypeVO> requestAimTypeSync(Context context){
        AimTypeVODao dao = DBManager.getInstance(context).getDaoSession().getAimTypeVODao();
        QueryBuilder<AimTypeVO> qb = dao.queryBuilder();
        List<AimTypeVO> list = qb.list();
        return list;
    }

    public static AimTypeVO requestAimTypeDatasByIdSync(Context context,Long aimTypeId){
        AimTypeVODao dao = DBManager.getInstance(context).getDaoSession().getAimTypeVODao();
        QueryBuilder<AimTypeVO> qb = dao.queryBuilder();
        AimTypeVO aimTypeVO = dao.load(aimTypeId);
        return aimTypeVO;
    }

    /**
     * 请求目标分类数据(固定分类)
     */
    public static List<AimTypeVO> requestAimTypeFixedSync(Context context){
        AimTypeVODao dao = DBManager.getInstance(context).getDaoSession().getAimTypeVODao();
        QueryBuilder<AimTypeVO> qb = dao.queryBuilder();
        qb.where(AimTypeVODao.Properties.Recyclable.eq(true));
        List<AimTypeVO> list = qb.list();
        return list;
    }

    /**
     * 请求目标分类数据(自动创建分类)
     */
    public static List<AimTypeVO> requestAimTypeAutoDatasSync(Context context){
        AimTypeVODao dao = DBManager.getInstance(context).getDaoSession().getAimTypeVODao();
        QueryBuilder<AimTypeVO> qb = dao.queryBuilder();
        qb.where(AimTypeVODao.Properties.Recyclable.eq(false));
        List<AimTypeVO> list = qb.list();
        return list;
    }

    /**
     * 根据周期请求目标分类数据(固定分类)
     * @param context
     * @param period
     * @return
     */
    public static List<AimTypeVO> requestAimTypeFixedSync(Context context,int period){
        AimTypeVODao dao = DBManager.getInstance(context).getDaoSession().getAimTypeVODao();
        QueryBuilder<AimTypeVO> qb = dao.queryBuilder();
        qb.where(AimTypeVODao.Properties.Recyclable.eq(true),AimTypeVODao.Properties.Period.eq(period));
        List<AimTypeVO> list = qb.list();
        return list;
    }

    /**
     *
     * @param context
     * @param date
     * @return
     */
    public static List<AimTypeVO> requestAimTypeFixedByDate(Context context, Date date){
        AimTypeVODao dao = DBManager.getInstance(context).getDaoSession().getAimTypeVODao();
        QueryBuilder<AimTypeVO> qb = dao.queryBuilder();
        qb.where(AimTypeVODao.Properties.Recyclable.eq(true),AimTypeVODao.Properties.StartTime.between(DBDateHelper.getDayStartMillisecond(date),DBDateHelper.getDayEndMillisecond(date)));
        List<AimTypeVO> list = qb.list();
        return list;
    }

    /**
     * 根据周期请求目标分类数据(自动创建分类)
     * @param context
     * @param period
     * @return
     */
    public static List<AimTypeVO> requestAimTypeAutoDatasSync(Context context,int period){
        AimTypeVODao dao = DBManager.getInstance(context).getDaoSession().getAimTypeVODao();
        QueryBuilder<AimTypeVO> qb = dao.queryBuilder();
        qb.where(AimTypeVODao.Properties.Recyclable.eq(false),AimTypeVODao.Properties.Period.eq(period));
        List<AimTypeVO> list = qb.list();
        return list;
    }

    /**
     * 根据日期查询分类目标
     * @param context
     * @param date
     * @return
     */
    public static List<AimTypeVO> requestAimTypeAutoByDate(Context context, Date date){
        AimTypeVODao dao = DBManager.getInstance(context).getDaoSession().getAimTypeVODao();
        QueryBuilder<AimTypeVO> qb = dao.queryBuilder();
        qb.where(AimTypeVODao.Properties.Recyclable.eq(false),AimTypeVODao.Properties.StartTime.between(DBDateHelper.getDayStartMillisecond(date),DBDateHelper.getDayEndMillisecond(date)));
        List<AimTypeVO> list = qb.list();
        return list;
    }

    /**
     * 根据父目标分类id查询已自动创建的分类
     * @param context
     * @param aimTypeId
     * @return
     */
    public static List<AimTypeVO> requestAimTypeAutoByAimTypeIdSync(Context context,Long aimTypeId){
        AimTypeVODao dao = DBManager.getInstance(context).getDaoSession().getAimTypeVODao();
        QueryBuilder<AimTypeVO> qb = dao.queryBuilder();
        qb.where(AimTypeVODao.Properties.Recyclable.eq(false),AimTypeVODao.Properties.FirstExtra.eq(aimTypeId));
        List<AimTypeVO> list = qb.list();
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

        AimTypeVODao dao = DBManager.getInstance(context).getDaoSession().getAimTypeVODao();
        QueryBuilder queryBuilder = dao.queryBuilder().where(AimTypeVODao.Properties.Id.eq(aimTypeId),AimTypeVODao.Properties.Customed.eq(true));
        DeleteQuery deleteQuery = queryBuilder.buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
    }

    /**
     * 新增 or 修改目标分类
     * @param context
     * @param aimTypeVO
     */
    public static void insertOrReplaceAimTypeSync(Context context,AimTypeVO aimTypeVO){
        AimTypeVODao dao =  DBManager.getInstance(context).getDaoSession().getAimTypeVODao();
        dao.insertOrReplace(aimTypeVO);
    }

    public static void insertOrReplaceAimTypeListSync(Context context,final List<AimTypeVO> aimTypeVOList){
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

    /**
     * 更新目标分类完成状态
     * @param context
     * @param aimTypeId
     * @param percent
     */
    public static void updateAimTypeStatusSync(Context context,Long aimTypeId,float percent){
        AimTypeVODao dao = DBManager.getInstance(context).getDaoSession().getAimTypeVODao();
        AimTypeVO itemVO = dao.load(aimTypeId);
        int val = (int)percent * 100;
        itemVO.setFinishPercent(val);
        dao.update(itemVO);
    }
}
