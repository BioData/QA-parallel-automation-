package com.biodata.labguru.tests;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;
/**
 * This class takes care for randomization of te tests methods.
 * Each test class should be annotate with '@Listeners(TestOrderRandomizer.class)' to use it.
 * @author goni
 *
 */
public class TestOrderRandomizer implements IMethodInterceptor {

	public List<IMethodInstance> intercept(List<IMethodInstance> methods,ITestContext context) {
		
        long seed = System.nanoTime();
        Collections.shuffle(methods, new Random(seed));
        return methods;
	    
	}
	

}
