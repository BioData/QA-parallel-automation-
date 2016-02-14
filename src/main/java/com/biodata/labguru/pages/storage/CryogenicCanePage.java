package com.biodata.labguru.pages.storage;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.pages.ITableView;


public class CryogenicCanePage extends BaseStoragePage implements ITableView{

	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	public String addCaryogenicCane(String name){
		
		if(hasList()){
			
			WebElement btnAdd = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new_cryogenic_cane")));
			btnAdd.click();
		}
		
		WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
		txtName.sendKeys(name);
		
		WebElement btnSave = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Save")));
		btnSave.click();
		
		WebElement  notyMsg = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".noty_text")));
		String msg = notyMsg.getText();
		notyMsg.click();
		return msg;
	}

	public boolean hasList() {
		//try to find the add cryogenic button which appears only when we already have canes defined.
		try {
			driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new_cryogenic_cane")));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
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

}
