package com.biodata.labguru.tests.storage;

import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.model.Stock;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public class StocksTest extends AbstractStoragesTest{


	@Override
	public void showMenu(){
		
		String pageTitle = showTableIndex();
		
		if(getPageManager().getStockPage().hasList()){				
			// Check the title of the page - if has stocks it will show 'Stocks'
			AssertJUnit.assertEquals(getMessageSource().getMessage("stocks.title.has.stocks",null,Locale.US), pageTitle);
		}else{
			// if has no stocks it will show 'Boxes - .....'(add new box or the name of exist box)
			AssertJUnit.assertTrue(pageTitle.startsWith("Boxes"));
		}
	}
	
	
	@Test(groups = {"basic sanity"})
	public void editStock(){
		
		try {
			
			//create 1 stock
			getPageManager().getAdminPage().showBoxes();
			
			String newBox = buildUniqueName(LGConstants.BOX_WITH_STOCK_PREFIX);
			getPageManager().getBoxPage().addNewBox(newBox,"1");		
			getPageManager().getBoxPage().addStock("stock", 1);
			
			getPageManager().getAdminPage().showStocks();

			
			String newName = buildUniqueName(LGConstants.STOCK_PREFIX);
			String stockType = LGConstants.STOCK_TYPES_ARRAY[3];//Vial
			Stock stock = getPageManager().getStockPage().editStock("stock",newName,stockType);
			Assert.assertEquals(stock.name, newName);
			Assert.assertEquals(stock.type, stockType);
		} catch (Exception e) {
			setLog(e,"editStock");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test(groups = {"deep"})
	public void changeStockLocation(){
		
		try {
			
			//create 1 stock in box1
			getPageManager().getAdminPage().showBoxes();
			String box1 = buildUniqueName(LGConstants.BOX_WITH_STOCK_PREFIX);
			getPageManager().getBoxPage().addNewBox(box1,"1");
			String stockName = buildUniqueName(LGConstants.STOCK_PREFIX);
			getPageManager().getBoxPage().addStock(stockName, 1);
			
			
			String box2 = buildUniqueName(LGConstants.BOX_WITH_STOCK_PREFIX);
			getPageManager().getBoxPage().addNewBox(box2,"1");
			
			getPageManager().getAdminPage().showStocks();
			
			//change the stock location into box2
			Stock stock = getPageManager().getStockPage().changeStockLocation(box2,stockName);

			Assert.assertEquals(stock.location,box2);
		} catch (Exception e) {
			setLog(e,"changeStockLocation");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	
	@Test(groups = {"deep"})
	public void markAsConsumedAllStocksFromIndexTable(){
		
		try {
			showTableIndex();
			
			//if has no stocks - create 3 stocks
			if(!getPageManager().getStockPage().hasList()){
				getPageManager().getAdminPage().showBoxes();
				
				String newBox = buildUniqueName(LGConstants.BOX_WITH_STOCK_PREFIX);
				getPageManager().getBoxPage().addNewBox(newBox,"1");
				int numOfStocks = 3;
				getPageManager().getBoxPage().addStock("stock", numOfStocks);
				
			}
			getPageManager().getAdminPage().showStocks();
			boolean hasStocks = getPageManager().getStockPage().markAsConsumedAllStocks();
			Assert.assertFalse(hasStocks);
			
		} catch (Exception e) {
			setLog(e,"markAsConsumedAllStocksFromIndexTable");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test(groups = {"deep"})
	public void deleteAllStocksFromUsedStocksIndexTable(){
		
		try {
			showTableIndex();
			
			//if has no stocks - create 3 stocks
			if(!getPageManager().getStockPage().hasList()){
				getPageManager().getAdminPage().showBoxes();
				
				String newBox = buildUniqueName(LGConstants.BOX_WITH_STOCK_PREFIX);
				getPageManager().getBoxPage().addNewBox(newBox,"1");
				int numOfStocks = 3;
				getPageManager().getBoxPage().addStock(LGConstants.STOCK_PREFIX, numOfStocks);
				
			}
			getPageManager().getAdminPage().showStocks();
			getPageManager().getStockPage().markAsConsumedAllStocks();
			getPageManager().getStockPage().deleteUsedStocksFromView();
			getPageManager().getAdminPage().showStocks();
			Assert.assertFalse(getPageManager().getStockPage().hasList(), "Not all stocks were deleted");
		} catch (Exception e) {
			setLog(e,"deleteAllStocksFromUsedStocksIndexTable");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test(groups = {"basic sanity"})
	public void markAsConsumedStockFromIndexTable(){
		
		try {

			//create 2 stock and 2 boxes
			getPageManager().getAdminPage().showBoxes();
			addNewItem();
			
			getPageManager().getAdminPage().showBoxes();
			String stockName = addNewItem();
			
			getPageManager().getAdminPage().showStocks();
			getPageManager().getStockPage().markAsConsumedSelectedStock(stockName);
			boolean markAsConsumed = getPageManager().getStockPage().searchInConsumedStocks(stockName);
			Assert.assertTrue(markAsConsumed,"Simple search in consumed stocks did not find stock");
			
		} catch (Exception e) {
			setLog(e,"markAsConsumedStockFromIndexTable");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void checkCustomizeTableView(){
		
		try {
			
			showTableIndex();
			
			if(!getPageManager().getStockPage().hasList()){
				addNewItem();
			}
			showTableIndex();
			
			String msg = getPageManager().getStockPage().checkCustomizeTableView();
			Assert.assertTrue(msg.equals(""),"Not all selected columns are shown: " + msg);
		}  catch (Exception e) {
			setLog(e,"customizeTableView");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"knowBugs"})//LAB-1185
	public void simpleSearchInConsumedStocksView(){
		
		try {
			
			getPageManager().getAdminPage().showBoxes();
			addNewItem();
			
			getPageManager().getAdminPage().showBoxes();
			String stockName = addNewItem();
		
			getPageManager().getAdminPage().showStocks();
			getPageManager().getStockPage().markAsConsumedAllStocks();
			
			//search stock- should have 1 item
			boolean markAsConsumed = getPageManager().getStockPage().searchInConsumedStocks(stockName);
			Assert.assertTrue(markAsConsumed,"Did not find 1 match.");
				
		}  catch (Exception e) {
			setLog(e,"simpleSearchInConsumedStocksView");
			AssertJUnit.fail(e.getMessage());
		}
	}

	@Override
	protected String showModule() {
		return getPageManager().getAdminPage().showStocks();
	}

	@Override
	protected String addNewItem() throws InterruptedException {

		String newBox = buildUniqueName(LGConstants.BOX_WITH_STOCK_PREFIX);
		getPageManager().getBoxPage().addNewBox(newBox,"1");	
		String stockName = buildUniqueName(LGConstants.STOCK_PREFIX);
		getPageManager().getBoxPage().addStockFromBox(stockName);
		
		return stockName;
	}
}
