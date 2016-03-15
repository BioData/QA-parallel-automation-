package com.biodata.labguru.pages.inventory.purchasables.sequenceable;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;

public class PlasmidsPage extends SequenceableCollectionPage{

	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	@Override
	protected String getEditCollectionPrefix() {
		return "edit_biocollections_plasmid_";
	}
	
	@Override
	protected String getFileNameToImport() {
		
		return LGConstants.PLASMIDS_TEMPLATE;
	}
	
	@Override
	protected String getCollectionName() {
		
		return LGConstants.PLASMIDS;
	}
	
	@Override
	public String getImportXPath() {
		
		return ".//*[@id='main-content']/div/div[1]/a[@href='/system/imports/new?class=Biocollections%3A%3APlasmid']";
	}
	
	@Override
	public List<String> getAvailableColumnsForCustomiseTableView() {
		List<String> columns = super.getAvailableColumnsForCustomiseTableView();
		columns.add("preferences_auto_name");//sysid
		columns.add("preferences_title");//name
		columns.add("preferences_length");
		columns.add("preferences_resistance");
		columns.add("preferences_insert");
		columns.add("preferences_usage");
		columns.add("preferences_host");
		columns.add("preferences_parent_id");//base vector
		columns.add("preferences_clone_number");
		
	
		return columns;
	}
	
	@Override
	protected void addItemWithGivenName(String name) {

		clickNewButton("new_plasmid");
	      
		
        WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated
        		(By.id("title")));
        sendKeys(txtName, name);
	}


	public String addPlasmidFromAddgene() throws Exception {
		
		showDirecrory("plasmids_directory");
		
		return addItemFromDirectory();
		
	}

	public String showPlasmidFromDirectory() throws InterruptedException {
		
		showDirecrory("plasmids_directory");
		
		List<WebElement> tableRows = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@id='data']/table/tbody/tr")));
		int cellInd = 2;
		for (WebElement row : tableRows) {
			WebElement link =  row.findElement(By.xpath("//tr[" + cellInd + "]/td[3]/a"));	
			if(link != null){
				link.click();
				break;
			}
			cellInd++;
		}
		
		
		WebElement logo = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='addgene_logo']/img")));
		return logo.getAttribute("alt");
	}

	@Override
	public String addNewItemWithSequence(String plasmidName) throws InterruptedException {
		
		String msg = super.addNewItemWithSequence(plasmidName);
        
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='tabs-sequences']/div[4]")));
		//try to add feature
		addFeatureToSeq();
		
		//check that the feature was added
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='myCollection_body']/table/tbody/tr[2]/td[1]")));
		
		String msgFeature = checkForNotyMessage();
		if(msgFeature.isEmpty())
			return msgFeature;//feature did not created successfully
		return msg;
  
	}
}
