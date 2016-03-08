package com.biodata.labguru.tests.knowledgebase;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.tests.BaseTest;
import com.biodata.labguru.tests.TestOrderRandomizer;


@Listeners(TestOrderRandomizer.class)
public class ImageBankTest extends BaseTest{


	@Test(groups = {"deep"})
	public void showMenu(){

		try {
			String pageTitle = getPageManager().getAdminPage().showImageBank();
			AssertJUnit.assertEquals(getMessageSource().getMessage("imagebank.title.has.items", null, Locale.US), pageTitle);
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
		
	}
	/**
	 * This is one method test that tests all options on image show page:
	 * edit,delete,add tag,add linked resource,search image in list.
	 */
	@Test(groups = {"deep"})
	public void testImageShowPage() {
		
		try {
			//upload image in project and open its show page
			//addImage();
			getPageManager().getWebDriver().get("https://staging.labguru.com/system/attachments/541");
			//edit image
			logger.debug("edit image");
			Assert.assertTrue(getPageManager().getImagePage().editImageFromShowPage(),"editing image failed");
			//add tag
			logger.debug("add Tag");
			//addTag();
			logger.debug("go back to show page");
			//getPageManager().getAdminPage().goBack();
			//add linked resource
			logger.debug("add Linked Resource");
			//addLinkedResource();
			logger.debug("go back to show page");
			//getPageManager().getAdminPage().goBack();
			//delete image
			logger.debug("delete image");
			//method should return false as there is no item with this name after delete
			Assert.assertFalse(getPageManager().getImagePage().deleteImageFromShowPage(),"Delete image failed");
		
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	private void addTag() throws InterruptedException {
		String tagName = buildUniqueName(LGConstants.TAG_PREFIX);
		String tag = getPageManager().getAdminPage().addTag(tagName);
		
		AssertJUnit.assertEquals("Tag with name '" + tagName + "' was not craeted as should be.",tagName, tag);
	}

	private void addLinkedResource() throws InterruptedException{
		
		//add linked resource
		String linkedRes = getPageManager().getAdminPage().addLinkedResource(LGConstants.EXPERIMENT);
		
		AssertJUnit.assertTrue("No resource was linked.",!linkedRes.equals(""));

	}

	private String addImage() throws InterruptedException {
		
		//go to projects and create new project to upload the image to it
		getPageManager().getAdminPage().selectProjects();
		TimeUnit.SECONDS.sleep(2);
		String resource = getPageManager().getProjectPage().addNewProjectUseDefault();
		//upload image and open its show page
		getPageManager().getAdminPage().uploadImage(resource);
		return LGConstants.UPLOAD_IMAGE_TEST_FILENAME;
	}

}
