package com.dev.cramit.adapters;

import java.util.List;

import com.dev.cramit.models.Word;

import android.content.Context;
import android.widget.ArrayAdapter;

public class WordsUsageAdapter extends ArrayAdapter<Word> {

	public WordsUsageAdapter(Context context, int textViewResourceId, List<Word> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}

}
