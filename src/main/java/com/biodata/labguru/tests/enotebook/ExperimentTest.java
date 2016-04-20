package com.biodata.labguru.tests.enotebook;

import org.testng.annotations.Listeners;

import com.biodata.labguru.tests.TestOrderRandomizer;
/**
**This class will not be in use after moving forward to beta version of experiment***
*/
@Deprecated
@Listeners(TestOrderRandomizer.class)
public class ExperimentTest {//extends AbstractEnotebookTest {
/*	
	private String createNewExperimentAndChangeToCurrentVersion(String expName) throws InterruptedException {
		
		showTableIndex();
		String name = getPage().addNewExperiment(expName);
		//change to version V2
		getPageManager().getExperimentPage().changeVersion(LGConstants.EXPERIMENT_CURRENT);
		return name;
	}
	
	//@Test(groups = {"deep"})
	public void signAndLock(){
		
		try {
			String name = buildUniqueName(LGConstants.EXPERIMENT_PREFIX);
			createNewExperimentAndChangeToCurrentVersion(name);
			
			String note = getPage().signAndLock();
			
			Assert.assertTrue(note.startsWith(getMessageSource().getMessage("signed.by.note.prefix",null, Locale.US)));
			
			boolean exist = getPageManager().getRecentResultsPage().checkSignedExperimentInList(name);
			Assert.assertTrue(exist, "Signed experiment is not shown in recent results page");
		} catch (Exception e) {
			setLog(e,"signAndLock");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	//@Test(groups = {"deep"})
	public void signExperimentWithAttachmentNotEditable(){
		
		try {
			//add new experiment with attachment
			String name = buildUniqueName(LGConstants.EXPERIMENT_PREFIX);
			createNewExperimentAndChangeToCurrentVersion(name);
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
	

	
	//@Test (groups = {"deep"})
	public void createAndUpdateExperiment() {
		try {
			
			createNewExperimentAndChangeToCurrentVersion(null);
			
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
	
	//@Test (groups = {"deep"})
	public void addSampleWithGenericCollection() {
		
		try {
			String collectionName = buildUniqueName(LGConstants.GENERIC_COLLECTION_PREFIX);
			getPageManager().getAccountSettingPage().addGenericCollection(collectionName);
			createNewExperimentAndChangeToCurrentVersion("sampleWithGenericCollection");
			String sampleName = buildUniqueName(LGConstants.SAMPLE_PREFIX);
			assertTrue(sampleName +" was not shown as expected after refresh.",getPageManager().getExperimentPage().addSampleWithGenericCollection(collectionName,sampleName));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	
	//@Test (groups = {"deep"})
	public void addSampleWithTube() {
		
		try {
			
			createNewExperimentAndChangeToCurrentVersion("sampleWithTube");
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
	
	
	//@Test (groups = {"basic sanity"})
	public void addTextDescriptionToExperiment() {
		
		try {
			createNewExperimentAndChangeToCurrentVersion("ExpWithDesc");
			
			String descToTest = "Test text in experiment description";
			String desc = getPageManager().getExperimentPage().addTextDescriptionToExperiment(descToTest);
			assertEquals(descToTest, desc);
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	
	//@Test (groups = {"basic sanity"})
	public void addSamplesToProcedure() {
		
		try {
			createNewExperimentAndChangeToCurrentVersion("ExpWithSample");
			String sampleName = buildUniqueName(LGConstants.SAMPLE_PREFIX);
			String notCreated = getPageManager().getExperimentPage().addSamplesToProcedure(sampleName);
			assertTrue("The following sample types were not created as should be: " + notCreated , notCreated.isEmpty());
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	
	//@Test (groups = {"basic sanity"})
	public void addPlateToProcedure() {
		
		try {
			
			String name = "ExpWithPlate";
			createNewExperimentAndChangeToCurrentVersion(name);
			assertTrue(getPageManager().getExperimentPage().addPlateToProcedure());
			
			assertEquals(name,getPageManager().getExperimentPage().openExperiment(name));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	//@Test (groups = {"basic sanity"})
	public void addStepsToProcedure() {
		try {
			String name = "ExpWithStep";
			createNewExperimentAndChangeToCurrentVersion(name);
			int numOfSteps = 1;
			assertTrue(getPageManager().getExperimentPage().addStepToProcedure(numOfSteps));
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	//@Test (groups = {"basic sanity"})
	public void deleteStepsOfProcedure() {
		try {
			String name = "ExpWithStepToDelete";
			createNewExperimentAndChangeToCurrentVersion(name);
			int numOfSteps = 1;
			getPageManager().getExperimentPage().addStepToProcedure(numOfSteps);
			
			assertTrue(getPageManager().getExperimentPage().deleteStepsOfProcedure());
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}

	//@Test (groups = {"basic sanity"})
	public void addTextDescriptionToExpProcedure() {
		
		try {
			String name = "ExpWithProcedureDesc";
			createNewExperimentAndChangeToCurrentVersion(name);
			
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
			String name = "ExpWithCompound";
			createNewExperimentAndChangeToCurrentVersion(name);

			assertTrue(getPageManager().getExperimentPage().addCompoundDescToExpProcedure());
			
			assertEquals(name,getPageManager().getExperimentPage().openExperiment(name));
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}	

	}
	

	//@Test (groups = {"basic sanity"})
	public void addTableToExpProcedure() {
		
		try {
			String name = "ExpWithTable";
			createNewExperimentAndChangeToCurrentVersion(name);

			String dataToWrite = "test";
			assertTrue(getPageManager().getExperimentPage().addTableToProcedure(dataToWrite));

			assertEquals(name,getPageManager().getExperimentPage().openExperiment(name));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	

	
	//@Test (groups = {"basic sanity"})
	public void addNewProcedure() {
		try {
	
			String name = "ExpWith2Procedures";
			createNewExperimentAndChangeToCurrentVersion(name);

			
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
			String name = "ExpWithReaction";
			createNewExperimentAndChangeToCurrentVersion(name);
			
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
			String name = buildUniqueName(LGConstants.EXPERIMENT_PREFIX);
			createNewExperimentAndChangeToCurrentVersion(name);
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
			String name = "ExpWithReactionInResults";
			createNewExperimentAndChangeToCurrentVersion(name);
			
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
			createNewExperimentAndChangeToCurrentVersion("ExpWithCompound");
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
		getPageManager().getExperimentPage().changeVersion(LGConstants.EXPERIMENT_CURRENT);
		return name;
	}

	@Override
	protected AbstractNotebookPage getPage() {
		return getPageManager().getExperimentPage();
	}
*/
}
