package model;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by My on 2016/10/15.
 */
public class WebViewJs {
    private Context context;
    public WebViewJs(Context context){
        this.context = context;
    }

    @JavascriptInterface
    public void test(){
        Toast.makeText(context,"js test",Toast.LENGTH_LONG).show();
    }
}
