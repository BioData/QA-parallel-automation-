package com.biodata.labguru.tests.inventory.purchasables;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.model.PurchasableCollectionItem;
import com.biodata.labguru.pages.inventory.purchasables.PurchasableCollectionPage;
import com.biodata.labguru.tests.TestOrderRandomizer;
import com.biodata.labguru.tests.inventory.AbstractCollectionsTest;

@Listeners(TestOrderRandomizer.class)
public abstract class PurchasableCollectionTest extends AbstractCollectionsTest{

	protected abstract String getPrefix();
	
	@Override
	public void checkShowPageOfCreatedItem(){
		
		try {
			showTableIndex();
			
			PurchasableCollectionPage page = (PurchasableCollectionPage) getPage();
			String name = buildUniqueName(getPrefix());
			String catalogNum = buildUniqueName("CAT");
			String owner = getPage().getAccountName();
			PurchasableCollectionItem newItem = new PurchasableCollectionItem(name,"MANUFACT",catalogNum,"1","100","₪","www.google.com",owner);
		
			PurchasableCollectionItem createdItem = page.checkCreatedItem(newItem);
			validateCommonFields(newItem, createdItem);

		}catch (Exception e) {
			setLog(e,"checkShowPageOfCreatedItem");
			Assert.fail(e.getMessage());
		}
	}
	
	protected void validateCommonFields(PurchasableCollectionItem newItem, PurchasableCollectionItem createdItem) {
		boolean purchasableEnabled = ((PurchasableCollectionPage )getPage()).isPurchasableEnabled(getCollectionId());
		
		Assert.assertEquals(newItem.name,createdItem.name,"The created item was not created correctly");
		Assert.assertEquals(newItem.owner,createdItem.owner,"The created item was not created correctly");
		if(purchasableEnabled){
		
			Assert.assertEquals(newItem.manufacturer,createdItem.manufacturer,"The created item was not created correctly");
			Assert.assertEquals(newItem.catalogNum,createdItem.catalogNum,"The created item was not created correctly");
			Assert.assertEquals(newItem.unit,createdItem.unit,"The created item was not created correctly");
			Assert.assertEquals(newItem.price + " " + newItem.currency,createdItem.price  ,"The created item was not created correctly");
			//Assert.assertEquals(newItem.webpage,createdItem.webpage,"The created item was not created correctly");
		}	
		Assert.assertEquals(newItem.description,createdItem.description,"The created item was not created correctly");
	}

	@Test (groups = {"basic sanity"})
	public void searchItemInShoppingList(){
		
		try {
			if(!((PurchasableCollectionPage )getPage()).isPurchasableEnabled(getCollectionId())){
				logger.info("Purchasable attributes are disabled - no test to run");
				return;
			}
			showTableIndex();
			
			String newItem = addNewItem();
			PurchasableCollectionPage page = (PurchasableCollectionPage) getPage();
			page.addToShoppingListFromShowPage();
			
			getPageManager().getAdminPage().showShoppingList();
			
			Assert.assertTrue(page.searchInShoppingList(newItem),"Item was not found..");
		}catch (Exception e) {
			setLog(e,"searchItemInShoppingList");
			Assert.fail(e.getMessage());
		}
		
	}

}
