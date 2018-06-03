package cn.openui.www.imbatao.dao;

import android.content.Context;

/**
 * Created by My on 2018/3/23.
 */
public class BaseDao {
    protected DBHelper helper;
    public BaseDao(Context context){
        helper = new DBHelper(context);
    }
}
