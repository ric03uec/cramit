package com.dev.cramit;

import java.util.ArrayList;
import java.util.List;

import com.dev.cramit.adapters.AllWordsAdapter;
import com.dev.cramit.adapters.WordsDbAdapter;
import com.dev.cramit.models.Word;
import com.dev.cramit.utils.WCConstants;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

public class MarkedWordsList extends ListActivity {
	protected static String TAG = "MARKED_WORD_LIST";
	
	
	protected static int UNMARK_WORD_DIALOG_ID = 1;
	protected static int EMPTY_LIST_DIALOG_ID = 2;
	
	protected List<Word> mWordList;
	protected WordsDbAdapter mWordsDbHelper;
	
	public MarkedWordsList(){
		mWordList = new ArrayList<Word>();
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		gatherMarkedWordList();
		
		if(0 == mWordList.size()){
			removeDialog(UNMARK_WORD_DIALOG_ID);
        	showDialog(EMPTY_LIST_DIALOG_ID, null);
        	return;
		}
		
		
		this.setListAdapter(new AllWordsAdapter(this, R.layout.all_words_layout, mWordList));
		getListView().setTextFilterEnabled(true);
		
		getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
				Bundle bundle = new Bundle();
				bundle.putInt(WCConstants.UNMARKED_WORD_ID, index);
				
				removeDialog(UNMARK_WORD_DIALOG_ID);
				showDialog(UNMARK_WORD_DIALOG_ID, bundle);
				return true;
			}
		});
		
	}
	
	
	
	protected void gatherMarkedWordList(){
		mWordsDbHelper = new WordsDbAdapter(this);
        mWordsDbHelper.open(WCConstants.isFirstRunInstance);
        
        Cursor c = mWordsDbHelper.getWordsWithRank(WCConstants.WORD_RANK_ONE);
        startManagingCursor(c);
        
        for(c.moveToFirst(); c.moveToNext(); c.isAfterLast()){
        	Word word = new Word(c.getInt(c.getColumnIndex("_id")), c.getString(c.getColumnIndex("word")), c.getString(c.getColumnIndex("meaning")), 
        			c.getInt(c.getColumnIndex("rank")), 0, 0);
        	mWordList.add(word);
        }
        
        c.close();
        mWordsDbHelper.close();
	}
	
	protected void updateWordWithRank(int wordId, int rank){
		mWordsDbHelper.open(WCConstants.isFirstRunInstance);
		
		mWordsDbHelper.updateWordRank(wordId, rank);
		
		mWordsDbHelper.close();
	}
	
	protected Dialog onCreateDialog(int id, Bundle args) {
		
		if(id == UNMARK_WORD_DIALOG_ID){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle("Unmark the word ???");
    		
    		int unMarkedWordIndex = args.getInt(WCConstants.UNMARKED_WORD_ID);
    		final int unMarkedWordId = mWordList.get(unMarkedWordIndex).getWordId();
    		
    		builder.setMessage(mWordList.get(unMarkedWordIndex).getWord());
    		
    		AlertDialog alert = builder.create();
    		
    		alert.setButton("Ok", new DialogInterface.OnClickListener() {
 			   public void onClick(DialogInterface dialog, int which) {
 				   updateWordWithRank(unMarkedWordId, WCConstants.WORD_RANK_ZERO);
 			   }
    		});
    		
    		return alert;
		}else if(id == EMPTY_LIST_DIALOG_ID){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			AlertDialog alert = builder.create();
			alert.setTitle("Empty List !!!");
			alert.setMessage("No Marked Words Present...");
			Log.d(TAG, "marked words nt present");
			alert.setButton("OK", new DialogInterface.OnClickListener() {
				   public void onClick(DialogInterface dialog, int which) {
				   }
			});
			
			return alert;
		}
		
		return null;
	}
	
}
