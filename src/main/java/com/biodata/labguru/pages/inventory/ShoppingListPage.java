package com.biodata.labguru.pages.inventory;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
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
        String notyMsg = checkForNotyMessage();

		return notyMsg;
	}

	public String approveOrder() {
		
		//go over the list and find the first 'Approve' button
		List<WebElement> approveButtons = getWebDriver().findElements(By.cssSelector(".btn.approve_order[value='Approve Order']"));
		for (WebElement btnApprove : approveButtons) {
			String id = getId(btnApprove);
			btnApprove.click();
			btnApprove = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".btn.approve_order[value='Submit Order'][id^='submit_" + id + "']")));
			return btnApprove.getAttribute("value");
		}
		
		return "Could not approve - no button found";
	}

	public String approveAndSubmitOrder() {
		
		//go over the list and find the first 'Approve &Submit' button
		List<WebElement> appSubBtns = getWebDriver().findElements(By.cssSelector(".btn.approve_order[value='Approve & Submit Order']"));
		for (WebElement btnApprove : appSubBtns) {		
			String id = getId(btnApprove);
			btnApprove.click();
			//after approve&submit - we look for the 'mark as arrived' button
			btnApprove = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".order_arrived.mark_as_arrived[id^='mark_as_arrived_" + id + "']")));
			return btnApprove.getText();

		}	
		return "Could not approve&submit - no button found";
	}


	public boolean markOrderAsArrived(String boxName) throws InterruptedException {
		
		
		//go over the list and find the first 'Mark as arrived' button
		List<WebElement> btns = getWebDriver().findElements(By.cssSelector(".order_arrived.mark_as_arrived"));
		for (WebElement btnMarkedArrive : btns) {	
			String id = getId(btnMarkedArrive);
			String nameToSearch = getWebDriver().findElement(By.cssSelector(".item_details>tbody>tr>td>b>a[name^='lineitem_" + id + "']")).getText();
			btnMarkedArrive.click();
			TimeUnit.SECONDS.sleep(2);
			//open the storage selection dialog to select location for item
			openStockSelectionDialogFromShoppingList("stock1",boxName) ;	
			TimeUnit.SECONDS.sleep(5);
			return searchInOrderList(nameToSearch);
		}
		
		return false;
	}
	
	public boolean markOrderAsCancelled(String string) throws InterruptedException {
		//go over the list and find the first 'Cancel' button
		List<WebElement> cancelledBtns = getWebDriver().findElements(By.cssSelector(".link_btn[id^='cancel_']"));
		for (WebElement btnCancel : cancelledBtns) {	
			String id = getId(btnCancel);
			String cancelled = getWebDriver().findElement(By.cssSelector(".item_details>tbody>tr>td>b>a[name^='lineitem_" + id + "']")).getText();
			btnCancel.click();
			TimeUnit.SECONDS.sleep(2);
			//after cancel - check that item is no longer in the list
			invokeSearchItem(cancelled);
			waitForPageCompleteLoading();
			List<WebElement> searchResults = getWebDriver().findElements(By.xpath(".//*[@id='shopping_list_data']/div/*[@id='data']/div/table/tbody/tr"));
			if(searchResults.size() > 1)
				return true;
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

	private String getId(WebElement element) {
		String FullId = element.getAttribute("id");
		String id = FullId.substring(FullId.lastIndexOf('_') +1);
		return id;
	}


}
