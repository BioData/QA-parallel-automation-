package com.biodata.labguru.tests.storage;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.biodata.labguru.tests.AbstractLGTest;
import com.biodata.labguru.tests.TestOrderRandomizer;

@Listeners(TestOrderRandomizer.class)
public abstract class AbstractStoragesTest extends AbstractLGTest{

	
	@Override
	@Test (enabled = false)
	public void addTask(){
		//no add task in cryogenic page
	}
	
	@Override
	@Test (enabled = false)
	public void duplicateItem(){
		// not implemented
		throw new UnsupportedOperationException("This action is not supported by this module");
	}
}
