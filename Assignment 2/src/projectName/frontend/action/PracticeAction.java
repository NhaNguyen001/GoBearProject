package projectName.frontend.action;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;
import core.action.BaseAction;
import core.util.Log;
import projectName.frontend.pageobject.LoginPage;

public class PracticeAction extends BaseAction {
	LoginPage loginPage;

	public PracticeAction(WebDriver driver) {
		super(driver);
		loginPage = new LoginPage(driver);
		
	}
	
	public void searchInGoogle(String sText) throws InterruptedException {
		type(loginPage.textboxSearch, sText);
		
		
		logStep("Search with :"+sText);
	}
	public void gotoMainPage() throws InterruptedException {
		 //WebDriver driver = new FirefoxDriver();

	        // And now use this to visit Google
	        driver.get("http://www.google.com");
	        
	        
	        driver.findElement(By.cssSelector("input.gLFyf.gsfi")).sendKeys("test");
	        
	        // Alternatively the same thing can be done like this
	        // driver.navigate().to("http://www.google.com");

	        // Find the text input element by its name
	        WebElement element = driver.findElement(By.name("q"));

	        // Enter something to search for
	        element.sendKeys("Cheese!");

	        // Now submit the form. WebDriver will find the form for us from the element
	        element.submit();

	        // Check the title of the page
	        System.out.println("Page title is: " + driver.getTitle());
	        
	        // Google's search is rendered dynamically with JavaScript.
	        // Wait for the page to load, timeout after 10 seconds
//	        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
//	            public Boolean apply(WebDriver d) {
//	                return d.getTitle().toLowerCase().startsWith("cheese!");
//	            }
//	        });

	        // Should see: "cheese! - Google Search"
	        System.out.println("Page title is: " + driver.getTitle());
	        
	        //Close the browser
	        driver.quit();
	}
}