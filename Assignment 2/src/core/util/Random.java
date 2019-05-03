package core.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;

public class Random {
	public static String randomString(int length) {
		return RandomStringUtils.randomAlphabetic(length);
	}

	public static String randomNumber(int length) {
		return RandomStringUtils.randomNumeric(length);
	}

	public static String randomString(String txt) {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String randomText = txt + dateFormat.format(date);
		return randomText;

	}

	public static String currentDateTimeAsString() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(new Date());
	}
}
