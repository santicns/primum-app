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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.primum.mobile.R;
import com.primum.mobile.model.Patient;
import com.primum.mobile.util.Constants;
import com.primum.mobile.util.PrefUtils;
import com.primum.mobile.util.PrimumPrefs_;

@EActivity
public class TestsActivity extends Activity implements DialogInterface.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
        setContentView(R.layout.tests);
    }
    
    @Click(R.id.btnStart)
	void clickOnStart() {
    	int selected = rgTests.getCheckedRadioButtonId();
    	if(selected!=-1){
    		if(!PrefUtils.isUserSelected(primumPrefs)){
    			PatientDataActivity_.intent(this)
    			.testKey(getSelectedTest())
    			.start();
    		}
    		else{
    			displayConfirmDialog();
    		}
    	}
    	else{
    		Toast.makeText(this, R.string.select_one_test,  Toast.LENGTH_SHORT).show();
    	}
	}
    
    @Click(R.id.btnBack)
	void clickOnBack() {
    	finish();
	}
    
    @Override
	public void onClick(DialogInterface dialog, int which) {
    	switch (which) {
		case AlertDialog.BUTTON1:
			Patient predfinedPatient = PrefUtils.getPredefinedPatient(primumPrefs);
			ResultActivity_.intent(this)
				.currentPatient(predfinedPatient)
				.testKey(getSelectedTest())
				.start();	
			break;

		case AlertDialog.BUTTON2:
			//NOP
			break;
		}
	}
    
	private void displayConfirmDialog() {
		new AlertDialog.Builder(this)
				.setTitle("Confirm user")
				.setMessage("Perform test with patient " + primumPrefs.patientId().get() + "?")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton(android.R.string.yes,this)
				.setNegativeButton(android.R.string.no, this)
				.show();
	}
	
	private String getSelectedTest(){
		String selectedTestKey="";
		int selectedTest = rgTests.getCheckedRadioButtonId();
		switch (selectedTest) {
		case R.id.rbElectro:
			selectedTestKey=Constants.TEST_KEY_ELECTROCARDIOGRAM;
			break;
		case R.id.rbWeight:
			selectedTestKey=Constants.TEST_KEY_WEIGHT;
			break;
		case R.id.rbOximetry:
			selectedTestKey=Constants.TEST_KEY_OXIMETRY;
			break;
		case R.id.rbPulse:
			selectedTestKey=Constants.TEST_KEY_PULSE;
			break;
		default:
			break;
		}
		return selectedTestKey;
	}
    
    @ViewById
    RadioGroup rgTests;
    @Pref
	PrimumPrefs_ primumPrefs;
    private static String TAG = "TestsActivity";
	
}

