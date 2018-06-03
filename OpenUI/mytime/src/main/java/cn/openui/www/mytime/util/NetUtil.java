package cn.openui.www.mytime.util;

import cn.openui.www.mytime.Config.Config;

/**
 * Created by My on 2018/1/24.
 */
public class NetUtil {
    private  OpenApiSdk api;
    private  RequestEntries entries;
    public NetUtil(){
        api  = new OpenApiSdk();
        entries = new RequestEntries(Config.KEY,Config.TOKEN);
    }

    public void getTypeInfo(final String usernmae,final CallBack callBack){
        entries.clear();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                entries.addParam("username",usernmae);

                String response = api.doGet(entries);
                ResponseModel model = parseResponse(response);
                if(model.isSuccess())
                    callBack.onSuccess(model.getInfo());
                else
                    callBack.onFaile(model.getInfo());
            }
        });
        t.start();
    }

    public ResponseModel parseResponse(String response){
        ResponseModel model = new ResponseModel();

        return model;
    }

    public interface CallBack{
        public void onSuccess(String msg);
        public void onFaile(String msg);
    }

    public class ResponseModel {
        private int status;
        private String msg;
        private String jsonData;
        public boolean isSuccess() {
            if(status==200)
                return true;
            else
                return false;
        }

        public String getInfo() {
            return msg;
        }

        public String getJsonData(){
            return jsonData;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public void setJsonData(String dataInfo) {
            this.jsonData = dataInfo;
        }
    }
}
