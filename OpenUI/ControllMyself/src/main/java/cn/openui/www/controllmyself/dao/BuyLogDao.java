package cn.openui.www.controllmyself.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cn.openui.www.controllmyself.MainActivity;
import cn.openui.www.controllmyself.model.BuyLogModel;
import cn.openui.www.controllmyself.model.LimitLineModel;
import cn.openui.www.controllmyself.model.StatInfoModel;
import cn.openui.www.controllmyself.util.TextUtils;

/**
 * Created by My on 2017/12/24.
 */
public class BuyLogDao {
    private Context context;
    private DBHelper helper;

    public BuyLogDao(Context context){
        this.context = context;
        helper = new DBHelper(context);
    }

    public List<BuyLogModel> getBuyLog(String user, String condition,String... orders){
        List<BuyLogModel> result = new ArrayList<>();
        SQLiteDatabase localSQLiteDatabase = this.helper.getReadableDatabase();

        String sql = "select id,buydate,content,price,win,type,addtime,coder,status from buyLog where user='"+user+"'";
        if(!TextUtils.isEmpty(condition)){
            sql += " and type='"+ URLEncoder.encode(condition)+"'";
        }
        Cursor cursor = localSQLiteDatabase.rawQuery(sql,null);
        while(cursor.moveToNext()){
            BuyLogModel model = new BuyLogModel();

            model.setId(cursor.getInt(cursor.getColumnIndex("id")));
            model.setBuyDate(cursor.getString(cursor.getColumnIndex("buydate")));
            model.setBuyContent(cursor.getString(cursor.getColumnIndex("content")));
            model.setPrice(cursor.getString(cursor.getColumnIndex("price")));
            model.setWin(cursor.getString(cursor.getColumnIndex("win")));
            model.setType(cursor.getString(cursor.getColumnIndex("type")));
            model.setAddtime(cursor.getString(cursor.getColumnIndex("addtime")));
            model.setCoder(cursor.getString(cursor.getColumnIndex("coder")));
            model.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
            result.add(model);
        }
        localSQLiteDatabase.close();
        return result;
    }

    public void store(BuyLogModel model) {
        SQLiteDatabase localSQLiteDatabase = this.helper.getWritableDatabase();
        Object[] arrayOfObject = new Object[9];
        arrayOfObject[0] = model.getBuyDate();
        arrayOfObject[1] = model.getBuyContent();
        arrayOfObject[2] = model.getPrice();
        arrayOfObject[3] = model.getWin();
        arrayOfObject[4] = model.getType();
        arrayOfObject[5] = model.getAddtime();
        arrayOfObject[6] = model.getCoder();
        arrayOfObject[7] = model.getStatus();
        arrayOfObject[8] = model.getUser();
        localSQLiteDatabase.execSQL("insert into buyLog(buydate,content,price,win,type,addtime,coder,status,user) values(?,?,?,?,?,?,?,?,?)", arrayOfObject);
        localSQLiteDatabase.close();

    }

    public List<StatInfoModel> getStatList(String user, int type) {
        String condition = "";
        if(type== MainActivity.BASKETBALL){
            condition = " user='"+user+"' and type ='"+URLEncoder.encode("篮球")+"'";
        }else if(type== MainActivity.FOOTBALL){
            condition = " user='"+user+"'and type ='"+URLEncoder.encode("足球")+"'";
        }
        List<StatInfoModel> result = new ArrayList<>();
        SQLiteDatabase localSQLiteDatabase = this.helper.getReadableDatabase();
        String sql_today = "select sum(price) cost,sum(win) win,'0' type from buyLog where strftime('%Y-%m-%d',buydate)=strftime('%Y-%m-%d',date('now'))"+condition;
        String sql_week = "select sum(price) cost,sum(win) win,'1' type  from buyLog where strftime('%W',buydate) = strftime('%W',date('now'))"+condition;
        String sql_month = "select sum(price) cost,sum(win) win,'2' type from buyLog where strftime('%Y-%m',buydate)= strftime('%Y-%m',date('now'))"+condition;
        String sql_year = "select sum(price) cost,sum(win) win,'3' type from buyLog where strftime('%Y',buydate)= strftime('%Y',date('now'))  "+condition;

        StringBuffer sql = new StringBuffer();
        sql.append(sql_today).append(" union ")
                .append(sql_week).append(" union ")
                .append(sql_month).append(" union ")
                .append(sql_year);

        Cursor cursor = localSQLiteDatabase.rawQuery(sql.toString(),null);
       // Log.i("sql----------------",sql.toString());
        while(cursor.moveToNext()){
            StatInfoModel model = new StatInfoModel();
            String cost = cursor.getString(cursor.getColumnIndex("cost"));
            String win = cursor.getString(cursor.getColumnIndex("win"));
            if(TextUtils.isEmpty(cost)) cost = "0";
            if(TextUtils.isEmpty(win)) win = "0";
            model.setCost(cost);
            model.setWin(win);
            model.setType(cursor.getInt(cursor.getColumnIndex("type")));

            result.add(model);
        }
        localSQLiteDatabase.close();
        return result;
    }

    public List<LimitLineModel> getRul(){
        List<LimitLineModel> result = new ArrayList<>();
        String sql = "";
        SQLiteDatabase local = this.helper.getReadableDatabase();
        Cursor cursor = local.rawQuery(sql,null);
        while(cursor.moveToNext()){
            LimitLineModel model = new LimitLineModel();
            model.setDatetype(cursor.getString(cursor.getColumnIndex("datetype")));
            model.setValue(cursor.getString(cursor.getColumnIndex("value")));
            model.setWinlose(cursor.getString(cursor.getColumnIndex("winlose")));
            result.add(model);
        }
        local.close();
        return result;
    }

    public void storeRul(LimitLineModel model){

        SQLiteDatabase local = this.helper.getWritableDatabase();
        String sql = "insert into rultable(datetype,value,winlose) values(?,?,?);";
        Object[] arrayobject = new Object[3];
        arrayobject[0] = model.getDatetype();
        arrayobject[1] = model.getValue();
        arrayobject[2] = model.getWinlose();
        local.execSQL(sql,arrayobject);
        local.close();

    }

    public void updateRul(LimitLineModel model){
        String sql = "";

    }

    public void removeRul(int id){

    }

    public String getLocalNewest() {
        StringBuffer result = new StringBuffer();
        String sql  = "select addtime,coder from buyLog where status=0";
        SQLiteDatabase local = this.helper.getReadableDatabase();
        Cursor cursor = local.rawQuery(sql,null);

        result.append("{[");
        while(cursor.moveToNext()){
            result.append("\"").append(cursor.getString(cursor.getColumnIndex("coder"))).
                    append("\":\"").append(cursor.getString(cursor.getColumnIndex("addtime"))).append("\"");
            if(!cursor.isLast()){
                result.append(",");
            }
        }
       result.append("]}");
        local.close();
        return result.toString();
    }

    public String getLocalNewestTime(String user) {
        StringBuffer result = new StringBuffer();
        String sql  = "select addtime,user from buyLog where user=? order by addtime desc limit 0,1";
        SQLiteDatabase local = this.helper.getReadableDatabase();
        try{
            Cursor cursor = local.rawQuery(sql,new String[]{user});

            while(cursor.moveToNext()){
                result.append(cursor.getString(cursor.getColumnIndex("user")));
            }
        }catch (SQLException e){
            e.getStackTrace();
        }

        local.close();
        return result.toString();
    }

    public boolean setSynFinish(String coder) {
        boolean result = true;
        String sql = "update buylog set status=1 where coder='"+coder+"'";
        SQLiteDatabase local = this.helper.getWritableDatabase();
        try {
            local.execSQL(sql);
        }catch (SQLException e){
            result = false;
        }finally {
            local.close();
        }
        return result;
    }

    public void storeLogs(String user,List<BuyLogModel> datas) {
        if(datas!=null&&!datas.isEmpty()) {
            String sql = "insert into buylog(buydate,content,price,type,addtime,coder,status,win,user) values(?,?,?,?,?,?,?,?,?);";
            SQLiteDatabase local = this.helper.getWritableDatabase();
            SQLiteStatement stat = local.compileStatement(sql);
            local.beginTransaction();

            for (BuyLogModel model : datas) {
                stat.bindString(1, model.getBuyDate());
                stat.bindString(2, model.getBuyContent());
                stat.bindString(3, model.getPrice());
                stat.bindString(4, model.getType());
                stat.bindString(5, model.getAddtime());
                stat.bindString(6, model.getCoder());
                stat.bindLong(7, 1);
                stat.bindString(8, model.getWin());
                stat.bindString(9, user);
                stat.executeInsert();
            }
            local.setTransactionSuccessful();
            local.endTransaction();
            local.close();
        }
    }

    public BuyLogModel getModelByCoder(String coder) {
        String sql = "select id,buydate,content,price,win,type,addtime,coder,status from buyLog where coder=?";
        SQLiteDatabase local = this.helper.getReadableDatabase();
        String[] params = new String[1];
        params[0] = coder;
        Cursor cursor = local.rawQuery(sql, params);
        if(cursor.moveToNext()){
            BuyLogModel model = new BuyLogModel();
            model.setId(cursor.getInt(cursor.getColumnIndex("id")));
            model.setBuyDate(cursor.getString(cursor.getColumnIndex("buydate")));
            model.setBuyContent(cursor.getString(cursor.getColumnIndex("content")));
            model.setPrice(cursor.getString(cursor.getColumnIndex("price")));
            model.setWin(cursor.getString(cursor.getColumnIndex("win")));
            model.setType(cursor.getString(cursor.getColumnIndex("type")));
            model.setAddtime(cursor.getString(cursor.getColumnIndex("addtime")));
            model.setCoder(cursor.getString(cursor.getColumnIndex("coder")));
            model.setStatus(cursor.getInt(cursor.getColumnIndex("status")));

            local.close();
            return model;
        }
        return null;
    }

    public void updateModel(BuyLogModel newmodel) {
        String sql = "update buyLog set buydate=?,content=?,price=?,win=?,type=?,addtime=?,status=? where coder=?";
        SQLiteDatabase local = this.helper.getWritableDatabase();
        Object[] arrayobject = new Object[8];
        arrayobject[0] = newmodel.getBuyDate();
        arrayobject[1] = newmodel.getBuyContent();
        arrayobject[2] = newmodel.getPrice();
        arrayobject[3] = newmodel.getWin();
        arrayobject[4] = newmodel.getType();
        arrayobject[5] = newmodel.getAddtime();
        arrayobject[6] = newmodel.getStatus();
        arrayobject[7] = newmodel.getCoder();
        local.execSQL(sql,arrayobject);
        local.close();
    }

    public void removeModel(String coder) {
        Log.i("remove coder",coder);
        String sql = "delete from  buyLog  where coder=?";
        SQLiteDatabase local = this.helper.getWritableDatabase();
        Object[] arrayobject = new Object[1];
        arrayobject[0] = coder;
        local.execSQL(sql,arrayobject);
        local.close();
    }
}
