package cn.openui.www.positionmsg.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by My on 2017/8/17.
 */
public class NetTools {
    public String getJsonData(String url){

        InputStream is = this.get(url);
        String result = streamToString(is);
        return result;
    }

    public Bitmap getImg(String url){

        InputStream is = this.get(url);
        Bitmap bm = BitmapFactory.decodeStream(is);
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;
    }
    private String post(String path, String postData){

        URL url = null;
        String result = "";

        try {
            url = new URL(path);
            HttpURLConnection  con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(2000);
            con.setReadTimeout(2000);
            //设置是否向httpURLConnection输出，因为post请求参数要放在http正文内，所以要设置为true
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-type","application/x-www-form-urlencoded");
            con.setRequestProperty("Accept-Charset","utf-8");
            con.setRequestMethod("post");
            con.connect();

            //getOutputStream会隐含调用connect()，所以不用写上述的httpURLConnection.connect()也行。
           //得到httpURLConnection的输出流
            OutputStream os = con.getOutputStream();
            DataOutputStream dataOut = new DataOutputStream(os);
            dataOut.writeBytes("data");
            //刷新对象输出流，将字节全部写入输出流中
            dataOut.flush();
            //关闭流对象
            dataOut.close();
            os.close();

            if(HttpURLConnection.HTTP_OK==con.getResponseCode()){
                InputStream is = con.getInputStream();
                result = streamToString(is);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }


    private InputStream get(String path){
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
        } catch (Exception e) {
            return null;
        }
    }
}
