package com.sharmastech.skillhouettes.imageloader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ImageDatabase extends SQLiteOpenHelper {

	private final static String APP_DATABASE_NAME = "imagedatabase.db";
	private final static int APP_DATABASE_VERSION = 1;

	final String CREATE_TABLE = "CREATE TABLE IMAGES(IMAGEURL TEXT, TIMESTAMP TEXT);";

	public ImageDatabase(Context context) {
		super(context, APP_DATABASE_NAME, null, APP_DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
	}

}
