package com.biodata.labguru.tests.inventory.purchasables.sequenceable;

import static org.testng.AssertJUnit.assertEquals;

import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.inventory.CollectionPage;

public class GenesTest extends SequenceableCollectionTest{
	
	@Override
	@Test (groups = {"knownBugs"})
	public void addNewItemWithSequence(){
		super.addNewItemWithSequence();
	}
	
	@Override
	@Test (enabled = false)
	public void duplicateItem(){
		throw new UnsupportedOperationException("This action is not supported by this collection.");
	}
	
	@Override
	protected CollectionPage getPage() {
		return getPageManager().getGenePage();
	}
	

	
	@Override
	public String getCollectionId() {
		return LGConstants.GENES;
	}
	
	@Override
	public void showMenu(){

		try {
			String pageTitle = showTableIndex();
			
			if(getPage().hasList()){
			    // Check the title of the page when we already have some items
				AssertJUnit.assertEquals(getMessageSource().getMessage("genes.title.has.genes",null, Locale.US), pageTitle);
				getPage().deleteAllItemsFromTable();
			}
			
			pageTitle = showTableIndex();
			// Check the title of the page when we does'nt have items
			AssertJUnit.assertEquals(getMessageSource().getMessage("genes.title.no.genes",null, Locale.US), pageTitle);
			
		} catch (Exception e) {
			setLog(e,"canSelectGene");
			Assert.fail(e.getMessage());
		}

	}
	
	@Override
	public void addSimpleItem(){
		
		try {
			showTableIndex();
			
			String name = buildUniqueName(LGConstants.GENE_PREFIX);
			
			String msg = getPage().addNewItem(name);
			
			// Check the title of the page
			assertEquals(getMessageSource().getMessage("collection.created.msg",new Object[]{getCollectionNameForMessage()}, Locale.US), msg.trim());
		} catch (Exception e) {
			setLog(e,"addNewGene");
			Assert.fail(e.getMessage());
		}
	}
	
	@Override
	protected String getCollectionNameForMessage() {
		return "Gene";
	}
	
	@Override
	public String importBioCollection() throws InterruptedException {
		return getPage().importCollection();		
	}

	@Override
	protected String showTableIndex() {
		return getPage().showCollection(getCollectionId());
		
	}
	
	@Override
	protected String showModule() {
		return getPageManager().getAdminPage().showAntibodies();
	}

	@Override
	protected String addNewItem() throws InterruptedException {
		
		String name = buildUniqueName(LGConstants.GENE_PREFIX);
		getPage().addNewItem(name);
		return name;
	}
	
	@Override
	public String getCollectionImportDetails() {
		
		return "2 " + LGConstants.GENES.toLowerCase();
	}
	
	@Override
	public String getTemplateImportDetails() {
		return "1 " + LGConstants.GENES.toLowerCase();
	}
	
	@Override
	protected String getPrefix() {
		return LGConstants.GENE_PREFIX;
	}

}
