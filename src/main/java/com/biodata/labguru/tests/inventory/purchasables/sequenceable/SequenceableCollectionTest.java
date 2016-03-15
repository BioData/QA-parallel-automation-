package com.biodata.labguru.tests.inventory.purchasables.sequenceable;

import static org.testng.AssertJUnit.assertEquals;

import java.util.Locale;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.LGConstants;
import com.biodata.labguru.pages.inventory.purchasables.sequenceable.SequenceableCollectionPage;
import com.biodata.labguru.tests.TestOrderRandomizer;
import com.biodata.labguru.tests.inventory.purchasables.PurchasableCollectionTest;

@Listeners(TestOrderRandomizer.class)
public abstract class SequenceableCollectionTest extends PurchasableCollectionTest{

	
	@Test (groups = {"deep"})
	public void addNewItemWithSequence(){
		
		try {
			showTableIndex();
			
			String name = buildUniqueName(getPrefix()) + " with sequence";
			
			String msg = ((SequenceableCollectionPage) getPage()).addNewItemWithSequence(name);
			
			// Check the title of the page
			 assertEquals(getMessageSource().getMessage("collection.created.msg",new Object[]{getCollectionNameForMessage()}, Locale.US), msg.trim());
		}catch (Exception e) {
			setLog(e,"addNewItemWithSequence");
			Assert.fail(e.getMessage());
		}
	}
	
	@Test (groups = {"deep"})
	public void addNewSequenceFromSequencesTab(){
		
		try {
			showTableIndex();
			
			String seqToAdd = buildUniqueName(LGConstants.SEQUENCE_PREFIX);
			//create collection item
			addNewItem();
			
			String addedSequence = ((SequenceableCollectionPage) getPage()).addNewSequenceFromSequencesTab(seqToAdd);
			
			// Check the title of the page
			 assertEquals(addedSequence, seqToAdd);
		}catch (Exception e) {
			setLog(e,"addNewItemWithSequence");
			Assert.fail(e.getMessage());
		}
	}
}
