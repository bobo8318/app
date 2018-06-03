package cn.openui.www.imbatao.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by My on 2018/3/2.
 */
public class ImageLoader {

    private MemoryCache memoryCache = new MemoryCache();
    private FileCache fileCache;

    private Map<ImageView,String> imageviews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());

    ExecutorService executorService;
    Handler handler = new Handler();

    public ImageLoader(Context context){
        fileCache = new FileCache(context);
        executorService = Executors.newFixedThreadPool(5);
    }

    public void displayImg(String url, ImageView imageView){
        imageviews.put(imageView,url);
        Bitmap bitmap = memoryCache.getBitmap(url);
        if(bitmap!=null){
            imageView.setImageBitmap(bitmap);
        }else{
            PhotoToLoad p = new PhotoToLoad(url, imageView);
            executorService.submit(new PhotosLoader(p));
            imageView.setImageDrawable(null);
        }

    }

    private class PhotoToLoad {
        private String url;
        private ImageView imageView;
        public PhotoToLoad(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }
    }

    private class PhotosLoader implements Runnable {
        private PhotoToLoad p;
        public PhotosLoader(PhotoToLoad p) {
            this.p = p;
        }

        @Override
        public void run() {
            try {
                if(imageViewReused(p)){
                    return;
                }
                Bitmap bmp = getBitmap(p.url);
                memoryCache.put(p.url,bmp);
                if(imageViewReused(p)){
                    return;
                }
                BitmapDisplayer bd = new BitmapDisplayer(bmp,p);
                handler.post(bd);
            }catch (Throwable th){
                th.printStackTrace();
            }

        }

        private Bitmap getBitmap(String url) {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                byte[] Picture_bt = response.body().bytes();
                Bitmap bmp = BitmapFactory.decodeByteArray(Picture_bt, 0, Picture_bt.length);
                return bmp;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        boolean imageViewReused(PhotoToLoad p) {
            String tag = imageviews.get(p.imageView);
            if(tag==null||!tag.equals(p.url)){
                return true;
            }
            return false;
        }

        private class BitmapDisplayer implements Runnable{
            Bitmap bmp;
            PhotoToLoad p;
            public BitmapDisplayer(Bitmap bmp, PhotoToLoad p){
                this.bmp = bmp;
                this.p = p;
            }
            @Override
            public void run() {
                if(imageViewReused(p)){
                    return;
                }
                if(bmp !=null ){
                    p.imageView.setImageBitmap(bmp);
                }else{
                    p.imageView.setImageDrawable(null);
                }
            }
        }


    }

    public void clearCache(){
        memoryCache.clear();
        fileCache.clear();
    }
}
