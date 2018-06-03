package cn.openui.www.drawmsg.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by My on 2017/8/15.
 */
public class MyView extends SurfaceView implements SurfaceHolder.Callback,View.OnTouchListener{

    private Paint paint ;
    private Path path;
    private Bitmap bitmap;
    private Canvas bitcanvas;


    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setOnTouchListener(this);

       this.bitmap =  Bitmap.createBitmap(600, 800, Bitmap.Config.ARGB_8888);
        Log.e("test", "MyView: "+getWidth()+" "+getHeight() );
        this.bitcanvas = new Canvas(bitmap);

        this.paint = new Paint();
        this.path = new Path();
        this.paint.setAntiAlias(true);
        this.paint.setColor(Color.RED);
        this.paint.setTextSize(10);
        this.paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }
    public void draw() {
        Canvas canvas = getHolder().lockCanvas();
        canvas.drawColor(Color.WHITE);
        canvas.drawPath(this.path,this.paint);

      this.bitcanvas.drawPath(this.path,this.paint);

        getHolder().unlockCanvasAndPost(canvas);
    }

    public void clear(){
       this.path.reset();
        draw();
    }

    public void save(){
        String name = "drawpic.png";
        File file = new File("/sdcard/DCIM/"+name);
        if(file.exists()){
            file.delete();
        }
        FileOutputStream out;
        try{
            out = new FileOutputStream(file);
            if(this.bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.draw();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:this.path.moveTo(event.getX(),event.getY());draw();break;
            case MotionEvent.ACTION_MOVE:this.path.lineTo(event.getX(),event.getY());draw();break;
            default:break;
        }
        return true;
    }
}
