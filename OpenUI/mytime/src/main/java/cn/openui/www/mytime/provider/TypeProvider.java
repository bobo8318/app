package cn.openui.www.mytime.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import cn.openui.www.mytime.dao.DBHelper;

/**
 * Created by My on 2017/12/13.
 */
public class TypeProvider extends ContentProvider {

    private DBHelper dbhelper;
    private static UriMatcher sMatcher;
    private static final String authority="www.openui.cn.provider";

    private static final int TYPE_INSERT_CODE = 0;
    private static final int TYPE_DELETE_CODE = 1;
    private static final int TYPE_UPDATE_CODE = 2;
    private static final int TYPE_QUERY_CODE = 3;
    private static final int TYPE_QUERYALL_CODE = 4;

    private static final int STUDY_INSERT_CODE = 10;
    private static final int STUDY_DELETE_CODE = 11;
    private static final int STUDY_UPDATE_CODE = 12;
    private static final int STUDY_QUERY_CODE = 13;
    private static final int STUDY_QUERYALL_CODE = 14;

    static{
        sMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //添加uri（分机号）
        //content://com.wzw.sqllitedemo.providers.PersonContentProvider/person/insert
        sMatcher.addURI(authority, "type/insert", TYPE_INSERT_CODE);
        sMatcher.addURI(authority, "type/delete", TYPE_DELETE_CODE);
        sMatcher.addURI(authority, "type/update", TYPE_UPDATE_CODE);
        sMatcher.addURI(authority, "type/query", TYPE_QUERY_CODE);
        sMatcher.addURI(authority, "type/queryAll", TYPE_QUERYALL_CODE);

        sMatcher.addURI(authority, "study/insert", STUDY_INSERT_CODE);
        sMatcher.addURI(authority, "study/delete", STUDY_DELETE_CODE);
        sMatcher.addURI(authority, "study/update", STUDY_UPDATE_CODE);
        sMatcher.addURI(authority, "study/query", STUDY_QUERY_CODE);
        sMatcher.addURI(authority, "study/queryAll/#", STUDY_QUERYALL_CODE);

    }
    @Override
    public boolean onCreate() {
        dbhelper = new DBHelper(getContext());
        return (dbhelper == null) ? false : true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
       SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        switch(sMatcher.match(uri)){
            case TYPE_QUERY_CODE://
                qb.setTables("type");
                //qb.setProjectionMap();
                break;
            case TYPE_QUERYALL_CODE:

                break;
            case STUDY_QUERY_CODE:break;
        }
       //qb.setTables(TABLE_NAME);
        Cursor c = qb.query(db, projection, selection, null, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
       return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}