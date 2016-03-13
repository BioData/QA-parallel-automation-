package com.biodata.labguru.pages.inventory.purchasables;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import com.biodata.labguru.GenericHelper;
import com.biodata.labguru.model.ICollectionItem;
import com.biodata.labguru.model.PurchasableCollectionItem;
import com.biodata.labguru.pages.inventory.CollectionPage;


public abstract class PurchasableCollectionPage extends CollectionPage{
	
	
	@Override
	public List<String> getAvailableColumnsForCustomiseTableView() {
		List<String> columns = super.getAvailableColumnsForCustomiseTableView();
		columns.add("preferences_manufacturer");
		columns.add("preferences_catalog_number");
		columns.add("preferences_units");
		columns.add("preferences_price");
		columns.add("preferences_web_page");
		return columns;
	}
	
	@Override
	public String addNewItem(String name){
		
		boolean isPurchasableEnabled = isPurchasableEnabled(getCollectionName());	
		showCollection(getCollectionName());
		
		addItemWithGivenName(name);
		if(isPurchasableEnabled){
			addPurchasableFields("MANUFACT",GenericHelper.buildUniqueName("CAT"),"1","100","www.google.com");
		}
	
		addDescription(name);
        save();
        
        //wait for the noty message
        return waitForNotyMessage(".noty_text");
	}
	


	/**TODO -  check once per class
	 *  check in customize fields of the collection if the purchasable field is enabled
	 * @param collectionName
	 * @return
	 */
	public boolean isPurchasableEnabled(String collectionName) {
		
		boolean enaabled = true;
//		//go to collections settings
//		showCollectionsAndSettings();
//		//click on customize of current collection
//		WebElement linkCustom = getWebDriver().findElement(By.xpath(getCustomizeLinkXpath(collectionName)));
//		linkCustom.click();
//		
//		//look for purchasable attributes field to check if selected or not
//		List <WebElement> defaultFields = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
//				(By.xpath(".//*[@class = 'default_fields config']/li")));
//		for (int i = 1; i <= defaultFields.size(); i++) {
//			WebElement fieldName = getWebDriver().findElement(By.xpath(".//*[@class = 'default_fields config']/li[" + i +"]/span"));
//			if(fieldName.getText().equals(LGConstants.PURCHASABLE_ATTRIBUTES_FIELD)){
//				WebElement checkbox = getWebDriver().findElement(By.xpath(".//*[@class = 'default_fields config']/li[" + i +"]/input"));
//				enaabled = checkbox.isSelected();
//				break;
//			}
//		}

		return enaabled;
	}

	public String addItemSaveAndNew(String name) {
	
		boolean isPurchasableEnabled = isPurchasableEnabled(getCollectionName());				
		showCollection(getCollectionName());
		
		addItemWithGivenName(name);
		if(isPurchasableEnabled){

			addPurchasableFields("MANUFACT",GenericHelper.buildUniqueName("CAT"),"1","100","www.google.com");
		}
		addDescription(name);
        saveAndNew();
        
        //wait for the noty message
        String msg = waitForNotyMessage(".noty_text");
        //check we are again in the new item page
        try {
			getWebDriver().findElement(By.xpath(".//*[@value='Save & New']"));
		} catch (NoSuchElementException e) {
			getLogger().debug("@@ missing button 'Save & New' - not the right page...");
		}
        return msg;
	}
	
	public PurchasableCollectionItem checkCreatedItem(PurchasableCollectionItem itemToCreate){

		boolean isPurchasableEnabled = isPurchasableEnabled(getCollectionName());
	
		showCollection(getCollectionName());
		
		addItemWithGivenName(itemToCreate.name);  
		if(isPurchasableEnabled){
			addPurchasableFields(itemToCreate.manufacturer,itemToCreate.catalogNum,itemToCreate.unit,itemToCreate.price,itemToCreate.webpage);
		}
		
		addDescription(itemToCreate.name);
        save();
        
        //load again show page to see that all data is saved well
        PurchasableCollectionItem item = (PurchasableCollectionItem) loadData(isPurchasableEnabled);
        
        return item;
	}
	
	protected ICollectionItem loadData(boolean isPurchasableEnabled) {
		
		PurchasableCollectionItem item = new PurchasableCollectionItem() ;
		super.loadCollectionData(item);
		if(isPurchasableEnabled){
	        try {
				WebElement txtManufacturer = getWebDriver().findElement(By.xpath(".//*[@id='lg_info_tab_manufacturer']/a"));
				item.setManufacturer(txtManufacturer.getText());
				
				WebElement txtCatNum = getWebDriver().findElement(By.id("lg_info_tab_catalog_number"));
				item.setCatalogNum(txtCatNum.getText());
				
				WebElement txtUnits = getWebDriver().findElement(By.id("lg_info_tab_units"));
				item.setUnits(txtUnits.getText());
				
				WebElement txtPrice = getWebDriver().findElement(By.id("lg_info_tab_price"));
				item.setPrice(txtPrice.getText());
				
				WebElement txtWebPage = getWebDriver().findElement(By.xpath(".//*[@id='lg_info_tab_web_page']/a"));
				item.setWebpage(txtWebPage.getText());
				
			} catch (NoSuchElementException e) {
				Assert.fail("One of the info fields is missing: " + e.getMessage(), e);
			}
		}
        return item;
	}

	protected void addPurchasableFields(String manufacturer,String catalogNum, String unit, String price, String webpage ) {
		
		WebElement txtManufacturer = getWebDriver().findElement(By.id("material_manufacturer"));
        sendKeys(txtManufacturer, manufacturer);
        
        WebElement txtCatNum = getWebDriver().findElement(By.id("material_catalog_number"));
        sendKeys(txtCatNum, catalogNum);
              
        WebElement txtUnit = getWebDriver().findElement(By.id("material_units"));
        sendKeys(txtUnit, unit);
                
        WebElement txtPrice = getWebDriver().findElement(By.id("material_price"));
        sendKeys(txtPrice, price);
   
        WebElement txtWebPage = getWebDriver().findElement(By.id("material_web"));
        sendKeys(txtWebPage,webpage);
        
	}


	
	public void addToShoppingList() throws InterruptedException {
		
		 WebElement btnAdd = getWebDriver().findElement(By.cssSelector(".add_to_cart"));		 
		 btnAdd.click();
		 
		 addToShoppingListDialogWithMissingData();

	}

	public boolean searchInShoppingList(String itemToSearch) throws InterruptedException {
		
       driverWait.until(ExpectedConditions.titleContains("Shopping list"));
       
       invokeSearchItem(itemToSearch);

		 driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("filterd_by")));
		 
       WebElement result = getWebDriver().findElement(By.xpath(".//table[@class='item_details']/tbody/tr[1]/td[2]/b/a"));
		 if(result.getText().equalsIgnoreCase(itemToSearch))
			 return true;
		 else
			 return false;
	}
	
	protected void addToShoppingListDialogWithMissingData() throws InterruptedException {
		
	  WebElement popupDialog = getWebDriver().switchTo().activeElement();
		
       driverWait.until(ExpectedConditions.visibilityOf(popupDialog));
       
       WebElement txtManufacturer = driverWait.until(ExpectedConditions.visibilityOfElementLocated
       		(By.xpath(".//*[@id='add_to_cart_frm']/div[2]/div[2]/input")));
       sendKeys(txtManufacturer, "testManufactor");
       
       WebElement txtCatNum = getWebDriver().findElement(By.xpath(".//*[@id='add_to_cart_frm']/div[2]/div[3]/input"));
       sendKeys(txtCatNum, GenericHelper.buildUniqueName("CAT"));
       
       WebElement txtQuantity = getWebDriver().findElement(By.id("quantity"));
       sendKeys(txtQuantity, "1");   
       
       WebElement txtPrice = getWebDriver().findElement(By.id("material_price"));
       sendKeys(txtPrice, "200");
       
       WebElement btnAdd = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save")));
       btnAdd.click();
       
       getWebDriver().switchTo().activeElement();
	}
	
	public String addToShoppingListFromShowPage() throws InterruptedException{
		
		WebElement btnAddShopList = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cart")));
		btnAddShopList.click();
		
		TimeUnit.SECONDS.sleep(2);
		
		
       openAddToShoppingListDialog();
       //get the noty message
       WebElement notyMsg = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".noty_message")));

       String msg = notyMsg.getText();
       notyMsg.click();
		return msg;
	}
	
	protected String openAddToShoppingListDialog() throws InterruptedException {
		
		WebElement popupDialog = getWebDriver().switchTo().activeElement();
       driverWait.until(ExpectedConditions.visibilityOf(popupDialog));

       WebElement name =  driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='add_to_cart']/div/div[1]/h3")));
       String title =name.getText();
       WebElement txtQuantity = getWebDriver().findElement(By.id("quantity"));
       sendKeys(txtQuantity, "1");   
       
       WebElement txtPrice = getWebDriver().findElement(By.id("material_price"));
       sendKeys(txtPrice, "200");
       
       WebElement btnAdd = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save")));
       btnAdd.click();
       
	     TimeUnit.SECONDS.sleep(1);
        getWebDriver().switchTo().activeElement();
		
		 TimeUnit.SECONDS.sleep(2);
       return title;
	}

}
