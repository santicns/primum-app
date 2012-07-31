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

package com.primum.mobile.model;

public class MedicalTest {
	
	private String patientKey;
	private String medicalTestKey;
	private String body;
	
	public String getPatientKey() {
		return patientKey;
	}
	public void setPatientKey(String patientKey) {
		this.patientKey = patientKey;
	}
	public String getMedicalTestKey() {
		return medicalTestKey;
	}
	public void setMedicalTestKey(String medicalTestKey) {
		this.medicalTestKey = medicalTestKey;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
}
