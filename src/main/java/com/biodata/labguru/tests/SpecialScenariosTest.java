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
			getPageManager().getExperimentPageV2().addNewExperiment(expName);
			
			String tagName = buildUniqueName(LGConstants.TAG_PREFIX);
			getPageManager().getAdminPage().addTag(tagName);
			
			boolean succeeded = getPageManager().getAdminPage().searchTagAndSearchByIt(tagName, expName);
			
			AssertJUnit.assertTrue("Search by tag not working as should be.", succeeded);
			
			getPageManager().getAdminPage().deleteTagFromTaggedEntitiesList();
		
		} catch (Exception e) {
			setLog(e,"addTagAndSearchByIt");
			AssertJUnit.fail(e.getMessage());
		}
	}
}
