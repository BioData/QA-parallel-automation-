package com.biodata.labguru.pages.knowledgebase;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


public class ImagebankPage extends AbstractKnowledgebasePage{

	
	public boolean hasList() {
		
		List<WebElement> list = getWebDriver().findElements(By.id("image_attachments"));
		return list.size() > 0;

	}

}
