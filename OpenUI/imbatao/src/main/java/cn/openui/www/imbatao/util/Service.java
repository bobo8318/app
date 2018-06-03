package cn.openui.www.imbatao.util;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by My on 2017/8/17.
 */
public class Service {

    private int imgCacheSize = 10;


    public String getJsonData(int page, String key, String type){

        OkHttpClient client = new OkHttpClient();

        String param = "";
        int parampage = page <=0 ?1:page;
        param = "page="+parampage;
        if(key!=null&&!"".equals(key))
            param += "&key="+ URLEncoder.encode(key.trim());
        String url = "http://10.0.3.2:8087/index.php/vip/get/"+param;
        Log.i("openui Service",url);

        Request request = new Request.Builder()
                .url(url)
                .build();
       // return nt.getJsonData("http://www.imba.pub/index.php/vip/get");
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response.isSuccessful()){
            return response.body().toString();
        }else
            return null;
    }

    public Bitmap getPic(String url){
        return null;
    }
}
