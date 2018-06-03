package cn.openui.www.caodian.net;

import com.alibaba.fastjson.JSONObject;
import com.megvii.cloud.http.HttpRequest;


import java.util.HashMap;

import cn.openui.www.caodian.config.Config;

/**
 * Created by My on 2016/12/22.
 */
public class DectectHelper extends HttpRequest {


    public String  DetectFace(HashMap<String,String> map, HashMap<String, byte[]> fileMap ) throws Exception {
        byte[] jsonbyte = HttpRequest.post(Config.FACE_DECTECT_URL,map,fileMap);
        return new String(jsonbyte);
       // JSONObject json = JSONObject.parseObject(new String(jsonbyte));


    }

    public static interface callback{
        public void success(JSONObject json);
        public  void fail(String errorInfo);
    };

}
