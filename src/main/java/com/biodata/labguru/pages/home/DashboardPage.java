package com.biodata.labguru.pages.home;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.pages.AdminPage;


public class DashboardPage extends AdminPage{
	
	private final String ALL_TASKS_BREADCRUMBS = "all tasks";
	
	@Override
	protected  void initPage(WebDriver webDriver) {
		
		PageFactory.initElements(webDriver, DashboardPage.class);

	}

	public String inviteMembers(String member) {

		WebElement textEmail = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email-text-field")));
		textEmail.sendKeys(member);
		
		WebElement btnSubmit = driverWait.until(ExpectedConditions.elementToBeClickable(By.id("submit-btn")));
		btnSubmit.submit();

		
		String msg = checkForNotyMessage();
		return msg;
	
	}
	
	/**
	 * Open the page that shows all tasks
	 * @return true if found the all tasks breadcrumbs
	 * @throws InterruptedException
	 */
	public boolean goToAllTasks() throws InterruptedException {
		
		WebElement btnAllTasks = driverWait.until(ExpectedConditions.elementToBeClickable(By.id("all_tasks_link")));
		btnAllTasks.click();
		
		TimeUnit.SECONDS.sleep(1);
		
		WebElement labelAllTasks = driverWait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@id='breadcrumbs']/ul/li/span/a")));
		
		if(labelAllTasks.getText().equals(ALL_TASKS_BREADCRUMBS))
			return true;
		return false;
	}
	
	/**
	 * Simple search on task name
	 * @param itemToSearch - the task name
	 * @return true if search succeeded
	 * @throws InterruptedException
	 */
	public boolean invokeSearchTask(String itemToSearch) throws InterruptedException{
		
		closeIridizePopups();
		
		 WebElement txtSearch = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".searchtextbox")));
         sendKeys(txtSearch, itemToSearch);
        
         WebElement btnSearch = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='simple_search']/form/*[@value='search-button']")));
         btnSearch.click();
         TimeUnit.SECONDS.sleep(3);
         
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("class_count"))); 
        List<WebElement> results = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
        		(By.xpath(".//*[@id='index_table']/tbody/tr"))); 
        
        //if there are more than one matching result (as should be) - return false
        if(results.size() > 2)
        	return false;
        
        for (int i = 2; i <= results.size(); i++) {
        	WebElement name = getWebDriver().findElement(By.xpath(".//*[@id='index_table']/tbody/tr[" + i + "]/td[2]/span/a")); 
        	//if the name is not matching - return false
        	if(!name.getText().equals(itemToSearch))
        		return false;
		}
        return true;
	}
	
	/**
	 * only invoked after clicking 'all tasks' to get to the all tasks page
	 */
	@Override
	public void deleteAllItemsFromTable() throws InterruptedException {
		
		String allTasksUrl = getWebDriver().getCurrentUrl();
		
		List<WebElement> results = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
        		(By.xpath(".//*[@id='index_table']/tbody/tr"))); 
		
		//go over all tasks and delete them one by one
		int i = 2;
        while(results.size() > 2) {
        	//go to task show page
        	WebElement name = getWebDriver().findElement(By.xpath(".//*[@id='index_table']/tbody/tr[" + i + "]/td[2]/span/a")); 
        	name.click();
        	TimeUnit.SECONDS.sleep(2);
        	WebElement btnDelete = getWebDriver().findElement(By.cssSelector(".delete"));
    		btnDelete.click();
    		checkForAlerts();
    		TimeUnit.SECONDS.sleep(2);
    		waitForPageCompleteLoading();
    		//back to akll tasks
    		getWebDriver().get(allTasksUrl);
    		results = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
            		(By.xpath(".//*[@id='index_table']/tbody/tr"))); 
    		
		}
	}
	
	public String checkLowStockAlerts() {
		
		showDashboard();
		try {
			WebElement numberAlert = getWebDriver().findElement(By.cssSelector(".overdue-tasks-counter.fright"));
			return numberAlert.getText();
		} catch (NoSuchElementException e) {
			//alert number not found
			return "";
		}
		
	}

}
