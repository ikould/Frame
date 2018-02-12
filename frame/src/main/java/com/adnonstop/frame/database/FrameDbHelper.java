package com.adnonstop.frame.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

/**
 * 数据表创建需要在onCreate中
 * <p>
 * Created by ikould on 2017/7/7.
 */

public abstract class FrameDbHelper {

    /**
     * 全局数据库锁对象
     */
    protected static final Object LOCK_OBJECT = new Object();

    protected abstract String getTableName();

    /**
     * 初始化
     */
    public void init() {
        synchronized (LOCK_OBJECT) {
            int[] versions = DbOpenHelper.getInstance().getVersions();
            SQLiteDatabase db = getWritableDatabase();
            String tableName = getTableName();
            if (versions[0] != versions[1] && isTableExist(tableName)) {
                onUpdate(db, versions[1], versions[0]);
            }
            if (!db.isOpen()) { // onUpdate 可能被关闭
                db = getWritableDatabase();
            }
            onCreate(db);
        }
    }

    public SQLiteDatabase getWritableDatabase() {
        return DbOpenHelper.getInstance().getWritableDatabase();
    }

    public SQLiteDatabase getReadableDatabase() {
        return DbOpenHelper.getInstance().getReadableDatabase();
    }

    protected abstract void onCreate(SQLiteDatabase db);

    protected abstract void onUpdate(SQLiteDatabase db, int oldVersion, int newVersion);

    /**
     * 判断某张表是否存在
     *
     * @param tableName 表名
     * @return
     */
    public boolean isTableExist(String tableName) {
        boolean result = false;
        if (TextUtils.isEmpty(tableName)) {
            return false;
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String sql = "select count(*) as c from Sqlite_master  where type ='table' and name ='" + tableName.trim() + "' ";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("BaseDbHelper", "isTableExist: e = " + e);
        }
        return result;
    }
}
