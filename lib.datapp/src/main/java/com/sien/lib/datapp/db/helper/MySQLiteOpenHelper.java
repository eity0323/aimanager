package com.sien.lib.datapp.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.sien.lib.datapp.db.AimItemVODao;
import com.sien.lib.datapp.db.AimObjectVODao;
import com.sien.lib.datapp.db.AimRecordVODao;
import com.sien.lib.datapp.db.AimTypeVODao;
import com.sien.lib.datapp.db.DaoMaster;

/**
 * @author sien
 * @date 2017/2/14
 * @descript 数据库管理类，替换DaoMaster中的DevOpenHelper
 */
public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db,AimTypeVODao.class,AimItemVODao.class,AimObjectVODao.class, AimRecordVODao.class);
    }
}
