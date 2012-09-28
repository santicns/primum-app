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

package com.primum.mobile.device;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;

import com.primum.mobile.activity.ResultActivity;
import com.primum.mobile.exception.TestResultException;

public abstract class BaseDevice implements Device {

	boolean testInitialized = false;

	private String DATE_FORMAT = "dd/MM/yyyy hh:mm";
	protected SimpleDateFormat sdf;
	protected Context context;

	public BaseDevice(Context context) {
		this.context=context;
		sdf = new SimpleDateFormat(DATE_FORMAT);
	}

	public abstract void performTest();
	
	public final void testFinished(String result){
		((ResultActivity) context).testFinished(result);
	}
	
	public abstract String getHL7Message() throws TestResultException;

	public abstract void printResult(Activity activity, int destLayout);

	protected void checkTextInitialized() throws TestResultException {
		if (!testInitialized)
			throw new TestResultException();
	}

	protected final String formatDate(Date date) {
		return sdf.format(date);
	}



}
