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

import android.app.Activity;


public class ElectrocardiogramDevice extends GenericDevice {

	public ElectrocardiogramDevice(){}
	
	@Override
	public void performTest() {
	}
	
	@Override
	public String getHL7Message() {
		return "";
	}

	@Override
	public void printResult(Activity activity, int destLayout) {
		// TODO Auto-generated method stub
		
	}

}