package com.biodata.labguru.tests.inventory.purchasables.sequenceable;

import static org.testng.AssertJUnit.assertEquals;

import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.inventory.CollectionPage;

public class SequencesTest extends SequenceableCollectionTest{

	@Override
	protected CollectionPage getPage() {
		return getPageManager().getSequencePage();
	}
	
	
	@Override
	public String getCollectionId() {
		return LGConstants.SEQUENCES;
	}
	
	@Override
	public void showMenu(){

		try {
			String pageTitle = showTableIndex();
			
			if(getPage().hasList()){
			    // Check the title of the page when we already have some items
				AssertJUnit.assertEquals(getMessageSource().getMessage("sequences.title.has.sequences",null, Locale.US), pageTitle);
				getPage().deleteAllItemsFromTable();
			}
			
			pageTitle = showTableIndex();
			// Check the title of the page when we does'nt have items
			AssertJUnit.assertEquals(getMessageSource().getMessage("sequences.title.no.sequences",null, Locale.US), pageTitle);
			
		} catch (Exception e) {
			setLog(e,"canSelectSequences");
			Assert.fail(e.getMessage());
		}

	}
	
	@Override
	public void addSimpleItem(){
		
		try {
			showTableIndex();
			
			String name = buildUniqueName(LGConstants.SEQUENCE_PREFIX);
			
			String msg = getPage().addNewItem(name);
			
			// Check the title of the page
			assertEquals(getMessageSource().getMessage("collection.created.msg",new Object[]{getCollectionNameForMessage()}, Locale.US), msg.trim());
		} catch (Exception e) {
			setLog(e,"addNewSequence");
			Assert.fail(e.getMessage());
		}
	}
	
	@Override
	protected String getCollectionNameForMessage() {
		return "Sequence";
	}

	@Override
	protected String addNewItem() throws InterruptedException {
		
		String name = buildUniqueName(LGConstants.SEQUENCE_PREFIX);
		getPage().addNewItem(name);
		return name;
	}
	
	@Override
	@Test (enabled = false)//no import for sequence.
	public void importCollection() {
		throw new UnsupportedOperationException("This action is not supported by this collection.");
	}
	
	@Override
	@Test (enabled = false)//no import for sequence.
	public void addCustomFieldAndGenerateCollectionTemplate(){
		// not implemented - no import for sequence
		throw new UnsupportedOperationException("This action is not supported by this collection.");	
	}
	
	@Override
	@Test (enabled = false)//no add sequence from tab in sequence.
	public void addNewSequenceFromSequencesTab(){
		// not implemented - no import for sequence
		throw new UnsupportedOperationException("This action is not supported by this collection.");
	}
	
	@Override
	public String getCollectionImportDetails() {
		// not implemented - no import for cages
		throw new UnsupportedOperationException("This action is not supported by this collection.");
	}
	
	@Override
	public String importBioCollection() throws InterruptedException {
		// not implemented - no import for cages
		throw new UnsupportedOperationException("This action is not supported by this collection.");
	}

	@Override
	public String getTemplateImportDetails() {
		// not implemented - no import for cages
		throw new UnsupportedOperationException("This action is not supported by this collection.");
	}


	@Override
	protected String getPrefix() {
		return LGConstants.SEQUENCE_PREFIX;
	}

}
