package com.biodata.labguru.model;

public class BotanyItem extends PurchasableCollectionItem{
	
	public String genotype = "";
	public String phenotype = "";
	public String generation = "F0";
	
	public BotanyItem(){
		super();
	}
	
	public BotanyItem(String name, String manufacturer,
			String catalogNum, String unit, String price,String currency, String webpage,String owner) {
		super(name, manufacturer, catalogNum, unit, price, currency, webpage, owner);
	}
	
	public void setGenotype(String genotype){
		this.genotype = genotype;
	}
	
	public void setPhenotype(String phenotype){
		this.phenotype = phenotype;
	}

	public void setGeneration(String generation){
		this.generation = generation;
	}
}
