package com.biodata.labguru.tests.storage;

import java.util.Locale;

import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.tests.BaseTest;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public class StoragesTest extends BaseTest{

	@Test(groups = {"basic sanity"})
	public void showMenu(){
		
		try {
			String pageTitle = getPageManager().getAdminPage().showStorages();
					
			// Check the title of the page
			AssertJUnit.assertEquals(getMessageSource().getMessage("storage.title.has.storage",null,Locale.US), pageTitle);
			
		} catch (Exception e) {
			setLog(e, "canSelectStorages");
		}
	}
	
	
	@Test (groups = {"basic sanity"})
	public void addNewStorage(){
		
		try {
			getPageManager().getAdminPage().showStorages();
			
			String name = buildUniqueName(LGConstants.STORAGE_PREFIX);
			String createdStorage = getPageManager().getStoragePage().addNewStorage(name);
			AssertJUnit.assertEquals(createdStorage, name);
			//delete after test is finished
			getPageManager().getStoragePage().deleteStorage(name);
		}  catch (Exception e) {
			setLog(e, "addNewStorage");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void editStorage(){
		
		try {
			getPageManager().getAdminPage().showStorages();
			
			String name = buildUniqueName(LGConstants.STORAGE_PREFIX);
			String createdStorage = getPageManager().getStoragePage().addNewStorage(name);
			String updatedName = getPageManager().getStoragePage().editStorage(createdStorage);
			AssertJUnit.assertTrue(!updatedName.equals(createdStorage));
			
		}  catch (Exception e) {
			setLog(e, "editStorage");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void deleteStorage(){
		
		try {
			getPageManager().getAdminPage().showStorages();
			
			String name = buildUniqueName(LGConstants.STORAGE_PREFIX);
			String createdStorage = getPageManager().getStoragePage().addNewStorage(name);
			String msg = getPageManager().getStoragePage().deleteStorage(createdStorage);
			AssertJUnit.assertEquals(getMessageSource().getMessage("storage.delete.msg", null,Locale.US), msg);
			
		}  catch (Exception e) {
			setLog(e, "deleteStorage");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void changeStorageLocation(){
		
		try {
			getPageManager().getAdminPage().showStorages();
			
			String name = buildUniqueName(LGConstants.STORAGE_PREFIX);
			String createdStorage = getPageManager().getStoragePage().addNewStorage(name);
			boolean updated = getPageManager().getStoragePage().changeStorageLocation(createdStorage);
			AssertJUnit.assertTrue(updated);
			
		}  catch (Exception e) {
			setLog(e, "changeStorageLocation");
			AssertJUnit.fail(e.getMessage());
			
		}
	}
}
