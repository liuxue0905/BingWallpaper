package com.lx.iruanmi.bingwallpaper.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table BING.
 */
public class BingDao extends AbstractDao<Bing, Long> {

    public static final String TABLENAME = "BING";

    public BingDao(DaoConfig config) {
        super(config);
    }

    ;


    public BingDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /**
     * Creates the underlying database table.
     */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "'BING' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'ID' TEXT," + // 1: ID
                "'BING_DATE' TEXT NOT NULL ," + // 2: bing_date
                "'BING_COUNTRY' TEXT NOT NULL ," + // 3: bing_country
                "'BING_PICNAME' TEXT," + // 4: bing_picname
                "'BING_MAXPIX' TEXT," + // 5: bing_maxpix
                "'BING_16X9' TEXT," + // 6: bing_16x9
                "'BING_9X16' TEXT," + // 7: bing_9x16
                "'BING_9X15' TEXT," + // 8: bing_9x15
                "'BING_COPYRIGHT' TEXT);"); // 9: bing_copyright
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_BING_BING_DATE_BING_COUNTRY ON BING" +
                " (BING_DATE,BING_COUNTRY);");
    }

    /**
     * Drops the underlying database table.
     */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'BING'";
        db.execSQL(sql);
    }

    /**
     * @inheritdoc
     */
    @Override
    protected void bindValues(SQLiteStatement stmt, Bing entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }

        String ID = entity.getID();
        if (ID != null) {
            stmt.bindString(2, ID);
        }
        stmt.bindString(3, entity.getBing_date());
        stmt.bindString(4, entity.getBing_country());

        String bing_picname = entity.getBing_picname();
        if (bing_picname != null) {
            stmt.bindString(5, bing_picname);
        }

        String bing_maxpix = entity.getBing_maxpix();
        if (bing_maxpix != null) {
            stmt.bindString(6, bing_maxpix);
        }

        String bing_16x9 = entity.getBing_16x9();
        if (bing_16x9 != null) {
            stmt.bindString(7, bing_16x9);
        }

        String bing_9x16 = entity.getBing_9x16();
        if (bing_9x16 != null) {
            stmt.bindString(8, bing_9x16);
        }

        String bing_9x15 = entity.getBing_9x15();
        if (bing_9x15 != null) {
            stmt.bindString(9, bing_9x15);
        }

        String bing_copyright = entity.getBing_copyright();
        if (bing_copyright != null) {
            stmt.bindString(10, bing_copyright);
        }
    }

    /**
     * @inheritdoc
     */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    /**
     * @inheritdoc
     */
    @Override
    public Bing readEntity(Cursor cursor, int offset) {
        Bing entity = new Bing( //
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
                cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // ID
                cursor.getString(offset + 2), // bing_date
                cursor.getString(offset + 3), // bing_country
                cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // bing_picname
                cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // bing_maxpix
                cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // bing_16x9
                cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // bing_9x16
                cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // bing_9x15
                cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9) // bing_copyright
        );
        return entity;
    }

    /**
     * @inheritdoc
     */
    @Override
    public void readEntity(Cursor cursor, Bing entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setID(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setBing_date(cursor.getString(offset + 2));
        entity.setBing_country(cursor.getString(offset + 3));
        entity.setBing_picname(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setBing_maxpix(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setBing_16x9(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setBing_9x16(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setBing_9x15(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setBing_copyright(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
    }

    /**
     * @inheritdoc
     */
    @Override
    protected Long updateKeyAfterInsert(Bing entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }

    /**
     * @inheritdoc
     */
    @Override
    public Long getKey(Bing entity) {
        if (entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /**
     * @inheritdoc
     */
    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }

    /**
     * Properties of entity Bing.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property ID = new Property(1, String.class, "ID", false, "ID");
        public final static Property Bing_date = new Property(2, String.class, "bing_date", false, "BING_DATE");
        public final static Property Bing_country = new Property(3, String.class, "bing_country", false, "BING_COUNTRY");
        public final static Property Bing_picname = new Property(4, String.class, "bing_picname", false, "BING_PICNAME");
        public final static Property Bing_maxpix = new Property(5, String.class, "bing_maxpix", false, "BING_MAXPIX");
        public final static Property Bing_16x9 = new Property(6, String.class, "bing_16x9", false, "BING_16X9");
        public final static Property Bing_9x16 = new Property(7, String.class, "bing_9x16", false, "BING_9X16");
        public final static Property Bing_9x15 = new Property(8, String.class, "bing_9x15", false, "BING_9X15");
        public final static Property Bing_copyright = new Property(9, String.class, "bing_copyright", false, "BING_COPYRIGHT");
    }

}
