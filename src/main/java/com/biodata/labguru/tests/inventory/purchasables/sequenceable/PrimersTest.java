package com.biodata.labguru.tests.inventory.purchasables.sequenceable;

import static org.testng.AssertJUnit.assertEquals;

import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.inventory.purchasables.PurchasableCollectionPage;


public class PrimersTest extends SequenceableCollectionTest{

	@Override
	public String getCollectionId() {
		return LGConstants.PRIMERS;
	}
	
	@Override
	protected String getPrefix() {
		return LGConstants.PRIMER_PREFIX;
	}

	@Override
	protected PurchasableCollectionPage getPage() {
		
		return getPageManager().getPrimersPage();
	}
	
	
	@Override
	public void showMenu(){

		try {
			String pageTitle = showTableIndex();
			
			if(getPageManager().getPrimersPage().hasList()){
			    // Check the title of the page when we already have some items
				AssertJUnit.assertEquals(getMessageSource().getMessage("primers.title.has.primers",null, Locale.US), pageTitle);
				getPage().deleteAllItems(getCollectionId());
			}
			
			pageTitle = showTableIndex();
			// Check the title of the page when we does'nt have items
			AssertJUnit.assertEquals(getMessageSource().getMessage("primers.title.no.primers",null, Locale.US), pageTitle);
			
		} catch (Exception e) {
			setLog(e,"canSelectPrimers");
			Assert.fail(e.getMessage());
		}

	}
	
	@Override
	public void addSimpleItem(){
		
		try {
			getPageManager().getAdminPage().showPrimers();
			
			String primerName = buildUniqueName(LGConstants.PRIMER_PREFIX);
			
			String msg = getPageManager().getPrimersPage().addNewItem(primerName);
			
			// Check the title of the page
			assertEquals(getMessageSource().getMessage("collection.created.msg",new Object[]{getCollectionNameForMessage()}, Locale.US), msg.trim());
		} catch (Exception e) {
			setLog(e,"addNewPrimer");
			Assert.fail(e.getMessage());
		}
	}
	
	@Override
	protected String getCollectionNameForMessage() {
		return "Primer";
	}
	
	@Override
	public String importBioCollection() throws InterruptedException {
		return getPageManager().getPrimersPage().importCollection();		
	}


	@Override
	protected String addNewItem() throws InterruptedException {
		
		String name = buildUniqueName(LGConstants.PRIMER_PREFIX);	
		getPageManager().getPrimersPage().addNewItem(name);
		return name;
		
	}
	@Override
	public String getTemplateImportDetails() {
		return "1 " + LGConstants.PRIMERS.toLowerCase();
	}
	
	@Override
	public String getCollectionImportDetails() {
		
		return "8 " + LGConstants.PRIMERS.toLowerCase();
	}
}
