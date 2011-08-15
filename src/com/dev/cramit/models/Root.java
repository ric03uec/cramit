package com.dev.cramit.models;

/**
 * POJO for word root
 * @author devashish
 *
 */
public class Root {
	private int rootId;
	private String root;
	private String rootMeaning;
	
	public Root(String root, String rootMeaning){
		this.root = root;
		this.rootMeaning = rootMeaning;
	}
	
	//------getter for rootId----------------//
	public int getRootId(){
		return this.rootId;
	}
	
	//-------getter/setter for root----------//
	public String getRoot(){
		return this.root;
	}
	
	public void setRoot(String root){
		this.root = root;
	}
	
	//---------getter/setter for root meaning-----//
	public String getRootMeaning(){
		return this.rootMeaning;
	}
	
	public void setRootMeaning(String rootMeaning){
		this.rootMeaning = rootMeaning;
	}
}
