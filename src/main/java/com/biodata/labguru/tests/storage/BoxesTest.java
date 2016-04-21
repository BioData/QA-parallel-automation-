package com.biodata.labguru.tests.storage;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.model.Stock;
import com.biodata.labguru.pages.storage.BoxPage;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public class BoxesTest extends AbstractStoragesTest{
	
	@AfterMethod
	public void deleteAllBoxesAndStocks(){
		//delete stocks
		try {
			markAsConsumedStocks();
			getPageManager().getBoxPage().deleteAllItemsFromTable();
			
		} catch (InterruptedException e) {
			setLog(e,"deleteAllBoxesAndStocks");
		}
	}
	
	
	@Test(groups = {"knownBugs"})//LAB-1149
	public void changeStockContentInGridlessBoxAndCheckDetails(){
		
		try {
			
			//create gridless box
			getPageManager().getBoxPage().showBoxes();
			String boxName = buildUniqueName("gridlessBox");
			getPageManager().getBoxPage().addNewGridlessBox(boxName);
			String stockName = buildUniqueName(LGConstants.STOCK_PREFIX);
			getPageManager().getBoxPage().addStockToGridlessBox(stockName,null);
			
			String celline = buildUniqueName(LGConstants.CELL_LINE_PREFIX);
			getPageManager().getCellLinesPage().addNewItem(celline);
			
			getPageManager().getAdminPage().showBoxes();
			getPageManager().getBoxPage().viewBoxShowPage(boxName);
			Stock stock = getPageManager().getBoxPage().editStockContent(stockName,celline);
			Assert.assertEquals(stock.content, celline);
		
			
		}  catch (Exception e) {
			setLog(e,"changeStockContentInGridlessBoxAndCheckDetails");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test(groups = {"knownBugs"})//LAB-1145
	public void checkStockContentInGridlessBox(){
		
		try {
			//create collection item
			String celline = buildUniqueName(LGConstants.CELL_LINE_PREFIX);
			getPageManager().getCellLinesPage().addNewItem(celline);
			
			//create gridless box
			getPageManager().getBoxPage().showBoxes();
			String boxName = buildUniqueName("gridlessBox");
			getPageManager().getBoxPage().addNewGridlessBox(boxName);
			String stockName = buildUniqueName(LGConstants.STOCK_PREFIX);
			getPageManager().getBoxPage().addStockToGridlessBox(stockName,celline);
			
			Stock stock = getPageManager().getBoxPage().checkStockFromListInBox(stockName);
			Assert.assertEquals(stock.content, celline);
		
			
		}  catch (Exception e) {
			setLog(e,"checkStockContentInGridlessBox");
			Assert.fail(e.getMessage());
		}
	}
	
	
	@Test (groups = {"deep"})
	public void addTagToBoxesFromIndexTable(){
		
		try {
			showTableIndex();
			TimeUnit.SECONDS.sleep(2);
			String box1 = addNewItem();
			showTableIndex();
			String box2 = addNewItem();
			List<String> boxesToTag = new ArrayList<String>();
			boxesToTag.add(box1);
			boxesToTag.add(box2);
			
			showTableIndex();
			String tagName = buildUniqueName("BoxTag");
			boolean succeeded = getPageManager().getBoxPage().addTagFromIndexTable(tagName,boxesToTag);
			
			AssertJUnit.assertTrue("Tag was not craeted as should be.",succeeded);
			
			succeeded = getPageManager().getAdminPage().searchTagAndSearchByIt(tagName, box2);
			AssertJUnit.assertTrue("Search by tag not working as should be.", succeeded);
			getPageManager().getAdminPage().deleteTagFromTaggedEntitiesList();
			
		} catch (Exception e) {
			setLog(e,"addTagToBoxesFromIndexTable");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void editBoxFromShowPage(){
		
		try {
			showTableIndex();
			addNewItem();
			String msg = getPageManager().getBoxPage().editItemFromShowPage();
			Assert.assertTrue(msg.endsWith("successfully updated."));
			
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
	
	
	@Test (groups = {"basic sanity"})
	public void markAsConsumedStockFromBoxCheckUpdateInTableIndex(){
		
		try {
			showTableIndex();
			
			String newBox = buildUniqueName(LGConstants.BOX_WITH_STOCK_PREFIX);
			getPageManager().getBoxPage().addNewBox(newBox,"1");
			
			showTableIndex();
			int stocks = getPageManager().getBoxPage().checkStocksNumber(newBox);
			assertEquals(0,stocks);//check no stocks in the box
	
			
			String stockToMarkAsUsed = "test";
	
			getPageManager().getBoxPage().searchBoxAndAddStockToBox(newBox,stockToMarkAsUsed,1);
			showTableIndex();
			stocks = getPageManager().getBoxPage().checkStocksNumber(newBox);
			assertEquals(1,stocks);//check 1 stock added to the box
			
			//true - represent to mark as used stock
			getPageManager().getBoxPage().viewBoxShowPage(newBox);
			getPageManager().getBoxPage().markAsConsumedSelectedStock(stockToMarkAsUsed);
			
			showTableIndex();
			stocks = getPageManager().getBoxPage().checkStocksNumber(newBox);
			assertEquals(0,stocks);//check 1 stock deleted from box - again no stocks
			
		}  catch (Exception e) {
			setLog(e,"markAsConsumedStockFromBoxCheckUpdateInTableIndex");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	
	@Test (groups = {"deep"})
	public void markAsConsumedStockFromBoxTwice(){
		
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
			//mark as used
			getPageManager().getBoxPage().markAsConsumedSelectedStock(stockToArchive);
	
			showTableIndex();
			stocks = getPageManager().getBoxPage().checkStocksNumber(newBox);
			assertTrue("stocks should be equal to 1", (stocks==0));//check 1 stock removed from the box
			
			
		}  catch (Exception e) {
			setLog(e,"markAsConsumedStockFromBoxTwice");
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
	public void markAsConsumedStockFromStockPageView(){
		
		try {
			showTableIndex();
			
			String newBox = buildUniqueName(LGConstants.BOX_WITH_STOCK_PREFIX);
			getPageManager().getBoxPage().addNewBox(newBox,"1");
			
			String stockName= buildUniqueName(LGConstants.STOCK_PREFIX);
	
			String pageTitle = getPageManager().getBoxPage().createStock(stockName);
			assertEquals("Stocks - " + stockName, pageTitle);
			
			
			String notyMsg = getPageManager().getStockPage().markAsConsumedStock();
			assertEquals(getMessageSource().getMessage("boxes.stock.marked.consumed.msg",new Object[]{"1"}, Locale.US), notyMsg);
			
			String updateStorageLocation =  getPageManager().getStockPage().checkStorage();
			assertTrue(updateStorageLocation.equals("None"));
			
		}  catch (Exception e) {
			setLog(e,"archiveStockFromStockPageView");
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
	public void markAsConsumedStockFromBoxView(){
		
		try {
			showTableIndex();
			
			String newBox = buildUniqueName(LGConstants.BOX_WITH_STOCK_PREFIX);
			getPageManager().getBoxPage().addNewBox(newBox,"1");
			
			String stockToMarkAsUsed = "markAsConsumedStockFromBoxView";
	
			String notyMsg = getPageManager().getBoxPage().markAsConsumedStockFromBoxView(stockToMarkAsUsed);
			assertEquals(getMessageSource().getMessage("boxes.stock.marked.consumed.msg",new Object[]{"1"}, Locale.US), notyMsg);
			
			getPageManager().getAdminPage().showStocks();
			
			assertTrue(getPageManager().getStockPage().searchInConsumedStocks(stockToMarkAsUsed));
			
		} catch (Exception e) {
			setLog(e,"markAsConsumedStockFromBoxView");
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
	
	

	
	private void markAsConsumedStocks() throws InterruptedException {
		//delete stocks	
		getPageManager().getAdminPage().showStocks();
		getPageManager().getStockPage().deleteAllItemsFromTable();
		getPageManager().getStockPage().deleteUsedStocksFromView();
	}

	@Test (groups = {"basic sanity"})
	public void markAsConsumedStockFromTableView(){
		
		try {
			showTableIndex();
			
			String newBox = buildUniqueName(LGConstants.BOX_WITH_STOCK_PREFIX);
			getPageManager().getBoxPage().addNewBox(newBox,"1");
			
			String stockName = "markAsConsumedStockFromTableView";
			
			String notyMsg = getPageManager().getBoxPage().markAsConsumedStockFromTableView(stockName);
			assertEquals(getMessageSource().getMessage("boxes.stock.marked.consumed.msg",new Object[]{"1"}, Locale.US), notyMsg);
			
			getPageManager().getAdminPage().showStocks();
			
			assertTrue(getPageManager().getStockPage().searchInConsumedStocks(stockName));
		} catch (Exception e) {
			setLog(e,"markAsConsumedStockFromTableView");
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
	
	@Test (groups = {"basic sanity"})
	public void markAsConsumedAllStocksFromTableView(){
		
		try {
			showTableIndex();
			
			String newBox = buildUniqueName(LGConstants.BOX_WITH_STOCK_PREFIX);
			getPageManager().getBoxPage().addNewBox(newBox,"1");
			
			String stockName = "StockToMarkFromTableView";
			int numOfStocks = 3;
			getPageManager().getBoxPage().addStock(stockName, numOfStocks);
			
			String notyMsg = getPageManager().getBoxPage().markAsConsumedStocksFromTableView();
			if(numOfStocks == 1)
				assertEquals(getMessageSource().getMessage("boxes.stock.marked.consumed.msg",new Object[]{String.valueOf(numOfStocks)}, Locale.US), notyMsg);
			else
				assertEquals(getMessageSource().getMessage("boxes.stocks.marked.consumed.msg",new Object[]{String.valueOf(numOfStocks)}, Locale.US), notyMsg);
				
		} catch (Exception e) {
			setLog(e,"markAsConsumedAllStocksFromTableView");
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
