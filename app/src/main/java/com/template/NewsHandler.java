package com.template;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NewsHandler implements Callback {

    private MainActivity activity;
    NewsHandler(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        if (response.isSuccessful()) {
            final ArrayList<String> newsList = new ArrayList<>();
            String html = response.body().string();

            try {
                Document document = Jsoup.parse(html);
                Elements newsElements = document.select(".short-text");

                for (Element element : newsElements) {
                    String newsTitle = element.text();
                    newsList.add(newsTitle);
                }

                activity.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        activity.newsAdapter.addAll(newsList);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                activity.controller.showToast("Failed to parse news");
            }

        } else {
            activity.controller.showToast("Failed to fetch news");
        }
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        e.printStackTrace();
        activity.controller.showToast("Failed to fetch news");
    }

}
