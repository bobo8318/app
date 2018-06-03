package cn.openui.www.controllmyself.util;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.openui.www.controllmyself.api.LoginModel;
import cn.openui.www.controllmyself.api.RequestEntries;
import cn.openui.www.controllmyself.api.OpenApiSdk;
import cn.openui.www.controllmyself.config.Config;
import cn.openui.www.controllmyself.model.BuyLogModel;

/**
 * 数据同步
 * Created by My on 2018/1/3.
 */
public class NetUtil {
   private OpenApiSdk api;
   private RequestEntries entries = new RequestEntries(Config.KEY,Config.TOKEN);

    private Map<String,Thread> threadpool = new HashMap<>();

    public NetUtil(){
        api  = new OpenApiSdk();
    }

    public void getNewest(String user,String localrecent, final  OpenApiSdk.CallBack callBack){
        String TAG = "neweset";
        if(threadpool.containsKey(TAG)){
            Thread t = threadpool.get(TAG);
            if(t.isAlive()) return;
        }

        entries.clear();

        entries.addParam("user", user);
        entries.addParam("timestamp",  ""+System.currentTimeMillis());
        entries.addParam("info", "test");
        entries.addParam("updatetime", localrecent);
        entries.setMethod("getBuyLogNewest");

        Log.i("getNewest",entries.getUrl());


        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String response = "";
                response = api.doGet(entries);
                if(response.equals("timeout")){
                    if(callBack!=null)
                        callBack.onFail("timeout");
                }else{


                    Log.i("get json response",response);
                    if(callBack!=null&&!TextUtils.isEmpty(response)){
                        JSONObject jsondata = JSON.parseObject(response);
                        if(jsondata.get("status").toString().equals("200"))
                            callBack.onSuccess(jsondata.get("data").toString());
                        else
                            callBack.onFail(jsondata.get("status").toString()+":"+jsondata.get("msg").toString());
                    }
                }



            }
        });
        threadpool.put(TAG,t);
        t.start();
    }


    public void storeNewModel(final BuyLogModel model,final OpenApiSdk.CallBack callBack) {


        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                entries.clear();

                entries.addParam("user", model.getUser());
                entries.addParam("timestamp",  model.getAddtime());
                entries.addParam("info", "add");
                entries.setMethod("storeBuyLogData");
                entries.setPostData(JSON.toJSONString(model));
                Log.i("store json data",JSON.toJSONString(model));

                String response = api.doPost(entries);


                Log.i("add json response",response);
                if(callBack!=null&&!TextUtils.isEmpty(response)){
                    JSONObject jsondata = JSON.parseObject(response);
                    if(jsondata.get("status").toString().equals("200")){
                        if(jsondata.get("data").toString().equals("1"))
                            callBack.onSuccess(jsondata.get("data").toString());
                        else
                            callBack.onFail("syn fail");
                    }
                    else {
                        callBack.onFail(jsondata.get("status").toString() + ":" + jsondata.get("msg").toString());
                    }
                }


            }
        });
        t.start();


    }

    public void updateModel(final BuyLogModel newmodel,final OpenApiSdk.CallBack callback) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                entries.clear();

                entries.addParam("user", newmodel.getUser());
                entries.addParam("timestamp",  newmodel.getAddtime());
                entries.addParam("info", "update");
                entries.setMethod("updateBuyLogData");
                entries.setPostData(JSON.toJSONString(newmodel));
                Log.i("update json data",JSON.toJSONString(newmodel));

                String response = api.doPost(entries);


                Log.i("update json response",response);
                if(callback!=null&&!TextUtils.isEmpty(response)){
                    JSONObject jsondata = JSON.parseObject(response);
                    if(jsondata.get("status").toString().equals("200")) {
                        if(jsondata.get("data").toString().equals("1"))
                        callback.onSuccess(jsondata.get("data").toString());
                    }else
                        callback.onFail(jsondata.get("status").toString()+":"+jsondata.get("msg").toString());
                }


            }
        });
        t.start();

    }

    public void removeModel(final String user,final String coder,final OpenApiSdk.CallBack callback) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                entries.clear();

                entries.addParam("user", user);
                entries.addParam("timestamp",  ""+System.currentTimeMillis());
                entries.addParam("info", "remove");
                entries.addParam("coder",coder);
                entries.setMethod("removeBuyLogData");
                Log.i("remove json data",coder);

                String response = api.doGet(entries);


                Log.i("remove json response ",response);
                if(callback!=null&&!TextUtils.isEmpty(response)){
                    JSONObject jsondata = JSON.parseObject(response);
                    if(jsondata.get("status").toString().equals("200"))
                        callback.onSuccess(jsondata.get("data").toString());
                    else
                        callback.onFail(jsondata.get("status").toString()+":"+jsondata.get("msg").toString());
                }


            }
        });
        t.start();
    }

    public void getNewVersion(OpenApiSdk.CallBack callBack) {
        //未完成
    }

    public void login(String username, String password,final OpenApiSdk.CallBack callBack){
        entries.clear();

        entries.addParam("timestamp",""+System.currentTimeMillis());
        entries.addParam("info", "login");
        entries.addParam("user", username);

        entries.addPostData("username",username);
        entries.addPostData("password",password);

        entries.setMethod("doLogin");
        Log.i("login",entries.getUrl());
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String response =  api.doPost(entries);
                LoginModel model = api.parseResponse(response,LoginModel.class);
                if(model!=null&&model.getStatus()==200){
                    callBack.onSuccess(model.getMsg());
                }else{

                    callBack.onFail(model.getMsg());
                }

            }
        });
        t.start();
    }


}
