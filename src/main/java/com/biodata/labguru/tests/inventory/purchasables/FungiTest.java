package com.biodata.labguru.tests.inventory.purchasables;

import static org.testng.AssertJUnit.assertEquals;

import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.inventory.purchasables.PurchasableCollectionPage;

public class FungiTest extends PurchasableCollectionTest{
	
	@Override
	protected PurchasableCollectionPage getPage() {
		return getPageManager().getFungiPage();
	}
	
	@Override
	protected String getPrefix() {
		return LGConstants.FUNGI_PREFIX;
	}
	
	@Override
	public String getCollectionId() {
		return LGConstants.FUNGI;
	}
	
	@Override
	public void showMenu(){

		try {
			String pageTitle = showTableIndex();;
			
			if(getPage().hasList()){
			    // Check the title of the page when we already have some items
				AssertJUnit.assertEquals(getMessageSource().getMessage("fungi.title.has.fungi",null, Locale.US), pageTitle);
				getPage().deleteAllItems(getCollectionId());
			}
			
			pageTitle = showTableIndex();
			// Check the title of the page when we does'nt have items
			AssertJUnit.assertEquals(getMessageSource().getMessage("fungi.title.no.fungi",null, Locale.US), pageTitle);
			
		} catch (Exception e) {
			setLog(e,"canSelectFungi");
			Assert.fail(e.getMessage());
		}

	}
	
	@Override
	public void addSimpleItem(){
		
		try {
			showTableIndex();
			
			String name = buildUniqueName(LGConstants.FUNGI_PREFIX);
			
			String msg = getPage().addNewItem(name);
			
			// Check the title of the page
			assertEquals(getMessageSource().getMessage("collection.created.msg",new Object[]{getCollectionNameForMessage()}, Locale.US), msg.trim());
		} catch (Exception e) {
			setLog(e,"addNewFungi");
			Assert.fail(e.getMessage());
		}
	}
	
	@Override
	protected String getCollectionNameForMessage() {
		return "Fungus";
	}
	
	@Override
	public String importBioCollection() throws InterruptedException {
		return getPage().importCollection();		
	}


	@Override
	protected String addNewItem() throws InterruptedException {
		
		String name = buildUniqueName(LGConstants.FUNGI_PREFIX);
		getPage().addNewItem(name);
		return name;
	}
	
	@Override
	public String getCollectionImportDetails() {
		
		return "3 " + LGConstants.FUNGI.toLowerCase();
	}
	
	@Override
	public String getTemplateImportDetails() {
		return "1 " + LGConstants.FUNGI.toLowerCase();
	}

}
