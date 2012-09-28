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

package com.primum.mobile.util;

import java.util.List;

import android.util.Log;

import com.primum.mobile.model.MedicalTest;
import com.primum.mobile.model.Patient;
import com.primum.mobile.persistence.MedicalTestDBManager;
import com.primum.mobile.rest.MedicalTestRESTClient;

public class MedicalTestUtils {

	public static void submitStoredMedicalTests(MedicalTestDBManager medicalTestDBManager, MedicalTestRESTClient medicalTestRestClient, Patient patient) {
		List<MedicalTest> medicalTests = medicalTestDBManager.getPatientTests(patient.getPatientKey());

		if (medicalTests!=null) {
			Log.d(TAG, "Medical tests stored: " + medicalTests.size());

            for (MedicalTest medicalTest : medicalTests) {
				Log.d(TAG, "Submitting stored medical test " + medicalTest.getMedicalTestId());

                medicalTestRestClient.addMedicalTest(patient.getPatientId(), medicalTest.getMedicalTestKey(), medicalTest.getBody());
				medicalTestDBManager.deleteMedicalTest(medicalTest);
			}
		}
	}
	
	static String TAG = "MedicalTestUtils";

}
