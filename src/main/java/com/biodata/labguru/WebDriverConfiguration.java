package com.biodata.labguru;

import org.openqa.selenium.Platform;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class WebDriverConfiguration {
	
	
	
	public static DesiredCapabilities getDesiredCapabilities(String browser,Platform platform,String className){
		
		DesiredCapabilities dc = new DesiredCapabilities();
		
		if(browser.equals(LGConstants.FIREFOX)){

	        FirefoxProfile fp = new LGFirefoxProfile();
	        dc.setCapability(FirefoxDriver.PROFILE, fp);      
	        dc.setBrowserName(DesiredCapabilities.firefox().getBrowserName());
	        
		}else if(browser.equals(LGConstants.CHROME)){
	
			System.setProperty("webdriver.chrome.driver","./Drivers/chromedriver");
			dc.setBrowserName(DesiredCapabilities.chrome().getBrowserName());
		}
		
		dc.setCapability("platform" ,platform);
		dc.setCapability("name", className);
        dc.setCapability("build", GenericHelper.buildUniqueName("Build_"));
        dc.setCapability("autoAcceptAlerts", "true");
        dc.setCapability("screenResolution", "1280x800");
        dc.setJavascriptEnabled(true);
        dc.setCapability("maxDuration",10800);
        dc.setCapability( "idleTimeout",1000);
        dc.setCapability(CapabilityType.SUPPORTS_FINDING_BY_CSS, "true");
        dc.setCapability(CapabilityType.SUPPORTS_ALERTS, "true");
		return dc;
	}

}
