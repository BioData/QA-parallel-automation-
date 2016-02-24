package com.biodata.labguru.tests.enotebook;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.enotebook.AbstractNotebookPage;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public class ExperimentV2Test extends AbstractEnotebookTest {

	private static final String DESCRIPTION_SECTION_INDEX = "0";
	private static final String PROCEDURE_SECTION_INDEX = "1";
	private static final String SECOND_PROCEDURE_SECTION_INDEX = "2";
	private static final String RESULTS_SECTION_INDEX = "2";
	private static final String CONCLUSIONS_SECTION_INDEX = "3";

	@Test(groups = {"v2"})
	public void signExperimentWithAttachmentNotEditable(){
		
		try {
			//add new experiment with attachment
			String attachmentToLoad = LGConstants.UPLOAD_TXT_TEST_FILENAME;
			String resource = createNewExperimentAndChangeVersion(null);
			getPageManager().getExperimentPageV2().uploadAttachmentToSection(DESCRIPTION_SECTION_INDEX,attachmentToLoad);
			
			//sign the experiment
			getPageManager().getExperimentPageV2().sign();
	
			//check that the attachment is not editable when experiment is sign
			getPageManager().getExperimentPageV2().checkAttachment(resource,2,attachmentToLoad);
			boolean editable = getPageManager().getAdminPage().isFileEditable();
			AssertJUnit.assertFalse(editable);
			
		} catch (Exception e) {
			setLog(e,"signExperimentWithAttachmentNotEditable");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Override
	@Test (groups = {"v2"})
	public void addTag() {
		try {

			showTableIndex();
			createNewExperimentAndChangeVersion(buildUniqueName(LGConstants.EXPERIMENT_PREFIX));	
			
			String tagName = buildUniqueName(LGConstants.TAG_PREFIX);
			String tag = getPageManager().getExperimentPageV2().addInlineTag(tagName);
			
			assertEquals("Tag with name '" + tagName + "' was not craeted as should be.",tagName, tag);
			
			assertTrue(getPageManager().getExperimentPageV2().deleteTagFromInlineTags(tagName));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"v2"})
	public void createAndUpdateExperiment() {
		try {

			showTableIndex();
			createNewExperimentAndChangeVersion(buildUniqueName(LGConstants.EXPERIMENT_PREFIX));	
			
	        String currentDate = getCurrentDateFormatted(LGConstants.CALENDAR_FORMAT);
			
			String account = getPageManager().getAdminPage().getAccountName();
			String bottomMsg = getPageManager().getExperimentPage().updateContent();
			
			assertEquals(getMessageSource().getMessage("item.update.versions.footer.msg",new Object[]{currentDate,account},Locale.US),bottomMsg);
		
			String versionsTitle = getPageManager().getExperimentPageV2().showExperimentVersionHistory();
			currentDate = getCurrentDateFormatted(LGConstants.VERSIONS_HISTORY_CALENDAR_FORMAT);
			assertEquals(getMessageSource().getMessage("version.history.title",new Object[]{account,currentDate},Locale.US),
					versionsTitle.substring(0, versionsTitle.lastIndexOf(' ')));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"v2"})
	public void addSingleStepToStepsInDescription() {
		
		try {
			
			createNewExperimentAndChangeVersion("ExperimentWith4StepsInDescription");	
			assertTrue("The single step was not added as expected.",getPageManager().getExperimentPageV2().addSingleStepToSection(DESCRIPTION_SECTION_INDEX));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	@Test (groups = {"v2"})
	public void loadSectionFromProtocol() {
		try {
			//add protocol with steps 
			getPageManager().getAdminPage().showProtocols();
			String protocol = getPageManager().getProtocolPage().addProtocolToAccount("ProtocolToSection");
			
			//create new experiment
			createNewExperimentAndChangeVersion("ExpWithSectionFromProtocol");

			//add new section from the created protocol
			Assert.assertTrue(getPageManager().getExperimentPageV2().addNewSectionFromProtocol(DESCRIPTION_SECTION_INDEX,protocol),"Section not created from protocol as expected");
			
		} catch (Exception e) {
			setLog(e,"loadSectionFromProtocol");
 			AssertJUnit.fail(e.getMessage());
		}
	}


	@Test (groups = {"v2"})
	public void undoDeletedElementInSection() {
		
		try {
			
			createNewExperimentAndChangeVersion(buildUniqueName(LGConstants.EXPERIMENT_PREFIX));	
			getPageManager().getExperimentPageV2().addStepToSection(PROCEDURE_SECTION_INDEX);
			assertTrue(getPageManager().getExperimentPageV2().undoDeleteElementActionInSection(PROCEDURE_SECTION_INDEX));
					
		} catch (Exception e) {
			setLog(e,"undoDeletedElementInSection");
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	@Test (groups = {"v2"})
	public void deleteElementInSection() {
		
		try {
			
			createNewExperimentAndChangeVersion(buildUniqueName(LGConstants.EXPERIMENT_PREFIX));	
			getPageManager().getExperimentPageV2().addStepToSection(PROCEDURE_SECTION_INDEX);
			assertTrue(getPageManager().getExperimentPageV2().deleteElementInSection(PROCEDURE_SECTION_INDEX));
					
		} catch (Exception e) {
			setLog(e,"deleteElementInSection");
			AssertJUnit.fail(e.getMessage());
		}

	}

	@Test (groups = {"v2"})
	public void deleteSection() {
		
		try {
			
			createNewExperimentAndChangeVersion(buildUniqueName(LGConstants.EXPERIMENT_PREFIX));	
			getPageManager().getExperimentPageV2().addStepToSection(PROCEDURE_SECTION_INDEX);
			assertTrue(getPageManager().getExperimentPageV2().deleteSection(PROCEDURE_SECTION_INDEX));
					
		} catch (Exception e) {
			setLog(e,"deleteSection");
			AssertJUnit.fail(e.getMessage());
		}

	}

	@Test (groups = {"load"}) 
	public void add40SectionsWithDataToProcedure() {
		try {
			createNewExperimentAndChangeVersion("ExpWith40SectionsInProcedure");
			
			for (int i = 0; i < 40; i++) {
				String procedureName = LGConstants.SECTION_PREFIX + i;
				String newProcedure = getPageManager().getExperimentPageV2().addNewSection(PROCEDURE_SECTION_INDEX,procedureName);
				assertEquals(procedureName, newProcedure);
				String descName = "Description in " + LGConstants.SECTION_PREFIX + i;
				String newDesc = getPageManager().getExperimentPageV2().addTextToSection(SECOND_PROCEDURE_SECTION_INDEX,descName);
				assertEquals(descName, newDesc);
				TimeUnit.SECONDS.sleep(1);
				assertTrue(getPageManager().getExperimentPageV2().addStepToSection(SECOND_PROCEDURE_SECTION_INDEX));
				TimeUnit.SECONDS.sleep(1);
				String dataToWrite = "test";
				assertTrue(getPageManager().getExperimentPageV2().addTableToSection(dataToWrite,SECOND_PROCEDURE_SECTION_INDEX));
			}
			
		} catch (Exception e) {
			setLog(e,"add40SectionsWithDataToProcedure");
 			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"load"}) 
	public void add3SectionsWith20StepsElementsInSection() {
		try {
			String expName = "ExpWith3SectionsWith20StepsElements";
			createNewExperimentAndChangeVersion(expName);
			
			for (int i = 0; i < 3; i++) {
				String procedureName = LGConstants.SECTION_PREFIX + i;
				String newProcedure = getPageManager().getExperimentPageV2().addNewSection(PROCEDURE_SECTION_INDEX,procedureName);
				assertEquals(procedureName, newProcedure);
				String descName = "Description in " + LGConstants.SECTION_PREFIX + i;
				String newDesc = getPageManager().getExperimentPageV2().addTextToSection(SECOND_PROCEDURE_SECTION_INDEX,descName);
				assertEquals(descName, newDesc);
				TimeUnit.SECONDS.sleep(1);
				//add 20 steps elements to section
				for (int j = 0; j < 20; j++) {
					getPageManager().getExperimentPageV2().addStepToSection(SECOND_PROCEDURE_SECTION_INDEX);
					TimeUnit.SECONDS.sleep(1);
				}
			}
			//try to load experiment
			try{
				getPageManager().getExperimentPageV2().searchExperiment(expName,true);
			}catch (Exception e) {
				Assert.fail("the experiment did not load as expected.", e);
			}
			
			
		} catch (Exception e) {
			setLog(e,"add3SectionsWith20StepsElementsInSection");
 			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Override
	@Test (groups = {"v2"})
	public void duplicateItem(){
		
		try {
	
			String expName = buildUniqueName(LGConstants.EXPERIMENT_PREFIX);
			createNewExperimentAndChangeVersion(expName);
			String duplicateItemName = getPageManager().getExperimentPageV2().duplicate();
			
			// Check the title of the page
			assertTrue("The duplication did not finished as expected - " + duplicateItemName,duplicateItemName.startsWith(expName) && duplicateItemName.contains("(duplicate)"));		
		
		} catch (Exception e) {
			setLog(e,"duplicateExperiment");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"v2"}) 
	public void saveAsProtocol() {
		try {
			String expName = buildUniqueName(LGConstants.EXPERIMENT_PREFIX);
			createNewExperimentAndChangeVersion(expName);

			String msg = getPageManager().getExperimentPageV2().saveAsProtocol();
			Assert.assertEquals(getMessageSource().getMessage("experiment.save.as.protocol.success.msg",new Object[]{expName}, Locale.US),msg);
			
			getPage().showProtocols();
			boolean found = getPageManager().getProtocolPage().searchProtocolInDirectory(expName);
			Assert.assertTrue(found);
		} catch (Exception e) {
			setLog(e,"saveAsProtocol");
 			AssertJUnit.fail(e.getMessage());
		}
	}
	
	
	@Test (groups = {"v2"}) 
	public void goToProjectFromExperiment() {
		try {
			createNewExperimentAndChangeVersion(null);
			
			assertTrue(getPageManager().getExperimentPageV2().changeToProjectPage());
			
		} catch (Exception e) {
			setLog(e,"goToProjectFromExperiment");
 			AssertJUnit.fail(e.getMessage());
		}
	}

	@Test (groups = {"v2"}) 
	public void goToFolderFromExperiment() {
		try {
			createNewExperimentAndChangeVersion(null);
			
			assertTrue(getPageManager().getExperimentPageV2().changeToFolderPage());
			
		} catch (Exception e) {
			setLog(e,"goToFolderFromExperiment");
 			AssertJUnit.fail(e.getMessage());
		}
	}
	

	@Override
	@Test (groups = {"v2"})
	public void addTask(){
		
		try {
	
			createNewExperimentAndChangeVersion(null);
			
			String taskName = buildUniqueName(LGConstants.TASK_PREFIX);
			String task = getPageManager().getAdminPage().addTask(taskName);
			
			AssertJUnit.assertEquals("Task with name '" + taskName + "' was not craeted as should be.",taskName, task);
		
		} catch (Exception e) {
			setLog(e,"addTask");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Override
	@Test (groups = {"v2"})
	public void uploadFile() {
		try {
			String attachmentToLoad = LGConstants.UPLOAD_TXT_TEST_FILENAME;
			String resource = createNewExperimentAndChangeVersion(null);
			getPageManager().getExperimentPageV2().uploadAttachmentToSection(DESCRIPTION_SECTION_INDEX,attachmentToLoad);
			String pageTitle = getPageManager().getExperimentPageV2().checkAttachment(resource,2,attachmentToLoad);
			AssertJUnit.assertEquals(attachmentToLoad,pageTitle);
			
		} catch (Exception e) {
			setLog(e,"uploadFile");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Override
	@Test (groups = {"v2"})
	public void uploadImage() {
		try {
			String attachmentToLoad = LGConstants.UPLOAD_IMAGE_TEST_FILENAME;
			String resource = createNewExperimentAndChangeVersion(null);
			getPageManager().getExperimentPageV2().uploadAttachmentToSection(DESCRIPTION_SECTION_INDEX,attachmentToLoad);
			String pageTitle =  getPageManager().getExperimentPageV2().checkAttachment(resource, 1,attachmentToLoad);
			AssertJUnit.assertEquals(attachmentToLoad,pageTitle);
			
			
		} catch (Exception e) {
			setLog(e,"uploadImage");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"v2"})
	public void deleteSingleAttachmentFromSection() {
		try {
			
			showTableIndex();
			TimeUnit.SECONDS.sleep(2);
			createNewExperimentAndChangeVersion(null);
			getPageManager().getExperimentPageV2().uploadAttachmentToSection(DESCRIPTION_SECTION_INDEX,LGConstants.UPLOAD_TXT_TEST_FILENAME);
			
			boolean deleted = getPageManager().getExperimentPageV2().deleteSingleAttachmentFromSection(DESCRIPTION_SECTION_INDEX);
			Assert.assertTrue(deleted,"Attachment could not be deleted.");
		} catch (Exception e) {
			setLog(e,"deleteSingleAttachmentFromSection");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Override
	@Test (groups = {"v2"})
	public void deleteAttachment() {
		try {
			
			showTableIndex();
			TimeUnit.SECONDS.sleep(2);
			createNewExperimentAndChangeVersion(null);
			getPageManager().getExperimentPageV2().uploadAttachmentToSection(DESCRIPTION_SECTION_INDEX,LGConstants.UPLOAD_TXT_TEST_FILENAME);
			
			boolean deleted = getPageManager().getExperimentPageV2().deleteAttachmentContainer(DESCRIPTION_SECTION_INDEX);
			Assert.assertTrue(deleted,"Attachment could not be deleted.");
		} catch (Exception e) {
			setLog(e,"deleteAttachment");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Override
	@Test (groups = {"v2"})
	public void addLinkedResource(){
		
		try {
			//before addResource i want to make sure there is at least one experiment as a link resource
			hasExperiments  = hasExperiments || getPageManager().getExperimentPage().hasList();
    		if(!hasExperiments ){
    			getPageManager().getExperimentPage().addNewExperiment("First Experiment");
    			hasExperiments = true;
    		}
    		
    		createNewExperimentAndChangeVersion(null);
			
			String linkedRes = getPageManager().getExperimentPageV2().addLinkedResource(LGConstants.EXPERIMENT);
			
			AssertJUnit.assertTrue("No resource was linked.",!linkedRes.equals(""));
		
		} catch (Exception e) {
			setLog(e,"addLinkedResource");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"test"})
	public void addLinkToSection(){
		
		try {
			showTableIndex();
    		String  resourceToLink = getPageManager().getExperimentPage().addNewExperiment(buildUniqueName(LGConstants.EXPERIMENT_PREFIX));
    		createNewExperimentAndChangeVersion(null);
			
			String linkedRes = getPageManager().getExperimentPageV2().addLinkToSection(DESCRIPTION_SECTION_INDEX,resourceToLink);
			
			AssertJUnit.assertTrue("No resource was linked.",!linkedRes.equals(""));
		
		} catch (Exception e) {
			setLog(e,"addLinkedResource");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"v2"})
	public void addStepsToDescription() {
		
		try {
			
			createNewExperimentAndChangeVersion("ExperimentWithStepsInDescription");	
			assertTrue(getPageManager().getExperimentPageV2().addStepToSection(DESCRIPTION_SECTION_INDEX));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}


	@Test (groups = {"v2"}) 
	public void addNewSectionToDescription() {
		try {
			createNewExperimentAndChangeVersion("ExpWith2SectionsInDescription");
			
			String descName = "Description2";
			String newDesc = getPageManager().getExperimentPageV2().addNewSection(DESCRIPTION_SECTION_INDEX,descName);
			assertEquals(descName, newDesc);
			
		} catch (Exception e) {
			setLog(e);
 			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"v2"})
	public void addTextToDescription() {
		try {
			createNewExperimentAndChangeVersion("ExpWithTextInDescription");
			
			String descName = "Description";
			String newDesc = getPageManager().getExperimentPageV2().addTextToSection(DESCRIPTION_SECTION_INDEX,descName);
			assertEquals(descName, newDesc);
		} catch (Exception e) {
			setLog(e);
 			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"v2"})
	public void addTableToDescription() {
		
		try {
			
			String expName = "ExpWithTableInDescription";
			createNewExperimentAndChangeVersion(expName);
			String dataToWrite = "test";
			assertTrue(getPageManager().getExperimentPageV2().addTableToSection(dataToWrite,DESCRIPTION_SECTION_INDEX));
			
			assertEquals(expName,getPageManager().getExperimentPage().openExperiment(expName));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	
	
	
	@Test (groups = {"v2"}) 
	public void addNewSectionToProcedure() {
		try {
			createNewExperimentAndChangeVersion("ExpWith2SectionsInProcedure");
			
			String procedureName = "Procedure2";
			String newProcedure = getPageManager().getExperimentPageV2().addNewSection(PROCEDURE_SECTION_INDEX,procedureName);
			assertEquals(procedureName, newProcedure);
		} catch (Exception e) {
			setLog(e);
 			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"v2"})
	public void addStepsToProcedure() {
		
		try {
			
			createNewExperimentAndChangeVersion("ExperimentWithStepsInProcedure");	
			assertTrue(getPageManager().getExperimentPageV2().addStepToSection(PROCEDURE_SECTION_INDEX));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}
	
		@Test (groups = {"v2"})
	public void deleteStepsOfProcedure() {
		try {
			createNewExperimentAndChangeVersion("DeleteStepsInProcedure");	
			assertTrue(getPageManager().getExperimentPageV2().addStepToSection(PROCEDURE_SECTION_INDEX));
			
			assertTrue(getPageManager().getExperimentPageV2().deleteStepsOfSection(PROCEDURE_SECTION_INDEX));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	
	
	@Test (groups = {"v2"})
	public void addTableToProcedure() {
		
		try {
			
			String expName = "ExpWithTableInProcedure";
			createNewExperimentAndChangeVersion(expName);
			String dataToWrite = "test";
			assertTrue(getPageManager().getExperimentPageV2().addTableToSection(dataToWrite,PROCEDURE_SECTION_INDEX));
			
			assertEquals(expName,getPageManager().getExperimentPage().openExperiment(expName));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	

	
	@Test (groups = {"v2"})
	public void addTextToProcedure(){
		try {
			createNewExperimentAndChangeVersion("ExpWithTextInProcedure");
			
			String descName = "Description in procedure";
			String newDesc = getPageManager().getExperimentPageV2().addTextToSection(PROCEDURE_SECTION_INDEX,descName);
			assertEquals(descName, newDesc);
		} catch (Exception e) {
			setLog(e);
 			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"v2"})
	public void addDataToConclusion(){
		try {
			createNewExperimentAndChangeVersion("ExpWithDataInConclusion");
			
			String descName = "Description in conclusion";
			getPageManager().getExperimentPageV2().addConclusionSection();
			String newDesc = getPageManager().getExperimentPageV2().addTextToSection(CONCLUSIONS_SECTION_INDEX,descName);
			assertEquals(descName, newDesc);
			TimeUnit.SECONDS.sleep(1);
			assertTrue(getPageManager().getExperimentPageV2().addStepToSection(CONCLUSIONS_SECTION_INDEX));
			TimeUnit.SECONDS.sleep(1);
			String dataToWrite = "test";
			assertTrue(getPageManager().getExperimentPageV2().addTableToSection(dataToWrite,CONCLUSIONS_SECTION_INDEX));
			
		} catch (Exception e) {
			setLog(e);
 			AssertJUnit.fail(e.getMessage());
		}
	}
		
	
	@Test (groups = {"v2"})
	public void addDataToResults(){
		try {
			createNewExperimentAndChangeVersion("ExpWithDataInResults");
			
			String descName = "Description in results";
			String newDesc = getPageManager().getExperimentPageV2().addTextToSection(RESULTS_SECTION_INDEX,descName);
			assertEquals(descName, newDesc);
			TimeUnit.SECONDS.sleep(1);
			assertTrue(getPageManager().getExperimentPageV2().addStepToSection(RESULTS_SECTION_INDEX));
			TimeUnit.SECONDS.sleep(1);
			String dataToWrite = "test";
			assertTrue(getPageManager().getExperimentPageV2().addTableToSection(dataToWrite,RESULTS_SECTION_INDEX));
			
		} catch (Exception e) {
			setLog(e,"addDataToResults");
 			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"fail1"})
	public void assignNewAccount() {
		
		try {
			//first we add a new member so we can assign to it the experiment
			getPageManager().getAdminPage().selectAccountMembersMenu();			
			String memberName = buildUniqueName(LGConstants.BIODATA_MEMBER_NAME);
			String email = LGConstants.QA_PREFIX_MAIL + memberName + LGConstants.GMAIL_SUFFIX_MAIL;
			getPageManager().getMembersPage().addNewMember(memberName, "test", email);
			
			
			createNewExperimentAndChangeVersion(buildUniqueName(LGConstants.EXPERIMENT_PREFIX));
			String currentAccout =getPageManager().getExperimentPageV2().getAccountName();
			String newAccount = getPageManager().getExperimentPageV2().assign();
			Assert.assertNotEquals(currentAccout,newAccount,"The assignment did not succeeded.");
		} catch (Exception e) {
			setLog(e,"assignNewAccount");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"v2"})
	public void signExperiment() {
		
		try {
			createNewExperimentAndChangeVersion("Signed Experiment");
			
			boolean signSucceeded = getPageManager().getExperimentPageV2().sign();
			assertTrue("The sign action did not succeeded.",signSucceeded);
			
			assertTrue("Some menu actions are not as they should be.",getPageManager().getExperimentPageV2().checkAllowedActionsOnSignedExp());
			
			assertTrue("Sign mark is missing from the list view for the signed experiment.",getPageManager().getExperimentPageV2().checkSignedImgInList());
		} catch (Exception e) {
			setLog(e,"signExperiment");
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	@Test (groups = {"v2"})
	public void revertSignature() {
		//Users that are not admin or signed the exp can't revert the signature.
		try {
			createNewExperimentAndChangeVersion(buildUniqueName(LGConstants.EXPERIMENT_PREFIX));
			
			assertTrue("The sign action did not succeeded.",getPageManager().getExperimentPageV2().sign());
			
			boolean revertSucceeded = getPageManager().getExperimentPageV2().revertSignature();
			assertTrue("The revert signature action did not succeeded.",revertSucceeded);
			
		} catch (Exception e) {
			setLog(e,"revertSignature");
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	@Test (groups = {"v2"})
	public void deleteExperiment() {
		
		try {
			String expName = buildUniqueName(LGConstants.EXPERIMENT_PREFIX);
			createNewExperimentAndChangeVersion(expName);		
			boolean deleted = getPageManager().getExperimentPageV2().deleteExperiment(expName);
			assertTrue("The delete action did not succeeded.",deleted);
		} catch (Exception e) {
			setLog(e,"deleteExperiment");
			AssertJUnit.fail(e.getMessage());
		}

	}

	@Test (groups = {"v2"})
	public void moveExperiment() {
		
		try {
			String expName = buildUniqueName(LGConstants.EXPERIMENT_PREFIX);
			createNewExperimentAndChangeVersion(expName);	
			
			getPageManager().getAdminPage().selectProjects();
			String newProject =buildUniqueName(LGConstants.PROJECT_PREFIX);
			 getPageManager().getProjectPage().addNewProject(newProject);
			
			getPageManager().getExperimentPage().openExperiment(expName);
			getPageManager().getExperimentPageV2().changeVersion(LGConstants.EXPERIMENT_BETA);
			
			String movedTo = getPageManager().getExperimentPageV2().moveExperimentToProject(newProject);
			assertTrue("The move action did not succeeded.",movedTo.equals(newProject));
		} catch (Exception e) {
			setLog(e,"moveExperiment");
			AssertJUnit.fail(e.getMessage());
		}

	}

	@Test (groups = {"v2"})
	public void addPlateToProcedure(){
		
		try {
			createNewExperimentAndChangeVersion("ExperimentWithPlate2X3InProcedure");	
			assertTrue(getPageManager().getExperimentPageV2().addPlate2X3ToSection(PROCEDURE_SECTION_INDEX));
			
		}  catch (Exception e) {
			setLog(e,"addPlateToProcedure");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"v2"})
	public void addSamplesToProcedure() {
		
		try {
			createNewExperimentAndChangeVersion("ExpWithSample");
			String sampleName = buildUniqueName(LGConstants.SAMPLE_PREFIX);
			String notCreated = getPageManager().getExperimentPageV2().addSamplesToSection(PROCEDURE_SECTION_INDEX,sampleName);
			assertTrue("The following sample types were not created as should be: " + notCreated , notCreated.isEmpty());
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	@Test (groups = {"v2"})
	public void uploadExcelAndAddToPage() {
		try {
			String attachmentToLoad = LGConstants.UPLOAD_XLS_TEST_FILENAME;
			createNewExperimentAndChangeVersion(null);
			getPageManager().getExperimentPageV2().uploadAttachmentToSection(DESCRIPTION_SECTION_INDEX,attachmentToLoad);
			getPageManager().getExperimentPageV2().refreshPage();
			boolean pageAdded = getPageManager().getExperimentPageV2().addToPage(DESCRIPTION_SECTION_INDEX);
			AssertJUnit.assertTrue("The file '" + attachmentToLoad + "' was not added to page.",pageAdded);
			
		} catch (Exception e) {
			setLog(e,"uploadFile");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	/*

	//@Test (groups = {"v2"})
	public void addReactionToResults() {
		
		try {
			
			createNewExperimentAndChangeVersion("ExpWithReactionInResults");
			assertTrue("Adding reaction to experiment results ",getPageManager().getExperimentPageV2().addReactionToResults());
			
			getPageManager().getAdminPage().showRecentResults();
			assertFalse("Reaction in recent results page should not be editable ",getPageManager().getRecentResultsPage().isReactionEditable());
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	//@Test (groups = {"v2"})
	public void addReactionToProcedure() {
		
		try {
			
			createNewExperimentAndChangeVersion("ExpWithReactionInResults");
			assertTrue("Adding reaction to experiment results ",getPageManager().getExperimentPageV2().addReactionToProcedure());
			
		
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	

	
	//@Test (groups = {"v2"})
	public void addCompoundToProcedure() {
		
		try {
			String expName = "ExpWithCompoundInProcedure";
			createNewExperimentAndChangeVersion(expName);
	
			assertTrue(getPageManager().getExperimentPageV2().addCompoundToProcedure());
			
			assertEquals(expName,getPageManager().getExperimentPage().openExperiment(expName));

			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
*/

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
		return getPageManager().getExperimentPageV2();
	}

	@Override
	public void showMenu() {
		
		String pageTitle = showTableIndex();
		
	     //Check the title of the page
		AssertJUnit.assertEquals(getMessageSource().getMessage("experiments.title",null, Locale.US), pageTitle);
	}
	
	private String createNewExperimentAndChangeVersion(String expName) throws InterruptedException {
		
		showTableIndex();
		String name = getPage().addNewExperiment(expName);
		//change to version V2
		getPageManager().getExperimentPageV2().changeVersion(LGConstants.EXPERIMENT_BETA);
		return name;
	}

}
