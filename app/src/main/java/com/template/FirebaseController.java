package com.template;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class FirebaseController {

    public LoadingActivityController controller;

    public FirebaseController(LoadingActivityController controller) {
        this.controller = controller;
    }

    public void initializeFirebase() {
        // Инициализация Firebase Analytics
        FirebaseAnalytics.getInstance(controller.activity);
        // Инициализация Firebase Cloud Messaging и других сервисов, если необходимо
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
    }

    public void getDomainFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("database").document("check");

        docRef.get().addOnCompleteListener(new FirebaseHandler(this));
    }

}
