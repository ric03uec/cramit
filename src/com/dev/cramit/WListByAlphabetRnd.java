package com.dev.cramit;

import android.util.Log;

public class WListByAlphabetRnd extends WListByAlphabetAbs {
	
	public WListByAlphabetRnd() {
		super(true);
		Log.d(TAG, "CONstructor------------ wlby RANDOMMMMMM" + mIsRandom);
	}

	@Override
	protected String getTag() {
		return "WL_BY_ALPHABET_RANDOM";
	}
}
