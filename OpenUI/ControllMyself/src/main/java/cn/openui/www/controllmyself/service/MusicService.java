package cn.openui.www.controllmyself.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import static java.lang.Thread.sleep;

/**
 * Created by My on 2017/10/9.
 */
public class MusicService extends Service {

    public static final int PLAY = 1;
    public static final int STOP = 2;
    public static final int PAUSE = 3;
    public static final int RESUME = 4;

    private int playCount = 5;
    private int playSleep = 1000;
    private boolean play_flag= false;

    private MediaPlayer mplayer = new MediaPlayer();

    private Messenger messageService = new Messenger(new Handler(){
        @Override
        public void handleMessage(Message msg) {

            Message msgToClient = Message.obtain(msg);
           // try {
                switch(msg.what){
                    case PLAY:
                        play_flag=true;
                        playCount=5;
                        playMusic(msg);
                        break;
                    case STOP:
                        play_flag=false;
                        break;
                    case PAUSE:
                        break;
                    default:break;
                }
               // msgToClient.replyTo.send(msgToClient);
           // } catch (RemoteException e) {
               // e.printStackTrace();
          //  }
            super.handleMessage(msg);
        }
    });

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messageService.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initMediaPlayer();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void playMusic(final Message clientMessage){
        new Thread(new Runnable(){
            @Override
            public void run() {
                while(play_flag){
                    if(playCount>0){//播放
                        if(!mplayer.isPlaying()){
                            mplayer.start();
                        }
                        playCount--;
                        float leftV = (float) (0.1*playCount);
                        float rightV = (float) (0.1*playCount);
                        mplayer.setVolume(leftV,rightV);
                        Log.i("sensor", "playCount:"+playCount);
                        try {
                            Thread.sleep(playSleep);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message message = Message.obtain(clientMessage);
                        try {
                            message.replyTo.send(message);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }else {//停止
                        if(mplayer.isPlaying()){
                            mplayer.pause();
                        }
                    }
                }
            }
        }).start();
    }

    private void initMediaPlayer() {
        try {
           //  AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.test);
           // mplayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());

            mplayer.prepare();
            mplayer.setLooping(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
