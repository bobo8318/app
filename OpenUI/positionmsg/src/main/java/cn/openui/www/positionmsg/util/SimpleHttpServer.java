package cn.openui.www.positionmsg.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by My on 2017/10/31.
 */
public class SimpleHttpServer {

    private WebConfig config;
    private  ServerSocket socket;
    private boolean isenable = true;
    private  ExecutorService pool;
    private Set<IUriResourceHandler> handlers = new HashSet<IUriResourceHandler>();

    public SimpleHttpServer(int port, int max){
        config = new WebConfig();
        config.setPort(port);
        config.setMax(max);

        pool = Executors.newCachedThreadPool();
    }
    public void startAsync(){
        new Thread(new Runnable(){
            @Override
            public void run() {

            }
        }).start();
    }
    public void stopAsync(){
        if(!isenable){
            return;
        }else{
            isenable = false;
            try {
                socket.close();
                socket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processSync(){

        try {
            InetSocketAddress isa = new InetSocketAddress(config.getPort());
            socket = new ServerSocket();
            socket.bind(isa);
            while (isenable){
                final Socket remotepeer = socket.accept();
                pool.submit(new Runnable() {
                    @Override
                    public void run() {//执行任务
                        onAcceptRemotePeer(remotepeer);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onAcceptRemotePeer(Socket remotepeer) {
        HttpContext context = new HttpContext();

        try {
            InputStream is = remotepeer.getInputStream();
            String headerLine = "";
            String resourceuri = StreamToolkit.readLine(is).split(" ")[1];
            while((headerLine =StreamToolkit.readLine(is))!=null){
                if(headerLine.equals("\r\n"))
                    break;
                String[] header = headerLine.split(": ");
                context.putHeader(header[0],header[1]);
            }
            //处理反映结果
            for(IUriResourceHandler handler:handlers){
                if(handler.accept(resourceuri)){
                    handler.handle(resourceuri,context);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resgistHandler(IUriResourceHandler handler){
        handlers.add(handler);
    }

}
