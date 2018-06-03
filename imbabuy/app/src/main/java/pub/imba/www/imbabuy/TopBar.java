package pub.imba.www.imbabuy;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * Created by My on 2017/9/13.
 */
public class TopBar extends RelativeLayout {

    private LayoutParams left,right,title;

    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.topBar);
        ta.getString(R.styleable.topBar_mytitle);

        ta.recycle();

        Button leftbtn = new Button(context);

        left = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        left.addRule(RelativeLayout.ALIGN_PARENT_LEFT,TRUE);
        addView(leftbtn,left);
    }
}
