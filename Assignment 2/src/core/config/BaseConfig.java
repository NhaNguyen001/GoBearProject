package core.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * @author LAN
 * Read configuration for system
 */
public class BaseConfig {
	Properties configFile;
	public static String currentProjectPath = System.getProperty("user.dir");
	
	public BaseConfig() {
		configFile = new java.util.Properties();
		try {
			InputStream input = getFile(currentProjectPath + "/src/core/config/config.properties");
			if(input!=null)
				configFile.load(input);
		} catch (Exception eta) {
			eta.printStackTrace();
		}
	}
	
	public BaseConfig(String name) {
		configFile = new java.util.Properties();
		try {
			InputStream input = getFile(currentProjectPath
					+ "/src/core/config/" + name);
			if (input != null)
				configFile.load(input);
		} catch (Exception eta) {
			eta.printStackTrace();
		}
	}

	public String getProperty(String key) {
		String value = this.configFile.getProperty(key);
		return value;
	}
	
	public int getTimeOut() {
		String value = this.configFile.getProperty("TIME_OUT");
		return Integer.valueOf(value);
	}
	
	public int getSleepBetweenPoll() {
		String value = this.configFile.getProperty("SLEEP_BETWEEN_POLL");
		return Integer.valueOf(value);
	}
	
	public int getPayloadTimeout() {
		String value = this.configFile.getProperty("PAGELOAD_TIMEOUT");
		return Integer.valueOf(value);
	}
	
	public int getImplicitWait() {
		String value = this.configFile.getProperty("IMPLICIT_WAIT");
		return Integer.valueOf(value);
	}
	
	public String getChromeDriver() {
		String value = currentProjectPath + this.configFile.getProperty("CHROME_DRIVER_PATH");
		return value;
	}
	
	public String getContext() {
		String value = this.getProperty("context") + this.configFile.getProperty("appPackage");
		return value;
	}
	
	/**
	 * get file from Path
	 * @param pathFile path of file
	 * @return FileInputStream or null when have exception
	 */
	public FileInputStream getFile(String pathFile) {
		File file = new File(pathFile);
		FileInputStream fileInput = null;
		try {
			fileInput = new FileInputStream(file);
			return fileInput;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}	
	}

}