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
	
	public boolean addMedicalTest(String userScreenName, String patientKey, String medicalTestKey, String body){
		String url = baseUrl + "/add-medical-test";
		
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
		parts.add("userScreenName", userScreenName);
		parts.add("patientKey", patientKey);
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
