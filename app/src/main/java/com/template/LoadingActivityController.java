package com.template;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;

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

        String savedResult = SaveLoadResult.loadResult("Results", "result", activity);

        if (!savedResult.isEmpty()) {
            // Проверка наличия интернета
            Log.d("INTERNET", networkController.isInternetAvailable()+"");
            if (networkController.isInternetAvailable()) {
                // Открывайте WebActivity с полученным сайтом
                openWebActivity(savedResult);
                Log.d("WEBACT",savedResult);
            } else {
                openNoInternetActivity();
            }
            return;
        }

        // Обращение к Firebase
        firebaseController.initializeFirebase();
        firebaseController.getDomainFromFirestore();

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

    public void openNoInternetActivity() {
        Intent intent = new Intent(activity, NoInternetActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

}
