package cn.openui.www.imbatao.customview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import cn.openui.www.imbatao.R;

/**
 * Created by My on 2018/3/22.
 */
public class MyFreshLayout extends SwipeRefreshLayout {

    private float startY;
    private float startX;
    // 记录viewPager是否拖拽的标记
    private boolean mIsVpDragger;
    private MoreItem moreItem;

    public MyFreshLayout(Context context) {
        super(context);
    }

    public MyFreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //int action = ev.getAction();
        StaggeredGridView child = (StaggeredGridView) this.findViewById(R.id.staggeredGridView);
        int top = child.getFirstPosition();
        int size = child.getChildCount();
        Log.i("onIntercep",top+"-"+size);
        if(top==0) {//顶部 刷新
            return super.onInterceptTouchEvent(ev);
        }else if(top == size&& top !=0){//底部加载
            Log.i("onIntercep","more data");
            if(moreItem!=null)
                moreItem.moreData();
            return false;
        }else{
            return false;

        }
    }
    public void setMoreItem(MoreItem mi){
        this.moreItem = mi;
    }
    public interface MoreItem{
        void moreData();
    }

}
