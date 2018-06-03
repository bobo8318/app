package cn.openui.www.controllmyself.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import cn.openui.www.controllmyself.R;

/**
 * Created by My on 2018/2/8.
 */
public class BottomBehavior extends CoordinatorLayout.Behavior<View> {

    public BottomBehavior() {
        Log.i("BottomBehavior","create 0");
    }

    public BottomBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i("BottomBehavior","create");
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return super.layoutDependsOn(parent,child,dependency);
        //return dependency.getId() == R.id.buyLog;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        //float translationY = Math.abs(dependency.getTop());//获取更随布局的顶部位置
        //child.setTranslationY(translationY);
        //Log.i("onDependentViewChanged",""+dependency.getId());
        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
       // Log.i("bottom onStartScroll",""+target.getTag());;

        if(target.getId() == R.id.logswesh && nestedScrollAxes == 2){
            return true;
        }
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    private static final float TARGET_HEIGHT = 200; // 最大滑动距离
    private float mTotalDy;     // 总滑动的像素数
    private float mLastScale;   // 最终放大比例
    private int mLastBottom;    // AppBarLayout的最终Bottom值
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.i("onNestedScroll",""+dyConsumed);
        if(target!=null&&dyConsumed>=0){//手指向上滑头

            child.setVisibility(View.GONE);
        }else{
            child.setVisibility(View.VISIBLE);
        }
        //super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }
}
