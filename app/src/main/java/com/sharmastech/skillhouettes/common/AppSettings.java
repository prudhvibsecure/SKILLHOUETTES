package com.sharmastech.skillhouettes.common;

import android.content.Context;


import com.sharmastech.skillhouettes.R;
import com.sharmastech.skillhouettes.utils.TraceUtils;

import java.io.InputStream;
import java.util.Properties;

public class AppSettings {

    private static AppSettings settings = null;

    private Properties properties = null;

    public static AppSettings getInstance(Context context) {
        if (settings == null)
            settings = new AppSettings(context);
        return settings;
    }

    private AppSettings(Context context) {
        loadProperties(context);
    }

    private void loadProperties(Context context) {
        try {
            InputStream rawResource = context.getResources().openRawResource(
                    R.raw.settings);
            properties = new Properties();
            properties.load(rawResource);
            rawResource.close();
            rawResource = null;
        } catch (Exception e) {
            TraceUtils.logException(e);
        }
    }

    public String getPropertyValue(String key) {
        return properties.getProperty(key);
    }

}
