package com.biodata.labguru.pages.enotebook;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.GenericHelper;
import com.biodata.labguru.LGConstants;


public class ProjectPage extends AbstractNotebookPage {

	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	@Override
	public  By getDuplicateLocator() {
		return By.cssSelector("#duplicate");
	}
	
	public boolean hasList() {
		selectProjects();
		try{
			List<WebElement> list = driverWait.until(ExpectedConditions
					.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@id='projects']/div")));
			return list.size() > 0;
		}catch(TimeoutException e){
			return false;
		}
	}
	
	
	@Override
	public String duplicateItem() throws InterruptedException {
		
		TimeUnit.SECONDS.sleep(2);
		
		WebElement btnDuplicate = driverWait.until(ExpectedConditions.visibilityOfElementLocated(getDuplicateLocator()));
		btnDuplicate.click();
		
		TimeUnit.SECONDS.sleep(2);
        WebElement pageTitle = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(getPageTitleXPath())));
        String newName = pageTitle.getText();
        return newName;
	}
	public String addNewProject(String projectName) throws InterruptedException {
		
        WebElement btnNewProj = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new_project")));

        btnNewProj.click();
        TimeUnit.SECONDS.sleep(2);
        
    	executeJavascript("document.getElementsByClassName('edit_me')[0].click();");
		TimeUnit.SECONDS.sleep(2);
		
		WebElement txtName = getWebDriver().findElement(By.id("projects_project_title"));
		txtName.click();
		txtName.clear();
		txtName.sendKeys(projectName);
		
		List<WebElement> saveBtnList = getWebDriver().findElements(By.xpath(".//*[@id='projects_project_submit_action']/input"));
		for (WebElement btnSave : saveBtnList) {
			if(btnSave.isDisplayed())
				btnSave.click();
		} 

		TimeUnit.SECONDS.sleep(1);
		String name = "";
		List<WebElement> txtist = getWebDriver().findElements(By.cssSelector(".element-plain-text"));
		for (WebElement title : txtist) {
			if(title.getText().equals(projectName)){
				name = title.getText();
				break;
			}
		}
		return name;
      
		
	}

	public String addFolder(String folderToCreate) throws InterruptedException {
	
        WebElement btnNewFolder = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add_folder")));

        btnNewFolder.click();
        return addFolderAction(folderToCreate);
	}



	public String addNewProjectUseDefault() throws InterruptedException {
	     WebElement btnNewProj = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new_project")));

        btnNewProj.click();
        TimeUnit.SECONDS.sleep(2);
				
		List<WebElement> txtist = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
				(By.cssSelector(".element-plain-text")));
	
		String name = txtist.get(0).getText();
		return name;
	}

	public String addFolderFromPlanned(String folderToCreate) throws InterruptedException {
		
	   WebElement btnPlanned = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toggler_future_milestones")));
	   btnPlanned.click();
	  
	   WebElement btnAddFolder = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='future_milestones']/a")));
	   btnAddFolder.click();
	   TimeUnit.SECONDS.sleep(2);
        return addFolderAction(folderToCreate);
	}

	public String addFolderFromInProgress(String folderToCreate) throws InterruptedException {
		
	   WebElement btnInProgress = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toggler_current_milestones")));
	   btnInProgress.click();
	   
	   WebElement btnAddFolder = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='current_milestones']/a")));
	   btnAddFolder.click();
	   TimeUnit.SECONDS.sleep(2);
       return addFolderAction(folderToCreate);
	}
	
	public String checkLinkedResource(String project2) throws InterruptedException {
		
		
		List< WebElement> resources = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
				(By.xpath(".//*[@class='resources']/li")));
		
		for (int i=1;i<= resources.size(); i++) {
			WebElement lblLink = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@class='resources']/li[" + i + "]/div[2]/a")));
			
			if(lblLink.getText().equals(project2)){
				lblLink.click();
				TimeUnit.SECONDS.sleep(2);
				WebElement title = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='projects_project_title_input']/span")));
				
				return title.getText();
			}
		}
		return "";
	}
	
	
	public String archivedProject() throws InterruptedException{

		WebElement btnArchive = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='doctools']/ul/li[2]/a")));
		btnArchive.click();
		TimeUnit.SECONDS.sleep(2);
		WebElement title = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".noty_text")));
		
		return title.getText();
	}
	
	
	public String activateProjectFromNotyMessage(){
		
		WebElement btnArchive = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='doctools']/ul/li[2]/a")));
		btnArchive.click();
		
		WebElement linkActivate = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@class='noty_bar']/div/span/a")));
		
		linkActivate.click();
		
		WebElement title = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".noty_text")));
		
		return title.getText();
	}
	
	public String activateProjectFromGreenMenu(){
		
		WebElement btnArchive = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='doctools']/ul/li[2]/a")));
		btnArchive.click();
		
		WebElement btnActivate = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='doctools']/ul/li[1]/a")));
		
		btnActivate.click();
		
		WebElement title = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".noty_text")));
		
		return title.getText();
	}
	
	
	
	private String addFolderAction(String folderToCreate) throws InterruptedException {
		
		TimeUnit.SECONDS.sleep(2);
        
    	executeJavascript("document.getElementsByClassName('edit_me')[0].click();");
		TimeUnit.SECONDS.sleep(2);
		
		WebElement txtName = getWebDriver().findElement(By.id("projects_milestone_title"));
		txtName.click();
		txtName.clear();
		txtName.sendKeys(folderToCreate);
		
		List<WebElement> saveBtnList = getWebDriver().findElements(By.xpath(".//*[@id='projects_milestone_submit_action']/input"));
		for (WebElement btnSave : saveBtnList) {
			if(btnSave.isDisplayed())
				btnSave.click();
		}
		
		TimeUnit.SECONDS.sleep(1);
		String name = "";
		List<WebElement> txtist = getWebDriver().findElements(By.cssSelector(".element-plain-text"));
		for (WebElement title : txtist) {
			if(title.getText().equals(folderToCreate)){
				name = title.getText();
				break;
			}
		}
		return name;
	}

	public boolean addTableToProjectDescription(String data) throws InterruptedException {
		
		boolean created = false;	
		created = addTable(getProjDescToolBarElement(),data);
		return created;
		
	}
	
	private WebElement getProjDescToolBarElement() {
		return driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='tabs-progress']/div[4]/div/ul")));
	}
	
	public boolean addFigureToFolder() throws InterruptedException{
		  
	    WebElement btnAddFigure = driverWait.until(ExpectedConditions.visibilityOfElementLocated
	    		(By.xpath(".//*[@id='main-content']/div[1]/a")));
	    btnAddFigure.click();
		
		WebElement popupDialog = getWebDriver().switchTo().activeElement();
        driverWait.until(ExpectedConditions.visibilityOf(popupDialog));
        
        getWebDriver().switchTo().activeElement();
        
        TimeUnit.SECONDS.sleep(4);
        
	    WebElement txtTitle = getWebDriver().findElement(By.id("title"));
	    txtTitle.sendKeys("test figure");
	    
	    getWebDriver().findElement(By.id("projects_figure_attachment")).click();
		String path = workingDir + LGConstants.ASSETS_FILES_DIRECTORY +  LGConstants.UPLOAD_IMAGE_TEST_FILENAME;
		uploadFile(path);
		
		save();
		TimeUnit.SECONDS.sleep(1);
		
		getWebDriver().switchTo().activeElement();
		
		List<WebElement> figuresList = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@class='figures']/li")));
		for (int i =1; i<= figuresList.size(); i++) {
			WebElement figure = getWebDriver().findElement(By.xpath(".//*[@class='figures']/li[" + i + "]/p"));
			if(figure.getText().endsWith("test figure"))
				return true;
		}
		return false;
	}

	public boolean addReactionToProjectDescription() throws InterruptedException {
		
		WebElement descToolBar = getProjDescToolBarElement();
		TimeUnit.SECONDS.sleep(1);
		WebElement reactionLoader = descToolBar.findElement(By.xpath("./li[4]/a"));
		reactionLoader.click();
		
		TimeUnit.SECONDS.sleep(2);
		
		return openMarvinJSDialogAndImport(workingDir + LGConstants.ASSETS_FILES_DIRECTORY + LGConstants.REACTION_FILES_DIRECTORY + LGConstants.REACTION_FILE_TO_IMPORT);
	}
	
	public String addTextProjectDescription(String descToTest) throws Exception {

		 return addTextToDesc(".//*[@id='tabs-progress']/div[4]/div/ul",descToTest);

	}
	
	protected String addTextToDesc(String toolBarXPath ,String descToTest) throws InterruptedException {
		
		WebElement descriptionToolBar = getWebDriver().findElement(By.xpath(toolBarXPath));
		
		TimeUnit.SECONDS.sleep(2);
		
		WebElement textLoader = descriptionToolBar.findElement(By.cssSelector(".text.load"));
		textLoader.click();
		
		writeInRedactor(descToTest);

		WebElement saveDescription = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@title='Save']")));
		saveDescription.click();
	

		WebElement text = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[starts-with(@id,'form_for_textarea_')]/span")));
		String description = text.getText();
		
		return description;
	}
	
    private void writeInRedactor(String text) throws InterruptedException{
    	
    	TimeUnit.SECONDS.sleep(1);
    	executeJavascript("textboxio.get('textarea')[1].content.set('"+text+"')");
    	TimeUnit.SECONDS.sleep(1);
     }

	public boolean addCompoundToProjectDescription() throws InterruptedException {
		
		WebElement descToolBar = getProjDescToolBarElement();
		TimeUnit.SECONDS.sleep(1);
		WebElement compoundLoader = descToolBar.findElement(By.xpath("li[3]/a"));		
		compoundLoader.click();
		TimeUnit.SECONDS.sleep(1);
		return drawCompound();
	}
	
	protected void writeInTable(String data) throws InterruptedException {
		
		String script = "var spread = $('.excel_container').find('.excel').wijspread('spread');"
				+ "var sheet = spread.getActiveSheet();"
				+ "var cell = sheet.getCell(1,1);"
				+ "cell.value('" + data + "');";	
		executeJavascript(script);
	}
	
	protected boolean addTable(WebElement procedureDescToolBar ,String data) throws InterruptedException {
		
		boolean created;
		WebElement tableLoader = procedureDescToolBar.findElement(By.cssSelector(".grid.load"));
		tableLoader.click();

		TimeUnit.SECONDS.sleep(4);
		
		writeInTable(data);
		
		TimeUnit.SECONDS.sleep(2);

		WebElement imgSaveDesc = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".fa.fa-check")));
		imgSaveDesc.click();
		TimeUnit.SECONDS.sleep(4);
		
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".delete_me")));
		
		String value = readFromTable();

		WebElement tableArea = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.cssSelector(".inline_form_container.type_excel.spreadJS_activated")));

		created = ((tableArea != null) && (data.equals(value)));
		return created;
	}
	
	protected void deleteSample() throws InterruptedException {
		  //delete all created samples
		getLogger().debug("before delete samples");
		executeJavascript("document.getElementsByClassName('delete_me')[0].click();");
		getLogger().debug("after delete samples");
		TimeUnit.SECONDS.sleep(2);
		
	}

	public String updateContent() throws Exception {
	
		addTextProjectDescription("update content to check version history");
		
		
		String msg = updateName("projects_project_title",".//*[@id='projects_project_submit_action']/input");
		return msg;
	}
	
	@Override
	public String getPageTitleXPath() {
		return ".//*[@id='projects_project_title_input']/span";
	}
	
	public boolean uploadFileFromDataProvider(String fileName) throws InterruptedException {

		TimeUnit.SECONDS.sleep(2);
		String path = workingDir + LGConstants.ASSETS_FILES_DIRECTORY  + fileName;
		TimeUnit.SECONDS.sleep(2);
		
		WebElement fileSelect = getWebDriver().findElement(By.xpath(".//*[@type='file']"));
		fileSelect.sendKeys(path);
		
		TimeUnit.SECONDS.sleep(4);

		WebElement file = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//div[@id='files']/ul/li[2]/span")));
		if (file.getText().equals(fileName)){
			return true;
		}
		return false;
	}

	
	public boolean addNoteFromNotesTab(String note) throws InterruptedException {

		clickOnTab("tabs-notes-link");
		
		clickOnButton("add_note");
		TimeUnit.SECONDS.sleep(1);
		
		//open note dialog and add data in it
		getWebDriver().switchTo().activeElement();
		
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new_projects_note")));
		
		WebElement title = getWebDriver().findElement(By.id("title"));
		title.sendKeys(note);
		
		
		writeInRedactor("This is a note description");
		
		WebElement save = getWebDriver().findElement(By.id("Save"));
		save.click();
		TimeUnit.SECONDS.sleep(2);
		
		getWebDriver().switchTo().activeElement();
		
		//wait until tab content appears again
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add_note")));
		
		return checkNoteInList(note);
	}

	
	public String addDocumentFromDocumentsTab(String projectName) throws InterruptedException{
		
		//add document to project
		clickOnTab("tabs-documents-link");
		clickOnButton("add_document");
		TimeUnit.SECONDS.sleep(3);
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("knowledgebase_document_title_input")));
		
		executeJavascript("document.getElementsByClassName('edit_me')[0].click();");
		TimeUnit.SECONDS.sleep(2);
		
		String addedDoc = GenericHelper.buildUniqueName(LGConstants.DOCUMENT_PREFIX);
		WebElement txtName = getWebDriver().findElement(By.id("knowledgebase_document_title"));
		txtName.click();
		txtName.clear();
		txtName.sendKeys(addedDoc);
		
		List<WebElement> saveBtnList = getWebDriver().findElements(By.xpath(".//*[@id='knowledgebase_document_submit_action']/input"));
		for (WebElement btnSave : saveBtnList) {
			if(btnSave.isDisplayed()){
				btnSave.click();
				TimeUnit.SECONDS.sleep(1);
			}
		} 
		
		writeInRedactor(addedDoc);

		TimeUnit.SECONDS.sleep(2);
		
		saveTextBoxIO();
		TimeUnit.SECONDS.sleep(1);
		//go back to project and check if document added
		searchInRecentlyViewedList(projectName);
		
		return checkDocumentInList(addedDoc);
		
	}
	


	
	public String addPaperFromPapersTab() throws InterruptedException {

		
		clickOnTab("tabs-key_papers-link");
		
		clickOnButton("add_paper");
		TimeUnit.SECONDS.sleep(3);
		
		String paperToAdd = addPaperFromSearchPub();
		TimeUnit.SECONDS.sleep(3);
		//back to project through breadscrumbs
		getWebDriver().findElement(By.xpath(".//*[@id='breadcrumbs']/ul/li[2]/span/a")).click();
		
		return checkPaperInList(paperToAdd);
	}



	private String addPaperFromSearchPub() throws InterruptedException {
		
		List<WebElement> papersList = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
				(By.xpath(".//*[@id='papers_list']/div")));
		
		for (int i = 1; i <= papersList.size(); i++) {
			
			WebElement icon = getWebDriver().findElement(By.xpath(".//*[@id='papers_list']/div["+ i + "]/h4/span/i"));		
			if(icon.getAttribute("class").contains("icon-greenadd")){
				WebElement paper = getWebDriver().findElement(By.xpath(".//*[@id='papers_list']/div["+ i + "]/h4/a"));
				String name = paper.getText();
				icon.click();
				TimeUnit.SECONDS.sleep(1);
				return name;
			}
		}
		
		return "No papers found";
	}
	
	protected void clickOnTab(String tabId) throws InterruptedException {
		
		WebElement tab = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(tabId)));
		tab.click();
		TimeUnit.SECONDS.sleep(1);
	}


	public String checkDuplicatedProject(String text,String paper, String note, String document) throws InterruptedException {
		
		clickOnTab("tabs-progress-link");
		getLogger().debug("check Document In List");
		String ok = checkDocumentInList(document);
		getLogger().debug("check Paper In List");
		ok += checkPaperInList(paper);
		getLogger().debug("check Note In List");
		ok += checkNoteInList(note);
		getLogger().debug("check Text In description");
		ok += checkTextInDescription(text);
		return ok;
	}
	
	
	private String checkTextInDescription(String text) throws InterruptedException {
		
		clickOnTab("tabs-progress-link");

		WebElement textInDesc = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[starts-with(@id,'form_for_textarea_')]/span")));
		if(textInDesc.getText().equals(text))
			return text;
	    return "";
		
	}
	
	private String checkPaperInList(String paperToAdd) throws InterruptedException {
		
		clickOnTab("tabs-key_papers-link");

		List<WebElement> papers = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
				(By.xpath(".//*[@id='tabs-key-papers']/div")));
		for (int i = 2; i <= papers.size(); i++) {
		
			WebElement paperElm = getWebDriver().findElement(By.xpath(".//*[@id='tabs-key-papers']/div["+ i + "]/h4/a"));		
			if(paperElm.getText().equals(paperToAdd)){
				return paperToAdd;
			}
		}
		return "";
	}


	private String checkDocumentInList(String addedDoc) throws InterruptedException {
		clickOnTab("tabs-documents-link");
		
		List<WebElement> docs = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
				(By.xpath(".//*[@id='tabs-documents']/ul/div")));
		for (int i = 1; i <= docs.size(); i++) {
		
			WebElement docElm = getWebDriver().findElement(By.xpath(".//*[@id='tabs-documents']/ul/div["+ i + "]/h4/a"));		
			if(docElm.getText().equals(addedDoc)){
				return addedDoc;
			}
		}
		return "";
	}
	
	private boolean checkNoteInList(String note) throws InterruptedException {
		
		clickOnTab("tabs-notes-link");
		
		//search for the added note
		List<WebElement> notes = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
				(By.xpath(".//*[@id='notes']/li")));
		for (int i = 1; i <= notes.size(); i++) {
		
			WebElement noteElm = getWebDriver().findElement(By.xpath(".//*[@id='notes']/li["+ i + "]/h3/a[2]"));		
			if(noteElm.getText().equals(note)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Share a project with spreadsheet data and then check if it is seen in the shared url as read-only.
	 * @param insertedData
	 * @return
	 * @throws InterruptedException
	 */
	public boolean shareProjectCheckPermissions(String insertedData) throws InterruptedException {
		
		WebElement btnShare = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.cssSelector(".share")));
		btnShare.click();
		TimeUnit.SECONDS.sleep(2);
		WebElement shareUrl = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#project_share_url")));
		String urlToOpen = shareUrl.getText();
		
		logout();
		
		waitForPageCompleteLoading();
		//go into the shared url and check that the project is read-only
		getWebDriver().get(urlToOpen);
		
		String value = readFromTable();

		WebElement tableArea = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.cssSelector(".inline_form_container_read_only>.excel_container>.excel>table")));

		boolean created = ((tableArea != null) && (insertedData.equals(value)));
		return created;

	}

}
