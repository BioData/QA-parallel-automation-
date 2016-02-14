package com.biodata.labguru.tests;

import static org.testng.AssertJUnit.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;

@Listeners({TestOrderRandomizer.class})
public abstract class AbstractLGTest extends BaseTest{
	
	protected static boolean hasExperiments = false;
	
	
	protected abstract String addNewItem() throws InterruptedException ;
	
	protected abstract String showModule();
	
	protected String showTableIndex() {
		String pageTitle = showModule();	
		closeIridizePopups();
		return pageTitle;
	}
	
	@Test (groups = {"basic sanity"})
	public abstract void showMenu();
	
	@Test (groups = {"deep"})
	public void duplicateItem(){
		
		try {
			showTableIndex();
			TimeUnit.SECONDS.sleep(2);
			addNewItem();
			String msg = getPageManager().getAdminPage().duplicateItem();
			
			// Check the msg - success
			assertTrue("The duplication did not finished as expected: " + msg,msg.contains("successfully"));
		} catch (Exception e) {
			setLog(e,"duplicateItem");
			Assert.fail(e.getMessage());
		}
	}
	

	@Test (groups = {"deep"})
	public void addTag(){
		
		try {
			showTableIndex();
			TimeUnit.SECONDS.sleep(2);
			addNewItem();
			
			String tagName = buildUniqueName(LGConstants.TAG_PREFIX);
			String tag = getPageManager().getAdminPage().addTag(tagName);
			
			AssertJUnit.assertEquals("Tag with name '" + tagName + "' was not craeted as should be.",tagName, tag);
			
			getPageManager().getAdminPage().deleteTagFromTaggedEntitiesList();
		
		} catch (Exception e) {
			setLog(e,"addTag");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void addTask(){
		
		try {
			showTableIndex();
			TimeUnit.SECONDS.sleep(2);
			addNewItem();
			
			String taskName = buildUniqueName(LGConstants.TASK_PREFIX);
			String task = getPageManager().getAdminPage().addTask(taskName);
			
			AssertJUnit.assertEquals("Task with name '" + taskName + "' was not craeted as should be.",taskName, task);
		
			//delete task after test
			getPageManager().getAdminPage().deleteTask();
			
		} catch (Exception e) {
			setLog(e,"addTask");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"test"})
	public void addLinkedResource(){
		
		try {
			//before addResource i want to make sure there is at least one experiment as a link resource
			hasExperiments  = hasExperiments || getPageManager().getExperimentPage().hasList();
    		if(!hasExperiments ){
    			getPageManager().getExperimentPage().addNewExperiment("First Experiment");
    			hasExperiments = true;
    		}
    		
			showTableIndex();
			addNewItem();
			
			String linkedRes = getPageManager().getAdminPage().addLinkedResource(LGConstants.EXPERIMENT);
			
			AssertJUnit.assertTrue("No resource was linked.",!linkedRes.equals(""));
		
		} catch (Exception e) {
			setLog(e,"addLinkedResource");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"test"})
	public void uploadFile() {
		try {
			
			showTableIndex();
			TimeUnit.SECONDS.sleep(2);
			addNewItem();
			boolean attachmentExist = getPageManager().getAdminPage().uploadFile();
			AssertJUnit.assertTrue("Attachment file: '" + LGConstants.UPLOAD_TXT_TEST_FILENAME +"' was not found.",attachmentExist);
			
		} catch (Exception e) {
			setLog(e,"uploadFile");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void uploadImage() {
		try {
			
			showTableIndex();
			TimeUnit.SECONDS.sleep(2);
			String resource = addNewItem();
			String pageTitle = getPageManager().getAdminPage().uploadImage(resource);
			AssertJUnit.assertEquals(LGConstants.UPLOAD_IMAGE_TEST_FILENAME,pageTitle);
			
			
		} catch (Exception e) {
			setLog(e,"uploadImage");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void deleteAttachment() {
		try {
			
			showTableIndex();
			TimeUnit.SECONDS.sleep(2);
			addNewItem();
			getPageManager().getAdminPage().uploadFile();
			boolean deleted = getPageManager().getAdminPage().deleteAttachment();
			Assert.assertTrue(deleted,"Attachment could not be deleted.");
		} catch (Exception e) {
			setLog(e,"deleteAttachment");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	protected String getCurrentDateFormatted(String datePattern) {
		
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat(datePattern);
		String currentDate = formater.format(calendar.getTime());
		return currentDate;
	}
}
