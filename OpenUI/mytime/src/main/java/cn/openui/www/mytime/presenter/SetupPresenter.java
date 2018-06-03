package cn.openui.www.mytime.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import cn.openui.www.mytime.mainview.BaseView;
import cn.openui.www.mytime.mainview.MyInfoView;

/**
 * Created by My on 2018/1/18.
 */
public class SetupPresenter extends BasePresenter {

    private MyInfoView view;
    public SetupPresenter(Context context, MyInfoView view, BaseView base){
        this.attach(context);
        this.view = view;
        this.setBaseView(base);
    }
    public void saveUserName(String usrname) {
        SharedPreferences spf = this.mcontext.getSharedPreferences("mytime", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("username",usrname);
        editor.commit();
    }

    public String getUserName() {
        SharedPreferences spf = this.mcontext.getSharedPreferences("mytime", Context.MODE_PRIVATE);

        return  spf.getString("username","");
    }
}
