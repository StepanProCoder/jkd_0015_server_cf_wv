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
            if (savedResult.equals("error")) {
                // Открывайте MainActivity
                openMainActivity();
                Log.d("MAINACT",savedResult);
            } else {
                // Открывайте WebActivity с полученным сайтом
                openWebActivity(savedResult);
                Log.d("WEBACT",savedResult);
            }
            return;
        }

        // Проверка наличия интернета
        if (networkController.isInternetAvailable()) {
            // Обращение к Firebase и серверу
            firebaseController.initializeFirebase();
            firebaseController.getDomainFromFirestore();
        } else {
            openMainActivity();
        }
    }

    public void beginProcessing() {
        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            process();
        }
        else {
            ActivityResultLauncher<Intent> notificationPermissionLauncher = activity.registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                            process();
                        } else {
                            Toast.makeText(activity, "Дайте разрешение на уведомления для дальнейшей работы", Toast.LENGTH_LONG).show();
                        }
                    });
            Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, activity.getPackageName());
            notificationPermissionLauncher.launch(intent);
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
