package cn.openui.www.controllmyself.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by My on 2017/10/12.
 */
public class BitMapUtil {

    public static Bitmap resize(Bitmap bitmap, int newWidth, int newHeight){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return bitmap;
    }
}
