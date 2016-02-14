package com.biodata.labguru.tests.home;

import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public class CalendarTest extends AbstractHomeTest{

	@Override
	public void showMenu(){

		String pageTitle = getPageManager().getAdminPage().showCalendar();
				
        // Check the title of the page
		AssertJUnit.assertEquals(getMessageSource().getMessage("calendar.title",null,Locale.US), pageTitle);

	}
	
	
	@Test(groups = {"deep"})
	public void addNewEvent(){
		
		try {
			getPageManager().getAdminPage().showCalendar();
			String eventName = buildUniqueName(LGConstants.EVENT_PREFIX);
			Assert.assertTrue(getPageManager().getCalendarPage().addNewEvent(eventName),"No event with name '" + eventName + "' was found.");
		
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test(groups = {"deep"})
	public void addNewEventFromCalenderBoard(){
		
		try {
			getPageManager().getAdminPage().showCalendar();
			String eventName = buildUniqueName(LGConstants.EVENT_PREFIX);
			Assert.assertTrue(getPageManager().getCalendarPage().addNewEventFromCalenderBoard(eventName),"No event with name '" + eventName + "' was found.");
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	
	@Test(groups = {"deep"})
	public void addNewExperimentFromCalenderBoard(){
		
		try {
			getPageManager().getAdminPage().showCalendar();
			String expName =buildUniqueName(LGConstants.EXPERIMENT_PREFIX);
			String pageTitle = getPageManager().getCalendarPage().addNewExperimentFromCalenderBoard(expName);
			Assert.assertEquals(expName, pageTitle);
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
}
