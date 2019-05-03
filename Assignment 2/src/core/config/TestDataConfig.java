package core.config;


public class TestDataConfig extends BaseConfig {

	public TestDataConfig() {
		super("/testData.properties");
	}

	public String getFullName(String keyLastName, String keyFirstName) {
		return getProperty(keyLastName) + " " + getProperty(keyFirstName);
	}

}