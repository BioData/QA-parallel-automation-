package com.biodata.labguru.pages.inventory.purchasables;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;


public class AntibodiesPage extends PurchasableCollectionPage{
	
	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	@Override
	protected String getFileNameToImport() {
		
		return LGConstants.ANTIBODIES_TEMPLATE;
	}
	
	@Override
	public String getImportXPath() {
		
		return ".//*[@id='main-content']/div/div[1]/a[@href ='/system/imports/new?class=Biocollections%3A%3AAntibody']";
	}
	
	@Override
	public List<String> getAvailableColumnsForCustomiseTableView() {
		List<String> columns = super.getAvailableColumnsForCustomiseTableView();
		columns.add("preferences_auto_name");//sysid
		columns.add("preferences_title");//name
		columns.add("preferences_immune");//clonallity
		columns.add("preferences_organism_id");//raised in
		columns.add("preferences_reacts_with");
		columns.add("preferences_antigene");
	
		return columns;
	}

	@Override
	protected void addItemWithGivenName(String name) {
		clickNewButton("new_antibody");
	      
        WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated
        		(By.id("title")));
        sendKeys(txtName, name);    
	}

	public String addAntibodyFromOnlineDir() throws Exception {
			
		showDirecrory("add_from_antibodies_online");
		
		return addItemFromDirectory();
	}


	public String showAntibodyFromDirectory() throws InterruptedException {
		
		showDirecrory("add_from_antibodies_online");
		
		List<WebElement> tableRows = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@id='data']/table/tbody/tr")));
		int cellInd = 2;
		for (WebElement row : tableRows) {
			WebElement link =  row.findElement(By.xpath("//tr[" + cellInd + "]/td[2]/a"));	
			if(link != null){
				link.click();
				break;
			}
			cellInd++;
		}
		
		
		WebElement logo = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='main-content']/div/div[3]/img")));
		return logo.getAttribute("alt");
	}

	@Override
	protected String getEditCollectionPrefix() {
		return "edit_biocollections_antibody_";
	}
	
	@Override
	protected String getCollectionName() {
		
		return LGConstants.ANTIBODIES;
	}

}
