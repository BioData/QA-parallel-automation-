package com.biodata.labguru.tests.storage;

import static org.testng.AssertJUnit.assertTrue;

import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public class PlatesTest extends AbstractStoragesTest{

	@BeforeClass(alwaysRun = true , dependsOnMethods = "initialize")
	public void addPlateInExperiment(){	
		try {
			//add plate in experiment
			getPageManager().getAdminPage().selectExperiments();
			String experimentName = buildUniqueName("ExpWithPlate_");
			getPageManager().getExperimentPage().addNewExperiment(experimentName);
			getPageManager().getAdminPage().discardNotyMessages();
			closeIridizePopups();
			assertTrue(getPageManager().getExperimentPage().addPlate2X3ToSection("1"));
		} catch (InterruptedException e) {
			setLog(e,"before class method: addPlateInExperiment");
		}
	}


	@Override
	protected String addNewItem() throws InterruptedException {
		// not implemented
		throw new UnsupportedOperationException("This action is not supported by this module");
	}

	@Override
	protected String showModule() {
		
		return getPageManager().getAdminPage().showPlates();
	}
	
	@Override
	@Test (groups = {"deep"})
	public void uploadFile() {
		try {
			
			showTableIndex();	
			
			getPageManager().getPlatePage().selectPlateFromTable();
			boolean attachmentExist = getPageManager().getAdminPage().uploadFileFromRightSide();
			AssertJUnit.assertTrue("Attachment file: '" + LGConstants.UPLOAD_TXT_TEST_FILENAME +"' was not found.",attachmentExist);
			getPageManager().getAdminPage().deleteAttachment();
		} catch (Exception e) {
			setLog(e,"uploadFile");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Override
	@Test (enabled = false)
	public void uploadImage() {
		// not implemented
		throw new UnsupportedOperationException("This action is not supported by this module");
	}
	
	@Override
	@Test (groups = {"deep"})
	public void deleteAttachment() {
		try {
			
			showTableIndex();	
			
			getPageManager().getPlatePage().selectPlateFromTable();
			getPageManager().getAdminPage().uploadFileFromRightSide();
			boolean deleted = getPageManager().getAdminPage().deleteAttachment();
			Assert.assertTrue(deleted,"Attachment could not be deleted.");
		} catch (Exception e) {
			setLog(e,"deleteAttachment");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Override
	public void addTag(){
		
		try {
			showTableIndex();	
		
			getPageManager().getPlatePage().selectPlateFromTable();
		
			String tagName = buildUniqueName(LGConstants.TAG_PREFIX);
			String tag = getPageManager().getAdminPage().addTag(tagName);
		
			AssertJUnit.assertEquals("Tag with name '" + tagName + "' was not craeted as should be.",tagName, tag);
		
			getPageManager().getAdminPage().deleteTagFromTaggedEntitiesList();
			
		
		} catch (Exception e) {
			setLog(e,"addTag");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Override
	@Test (enabled = false)
	public void addLinkedResource(){
		// not implemented
		throw new UnsupportedOperationException("This action is not supported by this module");
	}


	@Override
	public void showMenu() {
		
		try {
			
			String pageTitle = showTableIndex();
			AssertJUnit.assertEquals(getMessageSource().getMessage("plates.title.has.plates",null,Locale.US), pageTitle);		
			
		} catch (Exception e) {
			setLog(e, "showMenu of Plates");
		}	
	}
	
	@Test (groups = {"basic sanity"})
	public void editPlateFromShowPage(){
		
		try {
			showTableIndex();
			getPageManager().getPlatePage().selectPlateFromTable();
			String msg = getPageManager().getPlatePage().editItemFromShowPage();
			Assert.assertTrue(msg.endsWith("successfully updated."));
			
			
		}  catch (Exception e) {
			setLog(e,"editPlateFromShowPage");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void addTagToPlatesFromIndexTable(){
		
		try {
			showTableIndex();
			boolean succeeded = getPageManager().getPlatePage().addTagFromIndexTable();
			
			AssertJUnit.assertTrue("Tag was not craeted as should be.",succeeded);
			
			
		} catch (Exception e) {
			setLog(e,"addTagToPlatesFromIndexTable");
			AssertJUnit.fail(e.getMessage());
		}
	}

	private boolean hasList() {
		showTableIndex();
		return getPageManager().getPlatePage().hasList();
	}
}
