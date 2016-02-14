package com.biodata.labguru.pages.inventory.purchasables;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;

public class WormPage extends PurchasableCollectionPage{

	
	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	@Override
	protected String getImportXPath() {
		
		return ".//*[@id='main-content']/div/div[1]/a[@href='/system/imports/new?class=Biocollections%3A%3AWorm']";
	}

	@Override
	protected String getFileNameToImport() {
		return LGConstants.WORMS_TEMPLATE;
	}
	
	@Override
	protected String getCollectionName() {
		
		return LGConstants.WORM_PREFIX;
	}
	
	@Override
	public List<String> getAvailableColumnsForCustomiseTableView() {
		List<String> columns = super.getAvailableColumnsForCustomiseTableView();
		columns.add("preferences_auto_name");//sysid
		columns.add("preferences_name");//name
		columns.add("preferences_organism_id");
		columns.add("preferences_mutagen");
		columns.add("preferences_growth_conditions");//bind position
		columns.add("preferences_genotype");
		columns.add("preferences_phenotype");
		columns.add("preferences_made_by");
		columns.add("preferences_gene_id");//application
		columns.add("preferences_outcrossed");
		columns.add("preferences_dauer_defective");
		return columns;
	}

	@Override
	protected void addItemWithGivenName(String name) {
		clickNewButton("new_worm");
	      
        WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated
        		(By.id("name")));
        txtName.sendKeys(name);
	
	}

	@Override
	protected String getEditCollectionPrefix() {
		return "edit_biocollections_worm_";
	}

	public boolean searchInCGC() throws Exception {
		
		showDirecrory("search_cgc");
		String wormName = addItemFromCGC();
		
		WebElement wormsBreadCrumbsLink = getWebDriver().findElement(By.xpath(".//*[@id='breadcrumbs']/ul/li[2]/span/a"));
		wormsBreadCrumbsLink.click();
		TimeUnit.SECONDS.sleep(4);
		searchAndOpenItem(wormName);
		String pagetitle = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("page-title"))).getText();
		return wormName.equals(pagetitle);
	}

	private String addItemFromCGC() throws InterruptedException {
		
		String addedWorm = "";
		List<WebElement> rows = getWebDriver().findElements(By.xpath(".//*[@id='table_data']/tbody/tr"));
		for (int i = 3; i <= rows.size(); i++) {
			 //check if can be added with plus img
			 try{
				 WebElement plusImg = getWebDriver().findElement(By.xpath(".//*[@id='table_data']/tbody/tr[" + i + "]/td[1]/span/a/i"));
				 WebElement name = getWebDriver().findElement(By.xpath(".//*[@id='table_data']/tbody/tr[" + i + "]/td[@class='name']"));
				 addedWorm = name.getText();
				 plusImg.click();
				 TimeUnit.SECONDS.sleep(1);
				 break;
			 }catch(NoSuchElementException e){
				 //continue to next row
				 continue;
			 }
		}
		return addedWorm;
	}

}
