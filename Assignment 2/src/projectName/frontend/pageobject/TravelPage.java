package projectName.frontend.pageobject;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import core.pageObject.BaseObjectPage;

public class TravelPage extends BaseObjectPage {
	public TravelPage(WebDriver driver) {
		super(driver);
	}

	@FindBy(xpath = ".//a[@aria-controls='Insurance']")
	public WebElement btn_Insurance;
	
	@FindBy(xpath = ".//a[@aria-controls='Travel']")
	public WebElement btn_Travel;
	
	@FindBy(xpath = ".//div[@data-gb-name ='trip-type']")
	public WebElement lst_trip;
	
	@FindBy(xpath = ".//div[@data-gb-name = 'traveller']")
	public WebElement lst_traveller;
	
	@FindBy(xpath = ".//button[@name = 'product-form-submit']")
	public WebElement btn_showResult;

	@FindBy(xpath = ".//div[@class = 'col-sm-4 card-full']")
	public List<WebElement> card_result;
	
	@FindBy(xpath = ".//div[@class = 'filter-detail sidebar-item']")
	public WebElement item_filter;
	
	@FindBy(xpath = ".//div[@class = 'sort-detail sidebar-item']")
	public WebElement item_sort;
	
	@FindBy(xpath = ".//div[@class = 'edit-detail sidebar-item']")
	public WebElement item_details;
	
	@FindBy(xpath = ".//div[@data-gb-name = 'filter-option' and @class='radio radio-primary']")
	public List<WebElement> radio_filterOption;
	
	@FindBy(xpath = ".//div[ @class='no-results text-center']")
	public WebElement result_for_Filter;
	
}
