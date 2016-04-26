package com.biodata.labguru.pages.storage;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.pages.AdminPage;
import com.biodata.labguru.pages.ITableView;

public class PlatePage extends AdminPage implements ITableView{

	
	public static final String PLATE_NAME_HEADER_ID = "name";
	
	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	@Override
	public List<String> getAvailableColumnsForCustomiseTableView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String checkCustomizeTableView() throws InterruptedException {
		// not implemented
		throw new UnsupportedOperationException("This option is not supported by this module");
	}

	@Override
	public boolean hasList() {
		//try to find the message that is shown if there are no plates yet - meaning the list is empty
		try {
			driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='main-content']/div/div[1]/a[2]")));
			return false;
		} catch (Exception e) {
			return true;//list is not empty
		}
		
	}

	public void selectPlateFromTable() {
		
		List<WebElement> rows = getWebDriver().findElements(By.xpath(".//*[@id='index_table']/tbody/tr"));
		for (int i = 2; i <= rows.size(); i++) {
			WebElement row = getWebDriver().findElement(By.xpath(".//*[@id='index_table']/tbody/tr[" + i + "]/td[4]/a"));
			row.click();
			driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("page-title")));
			break;
		}
	}

	public boolean addTagFromIndexTable() throws InterruptedException {
		
		List<WebElement> rows = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
				(By.xpath(".//*[@id='index_table']/tbody/tr")));
		
		String tagName = "tag_";
		//select first line in table and tag it
		for (int i = 2; i <= rows.size(); i++) {

			//find the index for 'plate name' header
			int headerIndex = searchForColumnIndex(PLATE_NAME_HEADER_ID);

			WebElement plateName = driverWait.until(ExpectedConditions.visibilityOfElementLocated
					(By.xpath(".//*[@id='index_table']/tbody/tr[" + i + "]/td["+ headerIndex + "]/a")));
			tagName = tagName + plateName.getText();
			WebElement chekbox = getWebDriver().findElement(By.xpath(".//*[@id='index_table']/tbody/tr[" + i + "]/td[1]/input"));
			chekbox.click();
			TimeUnit.SECONDS.sleep(1);
			break;
		}
		
		//tag the selected item
		WebElement tagAction = getWebDriver().findElement(By.cssSelector(".tag-action"));
		tagAction.click();
		TimeUnit.SECONDS.sleep(1);
		addTagWithName(tagName);
		
		clickOnGivenTag(tagName);
	
		
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("class_count")));
		rows = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
				(By.xpath(".//*[@id='index_table']/tbody/tr")));
	
		//we expect that the tag filter will only show one row for the plate we selected and tagged
		boolean succeeded =  (rows.size() == 2);
		
		//after adding the tag - delete it to clean tags list
		
		deleteTag(tagName);
		
		return succeeded;
	}

	private void deleteTag(String tagName) throws InterruptedException {
		selectAccountTagsMenu();
		
		List<WebElement> tags = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".css1")));
		for (WebElement tag : tags) {
			if(tag.getText().equals(tagName)){
				tag.click();
				TimeUnit.SECONDS.sleep(2);
				deleteTagFromTaggedEntitiesList();
				break;
			}
		}
	}

}
