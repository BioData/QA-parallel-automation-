package com.biodata.labguru.tests.inventory.rodents;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.inventory.CollectionPage;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public class SpecimensTest extends AbstractRodentsTest{

	protected String getCreationPrefix(){
		return LGConstants.RODENTS_SPECIMEN_CREATION_TITLE;
	}

	
	@Override
	@Test (groups = {"basic sanity"})
	public void markAsConsumedStockFromStocksTab(){
		
		try {
			
			getPageManager().getAdminPage().showRodentSpecimens();
			String specimen = buildUniqueName(getPrefix());
			getPage().addNewItem(specimen);
			String tissueName = "tissue1";
			getPageManager().getRodentSpecimensPage().addTissueToSpecimen(tissueName);

			//add stock to the tissue	
			String type = LGConstants.STOCK_TYPES_ARRAY[0];
			String stockName = LGConstants.STOCK_PREFIX +  type;		
			getPageManager().getTissuePage().addStockFromStocksTab(stockName,type);
			
			//go to recently viewed specimen
			getPage().goToRecentlyViewed();
			
			getPageManager().getRodentSpecimensPage().selectTissuesAndSamplesTab();
	
			String notyMsg = getPage().markAsConsumedStockInTable(stockName);
			assertEquals(getMessageSource().getMessage("boxes.stock.marked.consumed.msg",new Object[]{"1"}, Locale.US), notyMsg);
			
			getPageManager().getAdminPage().showStocks();
			
			assertTrue("Simple search in consumed stocks did not find stock.",getPageManager().getStockPage().searchInConsumedStocks(stockName));
		
		} catch (Exception e) {
			setLog(e,"markAsConsumedStockFromStocksTab");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void addTissueToSpecimen(){
		
		try {		
		
			getPageManager().getAdminPage().showRodentSpecimens();
			String specimen = buildUniqueName(getPrefix());
			getPage().addNewItem(specimen);
			String tissueName = "tissue1";
			String pageTitle = getPageManager().getRodentSpecimensPage().addTissueToSpecimen(tissueName);
			AssertJUnit.assertEquals(pageTitle,specimen+ " - "+tissueName);
		} catch (Exception e) {
			setLog(e,"addTissueToSpecimen");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void addTissueWithStocksToSpecimen(){
		
		try {		
		
			getPageManager().getAdminPage().showRodentSpecimens();
			String specimen = buildUniqueName(getPrefix());
			getPage().addNewItem(specimen);
			String tissueName = "tissue1";
			getPageManager().getRodentSpecimensPage().addTissueToSpecimen(tissueName);
			
			int stockCount = 0;
			//add stocks to the tissue
			for (int i = 0; i < LGConstants.STOCK_TYPES_ARRAY.length; i++) {
				String type = LGConstants.STOCK_TYPES_ARRAY[i];
				String stockName = type + "_" + buildUniqueName(LGConstants.STOCK_PREFIX) ;
			
				getPageManager().getTissuePage().addStockFromStocksTab(stockName,type);
				stockCount++;
			}
			
			//open the specimen again and see that the stocks are shown
			getPageManager().getAdminPage().showRodentSpecimens();
			
			int createdStocks =getPageManager().getRodentSpecimensPage().openSpecimenAndCheckTissue(specimen);
			
			Assert.assertEquals(createdStocks, stockCount);
			
			
		} catch (Exception e) {
			setLog(e,"addTissueWithStocksToSpecimen");
			Assert.fail(e.getMessage());
		}
	}
	
	
	@Test (groups = {"knownBugs"})//LAB-1186
	public void addTissueWithStocksToSpecimenAndMarkItAsConsumed(){
		
		try {		
		
			getPageManager().getAdminPage().showRodentSpecimens();
			String specimen = buildUniqueName(getPrefix());
			getPage().addNewItem(specimen);
			String tissueName = "tissue1";
			getPageManager().getRodentSpecimensPage().addTissueToSpecimen(tissueName);
			
			//add stocks to the tissue
			String type = LGConstants.STOCK_TYPES_ARRAY[0];
			String stockName = type + "_" + buildUniqueName(LGConstants.STOCK_PREFIX) ;
		
			getPageManager().getTissuePage().addStockFromStocksTab(stockName,type);
			
			//go to recently viewed specimen
			getPage().goToRecentlyViewed();
			
			getPageManager().getRodentSpecimensPage().selectTissuesAndSamplesTab();
	
			String currentUrl = getPageManager().getWebDriver().getCurrentUrl();
			getPage().markAsConsumedStockInTable(stockName);
			
			Assert.assertEquals(getPageManager().getWebDriver().getCurrentUrl(), currentUrl);
			
			
		} catch (Exception e) {
			setLog(e,"addTissueWithStocksToSpecimenAndMarkItAsConsumed");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void addTissueToSpecimenCheckLink(){
		
		try {		
		
			getPageManager().getAdminPage().showRodentSpecimens();
			String specimen = buildUniqueName(getPrefix());
			getPage().addNewItem(specimen);
			
			String tissueName = getPageManager().getRodentSpecimensPage().addTissueToSpecimen("tissue1");

			//open the specimen again and go to the tissues tab
			getPageManager().getAdminPage().showRodentSpecimens();
			String tissuePageTitle = getPageManager().getRodentSpecimensPage().findTissueAndClick(specimen);

			Assert.assertEquals(tissuePageTitle, tissueName);
			
			
		} catch (Exception e) {
			setLog(e,"addTissueToSpecimenCheckLink");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void addTissueToSpecimenCheckSpecLinkInTissue(){
		
		try {		
		
			getPageManager().getAdminPage().showRodentSpecimens();
			String specimen = buildUniqueName(getPrefix());
			getPage().addNewItem(specimen);
			
			//add new tissue and open its show page
			getPageManager().getRodentSpecimensPage().addTissueToSpecimen("tissue1");

			//check that in tissue info there is a reffer link to the specimen that holds it
			String specimenLink = getPageManager().getTissuePage().checkSpecimenInfoAndClick();

			Assert.assertEquals(specimen, specimenLink);
			
			
		} catch (Exception e) {
			setLog(e,"addTissueToSpecimenCheckSpecLinkInTissue");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void setSpecimenWithDeathDate(){
		
		try {		
		
			boolean update = setDateOfDeathAndCheck(LGConstants.SPECIMEN_STATUS_ALIVE);
			AssertJUnit.assertTrue("Status should have been changed to " + LGConstants.SPECIMEN_STATUS_DEAD,update);

			update = setDateOfDeathAndCheck(LGConstants.SPECIMEN_STATUS_MISSING);
			AssertJUnit.assertTrue("Status should not change" ,update);

			
		} catch (Exception e) {
			setLog(e,"setSpecimenWithDeathDate");
			Assert.fail(e.getMessage());
		}
	}

	public boolean setDateOfDeathAndCheck(String currentStatus) throws InterruptedException {
		getPageManager().getAdminPage().showRodentSpecimens();
		String specimen = buildUniqueName(getPrefix());
		getPage().addNewItem(specimen);
		if(!currentStatus.equals(LGConstants.SPECIMEN_STATUS_ALIVE)){
			getPageManager().getRodentSpecimensPage().setStatus(currentStatus);
		}
		
		return getPageManager().getRodentSpecimensPage().checkSpecimenStatus(specimen,currentStatus);
		
	}
	
	@Test (groups = {"deep"})
	public void addTreatmentFromSpecimensTable(){
		
		try {		
			getPageManager().getAdminPage().showRodentSpecimens();
			if(!getPage().hasList()){
				addNewItem();
				showTableIndex();
			}

			boolean valid = getPageManager().getRodentSpecimensPage().addTreatmentFromSpecimensTable();
			
			AssertJUnit.assertTrue(valid);
			
		} catch (Exception e) {
			setLog(e,"addTreatmentFromSpecimensTable");
			Assert.fail(e.getMessage());
		}
	}

	@Override
	public void showMenu(){

		try {
			String pageTitle = showTableIndex();
			
			if(getPage().hasList()){
			    // Check the title of the page when we already have some items
				AssertJUnit.assertEquals(getMessageSource().getMessage("rodents.specimens.title.has.specimens",null, Locale.US), pageTitle);
			}else{
				// Check the title of the page when we does'nt have items
				AssertJUnit.assertEquals(getMessageSource().getMessage("rodents.specimens.title.no.specimens",null, Locale.US), pageTitle);
			}
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
		
		return "1 " + LGConstants.RODENTS_SPECIMEN.toLowerCase();
	}
	
	@Override
	public String getTemplateImportDetails() {
		
		return "1 " + LGConstants.RODENTS_SPECIMEN.toLowerCase();
	}

	@Override
	protected String showModule() {
		
		return getPage().showRodentSpecimens();
		
	}
	@Override
	protected String getPrefix() {
		return LGConstants.RODENT_SPECIMEN_PREFIX;
	}


	@Override
	protected CollectionPage getPage() {
		
		return getPageManager().getRodentSpecimensPage();
	}
}
