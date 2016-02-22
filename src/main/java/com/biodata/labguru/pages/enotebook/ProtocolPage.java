package com.biodata.labguru.pages.enotebook;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.model.Sample;


public class ProtocolPage extends ExperimentPage{
	

	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	@Override
	public By getDuplicateLocator() {
		
		return By.cssSelector(".duplicate");
	}
	
	public boolean searchProtocolInDirectory(String name) {
		
		WebElement txtSearch = getWebDriver().findElement(By.cssSelector("#gosearch"));
		sendKeys(txtSearch, name);
		
		WebElement btnSearch = getWebDriver().findElement(By.xpath(".//*[@id='frm_search_box']/input[@type='submit']"));
		btnSearch.click();
		
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("protocols")));
		
		List <WebElement> list = getWebDriver().findElements(By.xpath(".//*[@id='protocols']/div"));
		for (WebElement protocol : list) {
			if(protocol.getText().equals(name))
				return true;
		}
		return false;
		
	}

	public String addProtocolToAccount(String newProtocol) throws InterruptedException {
		
		String createdProtocol = addProtocol(newProtocol);
		
		addSteps();
		
		return createdProtocol;
	}
	
	public String addTextDescriptionToProtocol(String descToTest) throws Exception {

		return addTextToDesc(".//*[@id='main-content']/div[1]/div[6]/div/ul",descToTest);
	}
	
	public String addNewProcedureToProtocol(String procedureName) throws InterruptedException {

		//update first procedure tab
		executeJavascript("document.getElementsByClassName('edit_me')[3].click();");

		TimeUnit.SECONDS.sleep(2);
		WebElement procedure1 = getWebDriver().findElement(By.xpath(".//*[@id='experiment_procedure_name']"));
		procedure1.sendKeys("Procedure");
		
		executeJavascript("document.getElementsByClassName('inline_submit')[3].click();");
		TimeUnit.SECONDS.sleep(2);
		
		//add tab
		WebElement addTab = getWebDriver().findElement(By.xpath(".//*[@id='addtab']"));
		addTab.click();

		TimeUnit.SECONDS.sleep(2);

		executeJavascript("document.getElementsByClassName('edit_me')[4].click();");

		TimeUnit.SECONDS.sleep(2);
		
		
		List<WebElement> titlesList = getWebDriver().findElements(By.xpath(".//*[@id='experiment_procedure_name']"));
		
		for (WebElement txtProcedureName : titlesList) {
			if(txtProcedureName.getAttribute("value").equals("Untitled"))
				txtProcedureName.sendKeys(procedureName);
		}

		TimeUnit.SECONDS.sleep(1);
		executeJavascript("document.getElementsByClassName('inline_submit')[4].click();");
		TimeUnit.SECONDS.sleep(2);

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
	
	public String addSamplesToProcedureInProtocol(String name) throws InterruptedException {

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
	
			sampleId = "sample_tr_" + id;
			
			WebElement sampleType = getWebDriver().findElement(By.xpath(".//*[@id='"+ sampleId + "']/td[2]"));
			WebElement sampleName = getWebDriver().findElement(By.xpath(".//*[@id='"+ sampleId + "']/td[3]/a"));
			WebElement remark = getWebDriver().findElement(By.xpath(".//*[@id='"+ sampleId + "']/td[@class='remarks']"));
			
			//check the correctness of data - if not correct - collect the type of the broken type
			if (!sample.name.equals(sampleName.getText())){
				created = created + " , " + sample.type ;
				System.out.println("sample.type :" + sample.type + "***** " +sampleType.getText());
				System.out.println("sample.name :" + sample.name + "***** " +sampleName.getText());
				System.out.println("sample.remark :" + sample.remark + "***** " +remark.getText());
			}
			deleteSample();
			checkForAlerts();
			TimeUnit.SECONDS.sleep(2);
			samplesLoader = procedureDescToolBar.findElement(By
					.cssSelector(".samples.load"));
			samplesLoader.click();
			TimeUnit.SECONDS.sleep(2);
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
	@Override
	protected Sample addSamples(String name,int typeSelectionIndex,String sampleRowId)throws InterruptedException {

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
		
		TimeUnit.SECONDS.sleep(3);
		WebElement remark =  getWebDriver().findElement(By.xpath(".//*[@id='"+ sampleRowId +"']/fieldset/ol/li[@id='sample_remarks_input']/input"));
		remark.clear();
		remark.sendKeys(name);	
		remark.sendKeys(Keys.TAB);	
		TimeUnit.SECONDS.sleep(1);
		sample.setRemark(name);

		return sample;
	}
	
	public String addProtocol(String newProtocol) throws InterruptedException {
		
		if(hasList()){
			//look for button 'AddProtocol' and click it
			WebElement btnAddProtocol = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new_protocol")));
			btnAddProtocol.click();
			
		}else{
			//look for the link to add manually the protocol from the Protocol Directory
			WebElement linkAddManually = driverWait.until(ExpectedConditions.visibilityOfElementLocated
					(By.xpath(".//*[@id='main-content']/div/div[1]/a")));
			
			linkAddManually.click();
		}
		
		TimeUnit.SECONDS.sleep(2);
	   	executeJavascript("document.getElementsByClassName('edit_me')[0].click();");
	   	TimeUnit.SECONDS.sleep(2);
	   	closeIridizePopups();
	   	
		WebElement txtProtocolName = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.id("knowledgebase_protocol_name")));
		
		sendKeys(txtProtocolName, newProtocol);
		
		closeIridizePopups();
		
		List<WebElement> saveBtnList = getWebDriver().findElements(By.xpath(".//*[@id='knowledgebase_protocol_submit_action']/input"));
		for (WebElement btnSave : saveBtnList) {
			if(btnSave.isDisplayed()){
				btnSave.click();
				TimeUnit.SECONDS.sleep(1);
			}
		} 
		
		txtProtocolName = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.cssSelector(".element-plain-text")));
		String txt =  txtProtocolName.getText();
		return txt;
	}


	
	public String showProtocolsIndexTable(){
		
		WebElement pageTitle = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='index-header']/h1")));
		
		String formateTitle = pageTitle.getText();
		formateTitle = formateTitle.substring(0,formateTitle.indexOf('\n')).trim();
		return formateTitle;	
	}

	public boolean addProtocolFromDirectory(){
		
		WebElement btnAddFromDirectory = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.id("add_from_protocols_directory")));
		
		btnAddFromDirectory.click();
		
		WebElement pageTitle = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#main-content>div>h1")));
		
		if(pageTitle.getText().equals("Protocol Directory")){
			
			List <WebElement> list = getWebDriver().findElements(By.cssSelector(".external_protocol_container.fleft>ul>li>a"));
			for (WebElement protocol : list) {
				try {
					String selectedName = protocol.getText();
					protocol.click();
					pageTitle = driverWait.until(ExpectedConditions.visibilityOfElementLocated
								(By.id("page-title")));
					
					String fullTitle = pageTitle.getText();
					fullTitle = fullTitle.substring(fullTitle.indexOf('\n') +1,fullTitle.indexOf('|')).trim();
					
					return fullTitle.equals(selectedName);
				} catch (Exception e) {
					return !(checkForNotyMessage(By.cssSelector(".noty_text")).equals("Cannot load protocol data"));
				}
			}
		}
		return false;
	}
	
	public String openProtocol(String protocolName) throws InterruptedException {
		
		invokeSearchItem(protocolName);
		
		WebElement protocolToOpen = driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath(".//*[@id='protocols']/div[2]/h4/a/strong")));
		
		protocolToOpen.click();
		
		WebElement title = driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath(".//*[@id='knowledgebase_protocol_name_input']/span")));
		return title.getText();
	}

	/**
	 * hasList
	 * @return true if has 'Lab protocols',false if title is 'Protocol Directory'
	 */
	public boolean hasList() {
		
		//has lab protocols
		try{
	    	WebElement title = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='index-header']/h1")));
	    	String fullTitle = title.getText();
	    	fullTitle = fullTitle.substring(0, fullTitle.indexOf('\n') );
	    	return fullTitle.equals("Lab Protocols");
		}catch(Exception e){
			return false;
		}
	}

	public String startExperimentFromProtocol(String expName) throws InterruptedException {

		
		clickOnButton(newExperimentButtonId);
		TimeUnit.SECONDS.sleep(2);
		
		openNewExperimentDialog(expName);

		return getWebDriver().getTitle();
	}

	public void selectProtocol() throws InterruptedException {
		
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("protocols")));
		
		List <WebElement> list = getWebDriver().findElements(By.xpath(".//*[@id='protocols']/div"));
		for (int i=2 ; i<=list.size();) {
			WebElement link = getWebDriver().findElement(By.xpath(".//*[@id='protocols']/div[" +i  + "]/h4/a"));
			link.click();
			TimeUnit.SECONDS.sleep(1);
			break;
		}
	}

	public String updateContent() throws InterruptedException {
		
		return updateName("knowledgebase_protocol_name",".//*[@id='knowledgebase_protocol_submit_action']/input");

	}
	

	@Override
	public String getPageTitleXPath() {
		return ".//*[@id='knowledgebase_protocol_name_input']/span";
	}
}
