package core.pageObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class BaseObjectPage {

	WebDriver driver;

	public BaseObjectPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
		this.driver = driver;
	}
}
