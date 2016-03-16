package com.biodata.labguru.tests.inventory.purchasables.sequenceable;

import static org.testng.AssertJUnit.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.GenericHelper;
import com.biodata.labguru.LGConstants;
import com.biodata.labguru.LGConstants.SeqTypes;
import com.biodata.labguru.pages.inventory.purchasables.PurchasableCollectionPage;
import com.biodata.labguru.pages.inventory.purchasables.sequenceable.SequenceableCollectionPage;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public class GenericCollectionTest extends SequenceableCollectionTest{

	
	@AfterMethod(alwaysRun = true)
	public void deleteGenericCollections(){

		try {
			getPageManager().getGenericCollectionPage().deleteAllGenericCollection();
		} catch (Exception e) {
			setLog(e,"deleteGenericCollections");
		}
	}
	
	@Override
	@Test (enabled = false)
	public void checkCollectionCustomizeBreadCrumbs(){
		
	}
	
	@Override
	protected String getPrefix() {
		return LGConstants.GENERIC_COLLECTION_PREFIX;
	}
	
	@Override
	@Test (groups = {"deep"})
	public void addNewItemWithSequence(){
		
		
		try {
			String collectionName = LGConstants.GENERIC_COLLECTION_NAME;
			getPageManager().getAccountSettingPage().addGenericCollection(collectionName);
			
			((SequenceableCollectionPage) getPage()).checkCustomField(LGConstants.SEQUENCE_FIELD,getCollectionId());

			getPageManager().getAdminPage().showCollection(collectionName);
			String name = buildUniqueName(getPrefix()) + " with sequence";
			
			String msg = ((SequenceableCollectionPage) getPage()).addNewItemWithSequence(name);
			
			// Check the title of the page
			 assertEquals(getMessageSource().getMessage("collection.created.msg",new Object[]{collectionName}, Locale.US), msg.trim());
			
		} catch (Exception e) {
			setLog(e,"addNewItemWithSequence");
			Assert.fail(e.getMessage());
		}	
	}
	
	@Override
	@Test (groups = {"deep"})
	public void addNewSequenceFromSequencesTab(){
		
		try {
			String collectionName = LGConstants.GENERIC_COLLECTION_NAME;
			getPageManager().getAccountSettingPage().addGenericCollection(collectionName);
			
			((SequenceableCollectionPage) getPage()).checkCustomField(LGConstants.SEQUENCE_FIELD,getCollectionId());

			addNewItem();
			String seqToAdd = buildUniqueName(LGConstants.SEQUENCE_PREFIX);
			String addedSequence = ((SequenceableCollectionPage) getPage()).addNewSequenceFromSequencesTab(seqToAdd);
			
			// Check the title of the page
			 assertEquals(addedSequence, seqToAdd);
		
		}catch (Exception e) {
			setLog(e,"addNewSequenceFromSequencesTab");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void addDifferentTypesOfSequence(){
		
		try {
			String collectionName = LGConstants.GENERIC_COLLECTION_NAME;
			getPageManager().getAccountSettingPage().addGenericCollection(collectionName);
			
			((SequenceableCollectionPage) getPage()).checkCustomField(LGConstants.SEQUENCE_FIELD,getCollectionId());

			addSequenceByType(collectionName,SeqTypes.DNA_TYPE);
			addSequenceByType(collectionName,SeqTypes.RNA_TYPE);
			addSequenceByType(collectionName,SeqTypes.PROTEIN_TYPE);
			 
		}catch (Exception e) {
			setLog(e,"addDifferentTypesOfSequence");
			Assert.fail(e.getMessage());
		}
	}
	
	@Override
	public void showMenu(){

		try {
			String collectionName = buildUniqueName(LGConstants.GENERIC_COLLECTION_PREFIX);
			getPageManager().getAccountSettingPage().addGenericCollection(collectionName);
			String pageTitle = getPageManager().getAdminPage().showCollection(collectionName);
				
			if(getPage().hasList()){
			    // Check the title of the page when we already have items
				AssertJUnit.assertEquals(collectionName, pageTitle);
			}else{
				String formatedName = StringUtils.capitalize(collectionName).replace('_', ' ') + "s";
				// Check the title of the page when we dont have items
				AssertJUnit.assertEquals(LGConstants.GENERIC_COLLECTION_PAGE_TITLE_PREFIX + " " + formatedName, pageTitle);
			}

		}catch (Exception e) {
			setLog(e,"canSelectGenericCollection");
			Assert.fail(e.getMessage());
		}

	}
	
	@Override
	public void checkCustomizeTableView(){
		
		try {
			
			showTableIndex();
			
			if(!getPage().hasList()){
				addNewItem();
				getPageManager().getGenericCollectionPage().showCollectionFromBreadCrumbs();
			}
			
			String msg = getPage().checkCustomizeTableView();
			Assert.assertTrue(msg.equals(""),"Not all selected columns are shown: " + msg);
		}  catch (Exception e) {
			setLog(e,"customizeTableView");
			Assert.fail(e.getMessage());
		}
	}
	

	@Override
	public void addSimpleItem() {
		
		try {
			String collectionName = LGConstants.GENERIC_COLLECTION_NAME;
			getPageManager().getAccountSettingPage().addGenericCollection(collectionName);

			String msg = getPage().addNewItem(collectionName);
			
			// Check the title of the page
			assertEquals("Wrong upper message",getMessageSource().getMessage("collection.created.msg",new Object[]{collectionName}, Locale.US), msg.trim());
			
		} catch (Exception e) {
			setLog(e,"addSimpleItem");
			Assert.fail(e.getMessage());
		}	
	}
	
	@Override
	public void addItemSaveAndNew(){
		
		try {
			String collectionName = LGConstants.GENERIC_COLLECTION_NAME;
			getPageManager().getAccountSettingPage().addGenericCollection(collectionName);

			String msg = getPage().addItemSaveAndNew(collectionName);
		
			
			// Check the title of the page
			assertEquals(getMessageSource().getMessage("collection.created.msg",new Object[]{collectionName}, Locale.US), msg.trim());
		} catch (Exception e) {
			setLog(e,"addNewItemSaveAndNew");
			Assert.fail(e.getMessage());
		}
	}
	
	@Override
	public void editCollectionItemFromItemShowPage(){
		
		try {
			
			String collectionName = LGConstants.GENERIC_COLLECTION_NAME;
			getPageManager().getAccountSettingPage().addGenericCollection(collectionName);
			addNewItem();
			String msg = getPage().editItemFromShowPage();
			Assert.assertTrue(msg.endsWith("successfully updated."));		
			
		}  catch (Exception e) {
			setLog(e,"deleteCollectionItemFromItemShowPage");
			Assert.fail(e.getMessage());
		}
	}
	
	
	@Override
	@Test (groups = {"deep"})
	public void addCustomFieldAndGenerateCollectionTemplate(){
		
		try {

			String collectionName = LGConstants.GENERIC_COLLECTION_NAME;
			getPageManager().getAccountSettingPage().addGenericCollection(collectionName);
	
			List<String> fields = getPage().addCustomFields();

			getPageManager().getAdminPage().showCollection(collectionName);
			String msg = getPage().generateTemplateAndImport(fields);
			String formatedName = collectionName.replace('_', ' ') + "s";
			Assert.assertEquals(msg,getMessageSource().getMessage("biocollections.import.msg",
					new Object[]{getTemplateImportDetails() + formatedName},Locale.US));
			
			//delete the items of the specific collection
			getPage().deleteItemFromIndexTable(LGConstants.IMPORTED_NAME);
			//delete the generated files
			GenericHelper.deleteGeneratedFiles();
			
		} catch (Exception e) {
			setLog(e,"addCustomFieldAndGenerateTemplate");
			Assert.fail(e.getMessage());
		}
		
	}
	
	@Test (groups = {"deep"})
	public void addCustomField(){
		
		try {

			String collectionName = LGConstants.GENERIC_COLLECTION_NAME;
			getPageManager().getAccountSettingPage().addGenericCollection(collectionName);
			
			List<String> fields = new ArrayList<String>();
			for (int i = 0; i < LGConstants.CUSTOM_FIELD_TYPES_ARRAY.length; i++) {
				String type = LGConstants.CUSTOM_FIELD_TYPES_ARRAY[i];
				String fieldName = LGConstants.CUSTOM_FIELD_PREFIX + "_" + type;
				fields.add(fieldName);
				getPage().addCustomField(fieldName,type);
			}
			
			getPageManager().getAdminPage().showCollection(collectionName);
			getPage().addNewItem("my generic item");
			Assert.assertTrue(getPage().checkIfAllCustomFieldsAppear(fields), "Not all custom fields appear in the show page");
			
		} catch (Exception e) {
			setLog(e,"addCustomField");
			Assert.fail(e.getMessage());
		}
		
	}
	
	@Test  (groups = {"deep"})
	public void addNewGenericCollection(){

		try {
			String collectionName = LGConstants.GENERIC_COLLECTION_NAME;
			String pageTitle =getPageManager().getAccountSettingPage().addGenericCollection(collectionName);
			String formatedName = collectionName.replace('_', ' ');
			AssertJUnit.assertEquals(getMessageSource().getMessage("biocollections.customise.fields.title", new Object[]{formatedName},Locale.US), pageTitle);			
			
		}catch (Exception e) {
			setLog(e,"addNewGenericCollection");
			Assert.fail(e.getMessage());
		}

	}
	
	@Override
	protected PurchasableCollectionPage getPage() {
		return getPageManager().getGenericCollectionPage();
	}
	
	@Override
	@Test (groups="import")
	public void importCollection() {
		
		try {
			
			String collectionName = showTableIndex();
			String msg = importBioCollection();
			String formatedName = collectionName.replace('_', ' ') + "s";
			Assert.assertEquals(msg,getMessageSource().getMessage("biocollections.import.msg",
					new Object[]{getCollectionImportDetails() + formatedName},Locale.US));

			
		}catch (Exception e) {
			setLog(e,"importCollection");
			Assert.fail(e.getMessage());
		}
		
//		throw new UnsupportedOperationException("This test is not supported by this collection."
//				+ "Import is tested after generating the correct template.");
	}
	
	@Override
	public void deleteCollectionItemFromIndexTable(){
		
		try {
			
			showTableIndex();
			String item = addNewItem();
			showTableIndex();
			boolean deleted = getPage().deleteItemFromIndexTable(item);
			Assert.assertTrue(deleted, "Item was not deleted");
			
			
		}  catch (Exception e) {
			setLog(e,"deleteCollectionItemFromIndexTable");
			Assert.fail(e.getMessage());
		}
	}
	
	@Override
	public void deleteCollectionItemFromItemShowPage(){
		
		try {
			
			showTableIndex();
			String item = addNewItem();
			String msg = getPage().deleteItemFromShowPage(item);
			
			Assert.assertEquals(msg,"");//no message for removed item
			
		}  catch (Exception e) {
			setLog(e,"deleteCollectionItemFromItemShowPage");
			Assert.fail(e.getMessage());
		}
	}

	@Override
	public String importBioCollection() throws InterruptedException {
		return getPageManager().getGenericCollectionPage().importCollection();	
	}

	@Override
	public String getCollectionImportDetails() {
		//return "100 "; //template1
		return "5 "; //template2
	}

	@Override
	public String getCollectionId() {
		return LGConstants.GENERIC_COLLECTION_PREFIX;
				
	}

	@Override
	protected String showTableIndex() {
		
		try {
			String collectionName = LGConstants.GENERIC_COLLECTION_NAME;
			getPageManager().getAccountSettingPage().addGenericCollection(collectionName);
			getPageManager().getAdminPage().showCollection(collectionName);
			closeIridizePopups();
			return collectionName;
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		
	}

	@Override
	protected String addNewItem() throws InterruptedException {
		
		String name = LGConstants.GENERIC_COLLECTION_NAME;
		getPageManager().getGenericCollectionPage().addNewItem(name);
		return name;
	}
	
	@Override
	public String getTemplateImportDetails() {
		return "1 ";
	}

	@Override
	protected String getCollectionNameForMessage() {
		return "";
	}

	/**
	 * Create new item and add sequnce from the given type.Check that the given type is shown in the sequence table
	 * @param collectionName the generic collection name
	 * @param type of sequence to create
	 * @throws InterruptedException
	 */
	private void addSequenceByType(String collectionName,SeqTypes type) throws InterruptedException {
		
		getPageManager().getAdminPage().showCollection(collectionName);
		
		String name = buildUniqueName(getPrefix()) + " with " +SeqTypes.getTypeName(type) +  " sequence";
		
		//create sequence acording to givem type
		String createdType = getPageManager().getGenericCollectionPage().addNewItemWithSequence(name,type);	
		assertEquals(createdType,SeqTypes.getTypeName(type));
	}

}
