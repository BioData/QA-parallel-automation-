package com.biodata.labguru.pages.inventory.purchasables.sequenceable;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;

public class PrimersPage extends SequenceableCollectionPage{

	
	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	@Override
	protected String getImportXPath() {
		return ".//*[@id='main-content']/div/div[1]/a[@href='/system/imports/new?class=Biocollections%3A%3APrimer']";
	}

	@Override
	protected String getFileNameToImport() {
		return LGConstants.PRIMERS_TEMPLATE;
	}
	
	@Override
	protected String getCollectionName() {
		
		return LGConstants.PRIMER_PREFIX;
	}
	
	@Override
	public List<String> getAvailableColumnsForCustomiseTableView() {
		List<String> columns = super.getAvailableColumnsForCustomiseTableView();
		columns.add("preferences_auto_name");//sysid
		columns.add("preferences_title");//name
		columns.add("preferences_gene_id");
		columns.add("preferences_orientation");
		columns.add("preferences_target_position");
		columns.add("preferences_tm");
		columns.add("preferences_fragment_size");
		columns.add("preferences_organism");
		columns.add("preferences_used_for");//application
		columns.add("preferences_restriction_site");
		columns.add("preferences_tag");
		columns.add("preferences_sequence");
		return columns;
	}

	@Override
	protected void addItemWithGivenName(String plasmidName) {
		clickNewButton("new_primer");
	      
        WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated
        		(By.id("title")));
        sendKeys(txtName, plasmidName);
	}
	
	@Override
	protected String getEditCollectionPrefix() {
		return "edit_biocollections_primer_";
	}
}
