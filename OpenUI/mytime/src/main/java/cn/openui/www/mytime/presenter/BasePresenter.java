package cn.openui.www.mytime.presenter;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.openui.www.mytime.mainview.BaseView;
import cn.openui.www.mytime.util.TextUtils;

/**
 * Created by My on 2017/11/9.
 */
public class BasePresenter {

    protected Context mcontext;
    protected BaseView baseView;

    public void attach(Context mcontext){
        this.mcontext = mcontext;
    }

    public void setBaseView(BaseView view){
        this.baseView = view;
    }

    public void onPause(){

    }

    public void onResume(){

    }

    public  void onDestory(){
        mcontext = null;
    }


    public String LongToDate(long time){
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        java.util.Date date = new Date(time);
        String str = sdf.format(date);
        return str;
    }

    public int[] getTimeDifference(long... begin){
        long timelong = 0;

        if(begin.length==1)
            timelong = begin[0];
        else if(begin.length==2)
            timelong =  begin[1] - begin[0];

        int[] result = new int[4];

        Long day = timelong/86400000;
        Long hour = (timelong%86400000)/3600000;
        Long min = (timelong%86400000%3600000)/60000;
        Long sec = (timelong%86400000%3600000%60000)/1000;

        result[0] = day.intValue();
        result[1] = hour.intValue();
        result[2] = min.intValue();
        result[3] = sec.intValue();

        return result;
    }

    public String getTodayMillion(){
        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        String today = sdf.format(new Date());*/
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Long start = calendar.getTimeInMillis();
        //Log.i("time in million",""+start);
        return ""+start;
    }

    public void showTips(String text) {
        baseView.showTips(text);
    }

    public int getIdByViewTag(String viewtag){
        int id = -1;
        String[] splite = viewtag.split(":");
        if(splite!=null&&splite.length==2){
            id = Integer.valueOf(splite[1]);
        }
        return id;
    }

    public String getNameByViewTag(String viewtag){
        String name = "";
        String[] splite = viewtag.split(":");
        if(splite!=null&&splite.length==2){
            name = splite[0];
        }
        return name;
    }

    public String getTimeDiffStr(Long cotinues) {
        int[] times = this.getTimeDifference(cotinues);
        StringBuffer sb = new StringBuffer();
        sb.append(times[1]).append("小时").append(times[2]).append("分钟");
        return sb.toString();
    }
}
