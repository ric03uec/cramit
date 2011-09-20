package com.dev.cramit;


import com.dev.cramit.adapters.MainMenuAdapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

/**
 * 
 * @author devashish
 *
 */
public class Init extends ListActivity {
    /** Called when the activity is first created. */
	public static final String TAG 	= "INIT";
	
	public static final String SELECTED_LETTER = "";
	protected static final String LETTER_A 	= "A for Aghast";
	protected static final String LETTER_B 	= "B for Bludgeon";
	protected static final String LETTER_C 	= "C for Colloquy";
	protected static final String LETTER_D 	= "D for Despotism";
	protected static final String LETTER_E 	= "E for Exonerate";
	protected static final String LETTER_F 	= "F for Fetish";
	protected static final String LETTER_G 	= "G for Garrulity";
	protected static final String LETTER_H 	= "H for Hogshead";
	protected static final String LETTER_I 	= "I for Iniquitous";
	protected static final String LETTER_J 	= "J for Jingoism";
	protected static final String LETTER_K 	= "K for Knavery";
	protected static final String LETTER_L 	= "L for Lackadaisical";
	protected static final String LETTER_M 	= "M for Mellifluous";
	protected static final String LETTER_N 	= "N for Nefarious";
	protected static final String LETTER_O 	= "O for Obsequious";
	protected static final String LETTER_P 	= "P for Perfunctory";
	protected static final String LETTER_Q 	= "Q for Quintessence";
	protected static final String LETTER_R 	= "R for Rescind";
	protected static final String LETTER_S 	= "S for Stentorian";
	protected static final String LETTER_T 	= "T for Tutelage";
	protected static final String LETTER_U 	= "U for Umbrage";
	protected static final String LETTER_V 	= "V for Vie";
	protected static final String LETTER_W 	= "W for Wont";
	protected static final String LETTER_X 	= "X for Xanadu";
	protected static final String LETTER_Y 	= "Y for Yachter";
	protected static final String LETTER_Z 	= "Z for Zephyr";
	
	protected static final int PREFERENCE_ID	  = 4;
	protected static Integer LETTER_SELECT_DIALOG = 1;
	protected static Integer TEST_BEGIN_DIALOG	  = 2;
	private static final int LOADING_DIALOGUE_CODE = 3;
	
	protected static boolean isRandomWL		= false;
	
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
       	Log.d(TAG,"           new activity Createddd.....................");
   		String [] opts = getResources().getStringArray(R.array.mainMenu);
   		this.setListAdapter(new MainMenuAdapter(this, R.id.menu, opts));
   		getListView().setTextFilterEnabled(true);
    }
    
    
    @Override
    protected void onResume() {
    	super.onResume();
    	/**
    	 * Remove any pending Loading dialogues
    	 */
    	removeDialog(LOADING_DIALOGUE_CODE);
    }
    /**
     * Decide what action to be taken on the basis of option selected from main menu
     */
    protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if(3 == position){
			Bundle bundle = new Bundle();
//			bundle.putBoolean(WCConstants.IS_RANDOM_WL, false);
			isRandomWL = false;
			showDialog(LETTER_SELECT_DIALOG);
		}else if(5 == position){
			Bundle bundle = new Bundle();
//			bundle.putBoolean(WCConstants.IS_RANDOM_WL, true);
			isRandomWL = true;
			showDialog(LETTER_SELECT_DIALOG);
		}else if(7 == position){
			showDialog(LOADING_DIALOGUE_CODE);
			Intent intent = new Intent(Init.this, AllWordsList.class);
			startActivity(intent);
		}else if(9 == position){
			showDialog(LOADING_DIALOGUE_CODE);
			Intent intent = new Intent(Init.this, MarkedWordsList.class);
			startActivity(intent);
		}else if(11 == position){
			Intent intent = new Intent(Init.this, HelpPage.class);
			startActivity(intent);
		}
		/*else if(11 == position){
			showDialog(TEST_BEGIN_DIALOG);
		}*/
	}
    
    @Override
    protected void onStop() {
    	super.onStop();
    }
    
    protected Dialog onCreateDialog(int id) {
    	if(id == LETTER_SELECT_DIALOG){
	    	final CharSequence[] items = {LETTER_A, LETTER_B, LETTER_C, LETTER_D, LETTER_E, LETTER_F, LETTER_G, LETTER_H, LETTER_I,
	    								LETTER_J, LETTER_K, LETTER_L,LETTER_M, LETTER_N, LETTER_O,LETTER_P, LETTER_Q, LETTER_R, LETTER_S,
	    								LETTER_T, LETTER_U, LETTER_V, LETTER_W, LETTER_X, LETTER_Y, LETTER_Z};
	    	
	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	builder.setTitle("Select a Letter");
	    	builder.setItems(items, new DialogInterface.OnClickListener() {
	    		
			    public void onClick(DialogInterface dialog, int item) {
//			    	dialog.cancel();
			    	showDialog(LOADING_DIALOGUE_CODE);
			    	/*
			    	 * FIXME: Somehow, message is not being passed in the bundle, so a global variable is
			    	 * used for denoting whether the current list is random or not.
			    	 * FIXME: Dialoge box effects the restoration of application to previous state
			    	 */
//			    	boolean isRandom = bundle.getBoolean(WCConstants.IS_RANDOM_WL);
			    	boolean isRandom = isRandomWL;
			    	if(isRandom){
			    		Intent options = new Intent(Init.this, WListByAlphabetRnd.class);
				    	options.setAction((items[item]).toString());
				    	startActivity(options);
			    	}else{
				    	Intent options = new Intent(Init.this, WListByAlphabetOrd.class);
				    	options.setAction((items[item]).toString());
				    	startActivity(options);
			    	}
			    }
			});
	    	AlertDialog alert = builder.create();
	    	
	    	return alert;
    	}else if(id == TEST_BEGIN_DIALOG){
    		final CharSequence[] options = {"Easy", "Not So Easy"};
    		
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	builder.setTitle("Select Mode");
	    	builder.setItems(options, new DialogInterface.OnClickListener() {
	    		
			    public void onClick(DialogInterface dialog, int item) {
			    	Intent intent = new Intent(Init.this, TestMe.class);
			    	intent.setAction((options[item]).toString());
			    	startActivity(intent);
			    }
			});
	    	AlertDialog alert = builder.create();
	    	return alert;
    	}else if(id == LOADING_DIALOGUE_CODE){
    		ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Loading Words... Please Wait...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            return dialog;
    	}
    	return null;
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, PREFERENCE_ID, 0, R.string.preferences);
        return result;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
	        case PREFERENCE_ID:
	        	Intent preferences = new Intent(Init.this, Preferences.class);
	        	startActivity(preferences);
	        	
	        return true;
        }
	    return super.onOptionsItemSelected(item);
	}
    
}