package com.biodata.labguru.tests.inventory.purchasables;

import static org.testng.AssertJUnit.assertEquals;

import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.inventory.purchasables.PurchasableCollectionPage;

public class LipidsTest extends PurchasableCollectionTest{
	
	@Override
	protected PurchasableCollectionPage getPage() {
		return getPageManager().getLipidPage();
	}
	
	@Override
	protected String getPrefix() {
		return LGConstants.LIPID_PREFIX;
	}
	
	@Override
	public String getCollectionId() {
		return LGConstants.LIPIDS;
	}
	
	@Override
	public void showMenu(){

		try {
			String pageTitle = showTableIndex();;
			
			if(getPage().hasList()){
			    // Check the title of the page when we already have some items
				AssertJUnit.assertEquals(getMessageSource().getMessage("lipids.title.has.lipids",null, Locale.US), pageTitle);
				getPage().deleteAllItems(getCollectionId());
			}
			
			pageTitle = showTableIndex();
			// Check the title of the page when we does'nt have items
			AssertJUnit.assertEquals(getMessageSource().getMessage("lipids.title.no.lipids",null, Locale.US), pageTitle);
			
		} catch (Exception e) {
			setLog(e,"canSelectLipid");
			Assert.fail(e.getMessage());
		}

	}
	
	@Override
	public void addSimpleItem(){
		
		try {
			showTableIndex();
			
			String name = buildUniqueName(LGConstants.LIPID_PREFIX);
			
			String msg = getPage().addNewItem(name);
			
			// Check the title of the page
			assertEquals(getMessageSource().getMessage("collection.created.msg",new Object[]{getCollectionNameForMessage()}, Locale.US), msg.trim());
		} catch (Exception e) {
			setLog(e,"addNewLipid");
			Assert.fail(e.getMessage());
		}
	}
	
	@Override
	protected String getCollectionNameForMessage() {
		return "Lipid";
	}
	
	@Override
	public String importBioCollection() throws InterruptedException {
		return getPage().importCollection();		
	}

	@Override
	protected String addNewItem() throws InterruptedException {
		
		String name = buildUniqueName(LGConstants.LIPID_PREFIX);
		getPage().addNewItem(name);
		return name;
	}
	
	@Override
	public String getCollectionImportDetails() {
		
		return "3 " + LGConstants.LIPIDS.toLowerCase();
	}
	
	@Override
	public String getTemplateImportDetails() {
		return "1 " + LGConstants.LIPIDS.toLowerCase();
	}

}
