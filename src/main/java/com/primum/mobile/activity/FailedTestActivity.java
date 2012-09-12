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
import android.app.ProgressDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;



import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;

import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.primum.mobile.R;

import com.primum.mobile.device.GenericDevice;

import com.primum.mobile.model.Patient;
import com.primum.mobile.persistence.MedicalTestDBManager;
import com.primum.mobile.rest.MedicalTestRESTClient;
import com.primum.mobile.rest.PatientRESTClient;

import com.primum.mobile.util.Constants;
import com.primum.mobile.util.PrefUtils;
import com.primum.mobile.util.PrimumPrefs_;

@EActivity
public class FailedTestActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.failed_result);
		
    }
    
    @Override
	protected void onResume() {
		super.onResume();
		patientRestClient = new PatientRESTClient(primumPrefs);
		medicalTestRestClient = new MedicalTestRESTClient(primumPrefs);
		medicalTestDBManager = new MedicalTestDBManager(this);
	} 
   
    @Click(R.id.btnCancel)
   	void clickOnCancel() {
    	finish();
   		MainActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
   	} 
    @Click(R.id.btnRetry)
   	void clickOnRetry() {
    	
		if(!PrefUtils.isUserSelected(primumPrefs)){
			PatientDataActivity_.intent(this)
			.testKey(Constants.TEST_KEY_OXIMETRY)
			.start();
		}
		else{
			displayConfirmDialog(Constants.TEST_KEY_OXIMETRY);
			ResultActivity_.intent(this)
			.currentPatient(currentPatient)
			.testKey(testKey)
			.start();
		}

   	}
    private void displayConfirmDialog(String selectedTestType) {
		String text = getString(R.string.perform_test, getTestNameString(selectedTestType), primumPrefs.patientId().get());
		new AlertDialog.Builder(this)
				.setTitle(R.string.confirm_user)
				.setMessage(text)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton(android.R.string.yes,(OnClickListener) this)
				.setNegativeButton(android.R.string.no, (OnClickListener) this)
				.show();
	}
    private String getTestNameString(String selectedTestType){
		if(Constants.TEST_KEY_ELECTROCARDIOGRAM.equals(selectedTestType)){
			return getString(R.string.ELECTROCARDIOGRAM);
		}
		else if(Constants.TEST_KEY_OXIMETRY.equals(selectedTestType)){
			return getString(R.string.OXIMETRY);
		} 
		else if(Constants.TEST_KEY_WEIGHT.equals(selectedTestType)){
			return getString(R.string.WEIGHT_TEST);
		}
		else if(Constants.TEST_KEY_PULSE.equals(selectedTestType)){
			return getString(R.string.PULSE_TEST);
		}
		else return "";
		
	}

	

    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity, menu);
	    return true;
	}
    
    
    
    PatientRESTClient patientRestClient;
    MedicalTestRESTClient medicalTestRestClient;
    MedicalTestDBManager medicalTestDBManager;
    GenericDevice device;
    @Extra
	Patient currentPatient;
    @Extra
	String testKey;
    @Pref
	PrimumPrefs_ primumPrefs;
    ProgressDialog dialog;
    static String TAG = "ResultActivity";
    
    
 
}