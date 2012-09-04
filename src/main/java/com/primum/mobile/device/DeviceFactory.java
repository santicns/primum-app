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

import com.primum.mobile.util.Constants;

public class DeviceFactory {

	public static GenericDevice getDevice(String device_type){

		if(Constants.TEST_KEY_ELECTROCARDIOGRAM.equals(device_type)){
			return new ElectrocardiogramDevice();
		}
		else if(Constants.TEST_KEY_OXIMETRY.equals(device_type)){
			return new OximetryDevice();
		} 
		else if(Constants.TEST_KEY_WEIGHT.equals(device_type)){
			return new WeightDevice();
		}
		else if(Constants.TEST_KEY_PULSE.equals(device_type)){
			return new PulseDevice();
		}
		else{
			return null;
		}
	}
	
	
}
