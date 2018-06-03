package cn.openui.www.mytime.presenter;

import android.content.Context;

import java.util.List;

import cn.openui.www.mytime.dao.DBHelper;
import cn.openui.www.mytime.dao.StudyDao;
import cn.openui.www.mytime.mainview.BaseView;
import cn.openui.www.mytime.mainview.ListLogView;
import cn.openui.www.mytime.model.StudyInfo;

/**
 * Created by My on 2018/1/24.
 */
public class ListLogPresenter extends BasePresenter{

    private ListLogView view;
    private StudyDao dao;

    public ListLogPresenter(Context context, ListLogView view, BaseView view1){
        this.attach(context);
        this.view = view;
        this.setBaseView(view1);
        dao = new StudyDao(context);
    }

    public void showList(String type, String date) {
        List<StudyInfo> datas = dao.ListStudyInfo(type,date,"");
        view.showList(datas);
    }
}
