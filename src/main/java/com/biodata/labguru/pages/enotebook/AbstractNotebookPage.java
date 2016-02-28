package com.biodata.labguru.pages.enotebook;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.AdminPage;
import com.biodata.labguru.pages.IListView;



public abstract class AbstractNotebookPage extends AdminPage implements IListView{

	final String newExperimentButtonId = "new_experiment_popup_btn";
	
	public abstract String getPageTitleXPath();
	
	public  By getDuplicateLocator() {
		return By.cssSelector(".duplicate");
	}
	
	public String getTitle() {
		WebElement pageTitle = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(getPageTitleXPath())));
        String newName = pageTitle.getText();
        return newName;
	}
	
	protected int addSteps() throws InterruptedException {
		
		WebElement procedureDescToolBar =  getDescToolBarElement();
		TimeUnit.SECONDS.sleep(2);
					
		WebElement stepsLoader = procedureDescToolBar.findElement(By.cssSelector(".steps.load"));
		stepsLoader.click();
		TimeUnit.SECONDS.sleep(2);
		int steps = 0;

		
		List<WebElement> stepsList = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@id='steps']/ol/li")));
		for (int i = 1; i <= stepsList.size(); i++) {
			executeJavascript("$('#step_data_"+ i +"_').redactor('code.set', '<p>test step editor: " + i + "</p>');");
			steps++;
		}

		TimeUnit.SECONDS.sleep(1);
		List <WebElement> btnSaveList = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
				(By.xpath(".//*[@class='steps_form formtastic element']/fieldset/ol/li[2]/input")));
		for (WebElement btn : btnSaveList) {
			btn.click();
			TimeUnit.SECONDS.sleep(2);
		}
		
		return steps;
	}
	
	protected void writeInTable(String data) throws InterruptedException {
		
		String script = "var spread = $('.excel_container').find('.excel').wijspread('spread');"
				+ "var sheet = spread.getActiveSheet();"
				+ "var cell = sheet.getCell(1,1);"
				+ "cell.value('" + data + "');";	
		executeJavascript(script);
	}
	
	public void changeVersion(String version) throws InterruptedException {
		
		WebElement versionBtn;
		try {
			//new account - will open experiment on beta version by default
			versionBtn = getWebDriver().findElement(By.id("link_to_v1"));
			
			if(version.equals(LGConstants.EXPERIMENT_CURRENT)){
				
				versionBtn.click();
				TimeUnit.SECONDS.sleep(3);
			}
		
			
		} catch (NoSuchElementException e) {
			getLogger().debug("old account - should open on current version by default, switching to beta...");
			if(version.equals(LGConstants.EXPERIMENT_CURRENT)){
				//if we need current version - do nothing
				return;
			}
			versionBtn = getWebDriver().findElement(By.id("link_to_beta"));
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
	
	protected void deleteSample() throws InterruptedException {
		  //delete all created samples
		getLogger().debug("before delete samples");
		executeJavascript("document.getElementsByClassName('delete_me')[0].click();");
		getLogger().debug("after delete samples");
		TimeUnit.SECONDS.sleep(2);
		
	}
	
	protected void saveSample() throws InterruptedException {
		
		executeJavascript("document.getElementsByClassName('samples_save_button')[0].click();");
		TimeUnit.SECONDS.sleep(3);    
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
	
	@Override
	public String duplicateItem() throws InterruptedException {
		
		TimeUnit.SECONDS.sleep(2);
		
		WebElement btnDuplicate = driverWait.until(ExpectedConditions.visibilityOfElementLocated(getDuplicateLocator()));
		btnDuplicate.click();
		
		TimeUnit.SECONDS.sleep(2);
		
		//a popup is open to select what to duplicate
		if(!(this instanceof ProtocolPage)){
			 WebElement btnDuplicateInPopup = driverWait.until(ExpectedConditions.visibilityOfElementLocated
					 (By.xpath(".//*[@id='do_print']")));
			 btnDuplicateInPopup.click();
			 
			 switchToNewTab();
		}

        WebElement pageTitle = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(getPageTitleXPath())));
        String newName = pageTitle.getText();
        return newName;
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
				dropbox.click();
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

}
