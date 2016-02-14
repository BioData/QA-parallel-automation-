package com.biodata.labguru.tests.inventory.purchasables;

import static org.testng.AssertJUnit.assertEquals;

import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.inventory.purchasables.PurchasableCollectionPage;


public class CellLinesTest extends PurchasableCollectionTest{

	
	@Override
	public String getCollectionId() {
		return LGConstants.CELL_LINES;
	}
	
	@Override
	protected String getPrefix() {
		return LGConstants.CELL_LINE_PREFIX;
	}

	@Override
	protected PurchasableCollectionPage getPage() {
		
		return getPageManager().getCellLinesPage();
	}
	
	@Override
	public void showMenu(){

		try {
			String pageTitle = showTableIndex();
			
			if(getPageManager().getCellLinesPage().hasList()){
			    // Check the title of the page when we already have some items
				AssertJUnit.assertEquals(getMessageSource().getMessage("celllines.title.has.celllines",null, Locale.US), pageTitle);
				getPage().deleteAllItemsFromTable();
			}
			
			pageTitle = showTableIndex();
			// Check the title of the page when we does'nt have items
			AssertJUnit.assertEquals(getMessageSource().getMessage("celllines.title.no.celllines",null, Locale.US), pageTitle);
			
		} catch (Exception e) {
			setLog(e,"canSelectCellLines");
			Assert.fail(e.getMessage());
		}

	}
	
	@Override
	public void addSimpleItem(){
		
		try {
			getPageManager().getAdminPage().showCellLines();
			
			String name = buildUniqueName(LGConstants.CELL_LINE_PREFIX);
			
			String msg = getPageManager().getCellLinesPage().addNewItem(name);
			
			// Check the title of the page
			assertEquals(getMessageSource().getMessage("collection.created.msg",new Object[]{getCollectionNameForMessage()}, Locale.US), msg.trim());
		} catch (Exception e) {
			setLog(e,"addNewCellLine");
			Assert.fail(e.getMessage());
		}
	}
	
	@Override
	protected String getCollectionNameForMessage(){
		return "Cell line";
	}
	
	
	@Override
	public String importBioCollection() throws InterruptedException {
		return getPageManager().getCellLinesPage().importCollection();		
	}

	@Override
	protected String showModule() {
		
		return getPageManager().getCellLinesPage().showCellLines();	
	}

	@Override
	protected String addNewItem() throws InterruptedException {
		
		String name = buildUniqueName(LGConstants.CELL_LINE_PREFIX);	
		getPageManager().getCellLinesPage().addNewItem(name);
		return name;
	}

	@Override
	public String getCollectionImportDetails() {
		
		return "6 " + LGConstants.CELL_LINES_MSG;
	}
	
	@Override
	public String getTemplateImportDetails() {
		return "1 " + LGConstants.CELL_LINES_MSG;
	}

}
