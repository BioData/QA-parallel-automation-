package com.biodata.labguru.tests.storage;

import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public class PlatesTest extends AbstractStoragesTest{

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
			
			if(hasList()){
				getPageManager().getPlatePage().selectPlateFromTable();
				String msg = getPageManager().getPlatePage().editItemFromShowPage();
				Assert.assertTrue(msg.endsWith("successfully updated."));
			}
			
		}  catch (Exception e) {
			setLog(e,"editPlateFromShowPage");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void addTagToPlatesFromIndexTable(){
		
		try {
			if(hasList()){

				boolean succeeded = getPageManager().getPlatePage().addTagFromIndexTable();
				
				AssertJUnit.assertTrue("Tag was not craeted as should be.",succeeded);
			}else{
				//add first plate from experiment or protocol
				
			}
			
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
