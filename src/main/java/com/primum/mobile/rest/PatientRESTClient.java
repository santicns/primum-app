package com.primum.mobile.rest;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.googlecode.androidannotations.annotations.EBean;
import com.primum.mobile.model.Patient;
import com.primum.mobile.util.PrimumPrefs_;

public class PatientRESTClient extends AbstractRESTClient{
	
	public PatientRESTClient(PrimumPrefs_ primumPrefs) {
		super(primumPrefs);
	}

	public Patient getPatient(String deviceId, String patientKey){
		String url = baseUrl + "/get-patient";
		
		url=addParamToRequestURL(url,"userScreenName", deviceId);
		url=addParamToRequestURL(url,"patientKey", patientKey);
		Log.d(TAG,"URL " + url);
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		String jsonObj = rt.getForObject(url, String.class);
		return gsonBuilder.create().fromJson(jsonObj, Patient.class);
	}
	
	public Patient addPatient(Patient patient){
		String url = baseUrl + "/add-patient";
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		String jsonObj = rt.postForObject(url, patient, String.class);
		return gsonBuilder.create().fromJson(jsonObj, Patient.class);
	}
	
	
	public Patient addPatient(String userScreenName, String patientKey, String name, String firstSurname, String secondSurname, String birthDate){
		String url = baseUrl + "/add-patient";
		
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
		parts.add("userScreenName", userScreenName);
		parts.add("patientKey", patientKey);
		parts.add("name", name);
		parts.add("firstSurname", firstSurname);
		parts.add("secondSurname", secondSurname);
		parts.add("birthDate", "0");
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		String jsonObj = rt.postForObject(url, parts, String.class);
		Log.d(TAG,"jsonObj " + jsonObj);
		return gsonBuilder.create().fromJson(jsonObj, Patient.class);
	}
	
	@Override
	public String getServiceContext() {
		return "Primum-portlet";
	}

	@Override
	public String getModelName() {
		return "patient";
	}
}
