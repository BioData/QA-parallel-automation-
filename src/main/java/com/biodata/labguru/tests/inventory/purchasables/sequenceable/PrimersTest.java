package com.biodata.labguru.tests.inventory.purchasables.sequenceable;

import static org.testng.AssertJUnit.assertEquals;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.inventory.purchasables.PurchasableCollectionPage;


public class PrimersTest extends SequenceableCollectionTest{

	@Override
	public String getCollectionId() {
		return LGConstants.PRIMERS;
	}
	
	@Override
	protected String getPrefix() {
		return LGConstants.PRIMER_PREFIX;
	}

	@Override
	protected PurchasableCollectionPage getPage() {
		
		return getPageManager().getPrimersPage();
	}
	
	
	@Override
	public void showMenu(){

		try {
			String pageTitle = showTableIndex();
			
			if(getPage().hasList()){
			    // Check the title of the page when we already have some items
				AssertJUnit.assertEquals(getMessageSource().getMessage("primers.title.has.primers",null, Locale.US), pageTitle);
				getPage().deleteAllItems(getCollectionId());
			}
			
			pageTitle = showTableIndex();
			// Check the title of the page when we does'nt have items
			AssertJUnit.assertEquals(getMessageSource().getMessage("primers.title.no.primers",null, Locale.US), pageTitle);
			
		} catch (Exception e) {
			setLog(e,"canSelectPrimers");
			Assert.fail(e.getMessage());
		}

	}
	
	@Override
	public void addSimpleItem(){
		
		try {
			getPageManager().getAdminPage().showPrimers();
			
			String primerName = buildUniqueName(LGConstants.PRIMER_PREFIX);
			
			String msg = getPageManager().getPrimersPage().addNewItem(primerName);
			
			// Check the title of the page
			assertEquals(getMessageSource().getMessage("collection.created.msg",new Object[]{getCollectionNameForMessage()}, Locale.US), msg.trim());
		} catch (Exception e) {
			setLog(e,"addNewPrimer");
			Assert.fail(e.getMessage());
		}
	}
	
	
	@Test (groups = {"deep"})
	public void setThresholdFromStocksTab(){
		
		try {
			
			showTableIndex();
			addNewItem();
			
			TimeUnit.SECONDS.sleep(3);
			
			String stockName = buildUniqueName(LGConstants.STOCK_PREFIX);
			//add one stock and set the threshold to '2' - we expect a status message that alert for the low stocks
			getPage().addStockFromStocksTab(stockName, LGConstants.STOCK_TYPES_ARRAY[1]);
			String stockCountForThreshold = "2";
			String statusMsg = getPage().setThreshold(stockCountForThreshold);

			Assert.assertEquals("Threshold of " +  stockCountForThreshold + " stocks minimum is set. Currently there is only 1 available stock",statusMsg);
		
			statusMsg = getPage().setThreshold("1");
			Assert.assertEquals(statusMsg,"Threshold of 1 stocks minimum is set.");
			
			//TODO - check update status in dashboard
		
			
		} catch (Exception e) {
			setLog(e,"setThresholdFromStocksTab");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void checkThresholdStatusInStocksTab(){
		
		try {
			
			showTableIndex();
			addNewItem();
			
			TimeUnit.SECONDS.sleep(3);
			
			String stockName = buildUniqueName(LGConstants.STOCK_PREFIX);
			//add one stock and set the threshold to '2' - we expect a status message that alert for the low stocks
			getPage().addStockFromStocksTab(stockName, LGConstants.STOCK_TYPES_ARRAY[1]);
			String stockCountForThreshold = "2";
			String statusMsg = getPage().setThreshold(stockCountForThreshold);

			Assert.assertEquals("Threshold of " +  stockCountForThreshold + " stocks minimum is set. Currently there is only 1 available stock",statusMsg);
		
			//add one stock and set the threshold to '2' - we expect a status message that alert for the low stocks
			getPage().addStockFromStocksTab(stockName, LGConstants.STOCK_TYPES_ARRAY[1]);
			statusMsg = getPage().setThreshold(null);
			Assert.assertEquals(statusMsg,"Threshold of 2 stocks minimum is set.");
			
			
			//remove one stock when threshold set to '2' - we expect a status message that alert for the low stocks
			getPage().markAsConsumedStockInTable(stockName);
			statusMsg = getPage().setThreshold(null);
			Assert.assertEquals("Threshold of " +  stockCountForThreshold + " stocks minimum is set. Currently there is only 1 available stock",statusMsg);

		} catch (Exception e) {
			setLog(e,"checkThresholdStatusInStocksTab");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Override
	protected String getCollectionNameForMessage() {
		return "Primer";
	}
	
	@Override
	public String importBioCollection() throws InterruptedException {
		return getPageManager().getPrimersPage().importCollection();		
	}


	@Override
	protected String addNewItem() throws InterruptedException {
		
		String name = buildUniqueName(LGConstants.PRIMER_PREFIX);	
		getPageManager().getPrimersPage().addNewItem(name);
		return name;
		
	}
	@Override
	public String getTemplateImportDetails() {
		return "1 " + LGConstants.PRIMERS.toLowerCase();
	}
	
	@Override
	public String getCollectionImportDetails() {
		
		return "8 " + LGConstants.PRIMERS.toLowerCase();
	}
}
