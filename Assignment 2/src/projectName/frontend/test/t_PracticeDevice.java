

package projectName.frontend.test;

import org.testng.annotations.Test;

import core.setup.DeviceSetup;
import projectName.frontend.action.PracticeDeviceAction;

public class t_PracticeDevice extends DeviceSetup {

	PracticeDeviceAction practicedeviceAction;

	@Override
	public void declareAction() {
		practicedeviceAction = new PracticeDeviceAction( driver);
	}

	@Test(enabled = true, priority = 1)
	public void TC_002_VerifyLaunchApp() throws InterruptedException {
		boolean result = practicedeviceAction.checkLabelWelcome();
		practicedeviceAction.assertResult(result, "Screen display not match!",
				"Screen is matched with specification!");
	}
/*
	@Test(enabled = true, priority = 1)
	public void TC_003_VerifyPrivacyPolicy() throws InterruptedException {
		boolean result = practicedeviceAction.checkPrivacyPolicy();
		practicedeviceAction.assertResult(result, "Privacy Policy is not displayed to Click!",
				"Privacy Policy is displayed and click!");
		
	}
	*/
	
	@Test(enabled = true, priority = 1)
	public void TC_004_VerifyTermsOfUse() throws InterruptedException {
		boolean result = practicedeviceAction.checkTermsOfUse();
		practicedeviceAction.assertResult(result, "Privacy Policy is not displayed to Click!",
				"Privacy Policy is displayed and click!");
		practicedeviceAction.CloseTermOfUse();
		
	}
}

