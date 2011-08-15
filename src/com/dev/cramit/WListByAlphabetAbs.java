package com.dev.cramit;


import java.util.ArrayList;
import java.util.List;

import com.dev.cramit.adapters.WordsDbAdapter;
import com.dev.cramit.models.Answer;
import com.dev.cramit.models.Problem;
import com.dev.cramit.models.Word;
import com.dev.cramit.utils.Utils;
import com.dev.cramit.utils.WCConstants;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Abstract class for showing wordlist alphabeticallyhey
 * @author devashish
 *
 */
public abstract class WListByAlphabetAbs extends Activity {
	protected static String TAG = null;
	
	public static final int REQUEST_CODE = 1;
	
	protected WordsDbAdapter 	mWordsDbHelper;
	protected List<Word> 		mWordList ;
	protected Integer			mCounter;
	protected String 			mCurrentLetter;
	protected boolean			mIsRandom;
	private Integer				mIncorrectIndex;
	
	public WListByAlphabetAbs(boolean isRandom){
		mWordList 	= 	new ArrayList<Word>();
		mCounter 	= 	0;
		mIsRandom 	= 	isRandom;
		mIncorrectIndex = 0;
		setTag();
	}
	
	private OnClickListener continueButtonListener = new OnClickListener() {
		public void onClick(View v) {
			showProblem();
		}
	};
	
	private OnClickListener exitButtonListener = new OnClickListener() {
		public void onClick(View v) {
			finish();
		}
	};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.wl_by_alphabet_stats);
        
		mCurrentLetter = getIntent().getAction().substring(0,2);
		
		resumeState(mCurrentLetter);
		
        initializeData();
	}
	
	protected void onResume() {
		setHandlers();
		super.onResume();
	}
	
	/**
	 * Set handlers for Continue and Exit button. This is called everytime the activity is restarted to restore the
	 * handlers associated with the buttons
	 */
	protected void setHandlers(){
		Button continueButton = (Button)findViewById(R.id.wlByAlpContinueButton);
		Button exitButton	  = (Button)findViewById(R.id.wlByAlpExitButton);
		
		continueButton.setOnClickListener(continueButtonListener);
		exitButton.setOnClickListener(exitButtonListener);
	}
	
	
	/**
	 * Restore the state when resuming. This is done once when the activity is started
	 * @param currentLetter
	 */
	protected void resumeState(String currentLetter){
		if(mIsRandom){
			Integer currentWordIndex = this.getPreferences(Context.MODE_PRIVATE).getInt(WCConstants.RAND_LETTER_COUNTER + mCurrentLetter, -1);
	    	String currentWordLetter = this.getPreferences(Context.MODE_PRIVATE).getString(WCConstants.RAND_LETTER + mCurrentLetter, null);
	    	
	    	Log.d(TAG, "Current letter -------> " + currentWordLetter + "     and counter is : " + currentWordIndex + WCConstants.RAND_LETTER);
	    	if((null != currentWordLetter) && (-1 != currentWordIndex)){
	    		if(currentLetter.trim().equals(currentWordLetter.trim())){
	    			this.mCounter = currentWordIndex;
	    		}
	    	}
		}else{
			Integer currentWordIndex = this.getPreferences(Context.MODE_PRIVATE).getInt(WCConstants.IN_ORDER_LETTER_COUNTER + mCurrentLetter, -1);
	    	String currentWordLetter = this.getPreferences(Context.MODE_PRIVATE).getString(WCConstants.IN_ORDER_LETTER + mCurrentLetter, null);
	    	
	    	Log.d(TAG, "Current letter -------> " + currentWordLetter + "     and counter is : " + currentWordIndex + WCConstants.IN_ORDER_LETTER);
	    	if((null != currentWordLetter) && (-1 != currentWordIndex)){
	    		if(currentLetter.trim().equals(currentWordLetter.trim())){
	    			this.mCounter = currentWordIndex;
	    		}
	    	}
		}
	}
	
	/**
	 * Initialize the data once when the Activity is started.
	 */
	private void initializeData(){
		gatherWordList();
		
		if(mWordList.isEmpty()){
        	AlertDialog alert = new AlertDialog.Builder(this).create();
    		alert.setTitle("OMG!!!");
    		alert.setMessage("No words exist for specified letter");
    		alert.setButton("OK", new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int which) {
			   }
    		});
    		alert.show();
        	return;
        }
		showProblem();
	}
	
	private void showProblem(){
		Intent intent = new Intent(WListByAlphabetAbs.this, ProblemView.class);

		intent.putExtra("problem", getProblem());
		intent.putExtra(WCConstants.IS_RANDOM_WL, mIsRandom);
		startActivityForResult(intent, REQUEST_CODE);
	}
	
	/**
	 * Create and return a problem
	 * @return - Problem to be shown
	 */
	private Problem getProblem(){
		List<Word> wordsForProblem = new ArrayList<Word>();
		
		if(mCounter == (mWordList.size() - 1)){
			mCounter = 0;
		}
		wordsForProblem.add(mWordList.get(mCounter));
		
		Integer wordListLength = mWordList.size();
		Integer base = 10;
		while(true){
			int quotient = (int) (wordListLength/base);
			if(0 == quotient){
				break;
			}
			base *= 10;
		}
		
		List<Integer> optionsIDList = new ArrayList<Integer>();
		for(int i = 0 ; i < 4 ; i++){
			while(true){
				int index = (int)((Math.random() * base));
				if((index >= wordListLength) || (index == mCounter) || optionsIDList.contains(index)){
					continue;
				}
				wordsForProblem.add(mWordList.get(index));
				optionsIDList.add(index);
				break;
			}
		}
		
		Problem problem = new Problem(wordsForProblem.get(0), wordsForProblem.get(1), wordsForProblem.get(2), wordsForProblem.get(3), wordsForProblem.get(4));
		return problem;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		Log.d(TAG, "---------------Activity result received by wlist by adapter class with code : " + resultCode);
		
		switch(requestCode){
			case (REQUEST_CODE) : {
				if(resultCode == Activity.RESULT_OK){
					Answer answer = ((Answer)data.getParcelableExtra("answer"));
					
					if(data.getStringExtra("isShowAnswer") != null){
						if(data.getStringExtra("isShowAnswer").equals(WCConstants.SHOW_ANSWER_TRUE)){
							Log.d(TAG, "SHOWWWWWWWWWWWWWWWWWWW");
							/**
							 * TODO: handle the word accordingly
							 */
							Utils.updateWordWithRank(mWordsDbHelper, mWordList.get(mCounter).getWordId(), WCConstants.WORD_RANK_ONE);
							mIncorrectIndex ++;
						}
					}else{
						if(answer.isCorrect().equals(WCConstants.WORD_ANSWER_TRUE)){
							Log.d(TAG, "CORRECT ANSERRRRRRRRRRRRRRRRRRRRR");
							
						}else{
							Log.d(TAG, "WRONGGGGGGGGGGGGGG");
							Utils.updateWordWithRank(mWordsDbHelper, mWordList.get(mCounter).getWordId(), WCConstants.WORD_RANK_ONE);
							mIncorrectIndex ++;
						}
					}
					mCounter++;
					showProblem();
				}else if(resultCode == Activity.RESULT_CANCELED){
					showStatistics();
				}else if(resultCode == WCConstants.RESULT_RESET_LIST){
					mCounter = 0;
					showProblem();
				}else if(resultCode == WCConstants.RESULT_RANDOMIZE_LIST){
					mCounter 		= 0;
					mIncorrectIndex = 0;
					initializeData();
				}
				break;
			}
		}
	}
	/**
	 * Show the summary of the usage of current wordlist
	 */
	private void showStatistics(){
		setContentView(R.layout.wl_by_alphabet_stats);
		TextView currentLetter = (TextView) findViewById(R.id.wl_current_letter);
		currentLetter.setText("Current Letter\t:\t\t" + mCurrentLetter);
		
		TextView totalWordsCount = (TextView) findViewById(R.id.wl_total_words);
		totalWordsCount.setText("Total Words\t:\t\t" + mWordList.size());
		
		TextView viewedWordsCount = (TextView) findViewById(R.id.wl_viewed_words);
		viewedWordsCount.setText("Viewed Words\t:\t\t" + (mCounter + 1));
		
		TextView currentMode	= (TextView) findViewById(R.id.current_mode);
		if(mIsRandom){
			currentMode.setText("Current Mode\t:\t\tRandom");
		}else{
			currentMode.setText("Current Mode\t:\t\tIn Order");
		}
		
		TextView markedWords = (TextView) findViewById(R.id.session_marked_words);
		markedWords.setText("Incorrect In This Session\t:\t "+ mIncorrectIndex);
	}
	
	/**
	 * Populate the wordList array for all the words starting with the specified letter from database
	 */
	private void gatherWordList(){
		mWordsDbHelper = new WordsDbAdapter(this);
        mWordsDbHelper.open(WCConstants.isFirstRunInstance);
        
        Cursor c = mWordsDbHelper.getWordsForLetter(mCurrentLetter);
        startManagingCursor(c);
        
        mWordList.clear();
        
        for(c.moveToFirst(); c.moveToNext(); c.isAfterLast()){
        	Word word = new Word(c.getInt(c.getColumnIndex("_id")), c.getString(c.getColumnIndex("word")), c.getString(c.getColumnIndex("meaning")), c.getInt(c.getColumnIndex("rank")), 0, 0);
        	mWordList.add(word);
        }
        
        c.close();
        mWordsDbHelper.close();
        
        if(mIsRandom){
        	scrambleList();
        }
	}
	
	/**
	 * Randomize the wordlist
	 */
	protected void scrambleList(){
		
		Integer base = 10;
		while(true){
			int quotient = (int) (mWordList.size()/base);
			if(0 == quotient){
				break;
			}
			base *= 10;
		}
		
		for(int i = 0 ; i < mWordList.size(); i++){
			while(true){
				int index = (int)((Math.random() * base));
				if((index >= mWordList.size()) || (index == mCounter)){
					continue;
				}
				
				Word temp = new Word(mWordList.get(i));
				mWordList.set(i, mWordList.get(index));
				mWordList.set(index, temp);
				
				break;
			}
		}
		
	}
	
	public void onBackPressed() {
		if(mWordsDbHelper.isOpen()){
			mWordsDbHelper.close();
		}
		saveCurrentState();
		super.onBackPressed();
	}
	
	 @Override
    protected void onStop() {
    	saveCurrentState();
    	
    	if(mWordsDbHelper.isOpen()){
			mWordsDbHelper.close();
		}
    	super.onStop();
    }
	    
	private void saveCurrentState(){
		if(mIsRandom){
			Log.d(TAG, " RNDDDDDDDDDDD SAVE STATE CLLEDDDDDDDDDDDDDDDD " + WCConstants.RAND_LETTER);
	    	Editor e = this.getPreferences(Context.MODE_PRIVATE).edit();
	    	e.putInt(WCConstants.RAND_LETTER_COUNTER + mCurrentLetter, mCounter);
	    	e.putString(WCConstants.RAND_LETTER + mCurrentLetter, mCurrentLetter);
	    	e.commit();
		}else{
			Log.d(TAG, " inorderrrrrrrrrrr SAVE STATE CLLEDDDDDDDDDDDDDDDD" + WCConstants.IN_ORDER_LETTER);
	    	Editor e = this.getPreferences(Context.MODE_PRIVATE).edit();
	    	e.putInt(WCConstants.IN_ORDER_LETTER_COUNTER + mCurrentLetter, mCounter);
	    	e.putString(WCConstants.IN_ORDER_LETTER + mCurrentLetter, mCurrentLetter);
	    	e.commit();
		}
	}
	
	/**
	 * Get the tag from the subclass.
	 */
	private void setTag(){
		this.TAG = getTag();
	}
	
	/**
	 * @return The tag to be used for the class
	 */
	protected abstract String getTag();
	
}
