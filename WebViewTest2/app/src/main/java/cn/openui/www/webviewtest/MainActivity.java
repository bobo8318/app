package cn.openui.www.webviewtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import model.WebViewJs;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView webiew = (WebView) this.findViewById(R.id.webview);

        webiew.getSettings().setJavaScriptEnabled(true);
        webiew.loadUrl("file:///android_asset/index.html");
        WebViewJs wj = new WebViewJs(this);
        webiew.addJavascriptInterface(wj,"js");

        webiew.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.indexOf("test")>0)
                    Toast.makeText(MainActivity.this,"test",Toast.LENGTH_LONG).show();

                view.loadUrl("file:///android_asset/index.html");
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        webiew.setWebChromeClient(new WebChromeClient(){

        });
    }
}
