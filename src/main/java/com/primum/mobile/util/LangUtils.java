/**
 * Copyright (c) 2012 Primum Health IT S.L. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.primum.mobile.util;

import java.util.Locale;

import android.content.Context;
import android.content.res.Configuration;

public class LangUtils {
	
	public static void updateLanguage(Context context, String language) {
		if (language==null || language.equals("")) {
            return;
        }

		Locale locale = new Locale(language);
		updateLanguage(context,locale);
	}

	public static void updateLanguage(Context context, Locale locale) {
		Configuration config = new Configuration();
		config.locale = locale;
		context.getResources().updateConfiguration(config, null);
	}

}
