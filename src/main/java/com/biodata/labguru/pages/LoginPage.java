package com.biodata.labguru.pages;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;

public class LoginPage extends BasePage{


	protected  void initPage(WebDriver webDriver) {
		
		PageFactory.initElements(webDriver, LoginPage.class);

	}
	public String signIn(String url,String user,String password) throws InterruptedException{

		getWebDriver().get(url);	
	    // enter user 
		WebElement txtUser = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login_text_field")));
		sendKeys(txtUser,user);
	    
	    // enter password
		WebElement txtPassword = driverWait.until(ExpectedConditions.visibilityOfElementLocated( By.id("password")));
		sendKeys(txtPassword,password);
	
	    // Now submit the form
		WebElement btnSignIn = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login_btn")));
	    btnSignIn.click();
	    
	    TimeUnit.SECONDS.sleep(2);
	    String title = getWebDriver().getTitle();
	    
	    //set the screen dimention to work well from jenkins (on ubuntu server using xvfb)
	    //getWebDriver().manage().window().setSize(new Dimension(1440,755));
	    //getWebDriver().manage().window().setSize(new Dimension(1280,800));
	    checkForNotyMessage();
	    return title;
	}
	
	public String signInWrongLogin(String url,String user,String password) throws InterruptedException{
		
		getWebDriver().get(url);	
	    // enter user 
		WebElement txtUser = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login_text_field")));
		txtUser.sendKeys(user);
	    
	    // enter password
		WebElement txtPassword = driverWait.until(ExpectedConditions.visibilityOfElementLocated( By.id("password")));
		txtPassword.sendKeys(password);
	
	    // Now submit the form
		WebElement btnSignIn = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login_btn")));
	    btnSignIn.click();
	    TimeUnit.SECONDS.sleep(1);

	    String msg = checkForNotyMessage();
		return msg;
	}



	public String createNewAccount(String url,String memberFName,String memberLName,String email,String password) throws InterruptedException {
		
		//getWebDriver().manage().window().setSize(new Dimension(1280,800));
		getLogger().info("before insertAccountData");
		insertAccountData(url, memberFName, memberLName, email,password);
		TimeUnit.SECONDS.sleep(3);
		getLogger().info("after insertAccountData" + Thread.currentThread().getId());
		String script = "$('#submit_signup_form').click();";
		executeJavascript(script);
		TimeUnit.SECONDS.sleep(8);
		 String name = (String) executeJavascript("return $('.user-name').text();");
	    return name;
	   
	}
	
	
	public boolean createNewAccountJoinExistLab(String url,String memberFName,String memberLName,String email) throws InterruptedException {
		
		boolean allDataIn = insertAccountData(url, memberFName, memberLName, email,LGConstants.STRONG_PASSWORD);
		if(!allDataIn)//not all data were insert correct
			return false;

		 TimeUnit.SECONDS.sleep(2);
		 String labName = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".labs_list>li:last-child"))).getText();
		 String script = "$(\".labs_list>li:last-child>a\").click();";
		 executeJavascript(script);
		 TimeUnit.SECONDS.sleep(3);
		 
		 //if joining to lab failed with a mssage - return false
		 String failedMsg = checkForNotyMessage();
		 if(!failedMsg.isEmpty())
			 return false;
		 
		 WebElement userName = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@class='sixteencol last']/div[2]/p[1]/b")));
		 String name = userName.getText();
	     String lab = labName.substring(labName.indexOf('+')+1,labName.indexOf("\n"));
	     //return true if the name of the lab to join is equal to the lab written in the opened page
	     return lab.equals(name.substring(name.indexOf(' ')+1));
	}

	private boolean insertAccountData(String url, String memberFName,String memberLName, String email,String password) throws InterruptedException {
		
		getWebDriver().get(url);
//		WebElement signin = driverWait.until(ExpectedConditions.visibilityOfElementLocated( By.cssSelector("#sign-in>p>a")));
//		signin.click(); 
//		TimeUnit.SECONDS.sleep(2);
		
		WebElement fname = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user_first_name")));
		fname.sendKeys(memberFName);
		
		WebElement lname = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user_last_name")));
		lname.sendKeys(memberLName);
		
	    WebElement emailAdd = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email_add")));
	    emailAdd.sendKeys(email);
	    
	    selectDropDownOption("role", "Other","1");	    
	    TimeUnit.SECONDS.sleep(2);
	    
	    selectDropDownOption("select_institution", "Biodata","2");
	    TimeUnit.SECONDS.sleep(2);
	    
	    // Find the text input element by its name
	    WebElement passwordInput = getWebDriver().findElement(By.id("password"));
	    passwordInput.sendKeys(password);

	    WebElement chkTerms =  getWebDriver().findElement(By.id("terms_of_use_check"));
	    chkTerms.click();
	    TimeUnit.SECONDS.sleep(1);
	 
	    // Now submit the form. WebDriver will find the form for us from the element
	    WebElement submitBtn = getWebDriver().findElement(By.id("submit_btn"));
	    submitBtn.click();
	    TimeUnit.SECONDS.sleep(1);
	    String alert = checkForAlerts();
	    if(!alert.isEmpty())
	    	return false;
	   
	    String script = "$('.button.get_started').click();";
		executeJavascript(script);
		
	    TimeUnit.SECONDS.sleep(2);
	    return true;
	}
	


	public String resetPassword(String newPassword, String user) throws InterruptedException {
		
		WebElement linkForgotPass = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='loginform']/div[4]/a[@href='/sessions/forgot_password']")));
		linkForgotPass.click();
		TimeUnit.SECONDS.sleep(1);
		
		WebElement txtAccount = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login_text_field")));
		txtAccount.clear();
		txtAccount.sendKeys(user);
		
		WebElement btnReset = getWebDriver().findElement(By.id("submit-btn"));
		btnReset.click();
		TimeUnit.SECONDS.sleep(1);
		checkForNotyMessage();
		
		return checkEmailResetPassRecieved(newPassword);
	}
	
	private String checkEmailResetPassRecieved(String newPassword) throws InterruptedException{

		loginToGmailAccount();
		
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@href='http://www.google.co.il/intl/en/options/']")));
		
		getWebDriver().get("https://mail.google.com/mail/?pli=1#inbox");
		TimeUnit.SECONDS.sleep(10);
		List<WebElement> mails = getWebDriver().findElements(By.xpath(".//span[@name='Labguru']"));
		for (WebElement mail : mails) {
			mail.click();
			TimeUnit.SECONDS.sleep(2);
			
			try {
				List<WebElement> buttons = getWebDriver().findElements(By.xpath(".//table/tbody/tr[1]/td/table/tbody/tr[3]/td/p[2]/a"));
				for (WebElement button : buttons) {
					if(button.getText().startsWith("RESET PASSWORD")){
						button.click();
						TimeUnit.SECONDS.sleep(1);
						switchToNewTab();
						WebElement txtPassword = getWebDriver().findElement(By.id("password"));
						txtPassword.sendKeys(newPassword);
						TimeUnit.SECONDS.sleep(1);
						WebElement txtConfirmPassword = getWebDriver().findElement(By.id("password_confirmation"));
						txtConfirmPassword.sendKeys(newPassword);
						TimeUnit.SECONDS.sleep(1);
						WebElement btn = getWebDriver().findElement(By.id("login_btn"));
						btn.click();
						TimeUnit.SECONDS.sleep(1);
						return checkForNotyMessage();
					}
				}
			} catch (Exception e) {
				//has no buttons - do nothing and continue to next mail
			}
		}

		return "Password not reset";
		
	}
	
	public void deleteMail() throws InterruptedException{
		
		loginToGmailAccount();
		
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@href='http://www.google.co.il/intl/en/options/']")));
		
		getWebDriver().get("https://mail.google.com/mail/?pli=1#inbox");
		TimeUnit.SECONDS.sleep(10);
		
		WebElement checkbox = getWebDriver().findElement(By.xpath(".//span[@role='checkbox']"));
		checkbox.click();
		TimeUnit.SECONDS.sleep(1);
		
		WebElement lblDelete = getWebDriver().findElement(By.xpath(".//div[@aria-label='Delete']"));
		lblDelete.click();
		TimeUnit.SECONDS.sleep(1);

	}

	public void loadPage(String urlToTest) {
		getWebDriver().get(urlToTest);
		driverWait.until(ExpectedConditions.titleContains("Welcome to Labguru"));
	}
}
