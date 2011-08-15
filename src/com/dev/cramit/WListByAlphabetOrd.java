package com.dev.cramit;

import android.util.Log;

public class WListByAlphabetOrd extends WListByAlphabetAbs {

	public WListByAlphabetOrd() {
		super(false);
		Log.d(TAG, "CONstructor------------ wlby ORDERRRRR" + mIsRandom);
	}

	protected String getTag() {
		return "WL_BY_ALPHABET_IN_ORDER";
	}
}
