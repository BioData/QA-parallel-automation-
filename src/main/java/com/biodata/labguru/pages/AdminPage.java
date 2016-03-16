package com.biodata.labguru.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;

public class AdminPage extends BasePage{
	
	
	protected  void initPage(WebDriver webDriver) {
		
		PageFactory.initElements(webDriver, AdminPage.class);

	}
	
	public void selectMyProfileMenu() {
		
		selectUserDropDown();
		
		selectMenu(By.xpath(".//*[@id='header']/div/div[2]/span[2]/ul/li[1]/a"));
		
	}
	
	public void selectSetPasswordMenu() {
		
		selectUserDropDown();
		
		selectMenu(By.xpath(".//*[@id='header']/div/div[2]/span[2]/ul/li[2]/a"));
		
	}
	
	protected boolean selectDropDownMenuById(String id) {
		
		try {
			TimeUnit.SECONDS.sleep(2);		
			executeJavascript("document.getElementById('"+ id +"').click();");
			TimeUnit.SECONDS.sleep(2);		
			checkForAlerts();
			TimeUnit.SECONDS.sleep(2);
			
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public void selectAccountSettingMenu() {

		selectDropDownMenu(By.id("account_dropdown"));

		selectMenu(By.xpath(".//*[@id='header']/div/div[3]/span/ul/li[2]/a"));	
	}
	
	public void selectAccountMembersMenu() {

		selectDropDownMenu(By.id("account_dropdown"));
		selectMenu(By.xpath(".//*[@id='header']/div/div[3]/span/ul/li[1]/a"));
	}
	
	public void selectAccountBillingMenu() {

		selectDropDownMenu(By.id("account_dropdown"));
		selectMenu(By.xpath(".//*[@id='header']/div/div[3]/span/ul/li[3]/a"));	
	}
	
	private void selectUserDropDown() {
		try {
			TimeUnit.SECONDS.sleep(2);	
			selectDropDownMenu(By.id("user_dropdown"));
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			getLogger().debug(e.getMessage());
		}	
	}
	
	protected void selectMenu(By locator){
		try {
			TimeUnit.SECONDS.sleep(2);		
			selectDropDownMenu(locator);
			TimeUnit.SECONDS.sleep(2);		
			checkForAlerts();
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			getLogger().debug(e.getMessage());
		}		
	}
	
	public boolean selectDropDownMenu(By locator)  {
		try {
			if(getBrowserType().equals(LGConstants.CHROME)){
				
				WebElement element = driverWait.until(ExpectedConditions.visibilityOfElementLocated(locator));

				Actions actions = new Actions(getWebDriver());

				actions.moveToElement(element).click().perform();
				
			}else{
				
				WebElement elem = driverWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
				elem.click();
				TimeUnit.SECONDS.sleep(2);
			}
			checkForAlerts();
			TimeUnit.SECONDS.sleep(2);
			return true;
		} catch (Exception e) {
			getLogger().debug(e.getMessage());
			return false;
		}
	}
	
//	protected boolean selectDropDownMenuById(String id) {
//		
//		try {
//			TimeUnit.SECONDS.sleep(2);		
//			executeJavascript("document.getElementById('"+ id +"').click();");
//			TimeUnit.SECONDS.sleep(2);		
//			checkForAlerts();
//			TimeUnit.SECONDS.sleep(2);
//			
//		} catch (Exception e) {
//			return false;
//		}
//		return true;
//	}

	public void selectHomeMenu() {
		selectMenu(By.cssSelector("#home_menu"));
	}
	
	public void selectENotebookMenu() {
		selectMenu(By.cssSelector("#notebook_menu"));
	}
	
	public void selectKnowledgebaseMenu() {
		selectMenu(By.cssSelector("#knowledgebase_menu"));
	}
	
	public void selectInventoryMenu() {
		selectMenu(By.cssSelector("#inventory_menu"));
	}
	
	public void selectStorageEquMenu() {
		selectMenu(By.cssSelector("#storage_menu"));
	}
	
	public String showAccountSettings() {
	
		selectAccountSettingMenu();
		
		driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));
	
		return getWebDriver().getTitle();
	}
	
	
	public String showDashboard(){

		selectHomeMenu();
		selectDropDownMenu(By.id("my_dashboard_menu"));
	
	   checkForAlerts();
       driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));
       return getWebDriver().getTitle();
	}
	

	

	public String selectProjects() {

		selectENotebookMenu();
		
		selectDropDownMenu(By.id("projects_menu"));
		
		checkForAlerts();

        driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));
	
		return getWebDriver().getTitle();
	}

	public String selectExperiments(){
		
		selectENotebookMenu();

	    selectDropDownMenu(By.id("experiments_menu"));
		
        driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));

		return getWebDriver().getTitle();
	}
	
	public String showRecentResults() {

		selectHomeMenu();
		selectDropDownMenu(By.id("recent_results_menu"));

        driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));

		return getWebDriver().getTitle();

	}
	
	public String showLabActivities() {
	
		selectHomeMenu();	
		selectMenu(By.id("lab_activities_menu"));
		driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));
		return getWebDriver().getTitle();
	}


	public String showCalendar() {

        selectHomeMenu();
        selectMenu(By.id("calendar_link_menu"));
    
		driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));

		return getWebDriver().getTitle();

	}
	
	public String showShoppingList() {
	
        selectInventoryMenu();

        selectMenu(By.id("shopping_list_menu"));
       
		driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));
     
		return getWebDriver().getTitle();
	}
	
	public String showConsumables() {

        selectInventoryMenu();

        selectMenu(By.id("consumables_menu"));
    
		driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));

		return getWebDriver().getTitle();
	}
	
	public String showPrimers() {

        selectInventoryMenu();

        selectMenu(By.id("primers_menu"));
    	
		driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));

		return getWebDriver().getTitle();
	}
	
	public String showBacteria() {

        selectInventoryMenu();

    	selectMenu(By.id("bacteria_menu"));
    	
		driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));

		return getWebDriver().getTitle();
	}
	
	
	public String showCellLines() {

        selectInventoryMenu();

    	selectMenu(By.id("cell_lines_menu"));
    	
		driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));

		return getWebDriver().getTitle();
	}
	
	public String showRodentTreatments() {

        selectRodentsMenu();
        
        selectMenu(By.id("treatments_menu"));
       
		driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));

		return getWebDriver().getTitle();
	}
	
	public String showRodentStrains() {

        selectRodentsMenu();
        
        selectMenu(By.id("strains_menu"));
        
		driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));

		return getWebDriver().getTitle();
	}
	
	public String showRodentSpecimens() {

        selectRodentsMenu();
        
        selectMenu(By.id("specimens_menu"));
        
		driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));

		return getWebDriver().getTitle();
	}

	public void selectRodentsMenu() {
		
		selectInventoryMenu();

    	selectMenu(By.id("rodent_menu"));
    	
	}
	
	public String showRodentCages() {

        selectRodentsMenu();
        
        selectMenu(By.id("cages_menu"));
       
		driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));

		return getWebDriver().getTitle();
	}
	
	public void selectBotanyMenu() {
		
		selectInventoryMenu();

    	selectMenu(By.id("botany_menu"));
    	
	}
	
	public String showBotanyPlants() {

        selectBotanyMenu();
        
        selectMenu(By.id("plants_menu"));
       
		driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));

		return getWebDriver().getTitle();
	}
	
	public String showBotanySeeds() {

        selectBotanyMenu();
        
        selectMenu(By.id("seeds_menu"));
     
		driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));

		return getWebDriver().getTitle();
	}
	
	public String showCollectionsAndSettings() {

        selectInventoryMenu();

        selectMenu(By.cssSelector(".fa-plus"));
        
		driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));
      
		return getWebDriver().getTitle();
		
	}
	
	public String showStorages() {

        selectStorageEquMenu();

        selectMenu(By.id("storages_menu"));
        
        driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));
     
		return getWebDriver().getTitle();
	}


	public String showBoxes() {
		
        selectStorageEquMenu();
        selectMenu(By.id("boxes_menu"));
        
		driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));

		return getWebDriver().getTitle();
	}


	public String showStocks() {
		
        selectStorageEquMenu();
        selectMenu(By.id("stocks_menu"));
       
		driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));
 
		return getWebDriver().getTitle();
	}


	public String showCryogenicCanes() {
			
        selectStorageEquMenu();

        selectMenu(By.id("cryogenic_canes_menu"));
        
		driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));

		return getWebDriver().getTitle();
	}
	
	public String showPlasmids() {
		
        selectInventoryMenu();    
        selectMenu(By.id("plasmids_menu"));
      
		driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));
      
		return getWebDriver().getTitle();
	}
	
	public String showAntibodies(){
	
        selectInventoryMenu(); 
        selectMenu(By.id("antibodies_menu"));
      
		driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));
   
		return getWebDriver().getTitle();
	}


	public String showEquipment() {
		
	   selectStorageEquMenu();

	   selectMenu(By.id("equipment"));
	   
	   driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));		
	   return getWebDriver().getTitle();

	}
	
	public String showProtocols() {
	
        selectENotebookMenu();
 
		selectDropDownMenu(By.id("protocols_menu"));
   
		driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));	
		return getWebDriver().getTitle();
		
	}
	
	public String showRecipes() {

        selectKnowledgebaseMenu();

        selectMenu(By.id("recipes_menu"));
       
		driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));

		return getWebDriver().getTitle();	
	}
	
	public String showPapers() {
	
        selectKnowledgebaseMenu();

        selectMenu(By.id("papers_menu"));

		driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));

		return getWebDriver().getTitle();
	}


	public String showDocuments() {
			
        selectKnowledgebaseMenu();
        selectMenu(By.id("documents_menu"));
        
		driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));
      
		return getWebDriver().getTitle();
	}
	
	public String showImageBank() {

	   selectKnowledgebaseMenu();

	   selectMenu(By.id("image_bank_menu"));
	 
	   driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));

		return getWebDriver().getTitle();
	}
	
	public String showAttachments() {

        selectKnowledgebaseMenu();

        selectMenu(By.id("attachments_menu"));
    	
		driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));
      
		return getWebDriver().getTitle();
	}

	public String logout() throws InterruptedException {
	
		 WebElement btnLogout = driverWait.until(ExpectedConditions.visibilityOfElementLocated
	        		(By.cssSelector("#logout>a>img")));
        btnLogout.click();
        
        checkForAlerts();

        TimeUnit.SECONDS.sleep(1);
        return  getWebDriver().getTitle();

	}

	public String searchInRecentlyViewedList(String expName) {
			
		List<WebElement> list = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
				(By.xpath(".//*[@id='history_items']/ul/li")));
		
			
		for (int i = 1; i <= list.size(); i++) {
			WebElement item = getWebDriver().findElement(By.xpath(".//*[@id='history_items']/ul/li[" + i + "]"));
			String name = item.getText();
			if(name.equals(expName)){
				WebElement link = getWebDriver().findElement(By.xpath(".//*[@id='history_items']/ul/li[" + i + "]/a"));
				link.click();
				WebElement title = driverWait.until(ExpectedConditions.visibilityOfElementLocated
						(By.id("projects_experiment_title_input")));
				
				return title.getText();
			}
		}
		return "Opps - the item you search for was not found.";
	}
	
	public String inviteFriendToLabguru(String friend) throws InterruptedException {
		
		checkForNotyMessage();
		
		WebElement referralIcon = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("referral-icon")));
		referralIcon.click();
		TimeUnit.SECONDS.sleep(2);
		
		WebElement txtMail  = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@class='select2-search-field']/input")));
		txtMail.sendKeys(friend);
		TimeUnit.SECONDS.sleep(1);
		txtMail.sendKeys(Keys.ENTER);
		TimeUnit.SECONDS.sleep(1);
		WebElement btnSend = getWebDriver().findElement(By.cssSelector(".addnew"));
		btnSend.click();
		btnSend.click();
		TimeUnit.SECONDS.sleep(2);
		String msg = checkForNotyMessage();
	
		return msg;
	}
	

	public boolean inviteFriendToLabguruWrongPattern() throws InterruptedException {
		
		WebElement referralIcon = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("referral-icon")));
    	referralIcon.click();
    	TimeUnit.SECONDS.sleep(3);
    	
		WebElement txtMail = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@class='refer-a-friend']/div/ul/li")));
		txtMail.sendKeys("kolololololo");
		txtMail.sendKeys(Keys.ENTER);

		try {
			getWebDriver().findElement(By.cssSelector(".addnew.disabled"));
			return true; //send button is disabled
		} catch (Exception e) {
			return false;//the send button was not disabled
			
		}

	}

	public boolean changeAccountNameWithScriptInText(String name) throws InterruptedException {
		
		try {
			WebElement inputTxt = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("acc_name")));
			inputTxt.clear();
			inputTxt.sendKeys(name);
		

			WebElement btnSave = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Save")));
			btnSave.click();
			TimeUnit.SECONDS.sleep(2);
			
		} catch (UnhandledAlertException e) {
			//returns false if the alerts was prompt
			checkForAlerts();
			return false;
		}
		
		return true;//returns true if secured
	}

	public boolean changeUserNameWithScriptInText(String name) {
		
		try {
			WebElement btnEdit = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='edit_link']/span")));
			btnEdit.click();
			
			WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='edit-member']/div[2]/div[1]/input")));
			txtName.clear();
			txtName.sendKeys(name);
		

			WebElement btnSave = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Save")));
			btnSave.click();
			
		} catch (UnhandledAlertException e) {
			//returns false if the alerts was prompt
			checkForAlerts();
			return false;
		}
		
		return true;//returns true if secured
	}

	public String getAccountName() {
		
		WebElement accountElm = getWebDriver().findElement(By.xpath(".//*[@id='user_dropdown']"));
		return accountElm.getText();
	}


	
	protected String updateName(String titleId,String saveBtnXPath) throws InterruptedException{
		
        TimeUnit.SECONDS.sleep(2);
        
        //edit the name
    	executeJavascript("document.getElementsByClassName('edit_me')[0].click();");
		TimeUnit.SECONDS.sleep(2);
		
		WebElement txtName = getWebDriver().findElement(By.id(titleId));
		txtName.click();
		TimeUnit.SECONDS.sleep(1);
		String oldTitle = txtName.getAttribute("value");
		txtName.clear();
		txtName.sendKeys(oldTitle + " Update");
		
		//save the name
    	executeJavascript("document.getElementsByClassName('inline_submit')[0].click();");
		TimeUnit.SECONDS.sleep(2);

		refreshPage();
		
		TimeUnit.SECONDS.sleep(4);
		
		WebElement bottonMsg = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='bio_module_info']/div[2]")));
		String msg = bottonMsg.getText();

		return msg;
	}
	
	public String showVersionHistory(){
		
		WebElement linkVersions = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='bio_module_info']/div[2]/a[2]")));
		linkVersions.click();
		
		WebElement titleElm = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='main-content']/div/lg-versions/div[1]/h3")));
		String title = titleElm.getText();
		
		return title;
	}

	public String showCollection(String collectionName) {
		
		selectInventoryMenu();
		
	   	selectMenu(By.id("" + collectionName.toLowerCase() + "_menu"));
	   

		driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));

		return getWebDriver().getTitle();
	}
	
	public String duplicateItem() throws InterruptedException {
		
		TimeUnit.SECONDS.sleep(2);
		
		WebElement btnDuplicate = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".duplicate")));
		btnDuplicate.click();
		
		TimeUnit.SECONDS.sleep(2);
		save();
		
		//wait for the noty message
        String msg = checkForNotyMessage();
      
        return msg;
	}

	
	public void deleteTagFromTaggedEntitiesList() throws InterruptedException {
	
		WebElement btnDelete = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-item")));
		btnDelete.click();
		
		checkForAlerts();
		
		TimeUnit.SECONDS.sleep(2);
	}
	
	public void deleteTask() throws InterruptedException {
		
		WebElement deleteImg = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@class='tasks']/li[1]/a[1]")));
		deleteImg.click();
		
		checkForAlerts();
		
		TimeUnit.SECONDS.sleep(2);
	}
	
	protected void openCustomiseTableViewDialog() throws InterruptedException {
		
		WebElement customTableLink = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='customize']/span")));
		customTableLink.click();
		TimeUnit.SECONDS.sleep(2);
	}

	protected String checkTableHeaders(List<String> selectedColumns) {
		
		int headersCounter = 0;
		List<WebElement> headers = getWebDriver().findElements(By.xpath(".//*[@id='index_table']/tbody/tr[1]/th"));
		for (int i = 2; i <= headers.size(); i++) {
			WebElement columnHeader = getWebDriver().findElement(By.xpath(".//*[@id='index_table']/tbody/tr[1]/th["+ i + "]"));
			String header = columnHeader.getText();
			if(header.indexOf('▲') != -1)
				header = header.replace('▲', ' ').trim();
			if(header.indexOf('▼') != -1)
				header = header.replace('▼', ' ').trim();
			
			//if id not in the selected ids - stop checking and return the id to report
			if(!selectedColumns.contains(header)){
				return header;
			}else{
				headersCounter++;
			}
		}
		if(headersCounter == selectedColumns.size())
			return ""; //all selected headers exist in table
		
		return headersCounter  + " !=" + selectedColumns.size();
	}

	protected List<String> checkPreference(List<String> availableColumns) throws InterruptedException {
		
		openCustomiseTableViewDialog();
		//TODO - workarround for setting the correct ids
		WebElement btnUpdate = getWebDriver().findElement(By.xpath(".//*[@value='Update']"));
        btnUpdate.click();
		TimeUnit.SECONDS.sleep(2);
		
		openCustomiseTableViewDialog();

        WebElement newDialog = getWebDriver().switchTo().activeElement();
        driverWait.until(ExpectedConditions.visibilityOf(newDialog));
        
		List<String> selectedColumns = new ArrayList<String>();
		for (String columnId : availableColumns) {
			WebElement checkbox = getWebDriver().findElement(By.id(columnId));
			if(!checkbox.isSelected()){
				checkbox.click();
				TimeUnit.SECONDS.sleep(1);
			}
			//insert each column name to the list
			WebElement label = getWebDriver().findElement(By.xpath(".//*[@for='" + columnId + "']"));
			selectedColumns.add(label.getText());
		}
		
        btnUpdate = getWebDriver().findElement(By.xpath(".//*[@value='Update']"));
        btnUpdate.click();
		TimeUnit.SECONDS.sleep(2);
   
        getWebDriver().switchTo().activeElement();
        
		return selectedColumns;
	}
	
	public void deleteAllItemsFromTable() throws InterruptedException {
		
		WebElement chkCheckAll = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("check_all")));
		chkCheckAll.click();
		TimeUnit.SECONDS.sleep(2);
		
		try{
			//check if there is a link to select all items(there are more then 20 items in list)
			WebElement linkSelectAll  = getWebDriver().findElement(By.xpath(".//*[@id='selected_some']/a"));
			linkSelectAll.click();
			TimeUnit.SECONDS.sleep(1);
		}catch(NoSuchElementException e){
			//do nothing - les then 20 items in the list
		}
		
		WebElement btnDelete = getWebDriver().findElement(By.cssSelector(".delete"));
		btnDelete.click();
		checkForAlerts();
		TimeUnit.SECONDS.sleep(2);
	}
	
	protected void clickOnButton(String buttonId) {
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(buttonId)));
		WebElement btn = getWebDriver().findElement(By.id(buttonId));
		btn.click();
	}
	
	public String addNewExperiment(String expName) throws InterruptedException{
		
		clickOnButton("new_experiment_popup_btn");
		TimeUnit.SECONDS.sleep(2);
		return createExperiment(expName);
	}

	public String createExperiment(String expName) throws InterruptedException{

		openNewExperimentDialog(expName);

		return getWebDriver().getTitle();
	}

	public String searchFromElasticSearch(String strToSearch) throws InterruptedException {
		
		
		WebElement searchInput = getWebDriver().findElement(By.id("search"));
		sendKeys(searchInput, strToSearch);
		
		WebElement searchBtn = getWebDriver().findElement(By.id("submit_top_search"));
		searchBtn.click();
		TimeUnit.SECONDS.sleep(2);
		try{
			WebElement bestMatchesLbl = driverWait.until(ExpectedConditions.visibilityOfElementLocated
					(By.cssSelector(".ui-state-hover.filter_all_groups")));
			String txt = bestMatchesLbl.getText();
			return txt;
		}catch(NoSuchElementException e){
			//no best matches label - elastic search not working
			return " elastic search not working";
		}
	}
	
	public String searchTextFromAttachment(String strToSearch) throws InterruptedException {
		
		
		searchFromElasticSearch(strToSearch);
		
		List<WebElement> blockHeaders = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
				(By.xpath(".//*[@class='results-list']/li")));
		for (int i = 1; i <= blockHeaders.size(); i++) {
			WebElement header = getWebDriver().findElement(By.xpath(".//*[@class='results-list']/li[" + i + "]"));
			String label = header.getText();
			if(label.equals("Attachment")){
				WebElement resourceName = getWebDriver().findElement(By.xpath(".//*[@class='results-list']/h4[" + i + "]/a"));
				return resourceName.getText();
			}		
		}
			
		//no Attachment found - elastic search not working for attachments
		return "Elastic search did not find the text in attachment";
		
	}

	public void selectFromExperimentDropdown(String selectionId) throws InterruptedException {
		
		WebElement arrowBtn = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.id("new_experiment_popup_arrow")));
		arrowBtn.click();
		TimeUnit.SECONDS.sleep(1);
		
		
		WebElement selected = getWebDriver().findElement(By.id(selectionId));
		selected.click();
		TimeUnit.SECONDS.sleep(3);
	}
	
	public void goToRecentlyViewed() throws InterruptedException {
		
		WebElement recentViewLink = getWebDriver().findElement(By.xpath(".//*[@id='history_items']/ul/li[1]/a"));
		recentViewLink.click();
		TimeUnit.SECONDS.sleep(2);
		
	}

	public void discardNotyMessages() {
		
		checkForNotyMessage();

	}

	public boolean deleteAttachment() throws InterruptedException {
		
		
		WebElement crossIcon = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.cssSelector(".delete-attachment")));
		crossIcon.click();
		TimeUnit.SECONDS.sleep(3);
		checkForAlerts();
		
		//after delete attachment there should not be any cross icon
		try {
			getWebDriver().findElement(By.cssSelector(".delete-attachment"));
			return false;
		} catch (NoSuchElementException e) {
			return true;
		}
		
	}
	
	public String signAndLock() throws InterruptedException {
		TimeUnit.SECONDS.sleep(2);
		
		WebElement btnSign = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".sign_n_lock")));
		btnSign.click();
		
		TimeUnit.SECONDS.sleep(1);
		checkForAlerts();
		TimeUnit.SECONDS.sleep(2);
		int counter = 0;
		List<WebElement> btnList = getWebDriver().findElements(By.cssSelector(".sign_n_lock"));
		for (WebElement btn : btnList) {
			String label = btn.getText();
			//check that after 'sign' was clicked, the label that appear on the green menu are : Witness , Revert Signature
			if( label.equals("Sign") ){
				return "not signed";
			}else if( label.equals("Witness") ){
				counter++;
			}else if(label.equals("Revert Signature")){
				counter++;
			}	
		}
		//if both labels appear after clicking sign - return true..
       if (counter == 2){
    	   WebElement note = getWebDriver().findElement(By.cssSelector(".signed_note"));
    	   return note.getText();
       }
       return "not signed";
	}

	public String showSOPs() {
		
        selectKnowledgebaseMenu();
        selectMenu(By.id("sops_menu"));
        
		driverWait.until(ExpectedConditions.titleIs(getWebDriver().getTitle()));
      
		return getWebDriver().getTitle();
	}
	
	public boolean isFileEditable() {

		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("doctools")));
		try {
			getWebDriver().findElement(By.id("edit_link"));
			getWebDriver().findElement(By.id("delete-item"));
			//the actions for editing are displayed - file is editable
			return true;
		} catch (Exception e) {
			//the actions 'edit' & 'delete' are missing - meaning the file is not editable
			return false;
		}

	}

}
