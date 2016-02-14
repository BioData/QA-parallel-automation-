package com.biodata.labguru;

import org.openqa.selenium.firefox.FirefoxProfile;


public class LGFirefoxProfile extends FirefoxProfile{
	
	public LGFirefoxProfile() {
		
		String downloadPath = System.getProperty("user.dir") + LGConstants.ASSETS_FILES_DIRECTORY +  LGConstants.COLLECTIONS_TEMPLATES_DIRECTORY;
		setPreference("browser.download.folderList", 2);
		setPreference("browser.download.manager.showWhenStarting", false);
		setPreference("browser.download.dir", downloadPath);
		setPreference("browser.helperApps.neverAsk.openFile",
				"text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
		setPreference("browser.helperApps.neverAsk.saveToDisk",
   "text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
		setPreference("browser.helperApps.alwaysAsk.force", false);
		setPreference("browser.download.manager.alertOnEXEOpen", false);
		setPreference("browser.download.manager.focusWhenStarting", false);
		setPreference("browser.download.manager.useWindow", false);
		setPreference("browser.download.manager.showAlertOnComplete", false);
		setPreference("browser.download.manager.closeWhenDone", false);
		setPreference("xpinstall.signatures.required",false );
	}

}
