package com.hb.app.tong;

import android.os.Bundle;
import android.view.Menu;

public class PrefActivity extends android.preference.PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_preference);
		addPreferencesFromResource(R.xml.prefernece);
		
	}

	

}
