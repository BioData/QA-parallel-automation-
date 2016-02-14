package com.biodata.labguru.tests.inventory.purchasables;

import static org.testng.AssertJUnit.assertEquals;

import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.inventory.purchasables.PurchasableCollectionPage;

public class WormsTest extends PurchasableCollectionTest{

	@Override
	protected PurchasableCollectionPage getPage() {
		return getPageManager().getWormPage();
	}
	
	@Override
	protected String getPrefix() {
		return LGConstants.WORM_PREFIX;
	}
	
	@Override
	public String getCollectionId() {
		return LGConstants.WORMS;
	}
	
	@Override
	public void showMenu(){

		try {
			String pageTitle = showTableIndex();;
			
			if(getPage().hasList()){
			    // Check the title of the page when we already have some items
				AssertJUnit.assertEquals(getMessageSource().getMessage("worms.title.has.worms",null, Locale.US), pageTitle);
				getPage().deleteAllItems(getCollectionId());
			}
			
			pageTitle = showTableIndex();
			// Check the title of the page when we does'nt have items
			AssertJUnit.assertEquals(getMessageSource().getMessage("worms.title.no.worms",null, Locale.US), pageTitle);
			
		} catch (Exception e) {
			setLog(e,"canSelectWorms");
			Assert.fail(e.getMessage());
		}

	}
	
	@Override
	public void addSimpleItem(){
		
		try {
			showTableIndex();
			
			String name = buildUniqueName(LGConstants.WORM_PREFIX);
			
			String msg = getPage().addNewItem(name);
			
			// Check the title of the page
			assertEquals(getMessageSource().getMessage("collection.created.msg",new Object[]{getCollectionNameForMessage()}, Locale.US), msg.trim());
		} catch (Exception e) {
			setLog(e,"addNewWorm");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void searchAndAddFromCGC(){
		
		try {
			showTableIndex();
			//add worm from CGC collection and check that it was added to worms collection
			Assert.assertTrue(getPageManager().getWormPage().searchInCGC());
		} catch (Exception e) {
			setLog(e,"searchAndAddFromCGC");
			Assert.fail(e.getMessage());
		}
	}
	
	@Override
	protected String getCollectionNameForMessage() {
		return "Worm";
	}
	
	@Override
	public String importBioCollection() throws InterruptedException {
		return getPage().importCollection();		
	}


	@Override
	protected String addNewItem() throws InterruptedException {
		
		String name = buildUniqueName(LGConstants.WORM_PREFIX);
		getPage().addNewItem(name);
		return name;
	}
	
	@Override
	public String getCollectionImportDetails() {
		
		return "2 " + LGConstants.WORMS.toLowerCase();
	}
	
	@Override
	public String getTemplateImportDetails() {
		return "1 " + LGConstants.WORMS.toLowerCase();
	}

}
