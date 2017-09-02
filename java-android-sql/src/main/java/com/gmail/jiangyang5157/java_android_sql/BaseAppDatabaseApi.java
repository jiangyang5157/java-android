package com.gmail.jiangyang5157.java_android_sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

/**
 * Database helper, internal APIs
 *
 * @author Yang 9/16/2015.
 */
public abstract class BaseAppDatabaseApi {

    protected SQLiteDatabase db = null;

    private BaseAppDatabaseOpenHelper dbOpenHelper = null;

    /**
     * orderBy means:"cast (" + key + " as integer)" + orderBy
     * <p>
     * Example usage for params orderBy: Table.KEY + " " + ORDER_BY_DESC
     * </p>
     */
    public static final String ORDER_BY_DESC = "desc";
    public static final String ORDER_BY_ASC = "asc";

    public static String buildOrderByDesc(String key) {
        return buildOrderBy(key, ORDER_BY_DESC);
    }

    public static String buildOrderByAsc(String key) {
        return buildOrderBy(key, ORDER_BY_ASC);
    }

    private static String buildOrderBy(String key, String orderBy) {
        return key + " " + orderBy;
    }

    protected abstract BaseAppDatabaseOpenHelper getAppDatabaseOpenHelper(Context context);

    protected BaseAppDatabaseApi(Context context) {
        dbOpenHelper = getAppDatabaseOpenHelper(context);
    }

    /**
     * call open() before any sqlite operation
     */
    protected void open() {
        try {
            db = dbOpenHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            db = dbOpenHelper.getReadableDatabase();
        }
    }

    protected void close() {
        dbOpenHelper.close();
    }

    /**
     * beginTransaction() before call execSQL
     */
    protected void startTransaction() {
        db.beginTransaction();
    }

    protected void finishTransaction() {
        try {
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * call execSQLs
     */
    protected void execSQLs(String[] sqls) {
        db.beginTransaction();
        for (String sql : sqls) {
            db.execSQL(sql);
        }
        db.setTransactionSuccessful();
    }

    /**
     * call execSQL
     */
    protected void execSQL(String sql) {
        db.execSQL(sql);
    }

    /**
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    protected long insert(String tableName, ContentValues cv) {
        return db.insert(tableName, null, cv);
    }

    /**
     * @return the number of rows affected, return value <= 0 means failed
     */
    protected int update(String tableName, String rowId, ContentValues cv) {
        return db.update(tableName, cv, BaseTable.KEY_ROWID + " = ?", new String[]{rowId});
    }

    protected Cursor query(String tableName, String col[], String orderBy) {
        Cursor ret = db.query(tableName, col, null, null, null, null, orderBy);
        if (ret != null) {
            ret.moveToFirst();
        }
        return ret;
    }

    /**
     * key equals value
     */
    protected Cursor queryValue(String tableName, String col[], String key, String value, String orderBy) {
        Cursor ret = db.query(tableName, col, key + " = ?", new String[]{value}, null, null, orderBy);
        if (ret != null) {
            ret.moveToFirst();
        }
        return ret;
    }

    /**
     * key contains like
     */
    protected Cursor queryLike(String tableName, String col[], String key, String like, String orderBy) {
        Cursor ret = db.query(tableName, col, key + " like ?", new String[]{"%" + like + "%"}, null, null, orderBy);
        if (ret != null) {
            ret.moveToFirst();
        }
        return ret;
    }

    /**
     * @return the number of rows affected, return value <= 0 means failed
     */
    protected int clear(String tableName) {
        return db.delete(tableName, null, null);
    }

    /**
     * by key equals value
     *
     * @return the number of rows affected, return value <= 0 means failed
     */
    protected int delete(String tableName, String key, String value) {
        return db.delete(tableName, key + " = ?", new String[]{value});
    }
}
