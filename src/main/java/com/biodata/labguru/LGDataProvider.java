package com.biodata.labguru;

import org.testng.annotations.DataProvider;

public class LGDataProvider {

	
	@DataProvider(name = "uploadFiles")
    public static Object[][] getFilesToUpload() 
    {
        return new Object[][] { { "myTest.txt" }, { "zones.xls" }, {"ab1.pdf"} };
    }
}
