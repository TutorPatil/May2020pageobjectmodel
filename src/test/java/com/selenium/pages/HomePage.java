package com.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {
	
	private WebDriver driver;
	
	public  HomePage(WebDriver driver)
	{
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath="//a[text()='Logout']")
	private WebElement logoutLink;
	
	public boolean checkLogoutlinkDisplayed()
	{
		boolean displayStatus = false;
		
		try {
			displayStatus = logoutLink.isDisplayed();
		}catch(Exception e)
		{ e.printStackTrace();
		}		
		return displayStatus;
	}
	
	public void clickLogoutLink()
	{
		logoutLink.click();
	}

}
