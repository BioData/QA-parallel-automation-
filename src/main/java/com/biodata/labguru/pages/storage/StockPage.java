package com.biodata.labguru.pages.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

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
	 * showArchiveView
	 * @param stockName -the stock to search in the archive view
	 * @return
	 */
	public boolean showArchiveView(String stockName) {
		
		WebElement linkShowArchive;
		if(hasList()){
			linkShowArchive = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='index-header']/h1/a[@href='/storage/stocks/archived']")));
		}else{
			linkShowArchive = driverWait.until(ExpectedConditions.visibilityOfElementLocated
					(By.xpath(".//*[@id='main-content']/div[1]/div[1]/a")));
		}
		
		linkShowArchive.click();
		
		WebElement txtSearch = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.cssSelector(".searchtextbox")));
		sendKeys(txtSearch, stockName);
		
		WebElement btnSearch = getWebDriver().findElement(By.xpath(".//*[@value='search-button']"));
		btnSearch.click();
		
		WebElement label = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#class_count")));
		return !label.getText().equals("(no search results)");
	}
	
	/**
	 * deleteArchiveStock
	 * @param delete -true for delete action,false for archive
	 * @return
	 * @throws InterruptedException 
	 */
	public String deleteArchiveStock(boolean delete) throws InterruptedException{
		WebElement btnDelete = getWebDriver().findElement(By.id("delete-item"));
		btnDelete.click();
		return openDeleteItemPopup(delete);
	}
	
	public Stock editStock(String newName,String newType) throws InterruptedException{
		
		selectLastStockFromList();
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
		newStock.setType(getWebDriver().findElement(By.xpath(".//*[@id='main-content']/div[1]/div[4]/table/tbody/tr[2]/td")).getText());
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
	public boolean deleteAllStocks() throws InterruptedException {
		
		deleteAllItemsFromTable();
		openDeleteItemPopupFromIndexTable(true);
		TimeUnit.SECONDS.sleep(2);
		showStocks();
		return hasList();
	}
	
	/**
	 * Select specific stock in the list and delete it.
	 * @param name
	 * @return
	 * @throws InterruptedException
	 */
	public boolean deleteStock(String name) throws InterruptedException {
		
		selectStockFromList(name);
		
		deleteArchiveStock(true);//delete action
		TimeUnit.SECONDS.sleep(2);

		return hasList();
	}
	
	
	private void selectStockFromList(String name) throws InterruptedException {
		
		//find the specific stock and click its checkbox
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
		
		 WebElement txtSearch = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".searchtextbox")));
         sendKeys(txtSearch, stockName);
        
         WebElement btnSearch = getWebDriver().findElement(By.xpath(".//*[@value='search-button']"));
         btnSearch.click();
		
		TimeUnit.SECONDS.sleep(2);
		
		selectLastStockFromList();

		WebElement btnEdit = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".edit")));
		btnEdit.click();

		//change location
		selectBox(newLocation,true/*edit mode*/);
		//save
		WebElement btnSave = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save")));
		btnSave.click();
		TimeUnit.SECONDS.sleep(3);

		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("page-title")));
		Stock newStock = new Stock();//the xpath direct to 'Box' input text (when the box is without content - tr[3])
		newStock.setLocation(getWebDriver().findElement(By.xpath(".//*[@id='main-content']/div[1]/div[4]/table/tbody/tr[3]/td/a")).getText());
		return newStock;
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


}
