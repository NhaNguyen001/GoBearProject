package projectName.frontend.test;

import org.testng.annotations.Test;

import core.setup.WebSetup;
import projectName.frontend.action.LoginAction;
import projectName.frontend.action.TravelAction;

public class t_TravelSection extends WebSetup {

	TravelAction travelAction;

	@Override
	public void declareAction() {
		travelAction = new TravelAction(driver);
	}

	//Example 1: run test for web
	@Test
	public void TC001_Verify_Travel_Section() throws Exception {
		
		boolean result1 = travelAction.verifyInsuranceTab();
		travelAction.assertResult(result1, "Insurance Tab is not displayed",
				"Insurance Tab is displayed");
		travelAction.navigateToTravel();
		boolean result2 = travelAction.verifyTravelTab();
		travelAction.assertResult(result2, "Travel Tab is not displayed",
				"Travel Tab is displayed");
		travelAction.navigateToMyResult();
		travelAction.verifyLeftSideMenu();
		boolean result3 = travelAction.verifyCardMyResutlPage();
		travelAction.assertResult(result3, "At least 3 cards are being not displayed",
				"At least 3 cards are being displayed");
		
	}

}
