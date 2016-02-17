package com.biodata.labguru.pages.knowledgebase;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;


public class ImagebankPage extends AbstractKnowledgebasePage{

	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	public boolean hasList() {
		
		List<WebElement> list = getWebDriver().findElements(By.id("image_attachments"));
		return list.size() > 0;

	}

}
