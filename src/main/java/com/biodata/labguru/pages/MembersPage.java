package com.biodata.labguru.pages;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;


public class MembersPage extends BasePage{
	
	
	private By btnAddMemberLocator = By.id("add_member");
	
	public String addNewMember(String memberFName,String memberLName,String email) throws InterruptedException{
	
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(btnAddMemberLocator)).click();

        driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#new-member>h1")));
        
        WebElement txtFName = getWebDriver().findElement(By.id("first_name"));
        sendKeys(txtFName, memberFName);
        
        WebElement txtLName = getWebDriver().findElement(By.id("last_name"));
        sendKeys(txtLName, memberLName);
        
        WebElement txtEmail = getWebDriver().findElement(By.id("email"));
        sendKeys(txtEmail, email);
        
        
        
        WebElement btnSave = getWebDriver().findElement(By.id("Save"));
        btnSave.click();
        TimeUnit.SECONDS.sleep(1);
        
		String msg = checkForNotyMessage();
		return msg;
	}

	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}

}
