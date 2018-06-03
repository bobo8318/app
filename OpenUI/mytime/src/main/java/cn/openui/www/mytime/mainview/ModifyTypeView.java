package cn.openui.www.mytime.mainview;

import cn.openui.www.mytime.model.DetailType;

/**
 * Created by My on 2017/12/10.
 */
public interface ModifyTypeView {

    public void upDateButtonText(String text);
    public void listFatherType(String[] datalist);

    public void showModifyType(DetailType dt);
}
