package com.biodata.labguru.tests.inventory.purchasables;

import static org.testng.AssertJUnit.assertEquals;

import java.util.List;
import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.GenericHelper;
import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.inventory.purchasables.PurchasableCollectionPage;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public class ConsumablesTest extends PurchasableCollectionTest{
	
	@Override
	@Test (groups = {"deep"})//LAB-479
	public void checkShowPageOfCreatedItem(){
		super.checkShowPageOfCreatedItem();
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
	public void addCustomFieldAndGenerateCollectionTemplate(){
		try {
			showTableIndex();
			//add at least one material to be in the list
			addNewItem();
			showTableIndex();
			List<String> fields = getPageManager().getConsumablesPage().addCustomFieldToConsumables();
			
			showTableIndex();
			String msg = getPage().generateTemplateAndImport(fields);
			Assert.assertEquals(msg,getMessageSource().getMessage("biocollections.import.msg",new Object[]{getTemplateImportDetails()},Locale.US));
			
			//delete the items of the specific collection
			getPage().deleteItemFromIndexTable(LGConstants.IMPORTED_NAME);
			//delete the generated files
			GenericHelper.deleteGeneratedFiles();
			
			showTableIndex();
			getPageManager().getConsumablesPage().deleteCustomFieldsFromCollection();
		
		
		} catch (Exception e) {
			setLog(e,"addCustomFieldAndGenerateCollectionTemplate");
			Assert.fail(e.getMessage());
		}
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
	
	@Test (groups = {"deep"})
	public void moveToFromShowPage(){
		
		try {
			getPageManager().getAdminPage().showConsumables();
			String materialName = buildUniqueName(LGConstants.CONSUMABLE_PREFIX);
			getPageManager().getConsumablesPage().addNewItem(materialName);
			
			String msg = getPageManager().getConsumablesPage().moveTo(LGConstants.PRIMERS);
			String collectionName = LGConstants.PRIMERS.toLowerCase();
			// Check the title of the page
			assertEquals(getMessageSource().getMessage("consumable.moveto.msg",new Object[]{collectionName}, Locale.US), msg);
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
		return "1 " + LGConstants.MATERIALS.toLowerCase();
	}
}
