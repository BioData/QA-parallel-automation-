package com.biodata.labguru.pages.enotebook;

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

import com.biodata.labguru.GenericHelper;
import com.biodata.labguru.LGConstants;
import com.biodata.labguru.model.Sample;

public class ExperimentPageV2 extends AbstractNotebookPage {
	
	private static final String SUB_HTML_TAG = "sub";
	private static final String SUPER_HTML_TAG = "sup";
	private static final String DELETED_HTML_TAG = "del";
	private static final String UNDERLINE_HTML_TAG = "u";
	private static final String ITALIC_HTML_TAG = "em";
	private static final String BOLD_HTML_TAG = "strong";
	
	private By btnSignLocator = By.id("sign_toggle");
	private By btnPrintLocator = By.id("print");
	private By btnAssignLocator = By.id("assign");
	
	private final String duplicateActionId = "duplicate_item";
	private final String saveAsProtocolActionId = "save_item_as_protocol";
	private final String viewVersionsActionId ="view_version_history";
	private final String moveItemActionId = "move_item";
	private final String deleteExperimentActionId = "link_to_confirm_delete_experiment";
	
	private final String sectionFontActionBarId = "toggle_redactor_toolbar";
	private final String sectionCommentActionBarId = "section_comments";
	private final String sectionAttachmentsActionBarId = "section_attachments";
	private final String sectionLinksActionBarId = "section_links";

	private final String addTableActionId = "add_section_table_element";
	private final String addStepsActionId = "add_section_steps_element";
	private final String addSamplesActionId = "add_section_samples_element";
	private final String addReactionActionId = "add_section_reaction_element";
	private final String addCompoundActionId = "add_section_compound_element";
	private final String moveUpSectionActionId = "move_section_up";
	private final String moveDownSectionActionId = "move_section_down";
	private final String deleteSectionActionId = "delete_section";
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

	public boolean sign() throws InterruptedException{
		
		WebElement linkSign = driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(btnSignLocator));

		linkSign.click();
		TimeUnit.SECONDS.sleep(2);
		WebElement proceedBtn = getWebDriver().findElement(By.xpath(".//*[@id='approve_sign']/div"));
		proceedBtn.click();
		TimeUnit.SECONDS.sleep(5);
		driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(By.cssSelector(".signed_note.ng-binding")));
		
		WebElement signedNote = driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(By.cssSelector(".signed_note.ng-binding")));
		String msg = signedNote.getText();
		linkSign = driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(btnSignLocator));
		
		if(msg.startsWith("Signed by") && linkSign.getText().equals("Revert Signature"))
			return true;
		return false;
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
		return !searchExperiment(expName,false/*no loading*/);
		
	}
	public boolean searchExperiment(String expName,boolean loadExperiment) {
		
		if(hasList()){
			
			List<WebElement> rows = getWebDriver().findElements(By.xpath(".//*[@id='data']/table/tbody/tr"));
			for (int i = 1; i <= rows.size(); i++) {
				WebElement tableRow = getWebDriver().findElement(By.xpath(".//*[@id='data']/table/tbody/tr[" + i + "]/td[2]/b/a"));
				if(tableRow.getText().equals(expName)){
					if(loadExperiment){
						tableRow.click();
					}
					return true;
				}
					
			}
		}
		return false;
	}

	
	public String moveExperimentToProject(String newProject) throws InterruptedException{
		
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

	public String duplicate() throws InterruptedException{
		
		clickOnUpperMenuAction(duplicateActionId);	
		TimeUnit.SECONDS.sleep(2);
		WebElement duplicateBtn = getWebDriver().findElement(By.xpath(".//*[@id='do_print']"));
		duplicateBtn.click();
		TimeUnit.SECONDS.sleep(2);
		switchToNewTab();
		checkForNotyMessage();
		
		 WebElement pageTitle = getWebDriver().findElement(By.xpath(".//*[@id='projects_experiment_title_input']/span"));
	     String newName = pageTitle.getText();
		 return newName;
		
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

	public String addSamplesToSection(String sectionIndex,String name) throws InterruptedException {

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
		//String dropdownPrefix = ".samples.styled_table>tbody>tr:nth-child(1)>";
		WebElement dropDown = getWebDriver().findElement(By.cssSelector(/*dropdownPrefix + */inputId +">div>a>span.select2-arrow>b"));
		dropDown.click();
		TimeUnit.SECONDS.sleep(1);
		return dropDown;
	}
	
	protected void deleteSample() throws InterruptedException {
		  //delete all created samples
		
		WebElement btnDelete  = getWebDriver().findElement(By.cssSelector(".samples.styled_table>tbody>tr:nth-of-type(1)>td>i.fa.fa-close"));
		executeJavascript("arguments[0].click();", btnDelete);
		TimeUnit.SECONDS.sleep(1);
    	
		
	}

	/**
	 * 
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
		TimeUnit.SECONDS.sleep(2);
		
		//add name
		String sampleName = GenericHelper.buildUniqueName(sample.type);
	
		//workaround due to auto save that causes the dropdown to close before selection is made
		boolean addToFound = false;
		while (!addToFound) {
			openDropDown(idInput);
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
		//workaround due to auto save that causes the dropdown to close before selection is made
		boolean stockAdded = false;
		while (!stockAdded) {
			openDropDown(stockIdInput);
			TimeUnit.SECONDS.sleep(1);
			try {
				WebElement stockName =  getWebDriver().findElement(By.cssSelector("li:last-child>.select2-result-label:last-child"));			
				stockName.click();
				TimeUnit.SECONDS.sleep(8);
				
				WebElement inputTxt = getWebDriver().findElement(By.cssSelector(".lg-stock-name"));
				String stock = inputTxt.getText();
				sample.setStock(stock);
				stockAdded = true;
			} catch (Exception e) {
				//continue
			}
			
		}
		
		
		
	}


	public boolean addPlate2X3ToSection(String sectionIndex) throws InterruptedException {

		boolean created = false;
		
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

	public String addTextToSection(String sectionIndex,String descToTest) throws InterruptedException {

		return addText(sectionIndex, descToTest);
	}
	


	private String getSavedSectionTitle(String sectionIndex) throws InterruptedException {
		
		String script = "var text = $($('.section_title')["+ sectionIndex + "]).find('input').val(); return text;";
		String  text = (String) executeJavascript(script);
		return text;
	}
	// only when compound option is enabled
	public boolean addCompoundToProcedure() throws InterruptedException {
		
		String sectionIndex = "1";
		selectSection(sectionIndex);

		//TODO - add compound
		clickOnSectionMenuAction(sectionIndex, addCompoundActionId);
		TimeUnit.SECONDS.sleep(2);
		return drawCompound();
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

	

	public boolean addReactionToProcedure() throws InterruptedException {

		String sectionIndex = "1";
		selectSection(sectionIndex);
		//TODO - add reaction
		clickOnSectionMenuAction(sectionIndex, addReactionActionId);
		return openMarvinJSDialogAndImport(workingDir + LGConstants.ASSETS_FILES_DIRECTORY + "chemaxon_reaction_library/Benzoxazole formation from 2-aminophenol and carbonyls.mrv");
	
	}
	
	public boolean addReactionToResults() throws InterruptedException {

		String sectionIndex = "2";
		selectSection(sectionIndex); //results
		//TODO - add reaction
		clickOnSectionMenuAction(sectionIndex, addReactionActionId);
		
		return openMarvinJSDialogAndImport(workingDir + LGConstants.ASSETS_FILES_DIRECTORY + "chemaxon_reaction_library/Benzoxazole formation from 2-aminophenol and carbonyls.mrv");
		
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
	
	
	private void saveSection(String sectionIndex) throws InterruptedException {
		
		WebElement btnSave = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='section_toolbar_" + sectionIndex + "']/input[@class='inline_submit']")));
		btnSave.click();
		TimeUnit.SECONDS.sleep(1);
	}
	
	private void selectSection(String sectionIndex) throws InterruptedException {

		WebElement sectionArea = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("section_" + sectionIndex )));
		sectionArea.click();
		TimeUnit.SECONDS.sleep(2);
	}
	
	private void addSection(String sectionIndex) throws InterruptedException {
		
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
	
	private String addText(String sectionIndex ,String descToTest) throws InterruptedException {
		
		selectSection(sectionIndex);
	
		writeInEditor(sectionIndex, descToTest);

		saveSection(sectionIndex);
		WebElement textArea = getWebDriver().findElement(By.cssSelector("#section_" +sectionIndex+ ">div>div>.text-element>.redactor-box>.redactor-editor>p"));
		String text = textArea.getText();

		return text;
	}
	
	private boolean clickOnUpperMenuAction(String actionId) throws InterruptedException {
		
		WebElement action = getWebDriver().findElement(By.xpath(".//*[@id='more_actions']/ul/li[@id='" + actionId + "']/a"));	
		executeJavascript("arguments[0].click();",action);
		TimeUnit.SECONDS.sleep(1);
		return true;
	}

	protected boolean getMoreMenuAction(String actionId) {
		try {
			getWebDriver().findElement(By.xpath(".//*[@id='more_actions']/ul/li[@id='" + actionId + "']/a"));
		} catch (Exception e) {
			//menu action not found
			return false;
		}
		return true;
	}
	
	private void clickOnSectionMenuAction(String sectionIndex,String actionId) throws InterruptedException {
		
		WebElement action = getWebDriver().findElement(By.xpath(".//*[@id='section_toolbar_" + sectionIndex + "']/ul/li[@id='more_section_actions']/ul/li[@id='" + actionId + "']/a"));
		executeJavascript("arguments[0].click();",action);
		TimeUnit.SECONDS.sleep(2);
	}
	
	private void clickOnSectionActionBar(String sectionIndex,String actionId) throws InterruptedException {
		String script = "$('#section_toolbar_" + sectionIndex + ">ul>li#" + actionId + ">i').click();";
		executeJavascript(script);
		TimeUnit.SECONDS.sleep(2);
	}

	private void writeInEditor(String sectionIndex, String descToTest) throws InterruptedException {
		
		TimeUnit.SECONDS.sleep(1);
	
    	executeJavascript("$('#section_"+ sectionIndex +"').find('.redactor-editor').redactor('code.set', '<p>"+descToTest+"</p>');");
    	TimeUnit.SECONDS.sleep(1);
		
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
	
	@Override
	protected void clickOnResourceLink() throws InterruptedException {
		//switch to beta experiment and click on the link label there
		changeVersion(LGConstants.EXPERIMENT_BETA);
		super.clickOnResourceLink();
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

	/**
	 * This only applies for admin users or for the user that signed the experiment
	 */
	public boolean revertSignature() throws InterruptedException {
		//TODO:check if this is the user that signed the experiment or an Admin user
		WebElement linkSign = driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(btnSignLocator));

		linkSign.click();
		TimeUnit.SECONDS.sleep(2);
		WebElement proceedBtn = getWebDriver().findElement(By.xpath(".//*[@id='approve_sign']/div"));
		proceedBtn.click();
		TimeUnit.SECONDS.sleep(3);
		
		linkSign = driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(btnSignLocator));
		
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
		
		boolean buttonOk = true;	
		buttonOk = buttonOk && getMoreMenuAction(viewVersionsActionId);	
		buttonOk = buttonOk && getMoreMenuAction(saveAsProtocolActionId);	
		buttonOk = buttonOk && getMoreMenuAction(duplicateActionId);	
		buttonOk = buttonOk && !getMoreMenuAction(deleteExperimentActionId);	
		buttonOk = buttonOk && !getMoreMenuAction(moveItemActionId);	

		return buttonOk;
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
				WebElement tagTitle = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='page-title']/span")));
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
	
	public boolean addToPage(String sectionIndex) throws InterruptedException {
		
		selectSection(sectionIndex);
		WebElement action = getWebDriver().findElement(By.id("add_to_page"));
		executeJavascript("arguments[0].click();",action);
		TimeUnit.SECONDS.sleep(3);
		
		boolean finishLoadingElement = false;
		
		//wait until the excel page is loaded
		while(!finishLoadingElement){
			try{
				//if the pending message appears - keep waiting
				WebElement pendingMsg = getWebDriver().findElement(By.cssSelector(".pending_element"));
				finishLoadingElement= false;
			} catch (NoSuchElementException e) {
				//no pending message - stop waiting and check the loaded element
				finishLoadingElement= true;
			}	
		}
		List <WebElement> elements = getWebDriver().findElements(By.cssSelector(".element_container.excel_element"));
		return elements.size() > 0;
		
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
		TimeUnit.SECONDS.sleep(1);

		//check that the text is as ment to be
		try {
			driverWait.until(ExpectedConditions.visibilityOfElementLocated
					(By.xpath(".//*[@id='section_" +sectionIndex+ "']/div/div/element/div/div/p/" + tagToCheck+ "")));	
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

}
