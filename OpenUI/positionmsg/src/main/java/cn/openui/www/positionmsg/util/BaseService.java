package cn.openui.www.positionmsg.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import cn.openui.www.positionmsg.R;
import cn.openui.www.positionmsg.RegisterActivity;
import cn.openui.www.positionmsg.model.Conference;

/**
 * Created by My on 2017/10/21.
 */
public class BaseService {

    private String fileDirPath = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath()// 得到外部存储卡的数据库的路径名
            + "/fshy/img/";
    private List<Conference> conferenceList;
    private Context context;
    private DBHelper dbhelper;

    /*
    将拍照 或选取的相片存储到app指定文件夹下
     */
    public String storeFile(InputStream ins,File file) {
      if(file.exists()){
          FileOutputStream fos = null;
          try {
              fos = new FileOutputStream(file);
              byte[] buffer = new byte[8192];
              int count = 0;// 循环写出
              while ((count = ins.read(buffer)) > 0) {
                  fos.write(buffer, 0, count);
              }
              fos.close();// 关闭流
              ins.close();
          } catch (FileNotFoundException e) {
              e.printStackTrace();
          } catch (IOException e) {
              e.printStackTrace();
          }
          return file.getName();
      }
       return null;
    }

    public void storeBitMap(Bitmap bm, String File){

        try {
            File f = new File(fileDirPath, File);
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public static interface callback{//数据加载回调接口
        public void onSuccess(String jsondata);
        public void onFail(String wrongInfo);
    };

    public BaseService(Context context){
        this.context = context;
        dbhelper = new DBHelper(context);
    }

    public List<Conference> getConferenceList(){
        //会议室数据
        conferenceList = new ArrayList<Conference>();
        //默认数据
        String[] pcsname = new String[]{"大安山","大石窝","韩村河","河北","青龙湖","史家营","长沟","长沟检查站","琉璃河"};
        String[] pcsimg = new String[]{"das.jpg","dsw.jpg","hch.jpg","hb.jpg","qlh.jpg","sjy.jpg","cg.jpg","cgz.jpg","llh.jpg"};
        String[] pcsinfo = new String[]{"大安山会议室情况","大石窝会议室情况","韩村河会议室情况","河北情况","青龙湖","史家营","长沟","长沟检查站","琉璃河"};
        String[] pcsphone = new String[]{"大安山会议室电话","大石窝会议室电话","韩村河联电话","河北电话","青龙湖","史家营","长沟","长沟检查站","琉璃河"};

        for(int i=0;i<pcsname.length;i++){
            Conference pcs = new Conference();
            pcs.setName(pcsname[i]);
            pcs.setImg(pcsimg[i]);
            pcs.setInfo(pcsinfo[i]);
            pcs.setPhone(pcsphone[i]);
            conferenceList.add(pcs);
        }

        //数据库数据
        conferenceList.addAll(ListConference());
        return conferenceList;
    }

    public Conference getConferenceByName(String name){
        Iterator<Conference> it = conferenceList.iterator();
        while(it.hasNext()){
            Conference cf = it.next();
            if(cf.getName().equals(name)){
                return cf;
            }
        }
        return null;
    }

    public String[] getConferenceNameList(){
        String[] result = new String[conferenceList.size()];
        // Iterator<Map.Entry<String,Conference>> it = conferenceList.entrySet().iterator();
        for(int i=0;i<conferenceList.size();i++){
            result[i] = conferenceList.get(i).getName();
        }
        return result;
    }

    public Bitmap getImg(String bitmapname) {
        InputStream is = null;
        if(bitmapname==null||"".equals(bitmapname)){
            is = context.getResources().openRawResource(R.raw.timg);
        }else{

            try {
                is = context.getAssets().open(bitmapname);
            } catch (IOException e) {//从文件夹中区
                e.printStackTrace();
                try {
                    is = new FileInputStream(new File(fileDirPath+bitmapname));
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            }

        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }


    public void add(Conference cf) {
        SQLiteDatabase localSQLiteDatabase = this.dbhelper.getWritableDatabase();
        Object[] arrayOfObject = new Object[5];
        arrayOfObject[0] = cf.getName();
        arrayOfObject[1] = cf.getInfo();
        arrayOfObject[2] = cf.getPhone();
        arrayOfObject[3] = cf.getImg();
        arrayOfObject[4] = cf.getType();
        localSQLiteDatabase.execSQL("insert into conference(name,info,phone,img,type) values(?,?,?,?,?)", arrayOfObject);
        localSQLiteDatabase.close();
    }
    public void delete(int id) {
        SQLiteDatabase localSQLiteDatabase = this.dbhelper.getWritableDatabase();
        //设置了级联删除和级联更新
        //在执行有级联关系的语句的时候必须先设置“PRAGMA foreign_keys=ON”
        //否则级联关系默认失效
       // localSQLiteDatabase.execSQL("PRAGMA foreign_keys=ON");
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = id;
        localSQLiteDatabase.execSQL("delete from conference where id=?", arrayOfObject);
        localSQLiteDatabase.close();

        //删除缓存照片
    }

    public void update(Conference cf)
    {
        SQLiteDatabase localSQLiteDatabase = this.dbhelper.getWritableDatabase();
        Object[] arrayOfObject = new Object[4];

        arrayOfObject[0] = cf.getName();
        arrayOfObject[1] = cf.getInfo();
        arrayOfObject[2] = cf.getPhone();
        arrayOfObject[3] = cf.getImg();
        arrayOfObject[4] = cf.getType();
        arrayOfObject[5] = cf.getId();

        localSQLiteDatabase.execSQL("update conference set name=?,info=?,phone=?,img=?,type=?  where id=?", arrayOfObject);
        localSQLiteDatabase.close();
        //删除老的缓存照片
    }

    public List<Conference> ListConference()
    {
        List<Conference> localArrayList=new ArrayList<Conference>();

        SQLiteDatabase localSQLiteDatabase = this.dbhelper.getReadableDatabase();
        Cursor localCursor = localSQLiteDatabase.rawQuery("select id, name ,info,phone, img,type  from conference",null);

        while (localCursor.moveToNext())
        {
            Conference temp = new Conference();
            temp.setId(localCursor.getInt(localCursor.getColumnIndex("id")));
            temp.setName(localCursor.getString(localCursor.getColumnIndex("name")));
            temp.setInfo(localCursor.getString(localCursor.getColumnIndex("info")));
            temp.setPhone(localCursor.getString(localCursor.getColumnIndex("phone")));
            temp.setImg(localCursor.getString(localCursor.getColumnIndex("img")));
            temp.setType(localCursor.getInt(localCursor.getColumnIndex("type")));
            localArrayList.add(temp);
        }
        localSQLiteDatabase.close();
        return localArrayList;
    }

    public String getImgUrl(){
        return this.fileDirPath;
    }

    public String getRandomName(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        return sdf.format(new Date());
    }

}
