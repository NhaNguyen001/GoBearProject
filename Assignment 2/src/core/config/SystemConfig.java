package core.config;

public class SystemConfig extends BaseConfig {

	public SystemConfig() {
		super("/config.properties");
	}

	public String getContext() {
		String value = this.getProperty("context") ;//+ this.configFile.getProperty("appPackage");
		return value;
	}

	public String getAppLink() {
		//String value = this.getProperty("appLink") + configFile.getProperty("installFile"); Comment by Kathy
		String value = this.getProperty("appLink");
		return value;
	}

	public String getAppPath() {
		String value = this.getExtendFolderPath()
				+ configFile.getProperty("installFile");
		return value;
	}

	public String getScreenshotPath(String testcase) {
		String value = currentProjectPath + configFile.getProperty("folderScreenshot") + testcase + ".PNG";
		return value;
	}

	public String getReportFolderPath() {
		String value = currentProjectPath + configFile.getProperty("REPORT_FOLDER_PATH");
		return value;
	}

	public String getFileReportPath() {
		String value = configFile.getProperty("REPORT_FILE");
		return value;
	}

	public String getExtendFolderPath() {
		String value =currentProjectPath + configFile.getProperty("EXTEND_FOLDER_PATH");
		return value;
	}

	public String getWebViewAndroidPath() {
		String value = currentProjectPath + configFile.getProperty("webviewAndroid");
		return value;
	}

	public String getAppiumServerPath() {
		String value = currentProjectPath + configFile.getProperty("appiumServer");
		return value;
	}

	public String getEmulator() {
		String value = currentProjectPath + configFile.getProperty("emulator");
		return value;
	}

	public String getDataTestPath() {
		String value = currentProjectPath + configFile.getProperty("dataTest");
		return value;
	}
	
	public String getTestDataPath() {
		String value = currentProjectPath + configFile.getProperty("testData");
		return value;
	}
	
	public String getExtentConfig() {
		String value = currentProjectPath + configFile.getProperty("extentConfig");
		return value;
	}

	public String getChromeDriver() {
		String value ;
		if(configFile.getProperty("deviceMode").equals("0") || configFile.getProperty("deviceMode").equals("3") ) {
			 value = configFile.getProperty("chromeDriver");
		}else
		{
			 value = currentProjectPath + configFile.getProperty("chromeDriver");
		}
		
		return value;
	}
}