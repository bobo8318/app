package cn.openui.www.controllmyself.presenter;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by My on 2017/11/9.
 */
public class BasePresenter {

    protected Context mcontext;

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


    public String LongToDate(long time){
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date(time);
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


}
