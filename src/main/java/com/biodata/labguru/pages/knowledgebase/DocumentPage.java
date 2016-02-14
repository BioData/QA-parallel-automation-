package com.biodata.labguru.pages.knowledgebase;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;


public class DocumentPage extends AbstractKnowledgebasePage {

	public boolean hasList() {

		try{
			driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@class = 'empty_note']")));
			return false;
		}catch(Exception e){
			return true;
		}

	}

	public void addNewDocument(String docName) throws InterruptedException {
		
		addSimpleDocument(docName);
				
	   	executeJavascript("document.getElementsByClassName('edit_me')[0].click();");
	   	TimeUnit.SECONDS.sleep(2);
		
		WebElement txtDocName = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("knowledgebase_document_title")));
		sendKeys(txtDocName, docName);
		
		WebElement btnSave = getWebDriver().findElement(By.xpath(".//*[@id='knowledgebase_document_submit_action']/input"));
        btnSave.click();
		
        TimeUnit.SECONDS.sleep(2);
     
		executeJavascript("document.getElementsByClassName('grid load')[0].click();");
	   	TimeUnit.SECONDS.sleep(3);
	  
        saveAllItemsOnPage();
       
	}

	protected void saveAllItemsOnPage() {
		List <WebElement> imgSaveList = getWebDriver().findElements(By.cssSelector(".fa.fa-check"));
        for (WebElement imgSave : imgSaveList) {
        	if(imgSave.isDisplayed())
        		imgSave.click();
        }
	}

	public void addSimpleDocument(String docName) throws InterruptedException {
		try{
			//wait for page to load
			TimeUnit.SECONDS.sleep(3);
			if(hasList()){
				getLogger().info("has list");
				//if not first document - look for button 'New document'
				clickOnButton("new_document");
				TimeUnit.SECONDS.sleep(2);
				getLogger().info("click new document");
			}
			driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("knowledgebase_document_title_input")));
			
			executeJavascript("document.getElementsByClassName('edit_me')[0].click();");
			TimeUnit.SECONDS.sleep(2);
			
			WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("knowledgebase_document_title")));
			txtName.click();
			txtName.clear();
			txtName.sendKeys(docName);
			
			List<WebElement> saveBtnList = getWebDriver().findElements(By.xpath(".//*[@id='knowledgebase_document_submit_action']/input"));
			for (WebElement btnSave : saveBtnList) {
				if(btnSave.isDisplayed()){
					btnSave.click();
					TimeUnit.SECONDS.sleep(1);
				}
			} 
			
			writeInRedactor("element_data", docName);

			TimeUnit.SECONDS.sleep(2);
			
			 saveAllItemsOnPage();
			
		}catch(NoSuchElementException ex){
			//first document -  it automaticaly open in new document page - do nothing
			
		}
		
	}



	public String updateContent() throws InterruptedException {
		
		return updateName("knowledgebase_document_title",".//*[@id='knowledgebase_document_submit_action']/input");
		
	}

	@Override
	public String getPageTitleXPath() {

		return ".//*[@id='knowledgebase_document_title_input']/span";
	}

	public void saveDocument() {
	
		WebElement saveDescription = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".fa.fa-check")));
		saveDescription.click();
		
	}


}
