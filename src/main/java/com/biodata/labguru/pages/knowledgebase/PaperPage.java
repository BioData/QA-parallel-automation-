package com.biodata.labguru.pages.knowledgebase;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;


public class PaperPage extends AbstractKnowledgebasePage{


	public boolean hasList() {

		try {
			//no manual papers in the list
			WebElement txt = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".empty_note>a")));
			String lbl = txt.getText();
			return !lbl.equals("manually")  ;
			
		} catch (Exception e) {
			return true;//has manually papers in the list
		}
	}

	public void addNewPaper(String paperName) throws InterruptedException {
		
		TimeUnit.SECONDS.sleep(2);
		if(hasList()){
			//not first paper - look for button 'New paper'
			WebElement btnNewPaper = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new_paper")));
			btnNewPaper.click();
		}else{
			WebElement linkAddManually = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".empty_note>a")));
			linkAddManually.click();
		}
	
		TimeUnit.SECONDS.sleep(2);
		WebElement txtPaperName = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
		sendKeys(txtPaperName, paperName);
		
        WebElement btnSave = getWebDriver().findElement(By.id("Save"));
        btnSave.click();		
		
	}


	public String addPaperSaveAndNew(String paperName) throws InterruptedException {
		TimeUnit.SECONDS.sleep(2);
		if(hasList()){
			//not first paper - look for button 'New paper'
			WebElement btnNewPaper = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new_paper")));
			btnNewPaper.click();
		}else{
			WebElement linkAddManually = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".empty_note>a")));
			linkAddManually.click();
		}
	
		TimeUnit.SECONDS.sleep(2);
		WebElement txtPaperName = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
		sendKeys(txtPaperName, paperName);
		saveAndNew();
	     //wait for the noty message
        String msg = waitForNotyMessage(".noty_text");
        //check we are again in the new item page
        try {
			getWebDriver().findElement(By.xpath(".//*[@value='Save & New']"));
		} catch (NoSuchElementException e) {
			getLogger().debug("@@ missing button 'Save & New' - not the right page...");
		}
        return msg;
	}

}
