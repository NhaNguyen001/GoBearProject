package projectName.frontend.action;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.Logs;

import core.action.AppiumAction;
import io.appium.java_client.AppiumDriver;
import projectName.frontend.pageobject.LoginPage;

public class LoginAppAction extends AppiumAction {
	LoginPage loginPage;

	public LoginAppAction(AppiumDriver<WebElement> driver) {
		super(driver);
		loginPage = new LoginPage(driver);
	}

	
	public boolean checkLabel() {

		sleep(5);
		boolean istrue1 = loginPage.btnCompetition != null ? true : false;
		//driver.findElement(By.xpath("//android.widget.TextView[@text='Go to Competitions']"));		
		//driver.findElement(By.xpath("//*[contains(@text,'Go to Competitions')]"));
	
		waitUntilClick(loginPage.btnCompetition );
		if (istrue1)
			loginPage.btnCompetition.click();
		return istrue1;
		
	}
	
	

}