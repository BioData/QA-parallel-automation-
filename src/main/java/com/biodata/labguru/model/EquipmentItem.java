package com.biodata.labguru.model;


public class EquipmentItem {

	/** public fields to make the asserts more readable*/
	public String name;
	public String owner = "";
	public String equipmentType;
	public String manufacturer;
	public String location;
	public String serialNumber;
	public String modelNumber;
	public String purchaseDate;
	public String warrantyExpirationDate;
	public String maintenanceDate;
	public String maintenanceInformation;
	public String description;
	
	
	//default constractor
	public EquipmentItem() {
	}
	
	public EquipmentItem(String name,String owner) {
		this.name = name;
		this.owner = owner;
		this.description = name;
	}
	

	public void setName(String name) {
		this.name = name;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public void setEquipmentType(String equipmentType) {
		this.equipmentType = equipmentType;
	}
	
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}
	
	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	
	public void setWarrantyExpirationDate(String warrantyExpirationDate) {
		this.warrantyExpirationDate = warrantyExpirationDate;
	}
	
	public void setMaintenanceDate(String maintenanceDate) {
		this.maintenanceDate = maintenanceDate;
	}
	
	public void setMaintenanceInformation(String maintenanceInformation) {
		this.maintenanceInformation = maintenanceInformation;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}
