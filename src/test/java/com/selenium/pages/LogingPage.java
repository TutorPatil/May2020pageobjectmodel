package com.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LogingPage {	
	
	private WebDriver driver;
	
	public  LogingPage(WebDriver driver)
	{
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath ="//input[@name='username']")
	private WebElement userName;
	
	
	@FindBy(name = "pwd")
	private WebElement password;
	
	@FindBy(id="loginButton")
	private WebElement okBtn;
	
	@FindBy(xpath="//span[text()='Username or Password is invalid. Please try again.']")
	private WebElement errorMsg;
	
	
	
	public void setUserName(String usernameData)
	{
		userName.sendKeys(usernameData);
	}
	
	public void setPassword(String passwordData)
	{
		password.sendKeys(passwordData);
	}
	

	public HomePage clickOkButton(WebDriver driver)
	{
		okBtn.click();
		HomePage homepage = new HomePage(driver);
		return homepage;
	}

	
	public boolean errorMsgDisplay()
	{
		boolean displayStatus = false;
		
		try {
			displayStatus = errorMsg.isDisplayed();
		}catch(Exception e)
		{ e.printStackTrace();
		}		
		return displayStatus;
		
	}
	
	
	
	
	
	
	

}
