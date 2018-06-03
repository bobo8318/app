package cn.openui.www.positionmsg.presenter;

import android.content.Context;

/**
 * Created by My on 2017/11/9.
 */
public class BasePresenter {

    private Context mcontext;

    public void attach(Context mcontext){
        this.mcontext = mcontext;
    }

    public void onPause(){

    }

    public void onResume(){

    }

    public  void onDestory(){
        mcontext = null;
    }
}
