package com.sharmastech.skillhouettes.common;

import android.content.Context;
import android.os.Environment;

public class Constants {

    public static final int CONTENT_INSTALL = 601;

    private static String PATH = Environment.getExternalStorageDirectory().toString();

    public static final String DWLPATH = PATH + "/Skillhouettes/downloads/";

    public static String getCacheTempPath(Context context) {
        return context.getExternalFilesDir(null) + "/Skillhouettes/cache/temp/";
    }

    public static String getCacheDataPath(Context context) {
        return context.getExternalFilesDir(null) + "/Skillhouettes/cache/data/";
    }

    public static String getCacheImagePath(Context context) {
        return context.getExternalFilesDir(null) + "/Skillhouettes/cache/images/";
    }

}
