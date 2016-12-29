package com.example.sara.uppgift4;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * An activity that present a browser window in the app.
 */
public class BrowserActivity extends AppCompatActivity {

    /**
     * Lifecycle onCreate as usual...
     * Makes the app present a full screen web page with the url passed in the intent staring this
     * activity
     * @param savedInstanceState not used.
     */
    @SuppressLint("SetJavaScriptEnabled") //this is a security risk that I'm willing to take!
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);


        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        } else {
            Log.e(Constants.LOG_TAG,"Critical error! No actionbar found!");
        }


        WebView wv = (WebView) findViewById(R.id.web_view);
        if (wv == null){
            Toast.makeText(this,"Could not find web view",Toast.LENGTH_LONG).show();
        } else {

            wv.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });

            WebSettings ws = wv.getSettings();
            ws.setJavaScriptEnabled(true);

            Bundle b = getIntent().getExtras();
            LinkInfo item = (LinkInfo) b.getSerializable(Constants.LINK_INFO_ITEM);

            if (item != null){
                wv.loadUrl(item.getUrl());
            } else {
                Toast.makeText(BrowserActivity.this, "Item is null", Toast.LENGTH_SHORT).show();
            }

        }

    }

}
