package cn.openui.www.positionmsg.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cn.openui.www.positionmsg.R;

/**
 * Created by My on 2017/10/16.
 */
public class ConferenceInfo extends Fragment{

    private ImageView jigui;
    private ImageView tv;
    private TextView conference_text_info;

    private int type;

    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.img_frament,container,false);
        return view;
    }

    public void setJigui(Bitmap bitmap){
       jigui.setImageBitmap(bitmap);
    }

    public void setTv(Bitmap bitmap){
       tv.setImageBitmap(bitmap);
    }

    public void setInfo(String value){
        conference_text_info.setText(value);
    }


}
