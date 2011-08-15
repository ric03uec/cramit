package com.dev.cramit.models;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * POJO for an Answer that is returned.
 * @author devashish
 *
 */
public class Answer implements Parcelable {

	protected String TAG	= "ANSWER_POJO";
	protected Word 	answer;
	protected String isCorrect;
	
	
	public Answer(){};
	
	public Answer(Word answer, String isCorrect){
		this.answer 	= answer;
		this.isCorrect 	= isCorrect;
	}
	
	public Answer(Parcel source){
		/**
		 * WORKAROUND: could not figure out how to retreive boolean from parcel, so using string
		 * as true/false value for isCorrect
		 */
		answer 		= source.readParcelable(Word.class.getClassLoader());
		isCorrect	= source.readString();
	}
	
	//--------------------getter/setter for answer------------------//
	public Word getAnswer(){
		return (Word) this.answer;
	}
	
	public void setAnswer(Word answer){
		this.answer = answer;
	}
	
	//--------------------getter/setter for isCOrrect--------------//
	public String isCorrect(){
		return (String) this.isCorrect;
	}
	
	public void setIsCorrect(String isCorrect){
		this.isCorrect = isCorrect;
	}
	
	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.answer, flags);
		dest.writeString(this.isCorrect);
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

		public Answer createFromParcel(Parcel source) {
			return new Answer(source);
		}

		public Answer[] newArray(int size) {
			return new Answer[size];
		}
	};
}
