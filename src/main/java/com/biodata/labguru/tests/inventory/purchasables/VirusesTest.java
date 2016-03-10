package com.biodata.labguru.tests.inventory.purchasables;

import static org.testng.AssertJUnit.assertEquals;

import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.inventory.CollectionPage;

public class VirusesTest extends PurchasableCollectionTest{

	@Override
	protected CollectionPage getPage() {
		return getPageManager().getVirusPage();
	}
	
	
	@Override
	public String getCollectionId() {
		return LGConstants.VIRUSES;
	}
	
	@Override
	public void showMenu(){

		try {
			String pageTitle = showTableIndex();
			
			if(getPage().hasList()){
			    // Check the title of the page when we already have some items
				AssertJUnit.assertEquals(getMessageSource().getMessage("viruses.title.has.viruses",null, Locale.US), pageTitle);
				getPage().deleteAllItemsFromTable();
			}
			
			pageTitle = showTableIndex();
			// Check the title of the page when we does'nt have items
			AssertJUnit.assertEquals(getMessageSource().getMessage("viruses.title.no.viruses",null, Locale.US), pageTitle);
			
		} catch (Exception e) {
			setLog(e,"canSelectFlies");
			Assert.fail(e.getMessage());
		}

	}
	
	@Override
	public void addSimpleItem(){
		
		try {
			showTableIndex();
			
			String name = buildUniqueName(LGConstants.VIRUS_PREFIX);
			
			String msg = getPage().addNewItem(name);
			
			// Check the title of the page
			assertEquals(getMessageSource().getMessage("collection.created.msg",new Object[]{getCollectionNameForMessage()}, Locale.US), msg.trim());
		} catch (Exception e) {
			setLog(e,"addNewVirus");
			Assert.fail(e.getMessage());
		}
	}
	
	@Override
	protected String getCollectionNameForMessage() {
		return "Virus";
	}
	
	@Override
	public String importBioCollection() throws InterruptedException {
		return getPage().importCollection();		
	}


	@Override
	protected String addNewItem() throws InterruptedException {
		
		String name = buildUniqueName(LGConstants.VIRUS_PREFIX);
		getPage().addNewItem(name);
		return name;
	}
	
	@Override
	public String getCollectionImportDetails() {
		
		return "1 " + LGConstants.VIRUSES.toLowerCase();
	}
	
	@Override
	public String getTemplateImportDetails() {
		return "1 " + LGConstants.VIRUSES.toLowerCase();
	}
	
	@Override
	protected String getPrefix() {
		return LGConstants.VIRUS_PREFIX;
	}

}
