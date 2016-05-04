package com.biodata.labguru.tests.knowledgebase;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Locale;

import org.testng.AssertJUnit;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.knowledgebase.AbstractKnowledgebasePage;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public class RecipesTest extends AbstractKnowledgebaseTest{
	
	
	@Override
	public void showMenu(){

		String pageTitle = showTableIndex();

		// Check the title of the page
		assertEquals(getMessageSource().getMessage("recipes.title",null, Locale.US), pageTitle);
	
	}
	
	@Test (groups = {"basic sanity"})
	public void addRecipeFromDirectory(){
		
		try {
			showTableIndex();
			String recipeName = getPageManager().getRecipePage().addRecipeFromDirectory();

			getPageManager().getAdminPage().showRecipes();
			assertTrue(getPageManager().getRecipePage().searchForItem(recipeName));
			
			//delete the added recipe from list after the test 
			getPageManager().getRecipePage().deleteRecipe(recipeName);
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		} 
	}
	
	@Test (groups = {"basic sanity"})
	public void addNewRecipe(){
		
		try {
			showTableIndex();

			String recipeName = buildUniqueName(LGConstants.RECIPE_PREFIX);
			getPageManager().getRecipePage().addNewRecipe(recipeName);

			getPageManager().getAdminPage().showRecipes();
			assertTrue(getPageManager().getRecipePage().searchForItem(recipeName));
			
		} catch (Exception e) {
			setLog(e);
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void addRecipeWithIngredient(){
		
		try {
			showTableIndex();

			String recipeName = buildUniqueName(LGConstants.RECIPE_PREFIX);
			boolean ingredientAdded = getPageManager().getRecipePage().addRecipeWithIngredients(recipeName);
			assertTrue("Ingredient not added.",ingredientAdded);
			
			
		} catch (Exception e) {
			setLog(e,"addRecipeWithIngredient");
			AssertJUnit.fail(e.getMessage());
		}
	}
	
	@Override
	protected AbstractKnowledgebasePage getPage() {
		
		return getPageManager().getRecipePage();
	}


	@Override
	protected String addNewItem() throws InterruptedException {
	
		String recipeName = buildUniqueName(LGConstants.RECIPE_PREFIX);
		getPageManager().getRecipePage().addNewRecipe(recipeName);
		return recipeName;
	}

	@Override
	protected String showModule() {
		return getPageManager().getAdminPage().showRecipes();
	}
}
