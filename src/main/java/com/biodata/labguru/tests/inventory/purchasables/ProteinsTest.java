package com.biodata.labguru.tests.inventory.purchasables;

import static org.testng.AssertJUnit.assertEquals;

import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.inventory.purchasables.PurchasableCollectionPage;

public class ProteinsTest extends PurchasableCollectionTest{
	@Override
	protected PurchasableCollectionPage getPage() {
		return getPageManager().getProteinPage();
	}
	
	@Override
	protected String getPrefix() {
		return LGConstants.PROTEIN_PREFIX;
	}
	
	@Override
	public String getCollectionId() {
		return LGConstants.PROTEINS;
	}
	
	@Override
	public void showMenu(){

		try {
			String pageTitle = showTableIndex();
			
			if(getPage().hasList()){
			    // Check the title of the page when we already have some items
			AssertJUnit.assertEquals(getMessageSource().getMessage("proteins.title.has.proteins",null, Locale.US), pageTitle);
			getPage().deleteAllItems(getCollectionId());
			}
				
			pageTitle = showTableIndex();
			// Check the title of the page when we does'nt have items
			AssertJUnit.assertEquals(getMessageSource().getMessage("proteins.title.no.proteins",null, Locale.US), pageTitle);
			
		} catch (Exception e) {
			setLog(e,"canSelectProtein");
			Assert.fail(e.getMessage());
		}

	}
	
	@Override
	public void addSimpleItem(){
		
		try {
			showTableIndex();
			
			String name = buildUniqueName(LGConstants.PROTEIN_PREFIX);
			
			String msg = getPage().addNewItem(name);
			
			// Check the title of the page
			assertEquals(getMessageSource().getMessage("collection.created.msg",new Object[]{getCollectionNameForMessage()}, Locale.US), msg.trim());
		} catch (Exception e) {
			setLog(e,"addNewProtein");
			Assert.fail(e.getMessage());
		}
	}
	
	@Override
	protected String getCollectionNameForMessage() {
		return "Protein";
	}
	
	@Override
	public String importBioCollection() throws InterruptedException {
		return getPage().importCollection();		
	}


	@Override
	protected String addNewItem() throws InterruptedException {
		
		String name = buildUniqueName(LGConstants.PROTEIN_PREFIX);
		getPage().addNewItem(name);
		return name;
	}
	
	@Override
	public String getCollectionImportDetails() {
		
		return "2 " + LGConstants.PROTEINS.toLowerCase();
	}
	
	@Override
	public String getTemplateImportDetails() {
		return "1 " + LGConstants.PROTEINS.toLowerCase();
	}

}
