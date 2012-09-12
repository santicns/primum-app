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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;
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
import com.primum.mobile.util.Constants;
import com.primum.mobile.util.MedicalTestUtils;
import com.primum.mobile.util.PrimumPrefs_;

@EActivity
public class ResultActivity extends Activity {

	Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
		Log.i(TAG, "onCreate");
        setContentView(R.layout.result);
        Log.d(TAG, "Results of test " + testKey + " for patient " + currentPatient.getPatientKey());
        device = DeviceFactory.getDevice(testKey, this);     
        createCancelProgressDialog("",getString(R.string.performing_test_please_wait, currentPatient.getPatientKey()), getString(R.string.cancel));	
        performTest(testKey);
       
    }
    private void createCancelProgressDialog(String title, String message, String buttonText)
    {
        dialog = new ProgressDialog(this);
        //dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE,buttonText, new DialogInterface.OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int which) 
            {
                // Use either finish() or return() to either close the activity or just the dialog
            	device.cancelTest();
            	finish();           	
                
            }
        });
        dialog.show();
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
	public
	void testFinished(String mode){
    	dialog.cancel();
    	if(mode.compareTo(Constants.CORRECTLY)==0 ){
    		
			device.printResult(this, R.id.resultLayout);
			Toast.makeText(this, R.string.test_finished_correctly, Toast.LENGTH_LONG).show();
    	}else if(mode.compareTo(Constants.TIME_OUT)==0 || mode.compareTo(Constants.REMOVED_FINGER)==0){
    		Toast.makeText(this, R.string.test_finished_error, Toast.LENGTH_LONG).show();
    		FailedTestActivity_.intent(this).start();	
    	} else if(mode.compareTo(Constants.FORCED_EXIT)==0){
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
    

    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity, menu);
	    return true;
	}
    
    public ProgressDialog getProgressDialog(){
    	return dialog;
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
    
    
    private static final int BACK_TO_UI_THREAD_CODE_OK=0;
    private static final int BACK_TO_UI_THREAD_CODE_ERROR=-1;
}