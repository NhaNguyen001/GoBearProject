package core.setup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITest;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.relevantcodes.extentreports.ExtentReports;

import core.config.BaseConfig;
import core.config.DataConfig.DeviceMode;
import core.config.DataConfig.PlatformName;
import core.config.SystemConfig;
import core.config.TestDataConfig;
import core.util.Log;
import core.util.UnzipUtility;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

public class DeviceSetup implements ITest {

	protected PlatformName platform = PlatformName.Android;
	public SystemConfig config = new SystemConfig();
	public AppiumDriver<WebElement> driver;
	public WebDriverWait wait;
	public String current_project_path = System.getProperty("user.dir");
	public WebDriver webdriver;
	Process p;

	public String reportPath = config.getReportFolderPath();
	public static ExtentReports extent;
	public String fileReport = config.getFileReportPath();
	public TestDataConfig testDataConfig = new TestDataConfig();
	private String testName;
	String extendFolderPath = config.getExtendFolderPath();
	String appLink = config.getAppLink();

	String zipFilePath = config.getProperty("zipFilePath");
	String destDirectory = config.getProperty("destDirectory");

	@BeforeSuite(alwaysRun = true)
	public void startSuite() throws InterruptedException, IOException {
		setPlatform();
		if (PlatformName.iOS.equals(platform)) {
			startSuite_iOS();
		} else if (PlatformName.Android.equals(platform)) {
			startSuite_Android();
		} else {
			throw new IOException("cannot support");
		}
	}

	private void setPlatform() {
		platform = config.getProperty("deviceMode").equals(String.valueOf(DeviceMode.Genimotion.getValue()))
				|| config.getProperty("deviceMode").equals(String.valueOf(DeviceMode.Android.getValue()))
						? PlatformName.Android
						: PlatformName.iOS;
	}

	private void setupExtentReport() {
		
		
		extent = new ExtentReports(reportPath + fileReport, false);
		extent.loadConfig(new File(config.getExtentConfig()));
		extent.addSystemInfo("Device Name", config.getProperty("deviceName"));
		extent.addSystemInfo("IOS version", config.getProperty("platformVersion"));
		extent.addSystemInfo("Appium version", config.getProperty("appiumVersion"));
		Log.setExtentReport(extent);
	}

	public void startSuite_iOS() throws InterruptedException, IOException {

		setupExtentReport();
	}

	public void startSuite_Android() throws InterruptedException, IOException {
		removeAppsBeforeInstall();
		/*
		 * deleteFile(currentProjectPath + extendFolderPath);
		 * downloadAPKFile(extendFolderPath, appLink);
		 */

		stopAppium();

		if (config.getProperty("deviceMode").equals(String.valueOf(DeviceMode.Genimotion.getValue()))) {
			stopGenymotion();
			startGenymotion();
			installAndroidWebView();
		}

		deleteFile(reportPath);
		setupExtentReport();
		startAppium();

		Thread.sleep(5000);
	}

	@BeforeMethod
	public void setUp(Method method) throws Exception {
		if (PlatformName.iOS.equals(platform)) {
			setUp_iOS(method);
		} else if (PlatformName.Android.equals(platform)) {
			setUp_Android(method);
		} else {
			throw new IOException("cannot support");
		}
	}

	public void setUp_iOS(Method method) throws Exception {

		try {
			closeAllWindow();
			startAppium();

			DesiredCapabilities capabilities = initCapabilitiesIOS();
			setTestName(method.getName());
			for (int i = 0; i < 60; i++) {
				try {
					driver = new IOSDriver<WebElement>(new URL(config.getProperty("url")), capabilities);
					Thread.sleep(5000);
				} catch (Throwable e) {
					System.out.println("Catch : " + e.getMessage());
					closeAllWindow();
					startAppium();
					driver = new IOSDriver<WebElement>(new URL(config.getProperty("url")), capabilities);
					Thread.sleep(5000);
				}
				if (driver != null) {
					break;
				}
				Thread.sleep(1000);
			}

			driver.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			String s = method.getName();
			System.out.println("START TESTCASE: -------------" + s + "-------------");
			for (int i = 0; i < 60; i++) {
				try {
					if (driver.getContextHandles().contains("NATIVE_APP")) {
						break;
					} else
						Thread.sleep(1000);
				} catch (Exception e) {
					Thread.sleep(1000);
				}
			}
			Thread.sleep(5000);
			driver.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			for (int i = 0; i < 30; i++) {
				try {
					if (driver.getContextHandles().contains((String) driver.getContextHandles().toArray()[0])
							&& driver.getContextHandles().contains((String) driver.getContextHandles().toArray()[1])) {

						break;
					}
					Thread.sleep(1000);
				} catch (Exception e) {
					System.out.println(i);
					System.out.println(driver.getContext());
				}
			}

			startTestCase(method);

		} catch (Throwable e) {
			System.out.println("--Step: Have some Problem with New Session  " + e.getMessage());
			setUp(method);
		}
	}

	private void startTestCase(Method method) {
		String TC_name = method.getName();
		System.out.println("START TESTCASE: -------------" + TC_name + "-------------");
		// driver.context((String) driver.getContextHandles().toArray()[1]);

		Log.startOfTC(driver, TC_name, BaseConfig.currentProjectPath + config.getProperty("SCREENSHOT"));
		Log.logInfo("-- STEP: App is opened successfuly--");

		declareAction();
	}

	private DesiredCapabilities initCapabilitiesIOS() throws InterruptedException {
		File app = new File(current_project_path + "/File/instaGUARD_Test.zip");
		Thread.sleep(1000);
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platformVersion", config.getProperty("platformVersion"));
		capabilities.setCapability("platformName", PlatformName.iOS);
		capabilities.setCapability("autoAcceptAlerts", true);
		capabilities.setCapability("sendKeyStrategy", "setValue");
		capabilities.setCapability("nativeWebTap", true);
		capabilities.setCapability("deviceName", config.getProperty("deviceName"));
		capabilities.setCapability("launchTimeout", "120000000");
		// capabilities.setCapability("locationServicesEnabled", true);
		// capabilities.setCapability("locationServicesAuthorized", true);
		capabilities.setCapability("newCommandTimeout", 100000 * 5);
		capabilities.setCapability("app", app.getAbsolutePath());
		capabilities.setCapability("fullReset", false);
		return capabilities;
	}

	public void setUp_Android(Method method) throws Exception {

		setTestName(method.getName());

		
		DesiredCapabilities capabilities = initCapabilitiesAndroid();
		try {
			for (int i = 0; i < 10; i++) {
				try {

					driver = new AndroidDriver<WebElement>(new URL(config.getProperty("url")), capabilities);
					Thread.sleep(5000);
				} catch (Throwable ex) {
					stopAppium();
					/*
					 * StopGenymotion(); StartGenymotion();
					 */
					startAppium();
					driver = new AndroidDriver<WebElement>(new URL(config.getProperty("url")), capabilities);
					Thread.sleep(5000);
				}
				if (driver != null) {
					break;
				}
			}

			driver.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			for (int i = 0; i < 30; i++) {
				try {
					if (driver.getContextHandles().contains("NATIVE_APP")
							&& driver.getContextHandles().contains(config.getContext())) {
						break;
					}
					Thread.sleep(1000);
				} catch (Exception e) {
				}
			}
		/*// added by Kathy
		try {
			DesiredCapabilities caps = new DesiredCapabilities();
	        caps.setCapability("deviceName", "Galaxy Nexus API 24");
	        caps.setCapability("udid", "emulator-5554"); //DeviceId from "adb devices" command
	        caps.setCapability("platformName", "Android");
	        caps.setCapability("platformVersion", "7.0");
	        caps.setCapability("skipUnlock","true");
	        caps.setCapability("appPackage", "com.isinolsun.app");
	        caps.setCapability("appActivity","com.isinolsun.app.activities.SplashActivity");
	        caps.setCapability("noReset","false");
	        driver = new AndroidDriver<WebElement>(new URL("http://127.0.0.1:4723/wd/hub"),caps);
	        wait = new WebDriverWait(driver, 10);
	        */
			startTestCase(method);
			declareAction();
		} catch (Throwable e) {
			setUp(method);
		}
	}

	private DesiredCapabilities initCapabilitiesAndroid() {
		/* removedInstallNewDeivce(zipFilePath, destDirectory); */
		// String app_path =
		// "http://apps.soxes-projects.ch/alarm/android_build/staging/instaGUARD_Staging.apk";

		File file = new File(config.getAppPath());
		DesiredCapabilities capabilities = new DesiredCapabilities();

		capabilities.setCapability("platformVersion", config.getProperty("platformVersion"));
		capabilities.setCapability("deviceName", config.getProperty("deviceName"));
		capabilities.setCapability("platformName", PlatformName.Android);
		if (file.exists()) {
			capabilities.setCapability("app", file.getAbsolutePath());
		} else {
			capabilities.setCapability("app", appLink);
		}
		capabilities.setCapability("newCommandTimeout", 72000);
		capabilities.setCapability("appWaitDuration", 60000);
		capabilities.setCapability("deviceReadyTimeout", 120 * 5);
		capabilities.setCapability("appPackage", config.getProperty("appPackage"));
		capabilities.setCapability("appActivity", ".MainActivity");
		capabilities.setCapability("automationName", "UiAutomator2");
		capabilities.setCapability("--session-override", true);
		return capabilities;
	}

	@AfterMethod
	public void tearDown(ITestResult result) throws Exception {
		Log.endOfTC(result);
		driver.quit();
		stopAppium();
	}

	@Override
	public String getTestName() {
		return testName;
	}

	public void setTestName(String tn) {
		testName = tn;
	}

	@AfterSuite(alwaysRun = true)

	public void stopSuite() throws InterruptedException, IOException {
		if (PlatformName.iOS.equals(platform)) {
			stopSuite_iOS();
		} else if (PlatformName.Android.equals(platform)) {
			stopSuite_Android();
		} else {
			throw new IOException("cannot support");
		}
	}

	public void stopSuite_iOS() throws InterruptedException, IOException {

		Thread.sleep(2000);
		System.out.println("*** Stopped Emulator & Appium Server ***");
		extent.flush();
		extent.close();

	}

	public void stopSuite_Android() throws InterruptedException, IOException {
		if (config.getProperty("deviceMode").equals(String.valueOf(DeviceMode.Genimotion.getValue()))) {
			stopGenymotion();
		}
		Thread.sleep(2000);
		System.out.println("*** Stopped Emulator & Appium Server ***");
		extent.flush();
		extent.close();
		copyFile(reportPath + fileReport, config.getProperty("localReportPath") + fileReport);
	}

	public void installAndroidWebView() throws IOException {
		try {
			Runtime.getRuntime().exec("cmd.exe  /c adb install " + config.getWebViewAndroidPath());
			Thread.sleep(10000);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public void captureScreenShot(String testcase) throws IOException {
		driver.context("NATIVE_APP");
		String screenshotPath = config.getScreenshotPath(testcase);
		try {
			File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(file, new File(screenshotPath));
			Log.logInfo("Screenshot saved as: " + screenshotPath);
		} catch (Exception e) {
			Log.logInfo("FAILED: Could not capture screen!");
		}
	}

	public String captureScreen(String testcase) {
		driver.context("NATIVE_APP");
		String screenshotPath = config.getScreenshotPath(testcase);
		try {
			File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(file, new File(screenshotPath));
			System.out.println("Screenshot saved as: " + screenshotPath);
		} catch (Exception e) {
			System.out.println("FAILED: Could not capture screen!");
		}
		return screenshotPath;
	}

	@SuppressWarnings("unused")
	public static void copyFile(String from, String to) {
		InputStream inStream = null;
		FileOutputStream outStream = null;

		try {

			File afile = new File(from);
			File bfile = new File(to);

			inStream = new FileInputStream(afile);
			outStream = new FileOutputStream(bfile);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, length);
			}
			inStream.close();
			outStream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void deleteFile(String path) {
		File file = new File(path);
		String[] myFiles;
		if (file.isDirectory()) {
			myFiles = file.list();
			for (int i = 0; i < myFiles.length; i++) {
				File myFile = new File(file, myFiles[i]);
				myFile.delete();
			}
		}
	}

	public void startAppium() throws IOException {
		if (PlatformName.iOS.equals(platform)) {
			startAppiumiOS();
		} else if (PlatformName.Android.equals(platform)) {
			startAppiumAndroid();
		} else {
			throw new IOException("cannot support");
		}
	}

	public void startAppiumiOS() throws IOException {

		try {
			startAppiumServer(config.getProperty("port"));
			System.out.println("--- Start Appium Server ---");
		} catch (Throwable e) {
			System.out.println(e.getMessage());
		}

	}

	public void startAppiumAndroid() throws IOException {
		try {
			Runtime.getRuntime().exec("cmd.exe /c start " + config.getAppiumServerPath());
			Thread.sleep(10000);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public static void stopAppium() throws IOException {
		try {
			Runtime.getRuntime().exec("taskkill /FI \"WINDOWTITLE eq Start_Genymotion_Device.bat*\" /F");
			Runtime.getRuntime().exec("taskkill /FI \"WINDOWTITLE eq Select Start_Genymotion_Device.bat*\" /F");
			Runtime.getRuntime().exec("taskkill /FI \"WINDOWTITLE eq start_appium_server.bat*\" /F");
			Runtime.getRuntime().exec("taskkill /FI \"WINDOWTITLE eq Select start_appium_server.bat*\" /F");
			Runtime.getRuntime().exec("taskkill /f /im node.exe");
			Runtime.getRuntime()
					.exec("WMIC PROCESS WHERE \"COMMANDLINE LIKE '%start_appium_server.bat%'\" CALL TERMINATE");
			Thread.sleep(1000);

			/*
			 * CommandLine command = new CommandLine("cmd"); command.addArgument("/c");
			 * command.addArgument("taskkill"); command.addArgument("/F");
			 * command.addArgument("/IM"); command.addArgument("node.exe");
			 * 
			 * DefaultExecuteResultHandler resultHandler = new
			 * DefaultExecuteResultHandler(); DefaultExecutor executor = new
			 * DefaultExecutor(); executor.setExitValue(1);
			 * 
			 * try { executor.execute(command, resultHandler); } catch (IOException e) {
			 * e.printStackTrace(); }
			 */
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void closeNodeWindow() throws IOException {
		try {
			String tellCommand = "tell application \"Terminal\" to close (every window whose name contains \"node\")";
			String[] command = { "osascript", "-e", tellCommand };

			ProcessBuilder pBuilder = new ProcessBuilder(command);
			pBuilder.start();
			Thread.sleep(5000);
			// System.out.println("--- Stop Appium Server ---");
		} catch (Throwable e) {
			System.out.println(e.getMessage());
		}

	}

	public void startGenymotion() {
		/*
		 * System.out.println("*** Start Genymotion Device ***"); try {
		 * Runtime.getRuntime().exec("cmd.exe /c start " + config.getEmulator());
		 * Thread.sleep(60000); Runtime.getRuntime().
		 * exec("taskkill /FI \"WINDOWTITLE eq Start_Genymotion_Device.bat*\" /F");
		 * 
		 * } catch (Exception e) { System.out.println(e.getMessage()); }
		 */
	}

	public static void stopGenymotion() throws IOException {
		/*
		 * try { Runtime.getRuntime().exec("taskkill /f /im genymotion.exe");
		 * Runtime.getRuntime().exec("taskkill /f /im player.exe"); } catch (Exception
		 * e) { System.out.println(e.getMessage()); }
		 */
	}

	public static void stopVirtualBox() throws IOException {
		try {
			stopAppium();
			stopGenymotion();
			Runtime.getRuntime().exec("taskkill /f /im adb.exe");
			Runtime.getRuntime().exec("taskkill /f /im VBoxHeadless.exe");
			Runtime.getRuntime().exec("taskkill /f /im VBoxNetDHCP.exe");
			Thread.sleep(5000);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	private void downloadAPKFile(String downloadFilepath, String url) throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", config.getProperty("chromeDriver"));

		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", downloadFilepath);
		ChromeOptions options = new ChromeOptions();
		HashMap<String, Object> chromeOptionsMap = new HashMap<String, Object>();
		options.setExperimentalOption("prefs", chromePrefs);
		options.addArguments("--test-type");

		DesiredCapabilities cap = DesiredCapabilities.chrome();
		cap.setCapability(ChromeOptions.CAPABILITY, chromeOptionsMap);
		cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		cap.setCapability(ChromeOptions.CAPABILITY, options);
		WebDriver driver = new ChromeDriver(cap);
		driver.get(url);
		if (isExists(downloadFilepath)) {
			driver.quit();
		}
	}

	public void declareAction() {

	}

	public boolean isExists(String path) throws InterruptedException {
		boolean flag = false;
		File file = new File(path + config.getProperty("installFile"));
		while (!file.exists()) {
			file = new File(path + config.getProperty("installFile"));
			flag = true;
			Thread.sleep(1000);
		}
		System.out.println("*** IG App is downloaded to local ***");
		return flag;
	}

	public void removedInstallNewDeivce(String zipFilePath, String destDirectory) throws IOException {
		try {
			stopVirtualBox();
			File dir = new File(destDirectory + config.getProperty("deviceName"));
			if (deleteDirectory(dir)) {
				UnzipUtility.unzip(zipFilePath, destDirectory);
				Thread.sleep(5000);
				System.out.println("*** Install new " + config.getProperty("deviceName") + " Device ***");
			} else if (!dir.exists()) {
				UnzipUtility.unzip(zipFilePath, destDirectory);
				Thread.sleep(5000);
				System.out.println("*** Install new " + config.getProperty("deviceName") + " Device ***");
			} else {
				System.out.println("***Can NOT Install new " + config.getProperty("deviceName") + " Device ***");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public static boolean deleteDirectory(File dir) {
		if (dir.isDirectory()) {
			File[] children = dir.listFiles();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDirectory(children[i]);
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	private void removeAppsBeforeInstall() {
		try {
			Runtime.getRuntime().exec("cmd.exe adb uninstall io.appium.settings");
			Runtime.getRuntime().exec("cmd.exe adb uninstall io.appium.unlock");
			Runtime.getRuntime().exec("cmd.exe adb uninstall " + config.getProperty("appPackage"));
			Thread.sleep(1000);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void closeInstrumentsWindow() throws IOException {
		try {
			String tellCommand = "tell application \"Terminal\" to close (every window whose name contains \"instruments\")";
			String[] command = { "osascript", "-e", tellCommand };

			ProcessBuilder pBuilder = new ProcessBuilder(command);
			pBuilder.start();
			System.out.println("--- Close instruments window ---");
		} catch (Throwable e) {
			System.out.println(e.getMessage());
		}

	}

	public void closeBashWindow() throws IOException {
		try {
			String tellCommand = "tell application \"Terminal\" to close (every window whose name contains \"bash\")";
			String[] command = { "osascript", "-e", tellCommand };

			ProcessBuilder pBuilder = new ProcessBuilder(command);
			pBuilder.start();
			Thread.sleep(5000);
			System.out.println("--- CLose bash window ---");
		} catch (Throwable e) {
			System.out.println(e.getMessage());
		}
	}

	public void closeAllWindow() throws IOException {

		closeNodeWindow();
		closeBashWindow();
		closeInstrumentsWindow();
		System.out.println("--- Stop Appium Server ---");

	}

	/**
	 * To start Appium server
	 * 
	 * @param port
	 *            - port number on which the server starts
	 * @throws Exception
	 */
	private void startAppiumServer(String port) throws Exception {

		if (!isAppiumServerRunning(port)) {

			try {
				AppiumDriverLocalService service = AppiumDriverLocalService.buildService(
						new AppiumServiceBuilder().usingDriverExecutable(new File(config.getProperty("pathNode")))
								.withAppiumJS(new File(config.getProperty("pathAppium")))
								.withIPAddress(config.getProperty("ipAddress")).usingPort(Integer.valueOf(port)));
				service.start();
			} catch (Exception serverNotStarted) {

				throw new Exception(serverNotStarted.getMessage());
			}
		} else {
			System.out.println("Appium still running");
		}

	}

	/**
	 * To check if Appium server is already up and running on the desired port
	 * 
	 * @param port
	 *            desired port for server to start
	 * @return true if server running, else false.
	 * @throws Exception
	 */
	private boolean isAppiumServerRunning(String port) throws Exception {

		String checkCommand[] = new String[] { "sh", "-c", String.format("lsof -P | grep :%s", port) };
		if (runCommandAndWaitToComplete(checkCommand).equals("")) {

			return false;
		} else {

			return true;
		}
	}

	/**
	 * Helper method to run an arbitrary command-line 'command', waits for few
	 * seconds after command executes
	 * 
	 * @param command
	 *            string that will be sent to command-line
	 * @return The first line response after executing command. (can be used to
	 *         verify)
	 */
	public String runCMD(String command) throws Exception {

		try {
			Process process = Runtime.getRuntime().exec((command));
			process.waitFor(10, TimeUnit.SECONDS);
			BufferedReader response = new BufferedReader(new InputStreamReader(process.getInputStream()));
			return response.readLine();
		} catch (Exception e) {

			throw new Exception(e.getMessage());
		}
	}

	/**
	 * To execute a terminal command, and get the complete log response.
	 *
	 * @param command
	 *            - command we intend to execute via terminal
	 * @return - the execution log. We can scan this to check if the command
	 *         executed was a success or failure.
	 * @throws Exception
	 */
	public String runCommandAndWaitToComplete(String[] command) throws Exception {
		String completeCommand = String.join(" ", command);

		String line;
		String returnValue = "";

		try {
			Process processCommand = Runtime.getRuntime().exec(command);
			BufferedReader response = new BufferedReader(new InputStreamReader(processCommand.getInputStream()));

			try {
				processCommand.waitFor();
			} catch (InterruptedException commandInterrupted) {
				throw new Exception("Were waiting for process to end but something interrupted it"
						+ commandInterrupted.getMessage());
			}

			while ((line = response.readLine()) != null) {
				returnValue = returnValue + line + "\n";
			}

			response.close();

		} catch (Exception e) {
			throw new Exception("Unable to run command: " + completeCommand + ". Error: " + e.getMessage());
		}

		return returnValue;
	}

}
