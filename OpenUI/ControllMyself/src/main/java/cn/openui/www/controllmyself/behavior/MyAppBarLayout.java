package cn.openui.www.controllmyself.behavior;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import cn.openui.www.controllmyself.R;
import cn.openui.www.controllmyself.util.TextUtils;

/**
 * Created by My on 2018/2/13.
 */
public class MyAppBarLayout extends AppBarLayout.Behavior {




    public MyAppBarLayout() {
        Log.i("behavior test","create 0");
    }

    public MyAppBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i("behavior test","create 1");

    }

    /**
     * AppBarLayout布局时调用
     *
     * @param parent 父布局CoordinatorLayout
     * @param abl 使用此Behavior的AppBarLayout
     * @param layoutDirection 布局方向
     * @return 返回true表示子View重新布局，返回false表示请求默认布局
     */
    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout abl, int layoutDirection) {
        return super.onLayoutChild(parent, abl, layoutDirection);
    }

    /**
     * 当CoordinatorLayout的子View尝试发起嵌套滚动时调用
     *
     * @param parent 父布局CoordinatorLayout
     * @param child 使用此Behavior的AppBarLayout
     * @param directTargetChild CoordinatorLayout的子View，或者是包含嵌套滚动操作的目标View
     * @param target 发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
     * @param nestedScrollAxes 嵌套滚动的方向
     * @return 返回true表示接受滚动
     */
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes) {
        Log.i("onStartNestedScroll","accept"+target.getId());
        if(target.getId() == R.id.buyLog && nestedScrollAxes == 2){

            return true;
        }
        return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes);
    }

    /**
     * 当嵌套滚动已由CoordinatorLayout接受时调用
     *
     * @param coordinatorLayout 父布局CoordinatorLayout
     * @param child 使用此Behavior的AppBarLayout
     * @param directTargetChild CoordinatorLayout的子View，或者是包含嵌套滚动操作的目标View
     * @param target 发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
     * @param nestedScrollAxes 嵌套滚动的方向
     */
    @Override
    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes) {
        Log.i("onNestedScrollAccepted",""+target.getId());
        //super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    /**
     * 当准备开始嵌套滚动时调用
     *
     * @param coordinatorLayout 父布局CoordinatorLayout
     * @param child 使用此Behavior的AppBarLayout
     * @param target 发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
     * @param dx 用户在水平方向上滑动的像素数
     * @param dy 用户在垂直方向上滑动的像素数
     * @param consumed 输出参数，consumed[0]为水平方向应该消耗的距离，consumed[1]为垂直方向应该消耗的距离
     */
    private float mTotalDy;     // 总滑动的像素数
    private static final float TARGET_HEIGHT = 500; // 最大滑动距离
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed) {
       //super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        if(dy<0){//向上滑动
            mTotalDy += -dy;
            mTotalDy = Math.min(mTotalDy, TARGET_HEIGHT);

            //child.setBottom(mLastBottom);
        }
        Log.i("onNestedPreScroll",""+target.getId());
    }

    /**
     * 嵌套滚动时调用
     *
     * @param coordinatorLayout 父布局CoordinatorLayout
     * @param child 使用此Behavior的AppBarLayout
     * @param target 发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
     * @param dxConsumed 由目标View滚动操作消耗的水平像素数
     * @param dyConsumed 由目标View滚动操作消耗的垂直像素数
     * @param dxUnconsumed 由用户请求但是目标View滚动操作未消耗的水平像素数
     * @param dyUnconsumed 由用户请求但是目标View滚动操作未消耗的垂直像素数
     */
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
       // super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        Log.i("onNestedScroll",""+target.getId());
    }

    /**
     * 当嵌套滚动的子View准备快速滚动时调用
     *
     * @param coordinatorLayout 父布局CoordinatorLayout
     * @param child 使用此Behavior的AppBarLayout
     * @param target 发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
     * @param velocityX 水平方向的速度
     * @param velocityY 垂直方向的速度
     * @return 如果Behavior消耗了快速滚动返回true
     */
    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY) {
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    /**
     * 当嵌套滚动的子View快速滚动时调用
     *
     * @param coordinatorLayout 父布局CoordinatorLayout
     * @param child 使用此Behavior的AppBarLayout
     * @param target 发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
     * @param velocityX 水平方向的速度
     * @param velocityY 垂直方向的速度
     * @param consumed 如果嵌套的子View消耗了快速滚动则为true
     * @return 如果Behavior消耗了快速滚动返回true
     */
    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    /**
     * 当定制滚动时调用
     *
     * @param coordinatorLayout 父布局CoordinatorLayout
     * @param abl 使用此Behavior的AppBarLayout
     * @param target 发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
     */
    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target) {
        super.onStopNestedScroll(coordinatorLayout, abl, target);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, AppBarLayout child, View dependency) {
        return super.layoutDependsOn(parent,child,dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, AppBarLayout child, View dependency) {
        return super.onDependentViewChanged(parent, child, dependency);
    }
}
