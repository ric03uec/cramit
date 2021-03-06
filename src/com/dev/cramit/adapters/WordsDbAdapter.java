package com.dev.cramit.adapters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.dev.cramit.utils.WCConstants;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Database helper class 
 * @author devashish
 *
 */
public class WordsDbAdapter {
	
	private static final String TAG = "WordsDbAdapter";
	private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    private final Context ctx;
    
    public WordsDbAdapter(Context ctx){
    	this.ctx = ctx;
    }
    
    /**
     * Create or open an instance of DB adapter and return the same
     * @param isFirstInstance - True if DB has to be copied from assets folder or not. This will happen only once, after which,
     * the copied database will be used.
     * @return
     */
    public WordsDbAdapter open(boolean isFirstInstance){
    	Log.d(TAG, "IS first run instance " + isFirstInstance);
    	mDbHelper = new DatabaseHelper(ctx, isFirstInstance);
    	WCConstants.isFirstRunInstance = false;
    	mDb = mDbHelper.getWritableDatabase();
    	return this;
    }
    
    public void close(){
    	mDbHelper.close();
    	mDb.close();
    }
    
    
    public boolean isOpen(){
    	return mDb.isOpen();
    }
    /**
     * Get all the words from database
     * @return Cursor containing all the words from database.
     */
    public Cursor getWords(){
    	return mDb.query("word_list", 
    			new String[] {"_id", "word", "meaning", "word_usage", "rank"}, 
    			null, null, null, null, null);
    }
    
    /**
     * Get all the words with the specified rank
     * @param rank
     * @return
     */
    public Cursor getWordsWithRank(int rank){
    	return mDb.query("word_list", 
    			new String[] {"_id", "word", "meaning", "word_usage", "rank"}, 
    			"rank=" + Integer.toString(rank) , null, null, null, null);
    }
    
    /**
     * Update the rank of the word with the specified value
     * @param wordId
     * @param rank
     */
    public void updateWordRank(int wordId, int rank){
    	Log.d(TAG, "update word rank " + wordId + "    " + rank);
    	
    	ContentValues cv = new ContentValues();
    	cv.put("rank", rank);
    	mDb.update("word_list", cv, "_id" + "=" + wordId, null);
    }
    
    /**
     * Get all the words for the specified letter
     * @param letter
     * @return
     */
    public Cursor getWordsForLetter(String letter){
    	Cursor cursor = mDb.query("word_list", 
    					new String[] {"_id", "word", "meaning", "word_usage", "rank"}, "word like '" + (letter.trim().toLowerCase()) + "%'", 
    					null, null, null, null);
    	return cursor;
    }
    
    /**
     * Get the word with the specified ID
     * @param wordId
     * @return
     */
    public Cursor getWordWithId(int wordId){
    	return mDb.query("word_list", 
    			new String[] {"_id", "word", "meaning", "word_usage", "rank"}, 
    			"_id=" + Integer.toString(wordId) , null, null, null, null);
    }
    
    //-------------------------------------------------------------------//
    /**
     * Nested class which actually interacts with the database.
     * @author devashish
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {

    	private static String DB_PATH = "/data/data/com.dev.cramit/databases/";
    	
    	/**
    	 * Have to name the database as one of the extensions that are NOT compressed during the compression
    	 * of the APK. If it is compressed, then it has to be split and then joined before it can 
    	 * be used at the device.
    	 */
    	private static String DB_NAME = "CramIt.png";
    	
    	private SQLiteDatabase myDataBase; 
    	private final Context myContext;

        DatabaseHelper(Context context, boolean isFirsRunInstance) {
            super(context, DB_NAME, null, 2);
            this.myContext = context;
            
            if(!isFirsRunInstance){
            	openDataBase();
            	return;
            }else{
            	try {
    				createDataBase();
    			} catch (IOException e) {
    				throw new Error("Unable to create database...");
    			}
            }
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
//        	Log.d(TAG, "Creating Database from 'onCreate()' ...");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            
//            onCreate(db);
        }
        
        /**
         * Creates a empty database on the system and rewrites it with your own database.
         */
        public void createDataBase() throws IOException{
        	
        	checkAndDeleteDatabase();
        	
    		//By calling this method an empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
        	this.close();
        	
        	try {
    			copyDataBase();
    		} catch (IOException e) {
        		throw new Error("Error copying database");
        	}
        	
        	openDataBase();
        	
        }
        
        /**
         * Copies your database from your local assets-folder to the just created empty database in the
         * system folder, from where it can be accessed and handled.
         * This is done by transfering bytestream.
         * */
        private void copyDataBase() throws IOException{
     
        	try{
	        	//Open your local db as the input stream
	        	InputStream myInput = myContext.getAssets().open(DB_NAME);
	     
	        	// Path to the just created empty db
	        	String outFileName = DB_PATH + DB_NAME;
	        	
	        	Log.d(TAG, "Copying database from the 'assets' folder into " + outFileName);
	     
	        	//Open the empty db as the output stream
	        	OutputStream myOutput = new FileOutputStream(outFileName);
	     
	        	//transfer bytes from the inputfile to the outputfile
	        	Log.d(TAG, "DB file created... now copying..");
	        	byte[] buffer = new byte[1024];
	        	int length;
	        	while ((length = myInput.read(buffer))>0){
	        		myOutput.write(buffer, 0, length);
	        	}
	        	Log.d(TAG, "db copied... closing streams..");
	        	
	        	//Close the streams
	        	myOutput.flush();
	        	myOutput.close();
	        	myInput.close();
        	}catch (IOException e){
        		Log.d(TAG, "Error while copying database");
        	}
        }

        /**
         * Check whether a database already exists or not in the application data folder. If it exists, then delete it.
         * Do it everytime the application is deployed
         * @return
         */
        private boolean checkAndDeleteDatabase(){
    		String myPath = DB_PATH + DB_NAME;
    		File f = new File(myPath);
    		if(f.exists()){
    			Log.d(TAG, "Database found in the application folder... Now deleting it...");
    			if(f.delete()){
    				Log.d(TAG, "Database successfully deleted from the application folder...");
    			}else{
    				Log.d(TAG, "Error Deleting database...");
    			}
    			return true;
    		}
        	return false;
        }
        
        /**
         * Open the Database which exists in the assets folder
         * @throws SQLException
         */
        public void openDataBase() throws SQLException{
        	if(null == myDataBase){
        		Log.d(TAG, "opening database..." + DB_PATH + DB_NAME);
        		String myPath = DB_PATH + DB_NAME;
        		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
            }else{
            	Log.d(TAG, "Database already created... opening existing instance");
            }
        }
     
    	public synchronized void close() {
        	    if(myDataBase != null)
        		    myDataBase.close();
        	    super.close();
    	}
    }
    //------------------------database class ends----------------------------------------------//
}
