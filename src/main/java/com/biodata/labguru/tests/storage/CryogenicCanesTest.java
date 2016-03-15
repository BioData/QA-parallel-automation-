package com.biodata.labguru.tests.storage;

import static org.testng.AssertJUnit.assertEquals;

import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public class CryogenicCanesTest extends AbstractStoragesTest{

	
	@Override
	public void showMenu(){
		
		String pageTitle = showTableIndex();
		
		boolean hasCanes = getPageManager().getCryogenicPage().hasList();
		if(hasCanes){
		    // Check the title of the page
			AssertJUnit.assertEquals(getMessageSource().getMessage("caryogenic.cane.title.has.canes",null, Locale.US), pageTitle);
		}else{
			 // Check the title of the page
			AssertJUnit.assertEquals(getMessageSource().getMessage("caryogenic.cane.title.no.canes",null, Locale.US), pageTitle);
		}	   

	}
	
	@Test (groups = {"basic sanity"})
	public void editCryoFromShowPage(){
		
		try {
			showTableIndex();
			addNewItem();
			String msg = getPageManager().getCryogenicPage().editItemFromShowPage();
			Assert.assertTrue(msg.endsWith("successfully updated."));
			//TODO - after all messages will be the same
			//Assert.assertEquals(msg,getMessageSource().getMessage("collection.updated.msg",new Object[]{"Cryogenic vial cane"},Locale.US));
			
		}  catch (Exception e) {
			setLog(e,"editCryoFromShowPage");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void deleteCryoFromShowPage(){
		
		try {
			showTableIndex();
			String created = addNewItem();
		
			String msg = getPageManager().getCryogenicPage().deleteFromShowPage();
			AssertJUnit.assertTrue(!msg.isEmpty());
			
		}  catch (Exception e) {
			setLog(e, "deleteCryoFromShowPage");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void deleteAllCanesFromTableView(){
		
		try {
			
			showTableIndex();
			if(getPageManager().getCryogenicPage().hasList()){
				getPageManager().getCryogenicPage().deleteAllItemsFromTable();
				showTableIndex();
			}

			AssertJUnit.assertFalse("Not all items were deleted from index table.",getPageManager().getCryogenicPage().hasList());//check that there are no items in the list
		
		} catch (Exception e) {
			setLog(e,"deleteAllCanesFromTableView");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test(groups = {"basic sanity"})
	public void addNewCryogenicCaneDefaultContent(){
		
		showTableIndex();
		
		String cryoToAdd = buildUniqueName(LGConstants.CRYOGENIC_CANE_PREFIX);
		String title = getPageManager().getCryogenicPage().addCaryogenicCane(cryoToAdd);
		assertEquals(getMessageSource().getMessage("caryogenic.cane.msg.add", null, Locale.US),title);

	}
	
	@Test (groups = {"deep"})
	public void checkCustomizeTableView(){
		
		try {
			
			showTableIndex();
			
			if(!getPageManager().getCryogenicPage().hasList()){
				addNewItem();
			}
			showTableIndex();
			
			String msg = getPageManager().getCryogenicPage().checkCustomizeTableView();
			Assert.assertTrue(msg.equals(""),"Not all selected columns are shown: " + msg);
		}  catch (Exception e) {
			setLog(e,"customizeTableView");
			Assert.fail(e.getMessage());
		}
	}
	
	@Override
	protected String showModule() {
		return getPageManager().getAdminPage().showCryogenicCanes();
	}


	@Override
	protected String addNewItem() throws InterruptedException {
		
		String name = buildUniqueName(LGConstants.CRYOGENIC_CANE_PREFIX);
		getPageManager().getCryogenicPage().addCaryogenicCane(name);
		return name;
	}
}
