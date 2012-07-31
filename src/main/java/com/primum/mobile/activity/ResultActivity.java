package com.primum.mobile.activity;

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
import com.primum.mobile.util.Constants;
import com.primum.mobile.util.PrimumPrefs_;

@EActivity
public class ResultActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
        setContentView(R.layout.result);
        
        dialog = ProgressDialog.show(this, "",
				getString(R.string.performing_test_please_wait, patientId), true);
		dialog.show();
        
        performTest(0);
    }
    
    @Override
	protected void onResume() {
		super.onResume();
		patientRestClient = new PatientRESTClient(primumPrefs);
		medicalTestRestClient = new MedicalTestRESTClient(primumPrefs);
	}

	@Background
	void performTest(long testId) {
    	try {
			Thread.currentThread().sleep(5000);
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
    
	    @Background
		void saveTest(long testId) {
	    	Patient p = patientRestClient.getPatient(primumPrefs.serviceUser().get(), patientId);
	    	if(p==null || p.getPatientKey().equals("")){
	    		patientRestClient.
	    	}
	    	//Guardamos test
	    	testSaved();
		}
	    
	    @UiThread
		void testSaved(){
			dialog.cancel();
			Toast.makeText(this, R.string.test_correctly_saved, Toast.LENGTH_LONG).show();
		}
    
    @Click(R.id.btnSubmit)
   	void clickOnSubmit() {
    	//TODO:Save test locally and send to server
    	Toast.makeText(this, R.string.test_submited_correctly, Toast.LENGTH_LONG).show();
   	}
    
    @Click(R.id.btnHome)
   	void clickOnHome() {
    	finish();
   		MainActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
   		
   	}
    
    
    PatientRESTClient patientRestClient;
    MedicalTestRESTClient medicalTestRestClient;
    @Extra(Constants.PARAM_PATIENT_ID)
	String patientId;
    @Pref
	PrimumPrefs_ primumPrefs;
    ProgressDialog dialog;
    static String TAG = "ResultActivity";
}

