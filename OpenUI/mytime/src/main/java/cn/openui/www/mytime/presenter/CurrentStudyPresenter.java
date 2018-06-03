package cn.openui.www.mytime.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import cn.openui.www.mytime.dao.StudyDao;
import cn.openui.www.mytime.mainview.BaseView;
import cn.openui.www.mytime.mainview.ShowCurrentView;
import cn.openui.www.mytime.model.StudyInfo;

/**
 * Created by My on 2017/12/7.
 */
public class CurrentStudyPresenter extends BasePresenter{

    private ShowCurrentView scv;
    private  int status;

    private final int RUNNING = 1;
    private final int PAUSE = 2;
    private final int FINISH = 3;
    private StudyDao dao;

    public CurrentStudyPresenter(Context context){
        super.attach(context);
        dao = new StudyDao(context);
    }


    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
           if(msg.what==1){
               if(status==RUNNING)
                setTime(msg.obj.toString());
           }
        }
    };

    public void setScv(ShowCurrentView scv) {
        this.scv = scv;
    }

    /**
     * 当前学习 内容标题
     * @param type
     */
    public void setTitle(String type){
        scv.setTitle(type);
    }
    /**
     * 更新学习时间
     */
    public void setTime(String time){
        scv.updateTime(time);
}
    /**
     * 开始学习
     */
    public void start(){
        //设置开始状态
        status = RUNNING;
        //记录当前开始时间
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(status==RUNNING){
                    try {
                        Thread.sleep(1000);
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = getCurrentTime();
                        myHandler.sendMessage(msg);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
               }
            }
        }).start();
    }

    /**
     * 停止
     */
    public void finish(){

        status = FINISH;
    }

    public void storeStudyInfo(StudyInfo si){
        //保存数据
        dao.store(si);
        baseView.showTips("保存成功！");
    }

    public long getCurrentTime(){
        return System.currentTimeMillis();
    }
}
