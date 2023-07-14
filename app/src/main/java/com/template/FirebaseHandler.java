package com.template;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class FirebaseHandler implements OnCompleteListener {

    private FirebaseController firebaseController;

    public FirebaseHandler(FirebaseController firebaseController) {
        this.firebaseController = firebaseController;
    }

    @Override
    public void onComplete(@NonNull Task task) {
        if (task.isSuccessful()) {
            DocumentSnapshot document = (DocumentSnapshot) task.getResult();
            if (document.exists()) {
                String link = document.getString("link");
                Log.d("LINK", link);
                if (link != null && !link.isEmpty()) {
                    // В link есть домен сайта, переходите к следующему шагу
                    // Формирование ссылки и обращение к серверу
                    String serverUrl = firebaseController.controller.networkController.formServerUrl(link);
                    Log.d("URL", serverUrl);
                    firebaseController.controller.networkController.makeServerRequest(serverUrl);
                } else {
                    // link пустой, открывайте MainActivity
                    firebaseController.controller.openMainActivity();
                }
            } else {
                // Ошибка получения документа, открывайте MainActivity
                firebaseController.controller.openMainActivity();
            }
        } else {
            // Ошибка выполнения запроса, открывайте MainActivity
            firebaseController.controller.openMainActivity();
        }
    }
}
