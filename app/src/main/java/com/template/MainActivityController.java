package com.template;

import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivityController {

    public MainActivity activity;
    MainActivityController(MainActivity activity) {
        this.activity = activity;
    }

    public void fetchNews() {
        String newsUrl = activity.getString(R.string.news_source);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(newsUrl)
                .build();

        client.newCall(request).enqueue(new NewsHandler(activity));
    }

    public void showToast(final String message) {
        activity.handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
