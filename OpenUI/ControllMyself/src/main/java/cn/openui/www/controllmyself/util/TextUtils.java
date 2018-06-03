package cn.openui.www.controllmyself.util;

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
    public static boolean isJsonNull(String text){
        if(text==null||"".equals(text)||"{}".equals(text)||"[]".equals(text)){
            return true;
        }else{
            return false;
        }
    }

    public static String showBoolean(boolean result33) {
        if(result33)
            return "true";
        else
            return "false";
    }
}
