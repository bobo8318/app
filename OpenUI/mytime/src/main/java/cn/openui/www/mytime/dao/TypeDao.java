package cn.openui.www.mytime.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.openui.www.mytime.model.DetailType;

/**
 * 返回type 分类数据
 * Created by My on 2017/12/10.
 */
public class TypeDao {

    private DBHelper dbhelper;

    public TypeDao(Context context){
        dbhelper = new DBHelper(context);
    }

    /**
     * 取所有父类型列表
     * @return
     */
    public List<DetailType> getFatherList(){

        List<DetailType> result = new ArrayList<>();
        SQLiteDatabase localSQLiteDatabase = this.dbhelper.getReadableDatabase();
        Cursor localCursor = localSQLiteDatabase.rawQuery("select id, name ,fatherid  from mytype where fatherid=''",null);

        while (localCursor.moveToNext())
        {
            DetailType temp = new DetailType();
            temp.setId(localCursor.getInt(localCursor.getColumnIndex("id")));
            temp.setTypeName(localCursor.getString(localCursor.getColumnIndex("name")));
            result.add(temp);
        }
        localSQLiteDatabase.close();
        return result;
    }

    public int addNewType(DetailType type){
        int result = 1;
        SQLiteDatabase localSQLiteDatabase = this.dbhelper.getWritableDatabase();
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = type.getTypeName();
        arrayOfObject[1] = type.getFatherType();
        try{
            localSQLiteDatabase.execSQL("insert into mytype(name,fatherid) values(?,?)", arrayOfObject);

        }catch (SQLException e){
            result = 0;
        }finally{
            localSQLiteDatabase.close();
        }

        return result;
    }
    public boolean checkType(String type){
        int result = 0;
        String sql = "select count(*) typecount from mytype where name=?";
        SQLiteDatabase local = this.dbhelper.getReadableDatabase();
        Cursor cursor = local.rawQuery(sql,new String[]{type});
        if (cursor.moveToNext()){
            result = cursor.getInt(cursor.getColumnIndex("typecount"));
        }
        return result>=1;
    }
    public void modifyType(DetailType type){
        SQLiteDatabase localSQLiteDatabase = this.dbhelper.getWritableDatabase();
        Object[] arrayOfObject = new Object[3];

        arrayOfObject[0] = type.getTypeName();
        arrayOfObject[1] = type.getFatherType();
        arrayOfObject[2] = type.getId();

        localSQLiteDatabase.execSQL("update mytype set name=?,fatherid=?  where id=?", arrayOfObject);
        localSQLiteDatabase.close();
    }

    public DetailType getTypeById(int id){
        SQLiteDatabase localSQLiteDatabase = this.dbhelper.getReadableDatabase();

        Cursor localCursor = localSQLiteDatabase.rawQuery("select id, name ,fatherid  from mytype where id=?",new String[]{""+id});
        DetailType temp = null;
        if (localCursor.moveToNext()){
            temp = new DetailType();
            temp.setId(localCursor.getInt(localCursor.getColumnIndex("id")));
            temp.setTypeName(localCursor.getString(localCursor.getColumnIndex("name")));
        }
        localSQLiteDatabase.close();
        return temp;
    }
    public void removeType(int id){
        SQLiteDatabase localSQLiteDatabase = this.dbhelper.getWritableDatabase();

        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = id;
        localSQLiteDatabase.execSQL("delete from mytype where id=?", arrayOfObject);
        localSQLiteDatabase.close();
    }

    public List<DetailType> getChildrenByFather(String bigType) {
        List<DetailType> result = new ArrayList<>();
        SQLiteDatabase localSQLiteDatabase = this.dbhelper.getReadableDatabase();

        Cursor localCursor = localSQLiteDatabase.rawQuery("select id, name ,fatherid  from mytype where fatherid=?",new String[]{bigType});

        while (localCursor.moveToNext())
        {
            DetailType temp = new DetailType();
            temp.setId(localCursor.getInt(localCursor.getColumnIndex("id")));
            temp.setTypeName(localCursor.getString(localCursor.getColumnIndex("name")));
            temp.setFatherType(localCursor.getString(localCursor.getColumnIndex("fatherid")));
            result.add(temp);
        }
        localSQLiteDatabase.close();
        return result;
    }
}
