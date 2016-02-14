package com.biodata.labguru.tests.home;

import static org.testng.AssertJUnit.assertTrue;

import java.util.Locale;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public class LabActivitiesTest extends AbstractHomeTest{

	@Override
	public void showMenu(){

		String pageTitle = getPageManager().getAdminPage().showLabActivities();
				
        // Check the title of the page
		AssertJUnit.assertEquals(getMessageSource().getMessage("lab.activities.title",null,Locale.US), pageTitle);

	}

	
	@Test (groups = {"deep"})
	public void addWhiteBoardPost(){
		
		 try {
			getPageManager().getAdminPage().showLabActivities();
			 
			 String subject = buildUniqueName(LGConstants.MESSAGE_PREFIX);
			
			 String postedSubject = getPageManager().getLabActivitiesPage().addWhiteBoardPost(subject);
			 
			 Assert.assertEquals( postedSubject, subject,"post '"  + subject + "' on white board is not as should be.");
		} catch (Exception e) {
			setLog(e,"addWhiteBoardPost");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void goToSpecificPost(){
		
		 try {
			getPageManager().getAdminPage().showLabActivities();
			 
			 String post = buildUniqueName(LGConstants.MESSAGE_PREFIX);
			 
			 getPageManager().getLabActivitiesPage().addWhiteBoardPost(post);
			
			 String postedSubject = getPageManager().getLabActivitiesPage().goToSpecificPost(post);
			 
			 Assert.assertEquals( postedSubject, post,"post '"+ post + "' on white board not found");
		} catch (Exception e) {
			setLog(e,"goToSpecificPost");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void deleteWhiteBoardPost(){
		
		 try {
			getPageManager().getAdminPage().showLabActivities();
			 
			 String subject = buildUniqueName(LGConstants.MESSAGE_PREFIX);
			
			 getPageManager().getLabActivitiesPage().addWhiteBoardPost(subject);

			 assertTrue("post was not deleted as expected",getPageManager().getLabActivitiesPage().deleteWhiteBoardPost(subject));
		
		 } catch (Exception e) {
			setLog(e,"deleteWhiteBoardPost");
			Assert.fail(e.getMessage());
		}
	}

}
