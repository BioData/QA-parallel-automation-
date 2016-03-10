package com.biodata.labguru.tests.inventory.purchasables.sequenceable;

import static org.testng.AssertJUnit.assertEquals;

import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.inventory.purchasables.PurchasableCollectionPage;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public class PlasmidsTest extends SequenceableCollectionTest{

	
	@Override
	public String getCollectionId() {
		return LGConstants.PLASMIDS;
	}
	
	@Override
	protected String getPrefix() {
		return LGConstants.PLASMID_PREFIX;
	}
	
	@Override
	protected PurchasableCollectionPage getPage() {
		
		return getPageManager().getPlasmidsPage();
	}
	
	@Override
	public void showMenu(){

		try {
			String pageTitle = showTableIndex();
			
			if(getPageManager().getPlasmidsPage().hasList()){
			    // Check the title of the page when we already have some plasmids
				AssertJUnit.assertEquals(getMessageSource().getMessage("plasmids.title.has.plasmids",null, Locale.US), pageTitle);
			}else{
				// Check the title of the page when we does'nt have plasmids
				AssertJUnit.assertEquals(getMessageSource().getMessage("plasmids.title.no.plasmids",null, Locale.US), pageTitle);
			}
		} catch (Exception e) {
			setLog(e,"canSelectPlasmids");
			Assert.fail(e.getMessage());
		}

	}
	
	@Override
	public void addSimpleItem(){
		
		try {
			showTableIndex();
			
			String plasmidName = buildUniqueName(LGConstants.PLASMID_PREFIX);
			
			String msg = getPageManager().getPlasmidsPage().addNewItem(plasmidName);
			
			// Check the title of the page
			assertEquals(getMessageSource().getMessage("collection.created.msg",new Object[]{getCollectionNameForMessage()}, Locale.US), msg.trim());
		} catch (Exception e) {
			setLog(e,"addNewPlasmid");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void addPlasmidFromAddgene(){
		
		try {
			getPageManager().getAdminPage().showPlasmids();
				
			String pageTitle = getPageManager().getPlasmidsPage().addPlasmidFromAddgene();			
			String plasmidName = pageTitle.substring(pageTitle.indexOf("-")+1);
			String msg = getMessageSource().getMessage("plasmids.title.add.from.addgene",new Object[]{plasmidName}, Locale.US);
			// Check the title of the page
			assertEquals(msg, pageTitle);
			
		} catch (Exception e) {
			setLog(e,"addPlasmidFromAddgene");
			Assert.fail(e.getMessage());
		}
		
	}
	
	
	@Test (groups = {"basic sanity"})
	public void showPlasmidFromDirectory(){
		
		try {
			getPageManager().getAdminPage().showPlasmids();
			
			String logo = getPageManager().getPlasmidsPage().showPlasmidFromDirectory();
			
			// Check the title of the page
			assertEquals(logo,getMessageSource().getMessage("plasmids.online.logo", null,Locale.US));
		} catch (Exception e) {
			setLog(e,"showPlasmidFromDirectory");
			Assert.fail(e.getMessage());
		}
		
	}
	
	@Override
	protected String getCollectionNameForMessage() {
		return "Plasmid";
	}

	@Override
	protected String showModule() {
		
		return getPageManager().getAdminPage().showPlasmids();	
	}

	@Override
	protected String addNewItem() {
		
		String name = buildUniqueName(LGConstants.PLASMID_PREFIX);
		getPageManager().getPlasmidsPage().addNewItem(name);
		return name;
	}

	@Override
	public String getCollectionImportDetails() {
		
		return "7 " + LGConstants.PLASMIDS.toLowerCase();
	}
	
	@Override
	public String getTemplateImportDetails() {
		return "1 " + LGConstants.PLASMIDS.toLowerCase();
	}

	@Override
	public String importBioCollection() throws InterruptedException {
		
		return getPageManager().getPlasmidsPage().importCollection();
	}


}
