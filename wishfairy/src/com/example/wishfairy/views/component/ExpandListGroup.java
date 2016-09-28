package com.example.wishfairy.views.component;

import java.util.ArrayList;

public class ExpandListGroup {
 
	private String Name;
	private ArrayList<ExpandListChild> Items;
	private String subcat;

	public String getName() {
		return Name;
	}
	public void setName(String name) {
		this.Name = name;
	}
	public ArrayList<ExpandListChild> getItems() {
		return Items;
	}
	public void setItems(ArrayList<ExpandListChild> Items) {
		this.Items = Items;
	}
	public String getSubcat() {
		return subcat;
	}
	public void setSubcat(String name) {
		this.subcat = name;
	}
}
