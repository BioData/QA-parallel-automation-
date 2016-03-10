package com.biodata.labguru.tests.inventory.purchasables.sequenceable;

import static org.testng.AssertJUnit.assertEquals;

import java.util.Locale;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.biodata.labguru.pages.inventory.purchasables.sequenceable.SequenceableCollectionPage;
import com.biodata.labguru.tests.inventory.purchasables.PurchasableCollectionTest;

public abstract class SequenceableCollectionTest extends PurchasableCollectionTest{

	
	@Test (groups = {"test"})
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
}
