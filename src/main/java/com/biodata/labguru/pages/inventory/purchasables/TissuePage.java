package com.biodata.labguru.pages.inventory.purchasables;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;


public class TissuePage extends PurchasableCollectionPage{

	
	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	
	@Override
	protected String getImportXPath() {
		
		return ".//*[@id='main-content']/div/div[1]/a[@href='/system/imports/new?class=Biocollections%3A%3ATissue']";
	}

	@Override
	protected String getFileNameToImport() {
		return LGConstants.TISSUES_TEMPLATE;
	}
	
	@Override
	protected String getCollectionName() {
		
		return LGConstants.TISSUES;
	}
	
	@Override
	public List<String> getAvailableColumnsForCustomiseTableView() {
		List<String> columns = super.getAvailableColumnsForCustomiseTableView();
		columns.add("preferences_auto_name");//sysid
		columns.add("preferences_name");//name
		columns.add("preferences_harvest_date");
		columns.add("preferences_species");
		columns.add("preferences_genotype_phenotype");
		columns.add("preferences_animal_details");	
		columns.add("preferences_tissue_type");	
		columns.add("preferences_fixation_embedding_procedure");	
		columns.add("preferences_storage_conditions");	
		columns.add("preferences_applications");	
		return columns;
	}

	@Override
	protected void addItemWithGivenName(String name) {
		clickNewButton("new_tissue");
	      
        WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated
        		(By.id("name")));
        sendKeys(txtName, name);
	}

	@Override
	protected String getEditCollectionPrefix() {
		return "edit_biocollections_tissue_";
	}

	public String checkSpecimenInfoAndClick() throws InterruptedException {
		
		WebElement txtSpecimen = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("specimen_link")));
		txtSpecimen.click();
		TimeUnit.SECONDS.sleep(2);
		
		WebElement pageTitle =driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("page-title")));
	    return pageTitle.getText();
		
	}

}
