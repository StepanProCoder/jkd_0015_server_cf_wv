package com.template;

import android.content.Intent;

import com.google.firebase.FirebaseApp;

public class LoadingActivityController {

    public LoadingActivity activity;
    public FirebaseController firebaseController;
    public NetworkController networkController;

    public LoadingActivityController(LoadingActivity activity) {
        this.activity = activity;
        this.firebaseController = new FirebaseController(this);
        this.networkController = new NetworkController(this);
    }

    public void process() {
        // Инициализация Firebase SDK
        FirebaseApp.initializeApp(activity);

        // Проверка наличия интернета
        if (networkController.isInternetAvailable()) {
            // Обращение к Firebase и серверу
            firebaseController.initializeFirebase();
            firebaseController.getDomainFromFirestore();
        } else {
            openMainActivity();
        }
    }

    public void openWebActivity(String url) {
        Intent intent = new Intent(activity, WebActivity.class);
        intent.putExtra("url", url);
        activity.startActivity(intent);
        activity.finish();
    }

    public void openMainActivity() {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

}
