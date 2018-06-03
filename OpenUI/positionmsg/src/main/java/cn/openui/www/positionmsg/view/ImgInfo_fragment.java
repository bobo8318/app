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

import cn.openui.www.positionmsg.ConferenceActivity;
import cn.openui.www.positionmsg.R;

/**
 * Created by My on 2017/10/18.
 */
public class ImgInfo_fragment extends Fragment {

    private ImageView img;

    private Handler fragHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bitmap bm = (Bitmap) msg.obj;
            img.setImageBitmap(bm);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.img_frament,container,false);
        img = (ImageView)  view.findViewById(R.id.fragment_img);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       // System.out.println("attach img handler");
        ((ConferenceActivity) getActivity()).setHandler(fragHandler);
    }

    public void setImg(Bitmap bmp) {
        if(img!=null)
            img.setImageBitmap(bmp);
    }
}
