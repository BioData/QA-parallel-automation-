package com.biodata.labguru.pages.inventory;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.pages.AdminPage;


public class ShoppingListPage extends AdminPage{
	
	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	
	public String addNewServiceRequest(){
	
		WebElement btnAddRequest = getWebDriver().findElement(By.xpath(".//*[@id='service_requests_data']/a"));
		btnAddRequest.click();
	        
		WebElement newServiceReqDialog = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new_service_request")));
      
        WebElement txtCompany = newServiceReqDialog.findElement(By.id("company"));
        sendKeys(txtCompany, "Company1");
        
        WebElement txtDescription = newServiceReqDialog.findElement(By.id("description"));
        sendKeys(txtDescription, "test request");
        
        WebElement txtQuantity = newServiceReqDialog.findElement(By.id("quantity"));
        sendKeys(txtQuantity, "10");
        
        WebElement txtPrice = newServiceReqDialog.findElement(By.id("material_price"));
        sendKeys(txtPrice, "500");

        save();
        
        getWebDriver().switchTo().activeElement();
       
        //get the noty message
        WebElement notyMsg = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".noty_message")));

		return notyMsg.getText();
	}

	public String approveOrder() {
		
		//go over the list and find the first 'Approve' button
		List<WebElement> items = getWebDriver().findElements(By.xpath(".//*[@id='shopping_list_data']/div/div[2]/div/table/tbody/tr"));
		for (int j = 3; j <= items.size(); j++) {
			try {
				//take the id and concat it to the id of the button
				WebElement idElm = getWebDriver().findElement(By.xpath(".//*[@id='shopping_list_data']/div/div[2]/div/table/tbody/tr["+j+"]/td[2]"));
				String id = idElm.getText();			
				WebElement btnApprove = getWebDriver().findElement(By.id("approve_" + id));
				btnApprove.click();
				btnApprove = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submit_" + id)));
				return btnApprove.getAttribute("value");
			} catch (NoSuchElementException e) {
				continue;
			}
			
		}
		return "Could not approve - no button found";
	}

	public String approveAndSubmitOrder() {
		
		//go over the list and find the first 'Approve &Submit' button
		List<WebElement> items = getWebDriver().findElements(By.xpath(".//*[@id='shopping_list_data']/div/div[2]/div/table/tbody/tr"));
		for (int j = 3; j <= items.size(); j++) {
			//take the id and concat it to the id of the button
			WebElement idElm = getWebDriver().findElement(By.xpath(".//*[@id='shopping_list_data']/div/div[2]/div/table/tbody/tr["+j+"]/td[2]"));
			
			String id = idElm.getText();
			try {
				WebElement btnApprove = getWebDriver().findElement(By.id("approve_and_submit_" + id));
				btnApprove.click();
				//after approve&submit - we look for the 'mark as arrived' button
				btnApprove = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='lineitem_"+ id + "']/td[8]/center/a")));
				return btnApprove.getText();
			} catch (NoSuchElementException e) {
				continue;
			}
			
		}	
		return "Could not approve&submit - no button found";
	}


	public boolean markOrderAsArrived(String boxName) throws InterruptedException {
		
		//go over the list and find the first 'Marked as Arrived' button
		List<WebElement> items = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@id='shopping_list_data']/div/div[2]/div/table/tbody/tr")));
		for (int j = 3; j <= items.size(); j++) {
			try {
				
				WebElement idElm = getWebDriver().findElement(By.xpath(".//*[@id='shopping_list_data']/div/div[2]/div/table/tbody/tr["+j+"]/td[2]"));
				String id = idElm.getText();
			
				WebElement btnMarkedArrive =  getWebDriver().findElement(By.xpath(".//*[@id='lineitem_"+ id + "']/td[8]/center/a"));
				
				WebElement txtNameOfArrived = getWebDriver().findElement(By.xpath(".//*[@name='lineitem_" + id + "']"));
				String nameToSearch = txtNameOfArrived.getText();
				btnMarkedArrive.click();
				 
				if(btnMarkedArrive.getAttribute("class").equals("open_fancy fancybox.ajax order_arrived mark_as_arrived")){
					//open the storage selection dialog to select location for item
					openStockSelectionDialogFromShoppingList("stock1",boxName) ;	
					
				}
	
				TimeUnit.SECONDS.sleep(5);
				return searchInOrderList(nameToSearch);
	
			} catch (NoSuchElementException e) {	
				continue;
			}
			
		}	
		return false;
	}

	private void openStockSelectionDialogFromShoppingList(String stockName,String selectedBox) throws InterruptedException {
		
	    TimeUnit.SECONDS.sleep(2); 
        WebElement newDialog = getWebDriver().switchTo().activeElement();
        driverWait.until(ExpectedConditions.visibilityOf(newDialog));
        
        WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
        if(stockName != null)
        	sendKeys(txtName, stockName);
   

        WebElement location = getWebDriver().findElement(By.xpath(".//*[@id='storage_locations_tree']/div/ul/li/div/span/span"));
        location.click();

        selectBox(selectedBox,false/*not edit mode*/);
	    
	    WebElement btnSave = getWebDriver().findElement(By.id("save"));
	    btnSave.click();
	    TimeUnit.SECONDS.sleep(1); 
	    
	    getWebDriver().switchTo().activeElement();

	}

	public boolean searchInOrderList(String nameToSearch) throws InterruptedException {
		 
		 WebElement linkToOrderHistory = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='page-title']/a")));
		 linkToOrderHistory.click();
		 TimeUnit.SECONDS.sleep(2);
		 
		 invokeSearchItem(nameToSearch);
		 TimeUnit.SECONDS.sleep(2);
		 
		 try {
			 //look if there is at least one result match
			driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='table_data']/tbody/tr[3]")));
			return true;
		 } catch (Exception e) {
			return false;
		}
	
	}
	
	/** 
	 * This dialog opens when clicking on 'mark as arrived' 
	 * @throws InterruptedException 
	 */
	public void openStorageSelectionDialog() throws InterruptedException {
	
	    TimeUnit.SECONDS.sleep(2);  
	    
		List<WebElement> closeStorages = getWebDriver().findElements(By.cssSelector(".jqtree-folder.jqtree_common.jqtree-closed>div>span>span"));
		for (WebElement storage : closeStorages) {
			//select the first storage under the 'Lab Room'
			storage.click();
			TimeUnit.SECONDS.sleep(1);
			break;
		}
	    
	    WebElement btnSave = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save")));
	    btnSave.click();
		   			
	}

}
