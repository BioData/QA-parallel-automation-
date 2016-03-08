package com.biodata.labguru.pages.knowledgebase;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.GenericHelper;


public class ImagePage extends AbstractKnowledgebasePage{

	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	public boolean hasList() {
		
		List<WebElement> list = getWebDriver().findElements(By.xpath(".//*[@id='images']/ul/li"));
		return list.size() > 0;

	}
	
	public boolean deleteImageFromShowPage() throws InterruptedException {
		
		editImageFromShowPage();
		String imageToDelete = getWebDriver().findElement(By.id("page-title")).getText();
		
	   	clickOnButton("delete-item");
	   	 checkForAlerts();
	   	invokeSearchItem(imageToDelete);
		
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("index-header")));
		return hasList();
	}	
	
	public boolean editImageFromShowPage() throws InterruptedException {
		
		String currentImageName = getWebDriver().findElement(By.id("page-title")).getText();
		String noSuffix = currentImageName.substring(0,currentImageName.indexOf("."));
		clickOnButton("edit_link");
		WebElement imageName = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("title")));
		imageName.clear();
		imageName.sendKeys(GenericHelper.buildUniqueName(noSuffix) + ".gif");
		writeInRedactor("description", "edited image description");
	   	save();
	   
		
		WebElement newName = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("page-title")));
		return !newName.getText().equals(currentImageName);
	}	

}
