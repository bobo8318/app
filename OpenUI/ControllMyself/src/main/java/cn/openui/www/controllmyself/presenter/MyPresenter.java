package cn.openui.www.controllmyself.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.openui.www.controllmyself.MainActivity;
import cn.openui.www.controllmyself.R;
import cn.openui.www.controllmyself.api.OpenApiSdk;
import cn.openui.www.controllmyself.config.Config;
import cn.openui.www.controllmyself.dao.BuyLogDao;
import cn.openui.www.controllmyself.model.BuyLogModel;
import cn.openui.www.controllmyself.model.LimitLineModel;
import cn.openui.www.controllmyself.model.StatInfoModel;
import cn.openui.www.controllmyself.util.LimitChecker;
import cn.openui.www.controllmyself.util.NetUtil;
import cn.openui.www.controllmyself.util.TextUtils;
import cn.openui.www.controllmyself.viewInterface.BuyLogView;
import cn.openui.www.controllmyself.viewInterface.LimitView;
import cn.openui.www.controllmyself.viewInterface.StatView;
import cn.openui.www.controllmyself.viewInterface.UserInfoView;

/**
 * Created by My on 2017/12/24.
 */
public class MyPresenter extends BasePresenter {

    private BuyLogView view;
    private StatView statview;
    private LimitView limitview;
    private UserInfoView userview;

    private BuyLogDao dao;
    private LimitChecker checker;
    private SharedPreferences preferences = null;
    private final String TAG = "cm_";
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
           // super.handleMessage(msg);
            String jsondata = msg.obj.toString();
            switch(msg.what){
                case Config.FRESH_LOG://刷新log数据
                    if( msg.arg1 == Config.SUCCESS){//成功
                        List<BuyLogModel> datas = dao.getBuyLog(getUser(),"");
                        List<BuyLogModel> remoteData=null;
                        if(!TextUtils.isJsonNull(jsondata)){//有返回数据 如果返回数据是
                            JSONArray arraydata = JSON.parseArray(jsondata);
                            for(int i=0;i<arraydata.size();i++){
                                //BuyLogModel model  = arraydata.getObject(i,BuyLogModel.class);
                                BuyLogModel model = new BuyLogModel();
                                JSONObject object = JSON.parseObject(arraydata.get(i).toString());
                                model.setCoder(object.getString("coder"));
                                model.setBuyDate(object.getString("buydate"));
                                model.setBuyContent(object.getString("content"));
                                model.setType(object.getString("buytype"));
                                model.setPrice(object.getString("price"));
                                model.setWin(object.getString("win"));
                                model.setAddtime(object.getString("updateTime"));
                                if(remoteData==null) remoteData = new ArrayList<BuyLogModel>();
                                remoteData.add(model);
                            }
                        }
                        //同步到本地
                        MyPresenter.this.storeLogs(getUser(),remoteData);
                        MyPresenter.this.localLogShow( msg.arg2);
                    }else{//失败提示
                        view.showTips(jsondata);
                    }
                    view.unableLogFresh();

                    break;
                case Config.FRESH_STAT://刷新stat数据
                    statview.unableStatFresh();
                    break;
                default:break;
            }
        }
    };
    private NetUtil netUtil;

    public MyPresenter(Context context, BuyLogView view,StatView statview,LimitView limitview){
        attach(context);
        this.view = view;
        this.statview = statview;
        this.limitview = limitview;
        this.dao = new BuyLogDao(context);
        preferences = context.getSharedPreferences(Config.SHARE_FELD, Context.MODE_PRIVATE);

        checker = new LimitChecker();//初始化checker
        checker.addRuls(getRul());
        netUtil= new NetUtil();

    }

    /**
     * 初始运行动作放这里
     */
    public void firstRun(){
        //版本检查
        getNewsetVersion();
    }

    private void getNewsetVersion() {
        netUtil.getNewVersion(new OpenApiSdk.CallBack(){

            @Override
            public void onSuccess(String msg) {

            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    private void storeLogs(String user,List<BuyLogModel> datas) {
        dao.storeLogs(user,datas);
    }


    /**
     * 发送网络请求
     * syn 是否进行同步
     */
    public void showBuyLog(boolean syn,final int filtType) {
        String user = getUser();
        if(syn&&!TextUtils.isEmpty(user)){
            String localnewestTime = dao.getLocalNewestTime(user);
            Log.i("presenter getnew",user+"-"+localnewestTime);
            netUtil.getNewest(user, localnewestTime,new OpenApiSdk.CallBack() {
                @Override
                public void onSuccess(String msg) {
                    Message message = Message.obtain();
                    message.what = Config.FRESH_LOG;
                    message.arg1 = Config.SUCCESS;
                    message.arg2 = filtType;
                    message.obj = msg;
                    handler.sendMessage(message);
                }

                @Override
                public void onFail(String msg) {
                    Message message = Message.obtain();
                    message.what = Config.FRESH_LOG;
                    message.arg1 = Config.FAIL;
                    message.arg2 = filtType;
                    message.obj = msg;
                    handler.sendMessage(message);
                }
            });
        }else{
            this.localLogShow(filtType);
            this.view.unableLogFresh();
        }
    }

    public void localLogShow(int filtType){
        //本地取全部数据
        String condition = "";
        if(filtType == MainActivity.FOOTBALL)
            condition = "足球";
        else if(filtType == MainActivity.BASKETBALL)
            condition = "篮球";
        List<BuyLogModel> localdata =  dao.getBuyLog(getUser(),condition);
        //MyPresenter.this.getLocalLogDatas(condition);
        view.showBuyLog(localdata);
    }


    public void storeLog(BuyLogModel model) {
        if(model!=null){
            model.setUser(getUser());
            dao.store(model);
            //同步服务器数据
            this.storeLogToServer(model);
        }
    }

    public void showStat(int type) {
        List<StatInfoModel> result = dao.getStatList(getUser(),type);
        //Log.i("stat",""+JSON.toJSONString(result));
        statview.showStat(result);
    }

    public int checkLimit(){


        int result = 0;
        List<StatInfoModel> statlist = dao.getStatList(getUser(),0);
        for(StatInfoModel model:statlist){
            double cost = Double.valueOf(model.getCost());
            double win = Double.valueOf(model.getWin());

            switch (model.getType()){
                case Config.STAT_TYPE_DAY:
                    result = checker.check((win-cost),Config.LIMIT_DAY);
                    break;
                case Config.STAT_TYPE_WEEK:
                    result = checker.check((win-cost),Config.LIMIT_WEEK);
                    break;
                case Config.STAT_TYPE_MONTH:
                    result = checker.check((win-cost),Config.LIMIT_MONTH);
                    break;
                default:break;
            }
            if(result>0) break;
        }
        return result;
    }
    public List<LimitLineModel> getRul(){
        if(preferences!=null){
            List<LimitLineModel> result = new ArrayList<>();

            String dayRul = preferences.getString(Config.LIMIT_DAY, "");
            if(!TextUtils.isEmpty(dayRul)){
                LimitLineModel model = JSON.parseObject(dayRul,LimitLineModel.class);
                result.add(model);
            }
            String weekRul = preferences.getString(Config.LIMIT_WEEK, "");
            if(!TextUtils.isEmpty(weekRul)){
                LimitLineModel model = JSON.parseObject(weekRul,LimitLineModel.class);
                result.add(model);
            }
            String monthRul = preferences.getString(Config.LIMIT_MONTH, "");
            if(!TextUtils.isEmpty(monthRul)){
                LimitLineModel model = JSON.parseObject(monthRul,LimitLineModel.class);
                result.add(model);
            }

            return result;
        }
        return null;
    }
    public void putRul(LimitLineModel model){
        SharedPreferences.Editor editor = preferences.edit();
        String name = model.getDatetype();
        String json = JSON.toJSONString(model);
        editor.putString(name,json);
        //editor.putString("onlyTest", "onlyTest");
        editor.commit();

        //更新checker
        checker.addRul(model.getDatetype(),model);

        Log.i("preferences:"+name,preferences.getString(name,"none")+"-"+model.getDatetype()+"-"+json);

    }

    public void addRule(String key, LimitLineModel model){
        checker.addRul(key, model);
    }

    public void showLimit() {
        this.limitview.showLimit(this.getRul());
    }

    public void showTips(String text) {
        view.showTips(text);
    }
    /********************************************************************/
    public String synLogData(String user){
        //api.getNewest();
        return "";
    }

    private void storeLogToServer(final BuyLogModel model){
        String user = getUser();
        if(!TextUtils.isEmpty(user)){
            model.setUser(user);
            netUtil.storeNewModel(model,new OpenApiSdk.CallBack(){

                @Override
                public void onSuccess(String msg) {
                    Log.i("presenter syn success",msg);
                    if(!TextUtils.isEmpty(msg))
                        dao.setSynFinish(model.getCoder());
                }

                @Override
                public void onFail(String msg) {
                    Log.i("presenter syn fail",msg);
                }
            });
        }

    }

    public  String getUser(){
        String name = preferences.getString("user", "");

        return name;
    }

    public  void setUser(String user){
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putString(Config.SHARE_USER,user);
        editor.commit();
    }

    public void showDetailDialog(String coder,int mode) {
        if(mode==Config.ADD_MODE){//添加模式
            view.showDialog(null);
        }else if(mode==Config.UPDATE_MODE){
            BuyLogModel model = dao.getModelByCoder(coder);
            view.showDialog(model);
        }
    }

    public void updateLog(final BuyLogModel newmodel) {
        newmodel.setUser(getUser());
        //本地更新
        dao.updateModel(newmodel);
        //更新到服务器
        netUtil.updateModel(newmodel, new OpenApiSdk.CallBack() {
            @Override
            public void onSuccess(String msg) {
                if(!TextUtils.isEmpty(msg)&&msg.equals("1")){//成功
                    dao.setSynFinish(newmodel.getCoder());
                }
            }

            @Override
            public void onFail(String msg) {
                Log.i("update model fail", msg);
            }
        });
    }

    public void removeLog(String coder) {

        //本地更新
        dao.removeModel(coder);
        //更新到服务器
        netUtil.removeModel(getUser(),coder, new OpenApiSdk.CallBack() {
            @Override
            public void onSuccess(String msg) {
                ;
            }

            @Override
            public void onFail(String msg) {
                //存储一下
            }
        });
    }

    public BuyLogModel getModelByCoder(String coder) {
        return dao.getModelByCoder(coder);
    }

    public void showUserInputDialog() {
        view.showUserInputDialog(getUser());
    }

    public Bitmap getUserHead() {
        Bitmap header = BitmapFactory.decodeResource(mcontext.getResources(),R.drawable.weixinpay);

        return header;
    }

    public void exit() {
        setUser("");
        view.jumpToLogin();
    }

    public void showUserInfo(TextView username, ImageView header) {

        username.setText(getUser());

    }
}
