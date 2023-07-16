package com.template;

import android.webkit.WebSettings;
import android.webkit.WebViewClient;

public class WebActivityController {
    private WebActivity activity;

    public WebActivityController(WebActivity activity) {
        this.activity = activity;
    }

    public void process() {
        setupWebView();
        // Загрузка URL в WebView
        String url = activity.getIntent().getStringExtra("url");
        loadUrl(url);
    }

    private void setupWebView() {
        WebSettings webSettings = activity.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        activity.webView.setWebViewClient(new WebViewClient());
    }

    private void loadUrl(String url) {
        activity.webView.loadUrl(url);
    }
}
