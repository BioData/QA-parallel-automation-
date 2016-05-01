package com.biodata.labguru.pages;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.stereotype.Component;

import com.biodata.labguru.LGConstants;

@Component
public class AccountSettingsPage extends AdminPage{

	
	@Override
	protected  void initPage(WebDriver webDriver) {
		
		PageFactory.initElements(webDriver, AccountSettingsPage.class);

	}
	
	public void configSettings(){

		selectAccountSettingMenu();
		
		try {
			WebElement chkDrowCompound = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("admin_account_draw_compounds")));
			if(!chkDrowCompound.isSelected())
				chkDrowCompound.click();
			
			WebElement chkShareProjects = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("admin_account_share_projects")));
			if(!chkShareProjects.isSelected())
				chkShareProjects.click();
			
			WebElement chkShareRepos = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("admin_account_share_repositories")));
			if(!chkShareRepos.isSelected())
				chkShareRepos.click();
			
			//this option is missing from qut account (qut.labguru.com)
			WebElement chkShareOffice = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("admin_account_share_office")));
			if(!chkShareOffice.isSelected())
				chkShareOffice.click();
		} catch (Exception e) {
			getLogger().debug("one of the configuration is missing: " + e.getMessage());
		}

		//save anyway
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Save"))).click();	
		checkForAlerts();
		
		checkForNotyMessage();
	}
	
	public void setTimeZone() {
		
		selectAccountSettingMenu();
		
		WebElement selectedTimeZone = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='s2id_admin_account_time_zone']/a/span[1]")));
		if(selectedTimeZone.getText().contains(LGConstants.JERUSALEM_TIME_ZONE))
			return;
		
		WebElement dropTimeZone = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='s2id_admin_account_time_zone']/a/span[2]/b")));
		dropTimeZone.click();
		

		WebElement txtTimeZone = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='select2-drop']/div/input")));
		txtTimeZone.sendKeys(LGConstants.JERUSALEM_TIME_ZONE);
		
		selectedTimeZone = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='select2-drop']/ul/li/div")));
		selectedTimeZone.click();
		
		save();
	}
	
	public void selectBillingTab(){
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("billing"))).click();
	}
	
	public void selectCollectionsTab(){
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("collections"))).click();
	}
	
	public void selectLabelguruTab(){
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("labelguru"))).click();
	}
	
	public void selectDataExportTab(){
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("export"))).click();
	}
	
	public String addPaymentMethod() throws InterruptedException{
		
		WebElement btnSetPayment = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("set_payment_method")));
		btnSetPayment.click();
		TimeUnit.SECONDS.sleep(1);
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#main-content>div>h1")));
		
		WebElement txtFName = getWebDriver().findElement(By.id("customer_first_name"));
		sendKeys(txtFName, LGConstants.BIODATA_MEMBER_NAME);

		
		WebElement txtLName = getWebDriver().findElement(By.id("customer_last_name"));
		sendKeys(txtLName, "test");
		
		WebElement txtCardNum = getWebDriver().findElement(By.id("customer_credit_card_number"));
		sendKeys(txtCardNum, "4111111111111111");
		
		WebElement drpExpirationYear = getWebDriver().findElement(By.xpath(".//*[@id='s2id_customer_credit_card_expiration_year']/a/span[2]/b"));
		drpExpirationYear.click();
		TimeUnit.SECONDS.sleep(1);
		
		WebElement selectExpirationYear = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath(".//*[@id='select2-drop']/ul/li[last()]/div")));
		selectExpirationYear.click();
		TimeUnit.SECONDS.sleep(1);
		
		WebElement txtCvv = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.id("customer_credit_card_cvv")));
		sendKeys(txtCvv, "100");
		
		WebElement txtDiscountCode = getWebDriver().findElement(By.id("customer_custom_fields_discount_code"));
		sendKeys(txtDiscountCode, "");
		
		WebElement btnSave = driverWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".green-line-buttons.top-green-line>input")));
		btnSave.click();
		TimeUnit.SECONDS.sleep(1);
		
		String msg = checkForNotyMessage();
		
		return msg;
	}


	
	public String updatePlan(int numOfMembers) throws InterruptedException{
			
		if(numOfMembers < 100){
			
			WebElement drpNumMembers = getWebDriver().findElement(By.xpath(".//*[@id='s2id_number_of_members']/a/span[2]/b"));
			drpNumMembers.click();
			TimeUnit.SECONDS.sleep(1);
			int newNum = numOfMembers + 1;
			
			WebElement select = getWebDriver().findElement(By.xpath(".//*[@id='select2-drop']/ul/li["+String.valueOf(newNum) +"]/div"));
			select.click();
			TimeUnit.SECONDS.sleep(1);
		}
		
		WebElement btnUpdate = getWebDriver().findElement(By.xpath(".//*[@value='Update']"));
		btnUpdate.click();
		TimeUnit.SECONDS.sleep(1);
		
		String msg = checkForNotyMessage();
		return msg;
	}
	
	public boolean hasPaymentMethod(){
		selectAccountBillingMenu();
		try {
			List<WebElement> labels = getWebDriver().findElements(By.xpath(".//*[@class='long']"));
			for (WebElement webElement : labels) {
				if(webElement.getText().trim().equals("Payment method:")){
					return true;
				}
			}	
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public int getNumOfMembersInPlan() throws InterruptedException {
		
		selectAccountBillingMenu();
		
		WebElement btnUpdatePayment = driverWait.until(ExpectedConditions.visibilityOfElementLocated
				(By.id("update_plan")));
		btnUpdatePayment.click();
		TimeUnit.SECONDS.sleep(1);
		
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#main-content>div>h1")));

		String currentNumber = getWebDriver().findElement(By.xpath(".//*[@id='s2id_number_of_members']/a/span[1]")).getText();
		int newNum = Integer.valueOf(currentNumber) ;
		return newNum;
	}

	
	public boolean hasNotReachedToMaxUsersInPlan(int currentNumOfMembers) {

		selectAccountMembersMenu();
		
		List <WebElement> membersList = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
				(By.xpath(".//*[@id='active_members_list']/li")));
		
		return  membersList.size() <= currentNumOfMembers;
	}

	
	public String dataExport() throws Exception {
		
		String accountName = getAccountName();
		
		WebElement btnExport = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='export_cont']/p[2]/input")));
		btnExport.click();
		TimeUnit.SECONDS.sleep(1);
		//if we click on export in the last 24 hours - noty msg is shown 
		String msg = checkForNotyMessage();
		TimeUnit.SECONDS.sleep(2);
		if(msg.equals("")){//export can continue
			WebElement title = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='export_cont']/h3")));		
			if(!title.getText().contains(accountName))
				throw new Exception("Export data Failed for account " + accountName);
			
			WebElement msgElem = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='export_cont']/p[1]/b")));		
			String txt = msgElem.getText();
			return txt;
		}
		return msg;
	
	}

	public void checkCollection(String idOfCollection) throws InterruptedException {
		WebElement chkBtn = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(idOfCollection)));
		if(!chkBtn.isSelected()){
			chkBtn.click();
			TimeUnit.SECONDS.sleep(1);
			WebElement btnSave = getWebDriver().findElement(By.id("save_collections"));
			btnSave.click();	
			TimeUnit.SECONDS.sleep(2);
		}
	}
	public void uncheckCollection(String idOfCollection) throws InterruptedException {
		WebElement chkBtn = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(idOfCollection)));
		if(chkBtn.isSelected()){
			chkBtn.click();
			TimeUnit.SECONDS.sleep(1);
			WebElement btnSave = getWebDriver().findElement(By.id("save_collections"));
			btnSave.click();		
			TimeUnit.SECONDS.sleep(2);
		}
	}

	public String addGenericCollection(String genericCollectionName) throws InterruptedException {
		
		showCollectionsAndSettings();
		
		if(isGenericCollectionExist(genericCollectionName))
			return genericCollectionName;
		
		WebElement txtGenericName = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("generic_name")));
		txtGenericName.sendKeys(genericCollectionName);
		
		WebElement btnAddNew = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("biocollections_submit")));
		btnAddNew.click();		
		TimeUnit.SECONDS.sleep(2);
		
		WebElement lblName = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='main-content']/div/h1")));
		String name = lblName.getText();
		return name;
	}
	
	private boolean isGenericCollectionExist(String genericCollectionName) {
		List <WebElement> genericCollectionsList  = getWebDriver().findElements(By.xpath(".//*[@id='generic_list']/div/div"));
		for (int i=1;i<= genericCollectionsList.size();i++) {
			WebElement item = getWebDriver().findElement
					(By.xpath(".//*[@id='generic_list']/div/div["+ i +"]"));
			String name = item.getText();
			if(name.substring(0, name.indexOf(" (")).equals(genericCollectionName))
				return true;
		}
		return false;
	}

	public void deleteGenericCollection(String genericCollectionName) throws InterruptedException {
		
		showCollectionsAndSettings();
		
		List<WebElement> columns = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id(".//*[@id='generic_list']/div")));
		for (int i = 1; i <= columns.size(); i++) {
			
			List<WebElement> rows = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id(".//*[@id='generic_list']/div[" + i+ "]/div")));
			for (int j = 1; j <= rows.size(); j++) {
				WebElement name = getWebDriver().findElement(By.xpath(".//*[@id='generic_list']/div[" + i + "]/div[" + j+ "]"));
				if(name.getText().equals(genericCollectionName)){
					WebElement cross = getWebDriver().findElement(By.xpath(".//*[@id='generic_list']/div[" + i + "]/div[" +j+ "]/a/i"));
					cross.click();
					TimeUnit.SECONDS.sleep(1);
					break;
				}
			}
		}
	}

}
