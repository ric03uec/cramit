package com.dev.cramit.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * POJO for a Word
 * @author devashish
 *
 */
public class Word implements Parcelable{
	
	private static String TAG = "WORD_POJO";
	private int	wordId;
	private String word;
	private String meaning;
	private int rank;
	private int rootId;
	private int visitCount;
	

	public Word(Word original){
		this.wordId = original.getWordId();
		this.word 	= original.getWord();
		this.meaning = original.getMeaning();
		this.rank	= original.getRank();
		this.rootId	= original.getRootId();
		this.visitCount = original.getVisitCount();
	}
	
	public Word(int wordId, String word, String meaning, int rank, int rootId, int visitCount) {
		this.wordId = wordId;
		this.word = word;
		this.meaning = meaning;
		this.rank = rank;
		this.rootId = rootId;
		this.visitCount = visitCount;
	}
	
	public Word(Parcel source){
		/*
		 * Reconstruct the word from a Parcel
		 */
		wordId 	= source.readInt();
		word 	= source.readString();
		meaning = source.readString();
		rank 	= source.readInt();
		rootId	= source.readInt();
		visitCount = source.readInt();
	}
	
	//-----------getter for wordId--------------//
	public int getWordId(){
		return this.wordId;
	}
	
	//-----------getter/setter for word--------//
	public String getWord() {
		return word;
	}
	
	public void setWord(String word) {
		this.word = word;
	}
	
	//-----------getter/setter for meaning-----//
	public String getMeaning(){
		return this.meaning;
	}

	public void setMeaning(String meaning){
		this.meaning = meaning;
	}
	
	//-----------getter/setter for rank-------//
	public int getRank(){
		return this.rank;
	}
	
	public void setRank(int rank){
		this.rank = rank;
	}
	
	//-----------getter/setter for rootId-----//
	public int getRootId(){
		return this.rootId;
	}
	
	public void setRootId(int rootId){
		this.rootId = rootId;
	}
	
	//----------getter/setter for visiCount---//
	public int getVisitCount(){
		return this.visitCount;
	}
	
	public void setVisitCount(int visitCount){
		this.visitCount = visitCount;
	}
	
	public String toString() {
		return this.word + " ------> " + this.meaning;
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(wordId);
		dest.writeString(word);
		dest.writeString(meaning);
		dest.writeInt(rank);
		dest.writeInt(rootId);
		dest.writeInt(visitCount);
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

		public Word createFromParcel(Parcel source) {
			return new Word(source);
		}

		public Word[] newArray(int size) {
			return new Word[size];
		}
	};
}
