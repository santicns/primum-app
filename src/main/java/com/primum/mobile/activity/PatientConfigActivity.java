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
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.primum.mobile.R;
import com.primum.mobile.model.Patient;
import com.primum.mobile.rest.PatientRESTClient;
import com.primum.mobile.util.ConnectionUtils;
import com.primum.mobile.util.PrimumPrefs_;

@EActivity
public class PatientConfigActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patient_config);
		populateFieldsFromPrefs();
	}
	
	private void populateFieldsFromPrefs() {
		txPatientId.setText(primumPrefs.patientId().get());
		txName.setText(primumPrefs.patientName().get());
		txSurname1.setText(primumPrefs.patientSurname1().get());
		txSurname2.setText(primumPrefs.patientSurname2().get());
	}

	@Click(R.id.btnGetData)
	void clickOnGet() {

		String patientId = txPatientId.getText().toString();
		if(!ConnectionUtils.isOnline(this)){
			Toast.makeText(this, R.string.network_connection_not_available_you_can_enter_data_manually, Toast.LENGTH_LONG).show();
			enableFields();
			return;
		}
		else if(patientId.equals("")){
			Toast.makeText(this, R.string.please_enter_a_valid_patient_id, Toast.LENGTH_LONG).show();
			return;
		} 
		
		dialog = ProgressDialog.show(this, "", 
                getString(R.string.loading_please_wait), true);
		dialog.show();
		
		askForPatientData(txPatientId.getText().toString());
	}
	
	@Click(R.id.btnSave)
	void clickOnSave() {
		primumPrefs.patientId().put(txPatientId.getText().toString());
		primumPrefs.patientName().put(txName.getText().toString());
		primumPrefs.patientSurname1().put(txSurname1.getText().toString());
		primumPrefs.patientSurname2().put(txSurname2.getText().toString());

		Toast.makeText(this, R.string.patient_fixed, Toast.LENGTH_SHORT)
				.show();
		
	}
	
	@Click(R.id.btnCancel)
	void clickOnCancel(){
		finish();
	}
	
	@Click(R.id.btnClear)
	void clickOnClear(){
		txPatientId.setText("");
		txName.setText("");
		txSurname1.setText("");
		txSurname2.setText("");
	}
	
	@Background
	void askForPatientData(String patientKey) {
		Patient p = null;
		try {
			PatientRESTClient pClient = new PatientRESTClient(primumPrefs);
			p = pClient.getPatient(primumPrefs.serviceUser().get() , patientKey);
			Log.d(TAG,  "p.getName() " + p.getName());
		} 
		catch (Exception e) {
			Log.e(TAG, "Error getting patient", e);
		}
		gotUserFromServer(p);
	}
	
	@UiThread
	void gotUserFromServer(Patient patient){
		dialog.cancel();
		if(patient==null || patient.getPatientId()==0){
			Toast.makeText(this, R.string.user_not_found_please_enter_data_manually, Toast.LENGTH_LONG).show();
			enableFields();
		}
		else{
			populateFiledsFromPatient(patient);
		}
	}
	
	private void populateFiledsFromPatient(Patient patient) {
		txPatientId.setText(patient.getPatientKey());
		txName.setText(patient.getName());
		txSurname1.setText(patient.getSurname1());
		txSurname2.setText(patient.getSurname2());
	}
	
	private void enableFields(){
		txName.setFocusableInTouchMode(true);
		txSurname1.setFocusableInTouchMode(true);
		txSurname2.setFocusableInTouchMode(true);
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
	private ProgressDialog dialog;
	private static String TAG = "PatientConfigActivity";
}
