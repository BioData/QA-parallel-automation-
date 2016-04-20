package com.biodata.labguru.pages.enotebook;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.GenericHelper;
import com.biodata.labguru.LGConstants;
import com.biodata.labguru.model.Sample;
import com.biodata.labguru.pages.AdminPage;
import com.biodata.labguru.pages.IListView;



public abstract class AbstractNotebookPage extends AdminPage implements IListView{

	
	protected static final String REVERT_SIGNATURE = "Revert Signature";
	protected static final String SIGNED_BY = "Signed by";
	
	public abstract String getPageTitleXPath();
	
	final String newExperimentButtonId = "new_experiment_popup_btn";
	
	protected final String signActionId = "sign_toggle";
	protected By btnPrintLocator = By.id("print");

	
	protected final String duplicateActionId = "duplicate_item";	
	protected final String viewVersionsActionId ="view_version_history";
	protected final String deleteItemActionId = "delete_item";
	
	protected final String sectionFontActionBarId = "toggle_redactor_toolbar";
	protected final String sectionCommentActionBarId = "section_comments";
	protected final String sectionAttachmentsActionBarId = "section_attachments";
	protected final String sectionLinksActionBarId = "section_links";


	protected final String addTableActionId = "add_section_table_element";
	protected final String addStepsActionId = "add_section_steps_element";
	protected final String addSamplesActionId = "add_section_samples_element";
	protected final String addReactionActionId = "add_section_reaction_element";
	protected final String addCompoundActionId = "add_section_compound_element";
	protected final String moveUpSectionActionId = "move_section_up";
	protected final String moveDownSectionActionId = "move_section_down";
	protected final String deleteSectionActionId = "delete_section";

	
	public  By getDuplicateLocator() {
		return By.id("duplicate");
	}
	
	public String getTitle() {
		WebElement pageTitle = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(getPageTitleXPath())));
        String newName = pageTitle.getText();
        return newName;
	}
	
	protected boolean clickOnUpperMenuAction(String actionId) throws InterruptedException {
		
		WebElement action = getWebDriver().findElement(By.xpath(".//*[@id='more_actions']/ul/li[@id='" + actionId + "']"));	
		executeJavascript("arguments[0].click();",action);
		TimeUnit.SECONDS.sleep(1);
		return true;
	}

	protected List<String> getNotAllowedActionsIds() {
	
		List<String> notAllowedActionsIds = new ArrayList<String>();
		List <WebElement> notAllowedActions = getWebDriver().findElements(By.cssSelector("#more_actions>ul>li.ng-hide"));
		for (WebElement action : notAllowedActions) {
			String id = action.getAttribute("id");
			notAllowedActionsIds.add(id);
		}
		return notAllowedActionsIds;	
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
	
	public String duplicate() throws InterruptedException{
		
		clickOnUpperMenuAction(duplicateActionId);	
		TimeUnit.SECONDS.sleep(2);
		WebElement duplicateBtn = getWebDriver().findElement(By.xpath(".//*[@id='do_print']"));
		duplicateBtn.click();
		TimeUnit.SECONDS.sleep(2);
		switchToNewTab();
		checkForNotyMessage();
		
		WebElement title = getWebDriver().findElement(By.xpath(getPageTitleXPath()));
	    String newName = title.getText();
		return newName;
		
	}
	
	protected void clickOnSectionMenuAction(String sectionIndex,String actionId) throws InterruptedException {
		
		WebElement action = getWebDriver().findElement(By.xpath(".//*[@id='section_toolbar_" + sectionIndex + "']/ul/li[@id='more_section_actions']/ul/li[@id='" + actionId + "']/a"));
		executeJavascript("arguments[0].click();",action);
		TimeUnit.SECONDS.sleep(2);
	}
	
	protected void clickOnSectionActionBar(String sectionIndex,String actionId) throws InterruptedException {
		String script = "$('#section_toolbar_" + sectionIndex + ">ul>li#" + actionId + ">i').click();";
		executeJavascript(script);
		TimeUnit.SECONDS.sleep(2);
	}

	
	public boolean addCompoundToSection(String sectionIndex) throws InterruptedException {
		
		selectSection(sectionIndex);

		clickOnSectionMenuAction(sectionIndex, addCompoundActionId);
		TimeUnit.SECONDS.sleep(2);
		return drawCompound(sectionIndex);
	}
	
	private boolean drawCompound(String sectionIndex) throws InterruptedException {
		
		boolean created;
		// draw something
		drawBenzene();
		WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("compound_name")));
		txtName.clear();
		txtName.sendKeys(GenericHelper.buildUniqueName("compound_"));

		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btn-getmol")));
		WebElement btnSaveComp = getWebDriver().findElement(By.id("btn-getmol"));
		btnSaveComp.click();
		TimeUnit.SECONDS.sleep(3);

		getWebDriver().switchTo().activeElement();
		
		saveSection(sectionIndex);
		try {
			WebElement compoundImg = driverWait.until(ExpectedConditions.visibilityOfElementLocated
					(By.xpath(".//*[@id='section_" + sectionIndex + "']/div[@class='element_container compound_element']")));
			created = (compoundImg != null);

		} catch (Exception e) {
			created = false;
		}
		return created;
	}
	

	public boolean addTableToSection(String data,String sectionIndex) throws InterruptedException {

		boolean created = false;
		
		selectSection(sectionIndex);
		
		clickOnSectionMenuAction(sectionIndex, addTableActionId);
	
		writeInTableV2(data,sectionIndex);
		
		TimeUnit.SECONDS.sleep(2);

		saveSection(sectionIndex);
		
		String value = readFromTableV2(sectionIndex);

		WebElement tableArea = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.cssSelector(".element_container.excel_element")));

		created = ((tableArea != null) && (data.equals(value)));
		return created;
	}
	
	private void writeInTableV2(String data, String sectionIndex) throws InterruptedException {
		
		String script = "var spread = $('#section_" + sectionIndex + ">.excel_element').find('.excel').wijspread('spread');"
				+ "var sheet = spread.getActiveSheet();"
				+ "var cell = sheet.getCell(1,1);"
				+ "cell.value('" + data + "');";	
		executeJavascript(script);
		
	}
	
	private String readFromTableV2(String sectionIndex) throws InterruptedException {
		
		String script = "var spread = $('#section_" + sectionIndex + ">.excel_element').find('.excel').wijspread('spread');"
				+ "var sheet = spread.getActiveSheet();"
				+ "var cell = sheet.getCell(1,1);"
				+ "return cell.value();";	
		 
		return (String) executeJavascript(script);
	}

	
	public String addNewSection(String sectionIndex,String sectionName) throws InterruptedException {

		addSection(sectionIndex);

		String newSectionIndex = String.valueOf(Integer.valueOf(sectionIndex).intValue() + 1);
		
		WebElement textArea = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='section_" + newSectionIndex +"']/h3/input")));
		sendKeys(textArea, sectionName);
		TimeUnit.SECONDS.sleep(1);
		saveSection(newSectionIndex);//the new section should be saved (index+1)		
		
		String text = getSavedSectionTitle(newSectionIndex);

		return text;

	}
	
	protected String getSavedSectionTitle(String sectionIndex) throws InterruptedException {
		
		String script = "var text = $($('.section_title')["+ sectionIndex + "]).find('input').val(); return text;";
		String  text = (String) executeJavascript(script);
		return text;
	}
	
	
	protected void saveSection(String sectionIndex) throws InterruptedException {
		
		WebElement btnSave = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='section_toolbar_" + sectionIndex + "']/input[@class='inline_submit']")));
		btnSave.click();
		TimeUnit.SECONDS.sleep(1);
	}
	
	protected void selectSection(String sectionIndex) throws InterruptedException {

		WebElement sectionArea = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("section_" + sectionIndex )));
		sectionArea.click();
		TimeUnit.SECONDS.sleep(2);
	}
	
	
	protected void addSection(String sectionIndex) throws InterruptedException {
		
		List <WebElement> addSectionLinks = getWebDriver().findElements(By.cssSelector(".section_divider"));
		if(addSectionLinks.size() == 0)
			return;
		int dividerIndex = Integer.valueOf(sectionIndex).intValue();
		WebElement sectionDivider = addSectionLinks.get(dividerIndex);
		sectionDivider.click();

		TimeUnit.SECONDS.sleep(3);
		
		List <WebElement> dividers = getWebDriver().findElements(By.cssSelector(".add_section_button"));
		WebElement btnAddSection = dividers.get(dividerIndex);
		btnAddSection.click();

		TimeUnit.SECONDS.sleep(2);
	}
	

	
	public void changeVersion(String version) throws InterruptedException {
		
		WebElement versionBtn;
		try {
			//new account - will open experiment on beta version by default
			versionBtn = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("link_to_v1")));
			
			if(version.equals(LGConstants.EXPERIMENT_CURRENT)){
				
				versionBtn.click();
				TimeUnit.SECONDS.sleep(3);
			}
		
			
		} catch (Exception e) {
			getLogger().debug("old account - should open on current version by default, switching to beta...");
			if(version.equals(LGConstants.EXPERIMENT_CURRENT)){
				//if we need current version - do nothing
				return;
			}
			versionBtn = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("link_to_beta")));
			versionBtn.click();
			TimeUnit.SECONDS.sleep(3);
		}
		
		
	}
	
	protected String readFromTable() throws InterruptedException {
		
		String script = "var spread = $('.excel_container').find('.excel').wijspread('spread');"
				+ "var sheet = spread.getActiveSheet();"
				+ "var cell = sheet.getCell(1,1);"
				+ "return cell.value();";	
		 
		return (String) executeJavascript(script);
	}
	

	
	protected void saveSample() throws InterruptedException {
		
		executeJavascript("document.getElementsByClassName('samples_save_button')[0].click();");
		TimeUnit.SECONDS.sleep(3);    
	}
	

	
	public boolean openMarvinJSDialogAndImport(String pathToFile) throws InterruptedException {

		String parentWindow = getWebDriver().getWindowHandle();
		boolean created = false;
		TimeUnit.SECONDS.sleep(1);

		// open the canvas for drawing
		WebElement sketch = driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(By.id("sketch")));
		TimeUnit.SECONDS.sleep(2);
		WebDriver iframe = getWebDriver().switchTo().frame(sketch);
		TimeUnit.SECONDS.sleep(4);

		WebElement imgImport = iframe.findElement(By.xpath(".//div[starts-with(@title,'Import')]"));
		imgImport.click();
		TimeUnit.SECONDS.sleep(2);

		WebElement fileSelect = getWebDriver().findElement(By.cssSelector(".gwt-FileUpload"));
		fileSelect.sendKeys(pathToFile);

		TimeUnit.SECONDS.sleep(2);
		
		iframe.switchTo().parentFrame();

		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By
				.id("btn-getmol")));
		WebElement btnSaveComp = getWebDriver().findElement(By.id("btn-getmol"));
		btnSaveComp.click();

		TimeUnit.SECONDS.sleep(15);
		getWebDriver().switchTo().window(parentWindow);	
		
		TimeUnit.SECONDS.sleep(8);

		try {
			WebElement reactionImg = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By
							.xpath(".//*[@class='reaction ng-scope']/stoichiometric-table/img")));
			created = (reactionImg != null);

		} catch (NoSuchElementException e) {
			created = false;
		}
		return created;
	}
	
	
	protected String addTextToDesc(String toolBarXPath ,String descToTest) throws InterruptedException {
		
		WebElement descriptionToolBar = getWebDriver().findElement(By.xpath(toolBarXPath));
		
		TimeUnit.SECONDS.sleep(2);
		
		WebElement textLoader = descriptionToolBar.findElement(By.cssSelector(".text.load"));
		textLoader.click();
		
		writeInRedactor("element_data", descToTest);

		List<WebElement> saveImgList = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".re-save_button")));
		for (WebElement imgSave : saveImgList) {
			imgSave.click();
		}
	

		WebElement text = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='element_data_input']/span/p")));
		String description = text.getText();
		
		return description;
	}
	
	protected boolean drawCompound() throws InterruptedException {
		
		boolean created;
		// draw something
		drawBenzene();
		WebElement txtName = getWebDriver().findElement(By.id("compound_name"));
		txtName.clear();
		txtName.sendKeys("CompoundTest3");

		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btn-getmol")));
		WebElement btnSaveComp = getWebDriver().findElement(By.id("btn-getmol"));
		btnSaveComp.click();
		TimeUnit.SECONDS.sleep(15);
		//wait until sketch dialog is closed
		driverWait.until(ExpectedConditions.not(ExpectedConditions.visibilityOfElementLocated(By.id("sketch"))));
		getWebDriver().switchTo().activeElement();
		try {
			WebElement compoundImg = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@class='compound']/img")));
			created = (compoundImg != null);

		} catch (NoSuchElementException e) {
			created = false;
		}
		return created;
	}

	protected void drawBenzene() throws InterruptedException {
		
		WebElement sketch = driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(By.id("sketch")));
		
		TimeUnit.SECONDS.sleep(5);
		
		WebDriver iframe = getWebDriver().switchTo().frame(sketch);
		TimeUnit.SECONDS.sleep(3);

		List<WebElement> toolBarList = iframe.findElements(By.className("mjs-toolbar"));
		for (WebElement toolBar : toolBarList) {
			try {
				WebElement imgElm = toolBar.findElement(By
						.xpath("//tbody/tr/td[last()]/div[@title='Benzene']"));// index=8
				imgElm.click();
				TimeUnit.SECONDS.sleep(2);
				break;
			} catch (Exception e) {
				// do nothing
			}
		}

		TimeUnit.SECONDS.sleep(3);

		WebElement canvas = iframe.findElement(By.cssSelector("#canvas"));
		canvas.click();

		TimeUnit.SECONDS.sleep(3);

		iframe.switchTo().parentFrame();
	}


	public String createExperimentFromSelectedProtocol(String protocol) throws InterruptedException {
		
		
	   WebElement newExpDialog = getWebDriver().switchTo().activeElement();
	   driverWait.until(ExpectedConditions.visibilityOf(newExpDialog));
	        
		WebElement dropbox = getWebDriver().findElement(By.xpath(".//*[@id='s2id_protocol_select_box']/a"));
		dropbox.click();
		TimeUnit.SECONDS.sleep(1);
		
		
		List<WebElement> listProtocols = getWebDriver().findElements(By.xpath(".//*[@id='select2-drop']/ul/li"));
		for (int i = 1; i <= listProtocols.size(); i++) {
			
			WebElement selectedProtocol = getWebDriver().findElement(By.xpath(".//*[@id='select2-drop']/ul/li["+ i + "]/div"));
			if(selectedProtocol.getText().equals(protocol)){
				selectedProtocol.click();			
				TimeUnit.SECONDS.sleep(1);
				break;
			}
		}
		
        WebElement btnAdd = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@value='Add Experiment']")));
        btnAdd.click();
        
        getWebDriver().switchTo().activeElement();
       			
		TimeUnit.SECONDS.sleep(2);
		return getWebDriver().getTitle();
	}
	
	public String openExperiment(String expName) throws InterruptedException {
		
		selectExperiments();
		
		List<WebElement> expList = driverWait.until(ExpectedConditions
				.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@id='data']/table/tbody/tr")));
		for (int i = 1; i <= expList.size(); i++) {
			WebElement expToOpen = getWebDriver().findElement(By.xpath(".//*[@id='data']/table/tbody/tr[" + i + "]/td[3]/b/a"));
			if(expToOpen.getText().equals(expName)){
				expToOpen.click();
				TimeUnit.SECONDS.sleep(1);
				break;
			}
		}
		
		
		driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath(".//*[@id='projects_experiment_title_input']/span")));
		return getWebDriver().getTitle();
	}
	

	
	public boolean deleteElementInSection(String sectionIndex) throws InterruptedException {
		selectSection(sectionIndex);
		deleteElementFromTrashIcon(sectionIndex);
		//check that undo appears
		try {
			getWebDriver().findElement
					(By.xpath(".//*[@id='section_toolbar_"+ sectionIndex + "']/span/a"));
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}	
	}
	
	public boolean undoDeleteElementActionInSection(String sectionIndex) throws InterruptedException {
	
		selectSection(sectionIndex);
		List<WebElement> stepsList = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
				(By.cssSelector(getStepsListInSection(sectionIndex))));
		
		int steps = stepsList.size();
		selectSection(sectionIndex);
		deleteElementFromTrashIcon(sectionIndex);
		//check that undo appears
		try {
			WebElement undoElm = getWebDriver().findElement
					(By.xpath(".//*[@id='section_toolbar_"+ sectionIndex + "']/span/a"));
			undoElm.click();
			TimeUnit.SECONDS.sleep(2);
			stepsList = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
					(By.cssSelector(getStepsListInSection(sectionIndex))));
			return (stepsList.size() ==  steps);
			
		} catch (NoSuchElementException e) {
			return false;
		}	
	}

	protected String getStepsListInSection(String sectionIndex) {
		return "#section_"+ sectionIndex + ">.steps_element>div>.steps-element>div>table>tbody>tr";
	}

	private void deleteElementFromTrashIcon(String sectionIndex)
			throws InterruptedException {
		
		WebElement trashIcon = getWebDriver().findElement(By.xpath(".//*[@id='section_"+ sectionIndex + "']/div[2]/div/element/p/span[1]/i"));
		trashIcon.click();
		TimeUnit.SECONDS.sleep(1);

	}

	public boolean addSingleStepToSection(String sectionIndex) throws InterruptedException {
		
		selectSection(sectionIndex);

		clickOnSectionMenuAction(sectionIndex, addStepsActionId);
		
		addStep(sectionIndex);
		
		selectSection(sectionIndex);
		
		WebElement addStepBtn = getWebDriver().findElement(By.xpath(".//*[@id='section_"+ sectionIndex + "']/div[2]/div/element/div/a"));
		addStepBtn.click();
		TimeUnit.SECONDS.sleep(1);
		
		//write in step 4
		executeJavascript("$('#section_"+ sectionIndex + ">.element_container>div>element.steps-element>div>table>tbody>tr:nth-of-type(" + 4 + ")>td>div.redactor-box>div')"
					+ ".redactor('code.set', '<p>test step editor: 4 </p>');");
		saveSection(sectionIndex);
		
		List <WebElement> createdStepsList = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
					(By.cssSelector(getStepsListInSection(sectionIndex))));
		
		return createdStepsList.size() == 4;//we added 1 more step to the 3 existed steps
	}
	
	public boolean addStepToSection(String sectionIndex) throws InterruptedException {

		selectSection(sectionIndex);

		clickOnSectionMenuAction(sectionIndex, addStepsActionId);
		
		return addStep(sectionIndex);

	}
	
	

	
	private boolean addStep(String sectionIndex)throws InterruptedException {
		
		int created = 0;
		int steps = 0;
		List<WebElement> stepsList = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
				(By.cssSelector(getStepsListInSection(sectionIndex))));
		for (int i = 1; i <= stepsList.size(); i++) {
			executeJavascript("$('#section_"+ sectionIndex + ">.steps_element>div>.steps-element>div>table>tbody>tr:nth-of-type(" + i + ")>td>div.redactor-box>div')"
					+ ".redactor('code.set', '<p>test step editor: " + i + "</p>');");
			steps++;
		}
		
		saveSection(sectionIndex);
		try{
			List <WebElement> createdStepsList = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
					(By.cssSelector(getStepsListInSection(sectionIndex))));
			for (int i = 1; i <= createdStepsList.size(); i++) {
				WebElement input = getWebDriver().findElement
						(By.cssSelector("#section_"+ sectionIndex + ">.steps_element>div>.steps-element>div>table>tbody>tr:nth-of-type(" + i + ")>"
								+ "td>div.redactor-box>div.redactor-editor>p"));
				if(!input.getText().isEmpty()){
					created ++;	
				}
			}
		}catch(Exception e){
			//no steps found - test failed
			return false;
		}

		return (created == steps);
	}
	
	public boolean deleteStepsOfSection(String sectionIndex) throws InterruptedException {

		selectSection(sectionIndex);
		
		getWebDriver().findElement(By.cssSelector(".steps.styled_table>tbody")).click();
		
		try{
			List <WebElement> createdStepsList = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
					(By.cssSelector(".steps.styled_table>tbody>tr")));
			for (int i = createdStepsList.size(); i >= 1 ; i--) {
				WebElement btnDelete  = getWebDriver().findElement(By.cssSelector(".steps.styled_table>tbody>tr:nth-of-type(1)>td>i.fa.fa-close"));
				executeJavascript("arguments[0].click();", btnDelete);
				TimeUnit.SECONDS.sleep(1);
			}
		}catch(Exception e){
			//no steps found - test failed
			return false;
		}
	
		saveSection(sectionIndex);
		try{
			getWebDriver().findElement(By.cssSelector(".steps.styled_table>tbody>tr"));
			return false;
		}catch(Exception e){
			//should get here after delete steps
			return true;
		}
	}
	
	public String addLinkToSection(String sectionIndex,String resourceToLink) throws InterruptedException {

		selectSection(sectionIndex);
		
		clickOnSectionActionBar(sectionIndex, sectionLinksActionBarId);
		
		selectResourceFromDialog(resourceToLink);
		saveSection(sectionIndex);	
		TimeUnit.SECONDS.sleep(2);
		
		WebElement link = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.cssSelector(".link_name.ng-binding")));
		if (link.getText().startsWith(resourceToLink)){
			link.click();
			TimeUnit.SECONDS.sleep(2);
			
			return getWebDriver().getTitle();
		}
		return "";
	}
	
	
		
	private void selectResourceFromDialog(String resourceToLink) throws InterruptedException {
		
	      getWebDriver().switchTo().activeElement();
	      
	      driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search_term")));

	       //search for the given resource
		   WebElement searchInput = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search_term")));
		   searchInput.sendKeys(resourceToLink);
		   searchInput.sendKeys(Keys.ENTER);
		   TimeUnit.SECONDS.sleep(2);

		   
		  List<WebElement> resources = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
				  (By.cssSelector(".link_resource")));
		  for (WebElement resource : resources) {
			   resource.click();
			   TimeUnit.SECONDS.sleep(2); 
			   break;
		  }
  
	       WebElement AddLinkBtn = getWebDriver().findElement(By.id("add_links"));
	       AddLinkBtn.click();
	       TimeUnit.SECONDS.sleep(2);
	       
	       getWebDriver().switchTo().activeElement();
	       			
	       TimeUnit.SECONDS.sleep(2);
	}

	public void uploadAttachmentToSection(String sectionIndex,String fileNameToUpload) throws InterruptedException {

		selectSection(sectionIndex);
		
		clickOnSectionActionBar(sectionIndex, sectionAttachmentsActionBarId);
		
		TimeUnit.SECONDS.sleep(2);
		String path = workingDir + LGConstants.ASSETS_FILES_DIRECTORY  + fileNameToUpload;
		TimeUnit.SECONDS.sleep(2);
		
		uploadFile(path);
		TimeUnit.SECONDS.sleep(10);
		saveSection(sectionIndex);
		
		TimeUnit.SECONDS.sleep(2);
		
	}

	public String checkAttachment(String resource,int index,String nameToCompare) {
		
		WebElement file = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='main-content']/div[3]/experiment-attachments/section/div/a[" + index + "]/div/div/span[1]")));
		if (file.getText().equals(nameToCompare)){
			return viewAttachment(resource);
		}
		return "Attachment not found...";
	}
	
	public boolean checkLinkedResources(String protocol) {
		
		WebElement linkName = getWebDriver().findElement(By.cssSelector(".link_name.ng-binding"));
		String name = linkName.getText();

		return name.equals(protocol);
	}


	private String viewAttachment(String resource) {
		
		WebElement viewFile = getWebDriver().findElement
				(By.xpath(".//*[@id='main-content']/div[3]/experiment-attachments/section/div/div/a[2]"));
		viewFile.click();
		
		checkForAlerts();
		
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("page-title"))); 
		WebElement ToInput = getWebDriver().findElement(By.xpath(".//table/tbody/tr[3]/td/a"));
		String relatedTo = ToInput.getText();
		if(!relatedTo.equals(resource))
			return "Not the right attachment for the created resource...";
		return getWebDriver().getTitle();
	}
	
	
	public String addTextToSection(String sectionIndex,String descToTest) throws InterruptedException {

		return addText(sectionIndex, descToTest);
	}
	
	
	private String addText(String sectionIndex ,String descToTest) throws InterruptedException {
		
		selectSection(sectionIndex);
	
		writeInEditor(sectionIndex, descToTest);

		saveSection(sectionIndex);
		WebElement textArea = getWebDriver().findElement(By.cssSelector("#section_" +sectionIndex+ ">div>div>.text-element>.redactor-box>.redactor-editor>p"));
		String text = textArea.getText();

		return text;
	}
	
	private void writeInEditor(String sectionIndex, String descToTest) throws InterruptedException {
		
		TimeUnit.SECONDS.sleep(1);
	
    	executeJavascript("$('#section_"+ sectionIndex +"').find('.redactor-editor').redactor('code.set', '<p>"+descToTest+"</p>');");
    	TimeUnit.SECONDS.sleep(1);
		
	}
	
	public boolean editCompound(String sectionIndex,String newName)  throws InterruptedException{
		
		boolean edited = false;
		selectSection(sectionIndex);
		TimeUnit.SECONDS.sleep(1);
		
		WebElement compoundElm = getWebDriver().findElement(By.cssSelector(".skip_edit"));
		compoundElm.click();
		TimeUnit.SECONDS.sleep(2);
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btn-getmol")));
		
		WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("compound_name")));
		txtName.clear();
		txtName.sendKeys(newName);
		
		WebElement btnSaveComp = getWebDriver().findElement(By.id("btn-getmol"));
		btnSaveComp.click();

		TimeUnit.SECONDS.sleep(5);
		getWebDriver().switchTo().activeElement();
		
		saveSection(sectionIndex);
		try {
			WebElement compoundName = driverWait.until(ExpectedConditions.visibilityOfElementLocated
					(By.xpath(".//*[@id='section_" + sectionIndex + "']/div[@class='element_container compound_element']/div/element/p/b")));
			edited = (compoundName.getText().equals(newName));

		} catch (Exception e) {
			edited = false;
		}

		return edited;
	}
	
	public boolean addReactionToSection(String sectionIndex) throws InterruptedException{
		selectSection(sectionIndex);

		clickOnSectionMenuAction(sectionIndex, addReactionActionId);
		TimeUnit.SECONDS.sleep(2);
		String pathToImportFile = workingDir + LGConstants.ASSETS_FILES_DIRECTORY + LGConstants.REACTION_FILES_DIRECTORY + LGConstants.REACTION_FILE_TO_IMPORT;
		return openMarvinJSDialogAndImport(pathToImportFile,sectionIndex);
		
	}
	
	private boolean openMarvinJSDialogAndImport(String pathToFile,String sectionIndex) throws InterruptedException {


		boolean created = false;
		TimeUnit.SECONDS.sleep(1);

		// open the canvas for drawing
		WebElement sketch = driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(By.id("sketch")));
		
		TimeUnit.SECONDS.sleep(5);
		
		WebDriver iframe = getWebDriver().switchTo().frame(sketch);

		WebElement imgImport = iframe.findElement(By.xpath(".//div[starts-with(@title,'Import')]"));
		imgImport.click();
		TimeUnit.SECONDS.sleep(2);

		WebElement fileSelect = getWebDriver().findElement(By.cssSelector(".gwt-FileUpload"));
		fileSelect.sendKeys(pathToFile);

		TimeUnit.SECONDS.sleep(2);
		
		iframe.switchTo().parentFrame();
		

		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By
				.id("btn-getmol")));
		
		WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("compound_name")));
		txtName.clear();
		txtName.sendKeys(GenericHelper.buildUniqueName("reaction_"));
		
		WebElement btnSaveComp = getWebDriver().findElement(By.id("btn-getmol"));
		btnSaveComp.click();

		TimeUnit.SECONDS.sleep(5);
		getWebDriver().switchTo().activeElement();
		
		saveSection(sectionIndex);
		try {
			WebElement compoundImg = driverWait.until(ExpectedConditions.visibilityOfElementLocated
					(By.xpath(".//*[@id='section_" + sectionIndex + "']/div[@class='element_container reaction_element']")));
			created = (compoundImg != null);

		} catch (Exception e) {
			created = false;
		}

//		try {
//			WebElement reactionImg = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By
//							.xpath(".//*[@class='reaction ng-scope']/stoichiometric-table/img")));
//			created = (reactionImg != null);
//
//		} catch (NoSuchElementException e) {
//			created = false;
//		}
		return created;
	}
	
	public String addSamplesToSection(String sectionIndex) throws InterruptedException {

		String created = "";
		selectSection(sectionIndex);
		String typeInput = "#sample_item_type_input";


		clickOnSectionMenuAction(sectionIndex, addSamplesActionId);
		
		
		//open the dropdown for type list to count the number of types
		WebElement dropDown = openDropDown(typeInput);

		List <WebElement> typesList = getWebDriver().findElements(By.xpath(".//*[@id='select2-drop']/ul/li"));
		int numOftypes = typesList.size();
		dropDown = getWebDriver().findElement(By.id("select2-drop-mask"));
		dropDown.click();
		//create rows as the number of types (minus 1 because the first row is already exist)
		for (int i = 1; i <= numOftypes; i++) {
			Sample sample = addSample(i);
			TimeUnit.SECONDS.sleep(2);
			saveSection(sectionIndex);
			
			
			WebElement sampleType = getWebDriver().findElement(By.xpath(".//*[@id='sample_item_type_input']/p"));
			WebElement sampleNameElm = getWebDriver().findElement(By.xpath(".//*[@id='sample_item_id_input']/p/a"));
			WebElement stock;
			
			WebElement remark = getWebDriver().findElement(By.xpath(".//*[@id='sample_item_remark_input']/p/em"));
			//if it is rodent specimen the stock is empty
			if(sampleType.getText().equalsIgnoreCase(LGConstants.RODENTS_SPECIMEN))
				stock = getWebDriver().findElement(By.xpath(".//*[@id='sample_stock_id_input']/p/span[3]"));
			else
				stock = getWebDriver().findElement(By.xpath(".//*[@id='sample_stock_id_input']/p/a"));
			//check the correctness of data - if not correct - collect the type of the broken type
			if( !sample.name.equals(sampleNameElm.getText()) ||
					!stock.getText().startsWith(sample.stock) || !sample.remark.equals(remark.getText())){
				created = created + " , " + sample.type;
			}
			deleteSample();
			checkForAlerts();
			TimeUnit.SECONDS.sleep(2);
		}

		return created;
	}
	private WebElement openDropDown(String inputId) throws InterruptedException {

		WebElement dropDown = getWebDriver().findElement(By.cssSelector(inputId +">div>a>span.select2-arrow>b"));
		dropDown.click();
		TimeUnit.SECONDS.sleep(1);
		return dropDown;
	}
	
	public boolean editSample(String sectionIndex) throws InterruptedException {

		boolean created = false;
		

		clickOnSectionMenuAction(sectionIndex, addSamplesActionId);
	
		//select type
		openDropDown("#sample_item_type_input");	
		WebElement selectedType =  getWebDriver().findElement(By.cssSelector("li:nth-child(1)>.select2-result-label"));		
		String type = selectedType.getText();
		selectedType.click();
		TimeUnit.SECONDS.sleep(2);
		
		//add name
		String sampleName = GenericHelper.buildUniqueName(type);
	
		//workaround due to auto save that causes the dropdown to close before selection is made
		boolean addToFound = false;
		while (!addToFound) {
			openDropDown("#sample_item_id_input");
			String scriptSetSearchInput = "$('.select2-search>input').keydown().val('"+ sampleName +"').keyup();";
			executeJavascript(scriptSetSearchInput);
			TimeUnit.SECONDS.sleep(5);
			try{
				WebElement addTo = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@class='select2-no-results']/a")));
				TimeUnit.SECONDS.sleep(2);
				String addToText = addTo.getText();
				if(addToText.contains(sampleName.toLowerCase())){
					executeJavascript("arguments[0].click();", addTo);
					TimeUnit.SECONDS.sleep(2);
				}
				addToFound = true;
			}catch(Exception e){
				//continue - try again open dropdown
			}
		}

		WebElement selectedName =  driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.cssSelector("li:last-child>.select2-result-label:last-child")));
		selectedName.click();
		TimeUnit.SECONDS.sleep(2);
		saveSection(sectionIndex);
		
		selectSection(sectionIndex);

		try {
			WebElement dropTube = getWebDriver().findElement(By.xpath(".//*[@id='s2id_sample-stock-id']/a/span[2]/b"));
			if(!dropTube.isEnabled())
				return false;
			dropTube.click();
			TimeUnit.SECONDS.sleep(2);

			WebElement inputTxt = getWebDriver().findElement(By.xpath(".//*[@id='select2-drop']/ul/li/ul/li[last()]/div"));
			inputTxt.click();
			TimeUnit.SECONDS.sleep(3);
			created = true;
		} catch (Exception e) {
			created = false;
		}
		return created;
	}
	
	public boolean addSampleWithGenericCollection(String sectionIndex,String collectionName,String sampleName) throws InterruptedException {
		
		boolean created = false;
		selectSection(sectionIndex);
		String typeInput = "#sample_item_type_input";

		Sample sample = new Sample();
		clickOnSectionMenuAction(sectionIndex, addSamplesActionId);
	
		//open the dropdown for type list to count the number of types
		openDropDown(typeInput);

		List <WebElement> typesList = getWebDriver().findElements(By.xpath(".//*[@id='select2-drop']/ul/li"));
		int numOftypes = typesList.size();

		for (int j = 1; j <= numOftypes; j++) {
				
			WebElement selectedType =  getWebDriver().findElement(By.cssSelector("li:nth-child(" + j + ")>.select2-result-label"));		
			if(selectedType.getText().equals(collectionName)){
				selectedType.click();
				TimeUnit.SECONDS.sleep(1);		
				addSampleName(sampleName);
				sample.setName(sampleName);
				//add stock
				addStockName(sample);
				break;
			}
		}
		

		saveSection(sectionIndex);
		TimeUnit.SECONDS.sleep(2);
		
		refreshPage();
		selectSection(sectionIndex);
		TimeUnit.SECONDS.sleep(2);
		
		//check that after clicking on edit - the name of the sample from generic collection type is still shown
		WebElement sampleNameElm = getWebDriver().findElement(By.xpath(".//*[@id='s2id_sample-item-id']/a/span[1]"));
		WebElement stockName = getWebDriver().findElement(By.xpath(".//*[@id='s2id_sample-stock-id']/a/span[1]/span/span[2]"));
		if((sampleNameElm.getText().equals(sampleName)) && (stockName.getText().equals(sample.stock)) ){
			created = true;
		}
		
		return created;
	}
	
	private void addSampleName(String sampleName) throws InterruptedException {


		openDropDown("#sample_item_id_input");
		String scriptSetSearchInput = "$('.select2-search>input').keydown().val('"+ sampleName +"').keyup();";
		executeJavascript(scriptSetSearchInput);
		TimeUnit.SECONDS.sleep(5);
		
		WebElement addTo = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@class='select2-no-results']/a")));
		TimeUnit.SECONDS.sleep(2);
		String addToText = addTo.getText();
		if(addToText.contains(sampleName.toLowerCase())){
			executeJavascript("arguments[0].click();", addTo);
			TimeUnit.SECONDS.sleep(2);
		}

		WebElement selectedName =  driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.cssSelector("li:last-child>.select2-result-label:last-child")));
		selectedName.click();
		TimeUnit.SECONDS.sleep(3);
	}

	
	/**
	 * addSample by type
	 * @param name - name of the experiment for prefix to the new sample
	 * @param typeSelectionIndex - starts from 1 to select the type of sample to create
	 */
	private Sample addSample(int typeSelectionIndex) throws InterruptedException {

		
		if(!hasSamples()){
			WebElement addSampleBtn =  getWebDriver().findElement(By.cssSelector(".addto"));
			addSampleBtn.click();
			TimeUnit.SECONDS.sleep(1);
		}
		Sample sample = new Sample();
		
		String typeInput = "#sample_item_type_input";
		String idInput = "#sample_item_id_input";
	
		//select type
		openDropDown(typeInput);	
		WebElement selectedType =  getWebDriver().findElement(By.cssSelector("li:nth-child(" + typeSelectionIndex + ")>.select2-result-label"));		
		sample.setType(selectedType.getText());
		selectedType.click();
		TimeUnit.SECONDS.sleep(1);
		
		//add name
		String sampleName = GenericHelper.buildUniqueName(sample.type);

		openDropDown(idInput);
		String scriptSetSearchInput = "$('.select2-search>input').keydown().val('"+ sampleName +"').keyup();";
		executeJavascript(scriptSetSearchInput);
		TimeUnit.SECONDS.sleep(2);
		WebElement addTo = getWebDriver().findElement(By.xpath(".//*[@class='select2-no-results']/a"));
		String addToText = addTo.getText();
		if(addToText.contains(sampleName.toLowerCase())){
			executeJavascript("arguments[0].click();", addTo);
			TimeUnit.SECONDS.sleep(2);
		}
			

		WebElement selectedName =  driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.cssSelector("li:last-child>.select2-result-label:last-child")));
		selectedName.click();
		TimeUnit.SECONDS.sleep(3);
		sample.setName(sampleName);
		//add stock
		addStockName(sample);
		TimeUnit.SECONDS.sleep(3);
		//add remark
		WebElement remark =  getWebDriver().findElement(By.xpath(".//*[@id='sample_item_remark_input']/p/input"));
		remark.clear();
		remark.sendKeys(sampleName);
		sample.setRemark(sampleName);
		return sample;
	}
	
	private boolean hasSamples() {
		try{
			//has table with rows
			List <WebElement> rows = getWebDriver().findElements(By.xpath(".//*[@class='samples styled_table']/tbody/tr"));
			return rows.size() > 0;
		}catch(NoSuchElementException e){
			return false;
		}
		
	}
	private void addStockName(Sample sample) throws InterruptedException {
		
		String stockIdInput = "#sample_stock_id_input";
		
		if(getWebDriver().findElement(By.xpath(".//*[@id='s2id_sample-stock-id']/a/span[1]")).getText().contains("N/A")){
			sample.setStock("");
			return;
		}

		openDropDown(stockIdInput);
		TimeUnit.SECONDS.sleep(1);

		WebElement stockName =  getWebDriver().findElement(By.cssSelector("li:last-child>.select2-result-label:last-child"));			
		stockName.click();
		TimeUnit.SECONDS.sleep(4);
		
		WebElement inputTxt = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".lg-stock-name")));
		String stock = inputTxt.getText();
		sample.setStock(stock);

	}


	public boolean addPlate2X3ToSection(String sectionIndex) throws InterruptedException {

		boolean created = false;
		waitForPageCompleteLoading();
		selectSection(sectionIndex);
		//add plate size 6(3*2)
		WebElement action = getWebDriver().findElement(By.xpath
				(".//*[@id='section_toolbar_" + sectionIndex + "']/ul/li[@id='more_section_actions']/ul/li[@id='add_section_plate_element']/div/ul/li[1]"));
		String sizeOfPlate = "6";
		executeJavascript("arguments[0].click();",action);
		TimeUnit.SECONDS.sleep(2);

        WebElement btnTogglePlate = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ng-isolate-scope>center>button")));
        btnTogglePlate.click();
        TimeUnit.SECONDS.sleep(1);
        
        //check that the number of wells is correct according to rows & cols
        List<WebElement> rows = getWebDriver().findElements(By.xpath(".//table[@class='plate ui-selectable']/tbody/tr"));
        int rowSize = (rows.size()-1);
        List<WebElement> cols = getWebDriver().findElements(By.xpath(".//table[@class='plate ui-selectable']/tbody/tr[2]/td"));
        int colSize = cols.size();
        created = sizeOfPlate.equals(String.valueOf(rowSize*colSize));
       
        
		try {
			WebElement plateContent = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ng-isolate-scope>center")));
			created = created && (plateContent != null);

		} catch (NoSuchElementException e) {
			created = false;
		}
	
		return created;
	}

	
	protected void deleteSample() throws InterruptedException {
		  //delete all created samples
		
		WebElement btnDelete  = getWebDriver().findElement(By.cssSelector(".samples.styled_table>tbody>tr:nth-of-type(1)>td>i.fa.fa-close"));
		executeJavascript("arguments[0].click();", btnDelete);
		TimeUnit.SECONDS.sleep(1);

	}

	public boolean validateReactionEditingAndCalculating(String[] values) throws Exception{
		
		boolean valid = true;
		
		WebElement table = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("stoichiometric-table")));
		String id = table.getAttribute("element-id");
		String pathToCells = ".//*[@id='reaction_element_"+ id +"']/stoichiometric-table/table/tbody/";
		
		//edit name
		editCell(pathToCells,"tr[1]/td[3]/div/i","tr[1]/td[3]/div/div/input","Reactant1");
		editCell(pathToCells,"tr[2]/td[3]/div/i","tr[2]/td[3]/div/div/input","Reactant2");
		editCell(pathToCells,"tr[3]/td[3]/div/i","tr[3]/td[3]/div/div/input","Product");
		
		//edit density
		editCell(pathToCells,"tr[1]/td[6]/div/i","tr[1]/td[6]/div/div/input",values[0]);
		editCell(pathToCells,"tr[2]/td[6]/div/i","tr[2]/td[6]/div/div/input",values[1]);
		editCell(pathToCells,"tr[3]/td[6]/div/i","tr[3]/td[6]/div/div/input",values[2]);
		
		TimeUnit.SECONDS.sleep(2);
		
		//edit volume
		editCell(pathToCells,"tr[1]/td[8]/div/i","tr[1]/td[8]/div/div/input","500");
		editCell(pathToCells,"tr[2]/td[8]/div/i","tr[2]/td[8]/div/div/input","400");
		editCell(pathToCells,"tr[3]/td[8]/div/i","tr[3]/td[8]/div/div/input","300");	
	
		TimeUnit.SECONDS.sleep(2);
		
		//calculate according to formula : mol=W/MW
		for (int i = 1; i <= 3; i++) {
			
			WebElement txtMol = getWebDriver().findElement(By.xpath( pathToCells + "tr[" + i + "]/td[7]/div/ng-transclude/span"));
			String mol = txtMol.getText();
			
			WebElement txtMW = getWebDriver().findElement(By.xpath( pathToCells + "tr[" + i + "]/td[5]"));
			String MW = txtMW.getText();
			
			WebElement txtW = getWebDriver().findElement(By.xpath( pathToCells + "tr[" + i + "]/td[10]/div/ng-transclude/span"));
			String W = txtW.getText();
			
			WebElement txtVolume = getWebDriver().findElement(By.xpath( pathToCells + "tr[" + i + "]/td[8]/div/ng-transclude/span"));
			String volume = txtVolume.getText();
	
			WebElement txtDensity = getWebDriver().findElement(By.xpath( pathToCells + "tr[" + i + "]/td[6]/div/ng-transclude/span"));
			String density = txtDensity.getText();
			
			WebElement txtMolRatio = getWebDriver().findElement(By.xpath( pathToCells + "tr[" + i + "]/td[9]/div"));
			String molRatio = txtMolRatio.getText();
			if(i==1){
				valid = valid && molRatio.equals("1.00");
			}
			
			valid =  valid && calculateW(W,density,volume);
			
			//valid =  valid && calculateMol(mol,MW,W);
			//if one of the calculation is not accurate - return false and stop validate
			if(!valid)
				return false;
			TimeUnit.SECONDS.sleep(1);
		}
	
		return valid;
	}
	
	//calculate the Density = W/V formula 
	private boolean calculateW(String W,String density, String volume) throws ScriptException {
		
		Double WAsDouble = Double.valueOf(W);
	    String calculation = density +"*" + volume;
	
	    ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
	    Double result = (Double) engine.eval(calculation);
	    
		return (result.doubleValue() == WAsDouble.doubleValue());
	}
	
	private boolean calculateMol(String mol, String MW, String W) throws ScriptException {
	
	    Double molAsDouble = Double.valueOf(mol);
	    String calculation = W +"/" + MW;
	
	    ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
	    Double result = Double.valueOf(engine.eval(calculation).toString());
	    
		return result == molAsDouble;
	}
	
	private void editCell(String pathToCells,String pathToEditBtn,String pathToCellInput,String value) throws InterruptedException {
		 
		TimeUnit.SECONDS.sleep(1);
		WebElement btnEdit = getWebDriver().findElement(By.xpath(pathToCells + pathToEditBtn));
		
		executeJavascript("arguments[0].click();",btnEdit);
	 
		TimeUnit.SECONDS.sleep(2);
		
		WebElement txtInput =  driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(pathToCells + pathToCellInput)));
		sendKeys(txtInput, value);
		txtInput.sendKeys(Keys.ENTER);
		TimeUnit.SECONDS.sleep(1);
	}
}
