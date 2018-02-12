package com.adnonstop.frame.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库帮助类
 * <p>
 * Created by ikould on 2017/7/7.
 */

public class DbOpenHelper extends SQLiteOpenHelper {

    // 数据库名字
    private static String DBName = "Frame";
    // 数据库版本
    private static int DBVersion;
    // 当前数据库新旧版本号数据库旧版本号 0：新 1：旧
    private int[] versions = new int[2];

    // 单例
    private static DbOpenHelper instance;

    public static DbOpenHelper getInstance() {
        if (instance == null) {
            throw new IllegalArgumentException("The init() method is not initialized.");
        }
        return instance;
    }

    /**
     * 初始化数据库
     *
     * @param context   上下文
     * @param dbName    数据库名称
     * @param dbVersion 数据库版本
     */
    public static void init(Context context, String dbName, int dbVersion) {
        DBName = dbName;
        DBVersion = dbVersion;
        if (instance == null)
            synchronized (DbOpenHelper.class) {
                if (instance == null)
                    instance = new DbOpenHelper(context);
            }
    }

    private DbOpenHelper(Context context) {
        //第三个参数CursorFactory指定在执行查询时获得一个游标实例的工厂类,设置为null,代表使用系统默认的工厂类
        super(context, DBName, null, DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        versions[0] = newVersion;
        versions[1] = oldVersion;
    }

    /**
     * 获取版本信息
     */
    public int[] getVersions() {
        return versions;
    }
}
