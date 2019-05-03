package projectName.frontend.action;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import core.action.BaseAction;
import core.util.Log;
import projectName.frontend.pageobject.LoginPage;
import projectName.frontend.pageobject.TravelPage;

public class TravelAction extends BaseAction {
	TravelPage travelPage;

	public TravelAction(WebDriver driver) {
		super(driver);
		travelPage = new TravelPage(driver);
		
	}
	
	public boolean verifyInsuranceTab()
	{
		
		boolean istrue1 = isDisplayed(travelPage.btn_Insurance, "Insurance");
		return istrue1;
	}
	
	public void navigateToTravel()
	{
		try {
			click(travelPage.btn_Insurance);
			click(travelPage.btn_Travel);
			logStep("Navigate to Travel section");
		}
		catch (Exception ex)
		{
			logFail(ex.getMessage());
		}
	}
	public boolean verifyTravelTab()
	{
		boolean istrue1 = isDisplayed(travelPage.lst_trip, "Trip List");
		boolean istrue2 = isDisplayed(travelPage.lst_traveller, "Traveller List");
		boolean istrue3 = isDisplayed(travelPage.btn_showResult, "Show Button");
		return istrue1 && istrue2 && istrue3;
	}
	public void navigateToMyResult()
	{
		try {
			click(travelPage.btn_showResult);
			logStep("Navigate to Show My Result Page");
		}
		catch (Exception ex)
		{
			logFail(ex.getMessage());
		}
	}
	
	public boolean verifyCardMyResutlPage()
	{
		boolean istrue = false;
		int iCount = travelPage.card_result.size();
		if(iCount >= 3)
			istrue = true;
		logStep("My resul page is displayed "+ iCount + " cards");
		return istrue;
		
	}
	public void verifyLeftSideMenu()
	{
		if (isDisplayed(travelPage.item_details) && isDisplayed(travelPage.item_filter) && isDisplayed(travelPage.item_sort)) logPass("All item for Filter, Sort and Details are displayed");
		else logFail("All item for Filter, Sort and Details are not displayed");
		if (travelPage.radio_filterOption.size() > 0) {
			travelPage.radio_filterOption.get(1).click();
			logStep("Selected option " + travelPage.radio_filterOption.get(1).getAttribute("data-filter-name") );
			if (isDisplayed(travelPage.result_for_Filter) )
					logPass("Result for Filter is displayed");
	    } 
	}
	
	
}