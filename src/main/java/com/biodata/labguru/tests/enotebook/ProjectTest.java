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
import com.biodata.labguru.LGDataProvider;
import com.biodata.labguru.tests.AbstractLGTest;
import com.biodata.labguru.tests.TestOrderRandomizer;
//TODO - after project will switch to new UI layout we need to extend again from AbstractEnotebookTest class
@Listeners(TestOrderRandomizer.class)
public class ProjectTest extends AbstractLGTest{
	
	
	@Override
	public void duplicateItem(){
		
		try {
			showTableIndex();

			String name = addNewItem();
			
			String duplicateItemName = getPageManager().getProjectPage().duplicateItem();	
			
			// Check the title of the page
			assertTrue(duplicateItemName.startsWith(name));
		} catch (Exception e) {
			setLog(e,"duplicateItem");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test(groups = {"deep"})
	public void shareProjectCheckPermissions(){
		
		try {

			showTableIndex();
			
			String project = buildUniqueName(LGConstants.PROJECT_PREFIX);	
			getPageManager().getProjectPage().addNewProject(project);
			String dataToInsert = "test sharedata";
			getPageManager().getProjectPage().addTableToProjectDescription(dataToInsert);
			boolean succeeded = getPageManager().getProjectPage().shareProjectCheckPermissions(dataToInsert);
			
			AssertJUnit.assertTrue(succeeded);
			getPageManager().getLoginPage().signIn(urlToTest,userToTest,passwordToTest);
			
		} catch (InterruptedException e) {
			setLog(e,"shareProjectCheckPermissions");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test(groups = {"deep"})
	public void duplicateFullProject(){
		
		try {
			showTableIndex();
			TimeUnit.SECONDS.sleep(2);
			String project = addNewItem();
			
			//add to description:text
			logger.debug("adding text");
			String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis non rutrum odio.";
			getPageManager().getProjectPage().addTextProjectDescription(text);
			getPageManager().getProjectPage().refreshPage();

			//add paper
			logger.debug("adding paper");
			String paper = getPageManager().getProjectPage().addPaperFromPapersTab();
			//add note
			logger.debug("adding note");
			String note = "Note1";
			getPageManager().getProjectPage().addNoteFromNotesTab(note);
			//add document
			logger.debug("adding document");
			String document = getPageManager().getProjectPage().addDocumentFromDocumentsTab(project);

			//duplicate project
			logger.debug("duplicate project");
			getPageManager().getProjectPage().refreshPage();
			getPageManager().getProjectPage().duplicateItem();	
		
			//check all project fetures appears
			String allCreated = getPageManager().getProjectPage().checkDuplicatedProject(text,paper,note,document);
			assertTrue(!allCreated.isEmpty());
		
		} catch (Exception e) {
			setLog(e,"duplicateFullProject");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test(groups = {"test"})
	public void startNewExperimentFromProtocolFromDropdown(){
		
		try {

			//create protocol
			String protocol = createNewProtocol();
			
			showTableIndex();
			checkCreateExpFromProtocolFromDropdown(protocol);
			
			//delete protocol after test
			deleteProtocolAfterTest(protocol);
			
		}catch (Exception e) {
			setLog(e,"startNewExperimentFromProtocolFromDropdown");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	
	@Test(groups = {"deep"})
	public void startNewDocumentFromDropdown(){
		
		try {
			showTableIndex();
			
			checkCreateDocumentFromDropdown();
			
		}catch (Exception e) {
			setLog(e,"startNewDocumentFromDropdown");
			AssertJUnit.fail(e.getMessage());
		}
	}


	
	@Test(groups = {"deep"})
	public void startNewProjectFromDropdown(){
		
		try {
			showTableIndex();
			
			checkCreateProjectFromDropdown();
			
		}catch (Exception e) {
			setLog(e,"startNewProjectFromDropdown");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	
	@Test(groups = {"deep"})
	public void startNewProtocolFromDropdown(){
		
		try {
			showTableIndex();
			
			checkCreateProtocolFromDropDown();
		}catch (Exception e) {
			setLog(e,"startNewProtocolFromDropdown");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test(groups = {"deep"})
	public void startNewExperimentFromProtocolFromDropdownInShowPage(){
		
		try {
			
			//create protocol
			String protocol = createNewProtocol();
			
			//create project
			showTableIndex();
			addNewItem();
	
			checkCreateExpFromProtocolFromDropdown(protocol);
			
			//delete protocol after test
			deleteProtocolAfterTest(protocol);
			
		}catch (Exception e) {
			setLog(e,"startNewExperimentFromProtocolFromDropdownInShowPage");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	


	@Test(groups = {"deep"})
	public void startNewDocumentFromDropdownInShowPage(){
		
		try {
			//create project
			showTableIndex();
			addNewItem();
			
			checkCreateDocumentFromDropdown();
		}catch (Exception e) {
			setLog(e,"startNewDocumentFromDropdownInShowPage");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test(groups = {"deep"})
	public void startNewProjectFromDropdownInShowPage(){
		
		try {
			//create project
			showTableIndex();
			addNewItem();
			
			checkCreateProjectFromDropdown();
			
		}catch (Exception e) {
			setLog(e,"startNewProjectFromDropdownInShowPage");
			AssertJUnit.fail(e.getMessage());
		}
	}


	
	
	@Test(groups = {"test"})
	public void startNewProtocolFromDropdownInShowPage(){
		
		try {
			//create project
			showTableIndex();
			addNewItem();
			
			checkCreateProtocolFromDropDown();
			
		} catch (Exception e) {
			setLog(e,"startNewProtocolFromDropdownInShowPage");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test(groups = {"deep"})
	public void checkNewExperimentFromDropdownInFolderShowPage(){
		
		try {			
			//create protocol
			String protocol = createNewProtocol();
			
			//create project
			showTableIndex();
			addNewItem();
			
			//create folder
			String folderToCreate = buildUniqueName(LGConstants.FOLDER_PREFIX);
			String folder = getPageManager().getProjectPage().addFolder(folderToCreate);
			assertEquals(folderToCreate, folder);
			logger.info("creating experiment from protocol");
			checkCreateExpFromProtocolFromDropdown(protocol);

			//back to the folder
			logger.info("back to folder");
			getPageManager().getProjectPage().goBack();
			//create protocol
			logger.info("creating protocol from dropdown");
			checkCreateProtocolFromDropDown();
			
			//back to the folder
			logger.info("back to folder");
			getPageManager().getProjectPage().goBack();
			//create document
			logger.info("creating project from dropdown");
			checkCreateProjectFromDropdown();
			
			//back to the folder
			logger.info("back to folder");
			getPageManager().getProjectPage().goBack();
			//create document
			logger.info("creating document from dropdown");
			checkCreateDocumentFromDropdown();

		} catch (Exception e) {
			setLog(e,"checkNewExperimentFromDropdownInFolderShowPage");
			AssertJUnit.fail(e.getMessage());
		}
	}
	

	
	@Test(groups = {"deep"})
	public void addFolderWithFigure(){
		
		try {
			showTableIndex();
	
			getPageManager().getProjectPage().addNewProjectUseDefault();
			String folderToCreate = buildUniqueName(LGConstants.FOLDER_PREFIX);
			getPageManager().getProjectPage().addFolder(folderToCreate);
			assertTrue("Figure was not added as expected.",  getPageManager().getProjectPage().addFigureToFolder());
			
		} catch (Exception e) {
			setLog(e,"addFolderWithFigure");
			AssertJUnit.fail(e.getMessage());
		}
	}

	@Override
	public void showMenu(){
		
		String pageTitle = showTableIndex();
		// Check the title of the page
		AssertJUnit.assertEquals(getMessageSource().getMessage("projects.research.projects",null, Locale.US), pageTitle);
	}

	
	@Test(groups = {"deep"})
	public void addTagWithSamePrefix(){
		
		try {
			showTableIndex();
			
			getPageManager().getProjectPage().addNewProjectUseDefault();
			
			String tagName = buildUniqueName(LGConstants.TAG_PREFIX);
			String tag = getPageManager().getProjectPage().addTagWithSamePrefix(tagName);
	
			AssertJUnit.assertTrue("Tag with name '" + tagName + "' was not craeted as should be.",tagName.startsWith(tag));
		
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	
	@Test(groups = {"basic sanity"})
	public void addProject(){
		
		try {
			showTableIndex();
			
			String projNameToCreate = buildUniqueName(LGConstants.PROJECT_PREFIX);
			String newProjectName = getPageManager().getProjectPage().addNewProject(projNameToCreate);
			
			// Check the project name
			assertEquals(projNameToCreate, newProjectName);
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
		
	}
	
	@Test(groups = {"basic sanity"})
	public void addFolder(){
		
		try {
			showTableIndex();
	
			getPageManager().getProjectPage().addNewProjectUseDefault();
			String folderToCreate = buildUniqueName(LGConstants.FOLDER_PREFIX);
			String newFolderName = getPageManager().getProjectPage().addFolder(folderToCreate);
			assertEquals(folderToCreate, newFolderName);
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	

	
	@Test(groups = {"deep"})
	public void addFolderFromPlanned(){
		
		try {
			showTableIndex();
	
			getPageManager().getProjectPage().addNewProjectUseDefault();
			String folderToCreate = "Folder from Planned";
			String newFolderName = getPageManager().getProjectPage().addFolderFromPlanned(folderToCreate);
			assertEquals(folderToCreate, newFolderName);
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test(groups = {"deep"})
	public void addFolderFromInProgress(){
		
		try {
			showTableIndex();
	
			getPageManager().getProjectPage().addNewProjectUseDefault();
			String folderToCreate = "Folder from In Progress";
			String newFolderName = getPageManager().getProjectPage().addFolderFromInProgress(folderToCreate);
			assertEquals(folderToCreate, newFolderName);
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test(groups = {"basic sanity"})
	public void addNewExperiment(){
		
		try {
			showTableIndex();
			
			String name = buildUniqueName(LGConstants.EXPERIMENT_PREFIX);
			String expTitle = getPageManager().getExperimentPage().addNewExperiment(name);
			
			// Check the title of the page
			assertEquals(name, expTitle);
		} catch (InterruptedException e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Override
	public void addTag(){
		
		try {
			showTableIndex();
			addNewItem();
			
			String tagName = buildUniqueName(LGConstants.TAG_PREFIX);
			String tag = getPageManager().getAdminPage().addTag(tagName);
			
			AssertJUnit.assertEquals("Tag with name '" + tagName + "' was not craeted as should be.",tagName, tag);
		
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Override
	@Test (groups = {"basic sanity"})
	public void addTask(){
		
		try {
			showTableIndex();
			addNewItem();
			
			String taskName = buildUniqueName(LGConstants.TASK_PREFIX);
			String task = getPageManager().getAdminPage().addTask(taskName);
			
			AssertJUnit.assertEquals("Task with name '" + taskName + "' was not craeted as should be.",taskName, task);
		
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	/**
	 * This test method is performed 3 times according to the data provider(3 types of files are loaded)
	 * @param fileName ,the file to upload
	 */
	@Test (groups = {"basic sanity"},dataProvider = "uploadFiles", dataProviderClass = LGDataProvider.class)
	public void uploadFile(String fileName) {
		try {
			
			showTableIndex();
			addNewItem();
			boolean attachmentExist = getPageManager().getProjectPage().uploadFileFromDataProvider(fileName);
			AssertJUnit.assertTrue("Attachment file: '" + fileName +"' was not found.",attachmentExist);
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Override
	@Test (groups = {"basic sanity"})
	public void uploadImage() {
		try {
			
			showTableIndex();
			String resource = addNewItem();
			String pageTitle = getPageManager().getAdminPage().uploadImage(resource);
			AssertJUnit.assertEquals(LGConstants.UPLOAD_IMAGE_TEST_FILENAME,pageTitle);
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Override
	@Test (groups = {"basic sanity"})
	public void addLinkedResource(){
		
		try {
			
			//before addResource i want to make sure there is at least one experiment as a link resource
			showTableIndex();
			getPageManager().getExperimentPage().addNewExperiment(buildUniqueName(LGConstants.EXPERIMENT_PREFIX));
			hasExperiments = true;
    		
    		
			showTableIndex();
			addNewItem();
			
			String linkedRes = getPageManager().getAdminPage().addLinkedResource(LGConstants.EXPERIMENT);
			
			AssertJUnit.assertTrue("No resource was linked.",!linkedRes.equals(""));
		
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void checkTwoDirectionsLinkedResource(){
		
		try {

			showTableIndex();
			String project1 = LGConstants.PROJECT_PREFIX + "1";
			String project2 = LGConstants.PROJECT_PREFIX + "2";
			getPageManager().getProjectPage().addNewProject(project1);
			showTableIndex();
			getPageManager().getProjectPage().addNewProject(project2);
			
			//link project1 to project2
			getPageManager().getAdminPage().addProjectAsLinkedResource(project1);
			
			//now project1 is open - check that it has link resoutce to project2
			String linkedRes = getPageManager().getProjectPage().checkLinkedResource(project2);
			
			AssertJUnit.assertEquals("2 direction link not working.",linkedRes,project2);
		
		} catch (Exception e) {
			setLog(e,"checkTwoDirectionsLinkedResource");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void archivedProject(){
		
		try {
			//add 2 projects to get the right message for the projects list if this is the first test
			showTableIndex();
			addNewItem();
			showTableIndex();
			String project = buildUniqueName(LGConstants.PROJECT_PREFIX);	
			getPageManager().getProjectPage().addNewProject(project);
			String notyMsg = getPageManager().getProjectPage().archivedProject();
			
			AssertJUnit.assertEquals(getMessageSource().getMessage("projects.archived.msg",null, Locale.US),notyMsg);
		} catch (Exception e) {
			setLog(e,"archivedProject");
			AssertJUnit.fail(e.getMessage());
		}
		
	}
	
	@Test (groups = {"deep"})
	public void activateProjectFromNotyMessage(){
		
		try {
			showTableIndex();
			
			String project = buildUniqueName(LGConstants.PROJECT_PREFIX);	
			getPageManager().getProjectPage().addNewProject(project);
			
			String notyMsg = getPageManager().getProjectPage().activateProjectFromNotyMessage();
			
			AssertJUnit.assertEquals(getMessageSource().getMessage("projects.activated.msg",null, Locale.US),notyMsg);
		} catch (Exception e) {
			setLog(e,"activateProjectFromNotyMessage");
			AssertJUnit.fail(e.getMessage());
		}
		
	}
	
	@Test (groups = {"deep"})
	public void activateProjectFromGreenMenu(){
		
		try {
			showTableIndex();
			
			String project = buildUniqueName(LGConstants.PROJECT_PREFIX);	
			getPageManager().getProjectPage().addNewProject(project);
			
			String notyMsg = getPageManager().getProjectPage().activateProjectFromGreenMenu();
			
			AssertJUnit.assertEquals(getMessageSource().getMessage("projects.activated.msg",null, Locale.US),notyMsg);
		} catch (Exception e) {
			setLog(e,"activateProjectFromGreenMenu");
			AssertJUnit.fail(e.getMessage());
		}
		
	}
	
	
	@Test (groups = {"deep"})
	public void addTableToProjectDescription() {
		
		try {
			showTableIndex();
			
			String project = "ProjectWithTable";
			getPageManager().getProjectPage().addNewProject(project);
			String dataToWrite = "test";
			assertTrue(getPageManager().getProjectPage().addTableToProjectDescription(dataToWrite));

			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	

	
	@Test (groups = {"knowBugs"})//will be fix when project will be in the new layout
	public void addReactionToProjectDescription() {
		
		try {
			showTableIndex();
			
			String project =  "ProjectWithReaction";
			getPageManager().getProjectPage().addNewProject(project);
			assertTrue("Adding reaction to project description ",getPageManager().getProjectPage().addReactionToProjectDescription());
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"knownBugs"})//will be fix when project will be in the new layout
	public void addCompoundToProjectDescription() {//only when compound option is enabled

		try {
			showTableIndex();
			
			String project = "ProjectWithCompound";	
			getPageManager().getProjectPage().addNewProject(project);

			assertTrue(getPageManager().getProjectPage().addCompoundToProjectDescription());

		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}	

	}
		
	@Test(groups = {"deep"})
	public void addTextToProjectDescription() {
		
		try {
			showTableIndex();
			
			String project =  "ProjectWithText";	
			getPageManager().getProjectPage().addNewProject(project);
			
			String descToTest = "Test text in experiment description";
			String desc = getPageManager().getProjectPage().addTextProjectDescription(descToTest);
			assertEquals(descToTest, desc);
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	

	@Test (groups = {"deep"})
	public void uploadImageAndAnnotate() {
		try {
			
			showTableIndex();
			String resource = addNewItem();
			getPageManager().getAdminPage().uploadImage(resource);
			
			assertTrue("The image could not be annotate.",getPageManager().getAdminPage().checkAnnotation());
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void createAndUpdateProject() {
		try {
			
			showTableIndex();
			addNewItem();
			
	        String currentDate = getCurrentDateFormatted(LGConstants.CALENDAR_FORMAT);
			
			String account = getPageManager().getAdminPage().getAccountName();
			String bottomMsg = getPageManager().getProjectPage().updateContent();
			
			assertEquals(getMessageSource().getMessage("project.update.footer.msg",new Object[]{currentDate,account},Locale.US),bottomMsg);
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void addNoteFromNotesTab() {
		try {
			
			showTableIndex();
			addNewItem();
			String note = buildUniqueName("Note_");
			boolean added = getPageManager().getProjectPage().addNoteFromNotesTab(note);
			
			assertTrue("Note was not added as should be.",added);
			
		} catch (Exception e) {
			setLog(e,"addNoteFromNotesTab");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void addPaperFromPapersTab() {
		try {
			
			showTableIndex();
			addNewItem();
			
			String addedPaper = getPageManager().getProjectPage().addPaperFromPapersTab();
			
			assertTrue("Paper was not added as should be.",!addedPaper.isEmpty());
			
		} catch (Exception e) {
			setLog(e,"addPaperFromPapersTab");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"test"})
	public void addDocumentFromDocumentsTab() {
		try {
			
			showTableIndex();
			String newProject = addNewItem();
			
			String addedDoc = getPageManager().getProjectPage().addDocumentFromDocumentsTab(newProject);
			
			assertTrue("Document was not added as should be.",!addedDoc.isEmpty());
			
		} catch (Exception e) {
			setLog(e,"addDocumentFromDocumentsTab");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Override
	protected String showModule() {
		return getPageManager().getAdminPage().selectProjects();
	}

	@Override
	protected String addNewItem() throws InterruptedException {
		String projectName = buildUniqueName(LGConstants.PROJECT_PREFIX);
		return getPageManager().getProjectPage().addNewProject(projectName);
		
	}
	
	
	protected void checkCreateExpFromProtocolFromDropdown(String protocol) throws InterruptedException {
		
		getPageManager().getAdminPage().selectFromExperimentDropdown(LGConstants.NEW_EXP_FROM_PROTOCOL);
		getPageManager().getExperimentPage().createExperimentFromSelectedProtocol(protocol);
		// Check that the protocol is linked to this experiment
		boolean linked = getPageManager().getExperimentPage().checkLinkedResources(protocol);
		assertTrue(linked);
	}
	
	protected void checkCreateDocumentFromDropdown() throws InterruptedException {
		
		getPageManager().getAdminPage().selectFromExperimentDropdown(LGConstants.NEW_DOCUMENT);
		String expTitle = getPageManager().getDocumentPage().getTitle();
		// Check the title of the page
		assertTrue(expTitle.startsWith("My document"));
		getPageManager().getDocumentPage().saveTextBoxIO();
	}
	
	protected String createNewProtocol() throws InterruptedException {
		
		getPageManager().getAdminPage().showProtocols();
		String newProtocol = buildUniqueName(LGConstants.PROTOCOL_PREFIX);	
		getPageManager().getProtocolPage().addProtocolToAccount(newProtocol);
		return newProtocol;
	}
	
	protected void checkCreateProjectFromDropdown() throws InterruptedException {
		
		getPageManager().getAdminPage().selectFromExperimentDropdown(LGConstants.NEW_PROJECT);
		String title = getPageManager().getProjectPage().getTitle();
		// Check the title of the page
		assertTrue(title.startsWith("My project"));
	}

	protected void checkCreateProtocolFromDropDown() throws InterruptedException {

		//check create protocol
		getPageManager().getAdminPage().selectFromExperimentDropdown(LGConstants.NEW_PROTOCOL);
		String title = getPageManager().getProtocolPage().getTitle();
		// Check the title of the page
		assertTrue(title.startsWith("My protocol"));
	}
	
	protected void deleteProtocolAfterTest(String protocol) throws InterruptedException {
		//delete the created protocol
		getPageManager().getAdminPage().showProtocols();
		getPageManager().getProtocolPage().openProtocol(protocol);
		getPageManager().getProtocolPage().deleteProtocol();
	}
}
