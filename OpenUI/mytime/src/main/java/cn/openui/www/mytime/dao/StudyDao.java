package cn.openui.www.mytime.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cn.openui.www.mytime.model.DetailType;
import cn.openui.www.mytime.model.StudyInfo;
import cn.openui.www.mytime.util.TextUtils;

/**
 * Created by My on 2017/12/14.
 */
public class StudyDao {
    private DBHelper dbhelper;

    public StudyDao(Context context){
        dbhelper = new DBHelper(context);
    }
    /**
     *  存储学习信息
     * @param si
     */
    public void store(StudyInfo si) {
        SQLiteDatabase localSQLiteDatabase = this.dbhelper.getWritableDatabase();
        Object[] arrayOfObject = new Object[4];

        arrayOfObject[0] = si.getType();
        arrayOfObject[1] = si.getStartTime();
        arrayOfObject[2] = si.getEndTime();
        arrayOfObject[3] = si.getNote();
        try{
            localSQLiteDatabase.execSQL("insert into studylog(type,startTime,endTime,note) values(?,?,?,?);", arrayOfObject);
        }catch (SQLException e){
           e.printStackTrace();
        }finally {
            localSQLiteDatabase.close();
        }

        SQLiteDatabase localread = this.dbhelper.getReadableDatabase();
        String sql = "select count(*) allcount  from studylog ";
        Cursor cursor = localread.rawQuery(sql,null);
        if(cursor.moveToNext()){
            int count = cursor.getInt(cursor.getColumnIndex("allcount"));
            Log.i("store result:",""+count+"-"+ si.getType());
        }
        localread.close();

    }
    /**
     * 列出学习信息
     *
     */
    public List<StudyInfo> ListStudyInfo(String type, String startTime, String endTime){
        List<StudyInfo> result = new ArrayList<>();
        SQLiteDatabase localSQLiteDatabase = this.dbhelper.getReadableDatabase();
        String sql = "select a.id, name ,startTime,endTime,note  from studylog a left join mytype b on a.type=b.id  ";
        String condition = "";
        if(!TextUtils.isEmpty(type)){
            condition = "where name='"+type+"'";
        }
        if(!TextUtils.isEmpty(startTime)){
            if(TextUtils.isEmpty(condition))
                condition = " where startTime>='"+startTime+"'";
            else
                condition += " and startTime>='"+startTime+"'";
        }
        if(!TextUtils.isEmpty(endTime)){
            if(TextUtils.isEmpty(condition))
                condition = " where endTime<='"+endTime+"'";
            else
                condition += " and endTime<='"+endTime+"'";
        }
        Log.i("studydao sql",sql+condition);
        Cursor localCursor = localSQLiteDatabase.rawQuery(sql+condition,null);
        while (localCursor.moveToNext()){
            StudyInfo si = new StudyInfo();
            si.setId(localCursor.getInt(localCursor.getColumnIndex("id")));
            si.setType(localCursor.getString(localCursor.getColumnIndex("name")));
            si.setStartTime(localCursor.getString(localCursor.getColumnIndex("startTime")));
            si.setEndTime(localCursor.getString(localCursor.getColumnIndex("endTime")));
            si.setNote(localCursor.getString(localCursor.getColumnIndex("note")));
            result.add(si);
        }
        localSQLiteDatabase.close();
        return result;

    }

    public void removeStudyLogByTypeId(int id) {
        String sql = "delete from studylog where type=?";
        SQLiteDatabase localSQLiteDatabase = this.dbhelper.getWritableDatabase();
        localSQLiteDatabase.execSQL(sql,new Integer[]{id});
    }


    public List<String[]> getTodayStudyCount(String todayMillion) {
        List<String[]> result = new ArrayList<>();
        String sql = "select fatherid fatherType,b.name studyType,startTime,endTime from studylog a left join mytype b on a.type=b.id where startTime>? ";
        SQLiteDatabase local = this.dbhelper.getReadableDatabase();
        Cursor cursor = local.rawQuery(sql,new String[]{todayMillion});
        while(cursor.moveToNext()){
            String[] data = new String[3];
            data[0] = cursor.getString(cursor.getColumnIndex("fatherType"));
            data[1] = cursor.getString(cursor.getColumnIndex("studyType"));

            Log.i("studydao StudyCount",data[0]+"-"+data[1]);
            String startTime = cursor.getString(cursor.getColumnIndex("startTime"));
            String endTime = cursor.getString(cursor.getColumnIndex("endTime"));
            if(!TextUtils.isEmpty(startTime)&&!TextUtils.isEmpty(endTime)){
                data[2] = ""+(Long.valueOf(endTime) - Long.valueOf(startTime));
                result.add(data);
            }

        }
        return result;
    }
}
