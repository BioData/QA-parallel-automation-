package com.biodata.labguru.pages.inventory;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.biodata.labguru.LGConstants;


public class SequencePage extends CollectionPage{

	@Override
	protected void initPage(WebDriver webDriver) {
		PageFactory.initElements(webDriver, this);	
	}
	
	
	@Override
	public boolean hasList() {
		try {
			WebElement title = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".empty_note")));
			return !title.getText().trim().equals("Start your sequence collection by adding one manually");
		} catch (Exception e) {
			return true;
		}
	}
	@Override
	protected String getImportXPath() {
		throw new UnsupportedOperationException("This action is not supported by this collection.");
	}

	@Override
	protected String getFileNameToImport() {
		throw new UnsupportedOperationException("This action is not supported by this collection.");
	}
	
	@Override
	public List<String> getAvailableColumnsForCustomiseTableView() {
		List<String> columns = super.getAvailableColumnsForCustomiseTableView();
		columns.add("preferences_sysid");//sysid
		columns.add("preferences_title");//name
		columns.add("preferences_kind");//type
		columns.add("preferences_organism");
		columns.add("preferences_accsesion");
		columns.add("preferences_source_id");//belongs to
		
		//columns that not appear in sequence
		columns.remove("preferences_alternative_name");
		columns.remove("preferences_source");
		return columns;
	}

	@Override
	protected void addItemWithGivenName(String name) {
		clickNewButton("new_sequence");
	      
        WebElement txtName = driverWait.until(ExpectedConditions.visibilityOfElementLocated
        		(By.id("title")));
        sendKeys(txtName, name);
        
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
		
		WebElement txtSeq = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("seq")));
		txtSeq.sendKeys(sequence);
		
	}

	@Override
	protected String getEditCollectionPrefix() {
		throw new UnsupportedOperationException("This action is not supported by this collection.");
	}

	
	@Override
	protected String getCollectionName() {
		
		return LGConstants.SEQUENCE_PREFIX;
	}

}
