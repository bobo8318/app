package cn.openui.www.mytime.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import cn.openui.www.mytime.R;

/**
 * Created by My on 2018/1/19.
 */
public class MyBar extends View {

    private Paint mPaint;
    private Paint textPaint;
    private int mWidth;
    private int mHeight;
    private int mStartWidth;
    private int mSize;
    private int mChartWidth;

    private String item;


    private String title;
    private String xAxisName;
    private String yAxisName;
    private float axisTextSize;
    private int axisTextColor;

    private int width;
    private int height;

    private int originX;
    private int originY;

    private int colors[];

    private String  orientation;
    private float blankwidth;

    private BarDataAdapter adapter;
    private ClickListener listener;
    private float rate;//图表数值与图形长度 转换比；

    private float labellenth;
    private float ceilwidth;

    private Rect[] barRects;

    public MyBar(Context context) {
        this(context, null);
    }
    public MyBar(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }
    public MyBar(Context context, AttributeSet attrs,int defStyleAttr) {
        super(context, attrs,defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.MyBar);
        title = array.getString(R.styleable.MyBar_grapTitle);
        xAxisName = array.getString(R.styleable.MyBar_xAxisName);
        yAxisName = array.getString(R.styleable.MyBar_yAxisName);
        axisTextSize = array.getDimension(R.styleable.MyBar_axisTextSize,12);
        axisTextColor = array.getColor(R.styleable.MyBar_axisTextColor,context.getResources().getColor(android.R.color.background_dark));
        orientation = array.getString(R.styleable.MyBar_orientation);
        blankwidth = array.getDimension(R.styleable.MyBar_blankwidth,10);
        ceilwidth = array.getDimension(R.styleable.MyBar_ceilwidth,20);
        if(array!=null)
            array.recycle();
        init();
    }
    private void init(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//防锯齿
        mPaint.setDither(true);//防抖动
        mPaint.setColor(Color.BLUE);


        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(90f);

        mSize = 100;
        item = "测试";

        colors = new int[]{
                Color.BLUE,
                Color.GREEN,
                Color.RED,
                Color.YELLOW
        };

        originX = 0;
        originY = getHeight()-40;
    }
    public  void setAdapter(BarDataAdapter adapter){
        this.adapter = adapter;
        //调整数据适配
        if(orientation.equals("ud")) {//上下方向图
            rate = adapter.getMaxValue()/getHeight();
        }else  if(orientation.equals("lr")) {//上下方向图
            rate = adapter.getMaxValue()/getWidth();
        }
    }
    public void setOnClickListener(ClickListener listener){
        this.listener = listener;
    }
    @Override
    protected void onDraw(Canvas canvas) {

        //super.onDraw(canvas);

        //width = getWidth()-originX;
        //height = (originY>getHeight()?getHeight():height)-400;

        /*drawXAxis(canvas,mPaint);
        drawYAxis(canvas,mPaint);
        drawTitle(canvas,mPaint);
        drawXAxisScale(canvas,mPaint);
        drawXAxisScaleValue(canvas,mPaint);
        drawYAxisScale(canvas,mPaint);
        drawYAxisScaleValue(canvas,mPaint);
        drawXAxisArrow(canvas,mPaint);
        drawYAxisArrow(canvas,mPaint);*/
        drawXAxis(canvas,mPaint);
        drawColumn(canvas,textPaint);


       /* canvas.drawText(item, 0 ,mHeight ,textPaint);

        RectF rectF = new RectF();
        rectF.left = 0;
        rectF.right = mSize;
        rectF.bottom = mHeight;
        rectF.top = (float) (mHeight-40);

        canvas.drawRoundRect(rectF, 10, 10, mPaint);*/

    }

    private void drawXAxis(Canvas canvas, Paint mPaint) {
        mPaint.setTextSize(30);
        for(int i=0;i<adapter.getCount();i++){
            if(orientation.equals("ud")) {//上下
                ;
            }else if(orientation.equals("lr")){//左右
                float ceilwidth = height/adapter.getCount()-this.blankwidth;

                float xposition = 0;
                float yposition = (1+i)*(ceilwidth+blankwidth)-20;
                canvas.drawText(adapter.getLabel(i),xposition,yposition,mPaint);
               if( mPaint.measureText(adapter.getLabel(i))> this.labellenth)
                   this.labellenth =  mPaint.measureText(adapter.getLabel(i));
            }
        }

    }

    private void drawColumn(Canvas canvas, Paint mPaint) {
        //判断方向
        Log.i("draw column orientation",orientation);
        if(orientation.equals("ud")){//上下
            float ceilwidth = width/adapter.getCount()-this.blankwidth;
            for(int i=0;i<adapter.getCount();i++){
                //int color = Color.BLUE;
               // color  = adapter.getColor(i)==0? colors[i%4]: adapter.getColor(i);

                float left = i*(ceilwidth+blankwidth)+blankwidth;
                float top = adapter.getValue(i);
                float right = (i+1)*(blankwidth+ceilwidth);
                float bottom = height;
                Log.i("draw column",left+"-"+top+"-"+right+"-"+bottom);
                mPaint.setColor(colors[i%4]);
               canvas.drawRect(left,top,right,bottom,mPaint);

            }

        }else if(orientation.equals("lr")) {//左右
            this.barRects = new Rect[adapter.getCount()];
            float ceilwidth = height/adapter.getCount()-this.blankwidth;
            for(int i=0;i<adapter.getCount();i++) {
                int color = adapter.getColor(i) == 0 ? colors[i % 4] : adapter.getColor(i);

                int left = (int) labellenth;
                int top = (int) (i * (blankwidth + ceilwidth) + blankwidth);
                int right = (int) (labellenth+width*adapter.getRate(i));
                int bottom = (int) ((i + 1) * (blankwidth + ceilwidth));

                Rect bar = new Rect(left, top, right, bottom);
                this.barRects[i] = bar;
                Log.i("draw column", left + "-" + top + "-" + right + "-" + bottom+"-"+adapter.getRate(i)+"-"+width);
                mPaint.setColor(colors[i%4]);
                canvas.drawRect(bar, mPaint);
            }
        }



    }

    private void drawYAxisArrow(Canvas canvas, Paint mPaint) {
        Path mpathx = new Path();
        mpathx.moveTo(originX+width+30,originY);
        mpathx.moveTo(originX+width,originY+10);
        mpathx.moveTo(originX+width,originY-10);
        mpathx.close();
        canvas.drawPath(mpathx,mPaint);
        canvas.drawText(xAxisName,originX+width,originY+50,mPaint);
    }

    private void drawXAxisArrow(Canvas canvas, Paint mPaint) {
        Path mpathx = new Path();
        mpathx.moveTo(originX+width+30,originY);
        mpathx.moveTo(originX+width,originY+10);
        mpathx.moveTo(originX+width,originY-10);
        mpathx.close();
        canvas.drawPath(mpathx,mPaint);
        canvas.drawText(xAxisName,originX+width,originY+50,mPaint);
    }

    private void drawTitle(Canvas canvas, Paint mPaint) {
        if(!TextUtils.isEmpty(title)){
            mPaint.setTextSize(this.axisTextSize);
            mPaint.setColor(this.axisTextColor);
            mPaint.setFakeBoldText(true);

            canvas.drawText(title,(getWidth()/2)-(mPaint.measureText(title)),originY+40,mPaint);
        }
    }

    //测量高宽度
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width;
        int height;
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = widthSize * 1 / 2;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            if(orientation.equals("lr")){
                height = (int) ((blankwidth+ceilwidth)*adapter.getCount());
            }else{
                height = heightSize*1/2;
            }
        }

        setMeasuredDimension(width, height);
    }
    //计算高度宽度
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    public boolean onTouchEvent( MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                int x = (int) event.getX();
                int y = (int) event.getY();
                //Log.i("touch position",x+"-"+y);
                for(int i=0;i<barRects.length;i++){
                    Region region = new Region(barRects[i]);
                    if(region.contains(x,y)){
                        listener.onClick(adapter.getLabel(i),adapter.getValue(i));
                        break;
                    }
                }
                break;
            default:break;
        }
        return false;
    }

    public interface BarDataAdapter{
        public float getValue(int position);
        public float getMaxValue();
        public int getColor(int position);
        public int getCount();
        public String getLabel(int position);
        public float getRate(int postion);
    }

    public interface ClickListener{
        public void onClick(String tag, float value);
    }
}
