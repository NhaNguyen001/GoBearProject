package projectName.frontend.test;

import org.testng.annotations.Test;

import core.setup.WebSetup;
import projectName.frontend.action.PracticeAction;

public class t_Practice extends WebSetup {
	PracticeAction privateAction;

	@Override
	public void declareAction() {
		privateAction = new PracticeAction(driver);
	}
	
	@Test(enabled = true, priority = 1)
	public void TC_001_NavigateToGoogle() throws InterruptedException {
		privateAction.searchInGoogle("Test");
		privateAction.searchInGoogle("automation");
	}

	
}
