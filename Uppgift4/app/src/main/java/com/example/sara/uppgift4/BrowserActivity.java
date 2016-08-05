package com.example.sara.uppgift4;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class BrowserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);


        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


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
