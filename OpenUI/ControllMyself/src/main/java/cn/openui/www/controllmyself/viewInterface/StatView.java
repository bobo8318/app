package cn.openui.www.controllmyself.viewInterface;

import java.util.List;

import cn.openui.www.controllmyself.model.StatInfoModel;

/**
 * Created by My on 2017/12/25.
 */
public interface StatView {

    public void showStat(List<StatInfoModel> statInfoList);
    public void unableStatFresh();
}
