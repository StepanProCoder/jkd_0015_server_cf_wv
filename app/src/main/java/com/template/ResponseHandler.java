package com.template;

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
        SaveLoadResult.saveResult("Results", "result", "error", networkController.controller.activity);
        networkController.controller.activity.runOnUiThread(() -> networkController.controller.openMainActivity());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String responseString = response.body().string();
        SaveLoadResult.saveResult("Results", "result", responseString, networkController.controller.activity);

        if (responseString.equals("error")) {
            // Ошибка, открывайте MainActivity
            networkController.controller.activity.runOnUiThread(() -> networkController.controller.openMainActivity());
        } else {
            // Получен сайт, открывайте WebActivity
            networkController.controller.activity.runOnUiThread(() -> networkController.controller.openChromeTabs(responseString));
        }
    }

}
