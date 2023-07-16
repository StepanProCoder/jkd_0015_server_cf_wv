package com.template;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivityController {
    private WebActivity activity;
    private String mainUrl;
    public WebActivityController(WebActivity activity) {
        this.activity = activity;
        String url = activity.getIntent().getStringExtra("url");
        this.mainUrl = url;
        Log.d("WEB", url);
    }

    public void process(Bundle savedInstanceState) {
        setupWebView();

        if (savedInstanceState != null) {
            activity.webView.restoreState(savedInstanceState);
        } else {
            loadUrl(mainUrl);
        }

    }


    private void setupWebView() {
        WebSettings webSettings = activity.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        // Включение поддержки работы с куками
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(activity.webView, true);
        }

        // Включение поддержки всплывающих окон (pop-up windows)
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);

        activity.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                // Загрузка сохраненных куков из SharedPreferences
                String cookies = SaveLoadResult.loadResult("Cookies", url, activity);

                // Установка куков в CookieManager
                CookieManager.getInstance().setCookie(url, cookies);
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                // Получение текущих куков из CookieManager
                String cookies = CookieManager.getInstance().getCookie(url);

                // Сохранение куков в SharedPreferences
                SaveLoadResult.saveResult("Cookies", url, cookies, activity);
            }
        });

    }


    private void loadUrl(String url) {
        activity.webView.loadUrl(url);
    }

}
