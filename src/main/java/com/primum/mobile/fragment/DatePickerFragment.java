package com.primum.mobile.fragment;

import java.util.Calendar;

import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.primum.mobile.R;
import com.primum.mobile.util.Constants;
import com.primum.mobile.util.PrimumPrefs_;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.preference.Preference;
import android.widget.DatePicker;
import android.widget.EditText;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		EditText date = (EditText)getActivity().findViewById(R.id.txDate);
		
		String lang = getActivity().getSharedPreferences("PrimumPrefs", 0).getString("deviceLang", Constants.PREFS_LANG_ES);
		if(lang.equals(Constants.PREFS_LANG_ES)){
			date.setText(day + "/" + month+1 + "/" + year);
		}else{
			date.setText(month+1 + "/" + day + "/" + year);
		}
	}


}
