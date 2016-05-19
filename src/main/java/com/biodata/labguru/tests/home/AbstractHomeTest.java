package com.biodata.labguru.tests.home;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.tests.BaseTest;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public abstract class AbstractHomeTest extends BaseTest{

	
	@BeforeClass (alwaysRun = true , dependsOnMethods = "initialize")
	public void discardNotyMessages(){
		
		getPageManager().getAdminPage().discardNotyMessages();
	}
	
	
	//TODO - remove this after switching to beta version 
	@BeforeClass(alwaysRun = true , dependsOnMethods = "initialize")
	public void switchToBetaVersion(){
		try {
			//before starting i want to make sure there is at least one experiment in beta versuion
    		if(!getPageManager().getExperimentPage().hasList() ){
    			getPageManager().getExperimentPage().addNewExperiment("First Experiment");
    		}
		} catch (Exception e) {
			setLog(e,"switchToBetaVersion");
		}
	}

	@Test (groups = {"basic sanity"})
	public abstract void showMenu();
	
	protected String createNewProtocol() throws InterruptedException {
		getPageManager().getAdminPage().showProtocols();
		String newProtocol = buildUniqueName(LGConstants.PROTOCOL_PREFIX);	
		getPageManager().getProtocolPage().addProtocolToAccount(newProtocol);
		return newProtocol;
	}
	
	protected void deleteProtocolAfterTest(String protocol) throws InterruptedException {
		//delete the created protocol
		getPageManager().getAdminPage().showProtocols();
		getPageManager().getProtocolPage().openProtocol(protocol);
		getPageManager().getProtocolPage().deleteProtocol();
	}

}
