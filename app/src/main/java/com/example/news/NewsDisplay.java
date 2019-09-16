package com.example.news;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by 123 on 2019/9/15.
 */

public class NewsDisplay extends AppCompatActivity {

    private String newsUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_news);
        newsUrl = getIntent().getStringExtra("news_url");
        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(newsUrl);
    }
}