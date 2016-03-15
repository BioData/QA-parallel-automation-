package com.biodata.labguru.tests.storage;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.storage.BoxPage;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public class BoxesTest extends AbstractStoragesTest{
	
	@AfterMethod
	public void deleteAllStocks(){
		//delete stocks
		try {
			deleteStocks();
			getPageManager().getBoxPage().deleteAllItemsFromTable();
		} catch (InterruptedException e) {
			setLog(e,"deleteAllStocks");
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void editBoxFromShowPage(){
		
		try {
			showTableIndex();
			addNewItem();
			String msg = getPageManager().getBoxPage().editItemFromShowPage();
			Assert.assertTrue(msg.endsWith("successfully updated."));
			//TODO - after all messages will be the same
			//Assert.assertEquals(msg,getMessageSource().getMessage("collection.updated.msg",new Object[]{"Box"},Locale.US));
			
		}  catch (Exception e) {
			setLog(e,"editBoxFromShowPage");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void deleteBoxFromShowPage(){
		
		try {
			showTableIndex();
			addNewItem();
		
			String msg = getPageManager().getBoxPage().deleteFromShowPage();
			AssertJUnit.assertTrue(!msg.isEmpty());
			
		}  catch (Exception e) {
			setLog(e, "deleteBoxFromShowPage");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void checkCustomizeTableView(){
		
		try {
			
			showTableIndex();
			
			if(!getPageManager().getBoxPage().hasList()){
				addNewItem();
			}
			showTableIndex();
			
			String msg = getPageManager().getBoxPage().checkCustomizeTableView();
			Assert.assertTrue(msg.equals(""),"Not all selected columns are shown: " + msg);
		}  catch (Exception e) {
			setLog(e,"customizeTableView");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void addStockFromBoxCheckUpdateInTableIndex(){
		
		try {
			showTableIndex();
			
			String newBox = buildUniqueName(LGConstants.BOX_WITH_STOCK_PREFIX);
			getPageManager().getBoxPage().addNewBox(newBox,"1");
			
			showTableIndex();
			int stocks = getPageManager().getBoxPage().checkStocksNumber(newBox);
			assertEquals(0,stocks);
	
			getPageManager().getBoxPage().searchBoxAndAddStockToBox(newBox,"testStock",1);
			showTableIndex();
			stocks = getPageManager().getBoxPage().checkStocksNumber(newBox);
			assertEquals(1,stocks);
			
			
		}  catch (Exception e) {
			setLog(e,"addStockFromBoxCheckUpdateInTableIndex");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void deleteStockFromBoxCheckUpdateInTableIndex(){
		
		try {
			showTableIndex();
			
			String newBox = buildUniqueName(LGConstants.BOX_WITH_STOCK_PREFIX);
			getPageManager().getBoxPage().addNewBox(newBox,"1");
			
			showTableIndex();
			int stocks = getPageManager().getBoxPage().checkStocksNumber(newBox);
			assertEquals(0,stocks);//check no stocks in the box
	
			
			String stockToDelete = "test";
	
			getPageManager().getBoxPage().searchBoxAndAddStockToBox(newBox,stockToDelete,1);
			showTableIndex();
			stocks = getPageManager().getBoxPage().checkStocksNumber(newBox);
			assertEquals(1,stocks);//check 1 stock added to the box
			
			//true - represent to delete stock
			getPageManager().getBoxPage().viewBoxShowPage(newBox);
			getPageManager().getBoxPage().deleteArchiveSelectedStock(stockToDelete,true);
			
			showTableIndex();
			stocks = getPageManager().getBoxPage().checkStocksNumber(newBox);
			assertEquals(0,stocks);//check 1 stock deleted from box - again no stocks
			

			
		}  catch (Exception e) {
			setLog(e,"deleteStockFromBoxCheckUpdateInTableIndex");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void archieveStockFromBoxCheckUpdateInTableIndex(){
		
		try {
			showTableIndex();
			
			String newBox = buildUniqueName(LGConstants.BOX_WITH_STOCK_PREFIX);
			getPageManager().getBoxPage().addNewBox(newBox,"1");
			
			showTableIndex();
			int stocks = getPageManager().getBoxPage().checkStocksNumber(newBox);
			assertEquals(0,stocks);//check no stocks in the box
	
			
			String stockToArchive = "test";
	
			getPageManager().getBoxPage().searchBoxAndAddStockToBox(newBox,stockToArchive,1);
			showTableIndex();
			stocks = getPageManager().getBoxPage().checkStocksNumber(newBox);
			assertEquals(1,stocks);//check 1 stock added to the box
			
			//true - represent to delete stock
			getPageManager().getBoxPage().viewBoxShowPage(newBox);
			getPageManager().getBoxPage().deleteArchiveSelectedStock(stockToArchive,false);
			
			showTableIndex();
			stocks = getPageManager().getBoxPage().checkStocksNumber(newBox);
			assertEquals(0,stocks);//check 1 stock deleted from box - again no stocks
			
		}  catch (Exception e) {
			setLog(e,"archieveStockFromBoxCheckUpdateInTableIndex");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	
	@Test (groups = {"deep"})
	public void archieveStockFromBoxTwice(){
		
		try {
			showTableIndex();
			
			String newBox = buildUniqueName(LGConstants.BOX_WITH_STOCK_PREFIX);
			getPageManager().getBoxPage().addNewBox(newBox,"1");
			
			showTableIndex();
			int stocks = getPageManager().getBoxPage().checkStocksNumber(newBox);
			assertEquals(0,stocks);//check no stocks in the box
	
			
			String stockToArchive = "test";
	
			getPageManager().getBoxPage().searchBoxAndAddStockToBox(newBox,stockToArchive,1);
			showTableIndex();
			stocks = getPageManager().getBoxPage().checkStocksNumber(newBox);
			assertEquals(1,stocks);//check 1 stock added to the box
			
			
			getPageManager().getBoxPage().viewBoxShowPage(newBox);
			//archive
			getPageManager().getBoxPage().deleteArchiveSelectedStock(stockToArchive,false);
	
			showTableIndex();
			stocks = getPageManager().getBoxPage().checkStocksNumber(newBox);
			assertTrue("stocks should be equal to 1", (stocks==0));//check 1 stock removed from the box
			
			
		}  catch (Exception e) {
			setLog(e,"archieveStockFromBoxCheckUpdateInTableIndex");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void simpleSearchBoxFound(){
		
		try {
			showTableIndex();
			String newBox = LGConstants.BOX_PREFIX;
			
			for ( int i = 1;i <= 3; i++) {
				newBox =  buildUniqueName(LGConstants.BOX_PREFIX);
				getPageManager().getBoxPage().addNewBox(newBox,"1");
				showTableIndex();	
			}
			
			//search last created box- should have 1 item
			getPageManager().getBoxPage().invokeSearchInBoxes(newBox);
			int count = getPageManager().getBoxPage().findSpecificBoxAfterSearchInvoked(newBox);
			
			AssertJUnit.assertEquals(count,1);
		}  catch (Exception e) {
			setLog(e,"simpleSearchBox");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void simpleSearchBoxNotFound(){
		
		try {
			showTableIndex();
			String newBox = "tralalala";

			getPageManager().getBoxPage().addNewBox(buildUniqueName(LGConstants.BOX_PREFIX),"1");
			getPageManager().getAdminPage().showBoxes();		
			
			//search newBox- should have 0 item - it has not been created
			getPageManager().getBoxPage().invokeSearchInBoxes(newBox);
			int count = getPageManager().getBoxPage().findSpecificBoxAfterSearchInvoked(newBox);
			
			AssertJUnit.assertEquals(count,0);
		}  catch (Exception e) {
			setLog(e,"simpleSearchBoxNotFound");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void viewBoxShowPage(){
		
		try {
			showTableIndex();
			
			String newBox = buildUniqueName(LGConstants.BOX_TOSHOW_PREFIX);
			String pageTitle =  getPageManager().getBoxPage().addNewBox(newBox,"1");		
			
			assertTrue(pageTitle.contains(newBox));
			
		}  catch (Exception e) {
			setLog(e,"viewBoxShowPage");
			AssertJUnit.fail(e.getMessage());
		}
	}
	


	@Override
	public void showMenu(){
		
		try {
			String pageTitle = showTableIndex();
			
			boolean hasBoxes = getPageManager().getBoxPage().hasList();
			if(hasBoxes){
			    // Check the title of the page
				AssertJUnit.assertEquals(getMessageSource().getMessage("boxes.title.has.boxes",null, Locale.US), pageTitle);
			}else{
				 // Check the title of the page
				AssertJUnit.assertEquals(getMessageSource().getMessage("boxes.title.no.boxes",null, Locale.US), pageTitle);
			}
		}  catch (Exception e) {
			setLog(e,"canSelectBoxes");
			AssertJUnit.fail(e.getMessage());
		}  

	}
	
	@Test (groups = {"basic sanity"})
	public void addNewBoxWithCellLine(){
		
		try {
			showTableIndex();
			
			String boxToAdd = buildUniqueName(LGConstants.BOX_CELLINE_PREFIX);
			String title = getPageManager().getBoxPage().addNewBoxWithCellLine(boxToAdd,"1");
			assertEquals(title,boxToAdd);
		}  catch (Exception e) {
			setLog(e,"addNewBoxWithCellLine");
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	@Test (groups = {"basic sanity"})
	public void addNewBoxDefaultContent(){
		
		try {
			showTableIndex();
			
			String boxToAdd = buildUniqueName(LGConstants.BOX_PREFIX);
			String title = getPageManager().getBoxPage().addNewBox(boxToAdd,"1");
			assertEquals(title,boxToAdd);
		}  catch (Exception e) {
			setLog(e,"addNewBoxDefaultContent");
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	@Test (groups = {"basic sanity"})
	public void addExistingBoxShouldFail(){
		
		try {
			showTableIndex();
			
			String newBox = "BoxWithSameName";
			getPageManager().getBoxPage().addNewBox(newBox,"1");
			
			getPageManager().getAdminPage().showBoxes();
			String title = getPageManager().getBoxPage().addNewBox(newBox,"1");

			assertEquals(getMessageSource().getMessage("boxes.error.box.save",null, Locale.US),title);
		}  catch (Exception e) {
			setLog(e,"addExistingBoxShouldFail");
			AssertJUnit.fail(e.getMessage());
		}
		
	}
	

	
	@Test (groups = {"basic sanity"})
	public void addStockFromBox(){
		
		try {
			showTableIndex();
			
			String newBox = buildUniqueName(LGConstants.BOX_WITH_STOCK_PREFIX);
			getPageManager().getBoxPage().addNewBox(newBox,"1");

			String stockToAdd = buildUniqueName(LGConstants.STOCK_PREFIX);
			
			String pageTitle = getPageManager().getBoxPage().addStockFromBox(stockToAdd);
			assertEquals(pageTitle,stockToAdd);

			
		}  catch (Exception e) {
			setLog(e,"addStockFromBox");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void deleteStockFromStockPageView(){
		
		try {
			showTableIndex();
			
			String newBox = buildUniqueName(LGConstants.BOX_WITH_STOCK_PREFIX);
			getPageManager().getBoxPage().addNewBox(newBox,"1");
			
			String stockToDelete = "deleteStockFromStockPageView";
			String pageTitle = getPageManager().getBoxPage().createStockToDeleteArchiveFromPageView(stockToDelete);
			assertEquals("Stocks - " + stockToDelete , pageTitle);
			
			//true - represent to delete stock
			String notyMsg = getPageManager().getStockPage().deleteArchiveStock(true);
			assertEquals(getMessageSource().getMessage("boxes.stock.deleted.msg",new Object[]{"1"}, Locale.US), notyMsg);

			
		}  catch (Exception e) {
			setLog(e,"deleteStockFromStockPageView");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void archiveStockFromStockPageView(){
		
		try {
			showTableIndex();
			
			String newBox = buildUniqueName(LGConstants.BOX_WITH_STOCK_PREFIX);
			getPageManager().getBoxPage().addNewBox(newBox,"1");
			
			String stockToArchive= "archiveStockFromStockPageView";
			
			//false - represent to archive stock
			String pageTitle = getPageManager().getBoxPage().createStockToDeleteArchiveFromPageView(stockToArchive);
			assertEquals("Stocks - " + stockToArchive, pageTitle);
			
			String notyMsg = getPageManager().getStockPage().deleteArchiveStock(false);
			assertEquals(getMessageSource().getMessage("boxes.stock.archived.msg",new Object[]{"1"}, Locale.US), notyMsg);
			
			getPageManager().getAdminPage().showStocks();
			
			assertTrue(getPageManager().getStockPage().showArchiveView(stockToArchive));
			
		}  catch (Exception e) {
			setLog(e,"archiveStockFromStockPageView");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	
	@Test (groups = {"basic sanity"})
	public void deleteStockFromTableView(){
		
		try {
			showTableIndex();
			
			String newBox = buildUniqueName(LGConstants.BOX_WITH_STOCK_PREFIX);
			getPageManager().getBoxPage().addNewBox(newBox,"1");
			
			String stockToDelete = "deleteStockFromTableView";
			
			//true - represent to delete stock
			String notyMsg = getPageManager().getBoxPage().deleteArchiveStockFromTableView(stockToDelete,true);
			assertEquals(getMessageSource().getMessage("boxes.stock.deleted.msg",new Object[]{"1"}, Locale.US), notyMsg);
		} catch (Exception e) {
			setLog(e,"deleteStockFromTableView");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void editStockFromBoxView(){
		try {
			showTableIndex();
			
			String newBox = buildUniqueName(LGConstants.BOX_WITH_STOCK_PREFIX);
			getPageManager().getBoxPage().addNewBox(newBox,"1");

			String stockToEdit = "editStockFromBoxView";
			boolean edited = getPageManager().getBoxPage().editStockFromBoxView(stockToEdit);
			
			Assert.assertTrue(edited, "The stock was not edited as expected.");

			
		} catch (Exception e) {
			setLog(e,"editStockFromBoxView");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void deleteStockFromBoxView(){
		
		try {
			showTableIndex();
			
			String newBox = buildUniqueName(LGConstants.BOX_WITH_STOCK_PREFIX);
			getPageManager().getBoxPage().addNewBox(newBox,"1");
			
			String stockToDelete = "deleteStockFromBoxView";
			
			//true - represent to delete stock
			String notyMsg = getPageManager().getBoxPage().deleteArchiveStockFromBoxView(stockToDelete,true);
			assertEquals(getMessageSource().getMessage("boxes.stock.deleted.msg",new Object[]{"1"}, Locale.US), notyMsg);
			
		} catch (Exception e) {
			setLog(e,"deleteStockFromBoxView");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void archiveStockFromBoxView(){
		
		try {
			showTableIndex();
			
			String newBox = buildUniqueName(LGConstants.BOX_WITH_STOCK_PREFIX);
			getPageManager().getBoxPage().addNewBox(newBox,"1");
			
			String stockToArchive = "archiveStockFromBoxView";
			
			//false - represent to archive stock
			String notyMsg = getPageManager().getBoxPage().deleteArchiveStockFromBoxView(stockToArchive,false);
			assertEquals(getMessageSource().getMessage("boxes.stock.archived.msg",new Object[]{"1"}, Locale.US), notyMsg);
			
			getPageManager().getAdminPage().showStocks();
			
			assertTrue(getPageManager().getStockPage().showArchiveView(stockToArchive));
			
		} catch (Exception e) {
			setLog(e,"archiveStockFromBoxView");
			AssertJUnit.fail(e.getMessage());
		}
	}

	
	@Test (groups = {"basic sanity"})
	public void editStockFromBoxTableView(){
		try {
			showTableIndex();
			
			String newBox = buildUniqueName(LGConstants.BOX_WITH_STOCK_PREFIX);
			getPageManager().getBoxPage().addNewBox(newBox,"1");

			String stockToEdit = "editStockFromBoxTableView";
			boolean edited = getPageManager().getBoxPage().editStockFromBoxTableView(stockToEdit);
			
			Assert.assertTrue(edited, "The stock was not edited as expected.");

			
		} catch (Exception e) {
			setLog(e,"editStockFromBoxTableView");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	

	
	private void deleteStocks() throws InterruptedException {
		//delete stocks	
		getPageManager().getAdminPage().showStocks();
		getPageManager().getStockPage().deleteAllItemsFromTable();

	}

	@Test (groups = {"basic sanity"})
	public void archiveStockFromTableView(){
		
		try {
			showTableIndex();
			
			String newBox = buildUniqueName(LGConstants.BOX_WITH_STOCK_PREFIX);
			getPageManager().getBoxPage().addNewBox(newBox,"1");
			
			String stockToArchive = "archiveStockFromTableView";
			
			//false - represent to archive stock
			String notyMsg = getPageManager().getBoxPage().deleteArchiveStockFromTableView(stockToArchive,false);
			assertEquals(getMessageSource().getMessage("boxes.stock.archived.msg",new Object[]{"1"}, Locale.US), notyMsg);
			
			getPageManager().getAdminPage().showStocks();
			
			assertTrue(getPageManager().getStockPage().showArchiveView(stockToArchive));
		} catch (Exception e) {
			setLog(e,"archiveStockFromTableView");
			AssertJUnit.fail(e.getMessage());
		}
	}
	

	@Test  (groups = {"deep"})
	public void editStocksFromBoxView(){
		try {
			showTableIndex();
			
			String newBox = buildUniqueName(LGConstants.BOX_WITH_STOCK_PREFIX);
			getPageManager().getBoxPage().addNewBox(newBox,"1");

			String stockToEdit = "stockToEdit";
			boolean edited = getPageManager().getBoxPage().editStocksFromBoxView(stockToEdit);
			
			Assert.assertTrue(edited, "The stock was not edited as expected.");

			
		} catch (Exception e) {
			setLog(e,"editStocksFromBoxView");
			AssertJUnit.fail(e.getMessage());
		}
	}

	
	@Test  (groups = {"deep"})
	public void editStocksFromBoxTableView(){
		try {
			showTableIndex();
			
			String newBox = buildUniqueName(LGConstants.BOX_WITH_STOCK_PREFIX);
			getPageManager().getBoxPage().addNewBox(newBox,"1");

			String stockToEdit = "stockToEdit";
			boolean edited = getPageManager().getBoxPage().editStocksFromBoxTableView(stockToEdit,2);
			
			Assert.assertTrue(edited, "The stock was not edited as expected.");
			
		} catch (Exception e) {
			setLog(e,"editStocksFromBoxTableView");
			AssertJUnit.fail(e.getMessage());
		}
	}

	
	@Test (groups = {"basic sanity"})
	public void duplicateStock(){
		try {
			showTableIndex();
			
			String newBox = buildUniqueName(LGConstants.BOX_WITH_STOCK_PREFIX);
			getPageManager().getBoxPage().addNewBox(newBox,"1");

			String stockToDuplicate = "stockToDuplicate";
			
			String msg = getPageManager().getBoxPage().duplicateStock(stockToDuplicate);
			
			assertEquals(getMessageSource().getMessage("boxes.duplicate.stock.succeed", null,  Locale.US),msg);

			
		} catch (Exception e) {
			setLog(e,"duplicateStock");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void deleteAllBoxesFromTableView(){
		
		try {
			
			showTableIndex();
			if(getPageManager().getBoxPage().hasList()){
				getPageManager().getBoxPage().deleteAllItemsFromTable();
				showTableIndex();
			}

			AssertJUnit.assertFalse("Not all items were deleted from index table.",getPageManager().getBoxPage().hasList());//check that there are no items in the list
		
		} catch (Exception e) {
			setLog(e,"deleteAllBoxesFromTableView");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void deleteAllStocksFromTableView(){
		
		try {
			showTableIndex();
			
			String newBox = buildUniqueName(LGConstants.BOX_WITH_STOCK_PREFIX);
			getPageManager().getBoxPage().addNewBox(newBox,"1");
			
			String stockToDelete = "deleteStockFromTableView";
			int numOfStocks = 3;
			getPageManager().getBoxPage().addStock(stockToDelete, numOfStocks);
			
			String notyMsg = getPageManager().getBoxPage().deleteStocksFromTableView();
			if(numOfStocks == 1)
				assertEquals(getMessageSource().getMessage("boxes.stock.deleted.msg",new Object[]{String.valueOf(numOfStocks)}, Locale.US), notyMsg);
			else
				assertEquals(getMessageSource().getMessage("boxes.stocks.deleted.msg",new Object[]{String.valueOf(numOfStocks)}, Locale.US), notyMsg);
				
		} catch (Exception e) {
			setLog(e,"deleteAllStocksFromTableView");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void customizeTableViewInBoxIndexPageCheckBoxName(){
		
		try {
			showTableIndex();
			addNewItem();
			showTableIndex();
			Assert.assertTrue(getPageManager().getBoxPage().customizeTableView(),"The customization of 'Box name' header failed.");
			
			if(!getPageManager().getBoxPage().isHeaderAppears(BoxPage.BOX_NAME_HEADER_ID))
				getPageManager().getBoxPage().customizeTableView();
		}  catch (Exception e) {
			setLog(e,"customizeTableViewInBoxIndexPageCheckBoxName");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Override
	protected String showModule() {
		return getPageManager().getAdminPage().showBoxes();
	}

	@Override
	protected String addNewItem() throws InterruptedException {
		
		String name = buildUniqueName(LGConstants.BOX_WITH_STOCK_PREFIX);
		getPageManager().getBoxPage().addNewBox(name,"1");
		return name;
	}

}
