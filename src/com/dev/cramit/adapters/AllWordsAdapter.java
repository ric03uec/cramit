package com.dev.cramit.adapters;

import java.util.List;

import com.dev.cramit.R;
import com.dev.cramit.models.Word;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

public class AllWordsAdapter extends ArrayAdapter<Word>{

	protected final Activity context;
	protected final List<Word> wordList;
	
	public AllWordsAdapter(Activity context, int layoutResourceId, List<Word> wordList) {
		super(context, layoutResourceId, wordList);
		this.context = context;
		this.wordList = wordList;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if(null == convertView){
			LayoutInflater inflator = context.getLayoutInflater();
			final ViewHolder viewHolder = new ViewHolder();
			
			view = inflator.inflate(R.layout.all_words_entry, null);
			viewHolder.wordTextView 	= (TextView) view.findViewById(R.id.all_words_list_entry);
			viewHolder.meaningTextView 	= (TextView) view.findViewById(R.id.all_words_list_entry_meaning);
			
			view.setTag(viewHolder);
		}else{
			view = convertView;
			((ViewHolder) view.getTag()).wordTextView.setTag(position);
			((ViewHolder) view.getTag()).meaningTextView.setTag(position);
			
		}
		
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.wordTextView.setText(wordList.get(position).getWord());
		holder.meaningTextView.setText(wordList.get(position).getMeaning());
		return view;
	}
	
	static class ViewHolder {
 		public TextView wordTextView;
 		public TextView meaningTextView;
 	}
	

}
