package com.biodata.labguru.pages.inventory.rodents;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;


public class RodentSpecimensPage extends RodentPage{

	
	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	@Override
	protected String getEditCollectionPrefix() {
		
		return "edit_biocollections_rodent_specimen_";
	}
	
	@Override
	protected String getCollectionName() {
		return "rodent_" + LGConstants.RODENT_SPECIMEN_PREFIX;
	}
	
	@Override
	protected String getCustomizeLinkXpath(String collectionName) {
		
		return ".//a[@href='/system/custom_fields?class=Biocollections::RodentSpecimen']";

	}
	
	@Override
	public List<String> getAvailableColumnsForCustomiseTableView() {
		List<String> columns = super.getAvailableColumnsForCustomiseTableView();
		columns.add("preferences_auto_name");//sysid
		columns.add("preferences_strain");
		columns.add("preferences_experiment");
		columns.add("preferences_ethics");
		columns.add("preferences_sex");
		columns.add("preferences_age");
		columns.add("preferences_ear_tag");
		columns.add("preferences_coat_tag");
		columns.add("preferences_genotype");
		columns.add("preferences_phenotype");
		columns.add("preferences_cage");
		columns.add("preferences_dob");
		columns.add("preferences_dod");
		columns.add("preferences_reserved_by");
		columns.add("preferences_reserved_remarks");
		columns.add("preferences_reserved_date");
		columns.add("preferences_notify_by");
		columns.add("preferences_productive");//status - not selectable
		
		//columns that not appear in specimen
		columns.remove("preferences_source");
		return columns;
	}
	
	public String addTissueToSpecimen(String tissueName) throws InterruptedException {
		
		selectTissuesAndSamplesTab() ;
		
		WebElement addTissueBtn = getWebDriver().findElement(By.id("add_new_tissue"));
		addTissueBtn.click();
		TimeUnit.SECONDS.sleep(1);
		
		return openAddTissueDialog(tissueName);
	}
	
	private String openAddTissueDialog(String tissueName) throws InterruptedException {
		
      
        WebElement popupDialog = getWebDriver().switchTo().activeElement();
        driverWait.until(ExpectedConditions.visibilityOf(popupDialog));
        
        WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated
        		(By.id("name")));
        String prefix = txtName.getAttribute("value");
        if(tissueName != null)
        	sendKeys(txtName, prefix + tissueName);

	    save();
	    TimeUnit.SECONDS.sleep(1); 
	    getWebDriver().switchTo().activeElement();
	    checkForNotyMessage(By.cssSelector(".noty_text"));
	    
	    WebElement pageTitle =driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("page-title")));
	    return pageTitle.getText();

	}

	public void selectTissuesAndSamplesTab() throws InterruptedException{
		
		 WebElement tissuesTab = getWebDriver().findElement(By.id("tabs-tissue-link"));
		 tissuesTab.click();
		 TimeUnit.SECONDS.sleep(1);
	}
	
	public boolean addTreatmentFromSpecimensTable() throws InterruptedException{
		
		boolean valid = false;
		List<WebElement> rows = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
        		(By.xpath(".//*[@id='index_table']/tbody/tr")));
		
		 for (int i=2; i<= rows.size() ;) {
			 //select first specimen
			 WebElement chkElem = getWebDriver().findElement(By.xpath(".//*[@id='index_table']/tbody/tr[" + i + "]/td[1]/input"));
			 chkElem.click();
			 TimeUnit.SECONDS.sleep(1);
			 WebElement lblName = getWebDriver().findElement(By.xpath(".//*[@id='index_table']/tbody/tr[" + i + "]/td[3]/a"));
			 String specName = lblName.getText();
			 WebElement btnAddTreatment = getWebDriver().findElement(By.id("add_treatment"));
			 btnAddTreatment.click();
			 TimeUnit.SECONDS.sleep(1);
			 valid = checkOnlySelectedSpecimenAdded(specName);
			 break;
		 }
		 return valid;
	}
	@Override
	protected String getFileNameToImport() {
		
		return LGConstants.RODENT_SPECIMEN_TEMPLATE;
	}

	protected void addItemWithGivenName(String plasmidName) {

		clickNewButton("new_rodent_specimen");
	      
        setSpecimenDetails(plasmidName);
		
	}
	
	@Override
	protected String getImportXPath() {
		return ".//*[@id='main-content']/div/div[1]/a[@href='/system/imports/new?class=Biocollections%3A%3ARodentSpecimen']";
	}
	
	public boolean checkSpecimenStatus(String specimen,String currentStatus) throws InterruptedException {
		
		 editPage();
		 
		 WebElement dateDOD = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dod_date_picker")));
		 dateDOD.click();
		 TimeUnit.SECONDS.sleep(1);
		 for (int i = 0; i <= 6; i++) {
			 try{
				 //find today date in all available calenders and find the one that displayed and select it
				 List <WebElement> todaySelectList = getWebDriver().findElements(By.cssSelector(".xdsoft_date.xdsoft_day_of_week" + i + ".xdsoft_date.xdsoft_today.xdsoft_weekend>div"));
				 for (WebElement today : todaySelectList) {
					 if(today.isDisplayed()){
						 today.click();
						 i=7;
						 break;
					 }
				 }

			 }catch(NoSuchElementException e){
				 //do nothing - keep searching for today's date
				 continue;
			 }
		 }
		 TimeUnit.SECONDS.sleep(1);
		 save();
		 
		 TimeUnit.SECONDS.sleep(2);
		 WebElement newStatusElem = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				 (By.xpath(".//*[@id='tabs-info']/div[2]/table/tbody/tr[5]/td")));
		 
		//if status was 'alive' and DOD set - status should change to 'Dead'
		 if(currentStatus.equals(LGConstants.SPECIMEN_STATUS_ALIVE))
			 return newStatusElem.getText().equals(LGConstants.SPECIMEN_STATUS_DEAD);
		 else//if status was other then 'alive' - should stay the same
			 return newStatusElem.getText().equals(currentStatus); 
	}
	
	public void setStatus(String currentStatus) throws InterruptedException {
		
		 editPage();
		 
		 WebElement statusElem = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				 (By.xpath( ".//*[@id='s2id_biocollections_rodent_specimen_status']/a/span[2]/b")));
		 statusElem.click();
		 TimeUnit.SECONDS.sleep(1);
		 List <WebElement> statusList = getWebDriver().findElements(By.xpath(".//*[@id='select2-drop']/ul/li"));
		 for (int i = 1; i <= statusList.size(); i++) {
			 WebElement status = getWebDriver().findElement(By.xpath(".//*[@id='select2-drop']/ul/li[" + i + "]/div"));
			 if(status.getText().equals(currentStatus)){
				 status.click();
				 TimeUnit.SECONDS.sleep(1);
				 break;
			 }
		}
		 
		TimeUnit.SECONDS.sleep(1);
		save();
	}

	@Override
	public String showCollection(String collectionName) {
		
		return showRodentSpecimens();
	}

	public int openSpecimenAndCheckTissue(String specimen) throws InterruptedException {
		
		searchAndOpenItem(specimen);
		
		selectTissuesAndSamplesTab();
		
		List <WebElement> stocksList = getWebDriver().findElements(By.xpath(".//*[@class='stocks_table']/tr"));
		return stocksList.size();
	}

	public String findTissueAndClick(String specimen) throws InterruptedException {
		
		searchAndOpenItem(specimen);
		
		selectTissuesAndSamplesTab();
		WebElement tissueLink = getWebDriver().findElement(By.xpath(".//*[@id='ui-id-1']/h3[1]/a"));
		tissueLink.click();
		TimeUnit.SECONDS.sleep(3);
		
		 WebElement pageTitle =driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("page-title")));
		 return pageTitle.getText();
	}

}
