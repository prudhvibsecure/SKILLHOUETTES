package com.sharmastech.skillhouettes.imageloader;

import android.content.Context;

import java.io.File;

public class FileCache {

	private File cacheDir;
	private String PATH =  "";
	
	public String CACHEPATH = "";

	public FileCache(Context context, boolean save) {
		
		PATH =  context.getExternalFilesDir(null).toString();
		
		CACHEPATH = PATH + "/Skillhouettes/cache/images/";
		
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {

			cacheDir = new File(CACHEPATH);
		} else
			cacheDir = context.getCacheDir();

		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}

	public File getFile(String url) {

		String filename = String.valueOf(url.hashCode());
		// String filename = URLEncoder.encode(url);
		return new File(cacheDir, filename);
	}

	public void clear() {
		File[] files = cacheDir.listFiles();
		if (files == null)
			return;
		for (File f : files)
			f.delete();
	}

}