package cn.openui.www.positionmsg.util.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import cn.openui.www.positionmsg.util.HttpContext;
import cn.openui.www.positionmsg.util.IUriResourceHandler;

/**
 * Created by My on 2017/10/31.
 */
public class Myhandler implements IUriResourceHandler {

    private final String prefix = "/openui";

    @Override
    public boolean accept(String uri) {
        return uri.startsWith(prefix);
    }

    @Override
    public void handle(String uri, HttpContext context) {
        try {
            OutputStream  os = context.getUnderlySocket().getOutputStream();
            PrintWriter writer = new PrintWriter(os);
            writer.println("http/1.1 200 ok");
            writer.println();
            writer.println("out put content");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
