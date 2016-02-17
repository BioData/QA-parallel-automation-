package com.biodata.labguru.pages.knowledgebase;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;


public class AttachmentsPage extends AbstractKnowledgebasePage {

	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	public boolean hasList() {
		
		try{
			List<WebElement> list = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("attachments-list")));
			return list.size() > 0;
			
		}catch(Exception e){
			return false;
		}

	}
}
