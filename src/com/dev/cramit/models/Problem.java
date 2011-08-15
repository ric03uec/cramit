package com.dev.cramit.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * This defines one problem, which is a word and a collection of meanings that will appear as options for it
 * and the correct answer.
 * @author devashish
 *
 */
public class Problem implements Parcelable{
	
	protected static String TAG = "PROBLEM_POJO";
	protected Word question;
	protected Word optionOne;
	protected Word optionTwo;
	protected Word optionThree;
	protected Word optionFour;
	protected Word optionFive;
	
	List<Word> optionsList = new ArrayList<Word>();
	
	public Problem(Parcel source){
		
		question 	= source.readParcelable(Word.class.getClassLoader());
		optionOne 	= source.readParcelable(Word.class.getClassLoader());
		optionTwo = source.readParcelable(Word.class.getClassLoader());
		optionThree 	= source.readParcelable(Word.class.getClassLoader());
		optionFour 	= source.readParcelable(Word.class.getClassLoader());
		optionFive 	= source.readParcelable(Word.class.getClassLoader());
		
		optionsList.add(optionOne);
		optionsList.add(optionTwo);
		optionsList.add(optionThree);
		optionsList.add(optionFour);
		optionsList.add(optionFive);
	}
	
	
	/**
	 * Constructor. Provide the word to be used as the question and the options for it. The fifth option is set as the word itself
	 * @param question
	 * @param optionOne
	 * @param optionTwo
	 * @param optionThree
	 * @param optionFour
	 */
	public Problem(Word question, Word optionOne, Word optionTwo, Word optionThree, Word optionFour){
		if(!this.optionsList.isEmpty())
			this.optionsList.clear();
		
		this.question 	= question;
		
		this.optionOne 	= optionOne;
		optionsList.add(this.optionOne);
		
		this.optionTwo 	= optionTwo;
		optionsList.add(this.optionTwo);
		
		this.optionThree= optionThree;
		optionsList.add(this.optionThree);
		
		this.optionFour	= optionFour;
		optionsList.add(this.optionFour);
		
		this.optionFive = question;
		optionsList.add(this.optionFive);
		
		randomizeOptions();
	}
	
	private void randomizeOptions(){
		HashMap<Integer, Word> optionsMap = new HashMap<Integer, Word>(); 
		
		for(int i = 0 ; i < optionsList.size() ; i++){
			while(true){
				int rand = (int)((Math.random() * 10) % 5);
				if(optionsMap.containsKey(rand)){
					continue;
				}else{
					optionsMap.put(rand, optionsList.get(i));
					break;
				}
			}
		}
		optionsList.clear();
		
		for(int i = 0 ; i < 5; i++){
			while(true){
				int index = (int) ((Math.random() * 10) % 5);
				if(optionsMap.containsKey(index) && (optionsMap.get(index) != null)){
					this.optionsList.add(optionsMap.get(index));
					optionsMap.remove(index);
					break;
				}
			}
		}
		
	}
	
	/**
	 * Gives the word which is set as the question for this problem.
	 * @return question word
	 */
	public Word getQuestion(){
		return this.question;
	}
	
	/**
	 * Get the list of all the words in this problem. The word at first index is the question for this problem
	 * @return optionsList
	 */
	public List<Word> getOptionsList(){
		return this.optionsList;
	}
	
	/**
	 * Return word at the specified index. Index begins from 0. First word is the value of the problem
	 * Rest four are the options
	 * @param index
	 * @return
	 */
	public Word getAtIndex(int index){
		if(optionsList.get(index) != null)
			return optionsList.get(index);
		return null;
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(question, flags);
		dest.writeParcelable(optionsList.get(0), flags);
		dest.writeParcelable(optionsList.get(1), flags);
		dest.writeParcelable(optionsList.get(2), flags);
		dest.writeParcelable(optionsList.get(3), flags);
		dest.writeParcelable(optionsList.get(4), flags);
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

		public Problem createFromParcel(Parcel source) {
			return new Problem(source);
		}

		public Problem[] newArray(int size) {
			return new Problem[size];
		}
	};
}
