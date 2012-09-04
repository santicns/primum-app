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

import java.util.Date;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.primum.mobile.R;
import com.primum.mobile.exception.TestResultException;
import com.primum.mobile.util.HL7MessageGenerator;

public class OximetryDevice extends GenericDevice {

	private String pulsioximeterMacAddress="";
	private Date medicalTestTime;
	private String oxygenSaturation="";
	private Date oxygenSaturationTime;
	private String pulseRate="";
	private Date pulseRateTime;
	
	@Override
	public void performTest() {
		pulsioximeterMacAddress = "48:5d:60:95:dc:34";
		medicalTestTime = new Date();
		oxygenSaturation = String.valueOf(Math.round((97 + 2*Math.random())*10)/10.0d);
		oxygenSaturationTime = new Date();
		pulseRate = String.valueOf(Math.round(50 + 40*Math.random()));
		pulseRateTime = new Date();
		
		testInitialized=true;
	}

	@Override
	public String getHL7Message() throws TestResultException {
		checkTextInitialized();
		return HL7MessageGenerator.getPulsioximeterOBX(
				pulsioximeterMacAddress, 
				sdf.format(medicalTestTime),
				oxygenSaturation,
				sdf.format(oxygenSaturationTime),
				pulseRate,
				sdf.format(pulseRateTime));
	}
	
	@Override
	public void printResult(Activity activity, int destLayout) {
		LinearLayout resultLayout = (LinearLayout)activity.findViewById(destLayout);
		View result = activity.getLayoutInflater().inflate(R.layout.result_oximetry, null);
		resultLayout.addView(result);
		
		TextView tx = (TextView)activity.findViewById(R.id.txTime);
		tx.setText(formatDate(getMedicalTestTime()));
		
		tx = (TextView)activity.findViewById(R.id.txOxySaturation);
		tx.setText(getOxygenSaturation() + "%");
		
		tx = (TextView)activity.findViewById(R.id.txPulseRate);
		tx.setText(getPulseRate() + " BPM");
	}

	public String getPulsioximeterMacAddress() {
		return pulsioximeterMacAddress;
	}

	public Date getMedicalTestTime() {
		return medicalTestTime;
	}

	public String getOxygenSaturation() {
		return oxygenSaturation;
	}

	public Date getOxygenSaturationTime() {
		return oxygenSaturationTime;
	}

	public String getPulseRate() {
		return pulseRate;
	}

	public Date getPulseRateTime() {
		return pulseRateTime;
	}
	
	

}
