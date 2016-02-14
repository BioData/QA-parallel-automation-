package com.biodata.labguru.tests.enotebook;

import static org.testng.AssertJUnit.assertTrue;

import org.testng.Assert;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.enotebook.AbstractNotebookPage;
import com.biodata.labguru.tests.AbstractLGTest;

public abstract class AbstractEnotebookTest extends AbstractLGTest{
	
	protected abstract AbstractNotebookPage getPage();
	
	
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
		getPageManager().getDocumentPage().saveDocument();
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
	
	


}
