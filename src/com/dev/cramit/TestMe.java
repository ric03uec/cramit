package com.dev.cramit;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Collect the words according to the defined conditions and show them in a problem set of 30 words each
 * The words are collected from full collection of words.
 * @author devashish
 *
 */
public class TestMe extends Activity {
	
	private static final String TAG = "TEST_ME";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d(TAG, "THE action whcih started this activity is ----> " + getIntent().getAction());
	}
}
