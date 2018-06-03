package cn.openui.www.positionmsg.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.openui.www.positionmsg.R;

/**
 * Created by My on 2017/9/14.
 */
public class FlowLayout extends ViewGroup {

    private Context context;
    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public FlowLayout(Context context) {
        super(context);
        this.context = context;
    }
    public void adapterData(String ... tags){
        for(int i=0;i<tags.length;i++){
            TextView text  = (TextView) LayoutInflater.from(context).inflate(R.layout.label,this,false);
            text.setText(tags[i]);
            text.setTag(i);
            this.addView(text);
        }

    }
    private OnTagClickListener tagClickListener;

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(this.getContext(),attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        listview.clear();
        lineheight.clear();

        int widthsize = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);

        int heightsize = MeasureSpec.getSize(heightMeasureSpec);
        //int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = 0;

        int lineWidth = 0;
        int lineHeight = 0;
        int count = this.getChildCount();
        List<View> lineview = null;
        int i=0;
        for(;i<count;i++){
            View child = this.getChildAt(i);

            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            MarginLayoutParams mlp = (MarginLayoutParams) child.getLayoutParams();
            int childwidth = child.getMeasuredWidth()+mlp.leftMargin+mlp.rightMargin;
            int childheight = child.getMeasuredHeight()+mlp.topMargin+mlp.bottomMargin;
            if(lineWidth+childwidth > widthsize - getPaddingLeft() -getPaddingRight()||lineWidth==0){//新行
                lineview = new ArrayList<View>();

                width = Math.max(lineWidth,width);//取最宽的一行为最终宽度

                height += lineHeight;//增加总行高

                lineWidth = childwidth;//新行
                lineHeight = childheight;
                lineview.add(child);

                listview.add(lineview);
                if(lineHeight!=0)
                 lineheight.add(lineHeight);
            }else{//原行 加入child
                lineWidth += childwidth;
                lineHeight = Math.max(lineHeight,childheight);
                if(lineview!=null)
                    lineview.add(child);
            }

        }
        if(i==count){//最后一行
            height += lineHeight;
            width = Math.max(lineWidth,width);
            lineheight.add(lineHeight);
        }
        //wrap content
        if(MeasureSpec.AT_MOST == modeWidth){
            setMeasuredDimension(width+getPaddingRight()+getPaddingLeft(),height+getPaddingTop()+getPaddingBottom());
        }else{
            setMeasuredDimension(widthsize,heightsize);
        }
    }

    private List<List<View>> listview = new ArrayList<List<View>>();

    private List<Integer> lineheight = new ArrayList<Integer>();
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = getPaddingLeft();
        int top = getPaddingTop();

            for(int i=0;i<listview.size();i++){//每行

                List<View> lineview = listview.get(i);
                for(int j=0;j<lineview.size();j++){//每个
                    View childview = lineview.get(j);
                    childview.setOnClickListener(new View.OnClickListener(){//每个tag添加点击事件
                        @Override
                        public void onClick(View v) {
                            tagClickListener.onTagClick(v);
                        }
                    });
                    if(childview.getVisibility()==View.GONE){//隐藏
                        continue;
                    }


                    MarginLayoutParams mpl = (MarginLayoutParams) childview.getLayoutParams();
                    //Log.i("float","childview:"+((TextView)childview).getText());
                    childview.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    int childwidth = childview.getMeasuredWidth();
                    int childheight = childview.getMeasuredHeight();
                    //Log.i("float","children :"+childwidth+"-"+childwidth);
                    int lc = left + mpl.leftMargin;
                    int tc = top + mpl.topMargin;
                    int rc = lc + childwidth;
                    int bc = tc + childheight;

                   // Log.i("float",childview.getTag()+":第"+i+"行："+left+" top:"+top+"-"+lc+"-"+tc+"-"+rc+"-"+bc);

                    childview.layout(lc,tc,rc,bc);

                    left += childwidth + mpl.leftMargin + mpl.rightMargin;

                }
                top +=  lineheight.get(i) ;
                left = getPaddingLeft();
        }

    }

    public void setOnTagClickListener(OnTagClickListener tagClickListener){
       this.tagClickListener = tagClickListener;
    }

    public static interface OnTagClickListener{
        public void onTagClick(View view);
    }
}
