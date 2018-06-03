package cn.openui.www.controllmyself.customView;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by My on 2018/2/9.
 */
@CoordinatorLayout.DefaultBehavior(cn.openui.www.controllmyself.behavior.EasyBehavior.class)
public class MyButton extends Button {
    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
