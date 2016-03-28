package com.biodata.labguru.pages.knowledgebase;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.pages.AdminPage;
import com.biodata.labguru.pages.IListView;


public abstract class AbstractKnowledgebasePage extends AdminPage implements IListView{

	public abstract boolean hasList();
	
	public String getPageTitleXPath(){
		return ".//*[@id='page-title']";
	}
	
	
	public boolean searchForItem(String itemToSerch) throws InterruptedException {
		
		waitForPageCompleteLoading();
		
		invokeSearchItem(itemToSerch);
        
        return hasList();
	}

	public String duplicateItem() throws InterruptedException {
		
		TimeUnit.SECONDS.sleep(2);
		
		WebElement btnDuplicate = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".duplicate")));
		btnDuplicate.click();
		
		TimeUnit.SECONDS.sleep(1);
		checkForAlerts();
		TimeUnit.SECONDS.sleep(2);
		
		String title = getTitle();
        return title;
	}

	public String getTitle() {
		WebElement pageTitle = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(getPageTitleXPath())));
        String newName = pageTitle.getText();
        return newName;
	}
	
	public void deleteKnowledgebaseItem() throws InterruptedException {
		
		executeJavascript("$('#delete-item').click();");
	   	checkForAlerts();
	}

	public boolean archiveKnowledgebaseItem() throws InterruptedException {
		
		executeJavascript("$('.icon.icon-lock').click();");
	   
		String msg = checkForNotyMessage();

		return msg.endsWith("Archived");
	}
	
	public String activateArchivedItemFromNotyMessage() throws InterruptedException{
		
		executeJavascript("$('.icon.icon-lock').click();");
		
		WebElement linkActivate = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@class='noty_bar']/div/span/a")));
		
		linkActivate.click();
		TimeUnit.SECONDS.sleep(2);
		WebElement title = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".noty_text")));
		
		return title.getText();
	}

	public boolean checkInArchivedList(String resource) throws InterruptedException {
		
		WebElement viewArchiveList = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='index-header']/h1/a")));
		viewArchiveList.click();
		TimeUnit.SECONDS.sleep(2);
		invokeSearchItem(resource);
		return hasList();
	}

	public boolean searchForItemInList(String itemToSerch) throws InterruptedException {
		
		waitForPageCompleteLoading();
		
		invokeSearchItem(itemToSerch);
		
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("index-header")));
		try {
			//no results after search
			getWebDriver().findElement(By.id("no_search_results"));
			return false;
			
		} catch (NoSuchElementException e) {
			return true;//item found in list
		}
	}
}
