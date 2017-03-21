package com.sien.lib.databmob.db.helper;

import android.content.Context;

import com.sien.lib.databmob.beans.UserInfoVO;
import com.sien.lib.databmob.db.DBManager;
import com.sien.lib.databmob.db.UserInfoVODao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * @author sien
 * @date 2017/2/13
 * @descript 用户信息数据库管理类
 */
public class UserInfoDBHelper {
    /**
     * 查询用户信息
     * @param context
     * @param userName
     */
    public static List<UserInfoVO> requestUserInfoDataSync(Context context, String userName){
        UserInfoVODao dao = DBManager.getInstance(context).getDaoSession().getUserInfoVODao();
        QueryBuilder<UserInfoVO> qb = dao.queryBuilder();
        qb.where(UserInfoVODao.Properties.UserName.eq(userName));
        List<UserInfoVO> list = qb.list();
        return list;
    }

    public static List<UserInfoVO> requestUserInfoDataSync(Context context){
        UserInfoVODao dao = DBManager.getInstance(context).getDaoSession().getUserInfoVODao();
        QueryBuilder<UserInfoVO> qb = dao.queryBuilder();
        List<UserInfoVO> list = qb.list();
        return list;
    }

    /**
     * 新增用户信息
     * @param context
     * @param userInfoVO
     */
    public static void insertOrReplaceUserInfoSync(Context context,UserInfoVO userInfoVO){
        UserInfoVODao dao =  DBManager.getInstance(context).getDaoSession().getUserInfoVODao();
        dao.insertOrReplace(userInfoVO);
    }
}
