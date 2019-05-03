package core.action;

import org.testng.AssertJUnit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.LogStatus;

import core.config.BaseConfig;
import core.util.Log;
import io.appium.java_client.android.AndroidDriver;

public class BaseAction {
	public WebDriver driver;
	private WebDriverWait wait;
	JavascriptExecutor executor;
	private SoftAssert softAssert = new SoftAssert();
	public BaseConfig config = new BaseConfig();
	protected int TIME_WAITING = 4;

	public BaseAction(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, config.getTimeOut(), config.getSleepBetweenPoll());
		this.executor = (JavascriptExecutor) driver;
	}

	public BaseAction(AndroidDriver<WebElement> driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, config.getTimeOut(), config.getSleepBetweenPoll());
		this.executor = (JavascriptExecutor) driver;
	}
	
	public WebElement findElement(By by) {
		waitUntilVisible(by);
		return driver.findElement(by);
	}

	public List<WebElement> findElements(By by) {
		waitUntilVisible(by);
		return driver.findElements(by);
	}

	public void click(WebElement ele) {
		waitUntilVisible(ele);
		waitUntilClick(ele);
		String tagName = ele.getTagName();
		String txt = ele.getText().replace("\n", " ");
		try {
			ele.click();
		} catch (Exception e) {
			// e.printStackTrace();
			waitForAngular();
			ele.click();
		}
		Log.logInfo("------Click " + txt + " " + tagName);
	}

	public void click(By by) {
		WebElement ele = findElement(by);
		try {
			click(ele);
		} catch (Exception e) {
			waitForAngular();
			ele = findElement(by);
			click(ele);
		}
	}

	public void clickByText(List<WebElement> locator, String text) {
		int count = 0;
		for (WebElement element : locator) {
			if (element.getText().equals(text)) {
				click(element);
				System.out.println("Click on " + element + " with text " + text);
				count = 1;
				break;
			}
		}
		if (count == 0) {
			throw new NoSuchElementException("Element with text " + text + " is not found");
		}
	}

	public void clickByText(String cssLocator, String text) {
		int count = 0;
		List<WebElement> elements = driver.findElements(By.cssSelector(cssLocator));
		for (WebElement element : elements) {
			if (element.getText().equals(text)) {
				click(element);
				count = 1;
				System.out.println("Click on " + element + " with text " + text);
				break;
			}
		}
		if (count == 0) {
			throw new NoSuchElementException("Element with text " + text + " is not found");
		}
	}

	public void clickByTextContains(List<WebElement> locator, String text) {
		int count = 0;
		for (WebElement element : locator) {
			if (element.getText().contains(text)) {
				click(element);
				System.out.println("Click on " + element + " with text contains " + text);
				count = 1;
				break;
			}
		}
		if (count == 0) {
			throw new NoSuchElementException("Element with text " + text + " is not found");
		}
	}

	public void clickByTextContains(String cssLocator, String text) {
		int count = 0;
		List<WebElement> elements = driver.findElements(By.cssSelector(cssLocator));
		for (WebElement element : elements) {
			if (element.getText().contains(text)) {
				click(element);
				count = 1;
				System.out.println("Click on " + element + " with text contains " + text);
				break;
			}
		}
		if (count == 0) {
			throw new NoSuchElementException("Element with text " + text + " is not found");
		}
	}

	public void type(WebElement ele, String txt) {
		waitUntilVisible(ele);
		try {
			// ele.clear();
			ele.sendKeys(Keys.CONTROL + "a");
			ele.sendKeys(Keys.DELETE);
		} catch (InvalidElementStateException e) {
		}
		if (!txt.equals("")) {
			ele.sendKeys(txt);
			Log.logInfo("------Type " + txt + " to " + ele.getTagName());
		}
		sleep(1);
	}

	public void type(By by, String txt) {
		WebElement ele = findElement(by);
		type(ele, txt);
	}

	public String getText(WebElement ele) {
		Log.logInfo("------Get text of " + ele.getTagName() + " = " + ele.getText());
		return ele.getText();
	}

	public String getText(By by) {
		try {
			WebElement ele = findElement(by);
			return getText(ele);
		} catch (NoSuchElementException e) {
			Log.logInfo("------Get text of non-existing element => return empty string");
			return "";
		}
	}

	public boolean mapSourceInSelect(WebElement element, String[] lstText) {
		List<WebElement> options = getOptions(element);
		if (options.size() != lstText.length) {
			return false;
		}
		for (WebElement webElement : options) {
			for (String text : lstText) {
				if (webElement.getText().compareTo(text) == 0) {
					break;
				}
			}
			return false;
		}
		if (options.size() != lstText.length) {
			return false;
		}

		return false;
	}

	public List<WebElement> getOptions(WebElement element) {
		Select ele = new Select(element);
		return ele.getOptions();
	}

	public void selectByVisibleText(Select ele, String txt) {
		if (!txt.equals("")) {
			ele.selectByVisibleText(txt);
			Log.logInfo("------Select " + txt);
		}
	}

	public void selectByVisibleText(WebElement element, String txt) {
		Select ele = new Select(element);
		selectByVisibleText(ele, txt);
	}

	public void selectByVisibleText(By by, String txt) {
		Select ele = new Select(findElement(by));
		selectByVisibleText(ele, txt);
	}

	public void selectDropdown(WebElement ele, String val) {
		Log.logInfo("---Select dropdown: " + val);
		click(ele);
		click(ele.findElement(By.xpath("./../ul/li[.//text()='" + val + "']")));
	}

	public void selectKendoDropdown(WebElement ele, String val) {
		Log.logInfo("---Select dropdown: " + val);
		click(ele);
		click(By.xpath("//ul[@aria-hidden='false']/li[.//text()='" + val + "']"));
	}

	public WebElement getFirstSelectedOption(WebElement element) {
		Select select = new Select(element);
		WebElement option = select.getFirstSelectedOption();
		return option;
	}

	public void waitForAngular() {
		sleep(1);
		try {
			((JavascriptExecutor) driver).executeScript(
					"angular.element(document.body).injector().get('$browser').notifyWhenNoOutstandingRequests(function(){console.log('Done loading!');})");
		} catch (Exception e) {
			sleep(2);
			// e.printStackTrace();
		}
	}

	public void waitUntilClick(WebElement ele) {
		wait.until(ExpectedConditions.elementToBeClickable(ele));
	}

	public void waitUntilVisible(WebElement ele) {
		wait.until(ExpectedConditions.visibilityOf(ele));
	}
	

	public void waitUntilVisible(WebElement ele,int secondTime) {
		try {
			wait = new WebDriverWait(driver, secondTime);
			wait.until(ExpectedConditions.visibilityOf(ele));
		} catch (TimeoutException e) {
			Log.logInfo(String.format("%s not visible", ele.getTagName()));
			 throw e;
		}
	}


	public void waitUntilVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (TimeoutException e) {
			Log.logInfo(String.format("%s not Visible", by.toString()));
			 throw e;
		}
	}

	public void waitUntilInvisible(WebElement ele) {
		try {
			wait.until(ExpectedConditions.invisibilityOf(ele));
		} catch (TimeoutException e) {
			Log.logInfo(String.format("%s not Invisible", ele.getTagName()));
			 throw e;
		}
	}

	public void waitUntilInvisible(By by) {
		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
		} catch (TimeoutException e) {
			Log.logInfo(String.format("%s not Invisible", by.toString()));
			 throw e;
		}
	}
	public boolean isDisplayed(WebElement e, String name) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, TIME_WAITING);
			wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(e)));
			if (e != null) {
				return e.isDisplayed();
			} else
				return false;
		} catch (Exception ex) {
			logWarn("Element/s:" + name + " is not displaying ");
			return false;
		}
	}
	public boolean isDisplayed(WebElement ele) {
		try {
			waitUntilVisible(ele);
			return true;
		} catch (TimeoutException e) {
			return false;
		}
	}

	public boolean isDisplayed(By by) {
		try {
			waitUntilVisible(by);
			return true;
		} catch (TimeoutException e) {
			return false;
		}
	}

	public boolean isNotDisplayed(WebElement ele) {
		try {
			waitUntilInvisible(ele);
			return true;
		} catch (TimeoutException e) {
			return false;
		}
	}

	public boolean isNotDisplayed(By by) {
		try {
			waitUntilInvisible(by);
			return true;
		} catch (TimeoutException e) {
			return false;
		}
	}

	public void sleep(Integer s) {
		try {
			Log.logInfo("------Sleep " + s + " mns -----");
			Thread.sleep(s * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void scrolltoElement(WebElement elm) {
		JavascriptExecutor js = ((JavascriptExecutor) driver);
		js.executeScript("window.scrollTo(0," + elm.getLocation().y + ")");
	}
	
	public void confirmPopup(boolean selection) {
		wait.until(ExpectedConditions.alertIsPresent());
		Alert alert = driver.switchTo().alert();
		if (selection) {
			alert.accept();
			Log.logInfo("------Accept alert popup");
		} else {
			alert.dismiss();
			Log.logInfo("------Dismiss alert popup");
		}
	}

	public void scrollIntoView(WebElement ele) {
		executor.executeScript("arguments[0].scrollIntoView();", ele);
	}

	public void executeScript(String script) {
		executor.executeScript(script);
	}

	public void typeNumbericBox(WebElement eleClicked, WebElement eleTyped, String txt) {
		Log.logInfo("---Type to numbernic box: ");
		click(eleClicked);
		waitForAngular();
		type(eleTyped, txt);
	}

	public void checkCheckbox(WebElement ele, boolean state) {
		if (state) {
			if (!ele.isSelected()) {
				ele.click();
			}
			Log.logInfo("------Check checkbox");
		} else {
			if (ele.isSelected()) {
				ele.click();
			}
			Log.logInfo("------Uncheck checkbox");
		}
		waitForAngular();
	}

	public void checkCheckbox(By by, boolean state) {
		WebElement ele = findElement(by);
		checkCheckbox(ele, state);
	}

	public String getCurrentTimeString() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(new Date());
	}

	public String randomText(String text) {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String randomText = text + dateFormat.format(date);
		return randomText;
	}

	// Check Image
	private int invalidImageCount;

	public void checkImage() {
		try {
			invalidImageCount = 0;
			List<WebElement> imagesList = driver.findElements(By.tagName("img"));
			Log.logInfo("Total no. of images are " + imagesList.size());
			for (WebElement imgElement : imagesList) {
				if (imgElement != null) {
					verifyimageActive(imgElement);
				}
			}
			Log.logInfo("Total no. of invalid images are " + invalidImageCount);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		softAssert.assertAll();
	}

	public void verifyimageActive(WebElement imgElement) {
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(imgElement.getAttribute("src"));
			String src = imgElement.getAttribute("src").toString();
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() != 200)
				invalidImageCount++;
			softAssert.assertTrue(response.getStatusLine().getStatusCode() == 200,
					"Image(s)is broken: " + src + " - Code: " + response.getStatusLine().getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Check Link
	public void checkLink() {

		List<WebElement> total_links = driver.findElements(By.tagName("a"));
		System.out.println("Total Number of links found on page = " + total_links.size());

		boolean isValid = false;
		for (int i = 0; i < total_links.size(); i++) {
			String url = total_links.get(i).getAttribute("href");
			int invalid = 0;
			if (url != null) {

				// Call getResponseCode function for each URL to check response
				// code.
				isValid = getResponseCode(url);

				// Print message based on value of isValid which Is returned by
				// getResponseCode function.
				if (isValid) {
					Log.logInfo("Valid Link:" + url);
				} else {
					Log.logInfo("Broken Link ------> " + url);
					invalid++;
				}
			} else {
				// If <a> tag do not contain href attribute and value then print
				// this message
				Log.logInfo("String null");
				continue;
			}
			softAssert.assertTrue(isValid, "Link(s)is broken: " + url);
		}
		softAssert.assertAll();
	}

	// Function to get response code of link URL.
	// Link URL Is valid If found response code = 200.
	// Link URL Is Invalid If found response code = 404 or 505.
	public static boolean getResponseCode(String chkurl) {
		boolean validResponse = false;
		try {
			// Get response code of URL
			HttpResponse urlresp = new DefaultHttpClient().execute(new HttpGet(chkurl));
			int resp_Code = urlresp.getStatusLine().getStatusCode();
			Log.logInfo("Response Code Is : " + resp_Code);
			if ((resp_Code == 404) || (resp_Code == 505)) {
				validResponse = false;
			} else {
				validResponse = true;
			}
		} catch (Exception e) {

		}
		return validResponse;
	}

	public void playVideo() {
		WebElement video = driver.findElement(By.tagName("video"));

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("argument[0].play();", video);
		Log.logInfo("------Play Video");
	}

	public void playVideo(WebElement video) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("argument[0].play();", video);
		Log.logInfo("------Play Video");
	}

	public void playVideo(By by) {
		WebElement video = driver.findElement(by);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("argument[0].play();", video);
		Log.logInfo("------Play Video");
	}

	public void storeCookie() {
		File file = new File("Cookies.data");
		try {
			file.delete();
			file.createNewFile();

			FileWriter fileWrite = new FileWriter(file);
			BufferedWriter Bwrite = new BufferedWriter(fileWrite);

			// loop to get the cookie information
			for (Cookie ck : driver.manage().getCookies()) {
				Bwrite.write((ck.getName() + ";" + ck.getValue() + ";" + ck.getDomain() + ";" + ck.getPath() + ";"
						+ ck.getExpiry() + ";" + ck.isSecure()));
				Bwrite.newLine();
			}
			Bwrite.flush();
			Bwrite.close();
			fileWrite.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loadCookiesData() {
		try {
			File file = new File("Cookies.data");
			FileReader fileReader = new FileReader(file);
			BufferedReader Buffreader = new BufferedReader(fileReader);
			String strline;
			while ((strline = Buffreader.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(strline, ";");
				while (token.hasMoreTokens()) {
					String name = token.nextToken();
					String value = token.nextToken();
					String domain = token.nextToken();
					String path = token.nextToken();
					Date expiry = null;

					String val;
					if (!(val = token.nextToken()).equals("null")) {
						expiry = new Date(val);
					}
					Boolean isSecure = new Boolean(token.nextToken()).booleanValue();
					Cookie ck = new Cookie(name, value, domain, path, expiry, isSecure);
					driver.manage().addCookie(ck);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String getCurrentLanguage() {
		return driver.manage().getCookieNamed("wgclanguage").getValue().toString();
	}

	public void clearBrowserCache() {
		driver.manage().deleteAllCookies();
		driver.navigate().refresh();
	}

	public void doubleClick(WebElement element) {
		try {
			Actions action = new Actions(driver);
			action.moveToElement(element).doubleClick().perform();
			Log.logInfo("doubleClick for " + element.getTagName());
		} catch (Exception e) {
			Log.logWarn("Cannot doubleClick for " + element.getTagName());
		}
	}

	public void addTabPage() {
		((JavascriptExecutor) driver).executeScript("window.open();");
	}

	public void switchToPage(int index, String link) {
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(index)); // switches to new tab
		if (!link.isEmpty())
			driver.get(link);
	}

	public void assertResult(boolean i, String msgFail, String msgPass) {
		if (!i) {
			assertFail(msgFail);
		} else {
			logPass(msgPass);
		}
	}

	public void assertFail(String msgFail) {
		logFail(msgFail);
		AssertJUnit.fail(msgFail);
	}
	

	public void logPass(String msg) {
		Log.logInfo("-- PASSED: " + msg + " --");
		System.out.println("-- PASSED: " + msg + " --");
	}

	public void logFail(String msg) {
		Log.logError("-- FAILED: " + msg + " --");
		System.out.println("-- FAILED: " + msg + " --");
	}

	public void logWarn(String msg) {
		Log.logInfo("-- WARNING: " + msg + " --");
		System.out.println("-- WARNING: " + msg + " --");
	}

	public void logStep(String msg) {
		Log.logInfo("-- INFO: " + msg + " --");
		System.out.println("-- STEP: " + msg + " --");
	}
	public void waitForVisible(WebElement el, int time) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, TIME_WAITING);
			wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(el)));
			el.isDisplayed();
		} catch (NoSuchElementException ex) {
			logWarn("Element/s:" + getNameOfElement(el) + " is not visible");
		} catch (Exception ex) {
			logWarn("Element/s:" + getNameOfElement(el) + " is not visible");
		}

	}
	public String getNameOfElement(WebElement e) {
		String nameElement = "";
		try {

			if (e.getAttribute("id") != null && !e.getAttribute("id").isEmpty())
				nameElement = nameElement + " " + e.getAttribute("id");

			if (e.getAttribute("name") != null && !e.getAttribute("name").isEmpty())
				nameElement = nameElement + " " + e.getAttribute("name");
			if (e.getTagName() != null && !e.getTagName().isEmpty()) {
				nameElement = e.getTagName();
			}
			nameElement = nameElement + " " + e.getAttribute("class");
			return nameElement + " " + e;
		} catch (Exception ex) {
			return nameElement + e;
		}
	}

}
