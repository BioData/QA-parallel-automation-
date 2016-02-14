package com.biodata.labguru.pages.inventory.purchasables;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;


public class CellLinePage extends PurchasableCollectionPage{

	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	@Override
	protected String getImportXPath() {
		return ".//*[@id='main-content']/div/div[1]/a[@href='/system/imports/new?class=Biocollections%3A%3ACellLine']";
	}

	@Override
	protected String getFileNameToImport() {
		return LGConstants.CELLLINE_TEMPLATE;
	}
	
	@Override
	protected String getCollectionName() {
		
		return LGConstants.CELL_LINE_PREFIX;
	}
	
	@Override
	public List<String> getAvailableColumnsForCustomiseTableView() {
		List<String> columns = super.getAvailableColumnsForCustomiseTableView();
		columns.add("preferences_auto_name");//sysid
		columns.add("preferences_name");//name
		columns.add("preferences_organism");
		columns.add("preferences_tissue");
		columns.add("preferences_medium_and_serum");

		columns.remove("preferences_alternative_name");
	
		return columns;
	}
	
	@Override
	protected void addItemWithGivenName(String name) {
		clickNewButton("new_cell_line");
	      
        WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated
        		(By.id("name")));
        sendKeys(txtName, name);
	}

	@Override
	protected String getEditCollectionPrefix() {
		return "edit_biocollections_cell_line_";
	}
	

}
