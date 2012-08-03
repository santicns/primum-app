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


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.primum.mobile.R;
import com.primum.mobile.model.Patient;
import com.primum.mobile.rest.MedicalTestRESTClient;
import com.primum.mobile.rest.PatientRESTClient;
import com.primum.mobile.util.PrimumPrefs_;

@EActivity
public class ResultActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
        setContentView(R.layout.result);
        
        Log.d(TAG, "Results of test " + testKey + " for patient " + currentPatient.getPatientKey());
        
        dialog = ProgressDialog.show(this, "",
				getString(R.string.performing_test_please_wait, currentPatient.getPatientKey()), true);
		dialog.show();
        
        performTest(testKey);
    }
    
    @Override
	protected void onResume() {
		super.onResume();
		patientRestClient = new PatientRESTClient(primumPrefs);
		medicalTestRestClient = new MedicalTestRESTClient(primumPrefs);
	}

	@Background
	void performTest(String testKey) {
    	try {
			Thread.currentThread().sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		testFinished();
	}
    
    @UiThread
	void testFinished(){
		dialog.cancel();
		Toast.makeText(this, R.string.test_finished, Toast.LENGTH_LONG).show();
		
	}
    
    @Click(R.id.btnSave)
   	void clickOnSave() {
    	dialog = ProgressDialog.show(this, "",getString(R.string.saving_test_please_wait) , true);
		dialog.show();
   	}
   
    
    @Click(R.id.btnSubmit)
   	void clickOnSubmit() {
    	dialog = ProgressDialog.show(this, "",getString(R.string.submitting_test_please_wait) , true);
		dialog.show();
		submitTest(testKey);
    }
    
	    @Background
		void submitTest(String testKey) {
	    	Log.d(TAG, "Submitting test");
	    	Patient p = patientRestClient.getPatient(primumPrefs.serviceUser().get(), currentPatient.getPatientKey());
	    	if(p==null || p.getPatientId()==0){
	    		Log.d("TAG", "User does not exists in platform. Let's create it!");
	    		p=patientRestClient.addPatient(primumPrefs.serviceUser().get(),currentPatient.getPatientKey(), currentPatient.getName(), currentPatient.getSurname1(), currentPatient.getSurname2(), 0);
	    	}
	    	try {
				medicalTestRestClient.addMedicalTest(primumPrefs.serviceUser().get(), p.getPatientId(), testKey, readHL7());
				testSubmited(BACK_TO_UI_THREAD_CODE_OK);
			} catch (IOException e) {
				testSubmited(BACK_TO_UI_THREAD_CODE_ERROR);
			}
		}
	    
	    @UiThread
		void testSubmited(int errorCode){
			dialog.cancel();
			if(errorCode==0)Toast.makeText(this, R.string.test_correctly_saved, Toast.LENGTH_LONG).show();
			else Toast.makeText(this, "Unexpected error", Toast.LENGTH_LONG).show();
		}
    
    @Click(R.id.btnHome)
   	void clickOnHome() {
    	finish();
   		MainActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
   		
   	}
    
    private String readHL7() throws IOException{
    	InputStream is = getResources().getAssets().open("hl7.xml");
    	BufferedReader r = new BufferedReader(new InputStreamReader(is));
    	StringBuilder total = new StringBuilder();
    	String line;
    	while ((line = r.readLine()) != null) {
    	    total.append(line);
    	}
    	return total.toString();
    }
    
    
    PatientRESTClient patientRestClient;
    MedicalTestRESTClient medicalTestRestClient;
    @Extra
	Patient currentPatient;
    @Extra
	String testKey;
    @Pref
	PrimumPrefs_ primumPrefs;
    ProgressDialog dialog;
    static String TAG = "ResultActivity";
    
    private static final int BACK_TO_UI_THREAD_CODE_OK=0;
    private static final int BACK_TO_UI_THREAD_CODE_ERROR=-1;
}