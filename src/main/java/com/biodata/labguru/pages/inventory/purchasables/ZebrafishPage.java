package com.biodata.labguru.pages.inventory.purchasables;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;

public class ZebrafishPage extends PurchasableCollectionPage{

	
	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	@Override
	protected String getImportXPath() {
		
		return ".//*[@id='main-content']/div/div[1]/a[@href='/system/imports/new?class=Biocollections%3A%3AZebrafish']";
	}

	@Override
	protected String getFileNameToImport() {
		return LGConstants.ZEBRFISHES_TEMPLATE;
	}
	
	@Override
	protected boolean isPurchasableEnabled() {
		
		return false;
	}
	
	@Override
	public List<String> getAvailableColumnsForCustomiseTableView() {
		List<String> columns = super.getAvailableColumnsForCustomiseTableView();
		columns.add("preferences_auto_name");//sysid
		columns.add("preferences_name");//name
		columns.add("preferences_gene");
		columns.add("preferences_genotype");
		columns.add("preferences_phenotype");
		columns.add("preferences_transgene");
		columns.add("preferences_mutant_allele");
		return columns;
	}

	@Override
	protected void addItemWithGivenName(String name) {
		clickNewButton("new_zebrafish");
	      
        WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated
        		(By.id("name")));
        sendKeys(txtName, name);
		
	}
	

	@Override
	protected String getEditCollectionPrefix() {
		return "edit_biocollections_zebrafish_";
	}
	
	@Override
	protected String getCollectionName() {
		
		return LGConstants.ZEBRAFISH_PREFIX;
	}

}
