package com.biodata.labguru.model;

public class PurchasableCollectionItem extends CollectionItem {
	
	/* public fields to make the asserts more readable*/
	public String manufacturer = "";
	public String catalogNum = "";
	public String unit = "";
	public String price = "";
	public String currency = "₪";//default currency
	public String webpage = "";

	public PurchasableCollectionItem(){
		super();
	}
	public PurchasableCollectionItem(String name, String manufacturer,
			String catalogNum, String unit, String price,String currency, String webpage,String owner) {
		super(name,owner);
		this.manufacturer = manufacturer;
		this.catalogNum = catalogNum;
		this.unit = unit;
		this.price = price;
		this.webpage = webpage;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public void setCatalogNum(String catalogNum) {
		this.catalogNum = catalogNum;
	}

	public void setUnits(String unit) {
		this.unit = unit;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setWebpage(String webpage) {
		this.webpage = webpage;
	}
}
