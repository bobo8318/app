package pub.imba.www.util;

import android.graphics.Bitmap;
import android.util.Log;

import java.net.URLEncoder;

/**
 * Created by My on 2017/8/17.
 */
public class Service {

    private int imgCacheSize = 10;
    private NetTools nt;
    public Service(){
        nt = new NetTools();
    }

    public String getJsonData(int page, String key, String type){
        String param = "";
        int parampage = page <=0 ?1:page;
        param = "page="+parampage;
        if(key!=null&&!"".equals(key))
            param += "&key="+ URLEncoder.encode(key.trim());
        String url = "http://10.0.3.2:8087/index.php/vip/get/"+param;
        Log.i("openui Service",url);
        return nt.getJsonData(url);
       // return nt.getJsonData("http://www.imba.pub/index.php/vip/get");
    }

    public Bitmap getPic(String url){
        return null;
    }
}
