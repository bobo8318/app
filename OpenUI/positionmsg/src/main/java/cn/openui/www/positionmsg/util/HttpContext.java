package cn.openui.www.positionmsg.util;

import java.net.Socket;
import java.util.HashMap;

/**
 * Created by My on 2017/10/31.
 */
public class HttpContext {

    private Socket socket;
    private HashMap<String, String> headers = new HashMap<String, String>();

    public void setUnderlySocket(Socket socket){
        this.socket = socket;
    }

    public Socket getUnderlySocket(){
        return this.socket;
    }

    public void putHeader(String header, String value){
        this.headers.put(header,value);
    }

    public String getValue(String header){
        return headers.get(header);
    }
}
