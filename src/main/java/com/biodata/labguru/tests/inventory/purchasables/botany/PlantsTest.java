package com.biodata.labguru.tests.inventory.purchasables.botany;

import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.inventory.CollectionPage;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public class PlantsTest extends AbstractBotanyTest{
	
	protected String getCreationPrefix(){
		return LGConstants.BOTANY_PLANT_CREATION_TITLE;
	}
	
	
	@Override
	public void showMenu(){

		try {
			String pageTitle = showTableIndex();
			
			if(getPage().hasList()){
			    // Check the title of the page when we already have some items
				AssertJUnit.assertEquals(getMessageSource().getMessage("botany.plants.title.has.plants",null, Locale.US), pageTitle);
				getPage().deleteAllItemsFromTable();
			}
			
			pageTitle = showTableIndex();
			// Check the title of the page when we does'nt have items
			AssertJUnit.assertEquals(getMessageSource().getMessage("botany.plants.title.no.plants",null, Locale.US), pageTitle);
			
		} catch (Exception e) {
			setLog(e);
			Assert.fail(e.getMessage());
		}

	}	
	
	@Override
	public String importBioCollection() throws InterruptedException {
		
		return getPage().importCollection();
	}

	@Override
	public String getCollectionImportDetails() {
		
		return "6 " + LGConstants.BOTANY_PLANT.toLowerCase();
	}

	
	@Override
	protected String showModule() {
		return getPage().showBotanyPlants();
	}
	
	@Override
	protected String getPrefix() {
		return LGConstants.BOTANY_PLANT_PREFIX;
	}


	@Override
	protected CollectionPage getPage() {
		
		return getPageManager().getBotanyPlantsPage();
	}

	@Override
	public String getTemplateImportDetails() {
		return "1 " + LGConstants.BOTANY_PLANT.toLowerCase();
	}

}
