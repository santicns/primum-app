package com.primum.mobile.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.primum.mobile.R;
import com.primum.mobile.util.Constants;
import com.primum.mobile.util.LangUtils;
import com.primum.mobile.util.PrimumPrefs_;

@EActivity
public class StartActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		LangUtils.updateLanguage(this, primumPrefs.deviceLang().get());
		
        setContentView(R.layout.start);
    }
    
    @Click(R.id.btnStart)
    public void onClickStart(){
    	finish();
    	MainActivity_.intent(this).start();
    }
    
    @Click(R.id.btnExit)
    public void onClickExit(){
    	finish();
    }
    
    @Pref
	PrimumPrefs_ primumPrefs; 
    private static String TAG = "StartActivity";
}

