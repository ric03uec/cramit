package com.dev.cramit;

import java.util.ArrayList;
import java.util.List;

import com.dev.cramit.models.Answer;
import com.dev.cramit.models.Problem;
import com.dev.cramit.models.Word;
import com.dev.cramit.utils.WCConstants;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Display the UI of the problem and return the result
 * @author devashish
 *
 */
public class ProblemView extends Activity {
	
	protected static final int RESET_ID			= 1;
	protected static final int RANDOMIZE_ID		= 2;
	protected static String TAG = "Problem View";
	
	private boolean				isRandomWLProblem;
	private RadioButton 		activeRadioButton;
	private Problem 			currentProblem;
	private ArrayList<Word> 	optionsList;
	
	private TextView			problemTextView;
	private TextView 			optionOneTextView;
	private TextView 			optionTwoTextView;
	private TextView 			optionThreeTextView;
	private TextView 			optionFourTextView;
	private TextView 			optionFiveTextView;
	
	
	private OnClickListener onRadioButtonClick = new OnClickListener(){
		public void onClick(View v) {
			final RadioButton rb = (RadioButton) v;
			if(activeRadioButton != null){
				activeRadioButton.setChecked(false);
			}
			rb.setChecked(true);
			activeRadioButton = rb;
		}
	};
	
	private OnClickListener onShowAnswerButtonClick = new OnClickListener() {
		public void onClick(View v) {
			showAnswerDialog();
		}
	};
	
	private OnClickListener onNextButtonClick = new OnClickListener() {
		public void onClick(View v) {
			
			if(null == activeRadioButton){
				showSelectOptionAlert();
			}else{
				int optionOneId 	= ((RadioButton) findViewById(R.id.optionOneRadioButton)).getId();
				int optionTwoId 	= ((RadioButton) findViewById(R.id.optionTwoRadioButton)).getId();
				int optionThreeId 	= ((RadioButton) findViewById(R.id.optionThreeRadioButton)).getId();
				int optionFourId 	= ((RadioButton) findViewById(R.id.optionFourRadioButton)).getId();
				int optionFiveId	= ((RadioButton) findViewById(R.id.optionFiveRadioButton)).getId();
				
				Word answer = null;
				if(activeRadioButton.getId() == optionOneId){
					answer = optionsList.get(0);
				}else if(activeRadioButton.getId() == optionTwoId){
					answer = optionsList.get(1);
				}else if(activeRadioButton.getId() == optionThreeId){
					answer = optionsList.get(2);
				}else if(activeRadioButton.getId() == optionFourId){
					answer = optionsList.get(3);
				}else if(activeRadioButton.getId() == optionFiveId){
					answer = optionsList.get(4);
				}
				if(isCorrectAnswer(answer)){
					returnResult(answer, false);
				}else{
					showWrongAnswerAlert(answer);
				}
			}
		}
	};
	
	/**
	 * Check if the 'answer' is the correct answer to the current question
	 * @param answer
	 * @return
	 */
	protected boolean isCorrectAnswer(Word answer){
		if(answer.getWord().trim().equals(currentProblem.getQuestion().getWord().trim())){
			return true;
		}
		return false;
	}
	
	protected void returnResult(Word answer, boolean isShowAnswer){

		Intent resultIntent = new Intent();
		resultIntent.putExtra("REQUEST_CODE", "requestCode");
		
		Answer returnAnswer = new Answer();
		returnAnswer.setAnswer(answer);
		
		if(answer.getWord().trim().equals(currentProblem.getQuestion().getWord().trim())){
			returnAnswer.setIsCorrect(WCConstants.WORD_ANSWER_TRUE);
		}else{
			returnAnswer.setIsCorrect(WCConstants.WORD_ANSWER_FALSE);
		}
		
		resultIntent.putExtra("answer", returnAnswer);
		if(isShowAnswer){
			resultIntent.putExtra("isShowAnswer", WCConstants.SHOW_ANSWER_TRUE);
		}
		
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}
	
	protected void showWrongAnswerAlert(final Word wrongAnswer){
		AlertDialog alert = new AlertDialog.Builder(ProblemView.this).create();
		alert.setTitle("Wrong Answer !!!");
		alert.setMessage("Correct answer is : " + currentProblem.getQuestion().getMeaning());
		alert.setButton("OK", new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int which) {
			      returnResult(wrongAnswer, false);
			   }
		});
		alert.show();
	}
	
	protected void showSelectOptionAlert(){
		AlertDialog alert = new AlertDialog.Builder(ProblemView.this).create();
		alert.setTitle("No Choice Selected");
		alert.setMessage("Please Select One Choice as an Answer");
		alert.setButton("OK", new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int which) {
			      // here you can add functions
			   }
		});
		alert.show();
	}
	
	protected void showAnswerDialog(){
		AlertDialog alert = new AlertDialog.Builder(ProblemView.this).create();
		alert.setTitle(currentProblem.getQuestion().getWord());
		alert.setMessage(currentProblem.getQuestion().getMeaning());
		alert.setButton("OK", new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int which) {
			      returnResult(currentProblem.getQuestion(), true);
			   }
		});
		alert.show();
	}
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.problem_layout);

		
		isRandomWLProblem = getIntent().getBooleanExtra(WCConstants.IS_RANDOM_WL, false);	
		if(!isRandomWLProblem){
			setTitle(WCConstants.PROBLEM_TITLE_IN_ORDER);
		}else{
			setTitle(WCConstants.PROBLEM_TITLE_RANDOM);
		}
		
		groupRadioButtons();
		
		addListeners();
		
		initializeTextViews();
		
		setOptionsText(getIntent());
		
	}
	
	protected void addListeners(){
		Button nextButton = (Button)findViewById(R.id.nextButton);
		nextButton.setOnClickListener(onNextButtonClick);
		
		Button showAnswerButton = (Button)findViewById(R.id.answerButton);
		showAnswerButton.setOnClickListener(onShowAnswerButtonClick);
	}

	protected void initializeTextViews(){
		
		optionOneTextView 	= (TextView) findViewById(R.id.optionOneText);
		optionTwoTextView  	= (TextView) findViewById(R.id.optionTwoText);
		optionThreeTextView = (TextView) findViewById(R.id.optionThreeText);
		optionFourTextView	= (TextView) findViewById(R.id.optionFourText);
		optionFiveTextView	= (TextView) findViewById(R.id.optionFiveText);
	}
	
	protected void setOptionsText(Intent intent){
		
		currentProblem = intent.getParcelableExtra("problem");
		optionsList = new ArrayList<Word>();
		
		problemTextView = (TextView) findViewById(R.id.problemText);
		problemTextView.setText(currentProblem.getQuestion().getWord().toUpperCase());
	
		optionsList.add(currentProblem.getAtIndex(0));
		optionOneTextView.setText(processMeaningForWord(currentProblem.getAtIndex(0)));
		
		optionsList.add(currentProblem.getAtIndex(1));
		optionTwoTextView.setText(processMeaningForWord(currentProblem.getAtIndex(1)));
		
		optionsList.add(currentProblem.getAtIndex(2));
		optionThreeTextView.setText(processMeaningForWord(currentProblem.getAtIndex(2)));
		
		optionsList.add(currentProblem.getAtIndex(3));
		optionFourTextView.setText(processMeaningForWord(currentProblem.getAtIndex(3)));
		
		optionsList.add(currentProblem.getAtIndex(4));
		optionFiveTextView.setText(processMeaningForWord(currentProblem.getAtIndex(4)));
		
	}
	
	/**
	 * Return only one meaning out of the list for the given word. Randomise and choose one meaning if
	 * multiple meanings exist
	 * @param word - Word whose meanings are to be truncated
	 * @return single meaning of the word
	 */
	protected String processMeaningForWord(Word word){
		String [] meaningsList = word.getMeaning().split(";");
		List<String> meaningsListString = new ArrayList<String>();
		List<String> skippedWordsList = new ArrayList<String>();
		skippedWordsList.add("CF.");
		skippedWordsList.add("EX.");
		skippedWordsList.add("N:");
		skippedWordsList.add("N.");
		skippedWordsList.add("V.");
		skippedWordsList.add("V:");
		skippedWordsList.add("ADJ.");
		skippedWordsList.add("ADV.");
		skippedWordsList.add("OP.");
		skippedWordsList.add("ADJ:");
		skippedWordsList.add("SG.");
		
		for(String meaning : meaningsList){
			meaning = meaning.trim();
			meaning = meaning.toLowerCase();
			boolean canAddMeaning = true;
			for(String skippedWord : skippedWordsList){
				skippedWord = skippedWord.toLowerCase();
				/*
				 * If the meaning contains any of the Metadata characters which have to be skipped, then
				 * do not add that meaning in the meanings list
				 */
				if(meaning.contains(skippedWord)){
					canAddMeaning = false;
					break;
				}
			}
			
			/*
			 * If the meaning contains an instance or occurence of the word corresponding to that meaning, \
			 * then do not add that meaning in its list. We wouldn't want to make things too easy, would we :P
			 */
			if(meaning.contains(word.getWord())){
				canAddMeaning = false;
			}
			
			/*
			 * If the Word contains the instance of any word in its meanings, then that meaning is not 
			 * inserted into the meaning list.
			 */
			String [] wordsInMeaning = meaning.split(" ");
			for(String wordInMeaning : wordsInMeaning){
				if(word.getWord().contains(wordInMeaning)){
					canAddMeaning = false;
					break;
				}
			}
			
			if(canAddMeaning){
				meaningsListString.add(meaning);
			}
		}
		
		Integer randomMeaningIndex = 0;
		if(meaningsListString.size() != 0){
			randomMeaningIndex = (int) ((Math.random() * 10) % meaningsListString.size());
			return meaningsListString.get(randomMeaningIndex);
		}
		
		return meaningsList[0];
	}
	
	protected void groupRadioButtons(){
		RadioButton optionOneButton = (RadioButton) findViewById(R.id.optionOneRadioButton);
		optionOneButton.setOnClickListener(onRadioButtonClick);
		
		RadioButton optionTwoButton = (RadioButton) findViewById(R.id.optionTwoRadioButton);
		optionTwoButton.setOnClickListener(onRadioButtonClick);
		
		RadioButton optionThreeButton = (RadioButton) findViewById(R.id.optionThreeRadioButton);
		optionThreeButton.setOnClickListener(onRadioButtonClick);
		
		RadioButton optionFourButton = (RadioButton) findViewById(R.id.optionFourRadioButton);
		optionFourButton.setOnClickListener(onRadioButtonClick);
		
		RadioButton optionFiveButton = (RadioButton) findViewById(R.id.optionFiveRadioButton);
		optionFiveButton.setOnClickListener(onRadioButtonClick);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, RESET_ID, 0, R.string.reset_list);
        if(isRandomWLProblem)
        menu.add(2, RANDOMIZE_ID, 0, R.string.random_list);
        return result;
	}
	
	 public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
	        case RESET_ID:{
	    		setResult(WCConstants.RESULT_RESET_LIST);
	    		finish();
	    		return true;
    		}
	        case RANDOMIZE_ID:{
	        	setResult(WCConstants.RESULT_RANDOMIZE_LIST);
	        	finish();
	        	return true;
	        }
        }
	    return super.onOptionsItemSelected(item);
	}
	  
}
