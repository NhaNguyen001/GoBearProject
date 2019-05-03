package projectName.frontend.test;

import org.testng.annotations.Test;

import core.setup.WebSetup;
import projectName.frontend.action.LoginAction;

public class t_Login extends WebSetup {

	LoginAction loginAction;

	@Override
	public void declareAction() {
		loginAction = new LoginAction(driver);
	}

	//Example 1: run test for web
	@Test
	public void UC0047_Verify_Login() throws Exception {
		loginAction.login(config.getProperty("USER_NAME_ADMIN"), config.getProperty("PASSWORD_ADMIN"));
		boolean result = true;
		loginAction.assertResult(result, "Show Login Page after logout be right",
				"Show Login Page after logout be wrong");
		
	}

}
