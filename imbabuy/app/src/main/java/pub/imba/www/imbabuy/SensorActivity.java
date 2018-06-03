package pub.imba.www.imbabuy;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

import pub.imba.www.service.MusicService;

public class SensorActivity extends Activity {

    private boolean bound = false;
    private Thread musicThread = null;

    private Messenger message;


    public static final int PLAY = 1;
    public static final int STOP = 2;
    public static final int PAUSE = 3;
    public static final int RESUME = 4;

    private int playCount = 5;
    private int playSleep = 1000;
    private boolean play_flag= false;

    private MediaPlayer mplayer = new MediaPlayer();

    private Messenger clientMsg = new Messenger(new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    });

    private SensorManager sm;
    private Vibrator vibrator;

    private Bitmap bitmap;

    public SensorActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        doBindService();
        initMediaPlayer();

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);//传感器
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);//震动
        Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);// 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率



        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.timg);
        ImageView img = (ImageView) this.findViewById(R.id.sensor_img);
        img.setImageBitmap(bitmap);

        Button btn = (Button) this.findViewById(R.id.play_btn);

    }

    @Override
    protected void onStop() {
        super.onStop();
        sm.unregisterListener(sensorEventListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    private SensorEventListener sensorEventListener = new SensorEventListener(){
        @Override
        public void onSensorChanged(SensorEvent event) {//数值变化
            float[] values = event.values;
            float x = values[0]; // x轴方向的重力加速度，向右为正
            float y = values[1]; // y轴方向的重力加速度，向前为正
            float z = values[2]; // z轴方向的重力加速度，向上为正

            //Log.i("openui sensor","xyz:"+x+"-"+y+"-"+z);
            int medumValue = 10;
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {//摇一摇
                //播放声音
                Log.i("openui sensor","shake");
               /* Message msgFromClient = Message.obtain(null, MusicService.PLAY, 0,0);
                msgFromClient.replyTo = clientMsg;

                try {
                    message.send(msgFromClient);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }*/
                play_flag = true;
                playCount = 5;
                if(musicThread == null)
                    playMusic();


            }else{
                //停止播放
               // Log.i("openui sensor","stop");
               /* Message msgFromClient = Message.obtain(null, MusicService.PAUSE, 0,0);
                msgFromClient.replyTo = clientMsg;
                try {
                    message.send(msgFromClient);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }*/
            }
        }
        @Override
        public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {//精度变化

        }
    };



    private ServiceConnection sc = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            message = new Messenger(service);
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

    private void doBindService() {
        Log.i("bind", "begin to bind");
        Intent intent = new Intent(this,MusicService.class);
        bindService(intent, sc, Context.BIND_AUTO_CREATE);
        bound = true;
    }

    private void doUnbindService() {
        if (bound) {
            unbindService(sc);
            Log.i("bind", "begin to unbind");
            bound = false;
        }
    }


    public void playMusic(){
        musicThread = new Thread(new Runnable(){
            @Override
            public void run() {
                while(play_flag){
                    if(playCount>0){
                        if(!mplayer.isPlaying()){
                            mplayer.start();
                        }
                        playCount--;
                        float leftV = (float) (0.2*playCount);
                        float rightV = (float) (0.2*playCount);
                        mplayer.setVolume(leftV,rightV);
                        Log.i("sensor", "playCount:"+playCount);
                        try {
                            Thread.sleep(playSleep);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else {
                        if(mplayer.isPlaying()){
                            mplayer.pause();
                        }
                    }
                }
            }
        });
        musicThread.start();
    }

    private void initMediaPlayer() {
        try {
            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.test);
            mplayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());

            mplayer.prepare();
            mplayer.setLooping(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
