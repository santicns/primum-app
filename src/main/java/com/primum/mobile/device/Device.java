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

import com.primum.mobile.exception.TestResultException;

import android.app.Activity;

public interface Device {
	
	//Perform test and stores them in local variables
	public void performTest();
	
	public void cancelTest();
	
	public void testFinished(String result);
	
	//Based on local variables constructs HL7 message
    public String getHL7Message()throws TestResultException ;
    
    
    public void printResult(Activity activity, int destLayout);

    
    public static final String TEST_RESULT_TIME_OUT			= "TEST_RESULT_TIME_OUT";
    public static final String TEST_RESULT_OK				= "TEST_RESULT_OK";
    public static final String TEST_RESULT_REMOVED_FINGER	= "TEST_RESULT_REMOVED_FINGER";
    public static final String TEST_RESULT_FORCED_EXIT 		= "TEST_RESULT_FORCED_EXIT";


}
