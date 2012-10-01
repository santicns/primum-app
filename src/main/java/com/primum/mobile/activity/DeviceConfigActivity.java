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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.primum.mobile.R;
import com.primum.mobile.util.Constants;
import com.primum.mobile.util.LangUtils;
import com.primum.mobile.util.PrefUtils;
import com.primum.mobile.util.PrimumPrefs_;

@EActivity
public class DeviceConfigActivity extends Activity implements OnItemSelectedListener{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_config);
		populateLayout();
	}
	
	@Click(R.id.btnSave)
	void clickOnSave() {
		primumPrefs.edit()
			.serviceUrl().put(txServiceUrl.getText().toString())
			.serviceUser().put(txServiceUser.getText().toString())
			.servicepass().put(txServicePass.getText().toString())
			.apply();
			
		if (!PrefUtils.allPrefsSet(primumPrefs)) {
			Toast.makeText(this, R.string.not_all_preferences_set, Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(this, R.string.preferences_saved, Toast.LENGTH_SHORT).show();
		}
	}
	
	@Click(R.id.btnCancel)
	void clickOnCancel() {
		if (!PrefUtils.allPrefsSet(primumPrefs)) {
			Toast.makeText(this, getString(R.string.not_all_preferences_set), Toast.LENGTH_SHORT).show();
		}
		else {
			getParent().finish();
			finish();
		}
	}
	
	private void populateLayout() {
		txServiceUrl.setText(primumPrefs.serviceUrl().getOr(Constants.DEFAULT_SERVICE_URL));
		txServiceUser.setText(primumPrefs.serviceUser().get());
		txServicePass.setText(primumPrefs.servicepass().get());

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.languages, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spLanguage.setAdapter(adapter);

        for(int i=0; i<Constants.PREFS_LANG.length; i++) {
			if (primumPrefs.deviceLang().get().equals(Constants.PREFS_LANG[i])) {
				Log.d(TAG,"Constants.PREFS_LANG[i] " + Constants.PREFS_LANG[i]);
				spLanguage.setSelection(i);
				break;
			}
		}

		spLanguage.setOnItemSelectedListener(this);
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
		if (!primumPrefs.deviceLang().get().equals(Constants.PREFS_LANG[pos])) {
			primumPrefs.deviceLang().put(Constants.PREFS_LANG[pos]);
			LangUtils.updateLanguage(this, primumPrefs.deviceLang().get());
			reload();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}
	
	public void reload() {
		Intent intent = ConfigActivity_.intent(this).get();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0, 0);
		startActivity(intent);
	}

	@ViewById
	EditText txServiceUrl;
	@ViewById
	EditText txServiceUser;
	@ViewById
	EditText txServicePass;
	@ViewById
	Spinner spLanguage;

	@Pref
	PrimumPrefs_ primumPrefs; 
	private static String TAG = "ConfigActivity";
	
}
