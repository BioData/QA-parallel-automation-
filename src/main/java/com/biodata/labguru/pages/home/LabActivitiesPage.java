package com.biodata.labguru.pages.home;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.pages.AdminPage;


public class LabActivitiesPage extends AdminPage {
	

	@Override
	protected  void initPage(WebDriver webDriver) {
		
		PageFactory.initElements(webDriver, LabActivitiesPage.class);

	}
	
	public String addWhiteBoardPost(String subject) throws InterruptedException{
		
		WebElement btnAddPost = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@href='/system/whiteboard_posts/new']")));
		btnAddPost.click();
		
	    TimeUnit.SECONDS.sleep(2);
		
		getWebDriver().switchTo().activeElement();
		
		WebElement txtSubject = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("subject")));
		
		txtSubject.sendKeys(subject);
		
		writeInRedactor("system_whiteboard_post_body",0, "This is a test message");
		
		
		WebElement btnPostMsg = getWebDriver().findElement(By.id("create_whiteboard_posts_submit"));
		btnPostMsg.click();
		TimeUnit.SECONDS.sleep(1);
        getWebDriver().switchTo().activeElement();
		
		TimeUnit.SECONDS.sleep(2);
		

		List<WebElement> posts = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@class='posts']/div")));
		TimeUnit.SECONDS.sleep(2);
		for (int i = 1; i<= posts.size();i++) {
			
			WebElement postedSubject = getWebDriver().findElement(By.xpath(".//*[@class='posts']/div["+ i+"]/p/a"));
			if(postedSubject.getText().equals(subject)){
				return postedSubject.getText();
			}
			
		}
		return "Not found";
	}

	public boolean deleteWhiteBoardPost(String postToDelete) throws InterruptedException {
		
		List<WebElement> posts = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@class='posts']/div")));
		
		for (int i = 1; i<= posts.size();i++) {
			WebElement postTitleElm = getWebDriver().findElement(By.xpath(".//*[@class='posts']/div["+ i+"]/p/a"));
			if(postTitleElm.getText().equals(postToDelete)){
				WebElement cross = getWebDriver().findElement(By.xpath(".//*[@class='posts']/div["+ i+"]/div/span[2]/a/i"));
				executeJavascript("arguments[0].click();",cross);
				TimeUnit.SECONDS.sleep(1);
				checkForAlerts();
				TimeUnit.SECONDS.sleep(2);
				return true;
			}
		}
		return false;
	}

	public String goToSpecificPost(String post) throws InterruptedException {
		
		List<WebElement> posts = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@class='posts']/div")));
		
		for (int i = 1; i<= posts.size();i++) {
			WebElement postTitleElm = getWebDriver().findElement(By.xpath(".//*[@class='posts']/div["+ i+"]/p/a"));
			if(postTitleElm.getText().equals(post)){
				postTitleElm.click();
				TimeUnit.SECONDS.sleep(1);
				WebElement title = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("wb-subject")));
				return title.getText();
			}
		}

		return "";
	}
}
