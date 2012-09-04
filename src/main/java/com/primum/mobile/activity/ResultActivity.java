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
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.primum.mobile.R;
import com.primum.mobile.device.DeviceFactory;
import com.primum.mobile.device.GenericDevice;
import com.primum.mobile.exception.TestResultException;
import com.primum.mobile.model.Patient;
import com.primum.mobile.persistence.MedicalTestDBManager;
import com.primum.mobile.rest.MedicalTestRESTClient;
import com.primum.mobile.rest.PatientRESTClient;
import com.primum.mobile.util.ConnectionUtils;
import com.primum.mobile.util.MedicalTestUtils;
import com.primum.mobile.util.PrimumPrefs_;

@EActivity
public class ResultActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
        setContentView(R.layout.result);
        
        Log.d(TAG, "Results of test " + testKey + " for patient " + currentPatient.getPatientKey());
        device = DeviceFactory.getDevice(testKey);
        
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
		medicalTestDBManager = new MedicalTestDBManager(this);
	}

	@Background
	void performTest(String testKey) {
    	try {
    		device.performTest();
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
		device.printResult(this, R.id.resultLayout);
		Toast.makeText(this, R.string.test_finished, Toast.LENGTH_LONG).show();
		
	}
    
    @Click(R.id.btnCancel)
   	void clickOnCancel() {
    	finish();
   		MainActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
   	}
    
    @Click(R.id.btnSubmit)
   	void clickOnSubmit() {
    	if(!ConnectionUtils.isOnline(this)){
			Toast.makeText(this, R.string.network_connection_not_available_test_has_been_saved_locally, Toast.LENGTH_LONG).show();
			
			Log.d(TAG, "Test will be saved locally");
			try {
				medicalTestDBManager.addMedicalTest(currentPatient.getPatientKey(), testKey, device.getHL7Message());
			} catch (TestResultException e) {
				Log.e(TAG, "Error reading HL7. Device may not have been initialized");
			}
			
			finish();
		}
    	else{
    		dialog = ProgressDialog.show(this, "",getString(R.string.submitting_test_please_wait) , true);
    		dialog.show();
    		submitTest(testKey);
    	}
    }
    
	    @Background
		void submitTest(String testKey) {
	    	try {
				
		    	boolean success = true;
		    	Log.d(TAG, "Submitting test");
		    	Patient p = patientRestClient.getPatient(primumPrefs.serviceUser().get(), currentPatient.getPatientKey());
		    	if(p==null || p.getPatientId()==0){
		    		Log.d("TAG", "User does not exists in platform. Let's create it!");
		    		p=patientRestClient.addPatient(primumPrefs.serviceUser().get(),currentPatient.getPatientKey(), currentPatient.getName(), currentPatient.getSurname1(), currentPatient.getSurname2(), 0);
		    	}
		    	
	    		if(medicalTestRestClient.addMedicalTest(p.getPatientId(), testKey, device.getHL7Message())){
	    			testSubmited(BACK_TO_UI_THREAD_CODE_OK);
	    		}
	    		else{
	    			testSubmited(BACK_TO_UI_THREAD_CODE_ERROR);
	    			success = false;
	    		}
			
	    		if(success){
	    			MedicalTestUtils.submitStoredMedicalTests(medicalTestDBManager, medicalTestRestClient, p);
	    		}
	    		else{
	    			medicalTestDBManager.addMedicalTest(currentPatient.getPatientKey(), testKey, device.getHL7Message());
	    		}
    		
	    	} catch (TestResultException e) {
	    		Log.e(TAG, "Error reading HL7. Device may not have been initialized");
			}
	  
		}
	    

		@UiThread
		void testSubmited(int errorCode){
			dialog.cancel();
			if(errorCode==0)Toast.makeText(this, R.string.test_correctly_saved, Toast.LENGTH_LONG).show();
			else Toast.makeText(this, R.string.unexpected_error, Toast.LENGTH_LONG).show();
			finish();
		}
    

    
    /*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity, menu);
	    return true;
	}*/
    
    
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
    
    private static final int BACK_TO_UI_THREAD_CODE_OK=0;
    private static final int BACK_TO_UI_THREAD_CODE_ERROR=-1;
}