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

import android.util.Log;

import com.primum.mobile.model.Patient;

public class PrefUtils {

	public static boolean allPrefsSet(PrimumPrefs_ primumPrefs) {
		return allPrefsExist(primumPrefs) && noneBlank(primumPrefs);
	}

	public static boolean allPrefsExist(PrimumPrefs_ primumPrefs) {
		return primumPrefs.serviceUrl().exists()
            && primumPrefs.serviceUser().exists()
            && primumPrefs.servicepass().exists();
	}

	public static boolean noneBlank(PrimumPrefs_ primumPrefs) {
		return !primumPrefs.serviceUrl().get().equals("")
            && !primumPrefs.serviceUser().get().equals("")
            && !primumPrefs.servicepass().get().equals("");
	}
	
	public static boolean isUserSelected(PrimumPrefs_ primumPrefs) {
		return !primumPrefs.patientId().get().equals("");
	}
	
	public static Patient getPredefinedPatient(PrimumPrefs_ primumPrefs) {
		if (primumPrefs.patientId()!=null && !primumPrefs.patientId().equals("")) {
			Patient currentPatient = new Patient();
			currentPatient.setPatientKey(primumPrefs.patientId().get());
			currentPatient.setName(primumPrefs.patientName().get());
			currentPatient.setSurname1(primumPrefs.patientSurname1().get());
			currentPatient.setSurname2(primumPrefs.patientSurname2().get());
			return currentPatient;
		}
		else {
			Log.d(TAG, "No predefined patient");
			return null;
		}
	}
	
	static String TAG = "ResultActivity";

}
