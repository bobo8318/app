package cn.openui.www.positionmsg.util;

import android.graphics.Bitmap;

import cn.openui.www.positionmsg.model.Conference;
import cn.openui.www.positionmsg.view.ImgInfo_fragment;
import cn.openui.www.positionmsg.view.TextInfo_fragment;

/**
 * Created by My on 2018/2/1.
 */
public class FragmentHolder {

    private Conference model;
    private ImgInfo_fragment imgfrag;
    private TextInfo_fragment textinfo;

    public void setModel( Conference model){
        this.model = model;
    }

    public ImgInfo_fragment getImgfrag() {
        return imgfrag;
    }

    public void setImgfrag(ImgInfo_fragment imgfrag) {
        this.imgfrag = imgfrag;
    }

    public TextInfo_fragment getTextinfo() {
        return textinfo;
    }

    public void setTextinfo(TextInfo_fragment textinfo) {
        this.textinfo = textinfo;
    }

    public void showImg(Bitmap img) {
        if(imgfrag!=null){
            imgfrag.setImg(img);
        }
    }

    public void showConferenceInfo() {
        if(textinfo!=null&&model!=null){
            textinfo.setText(model.getInfo());
        }
    }

    public void showConferencePhone() {
        if(textinfo!=null&&model!=null){
            textinfo.setText(model.getPhone());
        }
    }
}
