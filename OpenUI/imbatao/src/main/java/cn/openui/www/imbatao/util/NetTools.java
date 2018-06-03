package cn.openui.www.imbatao.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by My on 2017/8/17.
 */
public class NetTools {
    public String getJsonData(String url){

        InputStream is = this.request(url);
        String result = streamToString(is);
        return result;
    }

    public Bitmap getImg(String url){

        InputStream is = this.request(url);
        Bitmap bm = BitmapFactory.decodeStream(is);
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;
    }

    private InputStream request(String path){
        // 2.把网址封装成一个url对象
        URL url = null;
        try {
            url = new URL(path);
            // 3.获取链接对象,此时还没有建立连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 4.对链接对象初始化
            // 设置请求方法，注意大写
            conn.setRequestMethod("GET");
            // 设置连接超时
            conn.setConnectTimeout(5000);
            // 设置读取超时
            conn.setReadTimeout(5000);
            // 5.发送请求与服务器建立连接
            conn.connect();
            // 如果状态码等于200，说明请求成功
            if (conn.getResponseCode() == 200) {
                // 获取服务器相应头中的流，流中的数据就是客户端请求的数据
                InputStream is = conn.getInputStream();
                return is;
            } else {
               return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public String streamToString(InputStream is) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
             }
            is.close();

            return sb.toString();
            /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);*/
        } catch (Exception e) {
           // Log.e(TAG, e.toString());
            return null;
        }
    }
}
