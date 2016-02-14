package com.biodata.labguru.tests.enotebook;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Locale;

import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.enotebook.AbstractNotebookPage;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public class ProtocolsTest extends AbstractEnotebookTest{
	
	
	@Test(groups = {"deep"})
	public void signAndLock(){
		
		try {
			showTableIndex();			
			addNewItem();
			
			String note = getPage().signAndLock();
			
			AssertJUnit.assertTrue(note.startsWith(getMessageSource().getMessage("signed.by.note.prefix",null, Locale.US)));
		} catch (Exception e) {
			setLog(e,"signAndLock");
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
			
			String versionsTitle = getPageManager().getAdminPage().showVersionHistory();
			currentDate = getCurrentDateFormatted(LGConstants.VERSIONS_HISTORY_CALENDAR_FORMAT);
			assertEquals(getMessageSource().getMessage("version.history.title",new Object[]{account,currentDate},Locale.US),
					versionsTitle.substring(0, versionsTitle.lastIndexOf(' ')));
			
		} catch (Exception e) {
			setLog(e);
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
	
	
	@Test (groups = {"basic sanity"})
	public void addTextDescriptionToProtocol() {
		
		try {
			showTableIndex();
			getPageManager().getProtocolPage().addProtocol("ProtocolWithDesc");
			
			String descToTest = "Test text in experiment description";
			String desc = getPageManager().getProtocolPage().addTextDescriptionToProtocol(descToTest);
			assertEquals(descToTest, desc);
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	
	@Test (groups = {"basic sanity"})
	public void addSamplesToProcedure() {
		
		try {
			showTableIndex();
			getPageManager().getProtocolPage().addProtocol("ProtocolWithSample");
			String sampleName = buildUniqueName(LGConstants.SAMPLE_PREFIX);
			String notCreated = getPageManager().getProtocolPage().addSamplesToProcedureInProtocol(sampleName);
			assertTrue("The following sample types were not created as should be: " + notCreated , notCreated.isEmpty());
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	
	@Test (groups = {"basic sanity"})
	public void addPlateToProcedure() {
		
		try {
			showTableIndex();
			String name = "ProtocolWithPlate";
			getPageManager().getProtocolPage().addProtocol(name);
			assertTrue(getPageManager().getProtocolPage().addPlateToProcedure());
			
			showTableIndex();
			assertEquals(name,getPageManager().getProtocolPage().openProtocol(name));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	@Test (groups = {"basic sanity"})
	public void addStepsToProcedure() {
		try {
			showTableIndex();
			getPageManager().getProtocolPage().addProtocol("ProtocolWithStep");
			int numOfSteps = 1;
			assertTrue(getPageManager().getProtocolPage().addStepToProcedure(numOfSteps));
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
			int numOfSteps = 1;
			getPageManager().getProtocolPage().addStepToProcedure(numOfSteps);
			
			assertTrue(getPageManager().getProtocolPage().deleteStepsOfProcedure());
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}

	@Test (groups = {"basic sanity"})
	public void addTextDescriptionToProcedure() {
		
		try {
			showTableIndex();
			getPageManager().getProtocolPage().addProtocol("ProtocolWithProcedureDesc");
			
			String descToTest = "Test description of procedure";
			String desc = getPageManager().getProtocolPage().addTextDescriptionToProcedure(descToTest);
			assertEquals(descToTest, desc);
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void addCompoundDescToProcedure() {//only when compound option is enabled

		try {
			showTableIndex();
			String name = "ProtocolWithCompound";
			getPageManager().getProtocolPage().addProtocol(name);

			assertTrue(getPageManager().getProtocolPage().addCompoundDescToExpProcedure());
			
			showTableIndex();
			assertEquals(name,getPageManager().getProtocolPage().openProtocol(name));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}	

	}
	

	@Test (groups = {"basic sanity"})
	public void addTableToProcedure() {
		
		try {
			showTableIndex();
			
			String name = "ProtocolWithTable";
			getPageManager().getProtocolPage().addProtocol(name);
			String dataToWrite = "test";
			assertTrue(getPageManager().getProtocolPage().addTableToProcedure(dataToWrite));
			
			showTableIndex();
			assertEquals(name,getPageManager().getProtocolPage().openProtocol(name));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	

	
	@Test (groups = {"basic sanity"})
	public void addNewProcedure() {
		try {
			showTableIndex();
			getPageManager().getProtocolPage().addProtocol("ProtocolWith2Procedures");
			
			String procedureName = "Procedure2";
			String newProcedure = getPageManager().getProtocolPage().addNewProcedureToProtocol(procedureName);
			assertEquals(procedureName, newProcedure);
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
			assertTrue("Adding reaction to protocol procedure description ",getPageManager().getProtocolPage().addReactionToExpProcedure());
			
			assertTrue("Some data are not calculated as should be.. ",getPageManager().getProtocolPage().validateReactionEditingAndCalculating(new String[]{"450","375","185"}));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	

	@Override
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
