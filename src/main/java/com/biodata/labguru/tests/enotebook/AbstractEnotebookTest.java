package com.biodata.labguru.tests.enotebook;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.enotebook.AbstractNotebookPage;
import com.biodata.labguru.tests.AbstractLGTest;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public abstract class AbstractEnotebookTest extends AbstractLGTest{
	
	protected abstract AbstractNotebookPage getPage();
	
	protected static final String DESCRIPTION_SECTION_INDEX = "0";
	protected static final String PROCEDURE_SECTION_INDEX = "1";
	protected static final String SECOND_PROCEDURE_SECTION_INDEX = "2";
	
	protected static final String DESCRIPTION_SECTION_LABEL = "Description";
	protected static final String PROCEDURE_SECTION_LABEL = "Procedure";
	protected static final String CUSTOM_SECTION_LABEL = "Custom";
	public static final String RESULTS_SECTION_LABEL = "Results";
	public static final String CONCLUSIONS_SECTION_LABEL = "Conclusions";
	
	
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
    		
    		showTableIndex();
    		addNewItem();
			
			String linkedRes = getPage().addLinkedResource(LGConstants.EXPERIMENT);
			
			AssertJUnit.assertTrue("No resource was linked.",!linkedRes.equals(""));
		
		} catch (Exception e) {
			setLog(e,"addLinkedResource");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Override
	@Test (groups = {"basic sanity"})
	public void uploadFile() {
		try {
			String attachmentToLoad = LGConstants.UPLOAD_TXT_TEST_FILENAME;
			showTableIndex();
			String resource = addNewItem();
		
			getPage().uploadAttachmentToSection(DESCRIPTION_SECTION_INDEX,attachmentToLoad);
			String pageTitle = getPage().checkAttachment(resource,2,attachmentToLoad);
			AssertJUnit.assertEquals(attachmentToLoad,pageTitle);
			
		} catch (Exception e) {
			setLog(e,"uploadFile");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Override
	@Test (groups = {"basic sanity"})
	public void deleteAttachment() {
		try {
			
			showTableIndex();
			addNewItem();
			getPage().uploadAttachmentToSection(DESCRIPTION_SECTION_INDEX,LGConstants.UPLOAD_TXT_TEST_FILENAME);
			
			boolean deleted = getPage().deleteAttachmentContainer(DESCRIPTION_SECTION_INDEX);
			Assert.assertTrue(deleted,"Attachment could not be deleted.");
		} catch (Exception e) {
			setLog(e,"deleteAttachment");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Override
	@Test (groups = {"basic sanity"})
	public void uploadImage() {
		try {
			String attachmentToLoad = LGConstants.UPLOAD_IMAGE_TEST_FILENAME;
			showTableIndex();
			String resource = addNewItem();
			getPage().uploadAttachmentToSection(DESCRIPTION_SECTION_INDEX,attachmentToLoad);
			String pageTitle =  getPage().checkAttachment(resource, 1,attachmentToLoad);
			AssertJUnit.assertEquals(attachmentToLoad,pageTitle);
			
			
		} catch (Exception e) {
			setLog(e,"uploadImage");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	
	@Override
	@Test (groups = {"basic sanity"})
	public void addTag() {
		try {

			showTableIndex();
			addNewItem();
			
			String tagName = buildUniqueName(LGConstants.TAG_PREFIX);
			String tag = getPage().addInlineTag(tagName);
			
			assertEquals("Tag with name '" + tagName + "' was not craeted as should be.",tagName, tag);

			assertTrue(getPage().deleteTagFromInlineTags(tagName));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Override
	public void duplicateItem(){
		
		try {
			showTableIndex();

			String name = addNewItem();
			
			String duplicateItemName = getPage().duplicateItem();
			
			// Check the title of the page
			assertTrue(duplicateItemName.startsWith(name));
		} catch (Exception e) {
			setLog(e,"duplicateItem");
			Assert.fail(e.getMessage());
		}
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
		getPageManager().getProtocolPage().deleteFromShowPage();
	}


	public String createNewExperimentAndChangeVersion(String expName) throws InterruptedException {
		
		getPageManager().getAdminPage().selectExperiments();
		String name = getPageManager().getExperimentPage().addNewExperiment(expName);
		closeIridizePopups();
		return name;
	}
}
