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

	@Test (groups = {"basic sanity"})
	public abstract void showMenu();
	
	protected String createNewProtocol() throws InterruptedException {
		getPageManager().getAdminPage().showProtocols();
		String newProtocol = buildUniqueName(LGConstants.PROTOCOL_PREFIX);	
		getPageManager().getProtocolPage().addProtocolToAccount(newProtocol);
		return newProtocol;
	}
}
