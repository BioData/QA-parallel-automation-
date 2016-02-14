package com.biodata.labguru.tests.inventory;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.tests.BaseTest;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public class ShoppingListTest extends BaseTest{
	
	@BeforeClass(alwaysRun = true)
	public void addItemsToShoppingList(){
		
		try {
			//add consumable to shopping list
			getPageManager().getAdminPage().showConsumables();		
			getPageManager().getConsumablesPage().addNewItem(buildUniqueName(LGConstants.CONSUMABLE_PREFIX));		
			getPageManager().getAdminPage().showConsumables();		
			getPageManager().getConsumablesPage().addConsumableToShoppingList();
			
			//add some plasmids to shopping list
			for (int i = 0; i < 4; i++) {
				getPageManager().getAdminPage().showPlasmids();
				getPageManager().getPlasmidsPage().addNewItem(buildUniqueName(LGConstants.PLASMID_PREFIX));
				getPageManager().getPlasmidsPage().addToShoppingListFromShowPage();
			}

		} catch (Exception e) {
			setLog(e);
		}
	}
	
	@Test (groups = {"deep"})
	public void canSelectShoppingList(){

		String pageTitle = getPageManager().getAdminPage().showShoppingList();
				
        // Check the title of the page
		AssertJUnit.assertEquals(getMessageSource().getMessage("shopping.list.title",null, Locale.US), pageTitle);

	}
	
	@Test (groups = {"basic sanity"})
	public void addNewServiceRequest(){
		
		getPageManager().getAdminPage().showShoppingList();

		String notiMsg = getPageManager().getShoppingListPage().addNewServiceRequest();
		
        // Check the title noti message
		assertEquals(getMessageSource().getMessage("shopping.service.request.add",null, Locale.US), notiMsg.trim());
	}
	
	
	@Test (groups = {"basic sanity"})
	public void approveOrder(){
		
		try {
			getPageManager().getAdminPage().showShoppingList();


			String btnTitle = getPageManager().getShoppingListPage().approveOrder();
			assertEquals(LGConstants.SUBMIT_ORDER, btnTitle);
		} catch (Exception e) {
			setLog(e,"approveOrder");
			Assert.fail(e.getMessage());
		}
	}

	@Test (groups = {"basic sanity"})
	public void approveAndSubmitOrder(){
		
		try {
			getPageManager().getAdminPage().showShoppingList();

			String btnTitle = getPageManager().getShoppingListPage().approveAndSubmitOrder();
			
			// Check the title noti message
			assertEquals(LGConstants.MARK_AS_ARRIVED, btnTitle);
		}  catch (Exception e) {
			setLog(e,"approveAndSubmitOrder");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test(groups = {"basic sanity"})
	public void markOrderAsArrived(){
		
		try {

			getPageManager().getAdminPage().showShoppingList();
			
			getPageManager().getShoppingListPage().approveAndSubmitOrder();

			assertTrue(getPageManager().getShoppingListPage().markOrderAsArrived("None"/*without box*/));
		}  catch (Exception e) {
			setLog(e,"approveAndSubmitOrder");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test(groups = {"basic sanity"})
	public void markOrderAsArrivedInGridlessBox(){
		
		try {
			
			//create gridless box
			getPageManager().getBoxPage().showBoxes();
			String boxName = buildUniqueName("gridlessBox");
			getPageManager().getBoxPage().addNewGridlessBox(boxName);
			
			//add consumable to shopping list
			getPageManager().getAdminPage().showConsumables();		
			getPageManager().getConsumablesPage().addNewItem(buildUniqueName(LGConstants.CONSUMABLE_PREFIX));	
			getPageManager().getConsumablesPage().addToShoppingListFromShowPage();
			
			getPageManager().getAdminPage().showShoppingList();
			
			getPageManager().getShoppingListPage().approveAndSubmitOrder();
		
			assertTrue(getPageManager().getShoppingListPage().markOrderAsArrived(boxName));
			
			//search in boxes
			getPageManager().getBoxPage().showBoxes();
			int stocks = getPageManager().getBoxPage().checkStocksNumber(boxName);//gridless box need to be update in counter
			assertEquals(1,stocks);
			
		}  catch (Exception e) {
			setLog(e,"markOrderAsArrivedInGridlessBox");
			Assert.fail(e.getMessage());
		}
	}

}
