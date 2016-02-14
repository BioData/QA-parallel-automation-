package com.biodata.labguru.pages.inventory.purchasables.botany;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;


public class BotanySeedsPage extends BotanyPage{

	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	@Override
	protected String getEditCollectionPrefix() {
		return "edit_biocollections_seed_";
	}
	
	@Override
	protected String getFileNameToImport() {
		
		return LGConstants.BOTANY_SEEDS_TEMPLATE;
	}
	
	@Override
	protected String getCollectionName() {
		return LGConstants.BOTANY_SEED_PREFIX;
	}
	@Override
	protected String getCustomizeLinkXpath(String collectionName) {
		
		return ".//a[@href='/system/custom_fields?class=Biocollections::Seed']";
	}
	
	@Override
	public List<String> getAvailableColumnsForCustomiseTableView() {
		List<String> columns = super.getAvailableColumnsForCustomiseTableView();
		columns.add("preferences_male_parent_id");
		columns.add("preferences_female_parent_id");
		return columns;
	}
	

	protected void addItemWithGivenName(String plasmidName) {

		clickNewButton("new_seed");
	      
        WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated
        		(By.id("name")));
        sendKeys(txtName, plasmidName);
	}
	
	
	@Override
	public void addExtraFields() {
		super.addExtraFields();
		
		WebElement txtSource =  getWebDriver().findElement
				(By.id("source"));
		txtSource.sendKeys("source of seed");
	}
	
	@Override
	protected String getImportXPath() {
		return ".//*[@id='main-content']/div/div[1]/a[@href='/system/imports/new?class=Biocollections%3A%3ASeed']";
	}
	
	@Override
	public String showCollection(String collectionName) {
		
		return showBotanySeeds();
	}
	
	@Override
	public void deleteCustomFieldsFromCollection(String collectionName) throws InterruptedException {
		
		deleteCustomFieldsFromListOfCustomize(collectionName);
		
	}
}
