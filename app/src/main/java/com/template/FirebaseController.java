package com.template;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseController {

    public LoadingActivityController controller;

    public FirebaseController(LoadingActivityController controller) {
        this.controller = controller;
    }

    public void initializeFirebase() {
        // Инициализация Firebase Analytics
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(controller.activity);
        // Инициализация Firebase Cloud Messaging и других сервисов, если необходимо
    }

    public void getDomainFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("database").document("check");

        docRef.get().addOnCompleteListener(new FirebaseHandler(this));
    }

}
