package pub.imba.www.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import pub.imba.www.OpenGameEngner.BackGround;
import pub.imba.www.OpenGameEngner.Decotator;
import pub.imba.www.OpenGameEngner.OpenGameEngner;
import pub.imba.www.OpenGameEngner.layer.TileLayer;
import pub.imba.www.imbabuy.R;

/**
 * Created by My on 2017/10/10.
 */
public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable,View.OnTouchListener {

    private SurfaceHolder sfh;

    private Bitmap background_bitmap;
    private Paint paint;

    private boolean flag = false;

    private OpenGameEngner game;

    public MySurfaceView(Context context) {
        super(context);
        sfh = this.getHolder();
        sfh.addCallback(this);
        background_bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.tile1);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        this.setOnTouchListener(this);
        setFocusable(true);

    }

    public void draw(){
        Canvas canvas = sfh.lockCanvas();

       // game.draw(canvas,paint);
        TileLayer tilelayer = new TileLayer(10,10,background_bitmap,10,10);
        tilelayer.isVisible();
        tilelayer.paint(canvas);

        sfh.unlockCanvasAndPost(canvas);
    }

    public void logic(){

    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        game = new OpenGameEngner(this.getWidth(),this.getHeight());
        //设置瓦片大小
        game.setTileSize(50,50);
        game.buildMap();//设置地图矩阵

        game.getBackGround().setBitmap(background_bitmap).setAlign(BackGround.FILL_BACKGROUND);//设置固定背景

        Decotator weather_layer = new Decotator();



        //game.setDecorator();//设置装饰品矩阵
        //game.setSpit();//设置精灵矩阵
        draw();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        flag = false;
    }

    @Override
    public void run() {
        while(flag){
            try {
                draw();
                Thread.sleep(200);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(flag){
            System.out.println("end back");
            flag = false;
        }
        else{
            System.out.println("start back");
            flag = true;
            new Thread(this).start();
        }
        return false;
    }
}
