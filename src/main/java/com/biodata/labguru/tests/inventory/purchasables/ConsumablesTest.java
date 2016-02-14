package com.biodata.labguru.tests.inventory.purchasables;

import static org.testng.AssertJUnit.assertEquals;

import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.inventory.purchasables.PurchasableCollectionPage;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public class ConsumablesTest extends PurchasableCollectionTest{
	
	@Override
	@Test (enabled = false)
	public void addStockFromMetaDataSection(){
		throw new UnsupportedOperationException("This action is not supported by this collection.");
	}
	
	@Override
	protected String getPrefix() {
		return LGConstants.CONSUMABLE_PREFIX;
	}

	@Override
	public void checkSingleCollection(){
		throw new UnsupportedOperationException("This action is not supported by this collection.");
	}
	
	@Override
	@Test (enabled = false)
	public void checkCollectionCustomizeBreadCrumbs(){
		
	}
	
	@Override
	@Test (enabled = false)
	public void addCustomFieldAndGenerateCollectionTemplate(){
		throw new UnsupportedOperationException("This action is not supported by this collection.");
	}
	
	@Override
	protected PurchasableCollectionPage getPage() {
		
		return getPageManager().getConsumablesPage();
	}
	
	@Override
	public void showMenu(){

		String pageTitle = showTableIndex();
			
		if(getPageManager().getConsumablesPage().hasList()){
	        // Check the title of the page when we already have some consumables
			AssertJUnit.assertEquals(getMessageSource().getMessage("consumable.title.has.consumables",null, Locale.US), pageTitle);
		}else{
			// Check the title of the page when we dont have consumables
			AssertJUnit.assertEquals(getMessageSource().getMessage("consumable.title.no.consumables",null, Locale.US), pageTitle);
		}

	}
	
	@Test (groups = {"basic sanity"})
	public void addFromProductDirectory(){
		
		try {
			showTableIndex();
			
			String msg = getPageManager().getConsumablesPage().addConsumableFromDirectory();
			String suffixMsg = getMessageSource().getMessage("consumable.title.add.from.directory.suffix",null, Locale.US);
			// Check the title of the page
			Assert.assertTrue(msg.endsWith(suffixMsg));
		} catch (Exception e) {
			setLog(e,"addFromProductDirectory");
			Assert.fail(e.getMessage());
		}
	}
	
	
	@Test (groups = {"deep"})
	public void addNonComercialMaterial(){
		
		try {
			getPageManager().getAdminPage().showConsumables();
			String materialName = buildUniqueName(LGConstants.CONSUMABLE_PREFIX);
			String msg = getPageManager().getConsumablesPage().addNonComercialMaterial(materialName);
			
	        // Check the title of the page
			assertEquals(getMessageSource().getMessage("collection.created.msg",new Object[]{"Material"}, Locale.US), msg.trim());
		} catch (Exception e) {
			setLog(e,"addFromProductDirectory");
			Assert.fail(e.getMessage());
		}
	}
	
	@Override
	public void addSimpleItem(){
		
		getPageManager().getAdminPage().showConsumables();
		String materialName = buildUniqueName(LGConstants.CONSUMABLE_PREFIX);
		String msg = getPageManager().getConsumablesPage().addNewItem(materialName);
		
        // Check the title of the page
		assertEquals(getMessageSource().getMessage("collection.created.msg",new Object[]{getCollectionNameForMessage()}, Locale.US), msg.trim());
	}
	
	@Test (groups = {"basic sanity"})
	public void addConsumableToShoppingList(){
		
		try {
			getPageManager().getAdminPage().showConsumables();
			String materialName = buildUniqueName(LGConstants.CONSUMABLE_PREFIX);
			getPageManager().getConsumablesPage().addNewItem(materialName);
			
			getPageManager().getAdminPage().showConsumables();
			
			String msg = getPageManager().getConsumablesPage().addConsumableToShoppingList();
			
			// Check the title of the page
			assertEquals(getMessageSource().getMessage("consumable.order.added.shoppinglist",null, Locale.US), msg.trim());
		} catch (Exception e) {
			setLog(e,"addConsumableToShoppingList");
			Assert.fail(e.getMessage());
		}
	}
	
	
	@Test (groups = {"basic sanity"})
	public void addToShoppingListFromShowPage(){
		
		try {
			getPageManager().getAdminPage().showConsumables();
			String materialName = buildUniqueName(LGConstants.CONSUMABLE_PREFIX);
			getPageManager().getConsumablesPage().addNewItem(materialName);
			
			String msg = getPageManager().getConsumablesPage().addToShoppingListFromShowPage();
			
			// Check the title of the page
			assertEquals(getMessageSource().getMessage("consumable.order.added.shoppinglist",null, Locale.US), msg.trim());
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		} 
	}
	
	@Override
	protected String getCollectionNameForMessage() {
		return "Material";
	}

	@Override
	public String importBioCollection() throws InterruptedException {
		return getPageManager().getConsumablesPage().importCollection();		
	}

	@Override
	protected String showModule() {
		
		return getPageManager().getAdminPage().showConsumables();
	}

	@Override
	protected String addNewItem() {
		
		String name = buildUniqueName(LGConstants.CONSUMABLE_PREFIX);
		getPageManager().getConsumablesPage().addNewItem(name);
		return name;
	}

	@Override
	public String getCollectionImportDetails() {
		return "6 " + LGConstants.MATERIALS;
	}

	@Override
	public String getCollectionId() {
		return LGConstants.CONSUMABLES_MENU;
	}

	@Override
	public String getTemplateImportDetails() {
		throw new UnsupportedOperationException("This action is not supported by this collection.");
	}
}
