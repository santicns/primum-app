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

import android.app.TabActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TabHost;

import com.googlecode.androidannotations.annotations.EActivity;
import com.primum.mobile.R;

@EActivity
public class ConfigActivity extends TabActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config);
		
		Resources res = getResources();
		tabHost = getTabHost();
	    TabHost.TabSpec spec;
	    
	    spec = tabHost.newTabSpec("device").setIndicator(getString(R.string.device),
                res.getDrawable(android.R.drawable.ic_menu_manage))
            .setContent(DeviceConfigActivity_.intent(this). get());
		tabHost.addTab(spec);
		
		
		spec = tabHost.newTabSpec("user").setIndicator(getString(R.string.patient),
                res.getDrawable(android.R.drawable.ic_menu_set_as))
            .setContent(PatientConfigActivity_.intent(this). get());
		tabHost.addTab(spec);
		
		tabHost.setCurrentTab(0);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity, menu);
	    return true;
	}

	TabHost tabHost;
	static String TAG = "ConfigActivity";
}
