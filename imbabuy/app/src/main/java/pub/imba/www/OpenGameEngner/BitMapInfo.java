package pub.imba.www.OpenGameEngner;

import android.graphics.Bitmap;

/**
 * Created by My on 2017/10/12.
 */
public class BitMapInfo {

    private Bitmap bitmap;

    private int position_x;
    private int posiiton_y;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getPosition_x() {
        return position_x;
    }

    public void setPosition_x(int position_x) {
        this.position_x = position_x;
    }

    public int getPosiiton_y() {
        return posiiton_y;
    }

    public void setPosiiton_y(int posiiton_y) {
        this.posiiton_y = posiiton_y;
    }

    public void move(int speed_x, int speed_y){
        position_x += speed_x;
        posiiton_y += speed_y;
    }
}
