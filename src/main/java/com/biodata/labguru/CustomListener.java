package com.biodata.labguru;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 * Created by Goni.
 */
public class CustomListener extends TestListenerAdapter{

    private String previousTest = "";

    
    @Override
    public void onTestFailure(ITestResult tr) {	
    	
        log(tr.getTestClass().getName()+ " : " +tr.getName()+ "--Test method failed\n",tr);
        printError("FAILED",tr);
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
    	
        log(tr.getTestClass().getName() + " : " + tr.getName()+ "--Test method skipped\n",tr);
        printError("SKIPPED",tr);
    }

    private void printError(String status,ITestResult tr) {
    	 StringBuilder msg = new StringBuilder();
         msg.append(status);
         // If the test failed due to an exception, include the exception info in the log message, unless it's a TestNG timeout exception
         Throwable t = tr.getThrowable();
         if (t != null) {
             String nl = System.getProperty("line.separator");
             msg.append(nl);
             msg.append("    ");
             msg.append(t.toString());

             // If it's not a thread timeout, include the stack trace too
             if (!(t instanceof org.testng.internal.thread.ThreadTimeoutException)) {

                 for (StackTraceElement e : t.getStackTrace()) {
                     msg.append(nl);
                     msg.append("    ");
                     msg.append(e.toString());
                 }
             }
         }
         
         System.out.println(msg);
		
	}

	@Override
    public void onTestSuccess(ITestResult tr) {

        log(tr.getTestClass().getName() + " : " + tr.getName() + "--Test method success\n",tr);
    }

    private void log(String string,ITestResult tr) {
       
        //after finish one test class - new line
        if(!tr.getTestClass().getName().equals(previousTest)){
        	 System.out.println(".................................");
        	 previousTest = tr.getTestClass().getName();
        }
        System.out.print(string);

    }

}
