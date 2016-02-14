package com.biodata.labguru.tests.inventory.purchasables;

import static org.testng.AssertJUnit.assertEquals;

import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.inventory.purchasables.PurchasableCollectionPage;


public class BacteriaTest extends PurchasableCollectionTest{
	
	@Override
	public String getCollectionId() {
		return LGConstants.BACTERIA;
	}
	
	@Override
	protected String getPrefix() {
		return LGConstants.BACTERIA_PREFIX;
	}
	
	@Override
	protected PurchasableCollectionPage getPage() {
		
		return getPageManager().getBacteriaPage();
	}
	
	@Override
	public void showMenu(){

		try {
			String pageTitle = showTableIndex();
			
			if(getPageManager().getBacteriaPage().hasList()){
			    // Check the title of the page when we already have some items
				AssertJUnit.assertEquals(getMessageSource().getMessage("bacteria.title.has.bacteria",null, Locale.US), pageTitle);
				getPage().deleteAllItemsFromTable();
			}
			
			pageTitle = showTableIndex();
			// Check the title of the page when we does'nt have items
			AssertJUnit.assertEquals(getMessageSource().getMessage("bacteria.title.no.bacteria",null, Locale.US), pageTitle);
			
		} catch (Exception e) {
			setLog(e,"canSelectBacteria");
			Assert.fail(e.getMessage());
		}

	}
	
	@Override
	public void addSimpleItem(){
		
		try {
			getPageManager().getAdminPage().showBacteria();
			
			String name = buildUniqueName(LGConstants.BACTERIA_PREFIX);
			
			String msg = getPageManager().getBacteriaPage().addNewItem(name);
			
			// Check the title of the page
			assertEquals(getMessageSource().getMessage("collection.created.msg",new Object[]{getCollectionNameForMessage()}, Locale.US), msg.trim());
		} catch (Exception e) {
			setLog(e,"addNewBacteria");
			Assert.fail(e.getMessage());
		}
	}
	
	@Override
	public String importBioCollection() throws InterruptedException {
		return getPageManager().getBacteriaPage().importCollection();		
	}

	@Override
	protected String showModule() {
		return getPageManager().getBacteriaPage().showBacteria();
		
	}
	
	@Override
	protected String getCollectionNameForMessage(){
		return "Bacterium";
	}

	@Override
	protected String addNewItem() throws InterruptedException {
		
		String name = buildUniqueName(LGConstants.BACTERIA_PREFIX);
		getPageManager().getBacteriaPage().addNewItem(name);
		return name;
	}
	
	@Override
	public String getCollectionImportDetails() {
		
		return "19 " + LGConstants.BACTERIA.toLowerCase();
	}
	
	@Override
	public String getTemplateImportDetails() {
		return "1 " + LGConstants.BACTERIA.toLowerCase();
	}

}
