package com.biodata.labguru.tests.enotebook;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Locale;
import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.enotebook.AbstractNotebookPage;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public class ProtocolsV2Test extends AbstractEnotebookTest{
	

	@Test(groups = {"basic sanity"})
	public void copyProtocolFromProtocolDirectory(){
		
		try {
			getPageManager().getAdminPage().showProtocols();
			
			//return true if we succeeded to add specific protocol from directory
			String msg = getPageManager().getProtocolPage().copyProtocolFromDirectory();
			
			AssertJUnit.assertEquals(getMessageSource().getMessage("protocol.external.copy.success",null, Locale.US),msg);
			
		} catch (Exception e) {
			setLog(e,"copyProtocolFromProtocolDirectory");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Override
	@Test (groups = {"basic sanity"})
	public void duplicateItem(){
		
		try {
	
			showTableIndex();			
			String protocol = addNewItem();
			String duplicateItemName = getPageManager().getProtocolPage().duplicate();
			
			// Check the title of the page
			assertTrue("The duplication did not finished as expected - " + duplicateItemName,duplicateItemName.startsWith(protocol) && duplicateItemName.contains("(duplicate)"));		
		
		} catch (Exception e) {
			setLog(e,"duplicateExperiment");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void signProtocol() {
		
		try {
			
			showTableIndex();
			
			String name = buildUniqueName("Signed Protocol");
			getPageManager().getProtocolPage().addProtocol(name);
			
			boolean signSucceeded = getPageManager().getProtocolPage().sign();
			assertTrue("The sign action did not succeeded.",signSucceeded);
			
			assertTrue("Some menu actions are not as they should be.",getPageManager().getProtocolPage().checkAllowedActionsOnSignedProtocol());
			
			
		} catch (Exception e) {
			setLog(e,"signProtocol");
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	@Test (groups = {"basic sanity"})
	public void revertSignature() {
		//Users that are not admin or signed the exp can't revert the signature.
		try {
			showTableIndex();
			
			String name = buildUniqueName(LGConstants.PROTOCOL_PREFIX);
			getPageManager().getProtocolPage().addProtocol(name);
			
			assertTrue("The sign action did not succeeded.",getPageManager().getProtocolPage().sign());
			
			boolean revertSucceeded = getPageManager().getProtocolPage().revertSignature();
			assertTrue("The revert signature action did not succeeded.",revertSucceeded);
			
		} catch (Exception e) {
			setLog(e,"revertSignature");
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	@Test (groups = {"deep"})
	public void archiveProtocol() {
		
		try {
			showTableIndex();
			
			String name = buildUniqueName(LGConstants.PROTOCOL_PREFIX);
			getPageManager().getProtocolPage().addProtocol(name);
			
			String msg = getPageManager().getProtocolPage().archiveProtocol();
			assertEquals(msg,getMessageSource().getMessage("protocol.arcive.success.msg", null,Locale.US));
			
		} catch (Exception e) {
			setLog(e,"archiveProtocol");
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	@Test (groups = {"basic sanity"})
	public void deleteProtocol() {
		
		try {
			showTableIndex();
			getPageManager().getProtocolPage().addProtocol("ProtocolToDelete");	
			String msg = getPageManager().getProtocolPage().deleteProtocol();
			assertTrue("The delete action did not succeeded.",msg.equals("Protocol was successfully deleted"));
		} catch (Exception e) {
			setLog(e,"deleteProtocol");
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	@Test(groups = {"TODO"})//TODO - not working yet
	public void shareProtocol(){
		
		try {
			showTableIndex();			
			String protocol = addNewItem();
			
			//share protocol
			String msg = getPageManager().getProtocolPage().shareProtocol(true);
			
			AssertJUnit.assertEquals(getMessageSource().getMessage("protocol.share.success",null, Locale.US),msg);
			boolean found = getPageManager().getProtocolPage().checkProtocolInProtocolsDirectory(protocol);
			assertTrue("Protocol was not found in Directory and should be.", found);
			
			//unshare protocol
			showTableIndex();	
			getPageManager().getProtocolPage().invokeSearchItem(protocol);
			getPageManager().getProtocolPage().selectProtocol();
			msg = getPageManager().getProtocolPage().shareProtocol(false);
			
			AssertJUnit.assertEquals(getMessageSource().getMessage("protocol.unshare.success",null, Locale.US),msg);
			
			found = getPageManager().getProtocolPage().checkProtocolInProtocolsDirectory(protocol);
			assertFalse("Protocol was found in Directory and should'nt be.", found);
			
		} catch (Exception e) {
			setLog(e,"shareProtocol");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void createAndUpdateProtocol() {
		try {
			
			showTableIndex();
			addNewItem();
			
	        String currentDate = getCurrentDateFormatted(LGConstants.CALENDAR_FORMAT);
			
			String account = getPageManager().getAdminPage().getAccountName();
			String bottomMsg = getPageManager().getProtocolPage().updateContent();
			
			assertEquals(getMessageSource().getMessage("item.update.versions.footer.msg",new Object[]{currentDate,account},Locale.US),bottomMsg);
			
			String versionsTitle = getPageManager().getProtocolPage().clickOnVersionHistory();
			currentDate = getCurrentDateFormatted(LGConstants.VERSIONS_HISTORY_CALENDAR_FORMAT);
			assertEquals(getMessageSource().getMessage("version.history.title",new Object[]{account,currentDate},Locale.US),
					versionsTitle.substring(0, versionsTitle.lastIndexOf(' ')));
			
		} catch (Exception e) {
			setLog(e,"createAndUpdateProtocol");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	
	@Override
	public void showMenu(){

		String pageTitle = showTableIndex();
		
		if(getPageManager().getProtocolPage().hasList()){
	        // Check the title of the page when we already have some consumables
			AssertJUnit.assertEquals(getMessageSource().getMessage("protocol.title.has.protocols",null, Locale.US), pageTitle);
		}else{
			// Check the title of the page
			assertEquals(getMessageSource().getMessage("protocol.title.no.protocols",null, Locale.US), pageTitle);
		}
	}
	
	@Test(groups = {"basic sanity"})
	public void addProtocolToAccount(){
		
		try {
			showTableIndex();		
			
			String newProtocol = buildUniqueName(LGConstants.PROTOCOL_PREFIX);
			
			assertEquals(newProtocol,getPageManager().getProtocolPage().addProtocolToAccount(newProtocol));
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"basic sanity"}) 
	public void goToProtocolsListFromProtocol() {
		try {
			
			showTableIndex();
			addNewItem();
			String title = getPageManager().getProtocolPage().goToProtocols();
			assertTrue(title.startsWith(getMessageSource().getMessage("protocol.title.has.protocols", null,Locale.US)));
			
		} catch (Exception e) {
			setLog(e,"goToProtocolsListFromProtocol");
 			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test(groups = {"basic sanity"})
	public void showProtocolsIndexTable(){
		
		try {
			showTableIndex();
			
			getPageManager().getProtocolPage().addProtocolToAccount(buildUniqueName(LGConstants.PROTOCOL_PREFIX));
			getPageManager().getAdminPage().showProtocols();
			
			String pageTitle = getPageManager().getProtocolPage().showProtocolsIndexTable();
			
			assertEquals(getMessageSource().getMessage("protocol.title.has.protocols",null, Locale.US), pageTitle);
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test(groups = {"basic sanity"})
	public void searchProtocolInDirectory(){
		
		try {
			showTableIndex();
			
			String nameToSearch = "My test";
			
			getPageManager().getProtocolPage().addProtocolToAccount(nameToSearch);
			
			getPageManager().getAdminPage().showProtocols();
			
			//return true if the search name found
			assertTrue(getPageManager().getProtocolPage().searchProtocolInDirectory(nameToSearch));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test(groups = {"basic sanity"})
	public void addProtocolFromDirectory(){
		
		try {
			showTableIndex();
			
			getPageManager().getProtocolPage().addProtocolToAccount(buildUniqueName(LGConstants.PROTOCOL_PREFIX));
			
			getPageManager().getAdminPage().showProtocols();
			
			//return true if we succeeded to add specific protocol from directory
			assertTrue(getPageManager().getProtocolPage().addProtocolFromDirectory());
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test(groups = {"basic sanity"})
	public void startExperimentFromProtocol(){
		
		try {
			showTableIndex();
			
			getPageManager().getProtocolPage().addProtocolToAccount(buildUniqueName(LGConstants.PROTOCOL_PREFIX));
			
			getPageManager().getAdminPage().showProtocols();
			
			getPageManager().getProtocolPage().selectProtocol();
			
			String newExp = buildUniqueName("expFromProtocol_");
			
			String pageTitle = getPageManager().getProtocolPage().startExperimentFromProtocol(newExp);
			//return true if we succeeded to add specific protocol from directory
			assertEquals(newExp,pageTitle);
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	


	
	
	@Test (groups = {"knownBugs"})//LAB-1086
	public void addSamplesToProcedure() {
		
		try {
			showTableIndex();
			getPageManager().getProtocolPage().addProtocol("ProtocolWithSample");
			String notCreated = getPageManager().getProtocolPage().addSamplesToSection(PROCEDURE_SECTION_INDEX);
			assertTrue("The following sample types were not created as should be: " + notCreated , notCreated.isEmpty());
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	@Test (groups = {"knownBugs"})//LAB-1086
	public void addSampleWithGenericCollection() {
		
		try {
			String collectionName = buildUniqueName(LGConstants.GENERIC_COLLECTION_PREFIX);
			getPageManager().getAccountSettingPage().addGenericCollection(collectionName);
			showTableIndex();
			getPageManager().getProtocolPage().addProtocol("ProtocolWithSampleWithGenericCollection");
			String sampleName = buildUniqueName(LGConstants.SAMPLE_PREFIX);
			assertTrue(sampleName +" was not shown as expected after refresh.",getPageManager().getProtocolPage().addSampleWithGenericCollection(PROCEDURE_SECTION_INDEX,collectionName,sampleName));
			
			getPageManager().getGenericCollectionPage().deleteGenericCollection(collectionName);
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	@Test (groups = {"basic sanity"})
	public void addPlateToProcedure(){
		
		try {
			
			showTableIndex();
			String name = "ProtocolWithPlate2X3InProcedure";
			getPageManager().getProtocolPage().addProtocol(name);
			assertTrue(getPageManager().getExperimentPage().addPlate2X3ToSection(PROCEDURE_SECTION_INDEX));
			
			showTableIndex();
			assertEquals(name,getPageManager().getProtocolPage().openProtocol(name));
			
		}  catch (Exception e) {
			setLog(e,"addPlateToProcedure");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	
	@Test (groups = {"basic sanity"})
	public void addStepsToProcedure() {
		
		try {
			showTableIndex();
			getPageManager().getProtocolPage().addProtocol("ProtocolWithStep");
			assertTrue(getPageManager().getProtocolPage().addStepToSection(PROCEDURE_SECTION_INDEX));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}
	
		@Test (groups = {"basic sanity"})
	public void deleteStepsOfProcedure() {
		try {
			showTableIndex();
			getPageManager().getProtocolPage().addProtocol("ProtocolWithStepToDelete");
			assertTrue(getPageManager().getProtocolPage().addStepToSection(PROCEDURE_SECTION_INDEX));
			
			assertTrue(getPageManager().getProtocolPage().deleteStepsOfSection(PROCEDURE_SECTION_INDEX));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}
		
	


	
	@Test (groups = {"basic sanity"})
	public void addTextToDescription() {
		try {
			showTableIndex();
			getPageManager().getProtocolPage().addProtocol("ProtocolWithProcedureDesc");
			
			String descName = "Description";
			String newDesc = getPageManager().getProtocolPage().addTextToSection(PROCEDURE_SECTION_INDEX,descName);
			assertEquals(descName, newDesc);
		} catch (Exception e) {
			setLog(e);
 			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void addTableToDescription() {
		
		try {
			
			showTableIndex();
			
			String name = "ProtocolWithTable";
			getPageManager().getProtocolPage().addProtocol(name);
			String dataToWrite = "test";
			assertTrue(getPageManager().getProtocolPage().addTableToSection(dataToWrite,DESCRIPTION_SECTION_INDEX));
			
			showTableIndex();
			assertEquals(name,getPageManager().getProtocolPage().openProtocol(name));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}

	
	@Test (groups = {"basic sanity"})
	public void addCompoundToSection() {//only when compound option is enabled
		
		try {
			showTableIndex();
			String name = "ProtocolWithCompound";
			getPageManager().getProtocolPage().addProtocol(name);
	
			boolean created = getPageManager().getProtocolPage().addCompoundToSection(PROCEDURE_SECTION_INDEX);
			assertTrue("The compound was not created as should be",created);
			
			showTableIndex();
			assertEquals(name,getPageManager().getProtocolPage().openProtocol(name));

			
		} catch (Exception e) {
			setLog(e,"addCompoundToSection");
			AssertJUnit.fail(e.getMessage());
		}
	}
	

	@Test (groups = {"basic sanity"}) 
	public void addNewSectionToDescription() {
		try {
			showTableIndex();
			getPageManager().getProtocolPage().addProtocol("ProtocolWith2Sections");
			
			String descName = "Description2";
			String newDesc = getPageManager().getProtocolPage().addNewSection(DESCRIPTION_SECTION_INDEX,descName);
			assertEquals(descName, newDesc);
			
		} catch (Exception e) {
			setLog(e);
 			AssertJUnit.fail(e.getMessage());
		}
	}
	
	

	@Test (groups = {"basic sanity"})
	public void importReactionToExpProcedure() {
		
		try {
			showTableIndex();
			getPageManager().getProtocolPage().addProtocol("protocolWithReaction");
			boolean created = getPageManager().getProtocolPage().addReactionToSection(PROCEDURE_SECTION_INDEX);
			assertTrue("Adding reaction to experiment results failed",created);
			
			//assertTrue("Some data are not calculated as should be.. ",getPageManager().getProtocolPage().validateReactionEditingAndCalculating(new String[]{"450","375","185"}));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}

	

	@Override
	@Test(enabled = false)
	public void addTask() {
		//no implementation for this for protocol - keep empty
	}
	
	@Override
	protected String showModule() {
		return getPageManager().getAdminPage().showProtocols();
	}
	

	@Override
	protected String addNewItem() throws InterruptedException {
		
		String newProtocol = buildUniqueName("protocol_");	
		getPageManager().getProtocolPage().addProtocolToAccount(newProtocol);
		return newProtocol;
	}


	@Override
	protected AbstractNotebookPage getPage() {
		return getPageManager().getProtocolPage();
	}

}
