package com.biodata.labguru;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

import jxl.Cell;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public final class GenericHelper {
	
	private static long counter = System.currentTimeMillis();
	
	public static void takeScreenshot(String fileName,WebDriver driver,String browserType){
		
		try{
			
			if(browserType.equals(LGConstants.CHROME)){
				JavascriptExecutor exe = (JavascriptExecutor) driver;
				exe.executeScript("window.scrollTo(0,0)");
				Boolean check = (Boolean) exe.executeScript("return document.documentElement.scrollHeight > document.documentElement.clientHeight");
				Long scrollH =  (Long) exe.executeScript("return document.documentElement.scrollHeight");
				Long clientH =  (Long) exe.executeScript("return document.documentElement.clientHeight");
				int index = 1;
				if(check.booleanValue()){
					while(scrollH.intValue() > 0){
						takeSingleScreeshot(LGConstants.SCREENSHOTS_DIRECTORY + fileName + " - " + index, driver);
						exe.executeScript("window.scrollTo(0," + clientH * index +")");
						scrollH = scrollH - clientH;
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						index++;
					}
				}else{
					takeSingleScreeshot(LGConstants.SCREENSHOTS_DIRECTORY + fileName , driver);
				}
				
				
			}else if(browserType.equals(LGConstants.FIREFOX)){
				
				 takeSingleScreeshot(LGConstants.SCREENSHOTS_DIRECTORY + fileName, driver);
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	private static void takeSingleScreeshot(String fileName, WebDriver driver)
			throws IOException {
		File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		 FileUtils.copyFile(src,new File(fileName + ".jpg"));
	}
	
	
	
	public static boolean writeToExcel(String fileName,List<String> fields) throws IOException, WriteException, BiffException{
		
		String pathToExcel = System.getProperty("user.dir") + LGConstants.ASSETS_FILES_DIRECTORY 
				+  LGConstants.COLLECTIONS_TEMPLATES_DIRECTORY + "/";

		Workbook workbook = Workbook.getWorkbook(new File(pathToExcel+ fileName));
		//This creates a readable spreadsheet. To obtain a writable version of this spreadsheet, a copy must be made, as follows:
		WritableWorkbook copyWorkbook = Workbook.createWorkbook(new File(pathToExcel + LGConstants.OUTPUT_EXCEL_FILE), workbook);
		
		WritableSheet sheet1 = copyWorkbook.getSheet(0); //take the first sheet

		int exist = 0;
		
		Cell[] row = sheet1.getRow(0);
		
		
		for (int i = 0; i < row.length; i++) {
			if(row[i].getContents().equals("Name")){
				//set data to the name
				Label label = new Label(0, 1, LGConstants.IMPORTED_NAME); 
				sheet1.addCell(label); 
			}
			//add data for each custom field 
			if(fields.contains(row[i].getContents())){
				
				if(row[i].getContents().endsWith(LGConstants.CUSTOM_FIELD_DATE)){
					Date now = Calendar.getInstance().getTime(); 
					DateFormat customDateFormat = new DateFormat ("dd MMM yyyy"); 
					WritableCellFormat dateFormat = new WritableCellFormat (customDateFormat); 
					DateTime dateCell = new DateTime(i, 1, now, dateFormat); 
					sheet1.addCell(dateCell); 
				}else if(row[i].getContents().endsWith(LGConstants.CUSTOM_FIELD_ONLY_NUMBERS)){
					
					WritableCellFormat integerFormat = new WritableCellFormat (NumberFormats.INTEGER); 
					jxl.write.Number number = new jxl.write.Number(i, 1, 123456, integerFormat); 
					sheet1.addCell(number); 
				}else{
					Label label = new Label(i, 1, "data" + i); 
					sheet1.addCell(label); 
				}
				exist++;
			}
		}

		//Writing the values into excel.
		copyWorkbook.write();
		//Close the workbook
		copyWorkbook.close();

		return (exist == fields.size());
	}

	public static void deleteGeneratedFiles() throws InterruptedException {
		String pathToDir = System.getProperty("user.dir") + LGConstants.ASSETS_FILES_DIRECTORY 
				+  LGConstants.COLLECTIONS_TEMPLATES_DIRECTORY ;
		File dir = new File(pathToDir);
		    for (File file: dir.listFiles()) {
		        if (!file.isDirectory()) {
		        	file.delete();
		        	TimeUnit.SECONDS.sleep(1);
		        }
		    }
	}
	
    public static String buildUniqueName(String prefix) {
        String result = prefix + counter++;
        return result;
    }
    
    public static String getCurrentDateDay() {
    	 
	   SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMHHmmss");

	   //get current date time with Calendar()
	   Calendar cal = Calendar.getInstance();
	   String day = dateFormat.format(cal.getTime());
	   return day;
	  
    }
    
    /**
     * isCurrentDatePreviousFutureDate - check that Date1 is previous to Date2
     *
     * @return true if result is less then 0  - fromDate is previous to  toDate,false otherwise
     */
    public static boolean isCurrentDatePreviousFutureDate(Date currentDate, Date futureDate) {

        int result = currentDate.compareTo(futureDate);

        return (result < 0);

    }
    
    public static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
    
    /**
     * Go over the gmail account labguru.qa@gmail.com to look for the notification we send from the labguru application.
     * Get all the inbox messages and take the subject content.
     * @return true if the given message to search is found.
     * @throws Exception
     */
    public static boolean checkMail(String msgToSearch) throws Exception{
    	
    	boolean found = false;
    	

		//create properties field
		String host = "imap.gmail.com";
		String username = "labguru.qa@gmail.com";
		String password = "labguru 2015";

		//create properties field
		Properties properties = new Properties();

		properties.put("mail.imap.host", host);
		properties.put("mail.imap.ssl.enable", "true");
		Session emailSession = Session.getInstance(properties);

		//create the POP3 store object and connect with the pop server
		Store store = emailSession.getStore("imap");

		store.connect(host, username, password);

		//create the folder object and open it
		Folder emailFolder = store.getFolder("INBOX");
		emailFolder.open(Folder.READ_WRITE);

		// retrieve the messages from the folder in an array and print it
		Message[] messages = emailFolder.getMessages();
		
		
		for (int i = 0, n = messages.length; i < n; i++) {
			Message message = messages[i];
			String msg = message.getSubject();
			System.out.println("Subject: " + msg);
			message.setFlag(Flag.DELETED, true);
			//the message we look for is found
			if(msg.equals(msgToSearch))
				found = true;
			
		}
		emailFolder.expunge();
	
		//close the store and folder objects
		emailFolder.close(true);
		store.close();
    	return found;
    }


}

