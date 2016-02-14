package com.biodata.labguru.model;

public class CollectionItem implements ICollectionItem{

	/* public fields to make the asserts more readable*/
	public String name = "";
	public String owner = "";
	public String createdAt = "";
	public String description = "";

	//default constractor
	public CollectionItem() {
	}
	
	public CollectionItem(String name,String owner) {
		this.name = name;
		this.owner = owner;
		this.description = name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
}
