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

package com.primum.mobile.rest;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.primum.mobile.util.PrimumPrefs_;

public class MedicalTestRESTClient extends AbstractRESTClient{
	
	public MedicalTestRESTClient(PrimumPrefs_ primumPrefs) {
		super(primumPrefs);
	}
	
	public boolean addMedicalTest(String userScreenName, long patientId, String medicalTestKey, String body){
		String url = baseUrl + "/add-medical-test";
		Log.d(TAG,"URL " + url);
		
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
		parts.add("userScreenName", userScreenName);
		parts.add("patientId", String.valueOf(patientId));
		parts.add("medicalTestKey", medicalTestKey);
		parts.add("body", body);
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		String jsonObj = rt.postForObject(url, parts, String.class);
		Log.d(TAG,"jsonObj " + jsonObj);
		Boolean result = gsonBuilder.create().fromJson(jsonObj, Boolean.class); 
		return result.booleanValue();
	}

	@Override
	public String getServiceContext() {
		return "Primum-portlet";
	}

	@Override
	public String getModelName() {
		return "medicaltest";
	}
}
