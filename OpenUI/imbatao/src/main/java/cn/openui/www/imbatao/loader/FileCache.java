package cn.openui.www.imbatao.loader;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by My on 2018/3/2.
 */
public class FileCache {
    private File cacheDir;

    public FileCache(Context context){
        if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(),"LasyList");
        }else{
            cacheDir = context.getCacheDir();
        }
        if(!cacheDir.exists())
            cacheDir.mkdir();
    }

    public File getFile(String url){
        String filename = String.valueOf(url.hashCode());
        File f = new File(cacheDir,filename);
        return f;
    }
    public void clear(){
        File[] files = cacheDir.listFiles();
        if(files==null){
            return;
        }
        for(File f:files){
            f.delete();
        }
    }
}
