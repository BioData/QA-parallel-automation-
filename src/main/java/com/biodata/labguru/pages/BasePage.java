package com.biodata.labguru.pages;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.biodata.labguru.LGConstants;


public abstract class BasePage {
		
	private WebDriver wdriver;
	 
	protected  WebDriverWait driverWait;
	
	private Logger logger;
	
	public Logger getLogger() {
		return logger;
	}


	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	private String browserType;
	
	public static final long STANDART_PAGE_LOAD_WAIT_TIME = 80;
	
	protected static String workingDir = System.getProperty("user.dir");
	

	protected abstract void initPage(WebDriver webDriver) ;


   public WebDriver getWebDriver() {
    	return wdriver;
	}  

	public String getBrowserType(){
		return browserType;
	}
	
   public void setWebDriver(WebDriver driver) {
    	wdriver = driver;
    	driverWait = new WebDriverWait(wdriver, STANDART_PAGE_LOAD_WAIT_TIME);
	}
   

	public void setBrowserType(String browser){
		browserType = browser;
	}

	public void goBack() throws InterruptedException {
		getWebDriver().navigate().back();
		TimeUnit.SECONDS.sleep(3);
	}
	
	public void sendKeys(WebElement txtElement ,String keysToSend){
		txtElement.clear();
		txtElement.sendKeys(keysToSend);
	}

	
	public WebElement find(By locator){
		return getWebDriver().findElement(locator);
	}
	
	public void visit(String url){
		 getWebDriver().get(url);
	}
	
	public boolean isDisplayed(By locator){
		try {
			return find(locator).isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public String checkForAlerts() {
		
		   try{
			   driverWait.withTimeout(3, TimeUnit.SECONDS);
			   driverWait.until(ExpectedConditions.alertIsPresent());
		       if(getWebDriver().switchTo().alert() != null){
		           Alert alert = getWebDriver().switchTo().alert();
		           String msg = alert.getText();
		           alert.accept();
		           driverWait.withTimeout(STANDART_PAGE_LOAD_WAIT_TIME, TimeUnit.SECONDS);
		           return msg;
		       }
		    }catch(Exception e){
		    	//do nothing
		    }
		   driverWait.withTimeout(STANDART_PAGE_LOAD_WAIT_TIME, TimeUnit.SECONDS);
		   return "";
		}
	
	public void closeIridizePopups() throws InterruptedException {
		JavascriptExecutor je = (JavascriptExecutor) getWebDriver();
	    je.executeScript("window.sessionStorage.iridizeLoadScript = \"0\";"); 
	    String script = "if(window.iridize){ iridize(\"api.guide.stop\",{});}";
	    je.executeScript(script);
 
	}
	
   
	public void openNewExperimentDialog(String expName) throws InterruptedException{


        getWebDriver().switchTo().activeElement();
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".txt-field.experiment_name")));
        
        if(expName != null){
	        WebElement txtExpName = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".txt-field.experiment_name")));
	        sendKeys(txtExpName, expName);
        }
 
        WebElement btnAdd = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@value='Add Experiment']")));
        btnAdd.click();
        TimeUnit.SECONDS.sleep(2);
        getWebDriver().switchTo().activeElement();
       			
		TimeUnit.SECONDS.sleep(2);

	}


	

	
	protected Object executeJavascript(String script) throws InterruptedException{

	    JavascriptExecutor je = (JavascriptExecutor) getWebDriver();
	    
	    TimeUnit.SECONDS.sleep(2);
	  
	    Object retVal = je.executeScript(script);  
	    return retVal;
	}
	
	protected Object executeJavascript(String script,WebElement elemToExecute) throws InterruptedException{

	    JavascriptExecutor je = (JavascriptExecutor) getWebDriver();
	    
	    TimeUnit.SECONDS.sleep(2);
	  
	    Object retVal = je.executeScript(script,elemToExecute);  
	    return retVal;
	}

	public String editItemFromShowPage() throws InterruptedException {
		TimeUnit.SECONDS.sleep(2); 
		
		WebElement editBtn = getWebDriver().findElement(By.cssSelector(".edit"));
		editBtn.click();
		TimeUnit.SECONDS.sleep(1); 
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Save")));
		
	    WebElement txtName;
	    //the text name is not with same id for all collections - some have 'name' and some have 'title' as id
		try {
			txtName = getWebDriver().findElement(By.id("name"));
		} catch (NoSuchElementException e) {
			//try 'title' id
			txtName = getWebDriver().findElement(By.id("title"));
		}
	     txtName.sendKeys(txtName.getText() + " Updated");
		
		save();
		
		String msg = checkForNotyMessage();
		TimeUnit.SECONDS.sleep(3); 
		return msg;
		
	}
	
	public String openMarkAsConsumedPopup() throws InterruptedException {
		
		getWebDriver().switchTo().activeElement();
		
		TimeUnit.SECONDS.sleep(1);
		WebElement btnOk =  getWebDriver().findElement(By.xpath(".//*[@value='OK']"));
		btnOk.click();
		TimeUnit.SECONDS.sleep(2);
		
		getWebDriver().switchTo().activeElement();
		String msg = checkForNotyMessage();
		return msg;

	}
	
	
	public void invokeSearchItem(String itemToSearch) throws InterruptedException{
		
		closeIridizePopups();
		
		 WebElement txtSearch = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".searchtextbox")));
         sendKeys(txtSearch, itemToSearch);
        
         WebElement btnSearch = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='frm_search_box']/*[@value='Search']")));
         btnSearch.click();
         TimeUnit.SECONDS.sleep(3);
       
	}
	
	public String deleteFromShowPage() throws InterruptedException {
		
		WebElement editBtn = getWebDriver().findElement(By.id("delete-item"));
		editBtn.click();
		TimeUnit.SECONDS.sleep(1); 
		
		checkForAlerts();
		
		String msg = checkForNotyMessage();
		return msg;
	}
	
	
	public boolean uploadFileFromRightSide() throws InterruptedException {

		TimeUnit.SECONDS.sleep(2);
		String path = workingDir + LGConstants.ASSETS_FILES_DIRECTORY  + LGConstants.UPLOAD_TXT_TEST_FILENAME;
		
		TimeUnit.SECONDS.sleep(5);
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".right-side-block")));
		
		uploadFile(path);

		WebElement file = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//div[@id='files']/ul/li[2]/span")));
		if (file.getText().equals(LGConstants.UPLOAD_TXT_TEST_FILENAME)){
			return true;
		}
		return false;
	}
	

	public String viewUploadedFile() throws InterruptedException {
		
		WebElement file = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//div[@id='files']/ul/li[2]/span")));
		if (file.getText().equals(LGConstants.UPLOAD_TXT_TEST_FILENAME)){
			WebElement viewFile = getWebDriver().findElement(By.xpath(".//a[@title='" + LGConstants.UPLOAD_TXT_TEST_FILENAME + "']"));
			viewFile.click();
			TimeUnit.SECONDS.sleep(2);
			checkForAlerts();
			driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("page-title"))); 
			return getWebDriver().getTitle();
		}
		

		return "File not found";
	}
	
	
	public void uploadFile(String pathToFile) throws InterruptedException{
		
		WebElement fileSelect = getWebDriver().findElement(By.xpath(".//*[@type='file']"));
		fileSelect.sendKeys(pathToFile);
		
		TimeUnit.SECONDS.sleep(4);
	}
	
	public String uploadImage(String resource) throws InterruptedException {

		String path = workingDir + LGConstants.ASSETS_FILES_DIRECTORY +  LGConstants.UPLOAD_IMAGE_TEST_FILENAME;
		TimeUnit.SECONDS.sleep(5);
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".right-side-block")));
		uploadFile(path);

		return checkIfImageUploaded(resource);
	}
	
	
	protected String checkIfImageUploaded(String resource) throws InterruptedException {
		
		WebElement image = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//div[@id='images']/ul/li[2]/span")));
		if (image.getText().equals(LGConstants.UPLOAD_IMAGE_TEST_FILENAME)){
			WebElement viewFile = getWebDriver().findElement(By.xpath(".//a[@title='" + LGConstants.UPLOAD_IMAGE_TEST_FILENAME + "']"));
			viewFile.click();
			TimeUnit.SECONDS.sleep(2);
			checkForAlerts();
			
			driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("page-title"))); 
			WebElement ToInput = getWebDriver().findElement(By.xpath(".//*[@id='lg_info_tab_to']/a"));
			String relatedTo = ToInput.getText();
			if(!relatedTo.equals(resource))
				return "Not the right image for the created resource...";
			return getWebDriver().getTitle();
		}

		return "Image not found...";
	}
	
	
	public String addTagWithSamePrefix(String tagName) throws InterruptedException{
		
		addSingleTag(tagName);
		String tagPrefix = tagName.substring(0, tagName.length() -2);
		addSingleTag(tagPrefix);
		
		return findTag(tagPrefix);
	}

	private void addSingleTag(String tagPrefix) throws InterruptedException {
		
		WebElement btnAddTag = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add_tag_click"))); 
		btnAddTag.click();
		
		TimeUnit.SECONDS.sleep(2);

		addTagWithName(tagPrefix);
	}
	
	protected void addTagWithName(String tagPrefix) throws InterruptedException {
		
		WebElement inputTxt = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='new_tag_select_box']/ul/li/input")));
		inputTxt.clear();
		inputTxt.sendKeys(tagPrefix);
		TimeUnit.SECONDS.sleep(1);
		inputTxt.sendKeys(Keys.ENTER);
		TimeUnit.SECONDS.sleep(2);
		
		WebElement btnSave = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new_tag_save")));
		btnSave.click();
		
		TimeUnit.SECONDS.sleep(2);
	}
	
	public String addTag(String tagName) throws InterruptedException{
		
		addSingleTag(tagName);
		
		TimeUnit.SECONDS.sleep(2);
		
		return findTag(tagName);	
	}

	/**
	 * Go over all tags list and find the one to search.click on it,see that it is open ok.
	 * @param tagName
	 * @return the tag show page title
	 */
	protected String findTag(String tagName) {
		List <WebElement> lblTags = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".filter-by-tag.ng-binding.ng-scope")));
		for (WebElement lblTag : lblTags) {
			String tag = lblTag.getText();
			if(tag.equals(tagName)){
				lblTag.click();
				checkForAlerts();
				WebElement tagTitle = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='page-title']/span")));
				return tagTitle.getText();
			}
		}
		return "No match";
	}
		
	public String addTask(String taskTitle) throws InterruptedException{
		
		checkForNotyMessage();
		
		WebElement btnAddTask = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-task-link"))); 
		btnAddTask.click();
		TimeUnit.SECONDS.sleep(2);
		
		WebElement txtTaskTitle = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("task_name")));
		txtTaskTitle.sendKeys(taskTitle);
		
		WebElement txtDueDate = getWebDriver().findElement(By.id("start_date_datepicker_task"));
		String dueDate = txtDueDate.getAttribute("value");
		//set notification date
		WebElement notificationDate = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='task_form']/div[5]/div[2]/input")));
		notificationDate.sendKeys(dueDate);
		
		WebElement btnSave = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='task_form']/div[last()]/input")));
		btnSave.click();
		
		TimeUnit.SECONDS.sleep(2);
		
		List<WebElement> tasks = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@class='tasks']/li")));
		
		for (int i = 1; i<= tasks.size();i++) {
			WebElement taskTitleElm = getWebDriver().findElement(By.xpath(".//*[@class='tasks']/li["+ i+"]/a[3]"));
			if(taskTitleElm.getText().equals(taskTitle))
				return taskTitleElm.getText();
		}
		
		
		return "No task match";
	}
	
	public String addLinkedResource(String resourceToLink) throws InterruptedException{
		
		addLink(resourceToLink);
		//we add experiment as linked resource so we look for the title id as it appears in the experiment page
		WebElement title = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='projects_experiment_title_input']/span")));
		
		return title.getText();
	}
	
	public String addProjectAsLinkedResource(String resourceToLink) throws InterruptedException{
		
		addLink(resourceToLink);
		
		WebElement title = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='projects_project_title_input']/span")));
		
		return title.getText();
	}


	
	public boolean deleteTask(String taskToDelete) throws InterruptedException {
		
		List<WebElement> tasks = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@class='tasks']/li")));
		
		for (int i = 1; i<= tasks.size();i++) {
			WebElement taskTitleElm = getWebDriver().findElement(By.xpath(".//*[@class='tasks']/li["+ i+"]/a[3]"));
			if(taskTitleElm.getText().equals(taskToDelete)){
				WebElement cross = getWebDriver().findElement(By.xpath(".//*[@class='tasks']/li["+ i+"]/a[1]"));
				executeJavascript("arguments[0].click();",cross);
				TimeUnit.SECONDS.sleep(2);
		   	
				checkForAlerts();
			
				return true;
			}
		}
		return false;
	}
	
	public boolean checkIfEmailRecieved(){
		
		String currentURL = getWebDriver().getCurrentUrl();
		
		loginToGmailAccount();
		
		getWebDriver().get("https://mail.google.com/mail/?pli=1#inbox");
		
		WebElement sender = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".zF")));
		
		boolean mailRecieved = sender.getAttribute("name").equals("Labguru") && sender.getAttribute("email").equals("notifications@labguru.com");
		
		getWebDriver().get(currentURL);
		return mailRecieved;
		
	}

	protected void loginToGmailAccount() {
		
		
		getWebDriver().get(LGConstants.GMAIL_URL);
		
		WebElement account;
		try {//new enter
			account = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Email")));
			account.sendKeys(LGConstants.GMAIL_ACCOUNT);
			getWebDriver().findElement(By.id("next")).click();
			account = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email-display")));
			
		} catch (Exception e) {
			account = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("reauthEmail")));
		}
		
		if(account.getText().equals(LGConstants.GMAIL_ACCOUNT)){
			
			WebElement password = getWebDriver().findElement(By.id("Passwd"));
			sendKeys(password,LGConstants.GMAIL_ACCOUNT_PASS);
			
			getWebDriver().findElement(By.id("signIn")).click();
		}
	}
	
    protected String checkForNotyMessage() {
		try {
			WebElement upperMsg = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(LGConstants.NOTY_TEXT_CLASS_NAME)));
			String msg = upperMsg.getText();				
			upperMsg.click();
			TimeUnit.SECONDS.sleep(2);
			return msg;
		} catch (Exception e) {
			//do nothing
		}
		return "";
	}
	public boolean addTaskWithScriptInName(String taskName) {
		
		try {
			WebElement btnAddTask = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-task-link"))); 
			btnAddTask.click();
			
			WebElement txtTaskTitle = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("task_name")));
			txtTaskTitle.sendKeys(taskName);
			
			WebElement btnSave = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='task_form']/div[7]/input")));
			btnSave.click();
			
		} catch (UnhandledAlertException e) {
			//returns false if the alerts was prompt
			checkForAlerts();
			return false;
		}
		
		return true;//returns true if secured
	}

	public boolean addTagWithScriptInName(String tagName) throws InterruptedException {
		
		try {
			addSingleTag(tagName);
			
		} catch (UnhandledAlertException e) {
			//returns false if the alerts was prompt
			checkForAlerts();
			return false;
		}
		
		return true;//returns true if secured

	}
	
	protected void hoverAndClick(By locator) {
		
		WebElement element = getWebDriver().findElements(locator).get(0);
		Actions actions = new Actions(getWebDriver());
		actions.moveToElement(element).click().perform();

	}
	
	public boolean checkAnnotation() throws InterruptedException {
		
		executeJavascript("$('.redactor-editor').find('img').trigger('mouseenter'); $('.annotate_image_button').trigger('click');\n");

		TimeUnit.SECONDS.sleep(1);
		
		
		WebElement dialog = getWebDriver().switchTo().activeElement();
        driverWait.until(ExpectedConditions.visibilityOf(dialog));
        
        
        TimeUnit.SECONDS.sleep(2);
        
		WebElement annotateEditor = getWebDriver().findElement(By.xpath(".//*[@id='content']"));

		boolean isOpen = annotateEditor != null;
		WebElement btnClose = getWebDriver().findElement(By.xpath(".//*[@class='lity-close']"));
		btnClose.click();
		TimeUnit.SECONDS.sleep(1);
		getWebDriver().switchTo().activeElement();
		return isOpen;
	}
	
	private void addLink(String resourceToLink) throws InterruptedException {
		WebElement btnAddLinkRes = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='resources_box']/h3/span/a"))); 
		btnAddLinkRes.click();
		TimeUnit.SECONDS.sleep(1);
		
		checkForAlerts();
		invokeSearchItem(resourceToLink);
		WebElement btnAddLink = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@class='icon icon-greenadd']")));
		btnAddLink.click();
		TimeUnit.SECONDS.sleep(2);
		
		//click on 'done linking' (workarround for jenkins)
		executeJavascript("document.getElementById('advSearch').click();");
		TimeUnit.SECONDS.sleep(1);
		
		clickOnResourceLink();
	}
	
	protected void clickOnResourceLink() throws InterruptedException {
		
		WebElement lblLink = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".link_name.ng-binding")));
		lblLink.click();
		TimeUnit.SECONDS.sleep(3);
	}
	
	/** 
	 * This dialog opens when clicking on 'Add Stock'
	 * @throws InterruptedException 
	 */
	protected String openStockSelectionDialog(String tubeName,String selectedBox,String type) throws InterruptedException {

        TimeUnit.SECONDS.sleep(2); 
        getWebDriver().switchTo().activeElement();
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
        
        WebElement txtName = getWebDriver().findElement(By.id("name"));
        if(tubeName != null)
        	sendKeys(txtName, tubeName);
        
        selectType(type);

        WebElement location = getWebDriver().findElement(By.xpath(".//*[@id='storage_locations_tree']/div/ul/li/div/span/span"));
        String locationPath = location.getText();
        location.click();

        String boxName = selectBox(selectedBox,false/*not edit mode*/);
	    
	    WebElement btnSave = getWebDriver().findElement(By.id("save"));
	    btnSave.click();
	    TimeUnit.SECONDS.sleep(1); 
	    
	    getWebDriver().switchTo().activeElement();
	    if(boxName.isEmpty())
	    	return locationPath;
	    return locationPath + "/" + boxName;
		
	}

	protected void selectType(String type) throws InterruptedException {
		
		WebElement dropdown = getWebDriver().findElement(By.xpath(".//*[@id='s2id_select_stock_type']/a/span[2]/b"));
		dropdown.click();
		TimeUnit.SECONDS.sleep(1); 
		WebElement input = getWebDriver().findElement(By.xpath(".//*[@id='select2-drop']/div/input"));
		input.click();
		input.sendKeys(type);
		
		
		WebElement selectedType = getWebDriver().findElement(By.xpath(".//*[@id='select2-drop']/ul/li/div/span"));
		selectedType.click();
		TimeUnit.SECONDS.sleep(1); 
	}
	
	public String selectBox(String boxName,boolean editMode) throws InterruptedException {
		
		 if(boxName == null){//no box specified - select the box from the drop down
				WebElement suggestedBoxArrow = getWebDriver().findElement(By.xpath(".//*[@id='s2id_suggested_box_id']/a/span[2]/b"));
				suggestedBoxArrow.click();
				TimeUnit.SECONDS.sleep(1); 
				WebElement selectedBox = getWebDriver().findElement(By.xpath(".//*[@id='select2-drop']/ul/li[1]/div"));
				selectedBox.click();
				TimeUnit.SECONDS.sleep(1); 
				WebElement boxNameElm = getWebDriver().findElement(By.xpath(".//*[@id='s2id_suggested_box_id']/a/span[1]"));
				boxName = boxNameElm.getText();
		}else if(boxName.equals("None")){//no box in location
			return "";
		}else{//select the given box in the box drop down
			selectFromDropDown(boxName);
		}
	
		 //click on next if we are in 'new' mode and a box (no gridless box) is selected
        if(!(boxName.equals("None") || boxName.equals("") || boxName.contains("gridless") || editMode)){
        
		    WebElement btnNext = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("next")));
		    btnNext.click(); 
		    TimeUnit.SECONDS.sleep(2); 
		    selectLocationInBox();
		    TimeUnit.SECONDS.sleep(1); 
        }
        if(boxName.contains("(")){
        	boxName = boxName.substring(0, boxName.indexOf(" ("));
        }
		return boxName;
	}
	
	private void selectLocationInBox() {
		
		 WebElement iconAdd = getWebDriver().findElement(By.xpath(".//*[starts-with(@id,'new_stock_')]/i[@class='icon icon-greenadd']"));
		 iconAdd.click();
		 
	}
	private String selectFromDropDown(String boxName) throws InterruptedException {
		
		WebElement drpContent = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='s2id_suggested_box_id']/a/span[1]")));
		drpContent.click();
		
		TimeUnit.SECONDS.sleep(1);
		WebElement input = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='select2-drop']/div/input")));
		input.click();
		TimeUnit.SECONDS.sleep(1);
		input.sendKeys(boxName);
		TimeUnit.SECONDS.sleep(1);
		
		WebElement selectedContent = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='select2-drop']/ul/li[last()]/div")));
		String selectedBox = selectedContent.getText();
		selectedContent.click();
		TimeUnit.SECONDS.sleep(1);
		return selectedBox;
	
	}
	
	protected void selectToday(WebElement txtStartDate) throws InterruptedException {
		
		 WebElement todaySelect = getWebDriver().findElement(By.cssSelector(".xdsoft_current.xdsoft_today>div"));
		 todaySelect.click();
		 TimeUnit.SECONDS.sleep(1);		
			
	}
	
	protected void selectTomorrow(WebElement txtStartDate) throws InterruptedException {
			 
		 //search for today (the week it is on ) - if found ,take this week next day
		 WebElement today = getWebDriver().findElement(By.cssSelector(".xdsoft_current.xdsoft_today"));
		 int dayNum = Integer.valueOf(today.getAttribute("data-date")).intValue() ;
		 String xpath = "";
		 //if this is the last days of the month - make it the 1st of the new month,else add 1
		 if(dayNum >= 28){
			 dayNum = 1;
			 xpath = "//*[(contains(@class, 'xdsoft_other_month'))][@data-date  ='" + String.valueOf(dayNum) + "']/div";
		 }else{
			 dayNum += 1;
			 xpath = "//*[not(contains(@class, 'xdsoft_other_month'))][@data-date  ='" + String.valueOf(dayNum) + "']/div";
		 }
		 List <WebElement> tommorowSelectList = getWebDriver().findElements(By.xpath(xpath));	 
		 for (WebElement tommorowSelect : tommorowSelectList) {
			 if(tommorowSelect.isDisplayed()){
				 tommorowSelect.click();
				 TimeUnit.SECONDS.sleep(1);
				 break;
			 }
		  }
	}
	
	protected void selectPast(WebElement txtStartDate) throws InterruptedException {
		
		
		 //search for today (the week it is on ) - if found ,take this week next day
		 WebElement today = getWebDriver().findElement(By.cssSelector(".xdsoft_current.xdsoft_today"));
		 int dayNum = Integer.valueOf(today.getAttribute("data-date")).intValue();
		 //if this is the first day of the month - make it the 28 of the previous month,else remove 1
		 String xpath = "";
		 if(dayNum == 1){
			 dayNum = 28;
			 xpath = "//*[(contains(@class, 'xdsoft_other_month'))][@data-date  ='" + String.valueOf(dayNum) + "']/div";
			
		 }else{	 
			 dayNum -= 1;
			 xpath = "//*[not(contains(@class, 'xdsoft_other_month'))][@data-date  ='" + String.valueOf(dayNum) + "']/div";
		 }
		 List <WebElement> pastSelectList = getWebDriver().findElements(By.xpath(xpath));	 
		 for (WebElement pastSelect : pastSelectList) {
			 if(pastSelect.isDisplayed()){
				 pastSelect.click();
				 TimeUnit.SECONDS.sleep(1);
				 break;
			 }
		  }

	}
	
	
	protected void save() {	
		try {
			TimeUnit.SECONDS.sleep(1);
			WebElement btnSave = getWebDriver().findElement(By.id("Save"));
			btnSave.click();
			TimeUnit.SECONDS.sleep(3);
		} catch (Exception e) {
			getLogger().debug("@@Error during saving..");
		}
	}
	
	protected void saveAndNew() {	
		try {
			TimeUnit.SECONDS.sleep(1);
			WebElement btnSaveAndNew = getWebDriver().findElement(By.xpath(".//*[@value='Save & New']"));
			btnSaveAndNew.click();
			TimeUnit.SECONDS.sleep(3);
		} catch (Exception e) {
			getLogger().debug("@@Error during 'save and new'..");
		}
	}
	
    /**
     * isCurrentDatePreviousFutureDate - check that Date1 is previous to Date2
     *
     * @return true if result is less then 0  - fromDate is previous to  toDate,false otherwise
     */
    public static boolean isCurrentDatePreviousFutureDate(Date currentDate, Date futureDate) {

        int result = currentDate.compareTo(futureDate);

        return (result < 0);

    }
    
//    public void writeInRedactor(String textAreaId,String text) throws InterruptedException{
//    	TimeUnit.SECONDS.sleep(1);
//    	executeJavascript("textboxio.get('#" + textAreaId + "')[0].content.set('"+text+"')");
//    	//executeJavascript("$('#" + textAreaId + "').redactor('code.set', '<p>"+text+"</p>');");
//    	TimeUnit.SECONDS.sleep(1);
//    }
    
    public void writeInRedactor(String textAreaId,int textAreaIndex,String text) throws InterruptedException{
    	TimeUnit.SECONDS.sleep(1);
    	executeJavascript("textboxio.get('#" + textAreaId + "')[" + textAreaIndex + "].content.set('"+text+"')");
    	TimeUnit.SECONDS.sleep(1);
    }
    
	protected WebElement getDescToolBarElement() {
		return driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='ui-id-2']/div[2]/div/ul")));
	}

	protected void waitForPageCompleteLoading() {

		driverWait.until(new ExpectedCondition<Object>() {
				@Override
				public Object apply(WebDriver driver) {
					getLogger().info("waiting for page load to complete");
					return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");	
				}
		    });

	}
	
	public void refreshPage() throws InterruptedException {
		
		TimeUnit.SECONDS.sleep(2);
		executeJavascript("document.location = document.location;");
		TimeUnit.SECONDS.sleep(2);
		checkForAlerts();
	}
	
	protected void switchToNewTab() {
		
		Set<String> winSet = getWebDriver().getWindowHandles();
		List<String> winList = new ArrayList<String>(winSet);
		String newTab = winList.get(winList.size() - 1);
		getWebDriver().close(); // close the original tab
		getWebDriver().switchTo().window(newTab); // switch to new tab
		driverWait = new WebDriverWait(getWebDriver(), STANDART_PAGE_LOAD_WAIT_TIME);
		getWebDriver().manage().window().setSize(new Dimension(1280,800));
		
	}
	
	protected void selectDropDownOption(String dropDownID, String option,String index) throws InterruptedException
    {
        for (int second = 0; ; second++)
        {
        	if (second >= 60)
        		Assert.fail("timeout");
            try
            {
                if (getWebDriver().findElement(By.cssSelector("div[id^=s2id_" + dropDownID + "]>a.select2-choice")).isDisplayed()) 
                	break;
            }
            catch (Exception e)
            { }
            Thread.sleep(1000);
        }

        getWebDriver().findElement(By.cssSelector("div[id^=s2id_" + dropDownID + "]>a.select2-choice")).click();
        Thread.sleep(1000);

        if (getWebDriver().findElement(By.cssSelector("#s2id_autogen"+ index+ "_search.select2-input.select2-focused")).isDisplayed() == true)
        {
        	getWebDriver().findElement(By.cssSelector("#s2id_autogen"+ index+ "_search.select2-input.select2-focused")).sendKeys(option);
            Thread.sleep(1000);
            getWebDriver().findElement(By.cssSelector("#s2id_autogen"+ index+ "_search.select2-input.select2-focused")).sendKeys(Keys.ENTER);
            Thread.sleep(1000);
        }
        else
        {
        	getWebDriver().findElement(By.cssSelector("input.select2-focusser.select2-offscreen")).sendKeys(option);
            Thread.sleep(1000);
            getWebDriver().findElement(By.cssSelector("input.select2-focusser.select2-offscreen")).sendKeys(Keys.ENTER);
            Thread.sleep(1000);
        }

    }
	
	/**
	 * Find the index of the given column name
	 * @return
	 * @throws InterruptedException 
	 */
	protected int searchForColumnIndex(String headerName) throws InterruptedException {
		
		TimeUnit.SECONDS.sleep(3);
		List<WebElement> headers = getWebDriver().findElements(By.xpath(".//*[@id='index_table']/tbody/tr[1]/th"));
		int  headerIndex= 2;
		for ( ;headerIndex <= headers.size(); headerIndex++) {
			WebElement elem = getWebDriver().findElement(By.xpath(".//*[@id='index_table']/tbody/tr[1]/th[" + headerIndex +"]"));
			String header = elem.getAttribute("id");
			if(header.equals(headerName)){
				return headerIndex;
			}
		}
		return -1;//table not exist
	}
	
	
	public void saveTextBoxIO() {
		
		WebElement saveDescription = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@title='Save']")));
		saveDescription.click();
	}


}
