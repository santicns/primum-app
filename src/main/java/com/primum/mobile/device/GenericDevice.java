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

import android.app.Activity;

import com.primum.mobile.exception.TestResultException;

public abstract class GenericDevice {

	boolean testInitialized=false;
	private String DATE_FORMAT = "dd/MM/yyyy hh:mm";
	protected SimpleDateFormat sdf;
	protected int destLayout;
	
	public GenericDevice(){
		 sdf = new SimpleDateFormat(DATE_FORMAT);
	}
	
	public abstract void performTest();
	public abstract String getHL7Message()throws TestResultException;
	public abstract void printResult(Activity activity, int destLayout);
	
	public void checkTextInitialized() throws TestResultException{
		if(!testInitialized) throw new TestResultException();
	}
	
	protected String formatDate(Date date){
		return sdf.format(date);
	}
}
