package com.primum.mobile.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.primum.mobile.R;
import com.primum.mobile.util.PrefUtils;
import com.primum.mobile.util.PrimumPrefs_;

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
    	int selected = rgTests.getCheckedRadioButtonId();
    	if(selected!=-1){
    		if(!PrefUtils.isUserSelected(primumPrefs)){
    			PatientData1Activity_.intent(this).start();
    		}
    		else{
    			ResultActivity_.intent(this).start();
    		}
    	}
    	else{
    		Toast.makeText(this, R.string.select_one_test,  Toast.LENGTH_SHORT).show();
    	}
	}
    
    
    @Click(R.id.btnBack)
	void clickOnBack() {
    	finish();
	}
    
    @ViewById
    RadioGroup rgTests;
    @Pref
	PrimumPrefs_ primumPrefs;
    private ProgressDialog dialog;
    private static String TAG = "TestsActivity";
}

