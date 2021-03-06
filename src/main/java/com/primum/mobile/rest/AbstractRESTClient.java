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

import java.util.Date;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

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
		requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		rt = new RestTemplate(requestFactory);
		rt.getMessageConverters().add(new GsonHttpMessageConverter());
	}
	
	protected String addParamToRequestURL(String requestURL, String paramName, Object value) {
		String newRequestURL = requestURL;

		if (value==null || value.toString().equals("")) {
            return requestURL + "/-" + paramName;
        }

        if (value instanceof Date) {
            newRequestURL = newRequestURL + "/" + paramName + "/" + ((Date)value).getTime();
        }
        else {
            newRequestURL = newRequestURL + "/" + paramName + "/" + value.toString();
        }

		return newRequestURL;
	}

	public abstract String getServiceContext();

	public abstract String getModelName();

	PrimumPrefs_ primumPrefs;
	protected DefaultHttpClient httpClient;
	protected String baseUrl = "";
	protected String TAG = this.getClass().getName();
	protected HttpComponentsClientHttpRequestFactory requestFactory;
	protected RestTemplate rt;

}
