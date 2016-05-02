package com.biodata.labguru.pages.enotebook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProtocolPageV2 extends AbstractNotebookPage{
	
	private final String shareToRepositoryActionId = "share-to-repository";
	private final String archiveActionId = "archive_item";
	
	
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
		
		addStepToSection("0");
		
		return createdProtocol;
	}
	
//	public String addTextDescriptionToProtocol(String descToTest) throws Exception {
//
//		return addTextToDesc(".//*[@id='main-content']/div[1]/div[6]/div/ul",descToTest);
//	}
	
//	public String addNewProcedureToProtocol(String procedureName) throws InterruptedException {
//
//		//update first procedure tab
//		executeJavascript("document.getElementsByClassName('edit_me')[3].click();");
//
//		TimeUnit.SECONDS.sleep(2);
//		WebElement procedure1 = getWebDriver().findElement(By.xpath(".//*[@id='experiment_procedure_name']"));
//		procedure1.sendKeys("Procedure");
//		
//		executeJavascript("document.getElementsByClassName('inline_submit')[3].click();");
//		TimeUnit.SECONDS.sleep(2);
//		
//		//add tab
//		WebElement addTab = getWebDriver().findElement(By.xpath(".//*[@id='addtab']"));
//		addTab.click();
//
//		TimeUnit.SECONDS.sleep(2);
//
//		executeJavascript("document.getElementsByClassName('edit_me')[4].click();");
//
//		TimeUnit.SECONDS.sleep(2);
//		
//		
//		List<WebElement> titlesList = getWebDriver().findElements(By.xpath(".//*[@id='experiment_procedure_name']"));
//		
//		for (WebElement txtProcedureName : titlesList) {
//			if(txtProcedureName.getAttribute("value").equals("Untitled"))
//				txtProcedureName.sendKeys(procedureName);
//		}
//
//		TimeUnit.SECONDS.sleep(1);
//		executeJavascript("document.getElementsByClassName('inline_submit')[4].click();");
//		TimeUnit.SECONDS.sleep(2);
//
//		String name = "";
//		List<WebElement> txtist = getWebDriver().findElements(By.cssSelector(".element-plain-text"));
//		for (WebElement title : txtist) {
//			if(title.getText().equals(procedureName)){
//				name = title.getText();
//				break;
//			}
//		}
//		return name;
//
//	}
	
//	public String addSamplesToProcedureInProtocol(String name) throws InterruptedException {
//
//		String created = "";
//
//		WebElement procedureDescToolBar = getDescToolBarElement();
//		TimeUnit.SECONDS.sleep(1);
//
//		WebElement samplesLoader = procedureDescToolBar.findElement(By.cssSelector(".samples.load"));
//		samplesLoader.click();
//		TimeUnit.SECONDS.sleep(2);
//		
//		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".inline_form_container.type_samples")));
//
//		TimeUnit.SECONDS.sleep(3);
//		//open the dropdown for type list to count the number of types
//		WebElement dropType = getWebDriver().findElement(By.xpath(".//*[@id='s2id_sample_item_type']/a"));
//		dropType.click();
//		TimeUnit.SECONDS.sleep(2);
//
//		List <WebElement> typesList = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
//				(By.xpath(".//*[@id='select2-drop']/ul/li")));
//		int numOftypes = typesList.size();
//		dropType = getWebDriver().findElement(By.id("select2-drop-mask"));
//		dropType.click();
//		TimeUnit.SECONDS.sleep(3);
//		//create rows as the number of types (minus 1 because the first row is already exist)
//		for (int i = 1; i <= numOftypes; i++) {
//			
//			WebElement idElm = driverWait.until(ExpectedConditions.visibilityOfElementLocated
//					(By.id("sample_name")));
//			String id = idElm.getAttribute("value");
//			String sampleId = "edit_sample_" + id;
//			Sample sample = addSamples(LGConstants.SAMPLE_PREFIX + id,i,sampleId);
//			
//			TimeUnit.SECONDS.sleep(2);
//			saveSample();
//	
//			sampleId = "sample_tr_" + id;
//			
//			WebElement sampleType = getWebDriver().findElement(By.xpath(".//*[@id='"+ sampleId + "']/td[2]"));
//			WebElement sampleName = getWebDriver().findElement(By.xpath(".//*[@id='"+ sampleId + "']/td[3]/a"));
//			WebElement remark = getWebDriver().findElement(By.xpath(".//*[@id='"+ sampleId + "']/td[@class='remarks']"));
//			
//			//check the correctness of data - if not correct - collect the type of the broken type
//			if (!sample.name.equals(sampleName.getText())){
//				created = created + " , " + sample.type ;
//				System.out.println("sample.type :" + sample.type + "***** " +sampleType.getText());
//				System.out.println("sample.name :" + sample.name + "***** " +sampleName.getText());
//				System.out.println("sample.remark :" + sample.remark + "***** " +remark.getText());
//			}
//			deleteSample();
//			checkForAlerts();
//			TimeUnit.SECONDS.sleep(2);
//			samplesLoader = procedureDescToolBar.findElement(By
//					.cssSelector(".samples.load"));
//			samplesLoader.click();
//			TimeUnit.SECONDS.sleep(2);
//		}
//
//		return created;
//	}
//	
//	/**
//	 * 
//	 * @param name - name of the experiment for prefix to the new sample
//	 * @param samplesContainer
//	 * @param typeSelectionIndex - starts from 1 to select the type of sample to create
//	 * @param sampleRowId - the element id row name
//	 */
//	@Override
//	protected Sample addSamples(String name,int typeSelectionIndex,String sampleRowId)throws InterruptedException {
//
//		Sample sample = new Sample();
//		
//		WebElement dropType =  driverWait.until(ExpectedConditions.visibilityOfElementLocated
//				(By.xpath(".//*[@id='"+ sampleRowId +"']/fieldset/ol/li[2]/div/a")));
//		dropType.click();
//		TimeUnit.SECONDS.sleep(2);
//		
//		
//		WebElement selectedType = driverWait.until(ExpectedConditions.visibilityOfElementLocated
//				(By.xpath(".//*[@id='select2-drop']/ul/li["+ typeSelectionIndex + "]/div")));
//		String type = selectedType.getText();
//		sample.setType(type);
//		selectedType.click();
//		TimeUnit.SECONDS.sleep(4);
//
//		WebElement dropName =  driverWait.until(ExpectedConditions.visibilityOfElementLocated
//				(By.xpath(".//*[@id='"+ sampleRowId +"']/fieldset/ol/li[3]/div/a/span[1]")));
//		dropName.click();
//		TimeUnit.SECONDS.sleep(4);
//
//	
//		WebElement inputTxt =  getWebDriver().findElement(By.xpath(".//*[@id='select2-drop']/div/input"));
//		inputTxt.clear();
//		inputTxt.sendKeys(name);
//		sample.setName(name);
//
//		TimeUnit.SECONDS.sleep(4);
//
//		WebElement addTo = getWebDriver().findElement(By.xpath(".//*[@class='select2-no-results']/a"));
//		if(addTo.getText().contains(name))
//			addTo.click();
//
//		TimeUnit.SECONDS.sleep(5);
//		
//		List<WebElement> labels = getWebDriver().findElements(By.xpath(".//*[@class='select2-result-label']"));
//		for (WebElement selectedName : labels) {
//			if (selectedName.getText().equals(name)){			
//				selectedName.click();
//				break;
//			}
//		}
//		
//		TimeUnit.SECONDS.sleep(3);
//		WebElement remark =  getWebDriver().findElement(By.xpath(".//*[@id='"+ sampleRowId +"']/fieldset/ol/li[@id='sample_remarks_input']/input"));
//		remark.clear();
//		remark.sendKeys(name);	
//		remark.sendKeys(Keys.TAB);	
//		TimeUnit.SECONDS.sleep(1);
//		sample.setRemark(name);
//
//		return sample;
//	}
	
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
		closeIridizePopups();
	   	executeJavascript("document.getElementsByClassName('edit_me')[0].click();");
	   	TimeUnit.SECONDS.sleep(2);

		WebElement txtProtocolName = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.id("knowledgebase_protocol_name")));
		
		sendKeys(txtProtocolName, newProtocol);
		
		closeIridizePopups();
		
		//save the name
    	executeJavascript("document.getElementsByClassName('inline_submit')[0].click();");
		TimeUnit.SECONDS.sleep(2);
		
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
					return !(checkForNotyMessage().equals("Cannot load protocol data"));
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
	    	return fullTitle.startsWith("Lab Protocols");
		}catch(Exception e){
			return false;
		}
	}

	public String startExperimentFromProtocol(String expName) throws InterruptedException {

		
		WebElement btn = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='start_experiment']/a")));
		btn.click();
		
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
	
	//TODO
	public String shareProtocol(boolean toShare) throws InterruptedException {

		clickOnUpperMenuAction(shareToRepositoryActionId);
		TimeUnit.SECONDS.sleep(2);
	
		WebElement checkRepo =  driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("repo-labguru_protocols")));
		if((toShare && !checkRepo.isSelected()) || (!toShare && checkRepo.isSelected()))
			checkRepo.click();
	
		TimeUnit.SECONDS.sleep(1);
		
		String msg = checkForNotyMessage();
		return msg;
	}
	
	public boolean checkProtocolInProtocolsDirectory(String protocol) throws InterruptedException {
		
		showProtocols();
		clickOnButton("add_from_protocols_directory");
		TimeUnit.SECONDS.sleep(1);
		invokeSearchItem(protocol);
	
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("protocols")));
		
		List <WebElement> list = getWebDriver().findElements(By.xpath(".//*[@id='protocols']/div"));
		if(list.size() <= 2){
			try {
				//check there are no results
				getWebDriver().findElement(By.id("no_search_results"));
				return false;
			} catch (Exception e) {
				//there are no matching results but there is also no message - "no search results"
				getLogger().debug(e.getMessage());
				return true;
			}
		}
		for (int i=2 ; i<=list.size();) {
			WebElement protocolName = getWebDriver().findElement(By.xpath(".//*[@id='protocols']/div[" +i  + "]/h4/a/strong"));
			if(protocolName.getText().equals(protocol)){
				return true;
			}
		}
		return false;
	}


	public String updateContent() throws InterruptedException {
		
		return updateName("knowledgebase_protocol_name",".//*[@id='knowledgebase_protocol_submit_action']/input");

	}
	

	@Override
	public String getPageTitleXPath() {
		return ".//*[@id='knowledgebase_protocol_name_input']/span";
	}

	public String copyProtocolFromDirectory() throws InterruptedException {
		
		WebElement btnAddFromDirectory = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.id("add_from_protocols_directory")));
		
		btnAddFromDirectory.click();
		
		WebElement pageTitle = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#main-content>div>h1")));
		
		if(pageTitle.getText().equals("Protocol Directory")){
			
			List <WebElement> list = getWebDriver().findElements(By.cssSelector(".vendor_protocols"));
			if(list.size()> 0){
				WebElement viewAllLink = list.get(0);
				viewAllLink.click();
				TimeUnit.SECONDS.sleep(1);
				selectProtocol();
				WebElement copyBtn = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("copy_protocol")));
				copyBtn.click();
				TimeUnit.SECONDS.sleep(1);
			}
			
		}

		String msg = checkForNotyMessage();
		return msg;
		
	}

	public boolean sign() throws InterruptedException {
		

		clickOnSignAction();

		WebElement proceedBtn = driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(By.id("proceed_sign_toggle")));
		proceedBtn.click();
		TimeUnit.SECONDS.sleep(5);
		driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(By.cssSelector(".signed_note.ng-binding")));
		
		WebElement signedNote = driverWait.until(ExpectedConditions
				.visibilityOfElementLocated(By.cssSelector(".signed_note.ng-binding")));
		String msg = signedNote.getText();
		TimeUnit.SECONDS.sleep(1);
		
		String script = "return $('#more_actions>ul>li#sign_toggle').text();";
		String actionText = (String) executeJavascript(script);
		
		if(msg.startsWith(SIGNED_BY) && actionText.trim().equals(REVERT_SIGNATURE))
			return true;
		return false;
		
	}

	private void clickOnSignAction() throws InterruptedException {
		WebElement action = getWebDriver().findElement(By.xpath(".//*[@id='more_actions']/ul/li[@id='" + signActionId + "']"));	
		executeJavascript("arguments[0].click();",action);
		TimeUnit.SECONDS.sleep(2);
	}

	/**
	 * This only applies for admin users or for the user that signed the experiment
	 */
	public boolean revertSignature() throws InterruptedException {
		
		//TODO:check if this is the user that signed the experiment or an Admin user
		clickOnSignAction();
		WebElement proceedBtn = getWebDriver().findElement(By.id("proceed_sign_toggle"));
		proceedBtn.click();
		TimeUnit.SECONDS.sleep(3);
		
		String script = "return $('#more_actions>ul>li#sign_toggle').text();";
		String action = (String) executeJavascript(script);
		
		
		if(action.trim().equals("Sign"))
			return true;
		return false;
		
	}
	
	public String archiveProtocol() throws InterruptedException {
		
		clickOnUpperMenuAction(archiveActionId);
		String msg = checkForNotyMessage();
		return msg;
		
	}
	
	public String deleteProtocol() throws InterruptedException{
		
		//select delete from menu
		String script = "$('#link_to_delete_item>span>a').click();";
		executeJavascript(script);
		
		TimeUnit.SECONDS.sleep(2);

		String msg = checkForNotyMessage();
		return msg;
		
	}
	
	/**
	 * Check that all permitted actions for sign experiment are there and the not permitted are not shown.
	 * @return true if all permitted buttons are shown and the other not,false otherwise.
	 * @throws InterruptedException
	 */
	public boolean checkAllowedActionsOnSignedProtocol() throws InterruptedException {
		
		//not allowed: archiveActionId,deleteItemActionId
		List<String> allowedActions = getAllowedActionsForSignResource();
		List<String> notAllowedActions = getNotAllowedActionsIds();

		return !allowedActions.contains(notAllowedActions);

	}
	
	protected List<String> getAllowedActionsForSignResource() {

		return new ArrayList<String>(Arrays.asList(viewVersionsActionId,signActionId,duplicateActionId/*,shareToRepositoryActionId*/));
	}

	public String goToProtocols() throws InterruptedException {
		
		WebElement protocolsLink = getWebDriver().findElement(By.xpath(".//*[@id='breadcrumb-file-text-o']/span"));
		protocolsLink.click();
		TimeUnit.SECONDS.sleep(5);
		WebElement title = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/*[@id='index-header']/h1")));
		
		return title.getText();
	}

}
