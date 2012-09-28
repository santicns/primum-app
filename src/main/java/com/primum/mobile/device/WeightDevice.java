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
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.primum.mobile.R;
import com.primum.mobile.util.HL7MessageGenerator;

public class WeightDevice{}/* extends BaseDevice {

	private String scaleMacAddress="";
	private Date medicalTestTime;
	private String bodyWeight="";
	private String bodyHeight="";
	private String bodyMassIndex="";
	
	@Override
	public void performTest() {
		
		scaleMacAddress = "48:5d:60:95:dc:34";
		medicalTestTime = new Date();
		bodyWeight = String.valueOf(Math.round(30 + 120*Math.random()));
		bodyHeight = String.valueOf(Math.round(130 + 90*Math.random()));
		bodyMassIndex = String.valueOf(Math.round(15 + 15*Math.random()));
		testInitialized=true;
	}

	@Override
	public String getHL7Message() {
		return HL7MessageGenerator.getWeightScaleOBX(
				scaleMacAddress,
				sdf.format(medicalTestTime),
				bodyWeight,
				bodyHeight,
				bodyMassIndex);
	}
	
	@Override
	public void printResult(Activity activity, int destLayout) {
		LinearLayout resultLayout = (LinearLayout)activity.findViewById(destLayout);
		View result = activity.getLayoutInflater().inflate(R.layout.result_weight, null);
		resultLayout.addView(result);
		
		TextView tx = (TextView)activity.findViewById(R.id.txTime);
		tx.setText(formatDate(getMedicalTestTime()));
		
		tx = (TextView)activity.findViewById(R.id.txBodyWeight);
		tx.setText(getBodyWeight() + " Kg");
		
		tx = (TextView)activity.findViewById(R.id.txBodyHeight);
		tx.setText(getBodyHeight() + " cm");
		
		tx = (TextView)activity.findViewById(R.id.txBodyMassIndex);
		tx.setText(getBodyMassIndex() );

	}

	public String getScaleMacAddress() {
		return scaleMacAddress;
	}

	public Date getMedicalTestTime() {
		return medicalTestTime;
	}

	public String getBodyWeight() {
		return bodyWeight;
	}

	public String getBodyHeight() {
		return bodyHeight;
	}

	public String getBodyMassIndex() {
		return bodyMassIndex;
	}
	
}*/
