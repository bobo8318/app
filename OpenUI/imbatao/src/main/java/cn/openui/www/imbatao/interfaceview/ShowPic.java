package cn.openui.www.imbatao.interfaceview;

import java.util.List;

import cn.openui.www.imbatao.po.ItemPo;

/**
 * Created by My on 2018/3/14.
 */
public interface ShowPic {
    void showPic(List<ItemPo> datas);
    void stopFresh();
}
