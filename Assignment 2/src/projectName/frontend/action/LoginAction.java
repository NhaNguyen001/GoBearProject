package projectName.frontend.action;

import org.openqa.selenium.WebDriver;

import core.action.BaseAction;
import core.util.Log;
import projectName.frontend.pageobject.LoginPage;

public class LoginAction extends BaseAction {
	LoginPage loginPage;

	public LoginAction(WebDriver driver) {
		super(driver);
		loginPage = new LoginPage(driver);
		
	}
	
	/**
	 * veryfy the UI of Login view
	 * @return
	 */
	public boolean checkLoginView() {
		boolean istrue1 = loginPage.txt_username_css != null ? loginPage.txt_username_css.isDisplayed() : false;
		boolean istrue2 = loginPage.txt_password_css != null ? loginPage.txt_password_css.isDisplayed() : false;
		boolean istrue3 = loginPage.lbl_login_title_css.getText().contentEquals("Login");
		boolean istrue4 = false;
		boolean istrue5 = false;
		if (loginPage.cbx_login_xpaths != null && loginPage.cbx_login_xpaths.size() > 0) {
			istrue4 = loginPage.cbx_login_xpaths.get(0).isDisplayed();
			istrue5 = loginPage.cbx_login_xpaths.get(1).isDisplayed();
		}
		String s = loginPage.lbl_version_css.getCssValue("color");
		logStep("******************* VERSION TEST: " + loginPage.lbl_version_css.getText() + " ********************");
		boolean istrue6 = s.contentEquals("rgba(255, 255, 255, 1)");
		return istrue1 && istrue2 && istrue3 && istrue4 && istrue5 && istrue6;
	}
	
	
	/**
	 * input usename , password to Login
	 * @param username
	 * @param password
	 * @throws InterruptedException
	 */
	public void login(String username, String password) throws InterruptedException {
		type(loginPage.txtUsername, username);
		type(loginPage.txtPassword, password);
		Thread.sleep(3000);
		click(loginPage.btnLogin);
		waitUntilInvisible(loginPage.btnLogin);
		logStep("Login with username :"+username+" password: "+password);
		
	}
}