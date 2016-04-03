package com.biodata.labguru.tests.storage;

import static org.testng.AssertJUnit.assertEquals;

import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.GenericHelper;
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
			setLog(e, "showMenu");
		}
	}
	
//	@Test (groups = {"deep"})
	@Test (groups = {"knownBugs"})
	public void addNewStorageByType(){
		
		try {
			getPageManager().getAdminPage().showStorages();
			
			
			String[] types = LGConstants.STORAGE_TYPES_ARRAY;
			for (int i = 0; i < types.length; i++) {	
				String type = types[i];
				String name = GenericHelper.buildUniqueName(type);
				String createdStorageType = getPageManager().getStoragePage().addNewStorageByType(name,i);
				AssertJUnit.assertEquals(createdStorageType,type);
				
				//delete after test is finished
				getPageManager().getStoragePage().deleteStorage(name);
			}
			
		}  catch (Exception e) {
			setLog(e, "addNewStorageByType");
			AssertJUnit.fail(e.getMessage());
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
			AssertJUnit.assertTrue("Could not change storage location ",updated);
			
		}  catch (Exception e) {
			setLog(e, "changeStorageLocation");
			AssertJUnit.fail(e.getMessage());
			
		}
	}
	
	@Test (groups = {"deep"})
	public void testBoxStorage(){
		
		try {
			
			//add new box to manipulate in the tree
			String boxName = buildUniqueName(LGConstants.BOX_PREFIX);
			getPageManager().getBoxPage().addNewBox(boxName, "1");
			
			//go to storage tree and select the box in the tree
			showStorageAndSelectBox(boxName);
	
			//mark as consumed stock from boxview
			String stockToMarkedAsConsumed = "stockToMarkedAsConsumed";
			String notyMsg = getPageManager().getBoxPage().markAsConsumedStockFromBoxView(stockToMarkedAsConsumed);
			assertEquals(getMessageSource().getMessage("boxes.stock.marked.consumed.msg",new Object[]{"1"}, Locale.US), notyMsg);
			
			//check the edit stock from tableview
			String stockToEdit = "editStockFromBoxTableView";			
			Assert.assertTrue(getPageManager().getBoxPage().editStockFromBoxTableView(stockToEdit), "The stock was not edited as expected.");

			showStorageAndSelectBox(boxName);
			//add stock to box from storage view
			String stockToAdd = buildUniqueName(LGConstants.STOCK_PREFIX);
			
			String pageTitle = getPageManager().getBoxPage().addStockFromBox(stockToAdd);
			assertEquals(pageTitle,stockToAdd);
			
			//delete after test is finished (go to box from the stock page
			getPageManager().getAdminPage().goToRecentlyViewed();
			getPageManager().getBoxPage().deleteFromShowPage();
		}  catch (Exception e) {
			setLog(e, "testBoxStorage");
			AssertJUnit.fail(e.getMessage());
		}
	}

	private void showStorageAndSelectBox(String boxName) throws InterruptedException {
		
		getPageManager().getAdminPage().showStorages();
		getPageManager().getStoragePage().selectBoxNodeWithName(boxName);
	}
}
