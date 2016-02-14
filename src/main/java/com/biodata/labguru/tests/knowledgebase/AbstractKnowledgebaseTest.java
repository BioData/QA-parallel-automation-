package com.biodata.labguru.tests.knowledgebase;

import static org.testng.AssertJUnit.assertTrue;

import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.pages.knowledgebase.AbstractKnowledgebasePage;
import com.biodata.labguru.tests.AbstractLGTest;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public abstract class AbstractKnowledgebaseTest extends AbstractLGTest{

	protected abstract AbstractKnowledgebasePage getPage();
	
	protected abstract String showModule();
	
	@Override
	protected String showTableIndex() {
		String pageTitle = showModule();	
		closeIridizePopups();
		return pageTitle;
	}
	
	@Override
	public void duplicateItem(){
		//only relevant for documents and recipe
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
	
	@Test (groups = {"deep"})
	public void deleteKnowledgebaseItem() {
		try {	
			showTableIndex();
			TimeUnit.SECONDS.sleep(2);
			//add two items to be able to search in list after delete
			addNewItem();
			showTableIndex();
			String resource = addNewItem();
			getPage().deleteKnowledgebaseItem();
			boolean found = getPage().searchForItemInList(resource);
			Assert.assertFalse(found,"Item was not deleted.");
		} catch (Exception e) {
			setLog(e,"deleteItem");
			AssertJUnit.fail(e.getMessage());
		}
	}

}
