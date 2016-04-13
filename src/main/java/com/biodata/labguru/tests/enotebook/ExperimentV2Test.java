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

	
	
//	@Test (groups = {"test"})
//	public void setDateRangeToProcedure(){
//		
//		try {
//			createNewExperimentAndChangeVersion("ExperimentWithSetDateRangeInProcedure");	
//			getPageManager().getExperimentPageV2().setDateRangeToProcedure(PROCEDURE_SECTION_INDEX);
//			
//		}  catch (Exception e) {
//			setLog(e,"setDateRangeToProcedure");
//			AssertJUnit.fail(e.getMessage());
//		}
//	}
	
	@Test (groups = {"basic sanity"})
	public void addNewExperimentGivenName(){
		
		try {
			showTableIndex();
			
			String name = buildUniqueName(LGConstants.EXPERIMENT_PREFIX);
			String expTitle = getPageManager().getExperimentPageV2().addNewExperiment(name);
			
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
	public void addManipulateTextToSection(){
		
		try {
			createNewExperimentAndChangeVersion("ExpWithTextWithManipulation");
			
			String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis non rutrum odio.";
			getPageManager().getExperimentPageV2().addTextToSection(DESCRIPTION_SECTION_INDEX,text);
			String platform = getPageManager().getPlatform();
			boolean succeeded = getPageManager().getExperimentPageV2().manipulateTextWithFontAction(DESCRIPTION_SECTION_INDEX,text,platform);
			AssertJUnit.assertTrue(succeeded);
		} catch (Exception e) {
			setLog(e,"addManipulateTextToSection");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test(groups = {"basic sanity"})
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
	@Test (groups = {"basic sanity"})
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
	
	@Test (groups = {"basic sanity"})
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
	
	@Test (groups = {"basic sanity"})
	public void addSingleStepToStepsInDescription() {
		
		try {
			
			createNewExperimentAndChangeVersion("ExperimentWith4StepsInDescription");	
			assertTrue("The single step was not added as expected.",getPageManager().getExperimentPageV2().addSingleStepToSection(DESCRIPTION_SECTION_INDEX));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	@Test (groups = {"basic sanity"})
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


	@Test (groups = {"basic sanity"})
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
	
	@Test (groups = {"basic sanity"})
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

	@Test (groups = {"basic sanity"})
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
	@Test (groups = {"basic sanity"})
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
	
	@Test (groups = {"basic sanity"}) 
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
	
	
	@Test (groups = {"basic sanity"}) 
	public void goToProjectFromExperiment() {
		try {
			createNewExperimentAndChangeVersion(null);
			
			assertTrue(getPageManager().getExperimentPageV2().changeToProjectPage());
			
		} catch (Exception e) {
			setLog(e,"goToProjectFromExperiment");
 			AssertJUnit.fail(e.getMessage());
		}
	}

	@Test (groups = {"basic sanity"}) 
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
	@Test (groups = {"basic sanity"})
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
	@Test (groups = {"basic sanity"})
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
	@Test (groups = {"basic sanity"})
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
	
	@Test (groups = {"basic sanity"})
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
	@Test (groups = {"basic sanity"})
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
	@Test (groups = {"basic sanity"})
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
	
	@Test (groups = {"basic sanity"})
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
	
	@Test (groups = {"TODO"})//TODO - not working with redactor
	public void addInlineCommentToSection(){
		
		try {
			createNewExperimentAndChangeVersion("ExpWithCommentOnDescription");
			
			String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis non rutrum odio.";
			getPageManager().getExperimentPageV2().addTextToSection(DESCRIPTION_SECTION_INDEX,text);
			String commentToAdd = "comment1";
			String platform = getPageManager().getPlatform();
			String addedComment = getPageManager().getExperimentPageV2().addInlineCommentToSection(DESCRIPTION_SECTION_INDEX,commentToAdd,platform);
			assertEquals(addedComment, "comment1");
		} catch (Exception e) {
			setLog(e,"addInlineCommentToSection");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void addStepsToDescription() {
		
		try {
			
			createNewExperimentAndChangeVersion("ExperimentWithStepsInDescription");	
			assertTrue(getPageManager().getExperimentPageV2().addStepToSection(DESCRIPTION_SECTION_INDEX));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}


	@Test (groups = {"basic sanity"}) 
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
	
	@Test (groups = {"basic sanity"})
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
	
	@Test (groups = {"basic sanity"})
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
	
	
	
	
	@Test (groups = {"basic sanity"}) 
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
	
	@Test (groups = {"basic sanity"})
	public void addStepsToProcedure() {
		
		try {
			
			createNewExperimentAndChangeVersion("ExperimentWithStepsInProcedure");	
			assertTrue(getPageManager().getExperimentPageV2().addStepToSection(PROCEDURE_SECTION_INDEX));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}
	
		@Test (groups = {"basic sanity"})
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
	
	
	
	@Test (groups = {"basic sanity"})
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
	

	
	@Test (groups = {"basic sanity"})
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
	
	@Test (groups = {"basic sanity"})
	public void addDataToConclusion(){
		try {
			createNewExperimentAndChangeVersion("ExpWithDataInConclusion");
			
			String descName = "Description in conclusion";
			getPageManager().getExperimentPageV2().addConclusionSection();
			String newDesc = getPageManager().getExperimentPageV2().addTextToSection(CONCLUSIONS_SECTION_INDEX,descName);
			assertEquals("failed to add text.",descName, newDesc);
			TimeUnit.SECONDS.sleep(1);
			assertTrue("failed to add steps.",getPageManager().getExperimentPageV2().addStepToSection(CONCLUSIONS_SECTION_INDEX));
			TimeUnit.SECONDS.sleep(1);
			String dataToWrite = "test";
			assertTrue("failed to add table with data.",getPageManager().getExperimentPageV2().addTableToSection(dataToWrite,CONCLUSIONS_SECTION_INDEX));
			
		} catch (Exception e) {
			setLog(e);
 			AssertJUnit.fail(e.getMessage());
		}
	}
		
	
	@Test (groups = {"basic sanity"})
	public void addDataToResults(){
		try {
			createNewExperimentAndChangeVersion("ExpWithDataInResults");
			
			String descName = "Description in results";
			String newDesc = getPageManager().getExperimentPageV2().addTextToSection(RESULTS_SECTION_INDEX,descName);
			assertEquals("failed to add text.",descName, newDesc);
			TimeUnit.SECONDS.sleep(1);
			assertTrue("failed to add steps.",getPageManager().getExperimentPageV2().addStepToSection(RESULTS_SECTION_INDEX));
			TimeUnit.SECONDS.sleep(1);
			String dataToWrite = "test";
			assertTrue("failed to add table with data.",getPageManager().getExperimentPageV2().addTableToSection(dataToWrite,RESULTS_SECTION_INDEX));
			
		} catch (Exception e) {
			setLog(e,"addDataToResults");
 			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void assignNewAccount() {
		
		try {
			//first we add a new member so we can assign to it the experiment
			getPageManager().getAdminPage().selectAccountMembersMenu();			
			String memberName = buildUniqueName(LGConstants.BIODATA_MEMBER_NAME);
			String email = LGConstants.QA_PREFIX_MAIL + memberName + LGConstants.GMAIL_SUFFIX_MAIL;
			getPageManager().getMembersPage().addNewMember(memberName, "test", email);
			
			
			createNewExperimentAndChangeVersion(buildUniqueName(LGConstants.EXPERIMENT_PREFIX));
			String currentAccout =getPageManager().getExperimentPageV2().getAccountName();
			String newAccount = getPageManager().getExperimentPageV2().assign(currentAccout);
			Assert.assertNotEquals(currentAccout,newAccount,"The assignment did not succeeded.");
		} catch (Exception e) {
			setLog(e,"assignNewAccount");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void signExperiment() {
		
		try {
			String name = buildUniqueName("Signed Experiment");
			createNewExperimentAndChangeVersion(name);
			
			boolean signSucceeded = getPageManager().getExperimentPageV2().sign();
			assertTrue("The sign action did not succeeded.",signSucceeded);
			
			assertTrue("Some menu actions are not as they should be.",getPageManager().getExperimentPageV2().checkAllowedActionsOnSignedExp());
			
			assertTrue("Sign mark is missing from the list view for the signed experiment.",getPageManager().getExperimentPageV2().checkSignedImgInList());
		
			boolean exist = getPageManager().getRecentResultsPage().checkSignedExperimentInList(name);
			Assert.assertTrue(exist, "Signed experiment is not shown in recent results page");
			
		} catch (Exception e) {
			setLog(e,"signExperiment");
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	@Test (groups = {"basic sanity"})
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
	
	@Test (groups = {"basic sanity"})
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

	@Test (groups = {"basic sanity"})
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

	@Test (groups = {"basic sanity"})
	public void addPlateToProcedure(){
		
		try {
			createNewExperimentAndChangeVersion("ExperimentWithPlate2X3InProcedure");	
			assertTrue(getPageManager().getExperimentPageV2().addPlate2X3ToSection(PROCEDURE_SECTION_INDEX));
			
		}  catch (Exception e) {
			setLog(e,"addPlateToProcedure");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"TODO"})//TODO - not working due to auto save
	public void addSamplesToSection() {
		
		try {
			createNewExperimentAndChangeVersion("ExpWithSample");
			String notCreated = getPageManager().getExperimentPageV2().addSamplesToSection(PROCEDURE_SECTION_INDEX);
			assertTrue("The following sample types were not created as should be: " + notCreated , notCreated.isEmpty());
		} catch (Exception e) {
			setLog(e,"addSamplesToSection");
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	@Test (groups = {"basic sanity"})
	public void addCompoundToSection() {
		
		try {
			String expName = "ExpWithCompoundInProcedure";
			createNewExperimentAndChangeVersion(expName);
	
			boolean created = getPageManager().getExperimentPageV2().addCompoundToSection(PROCEDURE_SECTION_INDEX);
			assertTrue("The compound was not created as should be",created);
			
			assertEquals(expName,getPageManager().getExperimentPage().openExperiment(expName));

			
		} catch (Exception e) {
			setLog(e,"addCompoundToSection");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void editCompoundInSection() {
		
		try {
			String expName = "ExpWithCompoundToEdit";
			createNewExperimentAndChangeVersion(expName);
	
			getPageManager().getExperimentPageV2().addCompoundToSection(DESCRIPTION_SECTION_INDEX);
			boolean edited = getPageManager().getExperimentPageV2().editCompound(DESCRIPTION_SECTION_INDEX,"editedCompound");
			assertTrue("The compound was not edited as should be",edited);


			
		} catch (Exception e) {
			setLog(e,"editCompoundInSection");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void addReactionToResultsSection() {
		
		try {
			String expName = "ExpWithReactionInResults";
			createNewExperimentAndChangeVersion(expName);
	
			boolean created = getPageManager().getExperimentPageV2().addReactionToSection(RESULTS_SECTION_INDEX);
			assertTrue("Adding reaction to experiment results failed",created);
			
			//TODO - add the check inn recent results
			//getPageManager().getAdminPage().showRecentResults();
			//assertFalse("Reaction in recent results page should not be editable ",getPageManager().getRecentResultsPage().isReactionEditable());
			
			
		} catch (Exception e) {
			setLog(e,"addReactionToResultsSection");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"TODO"})//TODO - not working due to auto save
	public void addSampleWithoutStockAndEdit() {
		
		try {
		
			String name = "sampleWithoutStock";
			createNewExperimentAndChangeVersion(name);
			assertTrue("Stock in sample could not be edited.",getPageManager().getExperimentPageV2().editSample(PROCEDURE_SECTION_INDEX));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	@Test (groups = {"basic sanity"})
	public void addSampleWithGenericCollection() {
		
		try {
			String collectionName = buildUniqueName(LGConstants.GENERIC_COLLECTION_PREFIX);
			getPageManager().getAccountSettingPage().addGenericCollection(collectionName);
			createNewExperimentAndChangeVersion("sampleWithGenericCollection");
			String sampleName = buildUniqueName(LGConstants.SAMPLE_PREFIX);
			assertTrue(sampleName +" was not shown as expected after refresh.",getPageManager().getExperimentPageV2().addSampleWithGenericCollection(PROCEDURE_SECTION_INDEX,collectionName,sampleName));
			
			getPageManager().getGenericCollectionPage().deleteGenericCollection(collectionName);
			
		} catch (Exception e) {
			setLog(e,"addSampleWithGenericCollection");
			AssertJUnit.fail(e.getMessage());
		}

	}
	
	@Test (groups = {"basic sanity"} ,timeOut = 600000)
	public void uploadDifferentFilesAndAddToPage() {
		try {
			logger.info("Uploading excel and add to page");
			String attachmentToLoad = LGConstants.UPLOAD_XLS_TEST_FILENAME;
			createNewExperimentAndChangeVersion(null);
			getPageManager().getExperimentPageV2().uploadAttachmentToSection(DESCRIPTION_SECTION_INDEX,attachmentToLoad);
			boolean pageAdded = getPageManager().getExperimentPageV2().addToPage(DESCRIPTION_SECTION_INDEX,attachmentToLoad);
			AssertJUnit.assertTrue("The file '" + attachmentToLoad + "' was not added to page.",pageAdded);
			
			//check adding pdf file
			logger.info("Uploading pdf and add to page");
			attachmentToLoad = LGConstants.UPLOAD_PDF_TEST_FILENAME;			
			getPageManager().getExperimentPageV2().uploadAttachmentToSection(PROCEDURE_SECTION_INDEX,attachmentToLoad);
			pageAdded = getPageManager().getExperimentPageV2().addToPage(PROCEDURE_SECTION_INDEX,attachmentToLoad);
			AssertJUnit.assertTrue("The file '" + attachmentToLoad + "' was not added to page.",pageAdded);
			
		} catch (Exception e) {
			setLog(e,"uploadDifferentFilesAndAddToPage");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	
	@Test(groups = {"basic sanity"})
	public void startNewExperimentFromProtocolFromDropdown(){
		
		try {
			//create protocol
			String protocol = createNewProtocol();
			
			showTableIndex();
			checkCreateExpFromProtocolFromDropdown(protocol);
			
			//delete protocol after test
			deleteProtocolAfterTest(protocol);
		} catch (InterruptedException e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	
	@Test(groups = {"basic sanity"})
	public void startNewDocumentFromDropdown(){
		
		try {
			showTableIndex();			
			checkCreateDocumentFromDropdown();
			
		} catch (InterruptedException e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test(groups = {"basic sanity"})
	public void startNewProjectFromDropdown(){
		
		try {
			showTableIndex();	
			checkCreateProjectFromDropdown();
			
		} catch (InterruptedException e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	
	@Test(groups = {"basic sanity"})
	public void startNewProtocolFromDropdown(){
		
		try {
			showTableIndex();
			checkCreateProtocolFromDropDown();
		} catch (InterruptedException e) {
			setLog(e);
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
