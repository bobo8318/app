package pub.imba.www.imbabuy;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Test extends Activity {

    private boolean bound = false;
    MyService binder;
    Messenger mService;

    Messenger clientmessage = new Messenger(new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_est);
        doBindService();
        Button testbtn = (Button) this.findViewById(R.id.test_bt);
        testbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msgFromClient = Message.obtain(null, 1, 1,2);
                msgFromClient.replyTo = clientmessage;
                try {
                    mService.send(msgFromClient);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                Log.i("bind",binder.TestService("haowenhao"));
               // Log.i("bind",binder.TestService("haowenhao"));
            }
        });
    }

    private ServiceConnection myLocalServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("bind", "onServiceConnected");
           // binder = (MyService) ((MyService.MyBinder)service).getService();
            mService = new Messenger(service);
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("bind", "onServiceDisconnected");
            bound = false;
        }

    };

    private void doUnbindService() {
        if (bound) {
            unbindService(myLocalServiceConnection);
            Log.i("bind", "begin to unbind");
            bound = false;
        }
    }

    private void doBindService() {
        Log.i("bind", "begin to bind");
        Intent intent = new Intent(Test.this,MyService.class);
        bindService(intent, myLocalServiceConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onDestroy() {
        doUnbindService();
        super.onDestroy();
    }
}
