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

package com.primum.mobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.primum.mobile.R;
import com.primum.mobile.util.Constants;
import com.primum.mobile.util.LangUtils;
import com.primum.mobile.util.PrefUtils;
import com.primum.mobile.util.PrimumPrefs_;

@EActivity
public class MainActivity extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (!PrefUtils.allPrefsSet(primumPrefs)) {
        	finish();
        	ConfigActivity_.intent(this).start();
        }

        setContentView(R.layout.main);
    }

	@Click(R.id.imgConfig)
	void clickOnConfig() {
		ConfigActivity_.intent(this).start();
	}
	
	@Click(R.id.imgNewTest)
	void clickOnNewTest() {
		TestsActivity_.intent(this).start();
	}

	@Override
	public void finishFromChild(Activity child) {
		Log.d(TAG, "finishFromChild");
		super.finishFromChild(child);
		Intent intent = getIntent();
		finish();
		startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity, menu);
	    return true;
	}

	@Pref
	PrimumPrefs_ primumPrefs; 
	private static String TAG = "MainActivity";

}
