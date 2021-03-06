package com.MobiSeeker.PrescriptionWatcher.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {

    private Context context = null;

    public Settings(Context context) {
        this.context = context;
    }

    public String getUserName() {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.context);

        return sharedPrefs.getString("prefUsername", "Guest");
    }

    public boolean isVibrate() {
        return this.getBooleanValue("prefVibrate", false);
    }

    public boolean isWatchingPrescriptions() {
        return this.getBooleanValue("prefWatch", true);
    }

    private boolean getBooleanValue(String key, boolean defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.context);

        return sharedPrefs.getBoolean(key, defaultValue);
    }




}
