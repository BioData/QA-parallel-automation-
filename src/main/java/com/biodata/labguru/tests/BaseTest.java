package com.biodata.labguru.tests;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import com.biodata.labguru.GenericHelper;
import com.biodata.labguru.LGConstants;
import com.biodata.labguru.WebDriverConfiguration;
import com.biodata.labguru.pages.PageManager;
import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.testng.SauceOnDemandAuthenticationProvider;

@ContextConfiguration("classpath*:/spring/applicationContext-automation.xml")
public class BaseTest extends AbstractTestNGSpringContextTests implements SauceOnDemandSessionIdProvider, SauceOnDemandAuthenticationProvider{
	
	public BaseTest() {
		super();
	}
	 /**
     * Constructs a {@link SauceOnDemandAuthentication} instance using the supplied user name/access key.  To use the authentication
     * supplied by environment variables or from an external file, use the no-arg {@link SauceOnDemandAuthentication} constructor.
     */
   // public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication("gonichazor", "bc580ecc-f8c7-4559-8337-fa6b16861e14");
	public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication("danainbar", "bff1466f-0523-4094-9329-69f12abc0f3c");


	@Autowired
    private ApplicationContext applicationContext;

	@Autowired
    private PageManager pageManager;
	
    @Autowired
	private ResourceBundleMessageSource messageSource;	

	protected static final Logger logger = Logger.getLogger(BaseTest.class);
    
    private DesiredCapabilities dc;

    /**
     * ThreadLocal variable which contains the  {@link WebDriver} instance which is used to perform browser interactions with.
     */
    private ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();

    /**
     * ThreadLocal variable which contains the Sauce Job Id.
     */
    private ThreadLocal<String> sessionId = new ThreadLocal<String>();
    

    private static final String USERNAME = System.getenv("SAUCE_USERNAME");
    private static final String ACCESS_KEY = System.getenv("SAUCE_ACCESS_KEY");
    public static final String URL = "http://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:80/wd/hub";
//    public static final String URL = "http://" + "gonichazor" + ":" + "bc580ecc-f8c7-4559-8337-fa6b16861e14" + "@ondemand.saucelabs.com:80/wd/hub";


	
	protected static String newAccountUser ;
	private static boolean newAccountCreated = false;
	
	protected String urlToTest;
	protected String userToTest;
	protected String passwordToTest;
	
	public String getUserToTest() {
		return userToTest;
	}
	
	@BeforeSuite(alwaysRun = true )	
	public void setup(){
		logger.info("set up started...");
		try {
			springTestContextPrepareTestInstance();
		} catch (Exception e) {
			setLog(e);
		}
		logger.info("set up finished...");
	}

	
	public ResourceBundleMessageSource getMessageSource() {
		return messageSource;
	}


	@AfterClass(alwaysRun=true)
	public void tearDown(){	
		logger.debug(getDebugMessage() + "driver is going to quit" );	
		pageManager.getWebDriver().quit();
		logger.debug(getDebugMessage() + "driver quit" );	
	}
	
	/**
    *
    * @return the {@link SauceOnDemandAuthentication} instance containing the Sauce username/access key
    */
   @Override
   public SauceOnDemandAuthentication getAuthentication() {
       return authentication;
   }
	
    /**
     * @return the {@link WebDriver} for the current thread
     */
    public WebDriver getWebDriver() {
        return webDriver.get();
    }

    /**
     *
     * @return the Sauce Job id for the current thread
     */
    public String getSessionId() {
        return sessionId.get();
    }
	
	@Parameters ({"url" ,"user","password","newAccount","browser"})
	@BeforeClass (alwaysRun = true)
	public void initialize(String url,String user,String password,String newAccount,String browser) throws Exception{

		initAccountData(url, user, password, newAccount);
		
		logger.info("start browser: " + browser);
		String className = this.getClass().getName();
		dc = WebDriverConfiguration.getDesiredCapabilities(browser, Platform.MAC,className);
        webDriver.set(createDriver(dc));
        logger.debug(getDebugMessage() + "driver was created successfully" );
        
        ((RemoteWebDriver) getWebDriver()).setFileDetector(new LocalFileDetector());
		pageManager.setBrowserType(browser,getWebDriver());
		pageManager.initPages(getWebDriver(),logger);
		pageManager.setLogger(logger);
		
		if(Boolean.valueOf(newAccount).booleanValue() && !newAccountCreated){
			newAccountCreated = true;
			createNewAccount(userToTest.substring(0, userToTest.indexOf(LGConstants.GMAIL_SUFFIX_MAIL)));
		}
		signin(newAccount);

	}

	protected void initAccountData(String url, String user, String password,String newAccount) throws InterruptedException {
	
		if(Boolean.valueOf(newAccount).booleanValue() && !newAccountCreated){
			newAccountUser = LGConstants.QA_PREFIX_MAIL + LGConstants.JENKINS + System.identityHashCode(Thread.currentThread());//GenericHelper.getCurrentDateDay();
			logger.info(getDebugMessage() + "newAccountUser :" + newAccountUser);
		}

		this.urlToTest = pageManager.getStagingUrl();
		if(url.equals("production")){
			this.urlToTest = pageManager.getProductionUrl();
			logger.info("Running on Production: " + urlToTest);
		}else if(url.equals("other")){
			this.urlToTest = pageManager.getOtherUrl();
			logger.info("Running on other: " + urlToTest);
		}else{
			logger.info("Running on Staging: " + urlToTest);
		}
		
		this.passwordToTest= password;
		if(Boolean.valueOf(newAccount).booleanValue()){
			this.userToTest =  newAccountUser  + LGConstants.GMAIL_SUFFIX_MAIL;
			
		}else{
			this.userToTest =  user  + LGConstants.GMAIL_SUFFIX_MAIL;
		}
		logger.info("User to test: " + userToTest);
	}

	public String buildUniqueName(String prefix) {
		
		return GenericHelper.buildUniqueName(prefix);
	}

	private void createNewAccount(String fName) {
		try {
			logger.debug( getDebugMessage() + "creating new account" + userToTest);
			String signupUrl = LGConstants.STAGING_SIGNUP_URL;
			pageManager.getLoginPage().createNewAccount(signupUrl,fName, LGConstants.BIODATA_MEMBER_NAME, userToTest,passwordToTest);
			logger.info("User name: " + fName);
			logger.info("User last name: " + LGConstants.BIODATA_MEMBER_NAME);		
			pageManager.getLoginPage().closeIridizePopups();
			pageManager.getAccountSettingPage().configSettings();
			
		} catch (InterruptedException e) {
			setLog(e);
		}
	}
	
    /**
     * Constructs a new {@link RemoteWebDriver} instance which is configured to use the capabilities defined by the browser,
     * version and os parameters, and which is configured to run against ondemand.saucelabs.com, using
     * the username and access key populated by the {@link #authentication} instance.
     *
     * @param browser Represents the browser to be used as part of the test run.
     * @param version Represents the version of the browser to be used as part of the test run.
     * @param platform Represents the operating system to be used as part of the test run.
     * @param methodName Represents the name of the test case that will be used to identify the test on Sauce.
     * @return
     * @throws MalformedURLException if an error occurs parsing the url
     */
    private WebDriver createDriver(DesiredCapabilities dc) throws MalformedURLException {
        // Launch remote browser and set it as the current thread	
//        webDriver.set(new RemoteWebDriver(
//                new URL("http://" + authentication.getUsername() + ":" + authentication.getAccessKey() + "@ondemand.saucelabs.com:80/wd/hub"),
//                dc));
        //for saucelabs plugin on jenkins
        webDriver.set(new RemoteWebDriver(
                new URL("http://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:80/wd/hub"),
                dc));

        // set current sessionId        
        String id = ((RemoteWebDriver) getWebDriver()).getSessionId().toString();
        sessionId.set(id);

        // print out sessionId and jobname for consumption by Sauce Jenkins plugin
        System.out.println(String.format("SauceOnDemandSessionID=%1$s job-name=%2$s", id, dc.getCapability("name")));

        return webDriver.get();
    }
	
    protected String getDebugMessage(){
    	return  "@@[PROCESS ID]: " + Thread.currentThread().getId() +" -[DRIVER ID]: " + getSessionId() + " - " ;
    }
    public void signin(String newAccount){
    	
		try {		
			logger.debug( getDebugMessage() + "sign in");
	    	pageManager.getLoginPage().signIn(urlToTest,userToTest,passwordToTest);		 
			//disable Iridize popups
			pageManager.getLoginPage().closeIridizePopups();
			if(Boolean.valueOf(newAccount).booleanValue()){
				pageManager.getAccountSettingPage().setTimeZone();
				getPageManager().getAccountSettingPage().configSettings();
				logger.debug( getDebugMessage() + "after configSettings of: " + userToTest);
			}
	    	TimeUnit.SECONDS.sleep(5);
		} catch (Exception e) {
			setLog(e);
		}
    }
    
    public void closeIridizePopups() {
    	try {
			pageManager.getLoginPage().closeIridizePopups();
		} catch (InterruptedException e) {
			setLog(e);
		}
    }
    
    public PageManager getPageManager() {
		return pageManager;
	}
    
	protected void setLog(Exception e) {
	
		if(getPageManager().getWebDriver().getTitle().equals(LGConstants.WOOOPS_MESSAGE)){
			logger.fatal(this.getClass().getName() + " : WHOOPS !!!!!!!");
			signin("false");
		}else if(getPageManager().getWebDriver().getTitle().equals(LGConstants.PAGE_NOT_FOUND)){
			logger.fatal(this.getClass().getName() + " : PAGE NOT FOUND!!!!!!");
			signin("false");
		}
		if(logger.isDebugEnabled()){
			logger.debug("DEBUG : ",e);
		}
 
		if(logger.isInfoEnabled()){
			logger.info("INFO : " + e.getMessage());
		}
		if(logger.isEnabledFor(Level.ERROR)){
			logger.error("ERROR : " ,e);
			logger.fatal("FATAL : " ,e);
		}
		
	}
	
	protected void setLog(Exception e,String testName) {
		
		if(getPageManager().getWebDriver().getTitle().equals(LGConstants.WOOOPS_MESSAGE)){
			logger.fatal(this.getClass().getName() + " : WHOOPS !!!!!!!"+ "in test '"+ testName + "'");
		}
		else if(getPageManager().getWebDriver().getTitle().equals(LGConstants.PAGE_NOT_FOUND)){
			logger.fatal(this.getClass().getName() + " : PAGE NOT FOUND!!!!!!" + "in test '"+ testName + "'");
			signin("false");
		}
		printSessionId(testName);
		setLog(e);
	}
	
	private void printSessionId(String testName) {
		 
	    String message = String.format("SauceOnDemandSessionID=%1$s job-name=%2$s",
	    (((RemoteWebDriver) getWebDriver()).getSessionId()).toString(), testName);
	    System.out.println(message);
	} 


}
