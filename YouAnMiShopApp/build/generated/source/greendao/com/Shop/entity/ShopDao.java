package com.Shop.entity;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SHOP".
*/
public class ShopDao extends AbstractDao<Shop, Long> {

    public static final String TABLENAME = "SHOP";

    /**
     * Properties of entity Shop.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Clickid = new Property(1, String.class, "clickid", false, "CLICKID");
        public final static Property Isclick = new Property(2, Boolean.class, "isclick", false, "ISCLICK");
    }


    public ShopDao(DaoConfig config) {
        super(config);
    }
    
    public ShopDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SHOP\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"CLICKID\" TEXT," + // 1: clickid
                "\"ISCLICK\" INTEGER);"); // 2: isclick
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SHOP\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Shop entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String clickid = entity.getClickid();
        if (clickid != null) {
            stmt.bindString(2, clickid);
        }
 
        Boolean isclick = entity.getIsclick();
        if (isclick != null) {
            stmt.bindLong(3, isclick ? 1L: 0L);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Shop entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String clickid = entity.getClickid();
        if (clickid != null) {
            stmt.bindString(2, clickid);
        }
 
        Boolean isclick = entity.getIsclick();
        if (isclick != null) {
            stmt.bindLong(3, isclick ? 1L: 0L);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Shop readEntity(Cursor cursor, int offset) {
        Shop entity = new Shop( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // clickid
            cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0 // isclick
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Shop entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setClickid(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setIsclick(cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Shop entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Shop entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Shop entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
