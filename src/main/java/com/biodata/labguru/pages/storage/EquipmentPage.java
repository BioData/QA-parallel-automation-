package com.biodata.labguru.pages.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.model.EquipmentItem;
import com.biodata.labguru.pages.AdminPage;
import com.biodata.labguru.pages.ITableView;


public class EquipmentPage extends AdminPage implements ITableView{

	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	public String addNewEquipment(String name) {
		
		addEmptyEquipment(name);
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

	private void addEmptyEquipment(String name) {
		boolean hasItems = hasList();
		if(hasItems){
			WebElement btnAdd = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new_equipment")));
			btnAdd.click();
		}
		
		WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
		sendKeys(txtName, name);
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
	
	public EquipmentItem checkCreatedItem(EquipmentItem itemToCreate) throws InterruptedException {
		addEmptyEquipment(itemToCreate.name);
		addAllEquipmentData(itemToCreate);
        save();
        
        //load again show page to see that all data is saved well
        EquipmentItem item = new EquipmentItem();
        loadEquipmentData(item);
        return item;
	}

	private void loadEquipmentData(EquipmentItem item) {
		try {
			WebElement txtName = getWebDriver().findElement(By.id("page-title"));
			item.setName(txtName.getText());
			 
			WebElement txtOwner = getWebDriver().findElement(By.id("lg_info_tab_owner"));
			item.setOwner(txtOwner.getText());
			
			WebElement txtManufacturer = getWebDriver().findElement(By.xpath(".//*[@id='lg_info_tab_manufacturer']/a"));
			item.setManufacturer(txtManufacturer.getText());
			
			WebElement txtSirialNum = getWebDriver().findElement(By.id("lg_info_tab_serial_number"));
			item.setSerialNumber(txtSirialNum.getText());
			
			WebElement txtEquType = getWebDriver().findElement(By.id("lg_info_tab_equipment_type"));
			item.setEquipmentType(txtEquType.getText());
			
			WebElement txtModelNum = getWebDriver().findElement(By.id("lg_info_tab_model_number"));
			item.setModelNumber(txtModelNum.getText());
			
			WebElement txtPurchaseDate = getWebDriver().findElement(By.id("lg_info_tab_purchase_date"));
			item.setPurchaseDate(txtPurchaseDate.getText());
			
			WebElement txtWarrantyExpirationDate = getWebDriver().findElement(By.id("lg_info_tab_warranty_expiration_date"));
			item.setWarrantyExpirationDate(txtWarrantyExpirationDate.getText());
			
			WebElement txtMaintenanceDate = getWebDriver().findElement(By.id("lg_info_tab_maintenance_date"));
			item.setMaintenanceDate(txtMaintenanceDate.getText());
			
			
			WebElement txtMaintainInfo = getWebDriver().findElement(By.xpath(".//*[@id='lg_info_tab_maintenance_information']/div/p"));
			item.setMaintenanceInformation(txtMaintainInfo.getText());
			
			WebElement txtDescription = getWebDriver().findElement(By.xpath(".//*[@id='lg_info_tab_description']/div/p"));
			item.setDescription(txtDescription.getText());
			
			WebElement txtLocation = getWebDriver().findElement(By.xpath(".//*[@id='lg_info_tab_location']/a/span"));
			item.setLocation(txtLocation.getText());
			
		} catch (NoSuchElementException e) {
			Assert.fail("One of the info fields is missing: " + e.getMessage(), e);
		}

	}

	private void addAllEquipmentData(EquipmentItem itemToCreate) throws InterruptedException {

		setTextToField("equipment_manufacturer","Addgene");
		itemToCreate.setManufacturer("Addgene");
		
		setTextToField("serial_number","123456789");
		itemToCreate.setSerialNumber("123456789");
		
		setTextToField("equipment_type","Tube");
		itemToCreate.setEquipmentType("Tube");
		
		setTextToField("model_number","AD0007");
		itemToCreate.setModelNumber("AD0007");
		
		String date = setDate("purchase_date_date_picker",LGConstants.TODAY);
		itemToCreate.setPurchaseDate(date);
		
		date = setDate("warranty_expired_date_picker",LGConstants.TOMORROW);
		itemToCreate.setWarrantyExpirationDate(date);
		
		date = setDate("maintenance_date_date_picker",LGConstants.TOMORROW);
		itemToCreate.setMaintenanceDate(date);
		
		addTextToTextArea("maintenance_information",itemToCreate.name);
		itemToCreate.setMaintenanceInformation(itemToCreate.name);
		
		addTextToTextArea("description",itemToCreate.name);
		itemToCreate.setDescription(itemToCreate.name);
		
		WebElement treeNode = getWebDriver().findElement(By.cssSelector(".jqtree_common.jqtree-title.jqtree-title-folder>span"));
		treeNode.click();
		TimeUnit.SECONDS.sleep(1);
		itemToCreate.setLocation(treeNode.getText());
	}
	
	private String setDate(String inputId, String date) throws InterruptedException {
		WebElement datePicker = getWebDriver().findElement(By.id(inputId));
		datePicker.click();
		TimeUnit.SECONDS.sleep(1);
		if(date.equals(LGConstants.TODAY))
			selectToday(datePicker);
		else if(date.equals(LGConstants.TOMORROW))
			selectTomorrow(datePicker);
		datePicker = getWebDriver().findElement(By.xpath(".//*[@id='" + inputId + "']"));
		return datePicker.getAttribute("value");
	}

	private void setTextToField(String id, String text) {
		WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
		sendKeys(txtName, text);
	}

	protected void addTextToTextArea(String textAreaId ,String name){
		try {
			writeInRedactor(textAreaId, name);
		} catch (Exception e) {
			getLogger().debug("@@Error while writing in redactor");
		}
	}


}
