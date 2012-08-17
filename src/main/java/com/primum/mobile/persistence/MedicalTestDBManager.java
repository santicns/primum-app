package com.primum.mobile.persistence;

import java.util.List;

import com.primum.mobile.model.MedicalTest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MedicalTestDBManager extends SQLiteOpenHelper {

	public MedicalTestDBManager(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(createTableSentence());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(newVersion>oldVersion){
			db.execSQL("DROP TABLE IF EXISTS MedicalTest");
			db.execSQL(createTableSentence());
		}
	}
	
	public MedicalTest addMedicalTest(MedicalTest medicalTest){
		return addMedicalTest(medicalTest.getMedicalTestId(), medicalTest.getPatientKey(), medicalTest.getMedicalTestKey(), medicalTest.getBody());
	}
	
	public MedicalTest addMedicalTest(long medicalTestId, String patientKey, String medicalTestKey, String body){
		if(medicalTestId==0){
			
		}
		return null;
	}
	
	public MedicalTest getMedicalTest(long medicalTestId){
		//TODO
		return null;
	}
	
	public boolean deleteMedicalTest(MedicalTest medicalTest){
		return deleteMedicalTest(medicalTest.getMedicalTestId());
	}
	
	public boolean deleteMedicalTest(long medicalTestId){
		//TODO
		return false;
	}
	
	public List<MedicalTest> getPatientTests(String patientKey){
		return null;
	}
	
	public boolean deletePatientTests(String patientKey){
		return false;
	}
	
	private String createTableSentence(){
		return "CREATE TABLE MedicalTest ( medicalTestId INTEGER primary key autoincrement, patientKey TEXT, medicalTestKey TEXT, body TEXT)";
	}
	
	protected String TAG = this.getClass().getName();
	
}
