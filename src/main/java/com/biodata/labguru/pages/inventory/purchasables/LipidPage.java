package com.biodata.labguru.pages.inventory.purchasables;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;

public class LipidPage extends PurchasableCollectionPage{

	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	@Override
	protected String getImportXPath() {
		
		return ".//*[@id='main-content']/div/div[1]/a[@href='/system/imports/new?class=Biocollections%3A%3ALipid']";
	}

	@Override
	protected String getFileNameToImport() {
		return LGConstants.LIPIDS_TEMPLATE;
	}
	
	@Override
	public List<String> getAvailableColumnsForCustomiseTableView() {
		List<String> columns = super.getAvailableColumnsForCustomiseTableView();
		columns.add("preferences_auto_name");//sysid
		columns.add("preferences_name");//name
		columns.add("preferences_cas_number");
		columns.add("preferences_molecular_weight");
		columns.add("preferences_molecular_formula");
		columns.add("preferences_stock_solution_prep");
		columns.add("preferences_solubility");
		columns.add("preferences_conditions_for_use");
		columns.add("preferences_conditions_for_storage");
		columns.add("preferences_safety_information");
		columns.add("preferences_impurities");
		return columns;
	}

	@Override
	protected void addItemWithGivenName(String name) {
		clickNewButton("new_lipid");
	      
        WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated
        		(By.id("name")));
        sendKeys(txtName, name);
		
	}

	@Override
	protected String getEditCollectionPrefix() {
		return "edit_biocollections_lipid_";
	}

	@Override
	protected String getCollectionName() {
		
		return LGConstants.LIPIDS;
	}


}
