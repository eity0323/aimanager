package com.sien.lib.datapp.db;

import com.sien.lib.datapp.beans.AimItemVO;
import com.sien.lib.datapp.beans.AimObjectVO;
import com.sien.lib.datapp.beans.AimRecordVO;
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

    private final DaoConfig aimRecordVODaoConfig;
    private final AimRecordVODao aimRecordVODao;

    private final DaoConfig aimObjectVODaoConfig;
    private final AimObjectVODao aimObjectVODao;

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

        aimRecordVODaoConfig = daoConfigMap.get(AimRecordVODao.class).clone();
        aimRecordVODaoConfig.initIdentityScope(type);
        aimRecordVODao = new AimRecordVODao(aimRecordVODaoConfig, this);
        registerDao(AimRecordVO.class, aimRecordVODao);

        aimObjectVODaoConfig = daoConfigMap.get(AimObjectVODao.class).clone();
        aimObjectVODaoConfig.initIdentityScope(type);
        aimObjectVODao = new AimObjectVODao(aimObjectVODaoConfig, this);
        registerDao(AimObjectVO.class, aimObjectVODao);
    }
    
    public void clear() {
        aimTypeVODaoConfig.clearIdentityScope();
        aimItemVODaoConfig.clearIdentityScope();
        userInfoVODaoConfig.clearIdentityScope();
        aimRecordVODaoConfig.clearIdentityScope();
        aimObjectVODaoConfig.clearIdentityScope();
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

    public AimRecordVODao getAimRecordVODao() {
        return aimRecordVODao;
    }

    public AimObjectVODao getAimObjectVODao() {
        return aimObjectVODao;
    }
}
