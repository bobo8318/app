package cn.openui.www.mytime.presenter;

import android.content.Context;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.openui.www.mytime.dao.StudyDao;
import cn.openui.www.mytime.mainview.StatView;
import cn.openui.www.mytime.model.StudyInfo;
import cn.openui.www.mytime.util.TextUtils;

/**
 * Created by My on 2017/12/19.
 */
public class StatPresenter extends BasePresenter {

    private StatView view;
    private StudyDao dao;

    public StatPresenter(Context context, StatView view){
        this.attach(context);
        this.view = view;
        dao = new StudyDao(context);
    }

    public void showTodayStat() {
        List<StudyInfo> statlist = dao.ListStudyInfo("","","");
        Map<String,StudyInfo>  map = new HashMap<>();
        for(StudyInfo info:statlist){
            if(!TextUtils.isEmpty(info.getStartTime())&&!TextUtils.isEmpty(info.getEndTime())){
                if(map.containsKey(info.getType())){
                    StudyInfo stored = map.get(info.getType());
                    stored.setStartTime(""+(Long.valueOf(stored.getStartTime())+Long.valueOf(info.getStartTime())));
                    stored.setEndTime(""+(Long.valueOf(stored.getEndTime())+Long.valueOf(info.getEndTime())));
                }else{
                    map.put(info.getType(),info);
                }

            }

        }
        StudyInfo stored = map.get("实例开发");
        Log.i("showTodayStat:",""+(Long.valueOf(stored.getStartTime())-Long.valueOf(stored.getEndTime())));
        view.showTodayStat(map);
    }


}
