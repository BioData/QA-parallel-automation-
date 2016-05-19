package com.biodata.labguru.pages.knowledgebase;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;


public class RecipePage extends AbstractKnowledgebasePage{

	
	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	public boolean hasList() {
		
		try{
			TimeUnit.SECONDS.sleep(2);
			List<WebElement> list = getWebDriver().findElements(By.xpath(".//*[@id='recipes_list']/div"));
			return list.size() > 2;
		}catch(Exception e){
			return false;
		}
		
	}

	public String addRecipeFromDirectory() throws InterruptedException {
		
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#add_from_labguru_recipes")));
		
		clickOnButton("add_from_labguru_recipes");
		
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".logo")));
		
		List<WebElement> list = getWebDriver().findElements(By.cssSelector(".recipes>li>a"));
		for (WebElement link : list) {
			link.click();
			TimeUnit.SECONDS.sleep(2);
			break;
		}
	
		WebElement btnCopyToLabguru = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("upload")));
		btnCopyToLabguru.click();
		TimeUnit.SECONDS.sleep(2);
		switchToNewTab();
		
		WebElement recipeElm = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='knowledgebase_recipe_name_input']/span")));
		String recipeName = recipeElm.getText();
		
		return recipeName;

	}

	
	public void addNewRecipe(String recipeName) throws InterruptedException {
		
		clickOnButton("new_recipe");
		TimeUnit.SECONDS.sleep(3);
		
	   	executeJavascript("document.getElementsByClassName('edit_me')[0].click();");
	   	TimeUnit.SECONDS.sleep(2);
		
		WebElement txtRecipeName = getWebDriver().findElement(By.id("knowledgebase_recipe_name"));
		sendKeys(txtRecipeName, recipeName);
		
		
		executeJavascript("document.getElementsByClassName('inline_submit')[0].click();");
        TimeUnit.SECONDS.sleep(2);
	}



	private WebElement findRecipeByName(String recipeName) {
		
		List<WebElement> elements = getWebDriver().findElements(By.xpath(".//*[@id='recipes_list']/div"));

		for (int i = 2; i <= elements.size()-1; i++) {
			WebElement link = getWebDriver().findElement(By.xpath(".//*[@id='recipes_list']/div["+ i  + "]/h4/a/b"));
			if(link.getText().startsWith(recipeName))
				return link;
		}
		return null;
	}

	public void deleteRecipe(String recipeName) throws InterruptedException {
		
	   	 WebElement link = findRecipeByName(recipeName); 
	   	 if(link != null){
		   	 link.click();
		   	 TimeUnit.SECONDS.sleep(3);
		   	 deleteKnowledgebaseItem();
	   	 }
	
	}
	
	@Override
	public boolean searchForItemInList(String itemToSerch) throws InterruptedException {
		
		invokeSearchItem(itemToSerch);
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("index-header")));
		try {
			//no results after search
			getWebDriver().findElement(By.xpath(".//*[@id='recipes_list']/p"));
			return false;
			
		} catch (NoSuchElementException e) {
			return true;//item found in list
		}
	}
	
	public boolean addRecipeWithIngredients(String recipeName) throws InterruptedException {
		
		waitForPageCompleteLoading();
		addNewRecipe(recipeName);
		
		
		WebElement btnAddIngredient = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.cssSelector(".addto.fleft")));
		btnAddIngredient.click();
		TimeUnit.SECONDS.sleep(1);
		
		String nameOfIngred = "ingredient1";
		addIngredient(nameOfIngred);
		TimeUnit.SECONDS.sleep(1);
		
		//check that the ingredient was added
		List<WebElement> table = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
				(By.xpath(".//*[@id='ingredient_table']/tbody/tr")));
		for (int i = 2; i <= table.size(); i++) {
			WebElement ingredientName = getWebDriver().findElement(By.xpath(".//*[@id='ingredient_table']/tbody/tr[" + i + "]/td[1]"));
			if(ingredientName.getText().equals(nameOfIngred))
					return true;
		}
		return false;
	}

	private void addIngredient(String name) throws InterruptedException {
		
		WebElement drpdwnArrow = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='s2id_knowledgebase_recipe_ingredient_name']/a/span[2]/b")));
		drpdwnArrow.click();
		TimeUnit.SECONDS.sleep(1);
		
		WebElement inputName =  driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='select2-drop']/div/input")));
		inputName.sendKeys(name);
		TimeUnit.SECONDS.sleep(1);
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='select2-drop']/ul/li/div/span"))).click();
		TimeUnit.SECONDS.sleep(1);
		WebElement mwInput = getWebDriver().findElement(By.id("ingredient_form_concentration_mw"));
		mwInput.sendKeys("12 gr/mol");	
		TimeUnit.SECONDS.sleep(1);
		
		WebElement concentaration = getWebDriver().findElement(By.id("ingredient_form_final_concentration"));
		concentaration.sendKeys("100");	
		TimeUnit.SECONDS.sleep(1);
		
		WebElement volume = getWebDriver().findElement(By.id("ingredient_form_weight_volume"));
		volume.sendKeys("500");
		TimeUnit.SECONDS.sleep(1);
		
		clickOnButton("submit_add_recipe_ingredient");
	}
	

	@Override
	public String getPageTitleXPath() {
		
		return ".//*[@id='knowledgebase_recipe_name_input']/span";
	}
}
