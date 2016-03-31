package com.biodata.labguru.tests.inventory.rodents;

import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.inventory.CollectionPage;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public class StrainsTest extends AbstractRodentsTest{

	protected String getCreationPrefix(){
		return LGConstants.RODENTS_STRAIN_CREATION_TITLE;
	}
	
	@Test (enabled = false)
	public void deleteStockFromStocksTab(){
		//this test is not relevant for this module
	}
	
	@Test (enabled = false)
	public void markAsUsedStockFromStocksTab(){
		//this test is not relevant for this module
	}
	
	@Test (groups = {"deep"})
	public void addSpecimenFromStrain(){
		
		try {		
			showTableIndex();
			addNewItem();
			
			String specimenName = buildUniqueName(LGConstants.RODENT_SPECIMEN_PREFIX);
			String created = getPageManager().getRodentStrainsPage().addSpecimenFromStrain(specimenName,1,true);
			
			AssertJUnit.assertTrue(created.startsWith(specimenName));
			
		} catch (Exception e) {
			setLog(e,"addSpecimenFromStrain");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void addSpecimenFromStrainNoGenderFails(){
		
		try {		
			showTableIndex();
			addNewItem();
			
			String specimenName = buildUniqueName(LGConstants.RODENT_SPECIMEN_PREFIX);
			String msg = getPageManager().getRodentStrainsPage().addSpecimenFromStrain(specimenName,0,true);
			
			AssertJUnit.assertEquals(getMessageSource().getMessage("rodents.strains.add.specimen.no.gender.alert", null,Locale.US),msg);
			
		} catch (Exception e) {
			setLog(e,"addSpecimenFromStrainNoGenderFails");
			Assert.fail(e.getMessage());
		}
	}
	
	//@Test (groups = {"deep"})
	public void add101SpecimenFromStrainFails(){
		
		try {		
			showTableIndex();
			addNewItem();
			
			String specimenName = buildUniqueName(LGConstants.RODENT_SPECIMEN_PREFIX);
			String msg = getPageManager().getRodentStrainsPage().addSpecimenFromStrain(specimenName,101,true);
			
			AssertJUnit.assertEquals(getMessageSource().getMessage("rodents.strains.add.specimen.no.gender.alert", null,Locale.US),msg);
			
		} catch (Exception e) {
			setLog(e,"add101SpecimenFromStrainFails");
			Assert.fail(e.getMessage());
		}
	}

	
	@Override
	public void showMenu(){

		try {
			String pageTitle = showTableIndex();
			
			if(getPage().hasList()){
			    // Check the title of the page when we already have some items
				AssertJUnit.assertEquals(getMessageSource().getMessage("rodents.strains.title.has.strains",null, Locale.US), pageTitle);
			}else{
				// Check the title of the page when we does'nt have items
				AssertJUnit.assertEquals(getMessageSource().getMessage("rodents.strains.title.no.strains",null, Locale.US), pageTitle);
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
		
		return "1 " + LGConstants.RODENTS_STRAIN.toLowerCase();
	}
	
	@Override
	public String getTemplateImportDetails() {
		
		return "1 " + LGConstants.RODENTS_STRAIN.toLowerCase();
	}

	@Override
	protected String showModule() {
		return getPage().showRodentStrains();
		
	}

	@Override
	protected String getPrefix() {
		return LGConstants.RODENT_STRAIN_PREFIX;
	}

	@Override
	protected CollectionPage getPage() {
		
		return getPageManager().getRodentStrainsPage();
	}


}
