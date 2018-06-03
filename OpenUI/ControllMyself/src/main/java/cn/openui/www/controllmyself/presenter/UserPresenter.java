package cn.openui.www.controllmyself.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import cn.openui.www.controllmyself.MainActivity;
import cn.openui.www.controllmyself.api.OpenApiSdk;
import cn.openui.www.controllmyself.config.Config;
import cn.openui.www.controllmyself.util.NetUtil;
import cn.openui.www.controllmyself.util.TextUtils;
import cn.openui.www.controllmyself.viewInterface.LoginView;
import cn.openui.www.controllmyself.viewInterface.RegistView;

/**
 * Created by My on 2018/2/5.
 */
public class UserPresenter extends BasePresenter{

    private LoginView loginView;
    private RegistView registView;
    private NetUtil netUtil;


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //取消登录加载
            loginView.showProgress(false);
            //显示错误信息
            loginView.showTips(msg.obj.toString());
        }
    };

    public UserPresenter(Context context){
        this.attach(context);
        netUtil = new NetUtil();
    }

    public void setLoginView(LoginView loginView) {
        this.loginView = loginView;
    }

    public void setRegistView(RegistView registView) {
        this.registView = registView;
    }

    public void login(final String username, String password){
        netUtil.login(username, password, new OpenApiSdk.CallBack() {
            @Override
            public void onSuccess(String msg) {//登录成功
                storeUserInfo(username);//存储一下
                //跳转
                loginView.JumpToMain();
            }

            @Override
            public void onFail(String msg) {
                Message message = Message.obtain();
                message.obj = msg;
                handler.sendMessage(message);
            }
        });
    }

    private void storeUserInfo(String username){
        SharedPreferences cf = mcontext.getSharedPreferences(Config.SHARE_FELD,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = cf.edit();
        editor.putString(Config.SHARE_USER,username);
        editor.commit();
    }

    private String getUserInfo(){
        SharedPreferences cf = mcontext.getSharedPreferences(Config.SHARE_FELD,Context.MODE_PRIVATE);
        return cf.getString(Config.SHARE_USER,"");
    }

    public void showProgress(boolean b) {
        loginView.showProgress(b);
    }

    /**
     * 登录成功后 跳转到主界面
     */
    public void JumpToMain() {
        loginView.JumpToMain();

    }

    public void checkLogin() {
        String username = getUserInfo();
        if(!TextUtils.isEmpty(username)){
            loginView.JumpToMain();
        }
    }
}
