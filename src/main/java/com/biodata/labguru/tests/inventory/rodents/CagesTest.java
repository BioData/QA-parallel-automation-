package com.biodata.labguru.tests.inventory.rodents;

import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.inventory.CollectionPage;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public class CagesTest extends AbstractRodentsTest{
	
	
	protected String getCreationPrefix(){
		return LGConstants.RODENTS_CAGE_CREATION_TITLE;
	}

	
	@Override
	@Test (enabled = false)
	public void markAsConsumedStockFromStocksTab() {
		//this test is not relevant for this module
	}
	
	@Override
	@Test (enabled = false)
	public void addItemSaveAndNew(){
		//this test is not relevant for this module
	}
	
	@Override
	@Test (enabled = false)
	public void exportAllItems(){
		//this test is not relevant for this module
	}
	
	@Test (groups = {"deep"})
	public void editSelectedSpecimensInSpecimensTab(){
		
		try {
					
			//create cage with 2 specimen 
			getPageManager().getAdminPage().showRodentCages();
			
			String cageName = buildUniqueName(LGConstants.CAGE_PREFIX);
			getPageManager().getCagesPage().addNewItem(cageName);
			
			getPageManager().getCagesPage().addSpecimenFromCage(cageName, false);
			getPageManager().getCagesPage().addSpecimenFromCage(cageName, false);
			
			//edit items in specific page
			String newName = buildUniqueName("test_");
			boolean succeeded = getPageManager().getCagesPage().editSelectedSpecimensInSpecimensTab(newName);
			
			AssertJUnit.assertTrue("Edit selected items on cage not working as should be.", succeeded);
			
		} catch (Exception e) {
			setLog(e,"editSelectedSpecimensInSpecimensTab");
			AssertJUnit.fail(e.getMessage());
		}	
	}
	
	@Test (groups = {"deep"})
	public void addTreatmentFromCage(){
		
		try {		
			getPageManager().getAdminPage().showRodentCages();
			
			String cageName = buildUniqueName(LGConstants.CAGE_PREFIX);
			getPageManager().getCagesPage().addNewItem(cageName);
			
			String treatmentName = buildUniqueName(LGConstants.TREATMENT_PREFIX);
 
			String created = getPageManager().getCagesPage().addTreatmentFromCage(treatmentName,cageName);
			
			AssertJUnit.assertEquals(treatmentName, created);
			
		} catch (Exception e) {
			setLog(e,"addTreatmentFromCage");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void addSpecimenFromCage(){
		
		try {		
			getPageManager().getAdminPage().showRodentCages();
			
			String cageName = buildUniqueName(LGConstants.CAGE_PREFIX);
			getPageManager().getCagesPage().addNewItem(cageName);
			
			String created = getPageManager().getCagesPage().addSpecimenFromCage(cageName,true);
			
			AssertJUnit.assertTrue(!created.isEmpty());
			
		} catch (Exception e) {
			setLog(e,"addSpecimenFromCage");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void addNewCageWithLocation(){
		
		try {
			showTableIndex();
			
			String name = buildUniqueName(getPrefix());
			
			boolean locationOK = getPageManager().getCagesPage().addNewCageWithLocation(name);
			
			// Check the title of the page
			Assert.assertTrue(locationOK,"The cage was not created in the right location");
		} catch (Exception e) {
			setLog(e,"addNewCageWithLocation");
			Assert.fail(e.getMessage());
		}
	}	
	
	@Override
	@Test (enabled = false)//no import for cages.
	public void importCollection() {
		throw new UnsupportedOperationException("This action is not supported by this collection.");
	}
	
	@Override
	@Test (enabled = false)
	public void addCustomFieldAndGenerateCollectionTemplate(){
		throw new UnsupportedOperationException("This action is not supported by this collection.");
	}
	
	
	@Override
	protected String showModule() {
		
		return getPageManager().getCagesPage().showRodentCages();
		
	}

	@Override
	protected String getPrefix() {
		return LGConstants.CAGE_PREFIX;
	}


	@Override
	public void showMenu() {
		try {
			
			String pageTitle = showTableIndex();
			
			if(getPage().hasList()){
				AssertJUnit.assertEquals(getMessageSource().getMessage("rodents.cages.title",null, Locale.US), pageTitle);
			}else{
				AssertJUnit.assertEquals(getMessageSource().getMessage("rodents.cages.title.no.cages",null, Locale.US), pageTitle);
			}
			
		} catch (Exception e) {
			setLog(e);
			Assert.fail(e.getMessage());
		}
		
	}

	@Override
	@Test (enabled = false)
	public void checkShowPageOfCreatedItem(){
		//this test is not relevant for this module
	}

	@Override
	protected CollectionPage getPage() {
		
		return getPageManager().getCagesPage();
	}




	@Override
	public String getCollectionImportDetails() {
		// not implemented - no import for cages
		throw new UnsupportedOperationException("This action is not supported by this collection.");
	}
	
	@Override
	public String importBioCollection() throws InterruptedException {
		// not implemented - no import for cages
		throw new UnsupportedOperationException("This action is not supported by this collection.");
	}

	@Override
	public String getTemplateImportDetails() {
		// not implemented - no import for cages
		throw new UnsupportedOperationException("This action is not supported by this collection.");
	}

}
