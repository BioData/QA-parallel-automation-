package com.biodata.labguru.pages.inventory.purchasables;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;


public class VirusPage extends PurchasableCollectionPage{

	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	@Override
	protected String getImportXPath() {
		
		return ".//*[@id='main-content']/div/div[1]/a[@href='/system/imports/new?class=Biocollections%3A%3AVirus']";
	}
	
	@Override
	protected String getCollectionName() {
		
		return LGConstants.VIRUSES;
	}

	@Override
	protected String getFileNameToImport() {
		return LGConstants.VIRUSES_TEMPLATE;
	}
	
	@Override
	public boolean isPurchasableEnabled(String collectionName) {
		return false;
	}
	
	@Override
	public List<String> getAvailableColumnsForCustomiseTableView() {
		List<String> columns = super.getAvailableColumnsForCustomiseTableView();
		columns.add("preferences_auto_name");//sysid
		columns.add("preferences_name");//name
		columns.add("preferences_virus_type");
		columns.add("preferences_gene_insert");
		columns.add("preferences_plasmid");
		columns.add("preferences_serotype_strain");
		columns.add("preferences_mutations_deletions");
		columns.add("preferences_tag");
		columns.add("preferences_selectable_marker");
		columns.add("preferences_producer_cell_type");
		columns.add("preferences_viral_coat");
		columns.add("preferences_tropism");
		columns.add("preferences_species");
		columns.add("preferences_storage_conditions");
		columns.add("preferences_safety_information");
		return columns;
	}

	@Override
	protected void addItemWithGivenName(String name) {
		clickNewButton("new_virus");
	      
        WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated
        		(By.id("name")));
        sendKeys(txtName, name);
		
	}

	@Override
	protected String getEditCollectionPrefix() {
		return "edit_biocollections_virus_";
	}

}
