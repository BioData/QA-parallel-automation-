package com.biodata.labguru.pages.home;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.biodata.labguru.pages.AdminPage;


public class RecentResultsPage extends AdminPage {

	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	public boolean isReactionEditable() throws InterruptedException {
	
		TimeUnit.SECONDS.sleep(2);
		
		WebElement table = getWebDriver().findElement(By.tagName("stoichiometric-table"));
		String id = table.getAttribute("element-id");
		String pathToEditBtn = ".//*[@id='reaction_element_"+ id +"']/stoichiometric-table/table/tbody/tr[1]/td[6]/div/i";
				
		WebElement btnEdit = getWebDriver().findElement(By.xpath(pathToEditBtn));
		return btnEdit.isDisplayed();

	}

}
