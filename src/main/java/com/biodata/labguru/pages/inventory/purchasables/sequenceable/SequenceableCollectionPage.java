package com.biodata.labguru.pages.inventory.purchasables.sequenceable;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.inventory.purchasables.PurchasableCollectionPage;

public abstract class SequenceableCollectionPage extends PurchasableCollectionPage{

	protected static Boolean sequenceEnabled = null;
	
	protected void selectSequencesTab() throws InterruptedException {
		
		 WebElement tabSeq = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tabs-sequences-link")));
	     tabSeq.click();
	     TimeUnit.SECONDS.sleep(2);
	}
	
	@Override
	public List<String> getAvailableColumnsForCustomiseTableView() {
		List<String> columns = super.getAvailableColumnsForCustomiseTableView();
		if(isSequenceEnabled(getCollectionName())){
			columns.add("preferences_sequence");
		}
		return columns;
	}
	
	/** check once per class
	 *  check in customize fields of the collection if the sequence field is enabled
	 * @param collectionName
	 * @return
	 */
	public boolean isSequenceEnabled(String collectionName) {
		
		//if we already checked the sequence status - don't check again
		if(sequenceEnabled != null)
			return sequenceEnabled.booleanValue();
		
		String currentUrl = getWebDriver().getCurrentUrl();
		//go to collections settings
		showCollectionsAndSettings();
		//click on customize of current collection
		WebElement linkCustom = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(getCustomizeLinkXpath(collectionName))));
		linkCustom.click();
		
		//look for purchasable attributes field to check if selected or not
		List <WebElement> defaultFields = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
				(By.xpath(".//*[@class = 'default_fields config']/li")));
		for (int i = 1; i <= defaultFields.size(); i++) {
			WebElement fieldName = getWebDriver().findElement(By.xpath(".//*[@class = 'default_fields config']/li[" + i +"]/span"));
			if(fieldName.getText().equals(LGConstants.SEQUENCE_FIELD)){
				WebElement checkbox = getWebDriver().findElement(By.xpath(".//*[@class = 'default_fields config']/li[" + i +"]/input"));
				sequenceEnabled = Boolean.valueOf(checkbox.isSelected());
				break;
			}
		}
		getWebDriver().get(currentUrl);
		waitForPageCompleteLoading();
		
		return sequenceEnabled.booleanValue();
	}
	
	public String addNewItemWithSequence(String plasmidName) throws InterruptedException {
		
		addItemWithGivenName(plasmidName);    

		addSequence();
		
        save();
        
        //wait for the noty message
        String msg = checkForNotyMessage();
		       
        selectSequencesTab();
        
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tabs-sequences")));
		
		List<WebElement> seqList = getWebDriver().findElements(By.xpath(".//*[@id='myCollection_body']/table/tbody/tr"));
		if(seqList.size() > 1)
			return msg;
		return "seq not added";
  
	}
	
	protected void addSequence() {
		
		String sequence = "TGGCGAATGGGACGCGCCCTGTAGCGGCGCATTAAGCGCGGCGGGTGTGGTGGTTACGCGCAGCGTGACCGCTACA"
		+ "CTTGCCAGCGCCCTAGCGCCCGCTCCTTTCGCTTTCTTCCCTTCCTTTCTCGCCACGTTCGCCGGCTTTCCCCG"
		+ "TCAAGCTCTAAATCGGGGGCTCCCTTTAGGGTTCCGATTTAGTGCTTTACGGCACCTCGACCCCAAAAAACTT"
		+ "GATTAGGGTGATGGTTCACGTAGTGGGCCATCGCCCTGATAGACGGTTTTTCGCCCTTTGACGTTGGAGTCCAC"
		+ "GTTCTTTAATAGTGGACTCTTGTTCCAAACTGGAACAACACTCAACCCTATCTCGGTCTATTCTTTTGATTTAT"
		+ "AAGGGATTTTGCCGATTTCGGCCTATTGGTTAAAAAATGAGCTGATTTAACAAAAATTTAACGCGAATTTTAAC"
		+ "AAAATATTAACGTTTACAATTTCAGGTGGCACTTTTCGGGGAAATGTGCGCGGAACCCCTATTTGTTTATTTTT"
		+ "CTAAATACATTCAAATATGTATCCGCTCATGAATTAATTCTTAGAAAAACTCATCGAGCATCAAATGAAACTGC"
		+ "AATTTATTCATATCAGGATTATCAATACCATATTTTTGAAAAAGCCGTTTCTGTAATGAAGGAGAAAACTCACC"
		+ "GAGGCAGTTCCATAGGATGGCAAGATCCTGGTATCGGTCTGCGATTCCGACTCGTCCAACATCAATACAACCTA"
		+ "TTAATTTCCCCTCGTCAAAAATAAGGTTATCAAGTGAGAAATCACCATGAGTGACGACTGAATCCGGTGAGAATG"
		+ "GCAAAAGTTTATGCATTTCTTTCCAGACTTGTTCAACAGGCCAGCCATTACGCTCGTCATCAAAATCACTCGCAT"
		+ "CAACCAAACCGTTATTCATTCGTGATTGCGCCTGAGCGAGACGAAATACGCGATCGCTGTTAAAAGGACAATTAC"
		+ "AAACAGGAATCGAATGCAACCGGCGCAGGAACACTGCCAGCGCATCAACAATATTTTCACCTGAATCAGGATATT"
		+ "CTTCTAATACCTGGAATGCTGTTTTCCCGGGGATCGCAGTGGTGAGTAACCATGCATCATCAGGAGTACGGATAA"
		+ "AATGCTTGATGGTCGGAAGAGGCATAAATTCCGTCAGCCAGTTTAGTCTGACCATCTCATCTGTAACATCATTG"
		+ "GCAACGCTACCTTTGCCATGTTTCAGAAACAACTCTGGCGCATCGGGCTTCCCATACAATCGATAGATTGTCGCA"
		+ "CCTGATTGCCCGACATTATCGCGAGCCCATTTATACCCATATAAATCAGCATCCATGTTGGAATTTAATCGCGGCC"
		+ "TAGAGCAAGACGTTTCCCGTTGAATATGGCTCATAACACCCCTTGTATTACTGTTTATGTAAGCAGACAGTTTTAT"
		+ "TGTTCATGACCAAAATCCCTTAACGTGAGTTTTCGTTCCACTGAGCGTCAGACCCCGTAGAAAAGATCAAAGGATC"
		+ "TTCTTGAGATCCTTTTTTTCTGCGCGTAATCTGCTGCTTGCAAACAAAAAAACCACCGCTACCAGCGGTGGTTTGTT"
		+ "TGCCGGATCAAGAGCTACCAACTCTTTTTCCGAAGGTAACTGGCTTCAGCAGAGCGCAGATACCAAATACTGTCCTTCTAGTGTAGCCG";
		
		WebElement txtSeq = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[starts-with(@id,'seq')][starts-with(@class,'txt-field')]")));
		txtSeq.sendKeys(sequence);
	}
	
	protected void addFeatureToSeq() throws InterruptedException {
		WebElement btnAddFeature = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add_new_feature")));
		btnAddFeature.click();
		TimeUnit.SECONDS.sleep(2);
		WebElement popupDialog = getWebDriver().switchTo().activeElement();
        driverWait.until(ExpectedConditions.visibilityOf(popupDialog));

		WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("title")));
		txtName.sendKeys("feature1");
		
		WebElement txtStartP = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("biocollections_feature_start_position")));
		txtStartP.sendKeys("3");
		
		WebElement txtEndP = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("biocollections_feature_end_position")));
		txtEndP.sendKeys("100");
		
		save();
		
		getWebDriver().switchTo().activeElement();
	}

	/**
	 * Switch to 'Sequences' tab in sequenceable item and add sequence from there.
	 * @param seqName - the name of the sequence to add
	 * @return
	 * @throws InterruptedException 
	 */
	public String addNewSequenceFromSequencesTab(String seqName) throws InterruptedException {
		
		selectSequencesTab();
		
		clickOnButton("add_new_sequence");
		TimeUnit.SECONDS.sleep(2);
		
		WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("title")));
		if(!txtName.getAttribute("value").startsWith("Seq. for:")){
			return "Wrong name for added sequence - has no prefix 'Seq. for:'.";
		}
		sendKeys(txtName, seqName);
		
		//add seq
		addSequence();		
		save();	
		checkForNotyMessage();

		WebElement title = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("page-title")));
		//in primer when we add sequence it stays in the sequence page and we need to go back to the primer
		if(title.getText().startsWith(LGConstants.SEQUENCE_PREFIX)){
			//we are in the created seq page and need to go back to recently viewed item
			//go back to the collection item which we added the seq from it
			goToRecentlyViewed();
			TimeUnit.SECONDS.sleep(2);
		}	
		
		selectSequencesTab();
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tabs-sequences")));
		
		List<WebElement> seqList = getWebDriver().findElements(By.xpath(".//*[@id='myCollection_body']/table/tbody/tr"));
		for (int i = 2; i <= seqList.size(); i++) {
			WebElement seqNameInRow = getWebDriver().findElement(By.xpath(".//*[@id='myCollection_body']/table/tbody/tr[" + i + "]/td[1]/p"));
			if(seqNameInRow.getText().equals(seqName))
				return seqName;
		}
		
		return "seq not added";
	}
	
	/**
	 * select the given custom field checkbox in the customize fields settings to enable it
	 * @param fieldName
	 * @param collectionName
	 */
	public void enableCustomField(String fieldName,String collectionName){
		
		showCollectionsAndSettings();
		//click on customize of current collection
		WebElement linkCustom = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(getCustomizeLinkXpath(collectionName))));
		linkCustom.click();
		
		//look for purchasable attributes field to check if selected or not
		List <WebElement> defaultFields = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
				(By.xpath(".//*[@class = 'default_fields config']/li")));
		for (int i = 1; i <= defaultFields.size(); i++) {
			WebElement fieldNameElm = getWebDriver().findElement(By.xpath(".//*[@class = 'default_fields config']/li[" + i +"]/span"));
			if(fieldNameElm.getText().equals(fieldName)){
				WebElement checkbox = getWebDriver().findElement(By.xpath(".//*[@class = 'default_fields config']/li[" + i +"]/input"));
				if(!checkbox.isSelected()){
					checkbox.click();
					break;
				}
				
			}
		}
	}
}
