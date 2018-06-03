package cn.openui.www.positionmsg.model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by My on 2017/10/20.
 */
public class Conference implements Serializable {

    private static final long serialVersionUID = -7060210544600464481L;

    public static final int FENJU = 0;
    public static final int PCS = 1;

    private int id;
    private String name;
    private String info;
    private String phone;
    private String img;
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
