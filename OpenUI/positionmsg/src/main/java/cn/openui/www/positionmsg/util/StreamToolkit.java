package cn.openui.www.positionmsg.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by My on 2017/10/31.
 */
public class StreamToolkit {

    public static String readLine(InputStream is){
        StringBuffer sb = new StringBuffer();
        int c1 = 0;
        int c2 = 0;
        while(c2!=-1 && !(c1=='\r' && c2=='\n')){
            c1 = c2;
            try {
                c2 = is.read();
                sb.append((char)c2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(sb.length()==0)
            return null;
        return sb.toString();
    }
}
