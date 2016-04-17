package com.biodata.labguru.pages.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import com.biodata.labguru.GenericHelper;
import com.biodata.labguru.LGConstants;
import com.biodata.labguru.model.CollectionItem;
import com.biodata.labguru.model.ICollectionItem;
import com.biodata.labguru.model.Stock;
import com.biodata.labguru.pages.AdminPage;
import com.biodata.labguru.pages.ITableView;


public abstract class CollectionPage extends AdminPage implements ITableView{
		
	protected static final String TAB_STOCKS_ID = "tabs-sample_stocks-link";
	protected static final String TABS_INFO_ID = "tabs-info-link";

	protected abstract String getImportXPath();
	
	protected abstract String getFileNameToImport();
	
	protected abstract void addItemWithGivenName(String name); 
	
	protected abstract String getCollectionName();
	
	protected abstract String getEditCollectionPrefix();

	/**
	 * Returns a list of all available columns fro the table in customize table view.
	 * @return list of id attribute for the headers
	 */
	public List<String> getAvailableColumnsForCustomiseTableView() {
		
		List<String> columns = new ArrayList<String>();
		columns.add("preferences_alternative_name");
		columns.add("preferences_description");
		columns.add("preferences_created_at");
		columns.add("preferences_member_first_name");//owner
		columns.add("preferences_source");//source
		columns.add("preferences_location");//location
		columns.add("preferences_linked_resources");//linked resource
		return columns;
	}
	
	public boolean hasList() {
		try{
			//look for 'manually' link when this is the first time adding 
			driverWait.until(ExpectedConditions.visibilityOfElementLocated
					(By.xpath(".//*[@id='main-content']/div/div[1]/a")));
			return false;
			
		}catch(Exception ex){
			//not first item - look for button 'New ...'
			return true;
		}
	}
	
	public String checkCollectionCustomizeBreadCrumbs(String collectionName) {
		
		showCollectionsAndSettings();
		String failedCollectin = "";
		
		try{		
		
			WebElement linkCustom = getWebDriver().findElement(By.xpath(getCustomizeLinkXpath(collectionName)));
			linkCustom.click();
			TimeUnit.SECONDS.sleep(1);
			//opens the customize page - check the bread crumbs label
			WebElement breadcrumbs = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("breadcrumbs")));
			String breadAsString = breadcrumbs.getText();
			if(collectionName.contains("_"))
				collectionName = collectionName.replace('_',' ');
			
			if(!breadAsString.equalsIgnoreCase(" " + LGConstants.SETTINGS_LABEL +" \n "+ collectionName + " \n " + LGConstants.CUSTOMIZE_LABEL + " " ))
				failedCollectin =  collectionName;

			
		}catch(Exception e){
			//do nothing - some collections have no customize link
		}

		
		return failedCollectin;
	}
	
	protected String getCustomizeLinkXpath(String collectionName) {
		return ".//*[@for='"+ collectionName +"']/a";
	}

	public CollectionItem checkCreatedItem(CollectionItem itemToCreate){

		addItemWithGivenName(itemToCreate.name);
		addDescription(itemToCreate.name);
        save();
        
        //load again show page to see that all data is saved well
        CollectionItem item = new CollectionItem();
        loadCollectionData(item);
        return item;
	}
	

	protected void loadCollectionData(ICollectionItem item) {
	
		try {
			WebElement txtName = getWebDriver().findElement(By.id("page-title"));
			item.setName(txtName.getText());
			 
			WebElement txtOwner = getWebDriver().findElement(By.id("lg_info_tab_owner"));
			item.setOwner(txtOwner.getText());
			
			WebElement txtCreatedAt = getWebDriver().findElement(By.id("lg_info_tab_created_at"));
			item.setCreatedAt(txtCreatedAt.getText());
			
			WebElement txtDescription = getWebDriver().findElement(By.xpath(".//*[@id='lg_info_tab_description']/div/p"));
			item.setDescription(txtDescription.getText());
		} catch (NoSuchElementException e) {
			Assert.fail("One of the info fields is missing: " + e.getMessage(), e);
		}

	}

	public boolean addStock(String stockName) throws InterruptedException{
		
		TimeUnit.SECONDS.sleep(2);
		
		WebElement btnAddStock = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sidebar_stock_add_link"))); 
		btnAddStock.click();;
		
		TimeUnit.SECONDS.sleep(2);

		String storage = openStockSelectionDialog(stockName,"None"/*without box*/,LGConstants.BOTTLE) ;
		
		TimeUnit.SECONDS.sleep(2);
		
		selectTabById(TAB_STOCKS_ID);
		TimeUnit.SECONDS.sleep(2);
		
		String savedLocation = findStockInTable(storage,stockName);
		return savedLocation.equals(storage);
	}
	

	
	
	public String showPage(String name) throws InterruptedException {
		
		//go over the list and open show the first one on the list
		List<WebElement> items = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@id='index_table']/tbody/tr")));
		for (int j = 2; j <= items.size(); j++) {
			WebElement nameItem = getWebDriver().findElement(By.xpath(".//*[@id='index_table']/tbody/tr["+j+"]/td[3]/a"));
			String itemName = nameItem.getText();
			if(itemName.equals(name)){
				nameItem.click();
				TimeUnit.SECONDS.sleep(2);
				WebElement title = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='page-title']")));
				return title.getText();
			}
		}
		//no item was found
		return "Opps...no item found";
	}
	
	public void showDirecrory(String id) throws InterruptedException {
		
		if(hasList()){	
			
			 WebElement btnAddFromDirectory = getWebDriver().findElement(By.id(id));
			 btnAddFromDirectory.click();
			 TimeUnit.SECONDS.sleep(2);
		}
	}

	public String addItemFromDirectory() throws Exception{
		List<WebElement> plusList =  driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".icon.icon-circle-plus")));
		for (WebElement plusElem : plusList) {
			plusElem.click();
			TimeUnit.SECONDS.sleep(2);
			break;
		}
		
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("page-title")));
		return getWebDriver().getTitle();
	}
	

	
	public String addNewItem(String name){
		
		addItemWithGivenName(name);    
		addDescription(name);
        save();
        
        //wait for the noty message
        return checkForNotyMessage();
	}
	
	protected void addDescription(String name){
		try {
			writeInRedactor("description", name);
		} catch (Exception e) {
			getLogger().debug("@@Error while writing in redactor");
		}
	}
	

	public String importCollection() throws InterruptedException{
		
		selectImportCollection();

		String pathToImport = workingDir + LGConstants.ASSETS_FILES_DIRECTORY +  LGConstants.COLLECTIONS_IMPORT_DIRECTORY + "/"+ getFileNameToImport();
		TimeUnit.SECONDS.sleep(5);
		uploadFileToImport(pathToImport);
		//wait maximum 5 minutes for the noty message that indicates that import finished
		TimeUnit.MINUTES.sleep(5);
        String msg = checkForNotyMessage();

	    return msg;
	}

	private void uploadFileToImport(String pathToImport) throws InterruptedException {
		
		uploadFile(pathToImport);

		WebElement btnUpload = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@value='Upload']")));
		btnUpload.click();
		TimeUnit.SECONDS.sleep(1);
		
		closeIridizePopups();
		
		WebElement btnImport = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//input[@value='Start Import']")));
		btnImport.click();
		TimeUnit.SECONDS.sleep(1);
	}

	protected void selectImportCollection() throws InterruptedException {
		
		if(hasList()){
			//import from the import button on the wheel on the right 
			WebElement wheelMenu = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='index_cog']/b")));
			wheelMenu.click();
			TimeUnit.SECONDS.sleep(1);
			WebElement menuImport = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='index-header']/h1/div[1]/ul/li[1]/a")));
			menuImport.click();	
			TimeUnit.SECONDS.sleep(1);
			
		}else{//click on import link when no items in the list
			String pathToImportLink = getImportXPath();
			WebElement linkImport = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(pathToImportLink)));
			linkImport.click();
			TimeUnit.SECONDS.sleep(1);
		}
	}
		

	protected void clickNewButton(String buttonId) {
	
		try{
			//first time to add 
			WebElement linkAddManually = driverWait.until(ExpectedConditions.visibilityOfElementLocated
					(By.xpath(".//*[@id='main-content']/div/div[1]/a/b")));
			
			if(linkAddManually.getText().equals("manually"))
				linkAddManually.click();
			
		}catch(Exception ex){
			
			if(hasList()){
				//not first  - look for button 'New'
				WebElement btnAdd = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(buttonId)));
				btnAdd.click();
				try {
					TimeUnit.SECONDS.sleep(2);
				}  catch (Exception e) {
					
					getLogger().debug(e.getMessage());
				} 
			}
		}
		
	}


	public boolean checkIfAllCustomFieldsAppear(List<String> fields) throws InterruptedException {
		
		int exist = 0;
		
		editPage();
		
		List<WebElement> fieldsList = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
				(By.xpath(".//*[starts-with(@id,'" + getEditCollectionPrefix() + "')]/div[1]/div[2]/div")));
		for (int i = 1; i <= fieldsList.size(); i++) {
			WebElement fieldName = getWebDriver().findElement(By.xpath(".//*[starts-with(@id,'" + getEditCollectionPrefix() + "')]/div[1]/div[2]/div["+ i +"]/label"));
			if(fields.contains(fieldName.getText())){
				exist++;
			}		
		}
		
		return (exist == fields.size());
	}
	


	protected void editPage() throws InterruptedException {
		WebElement btnEdit = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='edit_link']/span")));
		btnEdit.click();
		TimeUnit.SECONDS.sleep(2);
	}

	public String generateTemplateAndImport(List<String> fields) throws Exception{
		
		selectImportCollection();
		
		String templateName = downloadCollectionTemplate();

		//write In Template
		boolean allCFInTemplate = GenericHelper.writeToExcel(templateName,fields);
		if(!allCFInTemplate)
			return "Not all custom fields are in template";
		//load again the generated  template to see if all custom fields were crearted
		String pathToImport = workingDir + LGConstants.ASSETS_FILES_DIRECTORY +  LGConstants.COLLECTIONS_TEMPLATES_DIRECTORY+ "/" + LGConstants.OUTPUT_EXCEL_FILE;
		uploadFileToImport(pathToImport);	
	    return checkForNotyMessage();
	}

	private String downloadCollectionTemplate() throws InterruptedException {
		
		//download the generated template
		WebElement linkTemplate = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='main-content']/div/a")));
		String href = linkTemplate.getAttribute("href");
		String templateName = href.substring(href.lastIndexOf('/')+1);
		linkTemplate.click();
		
		TimeUnit.SECONDS.sleep(3);
	    
		return templateName;
	}

	public List<String> addCustomFieldToCollection(String collectionName) {
		
		try{		
			List <WebElement> customizeList = getWebDriver().findElements(By.xpath(".//*[@for='"+ collectionName +"']/a"));
			for (WebElement linkCustom : customizeList) {
				linkCustom.click();
				TimeUnit.SECONDS.sleep(2);
				//first we delete all custom fields if there are any
				deleteCustomFields();
				
				return addCustomFields();
			}


		}catch(Exception e){
			
			//do nothing - some collections have no customize link
		}
		
		return new ArrayList<String>();
	}
	

	public void deleteCustomFieldsFromCollection(String collectionName) throws InterruptedException {
		
		TimeUnit.SECONDS.sleep(2);	
		WebElement linkCustom = getWebDriver().findElement(By.xpath(".//*[@for='"+ collectionName +"']/a"));
		linkCustom.click();
		TimeUnit.SECONDS.sleep(2);
			
		deleteCustomFields();
	}
	
	public String deleteItemFromShowPage(String importedName) throws InterruptedException {
		
		TimeUnit.SECONDS.sleep(2); 
		
		WebElement deleteBtn = getWebDriver().findElement(By.cssSelector(".delete"));
		deleteBtn.click();
		TimeUnit.SECONDS.sleep(1); 
		
		checkForAlerts();
		
		String msg = checkForNotyMessage();
		TimeUnit.SECONDS.sleep(3); 
		return msg;
		
	}

	/**
	 * Search for the given item and delete it from index table.
	 * @param name
	 * @return true if delete succeeded
	 * @throws InterruptedException
	 */
	public boolean deleteItemFromIndexTable(String name){
		
		try {
			TimeUnit.SECONDS.sleep(2); 
			invokeSearchInCollection(name);
			
			TimeUnit.SECONDS.sleep(2); 
			
			WebElement chkbox = driverWait.until(ExpectedConditions.visibilityOfElementLocated
					(By.xpath(".//*[@id='index_table']/tbody/tr[2]/td[1]/input")));
			chkbox.click();
			TimeUnit.SECONDS.sleep(1); 
			
			WebElement linkDelete = getWebDriver().findElement(By.xpath(".//*[@id='delete_selected']/span"));
			linkDelete.click();
			TimeUnit.SECONDS.sleep(2); 
			checkForAlerts();
			
			//wait until the index table will be fully loaded
			waitForPageCompleteLoading();
			TimeUnit.SECONDS.sleep(3); 
			
		} catch (InterruptedException e) {
			getLogger().debug("@@Error while trying to delete item.",e);
			return false;
		}
		return true;	
	}
	protected void searchAndOpenItem(String item) throws InterruptedException {
		
		invokeSearchInCollection(item);
		TimeUnit.SECONDS.sleep(2);
		WebElement nameElem = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='index_table']/tbody/tr[2]/td[3]/a")));
		nameElem.click();
		TimeUnit.SECONDS.sleep(3);
	}
	
	public String invokeSearchInCollection(String itemToSearch) throws InterruptedException{
		
		WebElement txtSearch = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".searchtextbox")));
        sendKeys(txtSearch, itemToSearch);
       
        WebElement btnSearch = getWebDriver().findElement(By.xpath(".//*[@value='search-button']"));
        btnSearch.click();
        TimeUnit.SECONDS.sleep(2);
        WebElement lblCount = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("class_count")));
        String count = lblCount.getText();
        return count;
	}
	
	protected void deleteCustomFieldsFromListOfCustomize(String collectionName) throws InterruptedException {
		TimeUnit.SECONDS.sleep(2);	
		List <WebElement> customeLinks = getWebDriver().findElements(By.xpath(".//*[@for='"+ collectionName +"']/a"));
		
		for (WebElement linkCustom : customeLinks) {
			linkCustom.click();
			TimeUnit.SECONDS.sleep(2);
				
			deleteCustomFields();
			showCollectionsAndSettings();
			
		}
	}

	public void deleteAllItems(String collectionId) throws InterruptedException {
		
		showCollection(collectionId);
		
		deleteAllTable();
		
	}

	protected void deleteAllTable() throws InterruptedException {
		
		TimeUnit.SECONDS.sleep(2); 
		if(hasList()){
			WebElement chkboxAll = driverWait.until(ExpectedConditions.visibilityOfElementLocated
					(By.xpath(".//*[@id='check_all']")));
			chkboxAll.click();
			TimeUnit.SECONDS.sleep(1); 
			
			WebElement linkDelete = driverWait.until(ExpectedConditions.visibilityOfElementLocated
					(By.xpath(".//*[@id='delete_selected']/span")));
			linkDelete.click();
			
			checkForAlerts();
			TimeUnit.SECONDS.sleep(5); 
		}
	}

	public Stock addStockFromStocksTab(String stockName, String type) throws InterruptedException {
		
		Stock stockObj = new Stock();
		selectTabById(TAB_STOCKS_ID);
		TimeUnit.SECONDS.sleep(2);
		
		WebElement btnAddStock = getWebDriver().findElement(By.id("add_stock"));
		btnAddStock.click();
		
		TimeUnit.SECONDS.sleep(2);

		String location = openStockSelectionDialog(stockName,"None"/*without box*/,type) ;
		stockObj.setName(stockName);
		stockObj.setType(type);
		stockObj.setLocation(location);
		TimeUnit.SECONDS.sleep(2);
	
		selectTabById(TAB_STOCKS_ID);
		TimeUnit.SECONDS.sleep(2);
		
		findStockInTable(location,stockName);
		return stockObj;
	}

	private String findStockInTable(String storageName, String stockName) {
		
		List <WebElement> tableRows = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
				(By.xpath(".//*[@class='stocks_table']/tr")));
		for (int i = 1; i <= tableRows.size(); i++) {
			//find the name of the created stock
			WebElement stockNameCell = getWebDriver().findElement(By.xpath(".//*[@class='stocks_table']/tr["+ i +"]/td[4]/strong/a"));
			
			String stock = stockNameCell.getText();
			//find the name of the selected storage
			WebElement storageNameCell = getWebDriver().findElement(By.xpath(".//*[@class='stocks_table']/tr["+ i +"]/td[6]/div/a/span"));
			
			String storage = storageNameCell.getText();
			
			//TODO - check also the type and description
			if( storageName.endsWith(storage) && stockName.equals(stock) ){
				return storageName;
			}
		}

		return "not in location";
		
	}

	public boolean addStockLocatedInBox(String stockName, String boxName) throws InterruptedException {
		TimeUnit.SECONDS.sleep(2);
		selectTabById(TAB_STOCKS_ID);
		TimeUnit.SECONDS.sleep(1);
		
		WebElement btnAddStock = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sidebar_stock_add_link"))); 
		btnAddStock.click();
		
		TimeUnit.SECONDS.sleep(2);

		String storageName = openStockSelectionDialog(stockName,boxName,LGConstants.TUBE) ;

		TimeUnit.SECONDS.sleep(2);
		
		selectTabById(TAB_STOCKS_ID);
		TimeUnit.SECONDS.sleep(2);
		
		String savedLocation = findStockInTable(storageName,stockName);
		return savedLocation.equals(storageName);
	}


	public String markAsConsumedStockInTable(String stockName) throws InterruptedException {
		
		List <WebElement> tableRows = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
				(By.xpath(".//*[@class='stocks_table']/tr")));
		for (int i = 1; i <= tableRows.size(); i++) {
			//find the name of the created stock
			WebElement stockNameCell = getWebDriver().findElement(By.xpath(".//*[@class='stocks_table']/tr["+ i +"]/td[4]/strong/a"));
			
			String stock = stockNameCell.getText();
			if(stock.equals(stockName)){
				WebElement deleteIcon = getWebDriver().findElement(By.xpath(".//*[starts-with(@id,'archive_stock_')]/i"));
				deleteIcon.click();
				TimeUnit.SECONDS.sleep(2);
				
				String msg = openMarkAsConsumedPopup();
				return msg;
			}
		}
		return "The stock was not mark as consumed as expected";
	}
	
	public boolean invokeSearchStock(String itemToSearch) throws InterruptedException{
		
		WebElement txtSearch;
		try {
			txtSearch = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".searchtextbox")));
		} catch (Exception e) {
			//search box not found - no stocks available
			return false;
		}
		sendKeys(txtSearch, itemToSearch);
  
		WebElement btnSearch = getWebDriver().findElement(By.xpath(".//*[@value='search-button']"));
		btnSearch.click();
		TimeUnit.SECONDS.sleep(2);
		
		List<WebElement> tableRows = getWebDriver().findElements(By.xpath(".//*[@id='index_table']/tbody/tr"));
		return (tableRows.size() > 1);//check that the item that we search for is found
		
	}

	public String checkCustomizeTableView() throws InterruptedException {

        //select all available columns
        List<String> selectedColumns = checkPreference(getAvailableColumnsForCustomiseTableView()); 

        return checkTableHeaders(selectedColumns);
        
	}


	public String addItemSaveAndNew(String name) {
		addItemWithGivenName(name);    
		addDescription(name);
        saveAndNew();
        
        //wait for the noty message
        String msg = checkForNotyMessage();
        //check we are again in the new item page
        try {
			getWebDriver().findElement(By.xpath(".//*[@value='Save & New']"));
		} catch (NoSuchElementException e) {
			getLogger().debug("@@ missing button 'Save & New' - not the right page...");
		}
        return msg;
	}

	/**
	 * Set the threshold in the stocks tab and return the status message that shown.
	 * @param stockCount - the number of stocks to set or 'null' if we only want the status message.
	 * @return status message of the threshold
	 * @throws InterruptedException
	 */
	public String setThreshold(String stockCount) throws InterruptedException {
		
		//in this case we want to see what the status message is(in case we updated the stocks count by adding/removing stocks)
		if(stockCount == null){
			
			WebElement thresholdStatus = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("threshold_status")));
			String thresholdStatusMsg = thresholdStatus.getText();
			
			return thresholdStatusMsg;	
		}
		
		WebElement btnThreshold = getWebDriver().findElement(By.id("add_threshold"));
		btnThreshold.click();
		
		TimeUnit.SECONDS.sleep(2);
		
		WebElement stockCountInput = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("threshold_min_stock_count")));
		stockCountInput.clear();
		stockCountInput.sendKeys(stockCount);
		
		WebElement chkNotification = getWebDriver().findElement(By.id("threshold_notify_immediately"));
		chkNotification.click();
		TimeUnit.SECONDS.sleep(1);
		
		WebElement submit = getWebDriver().findElement(By.xpath(".//*[@id='new_threshold']/div[2]/input[@value='Subscribe']"));
		submit.click();
		TimeUnit.SECONDS.sleep(2);
		
		WebElement thresholdStatus = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("threshold_status")));
		String thresholdStatusMsg = thresholdStatus.getText();
		
		return thresholdStatusMsg;
	}
	
	public void selectStocksTab() throws InterruptedException{
		selectTabById(TAB_STOCKS_ID);
	}

	private void selectTabById(String tabId) throws InterruptedException {
		
		WebElement tab = getWebDriver().findElement(By.id(tabId));
		tab.click();
		TimeUnit.SECONDS.sleep(1); 
		
	}

}
