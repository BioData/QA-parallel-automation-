package com.biodata.labguru.tests.inventory.purchasables.botany;

import static org.testng.AssertJUnit.assertEquals;

import java.util.Locale;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.model.BotanyItem;
import com.biodata.labguru.pages.inventory.purchasables.botany.BotanyPage;
import com.biodata.labguru.tests.TestOrderRandomizer;
import com.biodata.labguru.tests.inventory.purchasables.PurchasableCollectionTest;

@Listeners(TestOrderRandomizer.class)
public abstract class AbstractBotanyTest extends PurchasableCollectionTest{
		
	protected abstract String getCreationPrefix();
	
	@Override
	protected String getCollectionNameForMessage() {
		return getCreationPrefix();
	}
	
	@Override
	public String getCollectionId() {
		return LGConstants.BOTANY;
	}
	
	@Override
	public void checkShowPageOfCreatedItem(){
		
		try {
			showTableIndex();
			
			BotanyPage page = (BotanyPage) getPage();
			String name = buildUniqueName(getPrefix());
			String catalogNum = buildUniqueName("CAT");
			String owner = getPage().getAccountName();
			BotanyItem newItem = new BotanyItem(name,"MANUFACT",catalogNum,"1","100","₪","www.google.com",owner);
		
			BotanyItem createdItem = (BotanyItem) page.checkCreatedItem(newItem);
			validateCommonFields(newItem, createdItem);
			
			Assert.assertEquals(newItem.genotype,createdItem.genotype,"The created item was not created correctly");
			Assert.assertEquals(newItem.phenotype,createdItem.phenotype,"The created item was not created correctly");
			Assert.assertEquals(newItem.generation,createdItem.generation,"The created item was not created correctly");

		}catch (Exception e) {
			setLog(e,"checkShowPageOfCreatedItem");
			Assert.fail(e.getMessage());
		}
	}



	
	@Override
	public void addSimpleItem(){
		
		try {
			showTableIndex();
			
			String name = buildUniqueName(getPrefix());
			
			String msg = getPage().addNewItem(name);
			
			// Check the title of the page
			assertEquals("Wrong upper message",getMessageSource().getMessage("collection.created.msg",
					new Object[]{getCreationPrefix()}, Locale.US), msg.trim());
		} catch (Exception e) {
			setLog(e,"addNew in " + getPage().toString());
			Assert.fail(e.getMessage());
		}
	}
	
	@Override
	//@Test (groups = {"deep"})
	public void addCustomFieldAndGenerateCollectionTemplate(){
		//TODO
//		try {
//				getPage().showCollectionsAndSettings();
//				
//				String collectionName = getCollectionId();
//				
//				List<String> fields = getPage().addCustomFieldToCollection(collectionName);
//				
//				getPage().showCollection(collectionName);
//				String msg = getPage().generateTemplateAndImport(fields);
//				
//				//delete all create custom fields
//				getPage().deleteItem(LGConstants.IMPORTED_NAME);
//				//delete the generated files
//				GenericHelper.deleteGeneratedFiles();
//				
//				getPage().showCollectionsAndSettings();
//				getPage().deleteCustomFieldsFromCollection(collectionName);
//				
//				Assert.assertEquals(msg,getMessageSource().getMessage("biocollections.import.msg",
//						new Object[]{getTemplateImportDetails() },Locale.US));
//			
//		} catch (Exception e) {
//			setLog(e,"addCustomFieldAndGenerateCollectionTemplate");
//			Assert.fail(e.getMessage());
//		}
	}
	
	@Override
	@Test (enabled = false)
	public void checkCollectionCustomizeBreadCrumbs(){
		
	}
	
	
	@Override
	protected String addNewItem() throws InterruptedException {
		
		String name = buildUniqueName(getPrefix());
		getPage().addNewItem(name);
		return name;
	}

}
