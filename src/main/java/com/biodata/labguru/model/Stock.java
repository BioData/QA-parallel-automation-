package com.biodata.labguru.model;

import java.util.Date;



public class Stock {
	
	/* public fields to make the asserts more readable*/
	public String type;
	public String name;
	public String content;
	public String location;
	public String description;
	public String units;
	public String volume;
	public String concentration;
	public String lot;
	public String color;
	public Date expiryDate;
	public String barcode;
	public String storedBy;
	public Date storedOn;

	public void setType(String type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public void setConcentration(String concentration) {
		this.concentration = concentration;
	}
	
	public void setLot(String lot) {
		this.lot = lot;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public void setStoredBy(String storedBy) {
		this.storedBy = storedBy;
	}

	public void setStoredOn(Date storedOn) {
		this.storedOn = storedOn;
	}
}
