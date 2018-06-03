package cn.openui.www.controllmyself.util;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;

/**
 * Created by My on 2018/1/3.
 */
public class StethoUtil extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init(){
        Stetho.initializeWithDefaults(this);;
        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }
}
