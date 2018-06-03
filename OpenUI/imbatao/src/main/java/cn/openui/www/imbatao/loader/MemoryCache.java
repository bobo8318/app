package cn.openui.www.imbatao.loader;

import android.graphics.Bitmap;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by My on 2018/3/2.
 */
public class MemoryCache {
     private String TAG = "MemoryCache";
     private Map<String,Bitmap> cache = Collections.synchronizedMap(new LinkedHashMap<String, Bitmap>(10,1.5f,true));
     private long size = 0;
     private long limit = 1000000;

     public MemoryCache(){
          setLimit(Runtime.getRuntime().maxMemory()/4);
     }

     public void setLimit(long l) {
          limit = l;
     }
     public Bitmap getBitmap(String id){
          try{
               if(!cache.containsKey(id)){
                    return null;
               }
               return cache.get(id);
          }catch(NullPointerException ex){
               ex.printStackTrace();
               return null;
          }
     }
     public void put(String id, Bitmap bitmap){
          try{
               if(!cache.containsKey(id)){
                    cache.put(id,bitmap);
                    size += getSizeBytes(bitmap);
                    checkSize();
               }

          }catch (NullPointerException ex){
               ex.printStackTrace();
          }

     }

     private void checkSize() {
          if(size > limit){
               Iterator<Map.Entry<String, Bitmap>> it = cache.entrySet().iterator();
               while(it.hasNext()){
                    Map.Entry<String, Bitmap> entry = it.next();
                    size -= getSizeBytes(entry.getValue());
                    it.remove();
                    if(size<= limit){
                         break;
                    }
               }
          }
     }

     private long getSizeBytes(Bitmap value) {
          if(value==null)
               return 0;
          else
               return value.getRowBytes()*value.getHeight();
     }

     public void clear(){
          try{
               cache.clear();
               size = 0;
          }catch (NullPointerException ex){
               ex.printStackTrace();
          }

     }
}
