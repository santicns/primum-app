package com.primum.mobile.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.primum.mobile.R;
import com.primum.mobile.model.Patient;

@EActivity
public class TestsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
        setContentView(R.layout.tests);
    }
    
    @Click(R.id.btnStart)
	void clickOnStart() {
		dialog = ProgressDialog.show(this, "", 
                "Performing test. Please wait...", true);
		dialog.show();
		performTest(0);
	}
    
    @Background
	void performTest(long testId) {
		
		try {
			Thread.currentThread().sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		testFinished();
	}
    
    @UiThread
	void testFinished(){
		dialog.cancel();
		Toast.makeText(this, "Test finished!", Toast.LENGTH_LONG).show();
		finish();
		ResultActivity_.intent(this).start();
	}
    
    private ProgressDialog dialog;
    private static String TAG = "TestsActivity";
}
