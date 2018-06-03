package cn.openui.www.positionmsg.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.openui.www.positionmsg.R;

/**
 * Created by My on 2017/10/15.
 */
public class OpenTitle extends RelativeLayout {

    private TextView back_btn;
    private TextView setup_btn;
    private TextView title;
    private OnClickListener listener;

    public void setTitle(String title){
        this.title.setText(title);
    }

    public OpenTitle(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.openTitle);

        String title_info = ta.getString(R.styleable.openTitle_title_info);
        String left_info = ta.getString(R.styleable.openTitle_left_info);
        String right_info = ta.getString(R.styleable.openTitle_right_info);

       // String background = ta.getString(R.styleable.openTitle_left_background);
        int left_background = ta.getColor(R.styleable.openTitle_left_background,Color.WHITE);

        boolean left_show = ta.getBoolean(R.styleable.openTitle_title_left_show,true);
        boolean right_show = ta.getBoolean(R.styleable.openTitle_title_right_show,true);

        ta.recycle();


        LayoutInflater.from(context).inflate(R.layout.opentitle, this);

        back_btn = (TextView) this.findViewById(R.id.title_back_btn);
        back_btn.setText(left_info);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onLeftClick(v);
            }
        });
        setup_btn = (TextView) this.findViewById(R.id.title_set_btn);
        setup_btn.setText(right_info);
        setup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRightClick(v);
            }
        });


        title = (TextView) this.findViewById(R.id.title_info);

        title.setText(title_info);
        back_btn.setBackgroundColor(left_background);

        if(left_show){
            back_btn.setVisibility(View.VISIBLE);
        }else{
            back_btn.setVisibility(View.GONE);
        }

        if(right_show){
            setup_btn.setVisibility(View.VISIBLE);
        }else{
            setup_btn.setVisibility(View.GONE);
        }


    }

    public void setLeftText(String text){
        this.back_btn.setText(text);
    }
    public void setRightText(String text){
        this.setup_btn.setText(text);
    }

    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }

    public static interface OnClickListener{
        public void onLeftClick(View v);
        public void onRightClick(View v);
    }


}
