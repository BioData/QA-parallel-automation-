package com.biodata.labguru.tests;

import static org.testng.AssertJUnit.assertTrue;

import java.util.Locale;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;

public class SecurityTextTest extends BaseTest{

	@Test (groups = {"deep"})
	public void changeUserNameWithScriptInTextNotSecured(){
		
		try {
			getPageManager().getAdminPage().selectMyProfileMenu();
			
			String name = getMessageSource().getMessage("script.msg.2",null, Locale.US);
			assertTrue(getMessageSource().getMessage("script.test.error.msg",null,Locale.US),
					getPageManager().getAdminPage().changeUserNameWithScriptInText(name));

		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void changeAccountNameWithScriptInTextNotSecured(){
		
		try {
			getPageManager().getAdminPage().selectAccountSettingMenu();
			
			String name = getMessageSource().getMessage("script.msg.2",null, Locale.US);
			assertTrue(getMessageSource().getMessage("script.test.error.msg",null,Locale.US),
					getPageManager().getAdminPage().changeAccountNameWithScriptInText(name));

		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	
	@Test (groups = {"deep"})
	public void addNewExperimentWithScriptInTextNotSecured(){
		
		try {
			getPageManager().getAdminPage().showRecentResults();
			
			String name = getMessageSource().getMessage("script.msg.1",null, Locale.US);
			assertTrue(getMessageSource().getMessage("script.test.error.msg",null,Locale.US),
					getPageManager().getExperimentPage().addNewExperimentWithScriptInName(name));

		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	
	@Test (groups = {"deep"})
	public void addTagWithScriptInTextNotSecured(){
		
		try {
			createProjectToTest();
			
			String tagName = getMessageSource().getMessage("script.msg.1",null, Locale.US);
			assertTrue(getMessageSource().getMessage("script.test.error.msg",null,Locale.US),
					getPageManager().getAdminPage().addTagWithScriptInName(tagName));
		
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	

	@Test (groups = {"deep"})
	public void addTaskWithScriptInTextNotSecured(){
		
		try {
			createProjectToTest();
			
			String taskName = getMessageSource().getMessage("script.msg.1",null, Locale.US);
			assertTrue(getMessageSource().getMessage("script.test.error.msg",null,Locale.US),
					getPageManager().getAdminPage().addTaskWithScriptInName(taskName));

		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	private void createProjectToTest() {
		
		try {
			
			getPageManager().getAdminPage().selectProjects();
			getPageManager().getProjectPage().addNewProjectUseDefault();
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}

}
