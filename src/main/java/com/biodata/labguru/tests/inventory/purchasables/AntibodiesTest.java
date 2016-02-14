package com.biodata.labguru.tests.inventory.purchasables;

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
public class AntibodiesTest extends PurchasableCollectionTest{

	
	@Override
	public String getCollectionId() {
		return LGConstants.ANTIBODIES;
	}
	
	@Override
	protected String getPrefix() {
		return LGConstants.ANTIBODY_PREFIX;
	}
	
	@Override
	public void showMenu(){

		try {
			String pageTitle = showTableIndex();
	
			if(getPageManager().getAntibodiesPage().hasList()){
		        // Check the title of the page when we already have some Antibodies
				AssertJUnit.assertEquals(getMessageSource().getMessage("antibodies.title.has.antibodies",null, Locale.US), pageTitle);
				getPage().deleteAllItemsFromTable();
			}
			
			pageTitle = showTableIndex();
			// Check the title of the page when we does'nt have Antibodies
			AssertJUnit.assertEquals(getMessageSource().getMessage("antibodies.title.no.antibodies",null, Locale.US), pageTitle);
			
		} catch (Exception e) {
			setLog(e,"canSelectAntibodies");
			Assert.fail(e.getMessage());
		}
	}
	
	@Override
	public void addSimpleItem(){
		
		try {
			getPageManager().getAdminPage().showAntibodies();
			
			String AntibodyName = buildUniqueName(LGConstants.ANTIBODY_PREFIX);
			
			String msg = getPageManager().getAntibodiesPage().addNewItem(AntibodyName);
			
			// Check the title of the page
			assertEquals(getMessageSource().getMessage("collection.created.msg",new Object[]{getCollectionNameForMessage()}, Locale.US), msg.trim());
		} catch (Exception e) {
			setLog(e,"addNewAntibody");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void addAntibodyFromOnlineDir(){
		
		try {
			getPageManager().getAdminPage().showAntibodies();
			
			String pageTitle = getPageManager().getAntibodiesPage().addAntibodyFromOnlineDir();
						
			String AntibodyName = pageTitle.substring(pageTitle.indexOf("-")+2);
			String msg = getMessageSource().getMessage("antibodies.title.add.from.online.dir",new Object[]{AntibodyName}, Locale.US);
			// Check the title of the page
			assertEquals(msg, pageTitle);
		} catch (Exception e) {
			setLog(e,"addAntibodyFromOnlineDir");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void showAntybodyFromDirectory(){
		
		try {
			getPageManager().getAdminPage().showAntibodies();
			
			String logo = getPageManager().getAntibodiesPage().showAntibodyFromDirectory();
			
			// Check the title of the page
			assertEquals(logo, getMessageSource().getMessage("antibodies.online.logo",null,Locale.US));
		} catch (Exception e) {
			setLog(e,"showAntybodyFromDirectory");
			Assert.fail(e.getMessage());
		}
		
	}
	
	@Override
	protected String getCollectionNameForMessage() {
		return "Antibody";
	}
	
	@Override
	protected String showModule() {
		return getPageManager().getAdminPage().showAntibodies();
	}

	@Override
	protected String addNewItem() {
		
		String name = buildUniqueName(LGConstants.ANTIBODY_PREFIX);	
		getPageManager().getAntibodiesPage().addNewItem(name);
		return name;
	}

	@Override
	public String importBioCollection() throws InterruptedException {
		
		return getPageManager().getAntibodiesPage().importCollection();
	}

	@Override
	public String getCollectionImportDetails() {
		
		return "19 " + LGConstants.ANTIBODIES.toLowerCase();
	}
	
	@Override
	public String getTemplateImportDetails() {
		return "1 " + LGConstants.ANTIBODIES.toLowerCase();
	}

	@Override
	protected PurchasableCollectionPage getPage() {
		
		return getPageManager().getAntibodiesPage();
	}
}
