package com.sien.lib.databmob.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.sien.lib.databmob.db.AimItemVODao;
import com.sien.lib.databmob.db.AimTypeVODao;
import com.sien.lib.databmob.db.DaoMaster;

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
        MigrationHelper.migrate(db,AimTypeVODao.class,AimItemVODao.class);
    }
}
