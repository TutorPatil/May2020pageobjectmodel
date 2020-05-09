package com.selenium.tests;

import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.selenium.base.BaseClass;
import com.selenium.pages.HomePage;
import com.selenium.pages.LogingPage;

public class LoginTest extends BaseClass{
	
	@Test	
	public void login_001()
	{		
		
		LogingPage loginpage = new LogingPage(driver);
		
		
		loginpage.setUserName("admin");
		loginpage.setPassword("manager");
		HomePage homepage = loginpage.clickOkButton(driver);
		
		//HomePage homepage = new HomePage(driver);
		
		boolean result = homepage.checkLogoutlinkDisplayed();
		homepage.clickLogoutLink();
		
		Assert.assertTrue(result, "Logout link is not diplayed");
		
		
	}
	
	@Test
	public void login_002()
	{
		LogingPage loginpage = new LogingPage(driver);
		
		
		loginpage.setUserName("admin");
		loginpage.setPassword("manager123");
		loginpage.clickOkButton(driver);
		
		boolean result = loginpage.errorMsgDisplay();
		Assert.assertTrue(result, "Login_002 Failed..");
		
		
		
		
	}

}
