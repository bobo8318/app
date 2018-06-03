package cn.openui.www.imbatao.presenter;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.openui.www.imbatao.interfaceview.ShowPic;
import cn.openui.www.imbatao.po.ItemPo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by My on 2018/3/14.
 */
public class MyPresenter extends BasePresenter {



    private ShowPic showview;
    private Handler mainHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:{
                    if(msg.obj!=null){
                        String jsondata = msg.obj.toString();
                        List<ItemPo> datas = parseJsonData(jsondata);
                        showview.showPic(datas);
                        showview.stopFresh();
                    }
                    break;
                }
            }
        }
    };

    public MyPresenter(Context context){
        this.mcontext =context;
    }

    public void setView(ShowPic showview){
        this.showview = showview;
    }


    private String makeUrl(int page, String key, String type){
        String param = "";
        int parampage = page <=0 ?1:page;
        param = "page="+parampage;
        if(key!=null&&!"".equals(key))
            param += "&key="+ URLEncoder.encode(key.trim());
        String url = "http://172.21.111.4:8087/index.php/vip/get/"+param;
        Log.i("openui Service",url);
        return url;
    }
    private String getJsonDataFromServer(String url) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();
        // return nt.getJsonData("http://www.imba.pub/index.php/vip/get");
        try {
            Log.i("get data from",""+url);
            Response response = client.newCall(request).execute();
            Log.i("okhttp",""+response.code());
            if(!response.isSuccessful()){
                throw new IOException("服务器端故障:" + response);
            }else if(response!=null&&response.isSuccessful()){
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<ItemPo> parseJsonData(String jsondata) {
        List<ItemPo> mDatas = new ArrayList<ItemPo>();
        try {
            JSONObject data = JSON.parseObject(jsondata);//{}
            JSONArray ja = JSON.parseArray(data.getString("data").toString());//"[]"
            Iterator<Object> it = ja.iterator();
            while(it.hasNext()){
                JSONObject ob = (JSONObject) it.next();
                ItemPo item = new ItemPo();

                item.setName(ob.getString("name"));
                item.setImgUrl(ob.getString("pic"));
                item.setDiscountEnd("优惠截止日期:"+ob.getString("discountEnd"));
                item.setDiscountInfo("优惠券:"+ob.getString("discountInfo"));
                item.setDiscountPrice("优惠价格:"+ob.getString("discountPrice")+"元");
                item.setSales("销量:"+ob.getString("sales"));
                item.setTbkurl(ob.getString("tbkurl"));
                item.setType("分类："+ob.getString("type"));
                item.setPrice("价格:"+ob.getString("price")+"元");
                //Log.i("openui",ob.getString("name"));
                mDatas.add(item);
            }
        }catch (Exception e){
            Log.i("openui","parseJsonData error!!!!!!!!!!!!!!!!!!!!");
            return null;
        }
        return mDatas;
    }

    public void loadData(int page, String key, String type) {
        final String url = this.makeUrl(page, key ,type);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String jsondata = getJsonDataFromServer(url);
                Log.i("response data","data:"+jsondata);
                Message msg =Message.obtain();
                msg.what = 1;
                msg.obj = jsondata;
                mainHandler.sendMessage(msg);
            }
        } );
        t.start();
    }
}
