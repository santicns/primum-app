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

package com.primum.mobile.persistence;

import java.util.ArrayList;
import java.util.List;

import com.primum.mobile.model.MedicalTest;
import com.primum.mobile.util.Constants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MedicalTestDBManager extends SQLiteOpenHelper {

	public MedicalTestDBManager(Context context) {
		super(context, Constants.DB_NAME,null, Constants.DB_VERSION_NUMBER);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(createTableSentence());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion>oldVersion) {
			db.execSQL("DROP TABLE IF EXISTS MedicalTest");
			db.execSQL(createTableSentence());
		}
	}
	
	public void addMedicalTest(MedicalTest medicalTest) {
		addMedicalTest(medicalTest.getPatientKey(), medicalTest.getMedicalTestKey(), medicalTest.getBody());
	}
	
	public void addMedicalTest(String patientKey, String medicalTestKey, String body) {
		ContentValues contentValues = new ContentValues();

        contentValues.put("patientKey", patientKey);
		contentValues.put("medicalTestKey", medicalTestKey);
		contentValues.put("body", body);
		
		getWritableDatabase().insert(TABLE_NAME, null, contentValues);
	}
	
	public MedicalTest getMedicalTest(long medicalTestId) {
		//TODO
		return null;
	}
	
	public int deleteMedicalTest(MedicalTest medicalTest) {
		return deleteMedicalTest(medicalTest.getMedicalTestId());
	}
	
	public int deleteMedicalTest(long medicalTestId) {
		String[] whereArgs = {String.valueOf(medicalTestId)};
		Log.d(TAG, "Delete medicalTest " + medicalTestId);
		return this.getWritableDatabase().delete(TABLE_NAME, "medicalTestId=?", whereArgs);
	}
	
	
	public List<MedicalTest> getAllTests() {
		String[] selectionArgs = {};
		Cursor c = getReadableDatabase().rawQuery("select patientKey, medicalTestKey, body from " + TABLE_NAME, selectionArgs);

        if (c==null) {
            return null;
        }

		List<MedicalTest> medicalTests = new ArrayList<MedicalTest>();

		while(c.moveToNext()) {
			medicalTests.add(cursorToMedicalTest(c));
		}

        return medicalTests;
	}
	
	public List<MedicalTest> getPatientTests(String patientKey) {
		String[] selectionArgs = {patientKey};
		Cursor c = getReadableDatabase().rawQuery("select medicalTestId, patientKey, medicalTestKey, body from " + TABLE_NAME + " where patientKey = ?", selectionArgs);

        if (c==null) {
            return null;
        }

		List<MedicalTest> medicalTests = new ArrayList<MedicalTest>();

        while(c.moveToNext()) {
			medicalTests.add(cursorToMedicalTest(c));
		}

        return medicalTests;
	}
	
	public boolean deletePatientTests(String patientKey) {
		return false;
	}
	
	private String createTableSentence() {
		return "CREATE TABLE " + TABLE_NAME + " ( medicalTestId INTEGER primary key autoincrement, patientKey TEXT, medicalTestKey TEXT, body TEXT)";
	}
	
	private MedicalTest cursorToMedicalTest(Cursor cursor) {
		MedicalTest medicalTest = new MedicalTest();

        medicalTest.setPatientKey(cursor.getString(cursor.getColumnIndex("patientKey")));
		medicalTest.setMedicalTestId(cursor.getInt(cursor.getColumnIndex("medicalTestId")));
		medicalTest.setMedicalTestKey(cursor.getString(cursor.getColumnIndex("medicalTestKey")));
		medicalTest.setBody(cursor.getString(cursor.getColumnIndex("body")));

        return medicalTest;
	}
	
	protected String TAG = this.getClass().getName();
	protected String TABLE_NAME = "MedicalTest";
	
}
