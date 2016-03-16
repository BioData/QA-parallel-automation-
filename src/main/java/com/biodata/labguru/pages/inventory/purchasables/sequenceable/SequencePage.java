package com.biodata.labguru.pages.inventory.purchasables.sequenceable;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;

public class SequencePage extends SequenceableCollectionPage{

	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	
	@Override
	public boolean hasList() {
		try {
			WebElement title = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".empty_note")));
			return !title.getText().trim().equals("Start your sequence collection by adding one manually");
		} catch (Exception e) {
			return true;
		}
	}
	@Override
	protected String getImportXPath() {
		throw new UnsupportedOperationException("This action is not supported by this collection.");
	}

	@Override
	protected String getFileNameToImport() {
		throw new UnsupportedOperationException("This action is not supported by this collection.");
	}
	
	@Override
	public boolean isPurchasableEnabled(String collectionName) {
		return false;
	}
	
	@Override
	public List<String> getAvailableColumnsForCustomiseTableView() {
		List<String> columns = super.getAvailableColumnsForCustomiseTableView();
		columns.add("preferences_sysid");//sysid
		columns.add("preferences_title");//name
		columns.add("preferences_kind");//type
		columns.add("preferences_organism");
		columns.add("preferences_accsesion");
		columns.add("preferences_source_id");//belongs to
		columns.add("preferences_seq");//TODO - ask the developers to change the id to the same as in plasmid
		
		//columns that not appear in sequence
		columns.remove("preferences_alternative_name");
		columns.remove("preferences_source");
		columns.remove("preferences_sequence");//in sequence the pref id is different from the other 
		return columns;
	}

	@Override
	protected void addItemWithGivenName(String name) {
		clickNewButton("new_sequence");
	      
        WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated
        		(By.id("title")));
        sendKeys(txtName, name);
        
		addSequence();
		
	}
	
	@Override
	public String addNewItemWithSequence(String plasmidName) throws InterruptedException {
		
		addItemWithGivenName(plasmidName);    

		addSequence();
		
        save();
        
        //wait for the noty message
        String msg = checkForNotyMessage();
		       
        selectSequencesTab();
        
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tabs-sequences")));
        
		//try to add feature
		addFeatureToSeq();
		
		//check that the feature was added
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='myCollection_body']/table/tbody/tr[2]/td[1]")));
		
		String msgFeature = checkForNotyMessage();
		if(msgFeature.isEmpty())
			return msgFeature;//feature did not created successfully
		return msg;
  
	}

	@Override
	protected String getEditCollectionPrefix() {
		throw new UnsupportedOperationException("This action is not supported by this collection.");
	}

	
	@Override
	protected String getCollectionName() {
		
		return LGConstants.SEQUENCES;
	}

}
