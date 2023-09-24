package com.template;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public ListView listViewNews;
    public ArrayAdapter<String> newsAdapter;
    public Handler handler;
    public MainActivityController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewNews = findViewById(R.id.listViewNews);
        newsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listViewNews.setAdapter(newsAdapter);
        handler = new Handler(Looper.getMainLooper());
        controller = new MainActivityController(this);

        // Вызываем метод для загрузки новостей
        controller.fetchNews();
    }
}
