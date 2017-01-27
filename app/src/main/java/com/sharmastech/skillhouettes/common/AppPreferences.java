package com.sharmastech.skillhouettes.common;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {

    private SharedPreferences pref = null;

    private static AppPreferences appPref = null;

    public static AppPreferences getInstance(Context context) {
        if (appPref == null)
            appPref = new AppPreferences(context);
        return appPref;
    }

    private AppPreferences(Context context) {
        if (pref == null)
            pref = context.getSharedPreferences("Skillhouettes", 0);
    }

    public void addToStore(String key, String value, boolean mode) {

        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        if(mode) {
            editor.apply();
            return;
        }
        editor.commit();
    }

    public String getFromStore(String key) {
        return pref.getString(key, "");
    }

    public void addToStoreinteger(String key, int value, boolean mode) {

        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        if(mode) {
            editor.apply();
            return;
        }
        editor.commit();
    }

    public int getFromStoreInt(String key) {
        return pref.getInt(key, 0);
    }

    public void clearSharedPreferences() {

        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();

    }

    public void clearSharedPreferences(String key) {

        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();

    }

}
