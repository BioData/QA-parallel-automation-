package com.biodata.labguru.pages.inventory.purchasables;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.model.ICollectionItem;

public class ConsumablesPage extends PurchasableCollectionPage{

	
	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	@Override
	protected String getEditCollectionPrefix() {
		return "edit_catalog_material_";
	}
	
	@Override
	protected String getCollectionName() {
		
		return LGConstants.CONSUMABLE_PREFIX;
	}
	
	@Override
	public boolean isPurchasableEnabled(String collectionName) {
		return true;
	}
	
	@Override
	public boolean hasList(){
		
		try{
			//first time to add material
			driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='main-content']/div/div[1]/a[1]/b")));
			return false;
			
		}catch(Exception ex){
			//not first material - look for button 'New Material'
			return true;
		}
	}
	
	@Override
	protected String getFileNameToImport() {
		
		return LGConstants.CONSUMABLES_TEMPLATE;
	}
	
	@Override
	public String getImportXPath() {
		
		return ".//*[@id='main-content']/div/div[1]/a[@href='/system/imports/new?class=Catalog%3A%3AMaterial']";
	}
	
	@Override
	public List<String> getAvailableColumnsForCustomiseTableView() {
		List<String> columns = super.getAvailableColumnsForCustomiseTableView();
		columns.add("preferences_manufacturer");
		columns.add("preferences_name");//name
		columns.add("preferences_materialtype_id");
		columns.add("preferences_produce_by");
		columns.add("preferences_cas_number");
		columns.add("preferences_mw");
		columns.add("preferences_add_to_shopping_list");
		columns.add("preferences_web");
		
		//this id not exist in consumables
		columns.remove("preferences_manufacturer");
		columns.remove("preferences_web_page");
		
		//cunsumable does not hold source column so we remove it
		columns.remove("preferences_source");
		return columns;
		
		
	}
	
	public String addConsumableFromDirectory() throws Exception {
		
		showDirecrory("add_from_product_directory");
		
		return addItemFromDirectory();
	}
	
	public String addItemFromDirectory() throws Exception{
		
		List<WebElement> plusList =  driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".fa.fa-plus-circle.ng-scope")));
		for (WebElement plusElem : plusList) {
			plusElem.click();
			TimeUnit.SECONDS.sleep(2);
			break;
		}
		
		String msg = checkForNotyMessage();
		return msg;
	}
	
	public String addConsumableToShoppingList() throws InterruptedException {
		
		List<WebElement> tableRows =  getWebDriver().findElements(By.xpath(".//*[@id='index_table']/tbody/tr"));
		
		for (int i = 2; i <= tableRows.size(); i++) {
			
			try{
				//see if itws already added to shopping list - search for the number in circle img
				getWebDriver().findElement(By.xpath(".//*[@id='index_table']/tbody/tr[" +i + "]/td/center/span[@class='tabnum']"));
				
			}catch (Exception e) {
				//if we get here - the item has not been added to shopping list yet - so we add it
				WebElement btnShopList = getWebDriver().findElement(By.xpath(".//*[@id='index_table']/tbody/tr[" +i + "]/td/center/a/i[@class='fa fa-shopping-cart']"));
				btnShopList.click();
				break;
			}
		}

		TimeUnit.SECONDS.sleep(2);	
		 
        String name = openAddToShoppingListDialog();//Add 'name' to Shopping List 
        String[] parts = name.split(" ");
        String part2 = parts[1]; //the name of the item
        
        
         //get the noty message
        WebElement linkMsg = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".noty_text>a")));
         WebElement notyMsg = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".noty_text")));
         
         String msg = notyMsg.getText();
         linkMsg.click();
         
         driverWait.until(ExpectedConditions.titleContains("Shopping list"));
         
         invokeSearchItem(part2);

		 driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("filterd_by")));
		 
         WebElement result = getWebDriver().findElement(By.xpath(".//table[@class='item_details']/tbody/tr[1]/td[2]/b/a"));
		 if(result.getText().toLowerCase().startsWith(part2.toLowerCase()))
			 return msg;
		 else
			 return "Something went wrong - not added to shopping list";
	      
	}
	
	@Override
	protected void addDescription(String name) {
		try {
			writeInRedactor("description",0, name);
		} catch (InterruptedException e) {
			
		}
		
	}
	
	@Override
	protected void addItemWithGivenName(String name) {
   
		clickNewButton("new_material");
		   
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
      
        WebElement txtName = getWebDriver().findElement(By.id("name"));
        sendKeys(txtName, name);    

	}
	
	@Override
	protected void addPurchasableFields(String manufacturer,String catalogNum, String unit, String price, String webpage ) {
		
        WebElement txtManufacturer = getWebDriver().findElement(By.id("catalog_material_manufacturer"));
        sendKeys(txtManufacturer,manufacturer);
        
        WebElement txtCatNum = getWebDriver().findElement(By.id("catalog_material_catalog_number"));
        sendKeys(txtCatNum, catalogNum);
        
        WebElement txtWebPage = getWebDriver().findElement(By.id("catalog_material_web"));
        sendKeys(txtWebPage,webpage);
        
        WebElement txtUnits = getWebDriver().findElement(By.id("material_units"));
        sendKeys(txtUnits, unit);
        
        WebElement txtPrice = getWebDriver().findElement(By.id("material_price"));
        sendKeys(txtPrice, price);
	}
	
	
	@Override
	protected void loadCollectionData(ICollectionItem item) {
		
		WebElement txtName = getWebDriver().findElement(By.id("page-title"));
		String pageTitle = txtName.getText();
		String formatName = pageTitle.substring(pageTitle.indexOf('\n')+1);
        item.setName(formatName);
        
		WebElement txtOwner = getWebDriver().findElement(By.id("lg_info_tab_owner"));
        item.setOwner(txtOwner.getText());
 
        WebElement txtDescription = getWebDriver().findElement(By.xpath(".//*[@id='lg_info_tab_description']/div"));
        item.setDescription(txtDescription.getText());

	}

	public String addNonComercialMaterial(String materialName) throws InterruptedException {
		
		addItemWithGivenName(materialName);   
		
		WebElement cbNonComercial = getWebDriver().findElement(By.id("non_commercial"));
		cbNonComercial.click();
		TimeUnit.SECONDS.sleep(1);
		
		WebElement producedBy = getWebDriver().findElement(By.id("catalog_material_produce_by"));
		producedBy.sendKeys("Automated");
		
        save();
        
        //wait for the noty message
        String msg = checkForNotyMessage();
        
        WebElement producedByLabel = getWebDriver().findElement(By.id("lg_info_tab_produced_by"));
        if(producedByLabel.getText().equals("Automated"))
        	return msg;
        
        return "Material not created as expected.";
	}

	public String moveTo(String moveToItem) throws InterruptedException {
		
		WebElement moveTo = getWebDriver().findElement(By.id("move_to"));
		moveTo.click();
		TimeUnit.SECONDS.sleep(1);
		
		List<WebElement> collectionsList = getWebDriver().findElements(By.cssSelector(".select2-result-label"));
		for (WebElement selection : collectionsList) {
			if(selection.getText().equals(moveToItem)){
				selection.click();
				TimeUnit.SECONDS.sleep(1);
				checkForAlerts();
				break;
			}
		}
		String msg = checkForNotyMessage() ;
		return msg;
	}
	
	public List<String> addCustomFieldToConsumables() throws InterruptedException {
		
		//first we delete all custom fields if there are any
		deleteCustomFieldsFromCollection();
		
		return addCustomFields();

	}

	public void deleteCustomFieldsFromCollection() throws InterruptedException {
		WebElement wheel = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("index_cog")));	
		wheel.click();
		TimeUnit.SECONDS.sleep(2);
		
		WebElement customize = getWebDriver().findElements(By.cssSelector(".icon.icon-spanner")).get(1);	
		customize.click();
		TimeUnit.SECONDS.sleep(2);

		//delete all custom fields if there are any	
		deleteCustomFields();
	
	}
}
