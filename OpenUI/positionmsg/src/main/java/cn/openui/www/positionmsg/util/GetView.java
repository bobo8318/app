package cn.openui.www.positionmsg.util;

import android.view.View;

/**
 * Created by My on 2017/10/17.
 */
public class GetView<T> {
    public T getView(View father, int id){
        return (T)father.findViewById(id);
    }
}
