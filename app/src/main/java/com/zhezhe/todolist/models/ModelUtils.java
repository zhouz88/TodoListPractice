package com.zhezhe.todolist.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public final class ModelUtils {
    private static final Gson gson = new Gson();

    /**
     * Save the object to sp
     *
     * @param context ActivityContext
     * @param key     the key in sp
     * @param object  the object to be saved
     */
    public static void save(Context context, String key, Object object) {
        SharedPreferences sharedPreferences = context.getApplicationContext()
                .getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonString = gson.toJson(object);
        editor.putString(key, jsonString).apply();
    }


    /**
     * Load the object from sp
     * @param context  ActivityContext
     * @param key the key in sp
     * @param token token to get the type
     * @param <T>
     * @return the Object
     */
    public static <T> T read(Context context, String key, TypeToken<T> token) {
        SharedPreferences sharedPreferences = context.getApplicationContext()
                .getSharedPreferences(key, Context.MODE_PRIVATE);
        try {
            return gson.fromJson(sharedPreferences.getString(key, ""), token.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
