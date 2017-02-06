package com.sien.lib.datapp.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.sien.lib.datapp.beans.AimItemVO;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "AIM_ITEM_VO".
*/
public class AimItemVODao extends AbstractDao<AimItemVO, Long> {

    public static final String TABLENAME = "AIM_ITEM_VO";

    /**
     * Properties of entity AimItemVO.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "ID");
        public final static Property AimName = new Property(1, String.class, "aimName", false, "AIM_NAME");
        public final static Property Desc = new Property(2, String.class, "desc", false, "DESC");
        public final static Property AimTypeId = new Property(3, Long.class, "aimTypeId", false, "AIM_TYPE_ID");
        public final static Property StartTime = new Property(4, java.util.Date.class, "startTime", false, "START_TIME");
        public final static Property EndTime = new Property(5, java.util.Date.class, "endTime", false, "END_TIME");
        public final static Property ModifyTime = new Property(6, java.util.Date.class, "modifyTime", false, "MODIFY_TIME");
        public final static Property FinishStatus = new Property(7, Integer.class, "finishStatus", false, "FINISH_STATUS");
        public final static Property FinishPercent = new Property(8, Integer.class, "finishPercent", false, "FINISH_PERCENT");
        public final static Property Priority = new Property(9, Integer.class, "priority", false, "PRIORITY");
        public final static Property Cover = new Property(10, String.class, "cover", false, "COVER");
    }


    public AimItemVODao(DaoConfig config) {
        super(config);
    }
    
    public AimItemVODao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"AIM_ITEM_VO\" (" + //
                "\"ID\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"AIM_NAME\" TEXT," + // 1: aimName
                "\"DESC\" TEXT," + // 2: desc
                "\"AIM_TYPE_ID\" INTEGER," + // 3: aimTypeId
                "\"START_TIME\" INTEGER," + // 4: startTime
                "\"END_TIME\" INTEGER," + // 5: endTime
                "\"MODIFY_TIME\" INTEGER," + // 6: modifyTime
                "\"FINISH_STATUS\" INTEGER," + // 7: finishStatus
                "\"FINISH_PERCENT\" INTEGER," + // 8: finishPercent
                "\"PRIORITY\" INTEGER," + // 9: priority
                "\"COVER\" TEXT);"); // 10: cover
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"AIM_ITEM_VO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, AimItemVO entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String aimName = entity.getAimName();
        if (aimName != null) {
            stmt.bindString(2, aimName);
        }
 
        String desc = entity.getDesc();
        if (desc != null) {
            stmt.bindString(3, desc);
        }
 
        Long aimTypeId = entity.getAimTypeId();
        if (aimTypeId != null) {
            stmt.bindLong(4, aimTypeId);
        }
 
        java.util.Date startTime = entity.getStartTime();
        if (startTime != null) {
            stmt.bindLong(5, startTime.getTime());
        }
 
        java.util.Date endTime = entity.getEndTime();
        if (endTime != null) {
            stmt.bindLong(6, endTime.getTime());
        }
 
        java.util.Date modifyTime = entity.getModifyTime();
        if (modifyTime != null) {
            stmt.bindLong(7, modifyTime.getTime());
        }
 
        Integer finishStatus = entity.getFinishStatus();
        if (finishStatus != null) {
            stmt.bindLong(8, finishStatus);
        }
 
        Integer finishPercent = entity.getFinishPercent();
        if (finishPercent != null) {
            stmt.bindLong(9, finishPercent);
        }
 
        Integer priority = entity.getPriority();
        if (priority != null) {
            stmt.bindLong(10, priority);
        }
 
        String cover = entity.getCover();
        if (cover != null) {
            stmt.bindString(11, cover);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, AimItemVO entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String aimName = entity.getAimName();
        if (aimName != null) {
            stmt.bindString(2, aimName);
        }
 
        String desc = entity.getDesc();
        if (desc != null) {
            stmt.bindString(3, desc);
        }
 
        Long aimTypeId = entity.getAimTypeId();
        if (aimTypeId != null) {
            stmt.bindLong(4, aimTypeId);
        }
 
        java.util.Date startTime = entity.getStartTime();
        if (startTime != null) {
            stmt.bindLong(5, startTime.getTime());
        }
 
        java.util.Date endTime = entity.getEndTime();
        if (endTime != null) {
            stmt.bindLong(6, endTime.getTime());
        }
 
        java.util.Date modifyTime = entity.getModifyTime();
        if (modifyTime != null) {
            stmt.bindLong(7, modifyTime.getTime());
        }
 
        Integer finishStatus = entity.getFinishStatus();
        if (finishStatus != null) {
            stmt.bindLong(8, finishStatus);
        }
 
        Integer finishPercent = entity.getFinishPercent();
        if (finishPercent != null) {
            stmt.bindLong(9, finishPercent);
        }
 
        Integer priority = entity.getPriority();
        if (priority != null) {
            stmt.bindLong(10, priority);
        }
 
        String cover = entity.getCover();
        if (cover != null) {
            stmt.bindString(11, cover);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public AimItemVO readEntity(Cursor cursor, int offset) {
        AimItemVO entity = new AimItemVO( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // aimName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // desc
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // aimTypeId
            cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)), // startTime
            cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)), // endTime
            cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)), // modifyTime
            cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7), // finishStatus
            cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8), // finishPercent
            cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9), // priority
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10) // cover
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, AimItemVO entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setAimName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDesc(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAimTypeId(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setStartTime(cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)));
        entity.setEndTime(cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)));
        entity.setModifyTime(cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)));
        entity.setFinishStatus(cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7));
        entity.setFinishPercent(cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8));
        entity.setPriority(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
        entity.setCover(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(AimItemVO entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(AimItemVO entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(AimItemVO entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
