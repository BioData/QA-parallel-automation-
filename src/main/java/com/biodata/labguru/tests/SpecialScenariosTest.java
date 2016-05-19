package com.biodata.labguru.tests;

import java.util.concurrent.TimeUnit;

import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;

@Listeners(TestOrderRandomizer.class)
public class SpecialScenariosTest extends BaseTest{
	
	
//	@Test (groups = {"basic sanity"})
//	public void checkStorageTreeInStockSelectionDialog(){
//		
//		try {
//			getPageManager().getBoxPage().showBoxes();
//			
//			String boxToAdd = buildUniqueName(LGConstants.BOX_PREFIX);
//			getPageManager().getBoxPage().addNewBox(boxToAdd,"1");
//			
//			
//			
//		}  catch (Exception e) {
//			setLog(e,"checkStorageTreeInStockSelectionDialog");
//			AssertJUnit.fail(e.getMessage());
//		}
//
//	}
	
	@Test (groups = {"deep"})
	public void addTagAndSearchByIt(){
		
		try {
			
			getPageManager().getAdminPage().selectExperiments();
			TimeUnit.SECONDS.sleep(2);
			String expName = buildUniqueName(LGConstants.EXPERIMENT_PREFIX);
			getPageManager().getExperimentPage().addNewExperiment(expName);
			String tagName = buildUniqueName(LGConstants.TAG_PREFIX);
			getPageManager().getExperimentPage().addInlineTag(tagName);
			
			boolean succeeded = getPageManager().getAdminPage().searchTagAndSearchByIt(tagName, expName);
			
			AssertJUnit.assertTrue("Search by tag not working as should be.", succeeded);
			
			getPageManager().getAdminPage().deleteTagFromTaggedEntitiesList();
		
		} catch (Exception e) {
			setLog(e,"addTagAndSearchByIt");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void tagAllItemsWithPagination(){
		
		try {		
			//check the 'Rodent' collection
			getPageManager().getAdminPage().showCollectionsAndSettings();
			getPageManager().getAccountSettingPage().checkCollection(LGConstants.RODENTS);
			
			//first delete all previous specimens
			getPageManager().getAdminPage().showRodentSpecimens();
			if(getPageManager().getRodentSpecimensPage().hasList())
				getPageManager().getRodentSpecimensPage().deleteAllItemsFromTable();
			
			//create strain with 41 specimen in order to have index table with pagination
			getPageManager().getAdminPage().showRodentStrains();
			
			String strainName = buildUniqueName(LGConstants.RODENT_STRAIN_PREFIX);
			getPageManager().getRodentStrainsPage().addNewItem(strainName);
			
			String specimenName = buildUniqueName(LGConstants.RODENT_SPECIMEN_PREFIX);
			getPageManager().getRodentStrainsPage().addSpecimenFromStrain(specimenName,41,false);
			
			//go to specimens index table
			getPageManager().getAdminPage().showRodentSpecimens();
			//tag all specimens (in all pages)
			String succeeded = getPageManager().getAdminPage().tagItemsAllPages();
			AssertJUnit.assertTrue(succeeded, !succeeded.startsWith("Failed"));
			
			
		} catch (Exception e) {
			setLog(e,"tagAllItemsWithPagination");
			AssertJUnit.fail(e.getMessage());
		
		}
	}
	@Test (groups = {"deep"})
	public void editItemsOnSpecificPage(){
		
		try {
			
			//check the 'Rodent' collection
			getPageManager().getAdminPage().showCollectionsAndSettings();
			getPageManager().getAccountSettingPage().checkCollection(LGConstants.RODENTS);
			
			//first delete all previous specimens
			getPageManager().getAdminPage().showRodentSpecimens();
			if(getPageManager().getRodentSpecimensPage().hasList())
				getPageManager().getRodentSpecimensPage().deleteAllItemsFromTable();
			
			//create strain with 41 specimen in order to have index table with pagination
			getPageManager().getAdminPage().showRodentStrains();
			
			String strainName = buildUniqueName(LGConstants.RODENT_STRAIN_PREFIX);
			getPageManager().getRodentStrainsPage().addNewItem(strainName);
			
			String specimenName = buildUniqueName(LGConstants.RODENT_SPECIMEN_PREFIX);
			getPageManager().getRodentStrainsPage().addSpecimenFromStrain(specimenName,41,false);
			
			//go to specimens index table
			getPageManager().getAdminPage().showRodentSpecimens();
			//edit items in specific page
			String newName = buildUniqueName("test_");
			boolean succeeded = getPageManager().getAdminPage().editItemsOnSpecificPage(1,newName);
			
			AssertJUnit.assertTrue("Edit selected items on page not working as should be.", succeeded);
			
		} catch (Exception e) {
			setLog(e,"editItemsOnSpecificPage");
			AssertJUnit.fail(e.getMessage());
		}
		
	}
}
