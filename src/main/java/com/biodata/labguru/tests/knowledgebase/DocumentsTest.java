package com.biodata.labguru.tests.knowledgebase;

import static org.testng.AssertJUnit.assertEquals;
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

@Listeners(TestOrderRandomizer.class)
public class DocumentsTest extends AbstractKnowledgebaseTest{
	
	@Test (groups = {"deep"})
	public void createAndUpdateDocument() {
		try {
			
			showTableIndex();
			addNewItem();
			
	        String currentDate = getCurrentDateFormatted(LGConstants.CALENDAR_FORMAT);
			
			String account = getPageManager().getAdminPage().getAccountName();
			String bottomMsg = getPageManager().getDocumentPage().updateContent();
			
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
		
		if(getPageManager().getDocumentPage().hasList()){
	        // Check the title of the page when we already have some document
			AssertJUnit.assertEquals(getMessageSource().getMessage("documents.title.has.documents", null, Locale.US), pageTitle);
		}else{
			//TODO - check that the right date is concatenate to it
			// Check the title of the page when we dont have any document 
			assertTrue(pageTitle.contains(getMessageSource().getMessage("documents.title.no.documents", null, Locale.US)));
		}

	}
	
	@Test (groups = {"basic sanity"})
	public void addNewDocument(){
		
		try {
			getPageManager().getAdminPage().showDocuments();
			
			String docName = buildUniqueName(LGConstants.DOCUMENT_PREFIX);
			getPageManager().getDocumentPage().addNewDocument(docName);

			getPageManager().getAdminPage().showDocuments();
			assertTrue(getPageManager().getDocumentPage().searchForItem(docName));
		} catch (Exception e) {
			setLog(e,"addNewDocument");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void archiveDocument() {
		try {	
			showTableIndex();
			TimeUnit.SECONDS.sleep(2);
			//add 2 documents to be able to archived
			addNewItem();
			showTableIndex();
			String resource = addNewItem();
			boolean archived = getPage().archiveKnowledgebaseItem();
			Assert.assertTrue(archived,"Item could not be archived.");
			showTableIndex();
			boolean found = getPage().checkInArchivedList(resource);
			Assert.assertTrue(found,"Item was not found in Archived list.");
		} catch (Exception e) {
			setLog(e,"deleteItem");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void activateDocumentFromNotyMessage(){
		
		try {
			showTableIndex();			
			addNewItem();
			
			String notyMsg = getPage().activateArchivedItemFromNotyMessage();
			
			AssertJUnit.assertEquals(getMessageSource().getMessage("documents.activated.msg",null, Locale.US),notyMsg);
		} catch (Exception e) {
			setLog(e,"activateDocumentFromNotyMessage");
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
		} catch (Exception e) {
			setLog(e,"signAndLock");
			AssertJUnit.fail(e.getMessage());
		}
	}
	

	@Override
	protected String showTableIndex() {
		
		return getPageManager().getAdminPage().showDocuments();
		
	}
	
	@Override
	protected String showModule() {
		return getPageManager().getAdminPage().showDocuments();
	}

	@Override
	protected String addNewItem() throws InterruptedException {
		
		String docName = buildUniqueName(LGConstants.DOCUMENT_PREFIX);
		getPageManager().getDocumentPage().addSimpleDocument(docName);
		return docName;
		
	}

	@Override
	protected AbstractKnowledgebasePage getPage() {
		
		return getPageManager().getDocumentPage();
	}
}
