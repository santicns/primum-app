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
	
	protected String addParamToRequestURL(String requestURL, String paramName, Object value){
		String newRequestURL = requestURL;
		if(value==null || value.toString().equals(""))  return requestURL + "/-" + paramName;
		else{
			if(value instanceof Date) {
				newRequestURL = newRequestURL + "/" + paramName + "/" + ((Date)value).getTime();
			}
			else  {
				newRequestURL = newRequestURL + "/" + paramName + "/" + value.toString();
			} 
		}
		return newRequestURL;
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
