package com.biodata.labguru.tests.knowledgebase;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Locale;

import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.knowledgebase.AbstractKnowledgebasePage;
import com.biodata.labguru.tests.TestOrderRandomizer;


@Listeners(TestOrderRandomizer.class)
public class PapersTest extends AbstractKnowledgebaseTest{

	@Override
	@Test (enabled = false)
	public void duplicateItem(){
		// not implemented
		throw new UnsupportedOperationException("This action is not supported by this module");
	}
	
	
	@Override
	public void showMenu(){

		String pageTitle = showTableIndex();
		
		if(getPageManager().getPaperPage().hasList()){
	        // Check the title of the page when we already have some papers
			AssertJUnit.assertEquals(getMessageSource().getMessage("papers.title.has.papers", null, Locale.US), pageTitle);
		}else{
			// Check the title of the page when we dont have papers
			AssertJUnit.assertEquals(getMessageSource().getMessage("papers.title.no.papers", null, Locale.US), pageTitle);
		}
	}
	
	@Test (groups = {"deep"})
	public void addNewPaper(){
		
		try {
			getPageManager().getAdminPage().showPapers();
			
			String paperName = buildUniqueName(LGConstants.PAPER_PREFIX);
			getPageManager().getPaperPage().addNewPaper(paperName);

			getPageManager().getAdminPage().showPapers();
			assertTrue(getPageManager().getPaperPage().searchForItem(paperName));
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void addPaperSaveAndNew(){
		
		try {
			getPageManager().getAdminPage().showPapers();
			
			String paperName = buildUniqueName(LGConstants.PAPER_PREFIX);
			
			String msg = getPageManager().getPaperPage().addPaperSaveAndNew(paperName);
			
			// Check the title of the page
			assertEquals(getMessageSource().getMessage("papers.added.msg",null, Locale.US), msg);		
		
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	
	@Override
	public void addLinkedResource(){
		
		try {
			getPageManager().getAdminPage().showPapers();
			
			String paperName = buildUniqueName(LGConstants.PAPER_PREFIX);
			getPageManager().getPaperPage().addNewPaper(paperName);

			super.addLinkedResource();
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Override
	@Test(enabled=false)
	public void addTask() {
		// empty implementation
	}


	@Override
	protected AbstractKnowledgebasePage getPage() {
		
		return getPageManager().getPaperPage();
	}


	@Override
	protected String showModule() {
		return getPageManager().getAdminPage().showPapers();
	}

	@Override
	protected String addNewItem() throws InterruptedException {
		
		String paperName = buildUniqueName(LGConstants.PAPER_PREFIX);
		getPageManager().getPaperPage().addNewPaper(paperName);
		return paperName;
	}

}
