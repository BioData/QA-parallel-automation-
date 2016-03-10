package com.biodata.labguru.pages.inventory.purchasables.sequenceable;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

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
        String msg = checkForNotyMessage(By.cssSelector(".noty_text"));
		       
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
}
