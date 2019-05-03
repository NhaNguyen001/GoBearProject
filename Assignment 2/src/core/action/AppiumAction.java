package core.action;

import java.io.IOException;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class AppiumAction extends BaseAction {
	public TouchAction touchAction;
	public AppiumDriver<WebElement> deviceDriver;

	public AppiumAction(AppiumDriver<WebElement> driver) {
		super(driver);
		deviceDriver = driver;
		this.touchAction = new TouchAction(deviceDriver);
	}

	public void swipeUp() throws InterruptedException {
		deviceDriver.context("NATIVE_APP");
		Dimension size = deviceDriver.manage().window().getSize();
		int endy = (int) (size.height * 0.1);
		int startx = (int) (size.width * 0.30);
		int starty = (int) (size.height * 0.5);
		touchAction.press(startx, starty).waitAction().moveTo(startx, endy).release().perform();
		Thread.sleep(5000);
		deviceDriver.context(config.getContext());
	}

	public void swipeTo(String to_direction) throws InterruptedException {
		deviceDriver.context("NATIVE_APP");
		Dimension size = driver.manage().window().getSize();
		touchAction = new TouchAction(deviceDriver);
		if (to_direction.toLowerCase() == "left") {
			int startx = (int) (size.width * 0.7);
			int endx = (int) (size.width * 0.10);
			int starty = size.height / 2;
			touchAction.press(startx, starty).waitAction().moveTo(endx, starty).release().perform();
		}
		if (to_direction.toLowerCase() == "right") {
			int endx = (int) (size.width * 0.7);
			int startx = (int) (size.width * 0.10);
			int starty = size.height / 2;
			touchAction.press(startx, starty).waitAction().moveTo(endx, starty).release().perform();
		}
		if (to_direction.toLowerCase() == "up") {
			int endy = (int) (size.height * 0.3);
			int startx = (int) (size.width * 0.30);
			int starty = (int) (size.height * 0.7);
			touchAction.press(startx, starty).waitAction().moveTo(startx, endy).release().perform();
		}
		if (to_direction.toLowerCase() == "down") {
			int endy = (int) (size.height * 0.7);
			int startx = (int) (size.width * 0.50);
			int starty = (int) (size.height * 0.3);
			touchAction.press(startx, starty).waitAction().moveTo(startx, endy).release().perform();
		}
		Thread.sleep(5000);
		deviceDriver.context(config.getContext());
	}

	public void waitForClickCenter(WebElement ele, int time) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, time);
			wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(ele)));
			wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(ele)));
			scrolltoElement(ele);
			clickCenter(ele);
		} catch (Exception ex) {
			logWarn("Element/s:" + ele.toString() + " is not clicked");
		}
	}

	private void clickCenter(WebElement ele) {
		int startx = (int) (ele.getSize().width * 0.5);
		int starty = (int) (ele.getSize().height * 0.5);
		TouchAction action = new TouchAction(deviceDriver);
		action.tap(startx, starty);
	}

	public void lockScreen(int seconds) throws InterruptedException {
		if (deviceDriver instanceof AndroidDriver) {
			((AndroidDriver<WebElement>) deviceDriver).lockDevice();
			logStep("Locked Screen And Wait For " + seconds + " Seconds");
			Thread.sleep(seconds * 1000);
		} else {
			((IOSDriver<WebElement>) deviceDriver).lockDevice(seconds);;
		}

	}

	public void unlockScreen() throws IOException, InterruptedException {
		if (deviceDriver instanceof AndroidDriver) {
			Runtime.getRuntime().exec("adb shell input keyevent 26");
			Thread.sleep(10000);
			logStep("Unlocked Screen");
		}
	}

	public void waitForClickCheckbox(WebElement ele, int time) throws InterruptedException {
		try {

			WebDriverWait wait = new WebDriverWait(deviceDriver, time);
			wait.until(ExpectedConditions.visibilityOf(ele));
			if (deviceDriver instanceof IOSDriver) {
				ele.sendKeys("");
			} else {
				ele.click();
			}
		} catch (Exception ex) {
			logWarn("Element/s:" + ele.toString() + " is not clicked");
		}
	}

	

	public void escapeMenu() throws InterruptedException {
		deviceDriver.context("NATIVE_APP");
		Dimension size = deviceDriver.manage().window().getSize();
		int x = (int) (size.width * 0.9);
		int y = size.height / 2;
		touchAction = new TouchAction(deviceDriver).tap(x, y).release().perform();
		Thread.sleep(5000);
		deviceDriver.context(config.getContext());
	}
}
