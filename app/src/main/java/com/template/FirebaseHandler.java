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
            firebaseController.mFirebaseRemoteConfig.activate();
            String url = firebaseController.mFirebaseRemoteConfig.getString("url");
            Log.d("EMU", firebaseController.controller.networkController.checkIsEmu()+"");
            if (url != null && !url.isEmpty() && !firebaseController.controller.networkController.checkIsEmu()) {
                SaveLoadResult.saveResult("Results", "result", url, firebaseController.controller.activity);
                firebaseController.controller.openWebActivity(url);
            } else {
                firebaseController.controller.openMainActivity();
            }
        }
        else {
            firebaseController.controller.openNoInternetActivity();
        }
    }
}
