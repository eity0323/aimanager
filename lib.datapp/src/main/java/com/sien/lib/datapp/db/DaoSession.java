package com.sien.lib.datapp.db;

import com.sien.lib.datapp.beans.AimItemVO;
import com.sien.lib.datapp.beans.AimTypeVO;
import com.sien.lib.datapp.beans.UserInfoVO;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;

/**
 * {@inheritDoc}
 * 
 * @see AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig aimTypeVODaoConfig;
    private final AimTypeVODao aimTypeVODao;

    private final DaoConfig aimItemVODaoConfig;
    private final AimItemVODao aimItemVODao;

    private final DaoConfig userInfoVODaoConfig;
    private final UserInfoVODao userInfoVODao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        aimTypeVODaoConfig = daoConfigMap.get(AimTypeVODao.class).clone();
        aimTypeVODaoConfig.initIdentityScope(type);
        aimTypeVODao = new AimTypeVODao(aimTypeVODaoConfig, this);
        registerDao(AimTypeVO.class, aimTypeVODao);

        aimItemVODaoConfig = daoConfigMap.get(AimItemVODao.class).clone();
        aimItemVODaoConfig.initIdentityScope(type);
        aimItemVODao = new AimItemVODao(aimItemVODaoConfig, this);
        registerDao(AimItemVO.class, aimItemVODao);

        userInfoVODaoConfig = daoConfigMap.get(UserInfoVODao.class).clone();
        userInfoVODaoConfig.initIdentityScope(type);
        userInfoVODao = new UserInfoVODao(userInfoVODaoConfig, this);
        registerDao(UserInfoVO.class, userInfoVODao);
    }
    
    public void clear() {
        aimTypeVODaoConfig.clearIdentityScope();
        aimItemVODaoConfig.clearIdentityScope();
        userInfoVODaoConfig.clearIdentityScope();
    }

    public AimTypeVODao getAimTypeVODao() {
        return aimTypeVODao;
    }

    public AimItemVODao getAimItemVODao() {
        return aimItemVODao;
    }

    public UserInfoVODao getUserInfoVODao() {
        return userInfoVODao;
    }
}
