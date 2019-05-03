package core.driver;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import core.config.BaseConfig;

public class DriverFactory {
	private WebDriver webDriver;
	private static ThreadLocal<WebDriver> localThread = new ThreadLocal<WebDriver>();
	private BaseConfig config = new BaseConfig();

	public WebDriver getWebDriver(String browser, String url) {
		switch (browser) {
		case "chrome":
		case "gc":
		default:
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-extensions");
			System.setProperty("webdriver.chrome.driver", config.getChromeDriver());
			webDriver = new ChromeDriver(options);
			break;
		case "firefox":
		case "ff":
			webDriver = new FirefoxDriver();
			break;
		case "safari":
			webDriver = new SafariDriver();
			break;
		}
		webDriver.manage().timeouts().implicitlyWait(config.getImplicitWait(), TimeUnit.SECONDS);
		webDriver.manage().timeouts().pageLoadTimeout(config.getPayloadTimeout(), TimeUnit.SECONDS);
		webDriver.manage().window().maximize();
		localThread.set(webDriver);
		webDriver = localThread.get();
		webDriver.get(url);
		//Log.logInfo("Openning " + url + " with " + browser + " browser");
		return webDriver;
	}

	public void removeWebDriver() {
		localThread.get().quit();
		localThread.remove();
	}
}
