package com.dev.cramit;

import java.util.ArrayList;
import java.util.List;

import com.dev.cramit.adapters.AllWordsAdapter;
import com.dev.cramit.adapters.WordsDbAdapter;
import com.dev.cramit.models.Word;
import com.dev.cramit.utils.Utils;
import com.dev.cramit.utils.WCConstants;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

public class AllWordsList extends ListActivity {

	private static final String TAG = "ALLWORDSLIST";
	protected static final int RESET_ID	= 1;
	protected static final int GOTO_ID = 2;
	
	protected static final int LETTER_DIALOG_ID 	= 3;
	protected static final int MARK_WORD_DIALOG_ID	= 4;
	
	protected List<Word> mWordList;
	protected WordsDbAdapter mWordsDbHelper;
	protected List<Integer> mLetterIndexList;
	
	public AllWordsList(){
		mWordList = new ArrayList<Word>();
		mLetterIndexList = new ArrayList<Integer>();
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		gatherWordList();
		populateLetterIndex();
		
		this.setListAdapter(new AllWordsAdapter(this, R.layout.all_words_layout, mWordList));
		getListView().setTextFilterEnabled(true);
		
		getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
				Bundle bundle = new Bundle();
				bundle.putInt(WCConstants.MARKED_WORD_INDEX, index);
				
				removeDialog(MARK_WORD_DIALOG_ID);
				showDialog(MARK_WORD_DIALOG_ID, bundle);
				
				return true;
			}
		});
		
		
//		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			
//            public void onItemClick(AdapterView<?> listView, View itemView, int position, long id) {
//            	
//            	itemView.set
//                itemView = findViewById(R.layout.all_words_only_word_layout);
//                TextView tv = (TextView) itemView.findViewById(R.id.all_words_only_word);
//                tv.setText("TEST");
//            }
//        });
	}
	
	protected void populateLetterIndex(){
		String currentLetter = mWordList.get(0).getWord().substring(0,1).toLowerCase();
		mLetterIndexList.add(0);
		for(int i = 0 ; i < mWordList.size(); i++){
			if(mWordList.get(i).getWord().substring(0,1).toLowerCase().equals(currentLetter)){
				continue;
			}
			currentLetter = mWordList.get(i).getWord().substring(0,1).toLowerCase();
			mLetterIndexList.add(i);
		}
	}
	
	protected void gatherWordList(){
		mWordsDbHelper = new WordsDbAdapter(this);
        mWordsDbHelper.open(WCConstants.isFirstRunInstance);
        
        Cursor c = mWordsDbHelper.getWords();
        startManagingCursor(c);
        
        for(c.moveToFirst(); c.moveToNext(); c.isAfterLast()){
        	Word word = new Word(c.getInt(c.getColumnIndex("_id")), 
        							c.getString(c.getColumnIndex("word")), 
        								c.getString(c.getColumnIndex("meaning")),
        									c.getString(c.getColumnIndex("word_usage")),
        										c.getInt(c.getColumnIndex("rank")), 0, 0);
        	mWordList.add(word);
        }
        
        c.close();
        mWordsDbHelper.close();
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, RESET_ID, 0, R.string.reset_words);
        menu.add(0, GOTO_ID, 0 , R.string.goto_letter);
        return result;
	}
	
    public boolean onOptionsItemSelected(MenuItem item) {
	    	switch (item.getItemId()) {
		        case RESET_ID:
		        	getListView().setSelection(0);
		            return true;
		        case GOTO_ID:
		        	showDialog(LETTER_DIALOG_ID);
		        	break;
	        }
	    	
	    return super.onOptionsItemSelected(item);
	}
    
	
    protected Dialog onCreateDialog(int id, Bundle args) {
    	if(id == LETTER_DIALOG_ID){
    		final CharSequence[] items = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
				"J", "K", "L","M", "N", "O","P", "Q", "R", "S",
				"T", "U", "V", "W", "X", "Y", "X"};

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Select a Letter");
			
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					setCurrentListIndex(mLetterIndexList.get(item));
				}
			});
			AlertDialog alert = builder.create();
    		return alert;
    	}else if(id == MARK_WORD_DIALOG_ID){
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle("Mark the word ???");
    		
    		int markedWordIndex = args.getInt(WCConstants.MARKED_WORD_INDEX);
    		final int markedWordId = mWordList.get(markedWordIndex).getWordId();
    		
    		builder.setMessage(mWordList.get(markedWordIndex).getWord());
    		
    		AlertDialog alert = builder.create();
    		
    		alert.setButton("Ok", new DialogInterface.OnClickListener() {
 			   public void onClick(DialogInterface dialog, int which) {
 				   Utils.updateWordWithRank(mWordsDbHelper, markedWordId, WCConstants.WORD_RANK_ONE);
 			   }
    		});
    		return alert;
    	}
    	return null;
    }
    
    protected void setCurrentListIndex(Integer index){
    	getListView().setSelection(index);
    }
    
	protected void onResume() {
		
		int savedPosition = this.getPreferences(Context.MODE_PRIVATE).getInt(WCConstants.ALL_WORDS_VISIT_INDEX, -1);
		if(savedPosition != 0){
			setCurrentListIndex(savedPosition);
		}
		super.onResume();
	}
	
	protected void onPause() {
		Editor e = this.getPreferences(Context.MODE_PRIVATE).edit();
    	e.putInt(WCConstants.ALL_WORDS_VISIT_INDEX, getListView().getFirstVisiblePosition());
    	e.commit();
    	super.onPause();
	}
}
