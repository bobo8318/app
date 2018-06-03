package cn.openui.www.imbatao.util;

import android.os.Environment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by My on 2018/3/26.
 */
public class QRCodeFactory {
     private static void createZxing() throws WriterException, IOException {
         int width=300;
         int hight=300;
         String format="png";
         String content="www.baidu.com";
         HashMap hints=new HashMap();
         hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
         hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);//纠错等级L,M,Q,H
         hints.put(EncodeHintType.MARGIN, 2); //边距
         BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, hight, hints);
         /*String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/imbatao/img/";
         File file = new File(dir+"imag.png");
         MatrixToImageWriter.writeToPath(bitMatrix, format, file);*/
     }
}
