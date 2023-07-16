package com.template;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.util.TimeZone;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class NetworkController {

    public LoadingActivityController controller;

    public NetworkController(LoadingActivityController controller) {
        this.controller = controller;
    }
    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) controller.activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
        } else {
            return false;
        }
    }

    public String formServerUrl(String link) {
        String domenFromFirebase = link; // Полученный домен из Firebase Cloud Firestore
        String packageName = controller.activity.getPackageName(); // Пакет приложения
        String userId = UUID.randomUUID().toString(); // Генерация UUID для пользователя
        String timeZone = TimeZone.getDefault().getID(); // Таймзона устройства

        String url = domenFromFirebase + "/?packageid=" + packageName +
                "&usserid=" + userId +
                "&getz=" + timeZone +
                "&getr=utm_source=google-play&utm_medium=organic";

        return url;
    }

    public String getUserAgent(Context context) {
        WebView webView = new WebView(context);
        WebSettings settings = webView.getSettings();
        return settings.getUserAgentString();
    }

    public void makeServerRequest(String serverUrl) {
        // Выполнение запроса к серверу и обработка ответа
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        Request request = new Request.Builder()
                .header("User-Agent", getUserAgent(controller.activity))
                .url(serverUrl)
                .build();

        String userAgent = request.header("User-Agent");
        if (userAgent != null) {
            Log.d("User-Agent", userAgent);
        } else {
            Log.d("User-Agent", "User-Agent не указан");
        }

        Log.d("FULL", request.toString());

        client.newCall(request).enqueue(new ResponseHandler(this));
    }

}
