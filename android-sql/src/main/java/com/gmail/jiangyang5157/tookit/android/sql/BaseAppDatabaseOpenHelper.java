package com.gmail.jiangyang5157.tookit.android.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author Yang 9/16/2015.
 */
public abstract class BaseAppDatabaseOpenHelper extends SQLiteOpenHelper {
    private final static String TAG = "[BAppDbOpenHelper]";

    protected abstract String[] getCreateTableSqlsOnCreate();

    protected abstract String[] getDropTableNamesOnUpgrade();

    protected BaseAppDatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String[] sqls = getCreateTableSqlsOnCreate();
        for (String sql : sqls) {
            db.execSQL(sql);
            Log.d(TAG, sql);
        }
    }

    /**
     * This will be called when you change version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Database going to upgrades from Version " + oldVersion + " to Version " + newVersion);
        String[] names = getDropTableNamesOnUpgrade();
        for (String name : names) {
            db.execSQL("drop table if exists " + name);
            Log.d(TAG, "Drop table: " + name);
        }

        onCreate(db);
    }
}
