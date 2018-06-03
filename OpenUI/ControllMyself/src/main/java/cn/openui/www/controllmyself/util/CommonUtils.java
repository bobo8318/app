package cn.openui.www.controllmyself.util;

/**
 * Created by My on 2018/1/10.
 */
public class CommonUtils {

    public static String getRandomCoder(int length){
        String randomChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMDOPQRSTUVWXYZ1234567890";
        if(length>randomChar.length())
            return "";
        else{
            StringBuffer sb = new StringBuffer();
            for(int i=0;i<length;i++){
                int index = (int)Math.floor(Math.random()*randomChar.length());
                //System.out.println(randomChar.length()+" "+index);
                sb.append(randomChar.charAt(index));
            }
            return sb.toString();
        }
    }

}
