package com.biodata.labguru;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;

public class LGFireFoxDriver extends FirefoxDriver{
	
	@Autowired
	private LGFirefoxProfile ffProfile;

	public LGFireFoxDriver(LGFirefoxProfile ffProfile) {
		super(ffProfile);
	}
}
