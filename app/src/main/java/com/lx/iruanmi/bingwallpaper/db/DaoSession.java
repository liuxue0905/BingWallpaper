package com.lx.iruanmi.bingwallpaper.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 *
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig bingDaoConfig;

    private final BingDao bingDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        bingDaoConfig = daoConfigMap.get(BingDao.class).clone();
        bingDaoConfig.initIdentityScope(type);

        bingDao = new BingDao(bingDaoConfig, this);

        registerDao(Bing.class, bingDao);
    }

    public void clear() {
        bingDaoConfig.getIdentityScope().clear();
    }

    public BingDao getBingDao() {
        return bingDao;
    }

}
