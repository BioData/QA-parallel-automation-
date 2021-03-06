package com.biodata.labguru.tests.storage;

import java.util.List;
import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.model.EquipmentItem;

public class EquipmentTest extends AbstractStoragesTest{

	
	@Test (groups = {"deep"})
	public void addCustomFieldAndCreateNewEquipment(){
		try {
			showTableIndex();
			addNewItem();
			showTableIndex();
			List<String> fields = getPageManager().getEquipmentPage().addCustomFieldsToEquipment();
			
			showTableIndex();
	
			boolean allFieldsShown = getPageManager().getEquipmentPage().checkCreatedEquipmentWithCustomFields(fields);
			Assert.assertTrue(allFieldsShown, "Some of the added custom fields are missing.");
			showTableIndex();
			getPageManager().getEquipmentPage().deleteCustomFieldsFromCollection();
		
		
		} catch (Exception e) {
			setLog(e,"addCustomFieldAndCreateNewEquipment");
			Assert.fail(e.getMessage());
		}
	}
	
	
	@Override
	public void showMenu(){
		
		try {
			
			String pageTitle = showTableIndex();
	
			boolean hasItems = getPageManager().getEquipmentPage().hasList();
			if(hasItems){
			    // Check the title of the page
				AssertJUnit.assertEquals(getMessageSource().getMessage("equipment.title.has.equipment",null,Locale.US), pageTitle);
			}else{
				 // Check the title of the page
				AssertJUnit.assertEquals(getMessageSource().getMessage("equipment.title.no.equipment",null,Locale.US), pageTitle);
			}
			
		} catch (Exception e) {
			setLog(e, "canSelectEquipment");
		}
	}
	
	
	@Test (groups = {"deep"})
	public void deleteAllEquipmentsFromTableView(){
		
		try {
			
			showTableIndex();
			if(getPageManager().getEquipmentPage().hasList()){
				getPageManager().getEquipmentPage().deleteAllItemsFromTable();
				showTableIndex();
			}

			AssertJUnit.assertFalse("Not all items were deleted from index table.",getPageManager().getEquipmentPage().hasList());//check that there are no items in the list
		
		} catch (Exception e) {
			setLog(e,"deleteAllEquipmentsFromTableView");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void addNewEquipment(){
		
		try {
			showTableIndex();
			
			String name = buildUniqueName(LGConstants.EQUIPMENT_PREFIX);
			getPageManager().getEquipmentPage().addNewEquipment(name);
			
		}  catch (Exception e) {
			setLog(e, "addNewEquipment");
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void editEquipmentFromShowPage(){
		
		try {
			showTableIndex();
			addNewItem();
			String msg = getPageManager().getEquipmentPage().editItemFromShowPage();
			Assert.assertTrue(msg.endsWith("successfully updated."));
			//TODO - after all messages will be the same
			//Assert.assertEquals(msg,getMessageSource().getMessage("collection.updated.msg",new Object[]{"Equipment"},Locale.US));
			
		}  catch (Exception e) {
			setLog(e,"editEquipmentFromShowPage");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void deleteEquipmentFromShowPage(){
		
		try {
			//add 2 equipments so when delete one we still have noty message(if it is the last item ,there is no message)
			showTableIndex();
			addNewItem();
			showTableIndex();
			String name = buildUniqueName(LGConstants.EQUIPMENT_PREFIX);
			getPageManager().getEquipmentPage().addNewEquipment(name);
		
			String msg = getPageManager().getEquipmentPage().deleteFromShowPage();
			AssertJUnit.assertEquals(getMessageSource().getMessage("equipment.delete.msg", null,Locale.US), msg);
			
		}  catch (Exception e) {
			setLog(e, "deleteEquipmentFromShowPage");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void checkCustomizeTableView(){
		
		try {
			
			showTableIndex();
			
			if(!getPageManager().getEquipmentPage().hasList()){
				addNewItem();
			}
			showTableIndex();
			
			String msg = getPageManager().getEquipmentPage().checkCustomizeTableView();
			Assert.assertTrue(msg.equals(""),"Not all selected columns are shown: " + msg);
		}  catch (Exception e) {
			setLog(e,"customizeTableView");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void addNewEquipmentAndCheckCreation(){
		
		try {
			showTableIndex();
			
			String name = buildUniqueName(LGConstants.EQUIPMENT_PREFIX);
			String owner = getPageManager().getAdminPage().getAccountName();		
			EquipmentItem newItem = new EquipmentItem(name,owner);
		
			EquipmentItem createdItem = getPageManager().getEquipmentPage().checkCreatedItem(newItem);
			Assert.assertEquals(newItem.name,createdItem.name,"The created item was not created correctly");
			Assert.assertEquals(newItem.owner,createdItem.owner,"The created item was not created correctly");
			Assert.assertEquals(newItem.manufacturer,createdItem.manufacturer,"The created item was not created correctly");
			Assert.assertEquals(newItem.equipmentType,createdItem.equipmentType,"The created item was not created correctly");
			Assert.assertEquals(newItem.serialNumber,createdItem.serialNumber,"The created item was not created correctly");
			Assert.assertEquals(newItem.modelNumber,createdItem.modelNumber,"The created item was not created correctly");
			Assert.assertEquals(newItem.purchaseDate,createdItem.purchaseDate,"The created item was not created correctly");
			Assert.assertEquals(newItem.warrantyExpirationDate,createdItem.warrantyExpirationDate,"The created item was not created correctly");
			Assert.assertEquals(newItem.maintenanceDate,createdItem.maintenanceDate,"The created item was not created correctly");
			Assert.assertEquals(newItem.maintenanceInformation,createdItem.maintenanceInformation,"The created item was not created correctly");
			Assert.assertEquals(newItem.description,createdItem.description,"The created item was not created correctly");
			
			
		}  catch (Exception e) {
			setLog(e, "addNewEquipmentAndCheckCreation");
			Assert.fail(e.getMessage());
		}
	}

	@Override
	protected String showModule() {
		return getPageManager().getAdminPage().showEquipment();
	}

	@Override
	protected String addNewItem() throws InterruptedException {
		String name = buildUniqueName(LGConstants.EQUIPMENT_PREFIX);
		getPageManager().getEquipmentPage().addNewEquipment(name);
		return name;
	}
}
