package com.biodata.labguru.tests.inventory.rodents;

import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.tests.BaseTest;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public class TreatmentsTest  extends BaseTest{
	

	@BeforeClass(alwaysRun = true,dependsOnMethods = "initialize")
	public void initDataForTest(){
		
		try {
			//check the 'Rodent' collection
			getPageManager().getAdminPage().showCollectionsAndSettings();
			getPageManager().getAccountSettingPage().checkCollection(LGConstants.RODENTS);
			
			//first we need to add some specimens and put them in cage
			getPageManager().getAdminPage().showRodentCages();
			if(!getPageManager().getCagesPage().hasList())
				getPageManager().getCagesPage().addNewItem(buildUniqueName(LGConstants.CAGE_PREFIX));
			
			getPageManager().getAdminPage().showRodentSpecimens();
			if(!getPageManager().getRodentSpecimensPage().hasList())
				getPageManager().getRodentSpecimensPage().addNewItem(buildUniqueName(LGConstants.RODENT_SPECIMEN_PREFIX));
			
			
		} catch (Exception e) {
			setLog(e);
			Assert.fail(e.getMessage());
		}
	}
	
	
	
	@Test (groups = {"basic sanity"})
	public void canSelectTreatments(){

		try {		
			String pageTitle = getPageManager().getAdminPage().showRodentTreatments();
			AssertJUnit.assertEquals(getMessageSource().getMessage("rodents.treatments.title",null, Locale.US), pageTitle);
			
		} catch (Exception e) {
			setLog(e,"canSelectTreatments");
			Assert.fail(e.getMessage());
		}

	}
	
	@Test (groups = {"basic sanity"})
	public void addNewTreatmentWithProtocol(){
		
		try {
			
			//we add one protocol to be able to start treatment from protocol
			getPageManager().getProtocolPage().showProtocols();
			
			String protocol = 	getPageManager().getProtocolPage().addProtocolToAccount(buildUniqueName(LGConstants.PROTOCOL_PREFIX));
			
			getPageManager().getAdminPage().showRodentTreatments();

			String created = getPageManager().getTreatmentPage().addNewTreatmentWithProtocol(protocol);
			
			AssertJUnit.assertFalse("The created treatment was not created as expected.",created.equals(getMessageSource().getMessage("rodents.treatments.not.found",null,Locale.US)));
			
		} catch (Exception e) {
			setLog(e,"addNewTreatmentWithProtocol");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"basic sanity"})
	public void addNewTreatmentNoProtocol(){
		
		try {		
			getPageManager().getAdminPage().showRodentTreatments();
			String name = buildUniqueName(LGConstants.TREATMENT_PREFIX);
			
			String created = getPageManager().getTreatmentPage().addNewTreatmentWithGivenName(name,LGConstants.TODAY);
			
			AssertJUnit.assertEquals(name, created);
			
		} catch (Exception e) {
			setLog(e,"addNewTreatmentNoProtocol");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void addNewTreatmentForTomorrow(){
		
		try {
			
			addCageWithSpecimen();
			
			getPageManager().getAdminPage().showRodentTreatments();
			String name = buildUniqueName(LGConstants.TREATMENT_PREFIX);
			
			String created = getPageManager().getTreatmentPage().addNewTreatmentWithGivenName(name,LGConstants.TOMORROW);
			
			AssertJUnit.assertEquals(name, created);
			
		} catch (Exception e) {
			setLog(e,"addNewTreatmentForTomorrow");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void addPastTreatment(){
		
		try {
			
			getPageManager().getAdminPage().showRodentCages();
			if(getPageManager().getCagesPage().hasList())
				getPageManager().getCagesPage().deleteAllItemsFromTable();
			addCageWithSpecimen();
			
			getPageManager().getAdminPage().showRodentTreatments();
			String name = buildUniqueName(LGConstants.TREATMENT_PREFIX);
			
			String created = getPageManager().getTreatmentPage().addNewTreatmentWithGivenName(name,"");
			
			AssertJUnit.assertEquals(name, created);
			
		} catch (Exception e) {
			setLog(e,"addPastTreatment");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void createTreatmentNoSpecimenFailed(){
		
		try {	
			getPageManager().getAdminPage().showRodentTreatments();
			
			String msg = getPageManager().getTreatmentPage().createTreatmentNoSpecimenFailed(buildUniqueName(LGConstants.TREATMENT_PREFIX));
			AssertJUnit.assertEquals(getMessageSource().getMessage("rodents.treatments.no.specimen.msg",null, Locale.US), msg);
			
		} catch (Exception e) {
			setLog(e,"createTreatmentNoSpecimenFailed");
			Assert.fail(e.getMessage());
		}	
	}
	
	@Test (groups = {"deep"})
	public void addPastTreatmentCheckDetails(){
		
		try {	
			addCageWithSpecimen();
			
			getPageManager().getAdminPage().showRodentTreatments();
			String name = buildUniqueName(LGConstants.TREATMENT_PREFIX);
			
			getPageManager().getTreatmentPage().addNewTreatmentNoValidation(name,"");
			boolean valid = getPageManager().getTreatmentPage().validatePage(name,"");
			
			AssertJUnit.assertTrue(valid);
			
		} catch (Exception e) {
			setLog(e,"addPastTreatmentCheckDetails");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void addFutureTreatmentCheckDetails(){
		
		try {	
			addCageWithSpecimen();
			
			getPageManager().getAdminPage().showRodentTreatments();
			String name = buildUniqueName(LGConstants.TREATMENT_PREFIX);
			
			getPageManager().getTreatmentPage().addNewTreatmentNoValidation(name,LGConstants.TOMORROW);
			boolean valid = getPageManager().getTreatmentPage().validatePage(name,LGConstants.TOMORROW);
			
			AssertJUnit.assertTrue(valid);
			
		} catch (Exception e) {
			setLog(e,"addFutureTreatmentCheckDetails");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void addTodayTreatmentCheckDetails(){
		
		try {		
			addCageWithSpecimen();
			
			getPageManager().getAdminPage().showRodentTreatments();
			String name = buildUniqueName(LGConstants.TREATMENT_PREFIX);
			
			getPageManager().getTreatmentPage().addNewTreatmentNoValidation(name,LGConstants.TODAY);
			boolean valid = getPageManager().getTreatmentPage().validatePage(name,LGConstants.TODAY);
			
			AssertJUnit.assertTrue(valid);
			
		} catch (Exception e) {
			setLog(e,"addTodayTreatmentCheckDetails");
			Assert.fail(e.getMessage());
		}
	}

	@Test (groups = {"deep"})
	public void addTreatmentCheckInCageAndSpeciment(){
		
		try {		
			
			addCageWithSpecimen();
			
			getPageManager().getAdminPage().showRodentTreatments();
			String name = buildUniqueName(LGConstants.TREATMENT_PREFIX);
			
			getPageManager().getTreatmentPage().addNewTreatmentNoValidation(name,LGConstants.TODAY);
			boolean valid = getPageManager().getTreatmentPage().validatePage(name,LGConstants.TODAY);
			
			AssertJUnit.assertTrue(valid);
			
		} catch (Exception e) {
			setLog(e,"addTreatmentCheckInCageAndSpeciment");
			Assert.fail(e.getMessage());
		}
	}
	
	private String addCageWithSpecimen() throws InterruptedException {
		
		getPageManager().getAdminPage().showRodentCages();
		
		String cageName = buildUniqueName(LGConstants.CAGE_PREFIX);
		getPageManager().getCagesPage().addNewItem(cageName);
		
		String created = getPageManager().getCagesPage().addSpecimenFromCage(cageName);
		return created;
	}

}
