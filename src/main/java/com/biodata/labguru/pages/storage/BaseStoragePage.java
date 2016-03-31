package com.biodata.labguru.pages.storage;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.biodata.labguru.pages.AdminPage;

public class BaseStoragePage extends AdminPage{
	
	
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
