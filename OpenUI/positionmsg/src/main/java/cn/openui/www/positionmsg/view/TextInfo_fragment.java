package cn.openui.www.positionmsg.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.openui.www.positionmsg.ConferenceActivity;
import cn.openui.www.positionmsg.R;

/**
 * Created by My on 2017/10/18.
 */
public class TextInfo_fragment extends Fragment {
    private TextView info;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
           // super.handleMessage(msg);
            info.setText(msg.obj.toString());

        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_frament,container,false);
        info = (TextView) view.findViewById(R.id.fragment_info);
        return view;
    }

    public void setText(String info){
        this.info.setText(info);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //System.out.println("attach info handler");
        ((ConferenceActivity)getActivity()).setTextHandler(handler);
    }
}
