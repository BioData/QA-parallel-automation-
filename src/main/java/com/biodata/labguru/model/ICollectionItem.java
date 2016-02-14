package com.biodata.labguru.model;

public interface ICollectionItem {

	public void setName(String name) ;
	
	public void setDescription(String description);

	public void setOwner(String owner) ;

	public void setCreatedAt(String createdAt);
	
	@Override
	public boolean equals(Object obj);
}
