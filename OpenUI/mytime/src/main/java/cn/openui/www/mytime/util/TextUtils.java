package cn.openui.www.mytime.util;

/**
 * Created by My on 2017/12/9.
 */
public class TextUtils {
    public static boolean isEmpty(String text){
        if(text==null||"".equals(text)){
            return true;
        }else{
            return false;
        }
    }
}
