package cn.openui.www.controllmyself.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by My on 2017/12/24.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DB_NAME = "controllmyself_db";//数据库名字
    public static String table = "buyLog";//学习记录
    public static String rulTable = "rultable";//学习记录
    private static final int DB_VERSION = 1;   // 数据库版本

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String  tablesql = "create table if not exists " + table +
                "(id integer primary key autoincrement ," +
                "buydate date not null," +
                "content text not null," +
                "price text," +
                "type text,"+
                "addtime text,"+
                "coder text,"+
                "status integer,"+
                "user text,"+
                "win text);";


        try {
            db.execSQL(tablesql);
        } catch (SQLException e) {
            Log.e(TAG, "onCreate " + tablesql+ "Error" + e.toString());
            return;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + table;
        db.execSQL(sql);
        onCreate(db);
    }
}
