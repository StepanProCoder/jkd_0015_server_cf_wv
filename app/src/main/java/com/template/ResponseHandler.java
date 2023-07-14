package com.template;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ResponseHandler implements Callback {

    private NetworkController networkController;

    public ResponseHandler(NetworkController networkController) {
        this.networkController = networkController;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        // Обработка ошибки
        networkController.controller.activity.runOnUiThread(() -> networkController.controller.openMainActivity());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        //if (response.isSuccessful()) {
        String responseString = response.body().string();
        if (responseString.equals("error")) {
            // Ошибка, открывайте MainActivity
            Log.d("HERE", "HERE");
            networkController.controller.activity.runOnUiThread(() -> networkController.controller.openMainActivity());
        } else {
            // Получен сайт, открывайте WebActivity
            networkController.controller.activity.runOnUiThread(() -> networkController.controller.openWebActivity(responseString));
        }
//                } else {
//
//                    // Ошибка сервера, открывайте MainActivity
//                    runOnUiThread(() -> openMainActivity());
//                }
    }

}
