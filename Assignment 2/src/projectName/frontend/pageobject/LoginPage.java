package projectName.frontend.pageobject;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import core.pageObject.BaseObjectPage;

public class LoginPage extends BaseObjectPage {
	public LoginPage(WebDriver driver) {
		super(driver);
	}

	@FindBy(css = "txt_Username")
	 public WebElement txt_username_name;

	@FindBy(xpath = ".//input[@placeholder='Username']")
	public WebElement txt_username_xpath;
	
	@FindBy(css = "input[placeholder='Username']")
	public WebElement txt_username_css;

	@FindBy(css = "txt_Password")
	public WebElement txt_password_name;

	@FindBy(xpath = ".//input[@placeholder='Password']")
	public WebElement txt_password_xpath;
	
	@FindBy(css = "input[placeholder='Password']")
	public WebElement txt_password_css;

	@FindBy(css = "btn_Login")
	public WebElement btn_login_name;

	@FindBy(css = ".button.button-positive.force-pull-right.button-login")
	public WebElement btn_login;

	@FindBy(css = "lbl_error_login")
	public WebElement lbl_error_login_name;

	@FindBy(css = "div_error_login")
	public WebElement div_error_login_name;

	@FindBy(css = "dialog")
	public WebElement div_error_login_id;

	@FindBy(css = "div_error_login")
	public WebElement btn_error_login_OK_name;

	@FindBy(css = ".popup-buttons button[ng-click='$buttonTapped(button, $event)']")
	public WebElement btn_error_login_OK_css;
	
	@FindBy(css = ".popup-body div.text-center")
	public WebElement txt_comment_error_dialog;

	@FindBy(css = "cbx_save_login")
	public WebElement cbx_save_login_name;

	@FindBy(xpath = "//input[contains(@type, 'checkbox')]")
	public List<WebElement> cbx_login_xpaths;
	
	@FindBy(xpath = "//input[@ng-model='loginObj.remember']")
	public WebElement cb_remember_password;
	
	@FindBy(xpath = "//input[@ng-model='loginObj.autoLogin']")
	public WebElement cb_auto_login;

	@FindBy(css = "lbl_error_login")
	public WebElement lbl_login_title_name;

	@FindBy(css = ".login-title")
	public WebElement lbl_login_title_css;

	@FindBy(css = "lbl_version")
	public WebElement lbl_version_name;

	@FindBy(css = ".col.force-center.no-padding.version-text.ng-binding")
	public WebElement lbl_version_css;
	
	@FindBy(css = ".button.icon.button-positive.instruction-font.full-width")
	public WebElement btn_logout;
	

	
	
	@FindBy(css = "input[formcontrolname='username']")
	public WebElement txtUsername;

	@FindBy(css = "input[formcontrolname='password']")
	public WebElement txtPassword;

	@FindBy(css = "button[class='btn btn-primary btn-block']")
	public WebElement btnLogin;
	
	@FindBy(xpath = "//div/img [@class='logoMenu']")
	public WebElement logo;
	
	@FindBy(xpath = "//div/p[2] [@class='version']")
	public WebElement labelVersion;
	
	@FindBy(xpath = "//div[@class='invalid-feedback'] /div")
	public List<WebElement> lstInvalidFeedback;
	
	@FindBy(xpath = "//*[contains(@text,'Go to Competitions')]")
	public WebElement btnCompetition;
	
	//element for Mobile sample
	@FindBy(xpath = "//*[contains(@text,'Hello and welcome to Gamescore')]")
	public WebElement labelWelcomeToApp;
	
	@FindBy(xpath = "//*[contains(@text,'Privacy Policy')]")
	public WebElement linkPrivacyPolicy;
	
	@FindBy(xpath = "//*[contains(@text,'Terms of use')]")
	public WebElement linkTermsOfUse;
	@FindBy(xpath = "//*[contains(@text,'Terms of use')]")
	public WebElement labelTermsOfUse;
	//element for Web sample
	
	@FindBy(css = "input.gLFyf.gsfi")
	public WebElement textboxSearch;
}
