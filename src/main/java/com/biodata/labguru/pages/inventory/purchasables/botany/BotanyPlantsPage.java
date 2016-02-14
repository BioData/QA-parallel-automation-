package com.biodata.labguru.pages.inventory.purchasables.botany;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;


public class BotanyPlantsPage extends BotanyPage{

	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	@Override
	protected String getEditCollectionPrefix() {
		return "edit_biocollections_plant_";
	}
	
	@Override
	protected String getFileNameToImport() {
		
		return LGConstants.BOTANY_PLANTS_TEMPLATE;
	}
	
	
	@Override
	protected String getCollectionName() {
		return LGConstants.BOTANY_PLANT_PREFIX;
	}
	
	@Override
	protected String getCustomizeLinkXpath(String collectionName) {
		
		return ".//a[@href='/system/custom_fields?class=Biocollections::Plant']";
	}
	
	@Override
	public List<String> getAvailableColumnsForCustomiseTableView() {
		List<String> columns = super.getAvailableColumnsForCustomiseTableView();
		columns.add("preferences_harvest_date");
		columns.add("preferences_seed_id");
		
		//ids that not exist in plants
		columns.remove("preferences_source");
		columns.remove("preferences_location");
		return columns;
	}

	protected void addItemWithGivenName(String plasmidName) {

		clickNewButton("new_plant");
	      
        WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated
        		(By.id("name")));
        sendKeys(txtName, plasmidName);
        
        addExtraFields();
	}
	
	@Override
	public void addExtraFields(){
		super.addExtraFields();
		try {
			WebElement harvestDate =  getWebDriver().findElement
					(By.id("harvest_date"));
			harvestDate.click();
			TimeUnit.SECONDS.sleep(1);
		   	selectToday(harvestDate);
		   	harvestDate =  getWebDriver().findElement
					(By.id("harvest_date"));
		    harvestDate.click();
		    TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.getMessage();
		}
	}
	
	@Override
	protected String getImportXPath() {
		return ".//*[@id='main-content']/div/div[1]/a[@href='/system/imports/new?class=Biocollections%3A%3APlant']";
	}
	
	@Override
	public String showCollection(String collectionName) {
		
		return showBotanyPlants();
	}
	
	@Override
	public void deleteCustomFieldsFromCollection(String collectionName) throws InterruptedException {
		
		deleteCustomFieldsFromListOfCustomize(collectionName);
		
	}
}
