package com.biodata.labguru.pages.enotebook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.biodata.labguru.LGConstants;

public class ExperimentPageV2 extends AbstractNotebookPage {
	
	private static final String SUB_HTML_TAG = "sub";
	private static final String SUPER_HTML_TAG = "sup";
	private static final String DELETED_HTML_TAG = "del";
	private static final String UNDERLINE_HTML_TAG = "u";
	private static final String ITALIC_HTML_TAG = "em";
	private static final String BOLD_HTML_TAG = "strong";

	private By btnAssignLocator = By.id("assign");
	
	private final String saveAsProtocolActionId = "save_item_as_protocol";
	private final String moveItemActionId = "move_item";

	private final String setDateRangeActionId = "set_section_daterange";

	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	public boolean hasList() {
		selectExperiments();
		try{
			List<WebElement> list = (new WebDriverWait(getWebDriver(), 10)).until(ExpectedConditions
					.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@id='data']/table/tbody/tr[@class='ng-scope']")));
			return list.size() > 0;
		}catch(TimeoutException e){
			return false;
		}
	}

	@Override
	protected void clickOnResourceLink() throws InterruptedException {
		//switch to beta experiment and click on the link label there
		changeVersion(LGConstants.EXPERIMENT_BETA);
		super.clickOnResourceLink();
	}

	public String saveAsProtocol() throws InterruptedException{
		
		clickOnUpperMenuAction(saveAsProtocolActionId);
		TimeUnit.SECONDS.sleep(2);
		WebElement saveAsProtocolBtn = getWebDriver().findElement(By.xpath(".//*[@id='do_print']"));
		saveAsProtocolBtn.click();
		TimeUnit.SECONDS.sleep(2);
		switchToNewTab();
		String msg = checkForNotyMessage();
		return msg;
		
	}

	public boolean deleteExperiment(String expName) throws InterruptedException{
		
		//select delete experiment from menu
		WebElement action = getWebDriver().findElement(By.cssSelector("#link_to_confirm_delete_experiment>a"));	
		executeJavascript("arguments[0].click();",action);
		TimeUnit.SECONDS.sleep(1);

		//choose the delete btn and click
		WebElement delete =  getWebDriver().findElement(By.cssSelector("#link_to_delete_experiment>.fright>a"));
		executeJavascript("arguments[0].click();",delete);
		TimeUnit.SECONDS.sleep(2);
		//check for alerts if experiment is not saved
		checkForAlerts();
		//cleck on the approve delete button
		WebElement approveDelete = driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(By.cssSelector(".green-line-buttons.top-green-line>input")));
		approveDelete.click();
		TimeUnit.SECONDS.sleep(2);
		
		
		checkForNotyMessage();	
		boolean found = searchExperiment(expName,false/*no loading*/);
		return !found;
		
	}
	public boolean searchExperiment(String expName,boolean loadExperiment) {
		
		if(hasList()){
			
			List<WebElement> rows = getWebDriver().findElements(By.xpath(".//*[@id='data']/table/tbody/tr"));
			for (int i = 1; i <= rows.size(); i++) {
				WebElement tableRow = getWebDriver().findElement(By.xpath(".//*[@id='data']/table/tbody/tr[" + i + "]/td[3]/b/a"));
				if(tableRow.getText().equals(expName)){
					if(loadExperiment){
						tableRow.click();
						waitForPageCompleteLoading();
					}
					return true;
				}
					
			}
		}
		return false;
	}

	
	public String moveExperimentToProject(String newProject) throws InterruptedException{
		
		waitForPageCompleteLoading();
		clickOnUpperMenuAction(moveItemActionId);
		
		openMoveDialog(newProject);
		refreshPage();
		WebElement projectLink = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='breadcrumb-briefcase']/span")));
		String projectName = projectLink.getText();
		return projectName;
	}
	
	private void openMoveDialog(String nameToSelect) throws InterruptedException {
		
       WebElement moveDialog = getWebDriver().switchTo().activeElement();
       driverWait.until(ExpectedConditions.visibilityOf(moveDialog));

	   WebElement projDrop = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='s2id_projects']/a/span[2]/b")));
	   projDrop.click();
	   TimeUnit.SECONDS.sleep(1);
	   
	   List<WebElement> projInputs = getWebDriver().findElements(By.xpath(".//*[@id='select2-drop']/ul/li"));
	   for (int i = 1; i <= projInputs.size(); i++) {
		   WebElement projElm = getWebDriver().findElement(By.xpath(".//*[@id='select2-drop']/ul/li["+ i+ "]/div"));
		   if(projElm.getText().equals(nameToSelect)){
			   projElm.click();
			   TimeUnit.SECONDS.sleep(2);
			   break;
		   }
	   }

       WebElement moveBtn = getWebDriver().findElement(By.id("do_the_move"));
       moveBtn.click();
       TimeUnit.SECONDS.sleep(1);
       getWebDriver().switchTo().activeElement();
       			
       TimeUnit.SECONDS.sleep(2);
		
	}



	//TODO - need to think what to check here
	public void print() throws InterruptedException{
		WebElement linkPrint = driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(btnPrintLocator));

		linkPrint.click();
		TimeUnit.SECONDS.sleep(2);
		WebElement printBtn = getWebDriver().findElement(By.id("do_print"));
		printBtn.click();
		TimeUnit.SECONDS.sleep(1);
	}

	public String assign(String currentUser) throws InterruptedException{
		
		WebElement linkAssign = driverWait.until(ExpectedConditions.visibilityOfElementLocated(btnAssignLocator));
		linkAssign.click();
		TimeUnit.SECONDS.sleep(2);
		WebElement acountDrop = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='s2id_projects_experiment_member_id']/a/span[2]/b")));
		acountDrop.click();
		TimeUnit.SECONDS.sleep(1);
		
		List<WebElement> users = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@id='select2-drop']/ul/li")));
		for (int i = 1; i <= users.size(); i++) {
			WebElement selectedAcc = driverWait.until(ExpectedConditions.visibilityOfElementLocated
					(By.xpath(".//*[@id='select2-drop']/ul/li[" + i + "]/div")));
			if(!currentUser.equals(selectedAcc.getText())){
				selectedAcc.click();
				TimeUnit.SECONDS.sleep(2);
				break;
			}
		}
		
		
		checkForNotyMessage();
		
		WebElement newAssign = getWebDriver().findElement(By.xpath(".//*[@id='assign']/span/em"));
		String account = newAssign.getText();
		return account;
	}
	
	@Override
	public By getDuplicateLocator() {
		
		return By.id("duplicate");
	}
	

	public String getPageTitleXPath() {
		
		return ".//*[@id='projects_experiment_title_input']/span";
	}
	

	
	public boolean changeToProjectPage() throws InterruptedException{
		
		WebElement projectLink = getWebDriver().findElement(By.xpath(".//*[@id='main-content']/ul/li[1]/a"));
		String projectName = projectLink.getText();
		projectLink.click();
		TimeUnit.SECONDS.sleep(4);
		if(projectName.contains(".")){
			projectName = projectName.replace(".", "");
		}
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='projects_project_title_input']/span")));
		return getWebDriver().getTitle().startsWith(projectName);
	}

	
	
	public boolean changeToFolderPage() throws InterruptedException{
		
		WebElement folderLink = getWebDriver().findElement(By.xpath(".//*[@id='main-content']/ul/li[2]/a"));
		String folderName = folderLink.getText();
		folderLink.click();
		TimeUnit.SECONDS.sleep(5);
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='projects_milestone_title_input']/span")));
		if(folderName.contains(".")){
			folderName = folderName.replace(".", "");
		}
		return getWebDriver().getTitle().startsWith(folderName);
	}





	



	
	public boolean addNewSectionFromProtocol(String sectionIndex,String protocolToSelect) throws InterruptedException {

		addSectionFromProtocol(sectionIndex,protocolToSelect);

		String newSectionIndex = String.valueOf(Integer.valueOf(sectionIndex).intValue() + 1);
	
		List<WebElement> elements = getWebDriver().findElements(By.cssSelector("#section_" + newSectionIndex + ">div"));

		return elements.size() >0;

	}
	
	private void addSectionFromProtocol(String sectionIndex,String protocolToSelect) throws InterruptedException {
		
		List <WebElement> addSectionLinks = getWebDriver().findElements(By.cssSelector(".section_divider"));
		if(addSectionLinks.size() == 0)
			return;
		int dividerIndex = Integer.valueOf(sectionIndex).intValue();
		WebElement sectionDivider = addSectionLinks.get(dividerIndex);
		sectionDivider.click();
		
		TimeUnit.SECONDS.sleep(3);
		
		List <WebElement> addProtocolBtns = getWebDriver().findElements(By.xpath(".//*[@id='s2id_protocol_select_box']/a/span[2]/b"));
		WebElement addFromProtocolArrow = addProtocolBtns.get(dividerIndex);
		
		//click on the arrow of the protocols selection drop down
		addFromProtocolArrow.click();
		TimeUnit.SECONDS.sleep(2);
		
		//enter protocol name into the search box
		WebElement searchBox = getWebDriver().findElement(By.xpath(".//*[@id='select2-drop']/div/input"));
		searchBox.sendKeys(protocolToSelect);
		searchBox.sendKeys(Keys.ENTER);
		TimeUnit.SECONDS.sleep(2);

	}
	


	

	public void addConclusionSection() throws InterruptedException {
		
		try{
			TimeUnit.SECONDS.sleep(1);
			//click on the 'Add conclusion' button if there is no open section for it
			WebElement addConclusion = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".add_conclusion")));
			executeJavascript("arguments[0].click();",addConclusion);
			TimeUnit.SECONDS.sleep(2);
		}catch(NoSuchElementException e){
			//do nothing if the add conclusion button is missing - there is already a section open
		}
	}


	
	
	public String showExperimentVersionHistory() throws InterruptedException{
		
		//select view version historyfrom menu
		clickOnUpperMenuAction(viewVersionsActionId);	
		TimeUnit.SECONDS.sleep(2);
		switchToNewTab();
		
		WebElement titleElm = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='main-content']/div/lg-versions/div[1]/h3")));
		String title = titleElm.getText();
		
		return title;
	}
	
	public boolean sign() throws InterruptedException {
		
		WebElement linkSign = driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(By.id(signActionId)));

		linkSign.click();
		TimeUnit.SECONDS.sleep(2);
		
		WebElement proceedBtn = driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(By.id("proceed_sign_toggle")));
		proceedBtn.click();
		TimeUnit.SECONDS.sleep(5);
		driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(By.cssSelector(".signed_note.ng-binding")));
		
		WebElement signedNote = driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(By.cssSelector(".signed_note.ng-binding")));
		String msg = signedNote.getText();
		
		linkSign = driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(By.id(signActionId)));
		
		if(msg.startsWith(SIGNED_BY) && linkSign.getText().equals(REVERT_SIGNATURE))
			return true;
		return false;
	}

	/**
	 * This only applies for admin users or for the user that signed the experiment
	 */
	public boolean revertSignature() throws InterruptedException {
		//TODO:check if this is the user that signed the experiment or an Admin user
		WebElement linkSign = driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(By.id(signActionId)));

		linkSign.click();
		TimeUnit.SECONDS.sleep(2);
		WebElement proceedBtn = getWebDriver().findElement(By.id("proceed_sign_toggle"));
		proceedBtn.click();
		TimeUnit.SECONDS.sleep(3);
		
		linkSign = driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(By.id(signActionId)));
		
		if(linkSign.getText().equals("Sign"))
			return true;
		return false;
		
	}

	/**
	 * Check that all permitted actions for sign experiment are there and the not permitted are not shown.
	 * @return true if all permitted buttons are shown and the other not,false otherwise.
	 * @throws InterruptedException
	 */
	public boolean checkAllowedActionsOnSignedExp() throws InterruptedException {
		
		List<String> allowedActions = getAllowedActionsForSignResource();
		List<String> notAllowedActions = getNotAllowedActionsIds();
		
		//not allowed:deleteItemActionId,moveItemActionId
		return !allowedActions.contains(notAllowedActions);
	}

	protected List<String> getAllowedActionsForSignResource() {
	
		return new ArrayList<String>(Arrays.asList(viewVersionsActionId,saveAsProtocolActionId,duplicateActionId));
	}


	/**
	 * open the experiments list and check that the sign img is marked in the signed experiment
	 * @return true if sign is in place
	 * @throws InterruptedException
	 */
	public boolean checkSignedImgInList() throws InterruptedException {
		
		selectExperiments();
		
		WebElement dropdown = getWebDriver().findElement(By.xpath(".//members-select/div/a/span[3]/b"));
		dropdown.click();
		TimeUnit.SECONDS.sleep(1);
		
		WebElement input = getWebDriver().findElement(By.xpath(".//*[@id='experiments-index']/div[2]/h4/members-select/div/div/div/input"));
		input.sendKeys("All Members");
		input.sendKeys(Keys.ENTER);
		TimeUnit.SECONDS.sleep(2);
		
		try {
			//look for the sign mark
			driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ng-scope>img")));
			return true; //sign mark found
		} catch (NoSuchElementException e) {
			return false;//sign mark is missing
		}

	}

	public boolean deleteSection(String sectionIndex) throws InterruptedException {
		
		//check number of sections before delete
		List<WebElement> sections = getWebDriver().findElements(By.xpath(".//*[@id='side_nav_container']/ul[1]/li"));
		int numOfSections = sections.size();
		selectSection(sectionIndex);
		clickOnSectionMenuAction(sectionIndex, deleteSectionActionId);
		TimeUnit.SECONDS.sleep(2);
		
		executeJavascript("$('.alert_button.confirm')[" + sectionIndex +"].click()");
		TimeUnit.SECONDS.sleep(2);
		sections = getWebDriver().findElements(By.xpath(".//*[@id='side_nav_container']/ul[1]/li"));
		return (numOfSections == sections.size()+1) ;
	}

	public String addInlineTag(String tagName) throws InterruptedException {
		
		addOneTag(tagName);
		
		TimeUnit.SECONDS.sleep(5);
		
		return findInlineTag(tagName);	
	}
	


	/**
	 * Go over all inline tags and find the one to search.click on it,see that it is open ok.
	 * @param tagName
	 * @return the tag show page title
	 * @throws InterruptedException 
	 */
	public String findInlineTag(String tagName) throws InterruptedException {
		
		List<WebElement> inlineTags = getWebDriver().findElements(By.xpath(".//*[@class='inline_tags']/inline-tag"));
		for (int i = 1; i <= inlineTags.size(); i++) {
			WebElement lblTag = getWebDriver().findElement(By.xpath(".//*[@class='inline_tags']/inline-tag[" + i  + "]/span"));
			if(lblTag.getText().equals(tagName)){
				//click on the link to the tag
				lblTag.click();
				TimeUnit.SECONDS.sleep(3);
				waitForPageCompleteLoading();
				switchToNewTab();
				TimeUnit.SECONDS.sleep(1);
				//check that the tag page appears
				WebElement tagTitle = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".tagged-entity")));
				return tagTitle.getText();
			}
		}
		return "No match";
	}

	private void addOneTag(String tagPrefix) throws InterruptedException {
		
		WebElement inlineTagSpan = getWebDriver().findElement(By.className("inline_tag_input")); 
		inlineTagSpan.click();
		inlineTagSpan.sendKeys(tagPrefix);
		inlineTagSpan.sendKeys(Keys.ENTER);
		TimeUnit.SECONDS.sleep(1);
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".fa.fa-close")));
		TimeUnit.SECONDS.sleep(1);
	}

	/**
	 * delete inline tag by name
	 * @param tagName
	 * @return true if delete succeeded,false otherwise.
	 * @throws InterruptedException 
	 */
	public boolean deleteTagFromInlineTags(String tagName) throws InterruptedException {
		
		WebElement title = getWebDriver().findElement(By.xpath(".//*[@id='page-title']/span"));
		
		if(title.getText().equals(tagName)){
			//go back to the experiment page
			WebElement expLink = getWebDriver().findElement(By.xpath(".//*[@id='main-content']/div[1]/ul/li[3]/span[1]/a"));
			expLink.click();
			TimeUnit.SECONDS.sleep(1);
		}else{
			getLogger().debug("not in correct page - Tagged Entities - " + tagName);
			return false;
		}
		
		List<WebElement> inlineTags = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
				(By.xpath(".//*[@class='inline_tags']/inline-tag")));
		for (int i = 1; i <= inlineTags.size(); i++) {
			WebElement tag = getWebDriver().findElement(By.xpath(".//*[@class='inline_tags']/inline-tag[" + i  + "]/span"));
			if(tag.getText().equals(tagName)){
				//delete the tag
				getWebDriver().findElement(By.xpath(".//*[@class='inline_tags']/inline-tag[" + i  + "]/span/i")).click();
				return true;
			}		
		}
		return false;
	}

	public boolean deleteAttachmentContainer(String sectionIndex) throws InterruptedException {
		
		WebElement deleteAttachmentAction = getWebDriver().findElement(By.xpath(".//*[@id='section_" + sectionIndex + "']/div[2]/div/element/p/span[1]/i"));
		executeJavascript("arguments[0].click();",deleteAttachmentAction);
		
		//after delete attachment - the 'Attachments' area is missing
		try {
			getWebDriver().findElement(By.xpath(".//*[@id='section_" + sectionIndex + "']/div[2]/div/attachments-element/header"));
			return false;
		} catch (NoSuchElementException e) {
			return true;
		}

	}
	
	public boolean deleteSingleAttachmentFromSection(String sectionIndex) throws InterruptedException{
		
		WebElement action = getWebDriver().findElement(By.xpath
				(".//*[starts-with(@id,'attachment_section_')]/section/div/div/ul/li[3]/a"));
		executeJavascript("arguments[0].click();",action);
		TimeUnit.SECONDS.sleep(3);
		
		try {
			getWebDriver().findElement(By.id("drop_in_attachment_"));
			return true;
		} catch (Exception e) {
			getLogger().debug(e.getMessage());
			return false;
		}	
	}

	public boolean addToPage(String sectionIndex,String fileType) throws InterruptedException {
		
		selectSection(sectionIndex);
		WebElement action = getWebDriver().findElement(By.id("add_to_page"));
		executeJavascript("arguments[0].click();",action);
		TimeUnit.SECONDS.sleep(3);
		
		boolean finishLoadingElement = false;
		
		//wait until the excel page is loaded
		while(!finishLoadingElement){
			try{
				//if the pending message appears - keep waiting
				getWebDriver().findElement(By.cssSelector(".pending_element"));
				finishLoadingElement= false;
			} catch (NoSuchElementException e) {
				//no pending message - stop waiting and check the loaded element
				finishLoadingElement= true;
			}	
		}
		
		boolean succeeded = false;
		TimeUnit.SECONDS.sleep(2);
		if(fileType.equals(LGConstants.UPLOAD_XLS_TEST_FILENAME)){
			List <WebElement> elements = getWebDriver().findElements(By.cssSelector(".element_container.excel_element"));
			succeeded = elements.size() > 0;
		}
		else {//pdf file
			List <WebElement> elements = getWebDriver().findElements(By.cssSelector(".element_container.pdf_element"));
			succeeded =  elements.size() > 0;
		}

		return succeeded;
		
	}


	public String addInlineCommentToSection(String sectionIndex,String comment,String platform) throws InterruptedException {
		
		selectTextAndSelectAll(sectionIndex, platform);
		
		//add comment
		return addComment(sectionIndex,comment);
		
	}


	protected void selectTextAndSelectAll(String sectionIndex, String platform) throws InterruptedException {
		selectSection(sectionIndex);

		//select All text
		selectAllText(platform, sectionIndex);
	}

	/**
	 * Perform COMMAND+"a" to select all text in editor
	 * @param sectionIndex
	 * @throws InterruptedException
	 */
	private void selectAllText(String platform, String sectionIndex ) {
		
		WebElement textArea = getWebDriver().findElement(By.xpath(".//*[@id='section_" + sectionIndex + "']/div/div/element/div/div/p"));
		if(platform.equals(Platform.WINDOWS))
			textArea.sendKeys(Keys.chord(Keys.CONTROL, "a"));
		else if(platform.equals(Platform.MAC))
			textArea.sendKeys(Keys.chord(Keys.COMMAND, "a"));
	}

	private String addComment(String sectionIndex,String comment) throws InterruptedException {
		
		String script = "$('#section_toolbar_" + sectionIndex + ">ul>li#" + sectionCommentActionBarId +"').mousedown();";
		executeJavascript(script);
		TimeUnit.SECONDS.sleep(1);
		WebElement commentText = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("comment_text_")));
		commentText.sendKeys(comment);
		
		WebElement saveComment = getWebDriver().findElement(By.cssSelector(".comment_block>div.lg_comment_buttons>a.inline_submit"));
		saveComment.click();
		TimeUnit.SECONDS.sleep(1);

		return checkIfCommentCreated();
	}

	private String checkIfCommentCreated() throws InterruptedException {
		
		refreshPage();
		
		WebElement newComment = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.cssSelector(".inline_comments_container>div>comment>div>p")));
		String txt = newComment.getText();
		return txt;
	}

	//TODO - not working
	public void setDateRangeToProcedure(String sectionIndex) throws InterruptedException {
		
		selectSection(sectionIndex);
		
		clickOnSectionMenuAction(sectionIndex, setDateRangeActionId);
		
		getWebDriver().switchTo().activeElement();
		driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[starts-with(@id,'edit_experiment_procedure_')]")));
        TimeUnit.SECONDS.sleep(2);
        
        //select start date
        WebElement startDate = getWebDriver().findElement(By.xpath("//*[@id='experiment_procedure_start_date_input']/input"));
        startDate.click();
        TimeUnit.SECONDS.sleep(1);
        getWebDriver().switchTo().activeElement();
        
        List <WebElement> datePickers = getWebDriver().findElements
        		(By.cssSelector(".xdsoft_datetimepicker.xdsoft_.xdsoft_noselect"));
		for (int i = 1; i <= 5; i++) {
			 try{
				 WebElement todaySelect = getWebDriver().findElement(
						 By.xpath(".//*[@class='xdsoft_calendar']/table/tbody/tr[" + i + "]/td[contains(@class,'xdsoft_today')]"));
				 todaySelect.click();
				 TimeUnit.SECONDS.sleep(1);
				 break;
			 }catch(NoSuchElementException e){
				 //do nothing - keep searching for today's date
				 continue;
			 }
		}
        
		TimeUnit.SECONDS.sleep(1);
		getWebDriver().switchTo().activeElement();
		startDate = getWebDriver().findElement(By.xpath("//*[@id='experiment_procedure_start_date_input']/input"));
		startDate.click();
		TimeUnit.SECONDS.sleep(1);
        //select checkbox 'all day'
		WebElement allDaychk = getWebDriver().findElement(By.xpath(".//*[@type='checkbox']"));
		allDaychk.click();
		TimeUnit.SECONDS.sleep(1);
		
		//save
		WebElement saveBtn = getWebDriver().findElement(By.xpath(".//*[@id='experiment_procedure_submit_action']/input"));
		saveBtn.click();
		TimeUnit.SECONDS.sleep(1);
		getWebDriver().switchTo().activeElement();
		
		saveSection(sectionIndex);
		
		//check that the date appears in the section
		WebElement dateRangeLabel = getWebDriver().findElement
				(By.cssSelector(".section_date_range.ng-binding"));
		
		String date = dateRangeLabel.getText();
	}

	/**
	 * Check simple manipulation on the text in redactor: bold,underline,italic, delete,sub script and superscript
	 * @param sectionIndex
	 * @param text 
	 * @return true if all manipulation succeeded
	 * @throws InterruptedException
	 */
	public boolean manipulateTextWithFontAction(String sectionIndex,String text, String platform) throws InterruptedException {
		
		selectTextAndToggleFontAction(sectionIndex,platform);
		
		checkTextForAction(sectionIndex,"re-bold",BOLD_HTML_TAG,platform);//check bold
		checkTextForAction(sectionIndex,"re-italic",ITALIC_HTML_TAG,platform);//check italic
		checkTextForAction(sectionIndex,"re-underline",UNDERLINE_HTML_TAG,platform);//check underline
		checkTextForAction(sectionIndex,"re-deleted",DELETED_HTML_TAG,platform);//check deleted
		checkTextForAction(sectionIndex,"re-superscript",SUPER_HTML_TAG,platform);//check super script
		checkTextForAction(sectionIndex,"re-subscript",SUB_HTML_TAG,platform);//check sub script
		return true;
	}

	private void selectTextAndToggleFontAction(String sectionIndex,String platform) throws InterruptedException {
		
		selectTextAndSelectAll(sectionIndex, platform);
		
		clickOnSectionActionBar(sectionIndex, sectionFontActionBarId);
		TimeUnit.SECONDS.sleep(2);
		
	}

	protected void checkTextForAction(String sectionIndex,String relClass,String tagToCheck,String platform) throws InterruptedException {

		//click on the action to activate (bold,italic ,etc)
		String script = "$('#section_toolbar_" + sectionIndex + ">div>ul>li>.re-icon." + relClass + "').click();";
		executeJavascript(script);
		TimeUnit.SECONDS.sleep(2);

		//check that the text is as ment to be
		try {
			getWebDriver().findElement(By.xpath
					(".//*[@id='section_" +sectionIndex+ "']/div/div/element/div/div/p/*[@data-redactor-tag='" +tagToCheck+ "']"));	
			TimeUnit.SECONDS.sleep(1);
			//revert action
			executeJavascript(script);
			TimeUnit.SECONDS.sleep(2);
		} catch (Exception e) {
			//element not found- toolbar action did not succeeded
			getLogger().info(e.getMessage());
			Assert.fail(relClass + " action did not succeeded");
		}
	}
	
	
	public String updateContent() throws InterruptedException {
	
		return updateName("projects_experiment_title",".//*[@id='projects_experiment_submit_action']/input");
	}
}
