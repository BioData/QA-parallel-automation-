package com.biodata.labguru.pages.inventory.purchasables.sequenceable;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.inventory.purchasables.PurchasableCollectionPage;

public abstract class SequenceableCollectionPage extends PurchasableCollectionPage{

	
	protected void selectSequenceTab() throws InterruptedException {
		
		 WebElement tabSeq = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tabs-sequences-link")));
	     tabSeq.click();
	     TimeUnit.SECONDS.sleep(2);
	}
	
	@Override
	public List<String> getAvailableColumnsForCustomiseTableView() {
		List<String> columns = super.getAvailableColumnsForCustomiseTableView();
		columns.add("preferences_sequence");
		return columns;
	}
	
	
	public String addNewItemWithSequence(String plasmidName) throws InterruptedException {
		
		addItemWithGivenName(plasmidName);    

		addSequence();
		
        save();
        
        //wait for the noty message
        String msg = checkForNotyMessage();
		       
        selectSequenceTab();
        
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
		
		selectSequenceTab();
		
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

		//look for the 'Sequences' tab - 
		WebElement title = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("page-title")));
		if(title.getText().startsWith(LGConstants.SEQUENCE_PREFIX)){
			//we are in the created seq page and need to go back to recently viewed item
			//go back to the collection item which we added the seq from it
			goToRecentlyViewed();
			TimeUnit.SECONDS.sleep(2);
		}	
		
		selectSequenceTab();
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tabs-sequences")));
		
		List<WebElement> seqList = getWebDriver().findElements(By.xpath(".//*[@id='myCollection_body']/table/tbody/tr"));
		for (int i = 2; i <= seqList.size(); i++) {
			WebElement seqNameInRow = getWebDriver().findElement(By.xpath(".//*[@id='myCollection_body']/table/tbody/tr[" + i + "]/td[1]/p"));
			if(seqNameInRow.getText().equals(seqName))
				return seqName;
		}
		
		return "seq not added";
	}
}
