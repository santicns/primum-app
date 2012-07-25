package com.primum.mobile.util;

import java.util.Locale;

import android.content.Context;
import android.content.res.Configuration;

public class LangUtils {
	
	public static void updateLanguage(Context context, String language) {
		if(language==null || language.equals("")) return;
		Locale locale = new Locale(language);
		updateLanguage(context,locale);
	}

	public static void updateLanguage(Context context, Locale locale) {
		Configuration config = new Configuration();
		config.locale = locale;
		context.getResources().updateConfiguration(config, null);
	}

}
