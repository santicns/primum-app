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

import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
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

    @Click(R.id.testOxy)
	void clickOnOxy() {
    	doTest(Constants.TEST_KEY_OXIMETRY);
    }
    
    @Click(R.id.testPulse)
	void clickOnPulse() {
    	doTest(Constants.TEST_KEY_PULSE);
    }

    @Click(R.id.testWeight)
	void clickOnWeight() {
    	doTest(Constants.TEST_KEY_WEIGHT);
    }
    
	void doTest(String testType) {
		selectedTestType = testType;

		if (!PrefUtils.isUserSelected(primumPrefs)) {
			PatientDataActivity_.intent(this)
			.testKey(selectedTestType)
			.start();
		}
		else {
			displayConfirmDialog(selectedTestType);
		}
	}

    @Override
	public void onClick(DialogInterface dialog, int which) {
    	switch (which) {
            case AlertDialog.BUTTON1:
                Patient predfinedPatient = PrefUtils.getPredefinedPatient(primumPrefs);
                finish();

                ResultActivity_.intent(this)
                    .currentPatient(predfinedPatient)
                    .testKey(selectedTestType)
                    .start();

                break;

            case AlertDialog.BUTTON2:
                break;
		}
	}
    
	private void displayConfirmDialog(String selectedTestType) {
		String text = getString(R.string.perform_test, getTestNameString(selectedTestType), primumPrefs.patientId().get());
		new AlertDialog.Builder(this)
				.setTitle(R.string.confirm_user)
				.setMessage(text)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton(android.R.string.yes,this)
				.setNegativeButton(android.R.string.no, this)
				.show();
	}
	
	private String getTestNameString(String selectedTestType) {
		if (Constants.TEST_KEY_ELECTROCARDIOGRAM.equals(selectedTestType)) {
			return getString(R.string.ELECTROCARDIOGRAM);
		}

        if (Constants.TEST_KEY_OXIMETRY.equals(selectedTestType)) {
			return getString(R.string.OXIMETRY);
		} 

        if (Constants.TEST_KEY_WEIGHT.equals(selectedTestType)) {
			return getString(R.string.WEIGHT_TEST);
		}

        if (Constants.TEST_KEY_PULSE.equals(selectedTestType)) {
			return getString(R.string.PULSE_TEST);
		}

        return "";
		
	}

    @Pref
	PrimumPrefs_ primumPrefs;
    private String selectedTestType="";
    private static String TAG = "TestsActivity";
	
}

