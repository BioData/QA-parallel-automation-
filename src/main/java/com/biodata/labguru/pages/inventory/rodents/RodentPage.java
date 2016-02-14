package com.biodata.labguru.pages.inventory.rodents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.inventory.CollectionPage;

public abstract class RodentPage extends CollectionPage{


	protected void selectSpecimensTab(){
		WebElement tab = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tabs-specimens-link")));
		tab.click();
	}
	
	@Override
	public List<String> getAvailableColumnsForCustomiseTableView() {
		List<String> columns = super.getAvailableColumnsForCustomiseTableView();
		columns.add("preferences_name");//name
		
		//rodents collections not hold location column so we remove it
		columns.remove("preferences_location");
		return columns;
	}
	
	protected boolean checkOnlySelectedSpecimenAdded(String specName) {
		
		boolean valid = false;
	    //check if selected  specimen is marked and the other in the cage are unchecked 
        List<WebElement> specimens = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@class='items_container']/li")));
        for (int i=1;i<=specimens.size();i++) {
    		WebElement checkbox = getWebDriver().findElement(By.xpath(".//*[@class='items_container']/li["+ i +"]/input"));
    		WebElement name = getWebDriver().findElement(By.xpath(".//*[@class='items_container']/li["+ i +"]"));
			if(checkbox.isSelected()){	
				if(name.getText().equals(specName))
					valid = true;
				else
					valid = false;
			}else{
				if(name.getText().equals(specName))
					valid = false;
				else
					valid = true;
			}
    	
        }
		return valid;
	}
	
	protected void applyTreatment() {
		
		WebElement btnApply = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("apply")));
        btnApply.click();
	}
	
	protected Map<String,List<String>> insertTreatmentDetails(String name, String date) throws InterruptedException {
		
		writeInRedactor("name", name);
        
        WebElement txtScheduledAt = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id( "scheduled_at")));
        txtScheduledAt.click();
        TimeUnit.SECONDS.sleep(1);
        
        if(date.equals(LGConstants.TODAY))
        	selectToday(txtScheduledAt);
        else if(date.equals(LGConstants.TOMORROW)){
        	selectTomorrow(txtScheduledAt);
        }else
        	selectPast(txtScheduledAt);

        
        WebElement input = driverWait.until(ExpectedConditions.visibilityOfElementLocated
        		(By.xpath(".//*[@id='s2id_reserved_from_']/a/span[1]")));
        String cage = input.getText();
        
        Map<String,List<String>> map = new HashMap<String,List<String>>();
        List<String> specNames = new ArrayList<String>();
        
        //take all specimes that were added
        List<WebElement> specimens = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@class='reservation_item']")));
        for (WebElement elem : specimens) {
    	   specNames.add(elem.getText());
        }
        map.put(cage, specNames);
        return map;
	}

	protected String checkTreatmentCreation(String name, List<String> specimens) throws InterruptedException {

        //go over all rows and check that the right name and specimens are written
        List<WebElement> rows = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
        		(By.xpath(".//*[@id='data']/tbody/tr")));
        for (int i=2; i<= rows.size() ; i++) {
        	WebElement nameEle = getWebDriver().findElement(By.xpath(".//*[@id='data']/tbody/tr[" + i + "]/td[3]"));
        	if(nameEle.getText().equals(name)){
        		List<WebElement> specList = getWebDriver().findElements(By.xpath(".//*[@id='data']/tbody/tr[" + i + "]/td[5]/a"));
        		 List<String> names = new ArrayList<String>();
        		for (WebElement specElem : specList) {
        			names.add(specElem.getText());
				}
        		if(specimens.equals(names) )
        			return name;
        	}
        	
		}
        
        return "The new treatment was not created as expected.(check name,specimens,cage)";
	}
	
	/**
	 * Check the specimen table in the specimens tab in cage
	 */
	protected String checkSpecimenCreation(String specName,String cageName) {
		
	  //go over all rows and check that the right name and specimens are written
	  List<WebElement> rows = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
	  		(By.xpath(".//*[@id='index_table']/tbody/tr")));
	  
	  int nameColumIndex = serchForColumnIndex("Name",rows);
	  int cageColumIndex = serchForColumnIndex("Cage",rows);
	  
	  for (int i=2; i<= rows.size() ; i++) {
		  	WebElement nameElm = getWebDriver().findElement(By.xpath(".//*[@id='index_table']/tbody/tr[" + i + "]/td[" + nameColumIndex + "]/a"));
		  	WebElement cageElm = getWebDriver().findElement(By.xpath(".//*[@id='index_table']/tbody/tr[" + i + "]/td[" + cageColumIndex + "]/a"));
		  	if(nameElm.getText().equals(specName) && cageElm.getText().equals(cageName)){
		  		return specName;
		  	}
			  	
	  }
	
	  return "";
	}
	
	protected int serchForColumnIndex(String columnName, List<WebElement> rows) {
		
		List<WebElement> cols = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
		  		(By.xpath(".//*[@id='index_table']/tbody/tr[1]/th")));
		  for (int col=2; col<= cols.size() ; col++) {
			  	WebElement nameElm = getWebDriver().findElement(By.xpath(".//*[@id='index_table']/tbody/tr[1]/th[" + col + "]/span"));
			  	
			  	if(nameElm.getText().equals(columnName)){
			  		return col;
			  	}	  	
		  }
		  return 0;
	}

	protected void setSpecimenDetails(String plasmidName) {
		
		try {
			WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated
					(By.id("name")));
			sendKeys(txtName, plasmidName);
			
			WebElement dropCage = getWebDriver().findElement
					(By.xpath(".//*[@id='s2id_biocollections_rodent_specimen_specimen_storage_id']/a/span[2]/b"));
			dropCage.click();
			TimeUnit.SECONDS.sleep(1);
			
			WebElement selectedCage = driverWait.until(ExpectedConditions.visibilityOfElementLocated
					(By.xpath(".//*[@id='select2-drop']/ul/li[last()]/div")));
			selectedCage.click();
			TimeUnit.SECONDS.sleep(1);
			
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}
	
	@Override
	public void deleteCustomFieldsFromCollection(String collectionName) throws InterruptedException {
		
		deleteCustomFieldsFromListOfCustomize(collectionName);
		
	}

}
