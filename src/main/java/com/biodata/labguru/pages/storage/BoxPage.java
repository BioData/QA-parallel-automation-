package com.biodata.labguru.pages.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.ITableView;


public class BoxPage extends BaseStoragePage implements ITableView{
	
	private static final String EDIT_SELECTED_BOX_VIEW_ID = "edit_selected_box_view";
	private static final String ID_ATTR = "id";
	public static final String BOX_NAME_HEADER_ID = "box_name";
	private static final String STOCK_COUNT_HEADER_ID = "stock_count";
	
	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}

	public String addNewBox(String boxName,String amount) throws InterruptedException {
		showBoxes();
		TimeUnit.SECONDS.sleep(2);
		boolean hasBoxes = hasList();
		if(hasBoxes){
			clickOnButton("new_box");
			TimeUnit.SECONDS.sleep(2);
		}
		
		setMandatoryFields(boxName, amount);

		save();
		checkForNotyMessage();
		try {
			WebElement pageTitle = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='page-title']")));
			return pageTitle.getText();
		} catch (TimeoutException e ) {
			
			WebElement errorMsg = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='errorExplanation']/h2")));
			return errorMsg.getText();
		}

	}
	
	public boolean hasList() {
		
		//try to find the add box button which appears only when we already have boxes defined.
		try {
			driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new_box")));
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}
	
	/**
	 * Returns a list of all available columns fro the table in customize table view.
	 * @return list of id attribute for the headers
	 */
	public List<String> getAvailableColumnsForCustomiseTableView() {
		
		List<String> columns = new ArrayList<String>();
		columns.add("preferences_name");//box name
		columns.add("preferences_description");
		columns.add("preferences_created_at");
		columns.add("preferences_member_first_name");//owner
		columns.add("preferences_location");//location
		columns.add("preferences_linked_resources");//linked resource
		columns.add("preferences_box_type");//content type
		columns.add("preferences_stocks_count");
		columns.add("preferences_shared");//content type
		return columns;
	} 
	
	public String checkCustomizeTableView() throws InterruptedException {
		
        //select all available columns
        List<String> selectedColumns = checkPreference(getAvailableColumnsForCustomiseTableView()); 

        return checkTableHeaders(selectedColumns);
        
	}

	public String addNewBoxWithCellLine(String boxName,String amount) throws InterruptedException {
		
		boolean hasBoxes = hasList();
		if(hasBoxes){
			WebElement btnAddBox = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new_box")));
			btnAddBox.click();
			
			TimeUnit.SECONDS.sleep(2);
		}
		
		setMandatoryFields(boxName, amount);
		
		WebElement drpContent = getWebDriver().findElement(By.cssSelector(".select2-chosen"));
		drpContent.click();
		
		WebElement selectedContent = getWebDriver().findElement(By.xpath(".//*[@id='select2-drop']/ul/li[last()]/div"));
		selectedContent.click();
		
		save();
		
		
		WebElement pageTitle = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='page-title']")));
		return pageTitle.getText();
	}
	
	public String viewBoxShowPage(String itemToShow) throws InterruptedException{
		
		invokeSearchInBoxes(itemToShow);
		
		//find the index for 'box name' header
		int headerIndex = searchForColumnIndex(BOX_NAME_HEADER_ID);
		if(headerIndex == -1)
			return "Opps...no item found";
		List<WebElement> items = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@id='index_table']/tbody/tr")));
		for (int j = 2; j <= items.size(); j++) {
			WebElement nameItem = driverWait.until(ExpectedConditions.visibilityOfElementLocated
					(By.xpath(".//*[@id='index_table']/tbody/tr["+j+"]/td[" + headerIndex + "]/p[2]/a")));
			String itemName = nameItem.getText();
			if(itemName.equals(itemToShow)){
				nameItem.click();
				TimeUnit.SECONDS.sleep(2);
				break;
			}
		}
		
		try {
			WebElement titleElm = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("page-title")));
			String title = titleElm.getText();
			return title;
		} catch (TimeoutException e) {
			return "Page not loaded as expected";
		}

		
	}

	public int findSpecificBoxAfterSearchInvoked(String itemToShow) throws InterruptedException {
		
		
		//find the index for 'box name' header
		int headerIndex = searchForColumnIndex(BOX_NAME_HEADER_ID);
		int count = 0;
		
		if(headerIndex == -1)
			return count;
		List<WebElement> items = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@id='index_table']/tbody/tr")));
		for (int j = 2; j <= items.size(); j++) {
			WebElement nameItem = driverWait.until(ExpectedConditions.visibilityOfElementLocated
					(By.xpath(".//*[@id='index_table']/tbody/tr["+j+"]/td[" + headerIndex + "]/p[2]/a")));
			String itemName = nameItem.getText();
			if(itemName.equals(itemToShow)){
				count++;
			}
		}
		//no item was found
		return count;
	}
	
	/**
	 * Find the index of the given column name
	 * @return
	 * @throws InterruptedException 
	 */
	private int searchForColumnIndex(String headerName) throws InterruptedException {
		
		TimeUnit.SECONDS.sleep(3);
		List<WebElement> headers = getWebDriver().findElements(By.xpath(".//*[@id='index_table']/tbody/tr[1]/th"));
		int  headerIndex= 2;
		for ( ;headerIndex <= headers.size(); headerIndex++) {
			WebElement elem = getWebDriver().findElement(By.xpath(".//*[@id='index_table']/tbody/tr[1]/th[" + headerIndex +"]"));
			String header = elem.getAttribute(ID_ATTR);
			if(header.equals(headerName)){
				return headerIndex;
			}
		}
		return -1;//table not exist
	}
	
	public void invokeSearchInBoxes(String itemToSearch) {
			
		 WebElement txtSearch = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".searchtextbox")));
         sendKeys(txtSearch, itemToSearch);
        
         WebElement btnSearch = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@value='search-button']")));
         btnSearch.click();
		
	}

	private void setMandatoryFields(String boxName, String amount) {
		
		WebElement txtBoxName = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
		sendKeys(txtBoxName, boxName);
		
		WebElement boxToCreate = getWebDriver().findElement(By.id("number_of_boxes"));
		sendKeys(boxToCreate,amount);
		
		setBoxSize();
	}

	private void setBoxSize() {
		WebElement rows = getWebDriver().findElement(By.id("box_rows"));
		rows.clear();
		rows.sendKeys("3");
		sendKeys(rows, "3");
		WebElement cols = getWebDriver().findElement(By.id("box_cols"));
		cols.clear();
		cols.sendKeys("8");
	}

	public String addStockFromBox(String stockName) throws InterruptedException {
		
		addStock(stockName,1);

		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("BoxViewTable")));
		List<WebElement> tableCells =  getWebDriver().findElements(By.xpath(".//*[@id='BoxViewTable']/tbody/tr"));
		for (int cellIndex = 2; cellIndex <= tableCells.size();)  {
			WebElement linkName = getWebDriver().findElement(By.xpath(".//*[@id='BoxViewTable']/tbody/tr[2]/td["+cellIndex + "]/div/div[1]/a"));
			String shortName = linkName.getText();
			if(shortName.contains("."))
				shortName = shortName.substring(0, shortName.indexOf("."));
			if(stockName.startsWith(shortName)){
				linkName.click();
				WebElement title = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='page-title']")));
				return title.getText();
			}
		}
		return "Opps...Something went wrong";
	}

	

	public String createStockToDeleteArchiveFromPageView(String stockName) throws InterruptedException{
		
		addStock(stockName,2);
		
		WebElement tabTableView =  driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("table_view")));
		tabTableView.click();
		
		WebElement table = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='data']/table")));
		List<WebElement> tableCells =  table.findElements(By.xpath("//tr"));
		
		for (WebElement cell : tableCells) {
			WebElement linkToStock = cell.findElement(By.xpath("//td[3]/strong/a"));
			
			if(linkToStock.getText().equals(stockName)){
				linkToStock.click();
				driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='page-title']")));
				return getWebDriver().getTitle();
			}
		}
		
		return "Opps...Something went wrong";
	}
	

	public void addStock(String stockName,int numOfStocks) throws InterruptedException {
		
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("BoxViewTable")));
		List<WebElement> tableCells =  getWebDriver().findElements(By.xpath(".//*[@id='BoxViewTable']/tbody/tr"));
		int i=0;
		
		for (int cellIndex = 2; cellIndex <= tableCells.size();) {
			WebElement topElem = getWebDriver().findElement(By.xpath(".//*[@id='BoxViewTable']/tbody/tr[2]/td["+cellIndex + "]/div/div/a"));
			if(i < numOfStocks){
				topElem.click();
				TimeUnit.SECONDS.sleep(2);
				openStockSelectionDialogFromBoxPage(stockName, LGConstants.TUBE);		
				i++;
				cellIndex++;
			}else{
				break;
			}
		}
		
		TimeUnit.SECONDS.sleep(2);
	}

	public String duplicateStock(String stockName) throws InterruptedException {
		
		
		addStock(stockName,1);
		
		//start duplicate
		WebElement btnDuplicate = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("duplicate")));
		btnDuplicate.click();
		
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("drag-message")));
		
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("BoxViewTable")));
		List<WebElement> tableCells =  getWebDriver().findElements(By.xpath(".//*[@id='BoxViewTable']/tbody/tr"));
		boolean stockSelected = false;
		for (int cellInd = 2; cellInd <= tableCells.size();) {

			WebElement linkName = getWebDriver().findElement(By.xpath(".//*[@id='BoxViewTable']/tbody/tr[2]/td["+cellInd + "]/div/div[1]/a"));
			String shortName = linkName.getText();
			if(shortName.contains("."))
				shortName = shortName.substring(0, shortName.indexOf("."));
			WebElement stockCell;
			if(stockSelected){
				stockCell = getWebDriver().findElement(By.xpath(".//*[@id='BoxViewTable']/tbody/tr[2]/td["+cellInd + "]/div/div[2]/span"));
			}else{
				stockCell = getWebDriver().findElement(By.xpath(".//*[@id='BoxViewTable']/tbody/tr[2]/td["+cellInd + "]/div/div[2]/span[2]"));
			}
			
			if(stockName.startsWith(shortName) && !stockSelected){
				stockCell.click();
				TimeUnit.SECONDS.sleep(1);
				stockSelected = true;
				cellInd++;
				continue;
			}else if(stockSelected){//click an empty cell to duplicate
				stockCell = getWebDriver().findElement(By.xpath(".//*[@id='BoxViewTable']/tbody/tr[2]/td["+cellInd + "]/div/div[2]/span"));
				stockCell.click();
				TimeUnit.SECONDS.sleep(1);
				break;
			}
		}
		
		WebElement msgElem = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("drag-message")));
		String msg = msgElem.getText();
		//to finish duplicate
		btnDuplicate = getWebDriver().findElement(By.id("duplicate"));
		btnDuplicate.click();
		

		return msg;
	}


	public String deleteArchiveStockFromBoxView(String stockName,boolean delete) throws InterruptedException {
		
		addStock(stockName,1);
		TimeUnit.SECONDS.sleep(2);
		return deleteArchiveSelectedStock(stockName, delete);

	}

	public String deleteArchiveSelectedStock(String stockName, boolean delete) throws InterruptedException {
		
		selectStockInBoxView(stockName);
		
		WebElement btnDelete = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-from-box-view")));
		btnDelete.click();
		TimeUnit.SECONDS.sleep(2);
		
		return openDeleteItemPopup(delete);
	}
	
	public String deleteArchiveStockFromTableView(String stockName,boolean delete) throws InterruptedException {
		
		addStock(stockName,1);
		
		selectStockInTableView(stockName);
		
		WebElement btnDelete = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='doctoolsbox']/div/ul/li[3]/a[@id='delete_selected']")));
		btnDelete.click();
		TimeUnit.SECONDS.sleep(3);
		return openDeleteItemPopup(delete);

	}


	
	public boolean editStockFromBoxView(String stockName) throws InterruptedException {
		
		addStock(stockName,1);

		int cellInd = selectStockInBoxView(stockName);
		
		WebElement btnEdit = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(EDIT_SELECTED_BOX_VIEW_ID)));
		btnEdit.click();
		TimeUnit.SECONDS.sleep(2);
		editStockData();
		
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("BoxViewTable")));
		WebElement linkName =  getWebDriver().findElement(By.xpath(".//*[@id='BoxViewTable']/tbody/tr[2]/td[" + cellInd + "]/div[1]/div[1]/a"));

		
		return linkName.getText().equals("edited");

	}
	
	public boolean editStocksFromBoxView(String stockName) throws InterruptedException {
		
		addStock(stockName,2);

		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("BoxViewTable")));
		
		WebElement source = getWebDriver().findElement(By.xpath(".//*[@id ='BoxViewTable']/tbody/tr[2]/td[2]"));
		WebElement target = getWebDriver().findElement(By.xpath(".//*[@id ='BoxViewTable']/tbody/tr[2]/td[3]"));
			
		multiSelect(source, target);	
		
		WebElement btnEdit = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(EDIT_SELECTED_BOX_VIEW_ID)));
		btnEdit.click();
		TimeUnit.SECONDS.sleep(2);
		return editStocks();

	}

	public boolean editStockFromBoxTableView(String stockName) throws InterruptedException {
		
		addStock(stockName,1);
		
		WebElement tabTableView =  driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("table_view")));
		tabTableView.click();
		
		WebElement table = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='data']/table")));
		List<WebElement> tableCells =  table.findElements(By.xpath("//tr"));
		
		for (WebElement cell : tableCells) {
			WebElement linkToStock = cell.findElement(By.xpath("//td[3]/strong/a"));
			
			if(linkToStock.getText().equals(stockName)){
				WebElement chkSelect = cell.findElement(By.xpath("//td[1]/input[@type='checkbox']"));
				chkSelect.click();

				WebElement btnEdit = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit_selected")));
				btnEdit.click();
				
				editStockData();
				break;
			}
		}
		
		tabTableView =  driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("table_view")));
		tabTableView.click();
		
		table = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='data']/table")));
		tableCells =  table.findElements(By.xpath("//tr"));
		for (WebElement cell : tableCells) {
			WebElement linkToStock = cell.findElement(By.xpath("//td[3]/strong/a"));
			if(linkToStock.getText().equals("edited"))
				return true;

		}
		return false;
	
	}

	private void editStockData() throws InterruptedException {
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='stocks_edit']/h1")));
		
		List<WebElement> editBtnList = getWebDriver().findElements(By.cssSelector(".icon.icon-edit-pen"));
		WebElement editNamePen = editBtnList.get(1);
		editNamePen.click();
		TimeUnit.SECONDS.sleep(1);
		
		WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("stock_name")));
		txtName.clear();
		txtName.sendKeys("edited");
		
		
		WebElement btnSave = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Save")));
		btnSave.click();
		TimeUnit.SECONDS.sleep(2);
	}

	public boolean editStocksFromBoxTableView(String stockName,int numOfStocks) throws InterruptedException {
		
		addStock(stockName,numOfStocks);

		WebElement tabTableView =  driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("table_view")));
		tabTableView.click();
		TimeUnit.SECONDS.sleep(1);
		
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='data']/table")));

		for (int i = 0; i<numOfStocks;i++) {
			
			int index = i+numOfStocks;
			WebElement linkToStock = getWebDriver().findElement(By.xpath(".//tr["+ index +"]/td[3]/strong/a"));
			
			if(linkToStock.getText().equals(stockName)){	
				WebElement chkSelect = getWebDriver().findElement(By.xpath(".//tr["+ index +"]/td[1]/input[@type='checkbox']"));
				chkSelect.click();
				TimeUnit.SECONDS.sleep(1);
			}else{
				break;
			}
		
		}
		
		WebElement btnEdit = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit_selected")));
		btnEdit.click();
		
		return editStocks();
	}
	
	public boolean customizeTableView() {
		
		boolean isBoxNameHeaderAppears = isHeaderAppears(BOX_NAME_HEADER_ID);
		
		WebElement btnCustomizeTableView =  driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("customize")));
		btnCustomizeTableView.click();
		
		WebElement chkBoxName =  driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='preferences_name']")));
		
		//if we uncheck the box name - we should not find the 'Box name' header in the table view
		//if we check it - we should see it.
		chkBoxName.click();
		
		WebElement btnUpdate = getWebDriver().findElement(By.xpath(".//*[@value='Update']"));
		btnUpdate.click();
		
		return (isBoxNameHeaderAppears != isHeaderAppears(BOX_NAME_HEADER_ID));
	
		
		
	}

	/**
	 * Find if the header with the given id appears
	 * @param headerId
	 * @return true if header appears,false if not
	 */
	public boolean isHeaderAppears(String headerId) {

		try{
			getWebDriver().findElement(By.xpath(".//*[@id='" + headerId+ "']"));
			return true;
		}catch(NoSuchElementException e){
			return false;
		}

	}

	private boolean editStocks() throws InterruptedException {
		
		editStockData();
		
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("BoxViewTable")));
		WebElement linkName1 =   getWebDriver().findElement(By.xpath(".//*[@id ='BoxViewTable']/tbody/tr[2]/td[2]/div/div[1]/a"));
		WebElement linkName2 =  getWebDriver().findElement(By.xpath(".//*[@id ='BoxViewTable']/tbody/tr[2]/td[3]/div/div[1]/a"));

		
		return (linkName1.getText().equals("edited") && linkName2.getText().equals("edited") );
	}
	
	private int selectStockInBoxView(String stockName) throws InterruptedException {

		int cellInd = 2;
		List<WebElement> rowcells =  getWebDriver().findElements(By.xpath(".//*[@id='BoxViewTable']/tbody/tr"));
		for (cellInd = 2; cellInd <= rowcells.size(); cellInd++) {
			
			WebElement linkName = getWebDriver().findElement(By.xpath(".//*[@id='BoxViewTable']/tbody/tr[2]/td["+cellInd + "]/div/div[1]/a"));
			String shortName = linkName.getText();
			if(shortName.contains("."))
				shortName = shortName.substring(0, shortName.indexOf("."));
			
			WebElement stockCell = getWebDriver().findElement(By.xpath
					(".//*[@id='BoxViewTable']/tbody/tr[2]/td[" + cellInd + "]/div/div[2]/span[starts-with(@id,'box_view_stock_image_')]"));
			
			if(stockName.startsWith(shortName)){
				stockCell.click();
				TimeUnit.SECONDS.sleep(1);
				return cellInd;
			}

		}
		
		return cellInd;
	}
	
	private void selectStockInTableView(String stockName) throws InterruptedException{
		
		WebElement tabTableView =  driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("table_view")));
		tabTableView.click();
		
		WebElement table = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='data']/table")));
		List<WebElement> tableCells =  table.findElements(By.xpath("//tr"));
		
		for (WebElement cell : tableCells) {
			WebElement linkToStock = cell.findElement(By.xpath("//td[3]/strong/a"));
			
			if(linkToStock.getText().equals(stockName)){
				WebElement chkSelect = cell.findElement(By.xpath("//td[1]/input[@type='checkbox']"));
				chkSelect.click();
				TimeUnit.SECONDS.sleep(1);
				break;
			}
		}
		
	}
	
	private void multiSelect(WebElement source,WebElement target) throws InterruptedException{
	
		TimeUnit.SECONDS.sleep(1);
		Actions actions = new Actions(getWebDriver());
		actions.click(source);
		actions.keyDown(Keys.COMMAND);
		actions.click(target);
		actions.keyUp(Keys.COMMAND);
		actions.build().perform();

		TimeUnit.SECONDS.sleep(1);
	}

	public String deleteStocksFromTableView() throws InterruptedException {
		
		WebElement tabTableView =  driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("table_view")));
		tabTableView.click();
		TimeUnit.SECONDS.sleep(1);
		WebElement chkSelectAll =  driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("check_all")));
		chkSelectAll.click();
		TimeUnit.SECONDS.sleep(1);
		WebElement btnDelete = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//a[@href='/storage/stocks/archive_or_delete']/span[@id='delete-item']")));
		btnDelete.click();
		TimeUnit.SECONDS.sleep(1);
		return openDeleteItemPopup(true);
	}

	public int checkStocksNumber(String boxToSearch) throws InterruptedException {
		
		invokeSearchInBoxes(boxToSearch);
		
		int headerIndex = searchForColumnIndex(STOCK_COUNT_HEADER_ID);
		if(headerIndex == -1)
			return -1;
		List<WebElement> items = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@id='index_table']/tbody/tr")));
		for (int j = 2; j <= items.size();) {
			WebElement stockCountItem = driverWait.until(ExpectedConditions.visibilityOfElementLocated
					(By.xpath(".//*[@id='index_table']/tbody/tr["+j+"]/td[" + headerIndex + "]")));
			String stockCount = stockCountItem.getText();
			String onlyStock = stockCount;
			if(stockCount.indexOf('/') > 0)//not gridless box
				onlyStock = stockCount.substring(0, stockCount.indexOf('/'));
			return Integer.valueOf(onlyStock).intValue();
		}
		return -1;
	}

	public void searchBoxAndAddStockToBox(String boxToSearch,String stockName,int numOfTybesToAdd) throws InterruptedException {
		
		viewBoxShowPage(boxToSearch);		
		addStock(stockName, numOfTybesToAdd);
	}

	public void deleteAllItemsFromTable() throws InterruptedException  {
		showBoxes();
		TimeUnit.SECONDS.sleep(2);
		boolean hasBoxes = hasList();
		if(hasBoxes){
			super.deleteAllItemsFromTable();
		}
	}

	public void addNewGridlessBox(String boxName) throws InterruptedException {
		
		boolean hasBoxes = hasList();
		if(hasBoxes){
			WebElement btnAddBox = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new_box")));
			btnAddBox.click();
			
			TimeUnit.SECONDS.sleep(2);
		}
		
		setMandatoryFields(boxName, "1");
		
		WebElement checkGridless = getWebDriver().findElement(By.id("unlimited"));
		checkGridless.click();
		
		save();
		
	}


}
