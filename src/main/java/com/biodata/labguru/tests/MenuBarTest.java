package com.biodata.labguru.tests;

import java.util.Locale;

import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(TestOrderRandomizer.class)
public class MenuBarTest extends BaseTest{
	
	
	/*****Tests the Knowledgebase menu****/
	@Test (groups = {"basic sanity"})
	public void canSelectAttachments(){

		String pageTitle = getPageManager().getAdminPage().showAttachments();
				
        // Check the title of the page
		AssertJUnit.assertEquals(getMessageSource().getMessage("attachments.title.no.attachments",null,Locale.US), pageTitle);

	}
	
	@Test (groups = {"basic sanity"})
	public void canSelectImageBank(){

		String pageTitle = getPageManager().getAdminPage().showImageBank();
				
        // Check the title of the page
		AssertJUnit.assertEquals(getMessageSource().getMessage("imagebank.title.no.images",null,Locale.US), pageTitle);

	}
}
