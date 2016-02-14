package com.biodata.labguru.tests;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;

public class AccountAdminTest extends BaseTest{
	
	
	@BeforeClass
	public void discardNotyMessages(){
		
		getPageManager().getAdminPage().discardNotyMessages();
	}
	
	@Test(groups = {"basic sanity"})
	public void canSelectSettings(){

		String pageTitle = getPageManager().getAdminPage().showAccountSettings();
				
        // Check the title of the page
		assertEquals(getMessageSource().getMessage("account.collection.settings.title",null, Locale.US), pageTitle);

	}
	
	@Test(groups = {"basic sanity"})
	public void canSelectCollectionsAndSettings(){

		String pageTitle = getPageManager().getAdminPage().showCollectionsAndSettings();
				
        // Check the title of the page
		AssertJUnit.assertEquals(getMessageSource().getMessage("account.collection.settings.title",null, Locale.US), pageTitle);

	}
	@Test(groups = {"fail"})
	public void addNewMember(){
		
		try {		
			int currentNumOfMembers = getPageManager().getAccountSettingPage().getNumOfMembersInPlan();
			
			getPageManager().getAdminPage().selectAccountMembersMenu();
			
			String memberName = buildUniqueName(LGConstants.BIODATA_MEMBER_NAME);
			String email = LGConstants.QA_PREFIX_MAIL + memberName + LGConstants.GMAIL_SUFFIX_MAIL;
			String notyMsg = getPageManager().getMembersPage().addNewMember(memberName, "test", email);
			if(getPageManager().getAccountSettingPage().hasNotReachedToMaxUsersInPlan(currentNumOfMembers))
				assertEquals(getMessageSource().getMessage("account.add.member.msg",null, Locale.US),notyMsg.trim());
			else
				assertEquals(getMessageSource().getMessage("account.add.member.increase.msg",null, Locale.US),notyMsg.trim());
		} catch (Exception e) {
			setLog(e,"addNewMember");
			Assert.fail(e.getMessage());
		}

	}
	
	@Test (groups = {"fail"})
	public void addExistMemberFailed(){
		
		try {
			getPageManager().getAdminPage().selectAccountMembersMenu();
			
			String memberName = buildUniqueName(LGConstants.BIODATA_MEMBER_NAME);
			String email = LGConstants.QA_PREFIX_MAIL + memberName + LGConstants.GMAIL_SUFFIX_MAIL;
			getPageManager().getMembersPage().addNewMember(memberName, "test", email);
			
			getPageManager().getAdminPage().selectAccountMembersMenu();
			
			String notiMsg = getPageManager().getMembersPage().addNewMember(memberName, "test", email);
			
			// Check the title noti message
			assertEquals(getMessageSource().getMessage("account.cannot.create.member.msg",null,Locale.US), notiMsg.trim());
		} catch (Exception e) {
			setLog(e,"addExistMemberFailed");
			Assert.fail(e.getMessage());
		}
	}

	@Test(groups = {"staging"})
	public void addPaymentMethod(){
		
		try {
			getPageManager().getAdminPage().selectAccountBillingMenu();
			
			String notiMsg = getPageManager().getAccountSettingPage().addPaymentMethod();
			
			// Check the title noti message
			assertEquals(getMessageSource().getMessage("account.billing.receive.msg",null,Locale.US), notiMsg.trim());
		} catch (Exception e) {
			setLog(e,"addPaymentMethod");
			Assert.fail(e.getMessage());
		}
	}

	
	@Test(groups = {"basic sanity"})
	public void updatePlan(){
		
		try {
			getPageManager().getAdminPage().selectAccountBillingMenu();
			
			int numOfMembers = getPageManager().getAccountSettingPage().getNumOfMembersInPlan();
			String notiMsg = getPageManager().getAccountSettingPage().updatePlan(numOfMembers);
			if(numOfMembers == 100){
				
				assertEquals(getMessageSource().getMessage("account.no.changes",null,Locale.US), notiMsg.trim());
				
			}else{
				
				assertEquals(getMessageSource().getMessage("account.update.msg",null,Locale.US), notiMsg.trim());
			}
		} catch (Exception e) {
			setLog(e,"updatePlan");
			Assert.fail(e.getMessage());
		}

	}
	
	@Test (groups ={"scarlet"})
	public void inviteFriendToLabguru(){

		try {
			String friend = LGConstants.QA_PREFIX_MAIL + "friend1" + LGConstants.GMAIL_SUFFIX_MAIL;
			String msg = getPageManager().getAccountSettingPage().inviteFriendToLabguru(friend);
			
			assertEquals(getMessageSource().getMessage("dashboard.invitation.msg", new Object[]{friend},Locale.US),msg);
			
			assertTrue(getPageManager().getAdminPage().checkIfEmailRecieved());
		} catch (Exception e) {
			setLog(e,"inviteFriendToLabguru");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups ={"scarlet"})
	public void inviteFriendToLabguruWrongPattern(){
		
		try {
			assertTrue(getPageManager().getAccountSettingPage().inviteFriendToLabguruWrongPattern());
		} catch (Exception e) {
			setLog(e,"inviteFriendToLabguruWrongPattern");
			Assert.fail(e.getMessage());
		}

	}
	
	@Test (groups = {"scarlet"})
	public void invite2FriendsToLabguru(){

		try {
			String friend1 = LGConstants.QA_PREFIX_MAIL + "friend1" + LGConstants.GMAIL_SUFFIX_MAIL;
			String friend2 = LGConstants.QA_PREFIX_MAIL + "friend2" + LGConstants.GMAIL_SUFFIX_MAIL;
			String msg = getPageManager().getAccountSettingPage().inviteFriendToLabguru(friend1 + ","+ friend2);
			
			assertEquals(getMessageSource().getMessage("dashboard.invitation.multi.msg", new Object[]{friend1,friend2},Locale.US),msg);
			
			assertTrue(getPageManager().getAdminPage().checkIfEmailRecieved());
		} catch (Exception e) {
			setLog(e,"invite2FriendsToLabguru");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void dataExport(){

		try {
			getPageManager().getAccountSettingPage().showAccountSettings();
			getPageManager().getAccountSettingPage().selectDataExportTab();
			
			
			String exportMsg = getPageManager().getAccountSettingPage().dataExport();
			
			//if export could not performed because one was done in the last 24 hhours - we get noty msg
			if(exportMsg.equals(getMessageSource().getMessage("account.data.export.not.done",null, Locale.US)))
				Assert.assertTrue(true);
			else //export should be generated
				Assert.assertTrue(exportMsg.equals(getMessageSource().getMessage("account.data.export.msg",null, Locale.US)));
			
		} catch (Exception e) {
			setLog(e,"dataExport");
			Assert.fail(e.getMessage());
		}
	}

}
