package pub.imba.www.OpenGameEngner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by My on 2017/10/12.
 */
public class BackGround{

    public final static int LeftTop = 0;
    public final static int RightTop = 1;
    public final static int RightBottom = 2;
    public final static int LeftBottom = 3;
    public final static int FILL_BACKGROUND = 4;
    public final static int TILE = 5;//瓦片图
    public final static int CUSTOM = 6;

    private int height;
    private int width;

    private int position_x = 0;
    private int position_y = 0;

    private Bitmap bitmap;//单张图片背景 固定背景

    private int align = CUSTOM;

    private boolean repeat_x = false;
    private boolean repeat_y = false;

    public BackGround setRepeat(boolean x, boolean y){
        repeat_x = x;
        repeat_y = y;

        return this;
    }

    public BackGround setAlign(int align){
        this.align = align;
        return this;
    }

    public BackGround setHeight(int height){
        this.height = height;
        return this;
    }
    public BackGround setWidth(int width){
        this.width = width;
        return this;
    }

    public BackGround setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
        return this;
    }


    public BackGround adaptBitmap(){

           //每个瓦片显示图片的对齐方式 调整图片 存入瓦片数组
            switch (align){
                case LeftTop://左上对齐
                    position_x = 0;
                    position_y = 0;
                    break;
                case RightTop:
                    position_y = 0;
                    position_x = width - bitmap.getWidth();
                    break;
                case LeftBottom:
                    position_x = 0;
                    position_y = height-bitmap.getHeight();
                    break;
                case RightBottom:
                    position_x = width - bitmap.getWidth();
                    position_y = height - bitmap.getHeight();
                    break;
                case FILL_BACKGROUND:
                    this.bitmap = BitMapUtil.resize(this.bitmap,width,height);
                    break;
                default:break;
            }
        return this;
    }

    public void draw(Canvas canvas, Paint paint){
        canvas.save();

        canvas.drawBitmap(bitmap,position_x,position_y,paint);

        canvas.restore();

    }

}
