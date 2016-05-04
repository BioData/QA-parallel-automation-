package com.biodata.labguru.tests.home;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public class DashboardTest extends AbstractHomeTest{

	@Test(groups = {"deep"})
	public void checkAllTasks(){
		
		try {
			getPageManager().getAdminPage().showDashboard();
			
			
			getPageManager().getDashboardPage().addTask(buildUniqueName(LGConstants.TASK_PREFIX));
			getPageManager().getDashboardPage().addTask(buildUniqueName(LGConstants.TASK_PREFIX));
			Assert.assertTrue(getPageManager().getDashboardPage().goToAllTasks(),"All Tasks page not as should be");
			
			getPageManager().getDashboardPage().deleteAllItemsFromTable();
		
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	
	@Test(groups = {"deep"})
	public void simpleSearchAllTasks(){
		
		try {
			getPageManager().getAdminPage().showDashboard();
			
			//add 2 tasks with different names and search for one of them
			String task = getPageManager().getDashboardPage().addTask(buildUniqueName(LGConstants.TASK_PREFIX));
			getPageManager().getDashboardPage().addTask(buildUniqueName(LGConstants.TASK_PREFIX));
			getPageManager().getDashboardPage().goToAllTasks();
			
			boolean found = getPageManager().getDashboardPage().invokeSearchTask(task);
			Assert.assertTrue(found, "Simple search for task not working as expected");

			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test(groups = {"deep"})
	public void startNewExperimentFromProtocolFromDropdown(){
		
		try {
			
			//create protocol
			String protocol = createNewProtocol();
	
			getPageManager().getAdminPage().showDashboard();
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
			getPageManager().getAdminPage().showDashboard();
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
			getPageManager().getAdminPage().showDashboard();
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
			getPageManager().getAdminPage().showDashboard();
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

		String pageTitle = getPageManager().getAdminPage().showDashboard();
				
        // Check the title of the page
		assertEquals(getMessageSource().getMessage("dashboard.title",null, Locale.US), pageTitle);

	}
	
	@Test(groups = {"deep"})
	public void showRecentlyViewed(){
		
		try {
			getPageManager().getAdminPage().showDashboard();
			
			String expName = buildUniqueName(LGConstants.EXPERIMENT_PREFIX);
			
			getPageManager().getDashboardPage().addNewExperiment(expName);
			getPageManager().getExperimentPage().changeVersion(LGConstants.EXPERIMENT_BETA);
			getPageManager().getAdminPage().showDashboard();
			
			String recentlyViewed = getPageManager().getAdminPage().searchInRecentlyViewedList(expName);
			assertEquals("The experiment that was last viewed is not in the recently viewed list.",recentlyViewed,expName);
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	@Test(groups = {"basic sanity"})
	public void inviteMembers(){
		
		getPageManager().getAdminPage().showDashboard();
		
		boolean hasPymentMethod = getPageManager().getAccountSettingPage().hasPaymentMethod();
		//no pay method - can invite members
		if(!hasPymentMethod){
			//did not pay yet - can invite members
			String member = LGConstants.QA_PREFIX_MAIL +"test" + LGConstants.GMAIL_SUFFIX_MAIL;
			getPageManager().getAdminPage().showDashboard();
			String msg = getPageManager().getDashboardPage().inviteMembers(member);
			
	        // Check the title of the page
			assertEquals(getMessageSource().getMessage("dashboard.invitation.msg",new Object[]{member}, Locale.US), msg);
		}
	}
	
	
	@Test(groups = {"basic sanity"})
	public void addExperimentFromDashboard(){
		try{
			getPageManager().getAdminPage().showDashboard();
			
			String expName = "Experiment from Dashboard";
			String pageTitle = getPageManager().getDashboardPage().addNewExperiment(expName);
			getPageManager().getExperimentPage().changeVersion(LGConstants.EXPERIMENT_BETA);
			Assert.assertEquals(expName, pageTitle);
		
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	@Test(groups = {"deep"})
	public void addTask(){
		
		try {
			getPageManager().getAdminPage().showDashboard();
			
			String taskName = buildUniqueName(LGConstants.TASK_PREFIX);
			String task = getPageManager().getDashboardPage().addTask(taskName);
			
			AssertJUnit.assertEquals("Task with name '" + taskName + "' was not craeted as should be.",taskName, task);
		
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test(groups = {"deep"})
	public void deleteTask(){
		try {
			getPageManager().getAdminPage().showDashboard();
			
			String taskName = buildUniqueName(LGConstants.TASK_PREFIX);
			getPageManager().getDashboardPage().addTask(taskName);
			
			TimeUnit.SECONDS.sleep(1);
			
			assertTrue("task was not deleted as expected",getPageManager().getAdminPage().deleteTask(taskName));
	
		
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test(groups = {"deep"})
	public void checkElasticSearch(){
		
		try {
			getPageManager().getAdminPage().showDashboard();
			getPageManager().getDashboardPage().addNewExperiment(buildUniqueName(LGConstants.EXPERIMENT_PREFIX));
			getPageManager().getExperimentPage().changeVersion(LGConstants.EXPERIMENT_BETA);
			getPageManager().getAdminPage().showDashboard();
			getPageManager().getDashboardPage().addNewExperiment(buildUniqueName(LGConstants.EXPERIMENT_PREFIX));
			String found = getPageManager().getAdminPage().searchFromElasticSearch(LGConstants.EXPERIMENT_PREFIX);
			Assert.assertEquals(found, "Best Matches");
		}catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test(groups = {"deep"} ,priority = 10)
	public void checkElasticSearchInAttachment(){
		
		try {
			getPageManager().getAdminPage().showDashboard();
			getPageManager().getDashboardPage().addNewExperiment(buildUniqueName(LGConstants.EXPERIMENT_PREFIX));
			getPageManager().getExperimentPage().changeVersion(LGConstants.EXPERIMENT_BETA);
			getPageManager().getExperimentPage().uploadAttachmentToSection("0",LGConstants.UPLOAD_TXT_TEST_FILENAME);
			String text = getPageManager().getAdminPage().searchTextFromAttachment(LGConstants.SEARCH_TEXT);
			Assert.assertEquals(text, LGConstants.UPLOAD_TXT_TEST_FILENAME);
		}catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test(groups = {"deep"})//LAB-1245
	public void checkLowStockAlertsChangeThreshold(){
		
		try {
			
			getPageManager().getAdminPage().showPlasmids();
			
			String itemName = buildUniqueName(LGConstants.PLASMID_PREFIX);
			getPageManager().getPlasmidsPage().addNewItem(itemName);
			
			TimeUnit.SECONDS.sleep(3);
			
			String stockName = buildUniqueName(LGConstants.STOCK_PREFIX);
			//add one stock and set the threshold to '2' - we expect a status message that alert for the low stocks
			getPageManager().getPlasmidsPage().addStockFromStocksTab(stockName, LGConstants.STOCK_TYPES_ARRAY[1]);
			
			getPageManager().getPlasmidsPage().setThreshold("2");
			//look for dashboard notification under 'Low Stuck Alerts' section - should be '1'
			Assert.assertEquals(getPageManager().getDashboardPage().checkLowStockAlerts(itemName),itemName);
		
			getPageManager().getAdminPage().goToRecentlyViewed();
			getPageManager().getPlasmidsPage().setThreshold("1");
			//look again in the dashboard to see that notification is gone
			Assert.assertEquals(getPageManager().getDashboardPage().checkLowStockAlerts(itemName),"");
			
			
		} catch (Exception e) {
			setLog(e,"checkLowStockAlerts");
			AssertJUnit.fail(e.getMessage());
		}
	}
}
