package cn.openui.www.positionmsg.util;

/**
 * Created by My on 2017/10/31.
 */
public interface IUriResourceHandler {
    public boolean accept(String uri);
    public void handle(String uri, HttpContext context);
}
