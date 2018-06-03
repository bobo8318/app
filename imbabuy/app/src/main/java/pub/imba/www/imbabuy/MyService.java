package pub.imba.www.imbabuy;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by My on 2017/9/12.
 */
public class MyService extends Service {
    private static final String TAG = "LocalService";

    private  Messenger serverMessage = new Messenger(new Handler(){
        @Override
        public void handleMessage(Message msgfromClient) {
            Message msgToClient = Message.obtain(msgfromClient);//使用客户端消息生成新message
            try {
                msgToClient.arg2 = msgfromClient.arg1 + msgfromClient.arg2;
                msgToClient.replyTo.send(msgToClient);// msgToClient.replyTo 客户端message对象
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            super.handleMessage(msgfromClient);
        }
    });

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //return new MyBinder();
        return serverMessage.getBinder();
    }

    @Override
    public void onCreate() {
        Log.i("bind", "onCreate");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.i("bind", "onDestroy");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("bind", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.i("bind", "onStart");
        super.onStart(intent, startId);
    }

    public class MyBinder extends Binder {
        public Service getService(){
            return MyService.this;
        }
    }

    public String TestService(String test){
        return "test:"+test;
    }
}
