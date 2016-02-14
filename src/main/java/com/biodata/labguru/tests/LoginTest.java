package com.biodata.labguru.tests;

import static org.testng.AssertJUnit.assertEquals;

import java.util.Locale;

import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.GenericHelper;
import com.biodata.labguru.LGConstants;

@Listeners(TestOrderRandomizer.class)
public class LoginTest extends BaseTest{
	
	
	@Test (groups = {"special"})
	public void resetPassword() {
		
        try {
        	getPageManager().getAdminPage().logout();
        	String newPassword = GenericHelper.buildUniqueName("");
        	String msg = getPageManager().getLoginPage().resetPassword(newPassword,getUserToTest());
        	AssertJUnit.assertEquals(getMessageSource().getMessage("reset.password.succeeded", null,Locale.US), msg);
        	
        	//logout and login again with the new password
        	getPageManager().getAdminPage().logout();
        	String pageTitle = getPageManager().getLoginPage().signIn(urlToTest,userToTest,newPassword);
        	// Check the title of the page
        	assertEquals(getMessageSource().getMessage("login.recent.results", null, Locale.US), pageTitle);
        	//delete the mail when finish reset
        	getPageManager().getLoginPage().deleteMail();

		} catch (Exception e) {
			setLog(e,"resetPassword");
			AssertJUnit.fail(e.getMessage());
		}
    }
	
	@Test (groups = {"basic sanity"})
	public void signIn() {
		
        try {
			String pageTitle = getPageManager().getLoginPage().signIn(urlToTest,userToTest,passwordToTest);
			
			// Check the title of the page
			assertEquals(getMessageSource().getMessage("login.recent.results", null, Locale.US), pageTitle);

		} catch (Exception e) {
			setLog(e,"signIn");
			AssertJUnit.fail(e.getMessage());
		}
    }
	
	@Test (groups = {"basic sanity"})
	public void signInWrongLogin() {
		
        try {
			String pageTitle = getPageManager().getLoginPage().signInWrongLogin(urlToTest,userToTest,passwordToTest+"1");
			
			// Check the title of the page
			assertEquals(getMessageSource().getMessage("login.wrong.details", null, Locale.US), pageTitle);

		} catch (Exception e) {
			setLog(e,"signInWrongLogin");
			AssertJUnit.fail(e.getMessage());

		}
    }
	
	@Test (groups = {"basic sanity"})
	public void logout() {
		
        try {
        	
        	getPageManager().getLoginPage().signIn(urlToTest,userToTest,passwordToTest);
        	
	        String pageTitle = getPageManager().getAdminPage().logout();
	        
	        // Check the title of the page
	        AssertJUnit.assertEquals(getMessageSource().getMessage("login.welcome", null, Locale.US), pageTitle);
	
	        getPageManager().getLoginPage().signIn(urlToTest,userToTest,passwordToTest);
		
		} catch (Exception e) {
			setLog(e,"logout");
			AssertJUnit.fail(e.getMessage());
		}
    }
	
	@Test (groups = {"basic sanity"})
	public void createNewAccount() {
		
        try {	
    		String memberName = buildUniqueName(LGConstants.NEW_ACCOUNT_PREFIX);
			String email = memberName + LGConstants.GMAIL_SUFFIX_MAIL;
	
	        String pageTitle = getPageManager().getLoginPage().createNewAccount(urlToTest,memberName, "test", email,LGConstants.STRONG_PASSWORD);
	        
	        // Check the title of the page
	        AssertJUnit.assertEquals(memberName + " test" , pageTitle);

		} catch (Exception e) {
			setLog(e,"createNewAccount");
			AssertJUnit.fail(e.getMessage());

		}

    }
	
	@Test (groups = {"basic sanity"})
	public void createNewAccountJoinExistLab() {
		
        try {	
			String memberName = buildUniqueName(LGConstants.NEW_ACCOUNT_PREFIX);
			String email = memberName + LGConstants.GMAIL_SUFFIX_MAIL;
	        
	        // Check the title of the page
	        AssertJUnit.assertTrue( getPageManager().getLoginPage().createNewAccountJoinExistLab(urlToTest,memberName, "test", email));
			
		} catch (Exception e) {
			setLog(e,"createNewAccountJoinExistLab");
			AssertJUnit.fail(e.getMessage());
		}

    }
	
	@Test (groups = {"basic sanity"})
	public void createNewAccountOnProduction() {
		
        try {	
    		String memberName = buildUniqueName(LGConstants.NEW_ACCOUNT_PREFIX);
			String email = memberName + LGConstants.GMAIL_SUFFIX_MAIL;
	
			getPageManager().getLogger().info("Sign up in production\n email: " + email);
	        String pageTitle = getPageManager().getLoginPage().createNewAccount(getPageManager().getProductionUrl(),memberName, "production", email,LGConstants.STRONG_PASSWORD);
	        
	        // Check the title of the page
	        AssertJUnit.assertEquals(memberName + " production" , pageTitle);
	        
			pageTitle = getPageManager().getLoginPage().signIn(getPageManager().getProductionUrl(),email,LGConstants.STRONG_PASSWORD);
			
			// Check the title of the page
			assertEquals(getMessageSource().getMessage("login.recent.results", null, Locale.US), pageTitle);
	        

		} catch (Exception e) {
			setLog(e,"createNewAccount");
			AssertJUnit.fail(e.getMessage());

		}

    }
	
	@Test (groups = {"basic sanity"})
	public void createNewAccountNoUserShouldFailed() {
		
        try {	
			String memberName = buildUniqueName(LGConstants.NEW_ACCOUNT_PREFIX);
			String email = memberName + LGConstants.GMAIL_SUFFIX_MAIL;
	        
	        // Check the title of the page
	        AssertJUnit.assertFalse( getPageManager().getLoginPage().createNewAccountJoinExistLab(urlToTest,memberName, "", email));
			
		} catch (Exception e) {
			setLog(e,"createNewAccountNoUserShouldFailed");
			AssertJUnit.fail(e.getMessage());
		}

    }
}
