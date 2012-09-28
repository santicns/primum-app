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

package com.primum.mobile.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.primum.mobile.model.Patient;

public class HL7MessageGenerator {

	private static String DATE_FORMAT = "yyyyMMddhhmmSS";
	private static SimpleDateFormat sdf;
	
	
	public static String getHeader(String deviceMacAddress, Date medicalTestTime, Patient patient){
		return getMSH(deviceMacAddress, medicalTestTime) + getPID(patient) + getOBR(deviceMacAddress, medicalTestTime);
	}
	
	public static String getMSH(String deviceMacAddress, Date medicalTestTime){
		sdf = new SimpleDateFormat(DATE_FORMAT);
		return "MSH|^~\\&|PrimumHealthIT^" + deviceMacAddress + "^EUI-64||||" + sdf.format(medicalTestTime) + "||ORU^R01^ORU_R01|MSGID1234|P|2.6|||NE|AL|||||IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^HL7";
	}
	
	public static String getPID(Patient patient){
		sdf = new SimpleDateFormat(DATE_FORMAT);
		return "PID|||" + patient.getPatientId() + "^^^Primum Health IT^PI||" + patient.getFullName() + "^^^^L^A||" + sdf.format(patient.getBirthDate()) + "|" + patient.getGender();
	}
	
	public static String getOBR(String deviceMacAddress, Date medicalTestTime){
		sdf = new SimpleDateFormat(DATE_FORMAT);
		return "OBR|1|0001^PrimumHealthIT^" + deviceMacAddress + "^EUI-64|0001^PrimumHealthIT^" + deviceMacAddress + "^EUI-64|182777000^monitoring of patient^SNOMED-CT|||" + sdf.format(medicalTestTime);
	}

	public static String getTensiometerOBX(String tensiometerMacAddress, String medicalTestTime, String systolic, String diastolic, String meanArterialPresure, String pulseRate) {
		return "OBX|1||528391^MDC_DEV_SPEC_PROFILE_BP^MDC|1|||||||X|||||||" + tensiometerMacAddress + "^EUI-64" + 
			"OBX|2||150020^MDC_PRESS_BLD_NONINV^MDC|1.0.1|||||||X|||" + medicalTestTime + 
			"OBX|3|NM|150021^MDC_PRESS_BLD_NONINV_SYS^MDC|1.0.1.1|" + systolic + "|266016^MDC_DIM_MMHG^MDC|||||R" + 
			"OBX|4|NM|150022^MDC_PRESS_BLD_NONINV_DIA^MDC|1.0.1.2|" + diastolic + "|266016^MDC_DIM_MMHG^MDC|||||R" + 
			"OBX|5|NM|150023^MDC_PRESS_BLD_NONINV_MEAN^MDC|1.0.1.3|" + meanArterialPresure + "|266016^MDC_DIM_MMHG^MDC|||||R" + 
			"OBX|6|NM|149546^MDC_PULS_RATE_NON_INV^MDC|1.0.0.1|" + pulseRate + "|264864^MDC_DIM_BEAT_PER_MIN^MDC|||||R|||" + medicalTestTime;
	}

	public static String getWeightScaleOBX(String scaleMacAddress, String medicalTestTime, String bodyWeight, String bodyHeight, String bodyMassIndex) {
		return "OBX|1||528399^MDC_DEV_SPEC_PROFILE_SCALE^MDC|1|||||||X|||||||" + scaleMacAddress + "^EUI-64" + 
			"OBX|2|NM|188736^MDC_MASS_BODY_ACTUAL^MDC|1.0.0.1|" + bodyWeight + "|263875^MDC_DIM_KILO_G^MDC|||||R|||" + medicalTestTime + 
			"OBX|3|NM|188740^MDC_LEN_BODY_ACTUAL^MDC|1.0.0.2|" + bodyHeight + "|263441^MDC_DIM_CENTI_M^MDC|||||R|||" + medicalTestTime +
			"OBX|4|NM|188752^MDC_RATIO_MASS_BODY_LEN_SQ^MDC|1.0.0.3|" + bodyMassIndex + "|264096^MDC_DIM_KG_PER_M_SQ^MDC|||||R|||" + medicalTestTime; 
	}

	public static String getPulsioximeterOBX(String pulsioximeterMacAddress, String medicalTestTime, String oxygenSaturation, String oxygenSaturationTime, String pulseRate, String pulseRateTime) {
		return "OBX|1||528388^MDC_DEV_SPEC_PROFILE_PULS_OXIM^MDC|1|||||||X|||" + medicalTestTime + "||||" + pulsioximeterMacAddress + "^EUI-64" + 
			"OBX|2|NM|150456^MDC_PULS_OXIM_SAT_O2^MDC|1.0.0.1|" + oxygenSaturation + "|262688^MDC_DIM_PERCENT^MDC|||||R|||" + oxygenSaturationTime +
			"OBX|3|NM|149546^MDC_PULS_RATE_NON_INV^MDC|1.0.0.2|" + pulseRate + "|264864^MDC_DIM_BEAT_PER_MIN^MDC|||||R|||" + pulseRateTime;
	}
	
	private final static String _deviceMacAddress = "ABCDE48234567ABCD"; // En formato EUI-64
	private final static String _medicalTestTime = "20120824183021+0000"; // Formato YY MM DD HH mm SS +/-ZZZZ
	private final static String _patientId = "28849103N";
	private final static String _patientFullName = "Alberto^Garcia^Martinez";
	private final static String _patientBirthday = "19430312000000+0000";	// Formato YY MM DD HH mm SS +/-ZZZZ
	private final static String _patientGender = "M";

	
	
public static void main (String args[]) {
		
		System.out.println("Testing the HL7 Message Generator");
	
		// Cabecera del mensaje

		String MSH = "MSH|^~\\&|PrimumHealthIT^" + _deviceMacAddress + "^EUI-64||||" + _medicalTestTime + "||ORU^R01^ORU_R01|MSGID1234|P|2.6|||NE|AL|||||IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^HL7";

		// Identificación del paciente

		String PID = "PID|||" + _patientId + "^^^Primum Health IT^PI||" + _patientFullName + "^^^^L^A||" + _patientBirthday + "|" + _patientGender;

		// OBR (Observation Request)

		String OBR = "OBR|1|0001^PrimumHealthIT^" + _deviceMacAddress + "^EUI-64|0001^PrimumHealthIT^" + _deviceMacAddress + "^EUI-64|182777000^monitoring of patient^SNOMED-CT|||" + _medicalTestTime;

		// OBX (Observation Result) - depende de cada dispositivo conectado
		
		String OBX = getTensiometerOBX("ABCD12345678DEFA", "20120912213421", "123", "54", "54", "123");
		
		String HL7message = MSH + PID + OBR + OBX;
		
		System.out.println("This is the generated HL7 message: \n " + HL7message);
	}
}


/*
Sample HL7 message:

MSH|^~\&|AcmeInc^ACDE48234567ABCD^EUI-64||||20090713090030+0000||ORU^R01^ORU_R01|MSGID1234|P|2.6|||NE|AL|||||IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^HL7 
PID|||789567^^^Imaginary Hospital^PI ||Doe^John^Joseph^^^^L^A|||M 
OBR|1|AB12345^AcmeAHDInc^ACDE48234567ABCD^EUI-64|CD12345^AcmeAHDInc^ACDE48234567ABCD^EUI- 64|182777000^monitoring of patient^SNOMED-CT|||20090813095715+0000 
OBX|1|CWE|68220^MDC_TIME_SYNC_PROTOCOL^MDC|0.0.0.1|532224^MDC_TIME_SYNC_NONE^MDC|||||R 
OBX|2||528391^MDC_DEV_SPEC_PROFILE_BP^MDC|1|||||||X|||||||0123456789ABCDEF^EUI-64 
OBX|3||150020^MDC_PRESS_BLD_NONINV^MDC|1.0.1|||||||X|||20090813095715+0000 
OBX|4|NM|150021^MDC_PRESS_BLD_NONINV_SYS^MDC|1.0.1.1|120|266016^MDC_DIM_MMHG^MDC|||||R 
OBX|5|NM|150022^MDC_PRESS_BLD_NONINV_DIA^MDC|1.0.1.2|80|266016^MDC_DIM_MMHG^MDC|||||R 
OBX|6|NM|150023^MDC_PRESS_BLD_NONINV_MEAN^MDC|1.0.1.3|100|266016^MDC_DIM_MMHG^MDC|||||R 
OBX|7|DTM|67975^MDC_ATTR_TIME_ABS^MDC|1.0.0.1|20091028123702||||||R|||20091028173702+0000

*/
