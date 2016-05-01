package com.biodata.labguru.pages.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.model.Stock;
import com.biodata.labguru.pages.ITableView;


public class StockPage extends BaseStoragePage implements ITableView{

	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	public boolean hasList(){
		
		try {
			WebElement title = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".empty_note")));
			return !(title.getText().trim().startsWith("Start your stock collection by adding one manually") 
					|| title.getText().trim().startsWith("Start your stock collection by adding a box first"));
		} catch (Exception e) {
			return true;
		}
	}
	
	
	/**
	 * searchInConsumedStocks
	 * @param stockName -the stock to search in the consumed stocks view
	 * @return true if found
	 * @throws InterruptedException 
	 */
	public boolean searchInConsumedStocks(String stockName) throws InterruptedException {
		
		showConsumedStocks();
		
		invokeSearchInStocks(stockName);
		
		
		List<WebElement> tableRows = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@id='index_table']/tbody/tr")));
		
		if(tableRows.size() == 2) {
			WebElement NameElem = getWebDriver().findElement(By.xpath(".//*[@id='index_table']/tbody/tr[2]/td[3]/span[2]/a"));
			String name = NameElem.getText();
			if(name.equals(stockName))
					return true;
			
		}
		return false;
		
	}


	private void invokeSearchInStocks(String stockName) throws InterruptedException {
		WebElement txtSearch = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.cssSelector(".searchtextbox")));
		sendKeys(txtSearch, stockName);
		
		WebElement btnSearch = getWebDriver().findElement(By.xpath(".//*[@value='search-button']"));
		btnSearch.click();
		TimeUnit.SECONDS.sleep(2);
	}
	
	
	private void showConsumedStocks() {
		
		WebElement linkShowArchive;
		if(hasList()){
			linkShowArchive = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='index-header']/h1/a[@href='/storage/stocks/archived']")));
		}else{
			linkShowArchive = driverWait.until(ExpectedConditions.visibilityOfElementLocated
					(By.xpath(".//*[@id='main-content']/div[1]/div[1]/a")));
		}
		
		linkShowArchive.click();
	}
	
	/**
	 * markAsConsumedStock - click on the action on the stock show page.
	 * @return noty message
	 * @throws InterruptedException 
	 */
	public String markAsConsumedStock() throws InterruptedException{
		WebElement btnMarkAsUsed = getWebDriver().findElement(By.cssSelector(".icon-mark-as-used"));
		btnMarkAsUsed.click();
		return openMarkAsConsumedPopup();
	}
	
	public Stock editStock(String oldName,String newName,String newType) throws InterruptedException{
		
		openStockFromList(oldName) ;
		
		TimeUnit.SECONDS.sleep(2);

		WebElement btnEdit = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".edit")));
		btnEdit.click();
		//change name
		WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
		sendKeys(txtName, newName);
		
		//change type	
		WebElement dropdownType = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='s2id_select_stock_type']/a/span[2]/b")));
		dropdownType.click();
		
		List<WebElement> types = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@id='select2-drop']/ul/li")));
		for (int i = 1; i <= types.size(); i++) {	
			WebElement type = getWebDriver().findElement(By.xpath(".//*[@id='select2-drop']/ul/li["+ i + "]/div"));
			if(type.getText().equals(newType)){
				type.click();
				TimeUnit.SECONDS.sleep(1);
				break;
			}
		}
		
		//save
		WebElement btnSave = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save")));
		btnSave.click();
		TimeUnit.SECONDS.sleep(3);

		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("page-title")));
		Stock newStock = new Stock();
		newStock.setName(getWebDriver().findElement(By.id("page-title")).getText());
		newStock.setType(getWebDriver().findElement(By.id("lg_info_tab_type")).getText());
		newStock.setContent(getWebDriver().findElement(By.xpath(".//*[@id='main-content']/div[1]/div[4]/table/tbody/tr[3]/td/a")).getText());
		return newStock;
	}

	public String selectLastStockFromList() throws InterruptedException {

		WebElement selectedStock = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='index_table']/tbody/tr[last()]/td[3]/span[2]/a")));
		String stockName = selectedStock.getText();
		selectedStock.click();
		TimeUnit.SECONDS.sleep(1);
		return stockName;
	}
	
	public void openBoxShowPageFromStock() {

		List <WebElement> lblBoxes = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
				(By.xpath(".//*[@id='stocks_and_boxes']/li")));
		for (WebElement elem : lblBoxes) {
			WebElement lblBox = elem.findElement(By.xpath("a"));
			lblBox.click();
		}
		
	}
	
	/**
	 * Select the 'select all' checkbox and delete all stocks.
	 * @return
	 * @throws InterruptedException
	 */
	public boolean markAsConsumedAllStocks() throws InterruptedException {
		
		checkAllTableItemsAllPages();
		markAsConsumedStock();
		TimeUnit.SECONDS.sleep(2);
		showStocks();
		return hasList();
	}
	
	/**
	 * Select specific stock in the list and delete it.
	 * @param name
	 * @throws InterruptedException
	 */
	public void markAsConsumedSelectedStock(String name) throws InterruptedException {
		
		selectStockFromList(name);
		
		markAsConsumedStock();
		TimeUnit.SECONDS.sleep(2);
	}
	
	
	private void selectStockFromList(String name) throws InterruptedException {
		
		//find the specific stock and click its checkbox
		invokeSearchInStocks(name);
		List<WebElement> stocks = getWebDriver().findElements(By.xpath(".//*[@id='index_table']/tbody/tr"));
		for (int i = 2; i <= stocks.size(); i++) {
			WebElement selectedStock = getWebDriver().findElement(By.xpath(".//*[@id='index_table']/tbody/tr["+i+"]/td[3]/span[2]/a"));
			String stockName = selectedStock.getText();
			if(stockName.equals(name)){
				
				WebElement chkBox = getWebDriver().findElement(By.xpath(".//*[@id='index_table']/tbody/tr["+i+"]/td[1]/input"));
				chkBox.click();
				TimeUnit.SECONDS.sleep(1);
				break;
			}		
		}
	}
	public Stock changeStockLocation(String newLocation,String stockName) throws InterruptedException {
		
		 invokeSearchInStocks(stockName);
		
		openStockFromList(stockName);

		editStockLocation(newLocation);

		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("page-title")));
		Stock newStock = new Stock();//the xpath direct to 'Box' input text (when the box is without content - tr[3])
		newStock.setLocation(getWebDriver().findElement(By.xpath(".//*[@id='main-content']/div[1]/div[4]/table/tbody/tr[3]/td/a")).getText());
		return newStock;
	}
	
	
	public String editStockLocation(String newLocation) throws InterruptedException {
		
		WebElement btnEdit = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".edit")));
		btnEdit.click();
		TimeUnit.SECONDS.sleep(2);
		//change location
		selectBox(newLocation,true/*edit mode*/);
		//save
		WebElement btnSave = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save")));
		btnSave.click();
		TimeUnit.SECONDS.sleep(3);
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("page-title")));
		String location = getWebDriver().findElement(By.id("lg_info_tab_box")).getText();
		return location;
	}
	
	@Override
	public List<String> getAvailableColumnsForCustomiseTableView() {
		List<String> columns = new ArrayList<String>();
		columns.add("preferences_id");//id
		columns.add("preferences_name");// name
		columns.add("preferences_description");
		columns.add("preferences_created_at");
		columns.add("preferences_member_first_name");//owner
		columns.add("preferences_container_type");//type
		columns.add("preferences_stored_by");// stored by/frozen by
		columns.add("preferences_content");//content
		columns.add("preferences_location_in_box");//storage location
		columns.add("preferences_stored_on");// stored on/frozen on
		columns.add("preferences_expiration_date");
		columns.add("preferences_color");
		columns.add("preferences_concentration");
		columns.add("preferences_volume");
		columns.add("preferences_units");
		columns.add("preferences_lot");
		columns.add("preferences_barcode");
		return columns;
	}
	
	public String checkCustomizeTableView() throws InterruptedException {
		
        //select all available columns
        List<String> selectedColumns = checkPreference(getAvailableColumnsForCustomiseTableView()); 

        return checkTableHeaders(selectedColumns);
        
	}
	public void deleteUsedStocksFromView() throws InterruptedException {
		
		showConsumedStocks();	
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".searchtextbox")));
		deleteAllItemsFromTable();
		
	}
	public void openStockFromList(String name) throws InterruptedException {
		
		//find the specific stock and click its checkbox
		List<WebElement> stocks = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
				(By.xpath(".//*[@id='index_table']/tbody/tr")));
		for (int i = 2; i <= stocks.size(); i++) {
			WebElement selectedStock = getWebDriver().findElement(By.xpath(".//*[@id='index_table']/tbody/tr["+i+"]/td[3]/span[2]/a"));
			String stockName = selectedStock.getText();
			if(stockName.equals(name)){
				//click on stock to go to its show page
				selectedStock.click();
				TimeUnit.SECONDS.sleep(1);
				break;
			}
		}	
	}

	/**
	 * check the storage field of stock in its show page
	 */
	public String checkStorage() {
		
		WebElement storage = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("lg_info_tab_storage")));
		String storageLocation = storage.getText();
		return storageLocation;

	
	}
	
	/**
	 * Export all stocks in the list. If list is NULL - export all.
	 * @param stocksToExport -list of stocks names to export or NULL if export all stocks
	 * @return noty message
	 * @throws InterruptedException
	 */
	public String export(List<String> stocksToExport) throws InterruptedException {
		
		if(stocksToExport == null){//select all
			WebElement checkAll = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("check_all")));
			checkAll.click();
			TimeUnit.SECONDS.sleep(1);
		}else{
			for (String stockName : stocksToExport) {
				selectStockFromList(stockName);
			}
		}
		WebElement wheel = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("index_cog")));
		wheel.click();
		TimeUnit.SECONDS.sleep(1);
		WebElement btnExport = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("export_link")));
		btnExport.click();
		TimeUnit.SECONDS.sleep(1);
		
		String msg = checkForNotyMessage();
		TimeUnit.SECONDS.sleep(2);
		return msg;
	}

	public String importStocks() throws InterruptedException {
		
		WebElement wheel = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("index_cog")));
		wheel.click();
		TimeUnit.SECONDS.sleep(1);
		WebElement btnImport = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("import_stocks")));
		btnImport.click();
		TimeUnit.SECONDS.sleep(1);
		
		String pathToImport = workingDir + LGConstants.ASSETS_FILES_DIRECTORY +  LGConstants.COLLECTIONS_IMPORT_DIRECTORY + "/"+ LGConstants.STOCKS_TEMPLATE;
		TimeUnit.SECONDS.sleep(5);
		uploadFile(pathToImport);
		
		TimeUnit.SECONDS.sleep(3);
		//wait until stocks index table is shown
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='index-header']/h1")));
		invokeSearchInStocks(LGConstants.IMPORTED_STOCK_NAME);
		openStockFromList(LGConstants.IMPORTED_STOCK_NAME);
		WebElement pageTitle = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("page-title")));
		String title = pageTitle.getText();
		return title;
	}

}
