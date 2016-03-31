package com.biodata.labguru.pages.inventory.rodents;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;


public class RodentStrainsPage extends RodentPage{

	
	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	@Override
	protected String getEditCollectionPrefix() {
		return "edit_biocollections_rodent_strain_";
	}
	
	
	@Override
	protected String getCollectionName() {
		return "rodent_" + LGConstants.RODENT_STRAIN_PREFIX;
	}
	
	@Override
	protected String getFileNameToImport() {
		
		return LGConstants.RODENT_STRAIN_TEMPLATE;
	}
		
	@Override
	protected String getCustomizeLinkXpath(String collectionName) {
		
		return ".//a[@href='/system/custom_fields?class=Biocollections::RodentStrain']";

	}
	
	@Override
	public List<String> getAvailableColumnsForCustomiseTableView() {
		List<String> columns = super.getAvailableColumnsForCustomiseTableView();
		columns.add("preferences_auto_name");//sysid
		columns.add("preferences_specimens");
		columns.add("preferences_genotype");
		columns.add("preferences_phenotype");
		columns.add("preferences_transgene");

		return columns;
	}

	protected void addItemWithGivenName(String name) {

		clickNewButton("new_rodent_strain");
	      
        WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated
        		(By.id("name")));
        sendKeys(txtName, name);
	}

	@Override
	protected String getImportXPath() {
		return ".//*[@id='main-content']/div/div[1]/a[@href='/system/imports/new?class=Biocollections%3A%3ARodentStrain']";
	}


	public String addSpecimenFromStrain(String specimenName,int numOfSpecimensToCreate,boolean checkCreation) throws InterruptedException {
		selectSpecimensTab();
		
		WebElement btnCreateSpec = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("specimens")));
		btnCreateSpec.click();
		
		TimeUnit.SECONDS.sleep(2);
		 
		getWebDriver().switchTo().activeElement();
		
		WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated
	        		(By.xpath(".//*[@id='create_specimens']/div/input[@id='name']")));
	    txtName.sendKeys(specimenName);
	    
	    TimeUnit.SECONDS.sleep(2);
	    
        WebElement input = driverWait.until(ExpectedConditions.visibilityOfElementLocated
        		(By.xpath(".//*[@id='create_specimens_cage']/option[last()]")));
        input.click();
        String cage = input.getText();
	    
        WebElement maleInput = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("create_specimens_count_male")));
        maleInput.click();
        sendKeys(maleInput, String.valueOf(numOfSpecimensToCreate));
        
        
        WebElement btnCreate = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("create_specimens_submit")));
        btnCreate.click();
        TimeUnit.SECONDS.sleep(2);
        String msg = checkForAlerts();
        
        if(!msg.equals("")){
        	WebElement btnCancel = driverWait.until(ExpectedConditions.visibilityOfElementLocated
        			(By.xpath(".//*[@id='create_specimens_form']/div[2]/a")));
        	btnCancel.click();
        	TimeUnit.SECONDS.sleep(2);
    	    getWebDriver().switchTo().activeElement();
        	return msg;
        }
        
        while(getWebDriver().findElement(By.xpath(".//*[@id='create_specimens_form']/div[2]/a")).getText().equals("Cancel"));
        
	    TimeUnit.SECONDS.sleep(2);
	    getWebDriver().switchTo().activeElement();
	    specimenName = specimenName + ".1";
	    if(checkCreation)
	    	return checkSpecimenCreation(specimenName, cage); 
	    else
	    	return specimenName;
	}
	
	@Override
	public String showCollection(String collectionName) {
		
		return showRodentStrains();
	}
}
