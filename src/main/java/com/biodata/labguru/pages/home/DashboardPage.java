package com.biodata.labguru.pages.home;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.pages.AdminPage;


public class DashboardPage extends AdminPage{
	
	@Override
	protected  void initPage(WebDriver webDriver) {
		
		PageFactory.initElements(webDriver, DashboardPage.class);

	}

	public String inviteMembers(String member) {

		WebElement textEmail = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email-text-field")));
		textEmail.sendKeys(member);
		
		WebElement btnSubmit = driverWait.until(ExpectedConditions.elementToBeClickable(By.id("submit-btn")));
		btnSubmit.submit();

		
		String msg = checkForNotyMessage(By.cssSelector(".noty_message"));
		return msg;
	
	}
}
