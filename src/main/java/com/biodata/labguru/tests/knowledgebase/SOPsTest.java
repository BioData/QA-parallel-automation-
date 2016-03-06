package com.biodata.labguru.tests.knowledgebase;

import static org.testng.AssertJUnit.assertTrue;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.knowledgebase.AbstractKnowledgebasePage;
import com.biodata.labguru.tests.TestOrderRandomizer;

/**
 * Special test for Industrial account to test SOP's module.
 * Also test different texts in document (name of document:'Test for Goni' ) to check that redactor works ok.
 * @author goni
 *
 */
@Listeners(TestOrderRandomizer.class)
public class SOPsTest extends AbstractKnowledgebaseTest{

	public static final String TEST_DOCUMENT_FOR_REDACTOR = "Test for Goni";
	
	@Override
	@Test(enabled = false)
	public void duplicateItem(){
		throw new UnsupportedOperationException("This action is not supported by this module.");
	}
	
	
	@Override
	public void showMenu(){

		String pageTitle = showTableIndex();
		
		if(getPageManager().getSOPPage().hasList()){
	        // Check the title of the page when we already have some sops
			AssertJUnit.assertEquals(getMessageSource().getMessage("sops.title.has.sops", null, Locale.US), pageTitle);
		}else{
	
			// Check the title of the page when we dont have any sops (title is like 'SOP - 2016-01-04')
			assertTrue(pageTitle.startsWith(getMessageSource().getMessage("sops.title.no.sops", null, Locale.US)));
		}

	}
	
	@Test (groups = {"basic sanity"})
	public void addNewSOP(){
		
		try {
			showTableIndex();
			
			String name = buildUniqueName(LGConstants.SOP_PREFIX);
			getPageManager().getSOPPage().addEmptySOP(name);

			showTableIndex();
			assertTrue(getPage().searchForItem(name));
			
			//delete sop after test
			getPageManager().getSOPPage().openSOPPage(name);
			getPageManager().getSOPPage().deleteKnowledgebaseItem();
			
		} catch (Exception e) {
			setLog(e,"addNewSOP");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void addNewSOPWithProtocol(){
		
		try {
			//create protocol to select later
			getPageManager().getProtocolPage().showProtocols();
			
			String protocol = getPageManager().getProtocolPage().addProtocolToAccount(buildUniqueName(LGConstants.PROTOCOL_PREFIX));
		
			showTableIndex();
			
			String name = buildUniqueName(LGConstants.SOP_PREFIX);
			String addedProtocol = getPageManager().getSOPPage().addSOPWithProtocol(name,protocol);

			Assert.assertEquals(protocol, addedProtocol);
			
			//delete sop after test
			getPageManager().getSOPPage().openSOPPage(name);
			getPageManager().getSOPPage().deleteKnowledgebaseItem();
			
			//delete the protocol after test
			deleteProtocolAfterTest(protocol);
			
		} catch (Exception e) {
			setLog(e,"addNewSOPWithProtocol");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void archiveSOP() {
		try {	
			showTableIndex();
			TimeUnit.SECONDS.sleep(2);
			String resource = addNewItem();
			boolean archived = getPage().archiveKnowledgebaseItem();
			Assert.assertTrue(archived,"Item could not be archived.");
			showTableIndex();
			boolean found = getPage().checkInArchivedList(resource);
			Assert.assertTrue(found,"Item was not found in Archived list.");
		} catch (Exception e) {
			setLog(e,"archiveSOP");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void activateSOPFromNotyMessage(){
		
		try {
			showTableIndex();			
			String name = addNewItem();
			
			String notyMsg = getPage().activateArchivedItemFromNotyMessage();
			
			AssertJUnit.assertEquals(getMessageSource().getMessage("sops.activated.msg",null, Locale.US),notyMsg);
			
			//delete sop after test
			showTableIndex();
			getPageManager().getSOPPage().openSOPPage(name);
			getPageManager().getSOPPage().deleteKnowledgebaseItem();
		} catch (Exception e) {
			setLog(e,"activateSOPFromNotyMessage");
			AssertJUnit.fail(e.getMessage());
		}
		
	}
	
	@Test(groups = {"deep"})
	public void signAndLock(){
		
		try {
			showTableIndex();			
			addNewItem();
			
			String note = getPage().signAndLock();
			
			AssertJUnit.assertTrue(note.startsWith(getMessageSource().getMessage("signed.by.note.prefix",null, Locale.US)));
			
			//delete sop after test
			getPageManager().getSOPPage().revertSignature();
			getPageManager().getSOPPage().deleteKnowledgebaseItem();
		} catch (Exception e) {
			setLog(e,"signAndLock");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void startExperimentFromSOP(){
		
		try {
			//create protocol to select later
			getPageManager().getProtocolPage().showProtocols();
			String protocol = getPageManager().getProtocolPage().addProtocolToAccount(buildUniqueName(LGConstants.PROTOCOL_PREFIX));
			showTableIndex();
			
			String name = buildUniqueName(LGConstants.SOP_PREFIX);
			String addedProtocol = getPageManager().getSOPPage().addSOPWithProtocol(name,protocol);
			getPage().goBack();
			String linkedProtocol = getPageManager().getSOPPage().startExperimentFromSOP(addedProtocol);
			
			AssertJUnit.assertEquals(addedProtocol, linkedProtocol);
			
			//delete the protocol after test
			deleteProtocolAfterTest(protocol);
			
		} catch (Exception e) {
			setLog(e,"startExperimentFromSOP");
			AssertJUnit.fail(e.getMessage());
		}
		
	}

	
	@Test (groups = {"redactor"})
	public void checkTextInEditorNotDissapear(){
		
		try {
			getPageManager().getAdminPage().showDocuments();
			
			getPageManager().getDocumentPage().openDocument(TEST_DOCUMENT_FOR_REDACTOR);

			boolean succeeded = getPageManager().getDocumentPage().checkAllTagsInEditorNotDissapear();
			AssertJUnit.assertTrue(succeeded);
		} catch (Exception e) {
			setLog(e,"checkTextInEditorNotDissapear");
			AssertJUnit.fail(e.getMessage());
		}
	}

	
	
	@Override
	protected String showModule() {
		return getPageManager().getAdminPage().showSOPs();
	}

	@Override
	protected String addNewItem() throws InterruptedException {
		
		String name = buildUniqueName(LGConstants.SOP_PREFIX);
		getPageManager().getSOPPage().addEmptySOP(name);
		return name;	
	}

	@Override
	protected AbstractKnowledgebasePage getPage() {
		
		return getPageManager().getSOPPage();
	}
	
	private void deleteProtocolAfterTest(String protocol) throws InterruptedException {
		
		getPageManager().getProtocolPage().showProtocols();
		getPage().searchForItemInList(protocol);
		getPageManager().getProtocolPage().selectProtocol();
		getPageManager().getProtocolPage().deleteFromShowPage(protocol);
	}
}
