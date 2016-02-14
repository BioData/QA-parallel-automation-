package com.biodata.labguru.pages.storage;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.pages.AdminPage;
import com.biodata.labguru.pages.ITableView;


public class EquipmentPage extends AdminPage implements ITableView{

	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	public String addNewEquipment(String name) {
		
		boolean hasItems = hasList();
		if(hasItems){
			WebElement btnAdd = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new_equipment")));
			btnAdd.click();
		}
		
		WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
		sendKeys(txtName, name);
		save();
		
		WebElement pageTitle;
		try {
			pageTitle = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("page-title")));
			return pageTitle.getText();
		} catch (TimeoutException e ) {
			
			WebElement errorMsg = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='errorExplanation']/h2")));
			return errorMsg.getText();
		}
	}

	public boolean hasList() {
		//try to find the add box button which appears only when we already have equipment defined.
		try {
			WebElement title = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".empty_note")));
			return !title.getText().trim().equals("Start your equipment collection by adding one manually");
		} catch (Exception e) {
			return true;
		}
	}

	@Override
	public List<String> getAvailableColumnsForCustomiseTableView() {
		
		List<String> columns = new ArrayList<String>();
		columns.add("preferences_name");//name
		columns.add("preferences_description");
		columns.add("preferences_created_at");
		columns.add("preferences_member_first_name");//owner
		columns.add("preferences_storage_id");//location
		columns.add("preferences_linked_resources");//linked resource
		columns.add("preferences_company_id");//manufacturer
		columns.add("preferences_serial_number");
		columns.add("preferences_equipment_type");
		columns.add("preferences_model_number");
		columns.add("preferences_purchase_date");
		columns.add("preferences_warranty_expired");
		columns.add("preferences_maintenance_date");
		columns.add("preferences_maintenance_information");
		columns.add("preferences_sharing");
		return columns;
	}
	
	public String checkCustomizeTableView() throws InterruptedException {
		
        //select all available columns
        List<String> selectedColumns = checkPreference(getAvailableColumnsForCustomiseTableView()); 

        return checkTableHeaders(selectedColumns);
        
	}

}
