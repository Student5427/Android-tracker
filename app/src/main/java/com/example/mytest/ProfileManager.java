package com.example.mytest;

import android.content.Context;
import android.content.SharedPreferences;

public class ProfileManager {
    private static final String PREFS_NAME = "profile_prefs";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_BIRTH_DATE = "birth_date";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_WEIGHT = "weight";

    private static ProfileManager instance;
    private SharedPreferences sharedPreferences;

    private ProfileManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized ProfileManager getInstance(Context context) {
        if (instance == null) {
            instance = new ProfileManager(context);
        }
        return instance;
    }

    // Сохранение данных профиля
    public void saveProfile(Profile profile) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FIRST_NAME, profile.getFirstName());
        editor.putString(KEY_LAST_NAME, profile.getLastName());
        editor.putString(KEY_BIRTH_DATE, profile.getBirthDate());
        editor.putString(KEY_EMAIL, profile.getEmail());
        editor.putInt(KEY_HEIGHT, profile.getHeight());
        editor.putFloat(KEY_WEIGHT, profile.getWeight());
        editor.apply();
    }

    // Загрузка данных профиля
    public Profile loadProfile() {
        Profile profile = new Profile();
        profile.setFirstName(sharedPreferences.getString(KEY_FIRST_NAME, "Иван"));
        profile.setLastName(sharedPreferences.getString(KEY_LAST_NAME, "Иванов"));
        profile.setBirthDate(sharedPreferences.getString(KEY_BIRTH_DATE, "01.01.2000"));
        profile.setEmail(sharedPreferences.getString(KEY_EMAIL, ""));
        profile.setHeight(sharedPreferences.getInt(KEY_HEIGHT, 180));
        profile.setWeight(sharedPreferences.getFloat(KEY_WEIGHT, 70.0f));
        return profile;
    }

    // Получение веса для расчета калорий
    public float getUserWeight() {
        return sharedPreferences.getFloat(KEY_WEIGHT, 70.0f);
    }
}