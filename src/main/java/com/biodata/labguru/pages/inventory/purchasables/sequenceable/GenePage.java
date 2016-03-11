package com.biodata.labguru.pages.inventory.purchasables.sequenceable;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;


public class GenePage extends SequenceableCollectionPage{

	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	@Override
	protected String getImportXPath() {
		
		return ".//*[@id='main-content']/div/div[1]/a[@href='/system/imports/new?class=Biocollections%3A%3AGene']";
	}

	@Override
	protected String getFileNameToImport() {
		return LGConstants.GENES_TEMPLATE;
	}
	
	@Override
	protected String getCollectionName() {
		
		return LGConstants.GENE_PREFIX;
	}
	
	@Override
	protected boolean isPurchasableEnabled() {
		
		return false;
	}
	
	@Override
	public List<String> getAvailableColumnsForCustomiseTableView() {
		List<String> columns = super.getAvailableColumnsForCustomiseTableView();
		columns.add("preferences_auto_name");//sysid
		columns.add("preferences_title");//name
		columns.add("preferences_expression_location");
		columns.add("preferences_pathway");
		columns.add("preferences_primers");
		
		columns.remove("preferences_source");
		columns.remove("preferences_location");
		return columns;
	}

	@Override
	protected void addItemWithGivenName(String name) {
		clickNewButton("new_gene");
	      
        WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated
        		(By.id("title")));
        sendKeys(txtName, name);
		
	}


	@Override
	protected String getEditCollectionPrefix() {
		return "edit_biocollections_gene_";
	}
}
