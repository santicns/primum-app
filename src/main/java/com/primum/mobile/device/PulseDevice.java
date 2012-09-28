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
import com.primum.mobile.util.HL7MessageGenerator;

public class PulseDevice {}/* extends BaseDevice {
	
	@Override
	public void performTest() {
		tensiometerMacAddress = "48:5d:60:95:dc:34";
		medicalTestTime = new Date();
		systolic = String.valueOf(Math.round(100 + 80*Math.random()));
		diastolic = String.valueOf(Math.round(50 + 80*Math.random()));
		meanArterialPresure= String.valueOf(Math.round(70 + 40*Math.random()));
		pulseRate= String.valueOf(Math.round(50 + 40*Math.random()));
		
		testInitialized=true;
	}
	
	@Override
	public String getHL7Message() {
		return HL7MessageGenerator.getTensiometerOBX(
				tensiometerMacAddress,
				sdf.format(medicalTestTime),
				systolic,
				diastolic,
				meanArterialPresure,
				pulseRate);
	}
	
	@Override
	public void printResult(Activity activity, int destLayout) {
		LinearLayout resultLayout = (LinearLayout)activity.findViewById(destLayout);
		View result = activity.getLayoutInflater().inflate(R.layout.result_pulse, null);
		resultLayout.addView(result);
		
		TextView tx = (TextView)activity.findViewById(R.id.txTime);
		tx.setText(formatDate(getMedicalTestTime()));
		
		tx = (TextView)activity.findViewById(R.id.txSystolic);
		tx.setText(getSystolic() + " mmHg");
		
		tx = (TextView)activity.findViewById(R.id.txDiastolic);
		tx.setText(getDiastolic() + " mmHg");
		
		tx = (TextView)activity.findViewById(R.id.txMeanPresure);
		tx.setText(getMeanArterialPresure() + " mmHg");
		
		tx = (TextView)activity.findViewById(R.id.txPulseRate);
		tx.setText(getPulseRate() + " BPM");
		
	}

	public String getTensiometerMacAddress() {
		return tensiometerMacAddress;
	}

	public Date getMedicalTestTime() {
		return medicalTestTime;
	}

	public String getSystolic() {
		return systolic;
	}

	public String getDiastolic() {
		return diastolic;
	}

	public String getMeanArterialPresure() {
		return meanArterialPresure;
	}

	public String getPulseRate() {
		return pulseRate;
	}

	private String tensiometerMacAddress="";
	private Date medicalTestTime;
	private String systolic="";
	private String diastolic="";
	private String meanArterialPresure="";
	private String pulseRate="";
	

}*/

