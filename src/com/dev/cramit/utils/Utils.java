package com.dev.cramit.utils;

import android.database.Cursor;
import android.util.Log;

import com.dev.cramit.adapters.WordsDbAdapter;
import com.dev.cramit.models.Word;

public class Utils {

	private static final String TAG = "WC_UTILS";
	
	/**
	 * Update the word with the specified ID, and give it the specified rank
	 * @param adapter - db adapter to be used
	 * @param wordId - ID of the word to be updated
	 * @param rank	- rank to be put against the word
	 */
	public static void updateWordWithRank(WordsDbAdapter adapter, int wordId, int rank){
		
		Log.d(TAG, "update word ------------------->>>   " + wordId + "         rank : " + rank);
		adapter.open(WCConstants.isFirstRunInstance);
		
		adapter.updateWordRank(wordId, rank);
		
		adapter.close();
	}
}
