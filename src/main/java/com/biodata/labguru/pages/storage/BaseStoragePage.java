package com.biodata.labguru.pages.storage;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.pages.AdminPage;

public class BaseStoragePage extends AdminPage{

	
	public String openDeleteItemPopup(boolean delete) throws InterruptedException {
		
		openDeleteArchivePopup(delete);
		
		String msg = checkForNotyMessage();
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
	
	
	@Override
	public String deleteFromShowPage() throws InterruptedException {
		String msg = super.deleteFromShowPage();
		try{
			//look if it has a second warning(when there are stocks in the box) for delete action - if has-click ok
			WebElement ok = getWebDriver().findElement(By.xpath(".//*[@id='main-content']/div/fieldset/form/div/li/input"));
			ok.click();
			TimeUnit.SECONDS.sleep(2);
			msg =  checkForNotyMessage();
		}catch(NoSuchElementException e){
			//if no second message (no stocks were in the box) - continue
		}
		return msg;
	}

}
