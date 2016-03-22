package com.biodata.labguru.tests.home;

import static org.testng.AssertJUnit.assertTrue;

import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public class RecentResultsTest extends AbstractHomeTest{

	
	@Test(groups = {"deep"})
	public void startNewExperimentFromProtocolFromDropdown(){
		
		try {
			//create protocol
			String protocol = createNewProtocol();
	
			getPageManager().getAdminPage().showRecentResults();
			
			getPageManager().getAdminPage().selectFromExperimentDropdown(LGConstants.NEW_EXP_FROM_PROTOCOL);
			getPageManager().getExperimentPage().createExperimentFromSelectedProtocol(protocol);
			// Check that the protocol is linked to this experiment
			boolean linked = getPageManager().getExperimentPage().checkLinkedResources(protocol);
			assertTrue(linked);
			
			deleteProtocolAfterTest(protocol);
			
		} catch (InterruptedException e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	
	@Test(groups = {"deep"})
	public void startNewDocumentFromDropdown(){
		
		try {
			getPageManager().getAdminPage().showRecentResults();
			getPageManager().getAdminPage().selectFromExperimentDropdown(LGConstants.NEW_DOCUMENT);
			String expTitle = getPageManager().getDocumentPage().getTitle();
			getPageManager().getDocumentPage().saveDocument();
			// Check the title of the page
			assertTrue(expTitle.startsWith("My document"));
		} catch (InterruptedException e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test(groups = {"deep"})
	public void startNewProjectFromDropdown(){
		
		try {
			getPageManager().getAdminPage().showRecentResults();
			getPageManager().getAdminPage().selectFromExperimentDropdown(LGConstants.NEW_PROJECT);
			String title = getPageManager().getProjectPage().getTitle();
			// Check the title of the page
			assertTrue(title.startsWith("My project"));
		} catch (InterruptedException e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	
	@Test(groups = {"deep"})
	public void startNewProtocolFromDropdown(){
		
		try {
			getPageManager().getAdminPage().showRecentResults();
			
			getPageManager().getAdminPage().selectFromExperimentDropdown(LGConstants.NEW_PROTOCOL);
			String title = getPageManager().getProtocolPage().getTitle();
			// Check the title of the page
			assertTrue(title.startsWith("My protocol"));
		} catch (InterruptedException e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Override
	public void showMenu(){

		String pageTitle = getPageManager().getAdminPage().showRecentResults();
				
        // Check the title of the page
		AssertJUnit.assertEquals(getMessageSource().getMessage("recent.results.title",null,Locale.US), pageTitle);

	}
	
	@Test  (groups = {"basic sanity"})
	public void addNewExperiment(){
		
		try{
			getPageManager().getAdminPage().showRecentResults();
			
			String expName = "Experiment from Recent Results";
			String pageTitle = getPageManager().getRecentResultsPage().addNewExperiment(expName);
			Assert.assertEquals(expName, pageTitle);
		
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
}
