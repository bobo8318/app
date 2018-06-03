package cn.openui.www.mytime.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by My on 2017/10/22.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String DB_NAME = "mytime_db";//数据库名字
    public static String study_table = "studylog";//学习记录
    public static String myType_table = "mytype";//我的分类
    public static String config_table = "config";// 表名
    private static final int DB_VERSION = 1;   // 数据库版本

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String  study_sql = "create table if not exists " + study_table +
                "(id integer primary key autoincrement ," +
                "startTime text not null," +
                "endTime text not null," +
                "note text," +
                "type text);";

        String  type_sql = "create table if not exists " + myType_table +
                "(id integer primary key autoincrement ," +
                "name text not null," +
                "fatherid text not null default ''" +
                ");";

        String  config_sql = "create table if not exists " + config_table +
                "(id integer primary key autoincrement ," +
                "startTime text not null," +
                "endTime text not null," +
                "note text," +
                "type integer);";
        try {
            db.execSQL(study_sql);
            db.execSQL(type_sql);
        } catch (SQLException e) {
            Log.e(TAG, "onCreate " + study_table +"-"+myType_table+ "Error" + e.toString());
            return;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql1 = "DROP TABLE IF EXISTS " + study_table;
        String sql2 = "DROP TABLE IF EXISTS " + myType_table;
        db.execSQL(sql1);
        db.execSQL(sql2);
        onCreate(db);
    }
}
