package com.biodata.labguru.pages.inventory.purchasables;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;

public class GenericCollectionPage extends PurchasableCollectionPage{

	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	@Override
	public boolean hasList(){
		
		try{
			//first time to add item
		    driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='main-content']/div/div[1]/a[1]/b")));
			return false;
			
		}catch(Exception ex){
			//not first item - look for button 'New ...'
			return true;
		}
	}
	
	@Override
	protected String getCollectionName() {
		return LGConstants.GENERIC_PREFIX;
	}
	
	@Override
	protected String getFileNameToImport() {
		
		return LGConstants.GENERIC_COLLECTION_TEMPLATE2;
	}
	
	@Override
	public String getImportXPath() {
		
		return ".//*[@class='empty_note']/a[2][starts-with(@href,'/system/imports/new?class=Biocollections%3A%3AGeneric&collection_id=')]";
	}
	
	@Override
	protected String getEditCollectionPrefix() {
		
		return "edit_biocollections_generic_";
	}
	
	@Override
	public List<String> getAvailableColumnsForCustomiseTableView() {
		List<String> columns = super.getAvailableColumnsForCustomiseTableView();
		columns.add("preferences_auto_name");//sysid
		columns.add("preferences_name");//name

		//columns that not appear in generic
		columns.remove("preferences_alternative_name");
		columns.remove("preferences_source");
		return columns;
	}

	@Override
	protected void addItemWithGivenName(String name) {
	
		if(hasList()){
			//not first  - look for button 'New'
			WebElement btnAdd = getWebDriver().findElement(By.xpath(".//*[starts-with(@id,'new_')]"));
			btnAdd.click();
		}else{
			WebElement linkAddManually = getWebDriver().findElement(By.xpath(".//*[@id='main-content']/div/div[1]/a/b"));
			
			if(linkAddManually.getText().equals("manually"))
				linkAddManually.click();
		}
		try {
			TimeUnit.SECONDS.sleep(3);

	        WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated
	        		(By.id("name")));
	        sendKeys(txtName, name);

		} catch (InterruptedException e) {
			
		}
	}
	
	public void showCollectionFromBreadCrumbs() throws InterruptedException {
		
		WebElement collectionLink = getWebDriver().findElement(By.xpath(".//*[@id='breadcrumbs']/ul/li[2]/span/a"));
		collectionLink.click();
		TimeUnit.SECONDS.sleep(2);
	}

	
	public void deleteGenericCollection(String collectionName) throws InterruptedException {
		
		//delete  all items in collection
		deleteAllItems(collectionName);
		TimeUnit.SECONDS.sleep(2); 
		//go to settings and delete the collection itself
		showCollectionsAndSettings();
		
		deleteCollectionFromList(collectionName);		

	}

	private void deleteCollectionFromList(String collectionName) throws InterruptedException {
		
		List <WebElement> columns = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@id='generic_list']/div")));
		
		for (int col = 1; col <= columns.size(); col++) {
			
			List <WebElement> genericList = getWebDriver().findElements(By.xpath(".//*[@id='generic_list']/div[" + col + "]/div"));
			
			for (int row = 1; row <= genericList.size(); row++) {
				WebElement link = getWebDriver().findElement(By.xpath(".//*[@id='generic_list']/div[" + col + "]/div[" + row +"]"));
				String name = link.getText().substring(0, link.getText().indexOf(" ("));
				if(name.equals(collectionName)){
					WebElement imgDelete = getWebDriver().findElement(By.xpath(".//*[@id='generic_list']/div[" + col + "]/div[" + row +"]/a[1]"));
					imgDelete.click();
					TimeUnit.SECONDS.sleep(1);
					checkForAlerts();
					break;
				}
			}
		}
	}

	public void deleteAllGenericCollection() throws InterruptedException {
	
		//check if generic collection exist - if exist - delete all items
		boolean genExist = selectDropDownMenu(By.id(LGConstants.GENERIC_COLLECTION_MENU_ID));
		if(genExist)
			deleteAllTable();

		//go to settings and delete all the generic collections
		showCollectionsAndSettings();
		
		List <WebElement> genericCollectionsList  = getWebDriver().findElements(By.cssSelector(".icon.icon-cross"));
		for (WebElement imgDelete : genericCollectionsList) {
			imgDelete.click();
			TimeUnit.SECONDS.sleep(1);
			checkForAlerts();
			TimeUnit.SECONDS.sleep(1);
		}
	}

}
