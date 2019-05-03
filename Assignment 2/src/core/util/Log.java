package core.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Log {
	private static final Logger logger = LoggerFactory.getLogger(Log.class);
	private static ExtentReports extentReport;
	private static ExtentTest extentTest;
	private static WebDriver driver;
	private static String screenShotPath;

	public static void logDebug(String msg) {
		logger.debug(msg);
		extentTest.log(LogStatus.INFO, msg);
	}

	public static void logInfo(String msg) {
		logger.info(msg);
		extentTest.log(LogStatus.PASS, msg);
	}

	public static void logWarn(String msg) {
		String str = "<img src='" + takeSnapShot(screenShotPath)
		+ "' style='width:304px;height:228px;'>";
		logger.warn(msg + str );
		extentTest.log(LogStatus.WARNING, msg+ str);
	}

	public static void logError(String msg) {
		String str = "<img src='" + takeSnapShot(screenShotPath)
		+ "' style='width:304px;height:228px;'>";
		logger.error(msg + str);
		extentTest.log(LogStatus.FAIL, msg+ str);
	}

	public static void logTrace(String msg) {
		logger.trace(msg);
		extentTest.log(LogStatus.INFO, msg);
	}

	public static void startOfTC(WebDriver webDriver, String name, String path) {
		logger.info("--------------------Start of " + name + "--------------------");
		extentTest = extentReport.startTest(name);
		driver = webDriver;
		screenShotPath = path + name + "/";
	}

	public static void endOfTC(ITestResult rs) {
		logTestResult(rs);
		extentReport.endTest(extentTest);
		logger.info("--------------------END of " + rs.getName() + "--------------------\n");

	}

	public static void logTestResult(ITestResult result) {
		// set start end
		extentTest.setStartedTime(getTime(result.getStartMillis()));
		extentTest.setEndedTime(getTime(result.getEndMillis()));
		// assign group
		for (String group : result.getMethod().getGroups()) {
			extentTest.assignCategory(group);
		}
		LogStatus stt = convertStatus(result.getStatus());
		String message = "Test " + stt.toString().toLowerCase() + "ed";

		if (result.getThrowable() != null) {
			message = result.getThrowable().getMessage();
		}
		String str = "<img src='" + takeSnapShot(screenShotPath)
				+ "' style='width:304px;height:228px;'>";
		extentTest.log(stt, message + str);
	}

	public static void setExtentReport(ExtentReports rp) {
		extentReport = rp;
	}

	private static Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

	private static LogStatus convertStatus(int stt) {
		switch (stt) {
		case ITestResult.SUCCESS:
			return LogStatus.PASS;
		case ITestResult.FAILURE:
			return LogStatus.FAIL;
		case ITestResult.SKIP:
			return LogStatus.SKIP;
		default:
			return LogStatus.UNKNOWN;
		}
	}

	public static String takeSnapShot(String fileWithPath) {
		String path = fileWithPath;
		try {
			// Convert web driver object to TakeScreenshot

			TakesScreenshot scrShot = ((TakesScreenshot) driver);

			// Call getScreenshotAs method to create image file

			File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);

			// Move image file to new destination

			String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

			File DestFile = new File(fileWithPath + timeStamp + ".png");

			// Copy file at destination

			FileUtils.copyFile(SrcFile, DestFile);
			path = DestFile.getAbsolutePath();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return path;
	}

}
