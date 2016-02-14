package com.biodata.labguru.pages.storage;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.pages.AdminPage;

public class BaseStoragePage extends AdminPage{

	
	public String openDeleteItemPopup(boolean delete) throws InterruptedException {
		
		openDeleteArchivePopup(delete);
		
		String msg = waitForNotyMessage(".noty_text");		
		return msg;

	}

	public void openDeleteItemPopupFromIndexTable(boolean delete) throws InterruptedException {
		
		WebElement popupDialog = getWebDriver().switchTo().activeElement();
        driverWait.until(ExpectedConditions.visibilityOf(popupDialog));
        
        getWebDriver().switchTo().activeElement();
        
        TimeUnit.SECONDS.sleep(2);
        
        WebElement chkBox;
		if(delete){
			//select the delete checkbox
			chkBox = getWebDriver().findElement(By.id("archive_or_delete_delete"));
		}else{
			//select the archive checkbox
			chkBox = getWebDriver().findElement(By.id("archive_or_delete_archive"));
		}
		
		if(!chkBox.isSelected())
			chkBox.click();

		WebElement btnOk =  getWebDriver().findElement(By.xpath(".//*[@value='OK']"));
		btnOk.click();
	}
	

}
