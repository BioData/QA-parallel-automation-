package com.biodata.labguru.tests.inventory.rodents;

import static org.testng.AssertJUnit.assertEquals;

import java.util.Locale;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.tests.TestOrderRandomizer;
import com.biodata.labguru.tests.inventory.AbstractCollectionsTest;

@Listeners(TestOrderRandomizer.class)
public abstract class AbstractRodentsTest extends AbstractCollectionsTest{

	
	protected abstract String getPrefix();
	protected abstract String getCreationPrefix();
	
	@Override
	protected String getCollectionNameForMessage() {		
		return getCreationPrefix();
	}
	
	@BeforeClass(alwaysRun = true ,dependsOnMethods = "initialize")
	public void initDataForTest(){
		
		try {
			
			//add cage if not exist
			getPageManager().getAdminPage().showRodentCages();
			if(!getPageManager().getCagesPage().hasList())
				getPageManager().getCagesPage().addNewItem(buildUniqueName(LGConstants.CAGE_PREFIX));
			
			//add specimen if not exist
			getPageManager().getAdminPage().showRodentSpecimens();
			if(!getPageManager().getRodentSpecimensPage().hasList())
				getPageManager().getRodentSpecimensPage().addNewItem(buildUniqueName(LGConstants.RODENT_SPECIMEN_PREFIX));
			
			//we add one protocol to be able to start treatment from protocol
			getPageManager().getProtocolPage().showProtocols();
			getPageManager().getProtocolPage().addProtocolToAccount(buildUniqueName(LGConstants.PROTOCOL_PREFIX));
			
		} catch (Exception e) {
			setLog(e);
			Assert.fail(e.getMessage());
		}
	}
	
	@Override
	public String getCollectionId() {
		return LGConstants.RODENTS;
	}
	
	@Override
	@Test (enabled = false)
	public void checkCollectionCustomizeBreadCrumbs(){
		
	}
	
	@Override
	@Test (enabled = false)
	public void addStockFromMetaDataSection(){
		throw new UnsupportedOperationException("This action is not supported by this collection.");
	}

	@Override
	@Test (enabled = false)
	public void addStockFromStocksTab() {
		throw new UnsupportedOperationException("This action is not supported by this collection.");
	}
	
	@Override
	@Test (enabled = false)
	public void addStockLocatedInBox() {
		throw new UnsupportedOperationException("This action is not supported by this collection.");
	}
	
	
	@Override
	@Test (enabled = false)
	public void duplicateItem() {
		throw new UnsupportedOperationException("This action is not supported by this collection.");
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
	protected String addNewItem() throws InterruptedException {
		
		String name = buildUniqueName(getPrefix());
		getPage().addNewItem(name);
		return name;
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

}
