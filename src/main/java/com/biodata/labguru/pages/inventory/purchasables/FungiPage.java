package com.biodata.labguru.pages.inventory.purchasables;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;


public class FungiPage extends PurchasableCollectionPage{

	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	@Override
	protected String getImportXPath() {
		
		return ".//*[@id='main-content']/div/div[1]/a[@href='/system/imports/new?class=Biocollections%3A%3AFungus']";
	}
	
	@Override
	protected String getCollectionName() {
		
		return LGConstants.FUNGI;
	}

	@Override
	protected String getFileNameToImport() {
		return LGConstants.FUNGUS_TEMPLATE;
	}
	
	@Override
	public List<String> getAvailableColumnsForCustomiseTableView() {
		List<String> columns = super.getAvailableColumnsForCustomiseTableView();
		columns.add("preferences_auto_name");//sysid
		columns.add("preferences_name");//name
		columns.add("preferences_genotype");
		columns.add("preferences_phenotype");
		columns.add("preferences_species");
		columns.add("preferences_host");
		columns.add("preferences_virulent");
		columns.add("preferences_sporulate");
		columns.add("preferences_mycelia");
		columns.add("preferences_fruiting_bodies");

		columns.remove("preferences_alternative_name");
	
		return columns;
	}

	@Override
	protected void addItemWithGivenName(String name) {
		clickNewButton("new_fungus");
	      
        WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated
        		(By.id("name")));
        sendKeys(txtName, name);
		
	}

	@Override
	protected String getEditCollectionPrefix() {
		return "edit_biocollections_fungus_";
	}


}
