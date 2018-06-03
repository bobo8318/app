package cn.openui.www.caodian.service;

import android.content.Context;

import android.graphics.Bitmap;
import android.widget.Toast;

import com.megvii.cloud.http.HttpRequest;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import cn.openui.www.caodian.config.Config;
import cn.openui.www.caodian.net.DectectHelper;
import cn.openui.www.caodian.net.HttpMethod;
import cn.openui.www.caodian.net.NetConnection;

/**
 * Created by My on 2016/10/30.
 */
public class MainService {

    private Context context;

    public MainService(Context context){
        this.context = context;
    }

    public void test(String test) {
        Toast.makeText(context,test,Toast.LENGTH_LONG).show();
    }

    public int getRandom(int bound){
        int result = (int) Math.random()*bound;
        return result;
    }

    public void dectedImg(NetConnection.SuccessCallback success,NetConnection.FailCallback fail, Bitmap bitmap) throws Exception {
        HashMap<String, String> keys = new HashMap<String, String>();
        keys.put("api_key",Config.FACE_KEY);
        keys.put("api_secret",Config.FACE_SECRET);
        keys.put("return_landmark","1");
        keys.put("return_attributes","gender,age,smiling,glass,headpose,facequality,blur");

        HashMap<String,  byte[]> file = new HashMap<String,  byte[]>();
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        boolean compress = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bo);
        byte[] byteArray  = bo.toByteArray();
        file.put("image_file",byteArray);

        NetConnection nc = new NetConnection(Config.FACE_DECTECT_URL,success,fail,keys,file);
        nc.execute();
;
    }
}
