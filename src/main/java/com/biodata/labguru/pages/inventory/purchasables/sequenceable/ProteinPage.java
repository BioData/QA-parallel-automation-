package com.biodata.labguru.pages.inventory.purchasables.sequenceable;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;


public class ProteinPage extends SequenceableCollectionPage{

	
	
	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	@Override
	protected String getImportXPath() {
		
		return ".//*[@id='main-content']/div/div[1]/a[@href='/system/imports/new?class=Biocollections%3A%3AProtein']";
	}

	@Override
	protected String getFileNameToImport() {
		return LGConstants.PROTEINS_TEMPLATE;
	}
	
	@Override
	protected String getCollectionName() {
		
		return LGConstants.PROTEIN_PREFIX;
	}
	
	@Override
	public List<String> getAvailableColumnsForCustomiseTableView() {
		List<String> columns = super.getAvailableColumnsForCustomiseTableView();
		columns.add("preferences_auto_name");//sysid
		columns.add("preferences_name");//name
		columns.add("preferences_gene");
		columns.add("preferences_species");
		columns.add("preferences_mutations");
		columns.add("preferences_chemical_modifications");
		columns.add("preferences_tag");
		columns.add("preferences_mw");
		columns.add("preferences_purification_method");
		columns.add("preferences_extinction_coefficient_280nm");
		columns.add("preferences_storage_buffer");
		columns.add("preferences_storage_temperature");
	
		return columns;
	}

	@Override
	protected void addItemWithGivenName(String name) {
		clickNewButton("new_protein");
	      
        WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated
        		(By.id("name")));
        sendKeys(txtName, name);
		
	}

	@Override
	protected String getEditCollectionPrefix() {
		return "edit_biocollections_protein_";
	}

}
