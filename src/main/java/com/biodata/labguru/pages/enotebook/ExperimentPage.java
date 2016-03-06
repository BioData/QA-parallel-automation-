package com.biodata.labguru.pages.enotebook;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.model.Sample;


public class ExperimentPage extends AbstractNotebookPage {

	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	@Override
	public By getDuplicateLocator() {
		
		return By.id("duplicate");
	}
	
	public String getPageTitleXPath() {
		
		return ".//*[@id='projects_experiment_title_input']/span";
	}
	

	public String addSamplesToProcedure(String name) throws InterruptedException {

		String created = "";

		WebElement procedureDescToolBar = getDescToolBarElement();
		TimeUnit.SECONDS.sleep(1);

		WebElement samplesLoader = procedureDescToolBar.findElement(By.cssSelector(".samples.load"));
		samplesLoader.click();
		TimeUnit.SECONDS.sleep(2);
		
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".inline_form_container.type_samples")));

		TimeUnit.SECONDS.sleep(3);
		//open the dropdown for type list to count the number of types
		WebElement dropType = getWebDriver().findElement(By.xpath(".//*[@id='s2id_sample_item_type']/a"));
		dropType.click();
		TimeUnit.SECONDS.sleep(2);

		List <WebElement> typesList = getWebDriver().findElements(By.xpath(".//*[@id='select2-drop']/ul/li"));
		int numOftypes = typesList.size();
		dropType.click();
		TimeUnit.SECONDS.sleep(2);
		//create rows as the number of types (minus 1 because the first row is already exist)
		for (int i = 1; i <= numOftypes; i++) {
			
			WebElement idElm = getWebDriver().findElement(By.id("sample_name"));
			String id = idElm.getAttribute("value");
			String sampleId = "edit_sample_" + id;
			Sample sample = addSamples(LGConstants.SAMPLE_PREFIX + id,i,sampleId);
			
			TimeUnit.SECONDS.sleep(2);
			saveSample();
			
			TimeUnit.SECONDS.sleep(3);
			sampleId = "sample_tr_" + id;
			
			WebElement sampleType = getWebDriver().findElement(By.xpath(".//*[@id='"+ sampleId + "']/td[2]"));
			WebElement sampleName = getWebDriver().findElement(By.xpath(".//*[@id='"+ sampleId + "']/td[3]/a"));
			WebElement remark = getWebDriver().findElement(By.xpath(".//*[@id='"+ sampleId + "']/td[5]"));

			WebElement stock;
			//if it is rodent specimen the stock is empty
			if(sampleType.getText().equalsIgnoreCase(LGConstants.RODENTS_SPECIMEN))
				stock = getWebDriver().findElement(By.xpath(".//*[@id='"+ sampleId + "']/td[4]"));
			else
				stock = getWebDriver().findElement(By.xpath(".//*[@id='"+ sampleId + "']/td[4]/a/span[2]"));
			
			
			//check the correctness of data - if not correct - collect the type of the broken type
			if (!sample.name.equals(sampleName.getText()) || !stock.getText().startsWith(sample.stock) ){
				created = created + " , " + sample.type ;
				System.out.println("sample.type :" + sample.type + "***** " +sampleType.getText());
				System.out.println("sample.name :" + sample.name + "***** " +sampleName.getText());
				System.out.println("sample.stock :" + sample.stock + "***** " +stock.getText());
			}
			
			//click on edit if there are still more types to check
			if(i<numOftypes){
				executeJavascript("document.getElementsByClassName('edit_me')[4].click();");
				TimeUnit.SECONDS.sleep(2);
			}else{
				deleteSample();
				checkForAlerts();
				TimeUnit.SECONDS.sleep(2);
			}

		}

		return created;
	}
	
	


	/**
	 * 
	 * @param name - name of the experiment for prefix to the new sample
	 * @param samplesContainer
	 * @param typeSelectionIndex - starts from 1 to select the type of sample to create
	 * @param sampleRowId - the element id row name
	 */
	protected Sample addSamples(String name,int typeSelectionIndex,String sampleRowId)
			throws InterruptedException {

		Sample sample = new Sample();
		
		WebElement dropType =  getWebDriver().findElement(By.xpath(".//*[@id='"+ sampleRowId +"']/fieldset/ol/li[2]/div/a"));
		dropType.click();
		TimeUnit.SECONDS.sleep(2);
		
		
		WebElement selectedType =  getWebDriver().findElement(By.xpath(".//*[@id='select2-drop']/ul/li["+ typeSelectionIndex + "]/div"));
		String type = selectedType.getText();
		sample.setType(type);
		selectedType.click();
		TimeUnit.SECONDS.sleep(4);

		WebElement dropName =  getWebDriver().findElement(By.xpath(".//*[@id='"+ sampleRowId +"']/fieldset/ol/li[3]/div/a/span[1]"));
		dropName.click();
		TimeUnit.SECONDS.sleep(4);

	
		WebElement inputTxt =  getWebDriver().findElement(By.xpath(".//*[@id='select2-drop']/div/input"));
		inputTxt.clear();
		inputTxt.sendKeys(name);
		sample.setName(name);

		TimeUnit.SECONDS.sleep(4);

		WebElement addTo = getWebDriver().findElement(By.xpath(".//*[@class='select2-no-results']/a"));
		if(addTo.getText().contains(name))
			addTo.click();

		TimeUnit.SECONDS.sleep(5);
		
		List<WebElement> labels = getWebDriver().findElements(By.xpath(".//*[@class='select2-result-label']"));
		for (WebElement selectedName : labels) {
			if (selectedName.getText().equals(name)){			
				selectedName.click();
				break;
			}
		}
		
		TimeUnit.SECONDS.sleep(4);
		addStockName(sample);
		TimeUnit.SECONDS.sleep(3);
		WebElement remark =  getWebDriver().findElement(By.xpath(".//*[@id='"+ sampleRowId +"']/fieldset/ol/li[@id='sample_remarks_input']/input"));
		remark.clear();
		remark.sendKeys(name);	
		TimeUnit.SECONDS.sleep(1);
		sample.setRemark(name);
		return sample;
	}

	public String addTextDescriptionToExperiment(String descToTest) throws Exception {

		return addTextToDesc(".//*[@id='main-content']/div[1]/div[4]/div[5]/div/ul",descToTest);
	}

	public boolean addPlateToProcedure() throws InterruptedException {

		boolean created = false;

		WebElement plateLoader = getWebDriver().findElement(By.cssSelector("a[class='plate open_fancy fancybox.ajax']"));
		plateLoader.click();
		TimeUnit.SECONDS.sleep(2);
		
        WebElement plateDialog = getWebDriver().switchTo().activeElement();
        driverWait.until(ExpectedConditions.visibilityOf(plateDialog));

        WebElement btnOk = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("plate_size_submit")));
        btnOk.click();
        TimeUnit.SECONDS.sleep(1);
        getWebDriver().switchTo().activeElement();
        
        WebElement btnTogglePlate = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ng-isolate-scope>center>button")));
        btnTogglePlate.click();
        TimeUnit.SECONDS.sleep(1);
		try {
			WebElement plateContent = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ng-isolate-scope>center")));
			created = (plateContent != null);

		} catch (NoSuchElementException e) {
			created = false;
		}
	
		return created;
	}

	public String addTextDescriptionToProcedure(String descToTest) {

		String description = "Opps...not the right text..";
		try {

			WebElement procedureDescToolBar = getDescToolBarElement();
			TimeUnit.SECONDS.sleep(2);
			WebElement textLoader = procedureDescToolBar.findElement(By.cssSelector(".text.load"));
			textLoader.click();

			TimeUnit.SECONDS.sleep(2);

			WebElement container = getWebDriver().findElement(By.xpath(".//*[@class='protocols_container']"));
			TimeUnit.SECONDS.sleep(2);

			writeInRedactor("element_data", descToTest);   

			WebElement imgSaveDesc = container.findElement(By.xpath(".//*[@class='redactor-toolbar']/li[last()]/a/i"));
			TimeUnit.SECONDS.sleep(1);
			imgSaveDesc.click();
			TimeUnit.SECONDS.sleep(2);

			WebElement text = container.findElement(By.xpath(".//*[@id='element_data_input']/span/p"));
			description = text.getText();

		} catch (InterruptedException e) {
			
		}
		return description;
	}
	


	// only when compound option is enabled
	public boolean addCompoundDescToExpProcedure() throws InterruptedException {
		
		WebElement procedureDescToolBar = getDescToolBarElement();
		TimeUnit.SECONDS.sleep(2);

		WebElement compoundLoader = procedureDescToolBar.findElement(By.xpath("./li[6]/a"));
		compoundLoader.click();
		TimeUnit.SECONDS.sleep(2);
		return drawCompound();
	}
	

	public boolean addTableToProcedure(String data) throws InterruptedException {

		boolean created = false;	
		created = addTable(getDescToolBarElement(),data);
		return created;
	}

	
	public boolean addReactionToExpProcedure() throws InterruptedException {

		WebElement procedureDescToolBar = getDescToolBarElement();
		TimeUnit.SECONDS.sleep(2);
		
		WebElement reactionLoader = procedureDescToolBar.findElement(By.xpath("./li[7]/a"));
		reactionLoader.click();
		
		return openMarvinJSDialogAndImport(workingDir + LGConstants.ASSETS_FILES_DIRECTORY + LGConstants.REACTION_FILES_DIRECTORY + LGConstants.REACTION_FILE_TO_IMPORT);
	
	}
	
	public boolean addReactionToExpProcedureResults() throws InterruptedException {
		
		WebElement resultsDescToolBar = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='main-content']/div[1]/div[4]/div[7]/div/ul")));
		TimeUnit.SECONDS.sleep(2);
		
		WebElement reactionLoader = resultsDescToolBar.findElement(By.xpath("./li[4]/a"));
		reactionLoader.click();
		
		return openMarvinJSDialogAndImport(workingDir + LGConstants.ASSETS_FILES_DIRECTORY + LGConstants.REACTION_FILES_DIRECTORY + LGConstants.REACTION_FILE_TO_IMPORT);
		
	}


	public String addNewProcedure(String procedureName) throws InterruptedException {

		WebElement addTab = getWebDriver().findElement(By.xpath(".//*[@id='addtab']"));
		addTab.click();

		WebElement btnAddProcedure = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add_new_procedure")));
		btnAddProcedure.click();

		TimeUnit.SECONDS.sleep(2);

		executeJavascript("document.getElementsByClassName('edit_me')[4].click();");

		TimeUnit.SECONDS.sleep(2);
		
		List<WebElement> titlesList = getWebDriver().findElements(By.xpath(".//*[@id='experiment_procedure_name']"));
		
		for (WebElement txtProcedureName : titlesList) {
			if(txtProcedureName.getAttribute("value").equals("Untitled"))
				txtProcedureName.sendKeys(procedureName);
		}

		TimeUnit.SECONDS.sleep(1);

		List<WebElement> saveBtnList = getWebDriver().findElements(By.xpath(".//*[@id='experiment_procedure_submit_action']/input"));
		for (WebElement btnSave : saveBtnList) {
			if(btnSave.isDisplayed())
				btnSave.click();
		}
		TimeUnit.SECONDS.sleep(1);
		String name = "";
		List<WebElement> txtist = getWebDriver().findElements(By.cssSelector(".element-plain-text"));
		for (WebElement title : txtist) {
			if(title.getText().equals(procedureName)){
				name = title.getText();
				break;
			}
		}
		return name;

	}



	public String addNewExperimentWithFDefaultName() {

		try {

			clickOnButton(newExperimentButtonId);

			TimeUnit.SECONDS.sleep(2);

			getWebDriver().switchTo().activeElement();
			driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("create_new_experiment_button")));

			WebElement btnAdd = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("create_new_experiment_button")));
			btnAdd.click();
			TimeUnit.SECONDS.sleep(2);
			getWebDriver().switchTo().activeElement();

			TimeUnit.SECONDS.sleep(2);

		} catch (InterruptedException e) {

			
		}

		return getWebDriver().getTitle();

	}

		public boolean addStepToProcedure(int numOfSteps) throws InterruptedException {
			
			int created = 0;		
			int steps = addSteps();;
			try{
				List <WebElement> stepsList = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@class='protocol-steps']/li")));
				for (WebElement txtArea : stepsList) {
					WebElement input = txtArea.findElement(By.xpath("div[1]"));
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

	public boolean deleteStepsOfProcedure() throws InterruptedException {

		WebElement steps = getWebDriver().findElement(By.cssSelector(".inline_form_container.type_steps"));
		steps.click();
		TimeUnit.SECONDS.sleep(1);
	
		WebElement btnDelete = getWebDriver().findElement(By.xpath(".//*[@class='inline_form_container type_steps']/div[4]/span[3]/a"));
		btnDelete.click();
		checkForAlerts();
		TimeUnit.SECONDS.sleep(2);
		try{
			getWebDriver().findElement(By.cssSelector(".steps-inputs.hidden>label"));
			return false;
		}catch(Exception e){
			//should get here after delete steps
			return true;
		}
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
		 
		WebElement btnEdit = getWebDriver().findElement(By.xpath(pathToCells + pathToEditBtn));
		
		executeJavascript("arguments[0].click();",btnEdit);
	 
		TimeUnit.SECONDS.sleep(2);
		
		WebElement txtInput =  driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(pathToCells + pathToCellInput)));
		sendKeys(txtInput, value);
		txtInput.sendKeys(Keys.ENTER);
	}
	
	public String openExperiment(String expName) throws InterruptedException {
		
		selectExperiments();
		
		List<WebElement> expList = driverWait.until(ExpectedConditions
				.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@id='data']/table/tbody/tr")));
		for (int i = 1; i <= expList.size(); i++) {
			WebElement expToOpen = getWebDriver().findElement(By.xpath(".//*[@id='data']/table/tbody/tr[" + i + "]/td[2]/b/a"));
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
	
	
	public boolean hasList() {
		selectExperiments();
		try{
			List<WebElement> list = driverWait.until(ExpectedConditions
					.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@id='data']/table/tbody/tr[@class='ng-scope']")));
			return list.size() > 0;
		}catch(TimeoutException e){
			return false;
		}
	}
	
	/**
	 * Only open the marvinJSforEditing
	 * @throws InterruptedException
	 */
	public void editMarvinJSComponent() throws InterruptedException {
		
		executeJavascript("document.getElementsByClassName('edit_me open_fancy fancybox.ajax')[0].click();");
	   	TimeUnit.SECONDS.sleep(2);
	   	
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By
				.id("btn-getmol")));
		WebElement btnSaveComp = driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(By.id("btn-getmol")));
		btnSaveComp.click();
		TimeUnit.SECONDS.sleep(10);
	}
	
	/**
	 * Export the c=data in the MarvinJS dialog
	 * @throws InterruptedException
	 */
	public void exportFromMarvinjsDialog() throws InterruptedException {
		
		
		getWebDriver().get("https://staging.labguru.com/knowledge/projects/21/milestones/51/experiments/731");
		TimeUnit.SECONDS.sleep(4);
		
		
		executeJavascript("document.getElementsByClassName('edit_me open_fancy fancybox.ajax')[0].click();");
	   	TimeUnit.SECONDS.sleep(2);
	   	
	   	
		WebElement sketch = driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(By.id("sketch")));
		TimeUnit.SECONDS.sleep(2);
		WebDriver iframe = getWebDriver().switchTo().frame(sketch);
		TimeUnit.SECONDS.sleep(4);
		
		WebElement imgExport = iframe.findElement(By.xpath(".//div[starts-with(@title,'Export')]"));
		imgExport.click();
		TimeUnit.SECONDS.sleep(2);
		
		
		WebElement btnDownload = getWebDriver().findElement(By.xpath(".//table[starts-with(@id,'saveDialogContent')]/tbody/tr[3]/td/table/tbody/tr/td/button"));
		btnDownload.click();

		
		WebElement btnClose = getWebDriver().findElement(By.xpath(".//*[@class='dialogTop']/td[2]/div/table/tbody/tr/td[2]/div"));
		btnClose.click();
		
	   	
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By
				.id("btn-getmol")));
		WebElement btnSaveComp = getWebDriver().findElement(By.id("btn-getmol"));
		btnSaveComp.click();
	}
	
	
	public boolean addNewExperimentWithScriptInName(String expName) throws InterruptedException {
		
		try {
			clickOnButton(newExperimentButtonId);

			TimeUnit.SECONDS.sleep(2);
			openNewExperimentDialog(expName);
			
		}catch (UnhandledAlertException e) {
			//returns false if the alerts was prompt
			checkForAlerts();
			return false;
		}
		
		return true;//returns true if secured
	}
	
	public boolean searchByCompound() throws InterruptedException {
		
		WebElement btnSearchByComp = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='topnavsearch']/div/search-ui/a/img")));
		btnSearchByComp.click();
		TimeUnit.SECONDS.sleep(5);	
		getWebDriver().switchTo().activeElement();
		drawBenzene();
		TimeUnit.SECONDS.sleep(2);
	
		WebElement btnSearch = getWebDriver().findElement(By.xpath(".//*[@class='green-line-buttons top-green-line']/input"));
		btnSearch.click();		
		TimeUnit.SECONDS.sleep(10);
			
		List<WebElement> resultGroups = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
				(By.xpath(".//*[@class='results-groups']/li")));
		
		return (resultGroups.size() > 0);
		
	}
	public boolean addSampleWithGenericCollection(String collectionName,String sampleName) throws InterruptedException {
		
		boolean created = false;
		WebElement procedureDescToolBar = getDescToolBarElement();
		TimeUnit.SECONDS.sleep(1);

		WebElement samplesLoader = procedureDescToolBar.findElement(By
				.cssSelector(".samples.load"));
		samplesLoader.click();

		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".inline_form_container.type_samples")));

		TimeUnit.SECONDS.sleep(2);
		//open the dropdown for type list to count the number of types
		WebElement dropType = getWebDriver().findElement(By.xpath(".//*[@id='s2id_sample_item_type']/a"));
		dropType.click();
		TimeUnit.SECONDS.sleep(2);

		List <WebElement> typesList = getWebDriver().findElements(By.xpath(".//*[@id='select2-drop']/ul/li"));
		for (int j = 1; j <= typesList.size(); j++) {
			WebElement type = getWebDriver().findElement(By.xpath(".//*[@id='select2-drop']/ul/li[" + j +"]/div"));
			if(type.getText().equals(collectionName)){
				type.click();

				TimeUnit.SECONDS.sleep(1);
				
				addSampleName(sampleName);
				break;
			}
		}
		

		WebElement btnSave = driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(By
						.cssSelector(".inline_submit.samples_save_button")));
		btnSave.click();
		TimeUnit.SECONDS.sleep(2);
		
		refreshPage();
		executeJavascript("document.getElementsByClassName('edit_me')[4].click();");
		TimeUnit.SECONDS.sleep(2);
		
		//check that after clicking on edit - the name of the sample from generic collection type is still shown
		WebElement name = getWebDriver().findElement(By.xpath(".//*[@id='s2id_sample_item_id']/a/span[1]"));
		if(name.getText().equals(sampleName)){
			created = true;
		}
		
		return created;
	}
	
	/**
	 * 
	 * @param sampleName
	 * @param addTube - true to add tube,false without tube
	 * @return
	 * @throws InterruptedException
	 */
	public boolean addSample(String sampleName,boolean addTube) throws InterruptedException {
		
		boolean created = false;
		WebElement procedureDescToolBar = getDescToolBarElement();
		TimeUnit.SECONDS.sleep(1);

		WebElement samplesLoader = procedureDescToolBar.findElement(By.cssSelector(".samples.load"));
		samplesLoader.click();

		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".inline_form_container.type_samples")));

		TimeUnit.SECONDS.sleep(2);
		//open the dropdown for type list to count the number of types
		WebElement dropType = getWebDriver().findElement(By.xpath(".//*[@id='s2id_sample_item_type']/a"));
		dropType.click();
		TimeUnit.SECONDS.sleep(2);

		List <WebElement> typesList = getWebDriver().findElements(By.xpath(".//*[@id='select2-drop']/ul/li"));
		for (int j = 1; j <= typesList.size(); j++) {
			WebElement type = getWebDriver().findElement(By.xpath(".//*[@id='select2-drop']/ul/li[" + j +"]/div"));
			if(LGConstants.PLASMIDS.startsWith(type.getText())){
				type.click();

				TimeUnit.SECONDS.sleep(1);
				
				addSampleName(sampleName);
				TimeUnit.SECONDS.sleep(3);
				if(addTube)
					addStockName();
				break;
			}
		}
		

		WebElement btnSave = driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(By
						.cssSelector(".inline_submit.samples_save_button")));
		btnSave.click();
		TimeUnit.SECONDS.sleep(2);
		
		//simulate refresh to the page
		refreshPage();
		executeJavascript("document.getElementsByClassName('edit_me')[4].click();");
		TimeUnit.SECONDS.sleep(2);
		
		//check that after clicking on edit - the name of the tube from is still shown and editable
		WebElement tube = getWebDriver().findElement(By.xpath(".//*[@id='s2id_sample_stock_id']/a/span[1]"));
		if(!tube.getText().equals("Select tube")){
			created = true;
		}
		refreshPage();
		checkForAlerts();
		return created;
	}
	
	private void addStockName() throws InterruptedException {
		
		WebElement dropTube = getWebDriver().findElement(By.xpath(".//*[@id='s2id_sample_stock_id']/a/span[2]/b"));
		if(!dropTube.isEnabled())
			return;
		dropTube.click();
		TimeUnit.SECONDS.sleep(2);

		WebElement inputTxt = getWebDriver().findElement(By.xpath(".//*[@id='select2-drop']/ul/li/ul/li[last()]/div"));
		inputTxt.click();
		TimeUnit.SECONDS.sleep(3);
		
	}
	
	private void addStockName(Sample sample) throws InterruptedException {
		
		
		if(getWebDriver().findElement(By.xpath(".//*[@id='s2id_sample_stock_id']/a/span[1]")).getText().contains("N/A")){
			sample.setStock("");
			return;
		}
		
		WebElement dropTube = getWebDriver().findElement(By.xpath(".//*[@id='s2id_sample_stock_id']/a/span[2]/b"));
		dropTube.click();
		TimeUnit.SECONDS.sleep(2);

		WebElement inputTxt = getWebDriver().findElement(By.xpath(".//*[@id='select2-drop']/ul/li/ul/li[last()]/div"));
		inputTxt.click();
		TimeUnit.SECONDS.sleep(6);
		inputTxt = getWebDriver().findElement(By.xpath(".//*[@id='s2id_sample_stock_id']/a/span[1]/span/span[2]"));
		String stockNameFormat = inputTxt.getText().substring(0, inputTxt.getText().indexOf(' '));
		sample.setStock(stockNameFormat);
		TimeUnit.SECONDS.sleep(3);
		
	}
	private void addSampleName(String sampleName) throws InterruptedException {
		
		WebElement dropName = getWebDriver().findElement(By.xpath(".//*[@id='s2id_sample_item_id']/a/span[2]/b"));
		dropName.click();
		TimeUnit.SECONDS.sleep(2);
		
		WebElement inputTxt = getWebDriver().findElement(By.xpath(".//*[@id='select2-drop']/div/input"));
		inputTxt.clear();
		inputTxt.sendKeys(sampleName);

		TimeUnit.SECONDS.sleep(3);

		WebElement addTo = getWebDriver().findElement(By.xpath(".//*[@id='select2-drop']/ul/li/a[@class='addto']"));
		addTo.click();

		TimeUnit.SECONDS.sleep(5);
		
		WebElement selectedName = getWebDriver().findElement(By.xpath(".//*[@id='select2-drop']/ul/li/ul/li/div"));
		if (selectedName.getText().equals(sampleName))
			selectedName.click();
	}

	public String updateContent() throws InterruptedException {
	
		return updateName("projects_experiment_title",".//*[@id='projects_experiment_submit_action']/input");
	}
	
	public boolean editSample() {
		boolean created = false;

		try {
			TimeUnit.SECONDS.sleep(2);
			executeJavascript("document.getElementsByClassName('edit_me')[4].click();");
			
			TimeUnit.SECONDS.sleep(3);
			addStockName();
			created = true;
		} catch (Exception e) {
			created = false;
		}
		return created;
	}
	public boolean checkLinkedResources(String protocol) {
		
		WebElement linkName = getWebDriver().findElement(By.cssSelector(".link_name.ng-binding"));
		String name = linkName.getText();

		return name.equals(protocol);
	}

}
