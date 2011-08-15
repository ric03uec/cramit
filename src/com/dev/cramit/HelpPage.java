package com.dev.cramit;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class HelpPage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.help_page);
		setHelpContent();
	}
	
	protected void setHelpContent(){
		TextView content = (TextView) findViewById(R.id.help_table_content);
		content.setText("");
		
		content.append(GETTING_STARTED);
		
		content.append("\n");
		
		content.append(ALPHABETICAL_ORDER);
		
		content.append("\n");
		
		content.append(ALPHABETICAL_RANDOM);
		
		content.append("\n");
		
		content.append(ALL_WORDS);
		
		content.append("\n");
		
		content.append(MARKED_WORDS);
	}
	
	private static final String GETTING_STARTED = "\n#GETTING STARTED : \n" +
			"\t Cram iT is an application that is designed for the users to who want to learn standard word lists, or just want to improve" +
			"their vocabulary. Below is the list of functionalities currently provided by the application. \n";
	
	private static final String ALPHABETICAL_ORDER = "\n#ALPHABETICAL (In Order): \n" +
			"\t In this mode, the user can select a Letter for which he/she wants to view the words for. The words will follow an lexicographic order" +
			"within the selected letter and will be repeated once all the words have been viewed." +
			"\n -Pressing BACK at any time gives the statistics" +
			"\n -Selecting a wrong option shows the answer and marks the same word at the same time.(Marked Words are explained below)"; 
	
	private static final String ALPHABETICAL_RANDOM = "\n#ALPHABETICAL (Random) : \n" +
			"\t In this mode the user can select a Letter for which he/she wants to view the words for. The words will NOT follow any order and will be randomly" +
			"shown from the list of words present for the selected letter. As with first option, list will be repeated once all words have been viewed." +
			"\n -Pressing MENU at any time will show 'RESET' and 'RANDOMISE' as two options." +
			"\n\t\t RESET : This will start the current list from beginning, without making any changes to it" +
			"\n\t\t RANDOMISE: This will reshuffle the words and their options, reform the list and start from the first word";
	
	private static final String ALL_WORDS = "\n#ALL WORDS : \n" +
			"\t In this mode, the complete word list is shown alphabetically." +
			"\n -Pressing MENU at any time will give an option to directly jump to the beginning of the selected letter in the word list." +
			"\n -Long Pressing on any word will ask if the user wants to mark that word or not. If marked, then the selected word goes into marked list." +
			"(Marked Words are explained below)";
	
	private static final String MARKED_WORDS = "\n#MARKED WORDS : \n" +
			"\t This will show the list of all the words for which wrong answer was selected by the user for first two modes or the words which the user" +
			"has explicitly marked in the ALL WORDS option by long press." +
			"\n -To remove a word from this list, long press that word and unmark it. The word will be removed when the list is viewed NEXT time.";
}
