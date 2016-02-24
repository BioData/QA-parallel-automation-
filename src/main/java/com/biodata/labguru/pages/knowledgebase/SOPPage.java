package com.biodata.labguru.pages.knowledgebase;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SOPPage extends DocumentPage{

	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	
	public boolean hasList() {
		
		try {
			List<WebElement> list = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("documents")));
			return list.size() > 0;
		} catch (Exception e) {
			return false;
		}

	}

	public void addSOP(String name) throws InterruptedException {
		
		addEmptySOP(name);
		
	   	executeJavascript("document.getElementsByClassName('edit_me')[0].click();");
	   	TimeUnit.SECONDS.sleep(2);
		
		WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("knowledgebase_sop_title")));
		sendKeys(txtName, name);
		
		WebElement btnSave = getWebDriver().findElement(By.xpath(".//*[@id='knowledgebase_sop_submit_action']/input"));
        btnSave.click();
		
        TimeUnit.SECONDS.sleep(2);
     
		executeJavascript("document.getElementsByClassName('text load')[0].click();");
	   	TimeUnit.SECONDS.sleep(3);
	  
        saveAllItemsOnPage();
       
	}

	public void addEmptySOP(String name) throws InterruptedException {
		try{
			//wait for page to load
			TimeUnit.SECONDS.sleep(3);
			
			//if not first sop - look for button 'New SOP'
			WebElement btnNewSop = getWebDriver().findElement(By.id("new_sop"));
			btnNewSop.click();
			
			TimeUnit.SECONDS.sleep(2);
			
			executeJavascript("document.getElementsByClassName('edit_me')[0].click();");
			TimeUnit.SECONDS.sleep(2);
			
			WebElement txtName = getWebDriver().findElement(By.id("knowledgebase_sop_title"));
			txtName.click();
			txtName.clear();
			txtName.sendKeys(name);
			
			List<WebElement> saveBtnList = getWebDriver().findElements(By.xpath(".//*[@id='knowledgebase_sop_submit_action']/input"));
			for (WebElement btnSave : saveBtnList) {
				if(btnSave.isDisplayed())
					btnSave.click();
			} 
			
			writeInRedactor("element_data", name);

			TimeUnit.SECONDS.sleep(2);
			
			 saveAllItemsOnPage();
			
		}catch(NoSuchElementException ex){
			//first document -  it automaticaly open in new document page - do nothing
			
		}
		
	}
	
	public String addSOPWithProtocol(String name,String protocolName) throws InterruptedException {
		
		addEmptySOP(name);
		
	   	executeJavascript("document.getElementsByClassName('edit_me')[0].click();");
	   	TimeUnit.SECONDS.sleep(2);
		
		WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("knowledgebase_sop_title")));
		sendKeys(txtName, name);
		
		WebElement btnSave = getWebDriver().findElement(By.xpath(".//*[@id='knowledgebase_sop_submit_action']/input"));
        btnSave.click();
		
        TimeUnit.SECONDS.sleep(2);
        
        return addProtocol(protocolName);
		
	}

	private String addProtocol(String protocolName) throws InterruptedException {
		
       TimeUnit.SECONDS.sleep(2);
       
		executeJavascript("document.getElementsByClassName('grid load')[0].click();");
	   	TimeUnit.SECONDS.sleep(3);
	   	
	   	getWebDriver().findElement(By.xpath(".//*[@id='s2id_element_data']/a/span[2]/b")).click();
	   	TimeUnit.SECONDS.sleep(1);
	   	
	   	List<WebElement> protocols = getWebDriver().findElements(By.xpath(".//*[@id='select2-drop']/ul/li"));
	   	for (int i = 1; i <= protocols.size(); i++) {
	   		WebElement protocol = getWebDriver().findElement(By.xpath(".//*[@id='select2-drop']/ul/li[" + i + "]/div"));
	   		if(protocol.getText().equals(protocolName)){
	   			protocol.click();
	   			TimeUnit.SECONDS.sleep(1);
	   			break;
	   		}
		}
		//save the protocol
    	executeJavascript("document.getElementsByClassName('inline_submit')[1].click();");
		TimeUnit.SECONDS.sleep(2);
		refreshPage();
		
		WebElement linkToProtocol = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='element_data_input']/span/a")));
		linkToProtocol.click();  
		TimeUnit.SECONDS.sleep(2);
		
		WebElement title = getWebDriver().findElement(By.xpath(".//*[@id='knowledgebase_protocol_name_input']/span"));
		return title.getText();
	}

	public String updateContent() throws InterruptedException {
		
		return updateName("knowledgebase_sop_title",".//*[@id='knowledgebase_sop_submit_action']/input");
		
	}

	@Override
	public String getPageTitleXPath() {

		return ".//*[@id='knowledgebase_sop_title_input']/span";
	}

	public String startExperimentFromSOP(String addedProtocol) throws InterruptedException {
		
		WebElement startExprBtn = getWebDriver().findElement(By.xpath(".//*[@id='new_experiment_popup']/span"));
		startExprBtn.click();
		TimeUnit.SECONDS.sleep(1);
		
	    WebElement newExpDialog = getWebDriver().switchTo().activeElement();
        driverWait.until(ExpectedConditions.visibilityOf(newExpDialog));

        WebElement btnAdd = getWebDriver().findElement(By.id("create_new_experiment_button"));
        btnAdd.click();
        
        getWebDriver().switchTo().activeElement();
       			
		TimeUnit.SECONDS.sleep(2);

		List<WebElement> experiments = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
				(By.xpath(".//*[@class='experiment-sortable ui-sortable']/li")));
		
		for (int i = 1; i <= experiments.size(); i++) {
			
			WebElement exp = getWebDriver().findElement(By.xpath(".//*[@class='experiment-sortable ui-sortable']/li[" + i + "]/h3/a[2]"));
			//if it is the added protocol - click on it to see it links to the protocol
			if(exp.getText().equals(addedProtocol)){
				exp.click();
				TimeUnit.SECONDS.sleep(2);
				 return getWebDriver().getTitle();
			}
		}
		return "Protocol not found.";
	}

	public void openSOPPage(String itemToSearch) throws InterruptedException {
		
		showSOPs();
		
		WebElement txtSearch = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".searchtextbox")));
        sendKeys(txtSearch, itemToSearch);
        
        WebElement btnSearch = getWebDriver().findElement(By.xpath(".//*[@value='Search']"));
        btnSearch.click();
        TimeUnit.SECONDS.sleep(3);
         
		WebElement item = driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath(".//*[@id='documents']/div[2]/h4/a/strong")));
		
		item.click();

		driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath(".//*[@id='knowledgebase_sop_title_input']/span")));
		
	}
	
	/**
	 * This only applies for admin users or for the user that signed the sop
	 */
	public boolean revertSignature() throws InterruptedException {
		
		WebElement btnSign = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".sign_n_lock")));
		btnSign.click();
		
		TimeUnit.SECONDS.sleep(1);
		checkForAlerts();
		TimeUnit.SECONDS.sleep(2);
		btnSign = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".sign_n_lock")));
		
		if(btnSign.getText().equals("Sign"))
			return true;
		return false;
		
	}
}
