package com.template;

import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class FirebaseController {

    public LoadingActivityController controller;
    public FirebaseRemoteConfig mFirebaseRemoteConfig;

    public FirebaseController(LoadingActivityController controller) {
        this.controller = controller;
    }

    public void initializeFirebase() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
    }

    public void getDomainFromFirestore() {
        // Однократный опрос Remote Config без асинхронности
        Log.d("FIRE", "HERE");
        mFirebaseRemoteConfig.fetch().addOnCompleteListener(new FirebaseHandler(this)).addOnFailureListener(task -> {Log.d("FIRE", "FAIL"); controller.openNoInternetActivity();});
    }

}
