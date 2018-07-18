package com.riseintech.spulla.utils;

import java.util.ArrayList;

/**
 * This code encapsulates RSS item data.
 * Our application needs title and link data.
 * 
 * @author ITCuties
 *
 */
public class SoppingItem {

	private ArrayList<String> powers;
	// item Category
	private String categoryName;
	// item Id
	private int catId;
	// item link
	private String link;

	public int getCatId() {
		return catId;
	}

	public void setCatId(int title) {
		this.catId = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String cat) {
		this.categoryName = cat;
	}

	public ArrayList<String> getPowerscat() {
		return powers;
	}

	public void setPowerscat(ArrayList<String> powers) {
		this.powers = powers;
	}

	/*@Override
	public String toString() {
		return title;
	}*/
	
}
