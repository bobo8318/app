package cn.openui.www.controllmyself.viewInterface;

import android.view.View;

import java.util.List;

import cn.openui.www.controllmyself.model.BuyLogModel;

/**
 * Created by My on 2017/12/24.
 */
public interface BuyLogView {
    void showBuyLog(List<BuyLogModel> datas);
    void unableLogFresh();
    void showTips(String test);

    void showDialog(BuyLogModel model);

    void showUserInputDialog(String user);

    void jumpToLogin();
}
