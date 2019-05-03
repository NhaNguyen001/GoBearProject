package projectName.frontend.test;

import org.testng.annotations.Test;

import core.setup.DeviceSetup;
import projectName.frontend.action.LoginAppAction;

public class t_LoginDevice extends DeviceSetup {

	LoginAppAction loginAction;

	@Override
	public void declareAction() {
		loginAction = new LoginAppAction( driver);
	}

	@Test(enabled = true, priority = 1)
	public void TC_002_VerifyApp() throws InterruptedException {
		boolean result = loginAction.checkLabel();
		loginAction.assertResult(result, "Screen display not match!",
				"Screen is matched with specification!");
	}


}