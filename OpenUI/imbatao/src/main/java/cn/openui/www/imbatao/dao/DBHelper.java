package cn.openui.www.imbatao.dao;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by My on 2018/3/23.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DB_NAME = "mytime_db";//数据库名字
    public static String item_table = "studylog";//首页数据缓存
    public static String type_table = "studylog";//分类缓存
    private static final int DB_VERSION = 1;   // 数据库版本

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String  sql = "create table if not exists " + item_table +
                "(id integer primary key autoincrement ," +
                "startTime text not null," +
                "endTime text not null," +
                "note text," +
                "type text);";
        try{
            db.execSQL(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + item_table;
        db.execSQL(sql);
        onCreate(db);
    }
}
