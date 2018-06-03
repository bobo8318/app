package cn.openui.www.positionmsg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.openui.www.positionmsg.util.BaseService;

public class MainActivity extends AppCompatActivity {

    private BaseService service;

    private String fileDirPath = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath()// 得到外部存储卡的数据库的路径名
            + "/fshy/img/";// 我要存储的目录
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        service = new BaseService(this);
        if(createDir()) {// 创建应用目录
            //新创建目录导入 raw文件到新目录

            int[] rawList = new int[]{
                    R.raw.cg,
                    R.raw.cgz
            };
            String[] namelist = new String[]{
                "cg","cgz"
            };
            copyFile(rawList,namelist);

        }

        Intent intent = new Intent(this,ConferenceActivity.class);
        startActivity(intent);
        this.finish();

    }

    private boolean createDir() {

        try {
            File dir = new File(fileDirPath);// 目录路径
            if (!dir.exists()) {// 如果不存在，则创建路径名
                System.out.println("create dir "+fileDirPath);
                return dir.mkdir();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    private void copyFile(int[] rawlist,String[] namelist){
        for(int i=0;i<rawlist.length;i++){
            File file = new File(fileDirPath+namelist[i]+".jpg");

            InputStream ins = getResources().openRawResource(R.raw.cg);// 通过raw得到数据资源
            service.storeFile(ins,file);

        }


    }
}
