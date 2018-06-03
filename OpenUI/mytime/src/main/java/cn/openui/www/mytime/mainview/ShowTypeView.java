package cn.openui.www.mytime.mainview;

import java.util.List;

import cn.openui.www.mytime.model.DetailType;

/**
 * Created by My on 2017/12/5.
 */
public interface ShowTypeView {

    public void listBigType(List<DetailType> datalist);
    public void listDetailType(List<DetailType> datalist);
    public void ShowDetailType(List<DetailType> datalist);
    public void closeSlibeTyle(String bigType);
}
