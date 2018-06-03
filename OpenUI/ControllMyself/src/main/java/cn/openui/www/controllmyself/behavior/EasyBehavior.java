package cn.openui.www.controllmyself.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by My on 2018/2/8.
 */
public class EasyBehavior extends CoordinatorLayout.Behavior<TextView>  {
    public EasyBehavior() {
        Log.i("EasyBehavior","create 0");

    }
    public EasyBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i("EasyBehavior","create 1");

    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, TextView child, View dependency) {
        //告知监听的dependency是Button
        return dependency instanceof Button;
    }

    @Override
    //当 dependency(Button)变化的时候，可以对child(TextView)进行操作
    public boolean onDependentViewChanged(CoordinatorLayout parent, TextView child, View dependency) {
        Log.i("onDependentViewChanged",""+child.getText());
        child.setX(dependency.getX()+200);
        child.setY(dependency.getY()+200);
        child.setText(dependency.getX()+","+dependency.getY());

        return true;
    }
}
