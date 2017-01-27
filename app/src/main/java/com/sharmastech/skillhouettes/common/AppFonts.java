package com.sharmastech.skillhouettes.common;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

public class AppFonts {
	
	static AppFonts appFonts = null;
	
	private Context mContext = null;

	//private Typeface zawgyiweb = null;

	private Typeface zawgyi = null;
	private Typeface futuraStdBook = null;
	private Typeface opificioBoldWebfont = null;
	
	public static AppFonts getInstance(Context context) {
		if (appFonts == null)
			appFonts = new AppFonts(context);
		return appFonts;
	}
	
	public AppFonts(Context context) {
		this.mContext = context;	
		
		AssetManager assets  = mContext.getAssets();
		
		zawgyi = Typeface.createFromAsset(assets, "fonts/padauk.ttf");

		//zawgyiweb = Typeface.createFromAsset(assets, "fonts/ZawgyiOne20071204.ttf");

		futuraStdBook = Typeface.createFromAsset(assets, "fonts/Futura-Std-Book_19044.ttf");
		
		opificioBoldWebfont = Typeface.createFromAsset(assets, "fonts/opificioboldwebfont.ttf");

	}	
		
	public Typeface getFutureStdBookFont() {
		return AppPreferences.getInstance(mContext).getFromStore("language").equals("2") ? zawgyi : futuraStdBook;
	}
	
	public Typeface getOpificioBoldWebfont() {
		return AppPreferences.getInstance(mContext).getFromStore("language").equals("2") ? zawgyi : opificioBoldWebfont;
	}
	
	public Typeface getZawgyiFont() {
		return zawgyi;
	}

	/*public Typeface getFutureStdBookFontZawgyiWeb() {
		return AppPreferences.getInstance(mContext).getFromStore("language").equals("2") ? zawgyiweb : futuraStdBook;
	}*/
				
}
