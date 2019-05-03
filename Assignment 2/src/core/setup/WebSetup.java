package core.setup;

import java.io.File;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.relevantcodes.extentreports.ExtentReports;

import core.config.BaseConfig;
import core.config.SystemConfig;
import core.driver.DriverFactory;
import core.util.Log;

public class WebSetup {
	// driver
	public WebDriver driver;

	// report
	protected ExtentReports extentReport;

	public SystemConfig config = new SystemConfig();
	public String reportPath = config.getReportFolderPath();
	public String fileReport = config.getFileReportPath();
	
	@BeforeSuite
	public void beforeSuite() {
		extentReport = new ExtentReports(reportPath + fileReport, false);
		extentReport.loadConfig(new File(config.getExtentConfig()));
		Log.setExtentReport(extentReport);
	}

	@AfterSuite
	public void afterSuite() {
		extentReport.flush();
		extentReport.close();
	}

	@BeforeMethod
	public void setUp(Method method) throws Exception {
		String TC_name = method.getName();
		driver = new DriverFactory().getWebDriver(
				config.getProperty("BROWSER"), config.getProperty("APP_URL"));
		Log.startOfTC(
				driver,
				TC_name,
				BaseConfig.currentProjectPath
						+ config.getProperty("SCREENSHOT"));
		Log.logInfo("Openning " + config.getProperty("APP_URL") + " with "
				+ config.getProperty("BROWSER") + " browser");
		declareAction();
	}

	@AfterMethod
	public void tearDown(ITestResult result) throws Exception {
		Log.endOfTC(result);
		driver.quit();
	}

	public void declareAction() {

	}
}
