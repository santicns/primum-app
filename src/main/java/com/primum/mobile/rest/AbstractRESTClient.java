package com.primum.mobile.rest;

import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.primum.mobile.model.Patient;
import com.primum.mobile.util.Constants;
import com.primum.mobile.util.PrimumPrefs_;

public abstract class AbstractRESTClient {

	public AbstractRESTClient(PrimumPrefs_ primumPrefs) {
		this.primumPrefs=primumPrefs;
		httpClient = new DefaultHttpClient();
		
		String serviceUrl = primumPrefs.serviceUrl().getOr(Constants.DEFAULT_SERVICE_URL);
		
		httpClient.getCredentialsProvider().setCredentials(
				new AuthScope(serviceUrl, 80),
				new UsernamePasswordCredentials(
						primumPrefs.serviceUser().get(), 
						primumPrefs.servicepass().get()));
	
		baseUrl = "http://" + serviceUrl  + "/" + getServiceContext() + "/api/secure/jsonws/" + getModelName();
	}
	
	public String getForJSONObject(String url){
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = httpClient.execute(httpGet);
			String result = EntityUtils.toString(response.getEntity());
			Log.d(TAG,"result " + result);
			return result;
		} catch (Exception e) {
			Log.e(TAG,"Error getting patient", e);
			return null;
		}
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

	public abstract String getServiceContext();

	public abstract String getModelName();

	PrimumPrefs_ primumPrefs;
	protected DefaultHttpClient httpClient;
	protected String baseUrl = "";
	protected String TAG = this.getClass().getName();
}
