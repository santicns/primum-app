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
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.primum.mobile.R;
import com.primum.mobile.model.Patient;
import com.primum.mobile.rest.PatientRESTClient;
import com.primum.mobile.util.PrimumPrefs_;

@EActivity
public class PatientDataActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		setContentView(R.layout.patient_data_1);
	}

	@Click(R.id.btnGetData)
	void clickOnGet() {

		String patientId = txPatientId.getText().toString();
		if(patientId.equals("")){
			Toast.makeText(this, R.string.please_enter_a_valid_patient_id, Toast.LENGTH_LONG).show();
			return;
		}
		
		dialog = ProgressDialog.show(this, "", 
                getString(R.string.loading_please_wait), true);
		dialog.show();
		
		askForPatientData(txPatientId.getText().toString());
	}
	
	@Click(R.id.btnStartTest)
	void clickOnStartTest() {
		if(validateForm()){
			Patient currentPatient = new Patient();
			currentPatient.setPatientKey(txPatientId.getText().toString());
			currentPatient.setName(txName.getText().toString());
			currentPatient.setSurname1(txSurname1.getText().toString());
			currentPatient.setSurname2(txSurname2.getText().toString());
			
			ResultActivity_.intent(this)
				.currentPatient(currentPatient)
				.testKey(testKey)
				.start();
		}
	}
	
	@Click(R.id.btnCancel)
	void clickOnCancel() {
		MainActivity_.intent(this).start();
	}

	@Background
	void askForPatientData(String patientKey) {
		Patient p = null;
		try {
			PatientRESTClient pClient = new PatientRESTClient(primumPrefs);
			p = pClient.getPatient(primumPrefs.serviceUser().get() , patientKey);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		gotUserFromServer(p);
	}
	
	@UiThread
	void gotUserFromServer(Patient patient){
		dialog.cancel();
		if(patient==null || patient.getPatientId()==0){
			Toast.makeText(this, R.string.user_not_found_please_enter_data_manually, Toast.LENGTH_LONG).show();
			txName.setFocusableInTouchMode(true);
			txSurname1.setFocusableInTouchMode(true);
			txSurname2.setFocusableInTouchMode(true);
		}
		else{
			populateFileds(patient);
		}
		
	}
	
	private void populateFileds(Patient patient) {
		txName.setText(patient.getName());
		txSurname1.setText(patient.getSurname1());
		txSurname2.setText(patient.getSurname2());
	}

	private boolean validateForm() {
		return true;
	}

	@ViewById
	EditText txPatientId;
	@ViewById
	EditText txName;
	@ViewById
	EditText txSurname1;
	@ViewById
	EditText txSurname2;
	@Pref
	PrimumPrefs_ primumPrefs; 
	@Extra
	String testKey;
	private ProgressDialog dialog;
	private static String TAG = "PatientData1Activity";
}
