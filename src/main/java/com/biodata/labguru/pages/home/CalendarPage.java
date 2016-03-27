package com.biodata.labguru.pages.home;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.AdminPage;


public class CalendarPage extends AdminPage {

	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	public boolean addNewEvent(String name) throws InterruptedException{
		
		clickOnButton("new_event");
		 
		return addAndFindEvent(name,true);//need to select start date
	}


	
	public boolean addNewEventFromCalenderBoard(String name) throws InterruptedException{

		findTodayInCalendar();
		    	 		
		TimeUnit.SECONDS.sleep(2);
		//create event
		WebElement linkCreateEvent = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("create_event_from_tooltip")));
		linkCreateEvent.click();
		 
		return addAndFindEvent(name,false);
	}
	
	private boolean addAndFindEvent(String name,boolean needStartDate) throws InterruptedException {
		
		TimeUnit.SECONDS.sleep(2);
		 	
		String date = openNewEventDialog(name,needStartDate);//May 18, 2015 09:00
		String time = date.substring(date.lastIndexOf(" ")+1);

		openMoreList();
		
		List<WebElement> events = getWebDriver().findElements(By.xpath(".//*[@class='fc-event-container']/a/div"));
		for (WebElement todayEventInCal : events) {
			WebElement titleElm = todayEventInCal.findElement(By.xpath("span[2]"));
			String eventName = titleElm.getText();
			
			if(eventName.equals(name)){
				WebElement timeOfEvent = todayEventInCal.findElement(By.xpath("span[1]"));
				String eventCreated = timeOfEvent.getText();
				return eventCreated.equals(time);
			}
				
		}
		return false;
	}

	/**
	 * go over the more buttons in calendar and click on them
	 * @param today
	 */
	private void openMoreList() {
		
			try {
				List<WebElement> btnList = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
						(By.cssSelector(".fc-more")));
				for (WebElement btnMore : btnList) {
					btnMore.click();
				}
				
			} catch (Exception e) {
				//do nothing
			}
	}

	public String addNewExperimentFromCalenderBoard(String expName) throws InterruptedException{
		
		TimeUnit.SECONDS.sleep(1);
		
		findTodayInCalendar();
		 
		TimeUnit.SECONDS.sleep(2);
		  
		//create experiment
		WebElement linkCreateExp = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.id("create_experiment_from_tooltip")));
		linkCreateExp.click();

	    TimeUnit.SECONDS.sleep(2);
	    openNewExperimentDialog(expName);
	     
		return getWebDriver().getTitle();
	}
	
	
	private void findTodayInCalendar() throws InterruptedException {
		
		int length = LGConstants.Day.values().length;
		for (int i = 1; i <= length; i++) {
			 try{
				 String day = LGConstants.Day.values()[i-1].getShortDay(i); 	 
				 WebElement todayCellInCal = getWebDriver().findElement(By.cssSelector(".fc-day-number.fc-"+ day +".fc-today.fc-state-highlight"));				 	
				 todayCellInCal.click();			
				 break;
			 }catch(NoSuchElementException e){
				 //do nothing - keep searching for today's date
				 continue;
			 }
		}
	}
	
	
	private String openNewEventDialog(String eventName,boolean needStartDate) throws InterruptedException {
		
        WebElement newDialog = getWebDriver().switchTo().activeElement();
        driverWait.until(ExpectedConditions.visibilityOf(newDialog));
	     
	     WebElement txtTitle = getWebDriver().findElement(By.id("name"));
	     txtTitle.sendKeys(eventName);
	     WebElement txtStartDate = getWebDriver().findElement(By.id("start_date_datepicker_event"));
	     
	     if(needStartDate){
		     txtStartDate.click();
		     TimeUnit.SECONDS.sleep(1);
		     selectToday(txtStartDate);
		    
	     }
	     
	     TimeUnit.SECONDS.sleep(1);
	     
	     String date = txtStartDate.getAttribute("value");
	     
	     WebElement btnSave = getWebDriver().findElement(By.id("Save"));
	     btnSave.click();
	     
	     TimeUnit.SECONDS.sleep(1);
         getWebDriver().switchTo().activeElement();
		
		 TimeUnit.SECONDS.sleep(2);

	     return date;
	}
}
