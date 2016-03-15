package com.biodata.labguru.pages.storage;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import com.biodata.labguru.pages.AdminPage;



public class StoragePage extends AdminPage{
		

	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	public String addNewStorage(String name) throws InterruptedException {
			
		int lastNodeIndex = collapseAllNodes();
		WebElement lblAddNew = getWebDriver().findElement(By.xpath(".//*[@id='storages_tree']/ul/li[1]/ul/li["+ lastNodeIndex + "]/div/span/span"));
		lblAddNew.click();
		TimeUnit.SECONDS.sleep(1);
		
		WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.id("name")));
		sendKeys(txtName, name);
		
		save();
		
		TimeUnit.SECONDS.sleep(2);
		
		WebElement selectedStorage = getSelectedNode();
		String storage =  selectedStorage.getText();
		return storage;
	}

	
	public String addNewStorageByType(String name,int typeIndex) throws InterruptedException{
		
		int lastNodeIndex = collapseAllNodes();
		WebElement lblAddNew = getWebDriver().findElement(By.xpath(".//*[@id='storages_tree']/ul/li[1]/ul/li["+ lastNodeIndex + "]/div/span/span"));
		lblAddNew.click();
		TimeUnit.SECONDS.sleep(1);
		
		WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.id("name")));
		sendKeys(txtName, name);
		

		String script = "return document.getElementById('select_storage_type').getElementsByTagName('option')[" +typeIndex + "].value;";
		String value = (String) executeJavascript(script);
		TimeUnit.SECONDS.sleep(1);
		new Select(getWebDriver().findElement(By.id("select_storage_type"))).selectByValue(value);
		TimeUnit.SECONDS.sleep(2);
		
		if(name.equals("Vertical Rack")){
			WebElement rows = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("rows")));
			rows.sendKeys("2");
		}else if(name.equals("Slide Rack") || name.equals("Horizontal Rack")){
			WebElement rows = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("rows")));
			rows.sendKeys("2");
			
			WebElement cols = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cols")));
			cols.sendKeys("4");
		}
		save();
		
		TimeUnit.SECONDS.sleep(2);

		
		getSelectedNode();
		WebElement typeElem = getWebDriver().findElement(By.id("lg_info_tab_type"));
		String type =  typeElem.getText();
		return type;
	
	}

	public String editStorage(String name) throws InterruptedException {
		//take the selected 
		TimeUnit.SECONDS.sleep(2);
		
		WebElement selectedStorage = getSelectedNode();
		if(selectedStorage.getText().equals(name)){

			TimeUnit.SECONDS.sleep(1);
			
			clickOnEdit();
			
			TimeUnit.SECONDS.sleep(2);
			
			WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated
					(By.id("name")));
			String newName = name + "_update";
			sendKeys(txtName, newName);
			
			WebElement txtTemp = driverWait.until(ExpectedConditions.visibilityOfElementLocated
					(By.id("system_storage_storage_temperature")));
			String oldTemprature = txtTemp.getAttribute("value");
			int oldTempInt = Integer.valueOf(oldTemprature).intValue();
			int newTemp = oldTempInt + 10;
			sendKeys(txtTemp,String.valueOf(newTemp));
			
			save();
			
			TimeUnit.SECONDS.sleep(5);
			refreshPage();
			//delete the storage after test ends
			deleteStorage(newName);
			
			return newName; 
		}
		return name;
	}



	public WebElement getSelectedNode() {
		
		WebElement selectedStorage = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.cssSelector(".jqtree_common.jqtree-selected>div>span")));
		if(!selectedStorage.getAttribute("class") .equals("jqtree_common jqtree-title jqtree-title-folder")){
			selectedStorage = getWebDriver().findElement(By.cssSelector(".jqtree_common.jqtree-selected>div>span>span"));
		}
		return selectedStorage;
	}
	
	public String deleteStorage(String name) throws InterruptedException{
		
		TimeUnit.SECONDS.sleep(2);
		
		WebElement selectedStorage = getSelectedNode();
		
		if(selectedStorage.getText().equals(name)){
			
			TimeUnit.SECONDS.sleep(1);
			
			WebElement btnDelete = driverWait.until(ExpectedConditions.visibilityOfElementLocated
					(By.id("delete-item")));
			TimeUnit.SECONDS.sleep(1);
			btnDelete.click();
			TimeUnit.SECONDS.sleep(1);
			checkForAlerts();
			
			return checkForNotyMessage();
		}
		return "storage not found";
	}
	
	
	public boolean changeStorageLocation(String name) throws InterruptedException {
		
		TimeUnit.SECONDS.sleep(2);
		
		WebElement selectedStorage = getSelectedNode();
		if(selectedStorage.getText().equals(name)){
			
			WebElement storageTitle = driverWait.until(ExpectedConditions.visibilityOfElementLocated
					(By.xpath(".//*[@id='Storages_desc']/h3")));
			String oldLocation = storageTitle.getText();
			
			clickOnEdit();
	
			TimeUnit.SECONDS.sleep(2);
	
			String script = "var storage = document.getElementById('select_storage');"
					+ "var option = storage.getElementsByTagName('option')[3];"
			        + "return option.value;";
	
			String value = (String) executeJavascript(script);
			TimeUnit.SECONDS.sleep(1);
			new Select(getWebDriver().findElement(By.id("select_storage"))).selectByValue(value);
			TimeUnit.SECONDS.sleep(1);
			
			save();
			TimeUnit.SECONDS.sleep(5);
			
			storageTitle = driverWait.until(ExpectedConditions.visibilityOfElementLocated
					(By.xpath(".//*[@id='Storages_desc']/h3")));
			String storage = storageTitle.getText();
			
			refreshPage();
			deleteStorage(name);
			return !storage.equals(oldLocation);
		}
		return false;
	}
	
	public void expandAllStorageTree() {

		List<WebElement>  closedNodes = getWebDriver().findElements(By.cssSelector(".jqtree_common.jqtree-folder.jqtree-closed>div>a"));	
		
		for (WebElement node : closedNodes) {
				node.click();
				closedNodes = getWebDriver().findElements(By.cssSelector(".jqtree_common.jqtree-folder.jqtree-closed>div>a"));	
		}
	}
	

	public int collapseAllNodes() throws InterruptedException {
		List<WebElement> folders = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
				(By.xpath(".//*[@id='storages_tree']/ul/li[1]/ul/li")));
		int size = folders.size();
		for (int i=1 ; i<size ;i++) {
			
			WebElement folder = getWebDriver().findElement(By.xpath(".//*[@id='storages_tree']/ul/li[1]/ul/li[" + i + "]"));
			if(folder.getAttribute("class").equals("jqtree_common jqtree-folder jqtree-closed")){
				WebElement arrow = getWebDriver().findElement(By.xpath(".//*[@id='storages_tree']/ul/li[1]/ul/li[" + i + "]/div/a"));
				if(arrow.getAttribute("class").equals("jqtree_common jqtree-toggler jqtree-closed")){
					arrow.click();
					TimeUnit.SECONDS.sleep(1);
				}
			}
		}
		return size;
	}
	
	private void clickOnEdit() {
		WebElement btnEdit = getWebDriver().findElement(By.xpath(".//*[@id='edit_link']/span"));
		btnEdit.click();
	}
	
	/**
	 * Look for the given box name in the tree and check it
	 * @param boxName
	 * @throws InterruptedException 
	 */
	public void selectBoxNodeWithName(String boxName) throws InterruptedException {

		List<WebElement> levelOneFolders = getWebDriver().findElements(By.xpath(".//*[@id='storages_tree']/ul/li"));
		for (int i = 1; i <= levelOneFolders.size(); i++) {
			WebElement level = getWebDriver().findElement(By.xpath(".//*[@id='storages_tree']/ul/li[" + i + "]/div/span/span"));
			if(level.getAttribute("class").equals("unstored_boxes")){
				WebElement toggler = getWebDriver().findElement(By.xpath(".//*[@id='storages_tree']/ul/li[" + i + "]/div/a"));
				toggler.click();
				TimeUnit.SECONDS.sleep(1);
				break;
			}
		}
		
		//find the given box to check
		List <WebElement> boxes= getWebDriver().findElements(By.xpath(".//*[starts-with(@id,'box_')]"));
		for (WebElement box : boxes) {
			if(box.getText().equals(boxName)){
				box.click();
				TimeUnit.SECONDS.sleep(2);
				break;
			}
		}
	
	}

}
