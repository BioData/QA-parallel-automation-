package com.biodata.labguru.tests.enotebook;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.enotebook.AbstractNotebookPage;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public class ExperimentTest extends AbstractEnotebookTest {
	
	
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
	
	@Test(groups = {"deep"})
	public void signExperimentWithAttachmentNotEditable(){
		
		try {
			//add new experiment with attachment
			showTableIndex();			
			addNewItem();
			getPageManager().getAdminPage().uploadFile();
			//sign the experiment
			getPage().signAndLock();
			//check that the attachment is not editable when experiment is sign
			getPageManager().getAdminPage().viewUploadedFile();
			boolean editable = getPageManager().getAdminPage().isFileEditable();
			AssertJUnit.assertFalse(editable);
			
		} catch (Exception e) {
			setLog(e,"signExperimentWithAttachmentNotEditable");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	
	@Test(groups = {"deep"})
	public void startNewExperimentFromProtocolFromDropdown(){
		
		try {
			//create protocol
			String protocol = createNewProtocol();
			
			showTableIndex();
			checkCreateExpFromProtocolFromDropdown(protocol);
			
		} catch (InterruptedException e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	
	@Test(groups = {"deep"})
	public void startNewDocumentFromDropdown(){
		
		try {
			showTableIndex();			
			checkCreateDocumentFromDropdown();
			
		} catch (InterruptedException e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test(groups = {"deep"})
	public void startNewProjectFromDropdown(){
		
		try {
			showTableIndex();	
			checkCreateProjectFromDropdown();
			
		} catch (InterruptedException e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	
	@Test(groups = {"deep"})
	public void startNewProtocolFromDropdown(){
		
		try {
			showTableIndex();
			checkCreateProtocolFromDropDown();
		} catch (InterruptedException e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void createAndUpdateExperiment() {
		try {
			
			showTableIndex();
			addNewItem();
			
	        String currentDate = getCurrentDateFormatted(LGConstants.CALENDAR_FORMAT);
			
			String account = getPageManager().getAdminPage().getAccountName();
			String bottomMsg = getPageManager().getExperimentPage().updateContent();
			
			assertEquals(getMessageSource().getMessage("item.update.versions.footer.msg",new Object[]{currentDate,account},Locale.US),bottomMsg);
		
			String versionsTitle = getPageManager().getExperimentPage().showVersionHistory();
			currentDate = getCurrentDateFormatted(LGConstants.VERSIONS_HISTORY_CALENDAR_FORMAT);
			assertEquals(getMessageSource().getMessage("version.history.title",new Object[]{account,currentDate},Locale.US),
					versionsTitle.substring(0, versionsTitle.lastIndexOf(' ')));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void addSampleWithGenericCollection() {
		
		try {
			String collectionName = buildUniqueName(LGConstants.GENERIC_COLLECTION_PREFIX);
			getPageManager().getAccountSettingPage().addGenericCollection(collectionName);
			getPageManager().getAdminPage().selectExperiments();
			getPageManager().getExperimentPage().addNewExperiment("sampleWithGenericCollection");
			String sampleName = buildUniqueName(LGConstants.SAMPLE_PREFIX);
			assertTrue(sampleName +" was not shown as expected after refresh.",getPageManager().getExperimentPage().addSampleWithGenericCollection(collectionName,sampleName));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	
	@Test (groups = {"deep"})
	public void addSampleWithTube() {
		
		try {
			showTableIndex();
			getPageManager().getExperimentPage().addNewExperiment("sampleWithTube");
			String sampleName = buildUniqueName(LGConstants.SAMPLE_PREFIX);
			assertTrue(sampleName +" was not shown as expected after refresh.",getPageManager().getExperimentPage().addSample(sampleName,true));//(true = with tube)
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	@Override
	public void showMenu(){
		
		String pageTitle = showTableIndex();
				
	     //Check the title of the page
		AssertJUnit.assertEquals(getMessageSource().getMessage("experiments.title",null, Locale.US), pageTitle);
	}
	
	
	@Test (groups = {"basic sanity"})
	public void addNewExperimentGivenName(){
		
		try {
			showTableIndex();
			
			String name = buildUniqueName(LGConstants.EXPERIMENT_PREFIX);
			String expTitle = getPageManager().getExperimentPage().addNewExperiment(name);
			
			// Check the title of the page
			assertEquals(name, expTitle);
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void addNewExperiment(){
		
		try {
			showTableIndex();
			
			String expTitle = getPageManager().getExperimentPage().addNewExperiment(null);
			
			// Check the title of the page
			assertTrue(expTitle.contains(LGConstants.EXPERIMENT));
			//- later we will add the date and sequence number as args and check that the right date is concatenate to it
			//assertEquals(getMessageSource().getMessage("experiments.default.exp.name",new Object[]{date,seqNum}, Locale.US), expTitle); 
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}	
	
	@Test (groups = {"basic sanity"})
	public void addTextDescriptionToExperiment() {
		
		try {
			showTableIndex();
			getPageManager().getExperimentPage().addNewExperiment("ExpWithDesc");
			
			String descToTest = "Test text in experiment description";
			String desc = getPageManager().getExperimentPage().addTextDescriptionToExperiment(descToTest);
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
			getPageManager().getExperimentPage().addNewExperiment("ExpWithSample");
			String sampleName = buildUniqueName(LGConstants.SAMPLE_PREFIX);
			String notCreated = getPageManager().getExperimentPage().addSamplesToProcedure(sampleName);
			assertTrue("The following sample types were not created as should be: " + notCreated , notCreated.isEmpty());
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	@Test (groups = {"deep"})
	public void addSampleWithoutTubeAndEdit() {
		
		try {
			showTableIndex();
			getPageManager().getExperimentPage().addNewExperiment("sampleWithoutTube");
			String sampleName = buildUniqueName(LGConstants.SAMPLE_PREFIX);
			getPageManager().getExperimentPage().addSample(sampleName,false);
			assertTrue("Tube in sample could not be edited.",getPageManager().getExperimentPage().editSample());
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	@Test (groups = {"basic sanity"})
	public void addPlateToProcedure() {
		
		try {
			showTableIndex();
			String expName = "ExpWithPlate";
			getPageManager().getExperimentPage().addNewExperiment(expName);
			assertTrue(getPageManager().getExperimentPage().addPlateToProcedure());
			
			assertEquals(expName,getPageManager().getExperimentPage().openExperiment(expName));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	@Test (groups = {"basic sanity"})
	public void addStepsToProcedure() {
		try {
			showTableIndex();
			getPageManager().getExperimentPage().addNewExperiment("ExpWithStep");
			int numOfSteps = 1;
			assertTrue(getPageManager().getExperimentPage().addStepToProcedure(numOfSteps));
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	@Test (groups = {"basic sanity"})
	public void deleteStepsOfProcedure() {
		try {
			showTableIndex();
			getPageManager().getExperimentPage().addNewExperiment("ExpWithStepToDelete");
			int numOfSteps = 1;
			getPageManager().getExperimentPage().addStepToProcedure(numOfSteps);
			
			assertTrue(getPageManager().getExperimentPage().deleteStepsOfProcedure());
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}

	@Test (groups = {"basic sanity"})
	public void addTextDescriptionToExpProcedure() {
		
		try {
			showTableIndex();
			getPageManager().getExperimentPage().addNewExperiment("ExpWithProcedureDesc");
			
			String descToTest = "Test description of procedure";
			String desc = getPageManager().getExperimentPage().addTextDescriptionToProcedure(descToTest);
			assertEquals(descToTest, desc);
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void addCompoundDescToExpProcedure() {//only when compound option is enabled

		try {
			showTableIndex();
			String expName = "ExpWithCompound";
			getPageManager().getExperimentPage().addNewExperiment(expName);

			assertTrue(getPageManager().getExperimentPage().addCompoundDescToExpProcedure());
			
			assertEquals(expName,getPageManager().getExperimentPage().openExperiment(expName));
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}	

	}
	

	@Test (groups = {"basic sanity"})
	public void addTableToExpProcedure() {
		
		try {
			showTableIndex();
			
			String expName = "ExpWithTable";
			getPageManager().getExperimentPage().addNewExperiment(expName);
			String dataToWrite = "test";
			assertTrue(getPageManager().getExperimentPage().addTableToProcedure(dataToWrite));

			assertEquals(expName,getPageManager().getExperimentPage().openExperiment(expName));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	

	
	@Test (groups = {"basic sanity"})
	public void addNewProcedure() {
		try {
			showTableIndex();
			getPage().addNewExperiment("ExpWith2Procedures");
			
			String procedureName = "Procedure2";
			String newProcedure = getPageManager().getExperimentPage().addNewProcedure(procedureName);
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
			getPageManager().getExperimentPage().addNewExperiment("ExpWithReaction");
			assertTrue("Adding reaction to experiment procedure description ",getPageManager().getExperimentPage().addReactionToExpProcedure());
			
			assertTrue("Some data are not calculated as should be.. ",getPageManager().getExperimentPage().validateReactionEditingAndCalculating(new String[]{"450","375","185"}));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}

	@Test (groups = {"deep"})
	public void reCalculateReaction() {
		
		try {
			showTableIndex();
			getPageManager().getExperimentPage().addNewExperiment(buildUniqueName(LGConstants.EXPERIMENT_PREFIX));
			getPageManager().getExperimentPage().addReactionToExpProcedure();
			
			getPageManager().getExperimentPage().validateReactionEditingAndCalculating(new String[]{"450","375","185"});
			
			getPageManager().getExperimentPage().editMarvinJSComponent();
			//after open for editing the data should be cleared and recalculated
			assertTrue("Some data are not calculated as should be.. ",getPageManager().getExperimentPage().validateReactionEditingAndCalculating(new String[]{"400","350","200"}));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}

	
	@Test(groups = {"basic sanity"})
	public void addReactionToExpProcedureResults() {
		
		try {
			showTableIndex();
			getPageManager().getExperimentPage().addNewExperiment("ExpWithReactionInResults");
			assertTrue("Adding reaction to experiment results ",getPageManager().getExperimentPage().addReactionToExpProcedureResults());
			
			getPageManager().getAdminPage().showRecentResults();
			assertFalse("Reaction in recent results page should not be editable ",getPageManager().getRecentResultsPage().isReactionEditable());
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	//@Test 
	public void addCompoundAndExportDownload() {//only when compound option is enabled

		try {
//			showTableIndex();
//			String expName = "ExportCompound";
//			getPageManager().getExperimentPage().addNewExperiment(expName);
//
//			getPageManager().getExperimentPage().addCompoundDescToExpProcedure();
			
			
			getPageManager().getExperimentPage().exportFromMarvinjsDialog();
			getPageManager().getExperimentPage().openMarvinJSDialogAndImport("//Users/goni/Downloads/marvinjs_untitled_file(1).mrv");
			
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}	

	}

	
	//@Test  (groups = {"deep"})
	public void searchByCompound(){
		
		try {
			showTableIndex();
			String expName = "ExpWithCompound";
			getPageManager().getExperimentPage().addNewExperiment(expName);

			getPageManager().getExperimentPage().addCompoundDescToExpProcedure();
			
			TimeUnit.SECONDS.sleep(1);
			
			assertTrue("Could not find compound in search.Something went wrong...",getPageManager().getExperimentPage().searchByCompound());
			
		} catch (Exception e) {
			setLog(e,"searchByCompound");
			AssertJUnit.fail(e.getMessage());
		}	
		
	}

	@Override
	protected String showModule() {
		return getPageManager().getAdminPage().selectExperiments();
	}


	@Override
	protected String addNewItem() throws InterruptedException {
		
		String name = buildUniqueName(LGConstants.EXPERIMENT_PREFIX);
		getPageManager().getExperimentPage().addNewExperiment(name);
		return name;
	}

	@Override
	protected AbstractNotebookPage getPage() {
		return getPageManager().getExperimentPage();
	}

}
