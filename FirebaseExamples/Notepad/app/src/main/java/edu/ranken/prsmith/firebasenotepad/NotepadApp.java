package edu.ranken.prsmith.firebasenotepad;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class NotepadApp extends Application {
    private static final String LOG_TAG = NotepadApp.class.getSimpleName();
    private static final String PREFERENCES_FILENAME = "preferences";
    private static final String PREFERENCE_UID = "userId";

    private SharedPreferences sharedPref;
    private String userId;

    @Override
    public void onCreate() {
        super.onCreate();

        // force android to apply the light theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // load preferences
        sharedPref = getSharedPreferences(PREFERENCES_FILENAME, MODE_PRIVATE);
        userId = sharedPref.getString(PREFERENCE_UID, null);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;

        sharedPref.edit()
            .putString(PREFERENCE_UID, userId)
            .apply();
    }
}
