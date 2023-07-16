package com.template;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveLoadResult {
    public static void saveResult(String result, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Results", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("result", result);
        editor.apply();
    }

    public static String loadResult(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Results", Context.MODE_PRIVATE);
        return sharedPreferences.getString("result", "");
    }


}
