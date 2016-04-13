package com.biodata.labguru.tests.inventory;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.GenericHelper;
import com.biodata.labguru.LGConstants;
import com.biodata.labguru.model.CollectionItem;
import com.biodata.labguru.model.Stock;
import com.biodata.labguru.pages.inventory.CollectionPage;
import com.biodata.labguru.pages.inventory.purchasables.ConsumablesPage;
import com.biodata.labguru.pages.inventory.purchasables.sequenceable.GenericCollectionPage;
import com.biodata.labguru.tests.AbstractLGTest;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners({TestOrderRandomizer.class})
public abstract class AbstractCollectionsTest extends AbstractLGTest{
	
	protected abstract CollectionPage getPage();
	protected abstract String getCollectionNameForMessage();
	
	public abstract String importBioCollection()  throws InterruptedException;
	
	public abstract String getCollectionImportDetails();
	public abstract String getTemplateImportDetails();
	
	public abstract String getCollectionId();
	
	@Override
	protected String showModule() {
		
		return getPage().showCollection(getCollectionId());

	}
	
	@BeforeClass (alwaysRun = true, dependsOnMethods = "initialize")
	public void checkSingleCollection(){
		
		try {
			logger.debug( getDebugMessage() + "in checkSingleCollection");
			getPageManager().getAdminPage().showCollectionsAndSettings();
			if((getPage() instanceof GenericCollectionPage) || (getPage() instanceof ConsumablesPage))
				return;
			getPageManager().getAccountSettingPage().checkCollection(getCollectionId());
		} catch (Exception e) {
			setLog(e);
			Assert.fail(e.getMessage());
		}
	}
	
	@AfterClass(alwaysRun = true)
	public void uncheckSingleCollection(){
		
		try {
			getPageManager().getAdminPage().showCollectionsAndSettings();
			if((getPage() instanceof GenericCollectionPage) || (getPage() instanceof ConsumablesPage))
				return;
			if(LGConstants.DEFAULT_COLLECTIONS.contains(getCollectionId()))
					return;
			getPageManager().getAccountSettingPage().uncheckCollection(getCollectionId());
		} catch (Exception e) {
			setLog(e);
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void checkCustomizeTableView(){
		
		try {
			
			showTableIndex();
			
			if(!getPage().hasList()){
				addNewItem();
			}
			showTableIndex();
			
			String msg = getPage().checkCustomizeTableView();
			Assert.assertTrue(msg.equals(""),"Not all selected columns are shown: " + msg);
		}  catch (Exception e) {
			setLog(e,"customizeTableView");
			Assert.fail(e.getMessage());
		}
	}
	
	

	@Test (groups = {"basic sanity"})
	public abstract void addSimpleItem();
	
	@Test (groups = {"deep"})
	public void addItemSaveAndNew(){
		
		try {
			showTableIndex();
			String name = buildUniqueName(getCollectionNameForMessage());
			String msg = getPage().addItemSaveAndNew(name);
			
			// Check the title of the page
			assertEquals(getMessageSource().getMessage("collection.created.msg",new Object[]{getCollectionNameForMessage()}, Locale.US), msg.trim());
		} catch (Exception e) {
			setLog(e,"addNewItemSaveAndNew");
			Assert.fail(e.getMessage());
		}
	}
	
	
	
	@Test (groups = {"basic sanity"})
	public void deleteCollectionItemFromIndexTable(){
		
		try {
			//create 2 items so after delete we will still have one in the list
			showTableIndex();
			addNewItem();
			showTableIndex();
			String item = addNewItem();
			showTableIndex();
			boolean deleted = getPage().deleteItemFromIndexTable(item);
			Assert.assertTrue(deleted, "Item was not deleted");
			
			String countLabel = getPage().invokeSearchInCollection(item);
			Assert.assertEquals(countLabel,getMessageSource().getMessage("biocollections.no.search.result.msg",null,Locale.US));
			
		}  catch (Exception e) {
			setLog(e,"deleteCollectionItemFromIndexTable");
			Assert.fail(e.getMessage());
		}
	}
	

	@Test (groups = {"basic sanity"})
	public void deleteCollectionItemFromItemShowPage(){
		
		try {
			showTableIndex();
			addNewItem();
			showTableIndex();
			String item = addNewItem();
			getPage().deleteItemFromShowPage(item);
			showTableIndex();
			String countLabel = getPage().invokeSearchInCollection(item);
			Assert.assertEquals(countLabel,getMessageSource().getMessage("biocollections.no.search.result.msg",null,Locale.US));
			
		}  catch (Exception e) {
			setLog(e,"deleteCollectionItemFromItemShowPage");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void editCollectionItemFromItemShowPage(){
		
		try {
			showTableIndex();
			addNewItem();
			String msg = getPage().editItemFromShowPage();
			Assert.assertTrue(msg.endsWith("successfully updated."));
			//TODO - after all messages will be the same
			//Assert.assertEquals(msg,getMessageSource().getMessage("collection.updated.msg",new Object[]{getCollectionNameForMessage()},Locale.US));
			
		}  catch (Exception e) {
			setLog(e,"editCollectionItemFromItemShowPage");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void checkCollectionCustomizeBreadCrumbs(){

		try {
	
			String breadCrumbs = getPage().checkCollectionCustomizeBreadCrumbs(getCollectionId());

			Assert.assertTrue(breadCrumbs.isEmpty(),getMessageSource().getMessage("account.collection.customize.braed.msg",new Object[]{breadCrumbs}, Locale.US));
		} catch (Exception e) {
			setLog(e,"checkCollectionCustomizeBreadCrumbs");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void checkShowPageOfCreatedItem(){
		
		try {
			showTableIndex();
			
			CollectionPage page = (CollectionPage) getPage();
			String name = buildUniqueName(getCollectionId());
			String owner = getPage().getAccountName();
			CollectionItem newItem = new CollectionItem(name,owner);
		
			CollectionItem createdItem = page.checkCreatedItem(newItem);
			Assert.assertEquals(newItem.name,createdItem.name,"The created item was not created correctly");
			Assert.assertEquals(newItem.owner,createdItem.owner,"The created item was not created correctly");
			Assert.assertEquals(newItem.description,createdItem.description,"The created item was not created correctly");

		}catch (Exception e) {
			setLog(e,"checkShowPageOfCreatedItem");
			Assert.fail(e.getMessage());
		}
	}
	
	
	@Test (groups = {"deep"})
	public void addCustomFieldAndGenerateCollectionTemplate(){

		try {
				getPage().showCollectionsAndSettings();
				
				String collectionName = getCollectionId();
				
				List<String> fields = getPage().addCustomFieldToCollection(collectionName);
				
				getPage().showCollection(collectionName);
				String msg = getPage().generateTemplateAndImport(fields);
				Assert.assertEquals(msg,getMessageSource().getMessage("biocollections.import.msg",new Object[]{getTemplateImportDetails()},Locale.US));
				
				//delete the items of the specific collection
				getPage().deleteItemFromIndexTable(LGConstants.IMPORTED_NAME);
				//delete the generated files
				GenericHelper.deleteGeneratedFiles();
				
				getPage().showCollectionsAndSettings();
				getPage().deleteCustomFieldsFromCollection(collectionName);
			
		} catch (Exception e) {
			setLog(e,"addCustomFieldAndGenerateCollectionTemplate");
			Assert.fail(e.getMessage());
		}
	}
	


	@Test (groups = {"deep"})
	public void importCollection() {
		
		try {
			showTableIndex();
			String msg = importBioCollection();
			Assert.assertEquals(msg,getMessageSource().getMessage("biocollections.import.msg",new Object[]{getCollectionImportDetails()},Locale.US));
			
			//delete all items in the collection when test finishd
			getPage().deleteAllItems(getCollectionId());
			
		}catch (Exception e) {
			setLog(e,"importCollection");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void addStockFromMetaDataSection(){
		
		try {
			
			getPageManager().getBoxPage().deleteAllItemsFromTable();
			
			showTableIndex();
			addNewItem();
			
			TimeUnit.SECONDS.sleep(3);
			
			String stockName = buildUniqueName(LGConstants.STOCK_PREFIX);

			//check the the location exist
			AssertJUnit.assertTrue("Stock with name '" + stockName + "' was not craeted as should be.",getPage().addStock(stockName));

		
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void addStockFromStocksTab(){
		
		try {
			
			showTableIndex();
			addNewItem();
			
			TimeUnit.SECONDS.sleep(3);
			
			
			for (int i = 0; i < LGConstants.STOCK_TYPES_ARRAY.length; i++) {
				String type = LGConstants.STOCK_TYPES_ARRAY[i];
				String stockName = type + "_"+ buildUniqueName(LGConstants.STOCK_PREFIX);
			
				Stock addedStock = getPage().addStockFromStocksTab(stockName,type);
				AssertJUnit.assertEquals("Stock with name '" + stockName + "' was not craeted as should be." ,addedStock.name,stockName);
				//check the the location exist
				AssertJUnit.assertEquals("Stock with name '" + stockName + "' with type '" + type + "'  was not craeted as should be.",addedStock.type,type);
			}

		
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}

	@Test (groups = {"basic sanity"})
	public void addStockLocatedInBox(){
		
		try {
			
			String boxName = buildUniqueName(LGConstants.BOX_PREFIX);
			getPageManager().getBoxPage().addNewBox(boxName, "1");
			
			showTableIndex();
			addNewItem();
			
			String stockName = buildUniqueName(LGConstants.STOCK_PREFIX);
			
			//check the the location exist
			AssertJUnit.assertTrue("Stock with name '" + stockName + "' was not craeted as should be.",
					getPage().addStockLocatedInBox(stockName,boxName));

		
		} catch (Exception e) {
			setLog(e,"addStockLocatedInBox");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void deleteAllItemsFromIndexTable(){
		
		try {
			
			if(getPage().hasList()){
				getPage().deleteAllItemsFromTable();
				//wait until page is refreshed
				TimeUnit.SECONDS.sleep(8);
			}

			boolean hasList = getPage().hasList();
			AssertJUnit.assertFalse("Not all items were deleted from index table.",hasList);//check that there are no items in the list
		
		} catch (Exception e) {
			setLog(e,"deleteAllItemsFromIndexTable");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	
	@Test (groups = {"basic sanity"})
	public void markAsConsumedStockFromStocksTab(){
		
		try {
			
			showTableIndex();
			addNewItem();
			
			String stockName = buildUniqueName(LGConstants.STOCK_PREFIX);
			getPage().addStockLocatedInBox(stockName,null);
			
			boolean archiveSucceeded = getPage().markAsConsumedStockInTable(stockName);
			AssertJUnit.assertTrue("The stock was not marked as expected",archiveSucceeded);
			
			getPageManager().getAdminPage().showStocks();
			
			assertTrue(getPageManager().getStockPage().searchInConsumedStocks(stockName));
		
		} catch (Exception e) {
			setLog(e,"markAsConsumedStockFromStocksTab");
			AssertJUnit.fail(e.getMessage());
		}
	}

}
