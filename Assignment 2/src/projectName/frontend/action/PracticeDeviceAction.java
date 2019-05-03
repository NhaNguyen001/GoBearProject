package projectName.frontend.action;

import org.openqa.selenium.WebElement;

import core.action.AppiumAction;
import io.appium.java_client.AppiumDriver;
import projectName.frontend.pageobject.LoginPage;

public class PracticeDeviceAction extends AppiumAction {
	LoginPage loginPage;

	public PracticeDeviceAction(AppiumDriver<WebElement> driver) {
		super(driver);
		loginPage = new LoginPage(driver);
	}

	
	public boolean checkLabelWelcome() {

		sleep(5);
		boolean istrue1 = loginPage.labelWelcomeToApp != null ? true : false;
		//driver.findElement(By.xpath("//android.widget.TextView[@text='Go to Competitions']"));		
		//driver.findElement(By.xpath("//*[contains(@text,'Go to Competitions')]"));
	
		//waitUntilClick(loginPage.btnCompetition );
		
		
		return istrue1;
	}
	
	public boolean checkPrivacyPolicy() {
		boolean istrue = loginPage.linkPrivacyPolicy  != null ? true : false;
		waitUntilClick(loginPage.linkPrivacyPolicy );
		if (istrue)
			//loginPage.linkPrivacyPolicy.click();
			click(loginPage.linkPrivacyPolicy);
		return istrue;
	}
	public boolean checkTermsOfUse() {
		boolean istrue = loginPage.linkTermsOfUse  != null ? true : false;
		waitUntilClick(loginPage.linkTermsOfUse );
		if (istrue)
			click(loginPage.linkTermsOfUse);
		return istrue;
		
	}
	
	public void CloseTermOfUse() {
		waitUntilClick(loginPage.labelTermsOfUse);
		click(loginPage.labelTermsOfUse);
		boolean istrue = loginPage.labelWelcomeToApp  != null ? true : false;
		if (istrue)
			logPass("Close Terms of use");
		else 
			logFail("Not Close Terms of use");
	}
}