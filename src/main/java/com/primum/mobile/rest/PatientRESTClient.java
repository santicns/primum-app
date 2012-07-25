package com.primum.mobile.rest;

import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.google.gson.GsonBuilder;
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
		String jsonObj = getForJSONObject(url);
		return gsonBuilder.create().fromJson(jsonObj, Patient.class);
	}
	
	


	@Override
	public String getServiceContext() {
		return "AdvancedPatients-portlet";
	}

	@Override
	public String getModelName() {
		return "patient";
	}
}
