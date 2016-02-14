package com.biodata.labguru.pages.inventory.purchasables.botany;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.biodata.labguru.model.BotanyItem;
import com.biodata.labguru.model.ICollectionItem;
import com.biodata.labguru.pages.inventory.purchasables.PurchasableCollectionPage;


public abstract class BotanyPage extends PurchasableCollectionPage{

	public void addExtraFields(){
		
		WebElement txtGenotype = getWebDriver().findElement
				(By.id("genotype"));
		txtGenotype.sendKeys("pp");
		
		WebElement txtPhenotype = getWebDriver().findElement
				(By.id("phenotype"));
		txtPhenotype.sendKeys("purple");
		
		WebElement txtGeneration = getWebDriver().findElement
				(By.id("generation"));
		
		txtGeneration.sendKeys("F1");
	}
	
	@Override
	public List<String> getAvailableColumnsForCustomiseTableView() {
		List<String> columns = super.getAvailableColumnsForCustomiseTableView();
		columns.add("preferences_auto_name");//sysid
		columns.add("preferences_name");//name
		columns.add("preferences_genotype");
		columns.add("preferences_phenotype");
		columns.add("preferences_generation");
		
		//botany not hold these columns so we remove it
		columns.remove("preferences_alternative_name");
		return columns;
	}
	
	
	@Override
	protected void loadCollectionData(ICollectionItem item) {
		
		BotanyItem botanyItem = new BotanyItem();
		super.loadCollectionData(botanyItem);

		WebElement txtGenotype = getWebDriver().findElement
				(By.id("lg_info_tab_genotype"));
		botanyItem.setGenotype(txtGenotype.getText());
		
		WebElement txtPhenotype = getWebDriver().findElement
				(By.id("lg_info_tab_phenotype"));
		botanyItem.setPhenotype(txtPhenotype.getText());
		
		WebElement txtGeneration = getWebDriver().findElement
				(By.id("lg_info_tab_generation"));
		botanyItem.setGeneration(txtGeneration.getText());
	}
	

}
