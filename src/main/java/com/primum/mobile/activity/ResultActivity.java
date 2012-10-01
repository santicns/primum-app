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
import android.content.Context;
import android.content.DialogInterface;
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
import com.primum.mobile.device.Device;
import com.primum.mobile.device.DeviceFactory;
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

	Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
		Log.i(TAG, "onCreate");
        setContentView(R.layout.result);
        
        Log.d(TAG, "Results of test " + testKey + " for patient " + currentPatient.getPatientKey());
        device = DeviceFactory.getDevice(testKey, this);
        createCancelProgressDialog("",getString(R.string.performing_test_please_wait, currentPatient.getPatientKey()));	
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
		device.performTest();
	}
    
    @UiThread
	public void testFinished(String mode) {
    	dialog.cancel();
    	
    	if (mode.compareTo(Device.TEST_RESULT_OK)==0 ) {
    		
			device.printResult(this, R.id.resultLayout);
			Toast.makeText(this, R.string.test_finished_correctly, Toast.LENGTH_LONG).show();
    	}
        else if  (mode.compareTo(Device.TEST_RESULT_TIME_OUT)==0 || mode.compareTo(Device.TEST_RESULT_REMOVED_FINGER)==0) {
    		createRetryDialog(getString(R.string.unexpected_error_happened),getString(R.string.test_finished_error_retry));
    	}
        else if (mode.compareTo(Device.TEST_RESULT_FORCED_EXIT)==0) {
    		this.finish();
    		MainActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
    	}
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
    

	private void createCancelProgressDialog(String title, String message) {
        dialog = new ProgressDialog(this);
        
        dialog.setCancelable(false);
        dialog.setMessage(message);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Use either finish() or return() to either close the activity or just the dialog
            	device.cancelTest();
            	finish();
            }
        });
        dialog.show();
    }
	
	private void createRetryDialog(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);
		
		builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	device.cancelTest();
            	alertDialog.dismiss();
            	finish();
            }
        });
		
		builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	createCancelProgressDialog("",getString(R.string.performing_test_please_wait, currentPatient.getPatientKey()));	
            	performTest(testKey);
            	alertDialog.dismiss();
            }
        });
		
		alertDialog = builder.create();
		alertDialog.show();
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity, menu);
	    return true;
	}
    
    public ProgressDialog getProgressDialog() {
    	return dialog;
    }
    
    PatientRESTClient patientRestClient;
    MedicalTestRESTClient medicalTestRestClient;
    MedicalTestDBManager medicalTestDBManager;
    Device device;
    @Extra
	Patient currentPatient;
    @Extra
	String testKey;
    @Pref
	PrimumPrefs_ primumPrefs;
    ProgressDialog dialog;
    AlertDialog alertDialog;
    static String TAG = "ResultActivity";
    
    private static final int BACK_TO_UI_THREAD_CODE_OK=0;
    private static final int BACK_TO_UI_THREAD_CODE_ERROR=-1;
}