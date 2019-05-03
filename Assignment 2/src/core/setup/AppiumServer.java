package core.setup;

import java.io.File;

import org.junit.Assert;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

public class AppiumServer {

	String appiumInstallationDir = "C:/Program Files (x86)";// e.g. in Windows
	// String appiumInstallationDir = "/Applications";// e.g. for Mac
	AppiumDriverLocalService service = null;

	public AppiumServer() {
		File classPathRoot = new File(System.getProperty("user.dir"));
		String osName = System.getProperty("os.name");

		if (osName.contains("Windows")) {
			service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
					.usingDriverExecutable(
							new File(appiumInstallationDir + File.separator + "Appium" + File.separator + "node.exe"))
					.withAppiumJS(new File(appiumInstallationDir + File.separator + "Appium" + File.separator
							+ "node_modules" + File.separator + "appium" + File.separator + "bin" + File.separator
							+ "appium.js"))
					.withLogFile(new File(new File(classPathRoot, File.separator + "log"), "androidLog.txt")));

		} else if (osName.contains("Mac")) {
			service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
					.usingDriverExecutable(
							new File(appiumInstallationDir + "/Appium.app/Contents/Resources/node/bin/node"))
					.withAppiumJS(new File(
							appiumInstallationDir + "/Appium.app/Contents/Resources/node_modules/appium/bin/appium.js"))
					.withLogFile(new File(new File(classPathRoot, File.separator + "log"), "androidLog.txt")));

		} else {
			// you can add for other OS, just to track added a fail message
			Assert.fail("Starting appium is not supporting the current OS.");
		}
	}

	/**
	 * Starts appium server
	 */
	public void startAppiumServer() {
		service.start();
	}

	/**
	 * Stops appium server
	 */
	public void stopAppiumServer() {
		service.stop();
	}
}