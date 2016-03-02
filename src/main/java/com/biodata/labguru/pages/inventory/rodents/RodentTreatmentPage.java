package com.biodata.labguru.pages.inventory.rodents;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.AdminPage;


public class RodentTreatmentPage extends AdminPage{

	private By tabTodayTreatmentsLocator = By.id("present_treatments_tab");
	private By tabFutureTreatmentsLocator = By.id("future_treatments_tab");
	private By tabHistoryTreatmentsLocator = By.id("past_treatments_tab");
	
	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	private void selectTodayTreatmentsTab() throws InterruptedException{
		selectTabByLocator(tabTodayTreatmentsLocator);
	}
	
	private void selectFutureTreatmentsTab() throws InterruptedException{
		selectTabByLocator(tabFutureTreatmentsLocator);
	}
	
	private void selectHistoryTreatmentsTab() throws InterruptedException{
		selectTabByLocator(tabHistoryTreatmentsLocator);
	}
	

	public String addNewTreatmentWithGivenName(String name,String date) throws InterruptedException {

		Map<String,List<String>> specimens = createNewTreatment(name,date);
		
		if(date.equals(LGConstants.TOMORROW) || date.equals(LGConstants.TODAY)){
			 return applyAndCheckCreation(name, specimens,date);
		}else   
			return applyAndCheckCreationForHistoryTreatment(name, specimens);
	}
	
	public void addNewTreatmentNoValidation(String name,String date) throws InterruptedException {

		createNewTreatment(name,date);
		
		applyTreatment();
	}

	private String applyAndCheckCreation(String name, Map<String,List<String>> map,String date) throws InterruptedException {
		
		applyTreatment();
		
		//wait until page loaded
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("index-header")));
		String tabId = selectCorrectTabAndGetId(date);
        
        String cage = map.keySet().iterator().next();
        List<String> specimens = map.get(cage);
        //go over all rows and check that the right name and specimens are written
        List<WebElement> rows = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
        		(By.xpath(".//*[@id='" + tabId + "']/table/tbody/tr")));
        for (int i=2; i<= rows.size() ; i++) {
        	try{
        		List<String> names = new ArrayList<String>();
	        	WebElement nameEle = getWebDriver().findElement(By.xpath(".//*[@id='" + tabId + "']/table/tbody/tr[" + i + "]/td[2]/p"));	
	        	if(nameEle.getText().equals(name) || name.startsWith(nameEle.getText())){
	        		WebElement cageElem;
	        		if(cage.equals("not in cage")){
	        			cageElem = getWebDriver().findElement(By.xpath(".//*[@id='" + tabId + "']/table/tbody/tr[" + i + "]/td[5]"));
	        		}else{
	        			cageElem = getWebDriver().findElement(By.xpath(".//*[@id='" + tabId + "']/table/tbody/tr[" + i + "]/td[5]/a"));
	        		}
	        		List<WebElement> specList = getWebDriver().findElements(By.xpath(".//*[@id='" + tabId + "']/table/tbody/tr[" + i + "]/td[4]/a"));
	        		 
	        		for (WebElement specElem : specList) {
	        			names.add(specElem.getText());
					}
	        		if(specimens.equals(names) && ((cageElem.getText().equals(cage) || (cageElem.getText().isEmpty() && cage.equals("not in cage")))))
	        			return name;
	        		
	        	}
        	}catch(NoSuchElementException e){
        		//do nothing - sometimes there is an empty row becauese a treatment holds more then 1 cage
        	}
        	
		}
        
        return "Treatment not found";
	}
	
	private String applyAndCheckCreationForHistoryTreatment(String name, Map<String,List<String>> map) throws InterruptedException {
		
		applyTreatment();
		
		//wait until page loaded
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("index-header")));
		selectHistoryTreatmentsTab();
        
        String cage = map.keySet().iterator().next();
        List<String> specimens = map.get(cage);
        //go over all rows and check that the right name and specimens are written
        List<WebElement> rows = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
        		(By.xpath(".//*[@id='past_treatments']/table/tbody/tr")));
        for (int i=2; i<= rows.size() ; i++) {
        	WebElement nameEle = getWebDriver().findElement(By.xpath(".//*[@id='past_treatments']/table/tbody/tr[" + i + "]/td[3]"));
        	if(nameEle.getText().equals(name)){
        		WebElement cageElem = getWebDriver().findElement(By.xpath(".//*[@id='past_treatments']/table/tbody/tr[" + i + "]/td[7]/a"));
        		List<WebElement> specList = getWebDriver().findElements(By.xpath(".//*[@id='past_treatments']/table/tbody/tr[" + i + "]/td[6]/a"));
        		 List<String> names = new ArrayList<String>();
        		for (WebElement specElem : specList) {
        			names.add(specElem.getText());
				}
        		if(specimens.equals(names) && cageElem.getText().equals(cage))
        			return name;
        	}
        	
		}
        
        return "The new treatment is not as expected.";
	}

	private String selectCorrectTabAndGetId(String date) throws InterruptedException {
		
		if(date.equals(LGConstants.TOMORROW)){
			selectFutureTreatmentsTab();
			return "future_treatments";
		}else if(date.equals(LGConstants.TODAY)){
			selectTodayTreatmentsTab();
			return "present_treatments";
		}
		
		selectHistoryTreatmentsTab();
		return "past_treatments";
	}

	private void applyTreatment() throws InterruptedException {
		
		WebElement btnApply = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("apply")));
        btnApply.click();
        TimeUnit.SECONDS.sleep(2);
	}
	
	
	public String createTreatmentNoSpecimenFailed(String name) throws InterruptedException{
		
		createNewTreatment(name,LGConstants.TODAY);
		
		uncheckSelectedSpecimens();
		
		applyTreatment() ;
		
		return checkForNotyMessage(By.cssSelector(".noty_text"));
	}
	
	private void uncheckSelectedSpecimens() {
		
		List<WebElement> chekList = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
	    		(By.xpath(".//*[@class='reservation_item']/input")));
		
		for (WebElement checkbox : chekList) {
			if(checkbox.isSelected())
				checkbox.click();
		}
		
	}

	public String addNewTreatmentWithProtocol(String protocol) throws InterruptedException {
		
		Map<String,List<String>> specimens = createNewTreatment("",LGConstants.TODAY);
		
		manageProtocols(protocol);
		
		TimeUnit.SECONDS.sleep(2);
		
		selectProtocolFromDropDown(protocol);
		TimeUnit.SECONDS.sleep(2);
		String name = selectStepFromDropDown();

		return applyAndCheckCreation(name, specimens,LGConstants.TODAY);
	}
	
	private String selectStepFromDropDown() throws InterruptedException {
		
		//click on the arrow of steps drop down
		WebElement dropSteps = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='s2id_pick_step']/a/span[2]/b")));
		dropSteps.click();
		TimeUnit.SECONDS.sleep(2);
		//take the last step in the list
		WebElement selected = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='select2-drop']/ul/li[last()]/div")));
		String selectedName = selected.getText();
		selected.click();
		TimeUnit.SECONDS.sleep(1);
		return selectedName;
		
	}

	public boolean filterTreatments() throws InterruptedException{
		
		  WebElement dropFilter = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				  (By.xpath(".//*[@id='s2id_filter_by']/a/span[2]/b")));
		  dropFilter.click();
		  TimeUnit.SECONDS.sleep(1);
		  String user = getWebDriver().findElement(By.id("account_dropdown")).getText();
		  List<WebElement> users = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
			    		(By.xpath(".//*[@id='select2-drop']/ul/li")));
		  //select the current user
		  for (int i=1; i<= users.size() ; i++) {
			WebElement nameEle = getWebDriver().findElement(By.xpath(".//*[@id='select2-drop']/ul/li[" + i + "]/div"));
			if(nameEle.getText().equals(user)){
				nameEle.click();
				TimeUnit.SECONDS.sleep(1);
				break;
			}	
		  }
		  
		  //check that the filter is working as should be and we cant see other users in the table after filtering.
		  List<WebElement> rows = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
		    		(By.xpath(".//*[@id='select2-drop']/ul/li")));
		  for (int j=2; j<= rows.size() ; j++) {
			WebElement administrate = getWebDriver().findElement(By.xpath(".//*[@id='data']/tbody/tr["+ j +"]/td[3]"));
			if(!administrate.getText().equals(user)){
				return false;
			}	
		  }
		 return true; 
		
	}

	private Map<String,List<String>> createNewTreatment(String name,String date) throws InterruptedException {
		
		WebElement btnNew = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new_treatments")));
		btnNew.click();
		TimeUnit.SECONDS.sleep(2);
		
        String cage = insertTreatmentDetails(name, date);

        TimeUnit.SECONDS.sleep(2);
        
        Map<String,List<String>> map = new HashMap<String,List<String>>();
        List<String> specNames = new ArrayList<String>();
        
        //take all specimes that were added
        List<WebElement> specimens = getWebDriver().findElements(By.xpath(".//*[@class='reservation_item']"));
        for (WebElement elem : specimens) {
    	   specNames.add(elem.getText());
        }
        map.put(cage, specNames);
        return map;
	}


	private String insertTreatmentDetails(String name, String date) throws InterruptedException {
	     
		//write in the redactor editor
		if(!name.isEmpty())
			writeInRedactor("name", name);   
 
        WebElement txtScheduledAt = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("scheduled_at")));
        txtScheduledAt.click();
        TimeUnit.SECONDS.sleep(1);
        
        if(date.equals(LGConstants.TODAY))
        	selectToday(txtScheduledAt);
        else if(date.equals(LGConstants.TOMORROW)){
        	selectTomorrow(txtScheduledAt);
        }else
        	selectPast(txtScheduledAt);
        
        getWebDriver().findElement(By.xpath(".//*[@id='new_treatment']/div/h3")).click();
       
        //we must add speciman to the selected cage
        WebElement dropApplyTo = driverWait.until(ExpectedConditions.visibilityOfElementLocated
        		(By.xpath(".//*[@id='s2id_reserved_from_']/a/span[2]/b")));
        dropApplyTo.click();
        TimeUnit.SECONDS.sleep(1);
        
        List<WebElement> cages = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
        		(By.xpath(".//*[@id='select2-drop']/ul/li")));
        for (int i =1; i<= cages.size() ;i++) {
        	WebElement cageElem = getWebDriver().findElement(By.xpath(".//*[@id='select2-drop']/ul/li["+i+"]/div"));
             String cage = cageElem.getText();
             cageElem.click();
             TimeUnit.SECONDS.sleep(2);
             List<WebElement> specimens = getWebDriver().findElements(By.xpath(".//*[@class='reservation_item']"));
             if(specimens.size() > 0){
            	 return cage;
             }else{
            	 dropApplyTo = getWebDriver().findElement(By.xpath(".//*[@id='s2id_reserved_from_']/a/span[2]/b"));
            	 dropApplyTo.click(); 
            	 TimeUnit.SECONDS.sleep(1);
             }
		}
        
		return "";
	}

	private void manageProtocols(String protocolToSelect) throws InterruptedException{
		
		WebElement btnManageProt = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toggle_protocols")));
		btnManageProt.click();
		TimeUnit.SECONDS.sleep(2);
		try {
			List<WebElement> protocols = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
					(By.xpath(".//*[@id='all_protocols']/li")));
			
			//select the first protocol and add it to the list of protocols to select from
			for (WebElement protocolElm : protocols) {
				if(protocolElm.getText().equals(protocolToSelect)){
					Actions action = new Actions(getWebDriver());
					action.doubleClick(protocolElm);
					action.perform();
					TimeUnit.SECONDS.sleep(2);
					break;
				}
			}
		} catch (Exception e) {
			//do nothing - no protocols in the list
		}
		//click 'Done'
		btnManageProt.click();
		TimeUnit.SECONDS.sleep(1);
	}
	
	private void selectProtocolFromDropDown(String selectedProtocol) throws InterruptedException {
		
		WebElement dropProtocols = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='s2id_pick_protocol']/a/span[2]/b")));
		dropProtocols.click();
		TimeUnit.SECONDS.sleep(1);
		
		WebElement inputText = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='select2-drop']/div/input")));
		sendKeys(inputText, selectedProtocol);
		inputText.sendKeys(Keys.ENTER);
		TimeUnit.SECONDS.sleep(1);
		//take the last protocol in the list
//		List<WebElement> list = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@id='select2-drop']/ul/li")));
//		for (int i = 1; i <= list.size(); i++) {
//			WebElement selected = driverWait.until(ExpectedConditions.visibilityOfElementLocated
//					(By.xpath(".//*[@id='select2-drop']/ul/li[" + i + "]/div")));
//			if(selected.getText().equals(selectedProtocol)){
//				selected.click();
//				TimeUnit.SECONDS.sleep(1);
//			}
//		}
	}
	
	private void selectTabByLocator(By locator) throws InterruptedException{
		WebElement tab = driverWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		tab.click();
		TimeUnit.SECONDS.sleep(2);
	}

	public boolean validatePage(String name, String date) throws InterruptedException {
		
		if(date.equals(LGConstants.TODAY))	
			 return validateTodayTreatment(name);
		
		else if(date.equals(LGConstants.TOMORROW))	
			 return validateTomorrowTreatment(name);
		else
			 return validatePastTreatment(name);

	}

	private boolean validatePastTreatment(String name) throws InterruptedException {
		
		String tabId = selectCorrectTabAndGetId("");
		
	       List<WebElement> rows = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
	        		(By.xpath(".//*[@id='"+tabId+"']/table/tbody/tr")));
	        for (int i=2; i<= rows.size() ; i++) {
	        	WebElement nameEle = getWebDriver().findElement(By.xpath(".//*[@id='"+tabId+"']/table/tbody/tr[" + i + "]/td[3]/p"));
	        	if(nameEle.getText().equals(name)){
	        		WebElement chkDone = getWebDriver().findElement(By.xpath(".//*[@id='"+tabId+"']/table/tbody/tr[" + i + "]/td[2]/input"));
	        		if(!chkDone.isSelected()){
	        			//if row 'done' not selected - the background is red
	        			WebElement row = getWebDriver().findElement(By.xpath(".//*[@id='"+tabId+"']/table/tbody/tr[" + i + "]"));
	        			if(row.getAttribute("class").equals(LGConstants.TREATMENT_RED_BACKGROUND)){
	        				chkDone.click(); //select the 'Done' checkbox
	        				TimeUnit.SECONDS.sleep(1);
	        				row = getWebDriver().findElement(By.xpath(".//*[@id='"+tabId+"']/table/tbody/tr[" + i + "]"));
	        				if(row.getAttribute("class").equals(LGConstants.TREATMENT_WHITE_BACKGROUND) && !chkDone.isEnabled())
		        				return true;
	        			}else 
	        				return false;
	        		}else{
	        			//if row 'done' selected - the background is white
	        			WebElement row = getWebDriver().findElement(By.xpath(".//*[@id='"+tabId+"']/table/tbody/tr[" + i + "]"));
	        			if(row.getAttribute("class").equals(LGConstants.TREATMENT_WHITE_BACKGROUND) && !chkDone.isEnabled())
	        				return true;
	        			else return false;
	        		}
	        	}
	        	
			}
		return false;
	}

	private boolean validateTomorrowTreatment(String name) throws InterruptedException {
		
		String tabId = selectCorrectTabAndGetId(LGConstants.TOMORROW);
		
	       List<WebElement> rows = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
	        		(By.xpath(".//*[@id='"+tabId+"']/table/tbody/tr")));
	        for (int i=2; i<= rows.size() ; i++) {
	        	WebElement nameEle = getWebDriver().findElement(By.xpath(".//*[@id='"+tabId+"']/table/tbody/tr[" + i + "]/td[2]/p"));
	        	if(nameEle.getText().equals(name)){
	        		WebElement lblDate = getWebDriver().findElement(By.xpath(".//*[@id='"+tabId+"']/table/tbody/tr[" + i + "]/td[1]"));
        			Date currentDate = Calendar.getInstance().getTime();
        			String time = lblDate.getText();
        			Calendar d = Calendar.getInstance();
        			int year = Integer.valueOf(time.substring(0, time.indexOf('-'))).intValue();
        			int month = Integer.valueOf(time.substring(time.indexOf('-')+1,time.lastIndexOf('-'))).intValue() -1; //in calander january is equal to 0 
        			int day = Integer.valueOf(time.substring(time.lastIndexOf('-') +1)).intValue();
        			d.set(year, month, day);
	        		if(currentDate.before(d.getTime()))
        				return true;
        			else
        				return false;
        			
	        		
	        	}
	        	
			}
		return false;
	}

	private boolean validateTodayTreatment(String name) throws InterruptedException {
		
		String tabId = selectCorrectTabAndGetId(LGConstants.TODAY);
		
	       List<WebElement> rows = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
	        		(By.xpath(".//*[@id='"+tabId+"']/table/tbody/tr")));
	        for (int i=2; i<= rows.size() ; i++) {
	        	WebElement nameEle = getWebDriver().findElement(By.xpath(".//*[@id='"+tabId+"']/table/tbody/tr[" + i + "]/td[2]/p"));
	        	if(nameEle.getText().equals(name)){
	        		WebElement chkDone = getWebDriver().findElement(By.xpath(".//*[@id='"+tabId+"']/table/tbody/tr[" + i + "]/td[1]/input"));
	        		if(!chkDone.isSelected()){
	        			chkDone.click();
	        			TimeUnit.SECONDS.sleep(1);
	        			WebElement row = getWebDriver().findElement(By.xpath(".//*[@id='"+tabId+"']/table/tbody/tr[" + i + "]"));
	        			if(row.getAttribute("class").equals(LGConstants.TREATMENT_GREEN_BACKGROUND))
	        				return true;
	        			else 
	        				return false;
	        		}
	        	}
	        	
			}
		return false;
	}





}
