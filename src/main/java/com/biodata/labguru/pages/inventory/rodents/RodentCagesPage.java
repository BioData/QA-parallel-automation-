package com.biodata.labguru.pages.inventory.rodents;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;


public class RodentCagesPage extends RodentPage{

	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	@Override
	protected String getCollectionName() {
		return LGConstants.CAGE_PREFIX;
	}
	
	@Override
	protected void addDescription(String name){
		try {
			writeInRedactor("system_storage_rodent_cage_description", "this is the description for the " + name);
		} catch (InterruptedException e) {
			
		}
	}
	
	@Override
	public List<String> getAvailableColumnsForCustomiseTableView() {
		List<String> columns = super.getAvailableColumnsForCustomiseTableView();
		columns.add("preferences_specimens");
		columns.add("preferences_breeding");
		columns.add("preferences_storage_location");

		
		//cage not hold some columns so we remove it
		columns.remove("preferences_auto_name");//sysid
		columns.remove("preferences_source");
		columns.remove("preferences_alternative_name");
		return columns;
	}
	
	@Override
	public boolean hasList() {
		try{
			//look for 'new' button when the list is populated
			driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new_cage")));
			return true;
			
		}catch(Exception ex){
			//first item - page already open
			return false;
		}
	}
	public String addSpecimenFromCage(String cage) throws InterruptedException{
		
		WebElement btnAdd = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add_specimen")));
		btnAdd.click();
		TimeUnit.SECONDS.sleep(5);
		try {
			String specName = createNewSpecimenIfNotExist(cage);
			showCreatedCage(cage);
			return checkSpecimenCreation(specName,cage);
		} catch (NoSuchElementException e) {
			//regular dialog to add specimen was open
			TimeUnit.SECONDS.sleep(2);
			String specName = addSingleSpecimen();
			return checkSpecimenCreation(specName,cage);
		}
		
	}

	private void createNewSpecimen(String specName) {
		setSpecimenDetails(specName);
		save();
	}
	

	
	private void selectTreatmentsTab(){
		WebElement tab = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tabs-cage_treatment-link")));
		tab.click();
	}


	@Override
	protected void addItemWithGivenName(String name) {
		
		if(hasList()){
			//not first  - look for button 'New'
			WebElement btnAdd = getWebDriver().findElement(By.id("new_cage"));
			btnAdd.click();
			
		}
	      
        WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
        sendKeys(txtName, name);
	}

	public String addTreatmentFromCage(String name,String cageName) throws InterruptedException {
		
		selectTreatmentsTab();
		TimeUnit.SECONDS.sleep(2);

		List<String> specNames = new ArrayList<String>();
		
		WebElement btnAdd = getWebDriver().findElement(By.xpath(".//*[@id='tabs-treatment']/div/div/ul/li/a/span"));
		String buttonTitle = btnAdd.getText();
		btnAdd.click();
		TimeUnit.SECONDS.sleep(2);
		//if account has no specimen - first add one specimen 
		if(buttonTitle.equals("Add Specimen")){
			try {
				//check if the dialog that says "You have no rodent specimens to add " is opened - if open - click on 'add specimen' link
				String specName = createNewSpecimenIfNotExist(cageName);
				specNames.add(specName);
				showCreatedCage(cageName);
				selectTreatmentsTab();
				TimeUnit.SECONDS.sleep(2);
				btnAdd = getWebDriver().findElement(By.xpath(".//*[@id='tabs-treatment']/div/div/ul/li/a/span"));
				btnAdd.click();
				//regular dialog to add specimen 
				TimeUnit.SECONDS.sleep(2);
				addSpecimenIfExist(name, specNames);
			} catch (NoSuchElementException e) {
				//regular dialog to add specimen was open
				addSpecimenIfExist(name, specNames);
				selectSpecimensTab();
				TimeUnit.SECONDS.sleep(2);
				selectTreatmentsTab();
				TimeUnit.SECONDS.sleep(2);
				btnAdd = getWebDriver().findElement(By.xpath(".//*[@id='tabs-treatment']/div/div/ul/li/a/span"));
				btnAdd.click();
				TimeUnit.SECONDS.sleep(2);
				specNames = addSingleTreatment(name, cageName);
			}


			 
		}else if(buttonTitle.equals("Add Treatment")){//already has specimen click button 'AddTreatment' 
			specNames = addSingleTreatment(name, cageName);
		}
		
		 //refreshing the treatments tab
		 selectSpecimensTab();
		//check the creation of the treatment in the treatments tab of the cage
		selectTreatmentsTab();
		return checkTreatmentCreation(name,specNames);

	}
	public List<String> addSingleTreatment(String name, String cageName)
			throws InterruptedException {
		List<String> specNames;
		Map<String,List<String>> map = insertTreatmentDetails(name, LGConstants.TODAY);
		specNames = map.get(cageName);
		applyTreatment();
		TimeUnit.SECONDS.sleep(2);
		return specNames;
	}

	private void addSpecimenIfExist(String name, List<String> specNames) throws InterruptedException {

		addSingleSpecimen();

		TimeUnit.SECONDS.sleep(2);
		/*
		WebElement txtTreatName = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//input[starts-with(@id,'td_name')]")));
		txtTreatName.sendKeys(name);
		 
		WebElement specimenNameElm = getWebDriver().findElement(By.xpath(".//a[starts-with(@id,'link_to')]"));
		specNames.add(specimenNameElm.getText());
		 
		WebElement btnSubmitAll = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add_all_btn")));
		btnSubmitAll.click();*/
	}

	public void showCreatedCage(String cageName) {
		//search for the created cage and open it
		showRodentCages();
		searchCageAndSelect(cageName);		
	}
	
	private void searchCageAndSelect(String cageName){
		
		 WebElement txtSearch = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".searchtextbox")));
         sendKeys(txtSearch, cageName);
        
         WebElement btnSearch = getWebDriver().findElement(By.xpath(".//*[@value='search-button']"));
         btnSearch.click(); 
         
         WebElement item = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				 (By.xpath(".//*[@id='index_table']/tbody/tr[2]/td[2]/a")));
		 item.click();
		
	}

	private String createNewSpecimenIfNotExist(String cageName) throws InterruptedException {
	
		WebElement btnAdd = getWebDriver().findElement(By.xpath(".//*[@id='data']/div/p/a"));

		btnAdd.click();
		
		TimeUnit.SECONDS.sleep(2);
		String specName = LGConstants.RODENT_SPECIMEN_PREFIX + cageName;
		createNewSpecimen(specName);
		
		return specName;
	}

	private String addSingleSpecimen() throws InterruptedException {

		TimeUnit.SECONDS.sleep(5);
		getWebDriver().switchTo().activeElement();
		//add specimen,select one,add to cage
		String specimenName = "";
		List<WebElement> rows = getWebDriver().findElements
				(By.xpath(".//*[@id='index_table']/tbody/tr"));
		
		TimeUnit.SECONDS.sleep(2);
		
		for (int i=2; i<= rows.size() ;i++) {	 
			 WebElement lblName = getWebDriver().findElement(By.xpath(".//*[@id='index_table']/tbody/tr[" + i + "]/td[3]/a"));
			 specimenName = lblName.getText();
			 
			 //select first specimen
			 WebElement chkElem = getWebDriver().findElement(By.xpath(".//*[@id='index_table']/tbody/tr[" + i + "]/td[1]/input[@type='checkbox']"));
			 if(!chkElem.isSelected())
				 chkElem.click();

			 TimeUnit.SECONDS.sleep(1);
			 //add it to cage
			 WebElement btnAddToCage = getWebDriver().findElement(By.id("place_specimens_in_cage"));
			 btnAddToCage.click();

			 TimeUnit.SECONDS.sleep(3);
			 getWebDriver().switchTo().activeElement();
			 break;
		 }
		 
		 return specimenName;
	}

	@Override
	protected String getEditCollectionPrefix() {	
		throw new UnsupportedOperationException("This action is not supported by this collection.");
	}

	@Override
	protected String getImportXPath() {
		throw new UnsupportedOperationException("This action is not supported by this collection.");
	}

	@Override
	protected String getFileNameToImport() {
		throw new UnsupportedOperationException("This action is not supported by this collection.");
	}
	
	
	public boolean addNewCageWithLocation(String name) throws InterruptedException {
		
		addItemWithGivenName(name);    
		
		WebElement arrow = getWebDriver().findElement(By.xpath(".//*[@id='storages_tree']/ul/li[1]/div/a"));
		arrow.click();
		TimeUnit.SECONDS.sleep(1);
		
		List<WebElement> closeStorages = getWebDriver().findElements(By.cssSelector(".jqtree-folder.jqtree_common.jqtree-closed>div>span>span"));
		for (WebElement storage : closeStorages) {
			//select the first storage under the 'Lab Room'
			storage.click();
			TimeUnit.SECONDS.sleep(1);
			break;
		}
        
        
        save();
        String selectedLocations = "";
       
        List<WebElement> storageList = getWebDriver().findElements(By.xpath(".//*[@id='lg_info_tab_storage']/a"));
        for (int j = 1; j <= storageList.size(); j++) {
        	 WebElement storageLocationInPage = getWebDriver().findElement(By.xpath(".//*[@id='lg_info_tab_storage']/a[" + j + "]/span"));
        	 selectedLocations = selectedLocations +" " + storageLocationInPage.getText();
		}
       
     
        editPage();
        
        WebElement existLocation = getWebDriver().findElement(By.id("exist_storage"));
        String existLocationString = existLocation.getText();
        String formattedLocation =  (existLocationString.substring(0, existLocationString.lastIndexOf(name))).trim();
        
		return selectedLocations.trim().equals(formattedLocation);
	}
}
