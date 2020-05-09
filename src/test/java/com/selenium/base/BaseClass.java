package com.selenium.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.google.common.io.Files;

public class BaseClass implements ITestListener{

	public static WebDriver driver = null;
	
	public static HashMap<String,String> locatorData = new HashMap<String,String>();
	public static HashMap<String,String> testData = new HashMap<String,String>();
	
	public static Logger logger = Logger.getLogger("BaseClass");
	
	
	public static ExtentHtmlReporter reporter;
	public static ExtentReports extent;
	public static ExtentTest  exLogger;
	
	
	
	public static void writeLogs(String msg)
	{
		logger.info(msg);
		exLogger.log(Status.INFO, msg);
	}
	
	public static void writeErrorLogs(Throwable t)
	{
		String s = Arrays.toString(t.getStackTrace());		
		String s1 = s.replaceAll(",", "\n");		
		logger.error(s1);
		exLogger.log(Status.ERROR, s);
	}
	
	
	//***************************************************************************************
	
	/**
	 * Title This method is for reading the config Data
	 * @author Patil
	 * @param keyname
	 * @return PropertyValue
	 * @throws Exception
	 */
	
	public static String getConfigData(String keyname) throws Exception {
		String value = "";

		File f = new File("./src/test/data/config.properties");
		FileInputStream fis = new FileInputStream(f);

		Properties prop = new Properties();
		prop.load(fis);

		value = prop.getProperty(keyname);

		return value;

	}
	
	//***************************************************************************************

	/**
	 * Title : - This method is to launch the Actitime Application
	 * @author Patil
	 * @throws Exception
	 * @param none
	 */
	
	@BeforeMethod(alwaysRun=true)
	public static void launchActitime() throws Exception {
		
		writeLogs("Before Method....Inside before Method");

		String browser = getConfigData("browser");

		if (browser.equals("chrome")) {
			System.setProperty("webdriver.chrome.silentOutput", "true");
			System.setProperty("webdriver.chrome.driver", "./src/test/tools/chromedriver.exe");
			driver = new ChromeDriver();

		}

		if (browser.equals("firefox")) {
			System.setProperty("webdriver.gecko.driver", "./src/test/tools/geckodriver.exe");
			driver = new FirefoxDriver();
		}

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.get(getConfigData("url"));
	}
	
	//***************************************************************************************

	/**
	 * Title : - This method is to close the Actitime Application
	 * @author Patil
	 * @throws none
	 * @param none
	 * @return none
	 * 
	 */
	
	@AfterMethod(alwaysRun = true)
	public static void closeBrowser() {
		writeLogs("After Method....Inside After Method");
		driver.close();
	}
	
	//***************************************************************************************
	
	/**
	 * Title : - This method is to write results to a txt file
	 * @author Patil
	 * @throws Exception
	 * @Param testCaseName, status
	 * @return none
	 * 
	 */

	
	public static void writeResultsToFile(String testCaseName, String status) throws Exception {
		File f = new File("./src/test/results/results.txt");
		FileWriter fw = new FileWriter(f, true);

		fw.write(testCaseName + "-----" + status+"\n");

		fw.flush();
		fw.close();

	}
	
	//***************************************************************************************
	
	/**
	 * Title : - This method is to capture  the screenshots of failed tests
	 * @author Patil
	 * @throws Exception
	 * @Param fileName
	 * @return none
	 * 
	 */

	public static void captureScreenShot(String fileName) throws IOException {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		File dest = new File("./src/test/results/screenshots/" + fileName + ".png");
		Files.copy(src, dest);
		
		exLogger.fail("The Login 01 Faied", MediaEntityBuilder.createScreenCaptureFromPath(dest.getAbsolutePath()).build());

	}

	//***************************************************************************************
	/**
	 * Title : - This method is to fetch locatordata and test data from excel file
	 * @author Patil
	 * @throws Exception
	 * @Param fileName,pageName,elementName
	 * @return locator or Test data
	 * 
	 */
	
	
	public static String getDataFromExcelFile(String fileName, String pageName, String elementName) throws Exception {
		String locator = "";
		File file = null;

		if (fileName.contains("locator")) {
			file = new File("./src/test/data/locatordata.xlsx");
		} else if (fileName.contains("test")) {
			file = new File("./src/test/data/testdata.xlsx");
		}
		FileInputStream fio = new FileInputStream(file);

		XSSFWorkbook workbook = new XSSFWorkbook(fio);

		XSSFSheet worksheet = workbook.getSheet("Sheet1");

		int rows = worksheet.getLastRowNum();
		// rows = rows+1;

		for (int x = 1; x <= rows; x++) {

			String page = worksheet.getRow(x).getCell(0).getStringCellValue();
			String loc = worksheet.getRow(x).getCell(1).getStringCellValue();

			if ((pageName.equals(page)) && (elementName.equals(loc))) {
				locator = worksheet.getRow(x).getCell(2).getStringCellValue();
				break;
			}
		}
		workbook.close();

		return locator;

	}
	
	//***************************************************************************************
	/*
	@BeforeSuite
	public static void beforeSuite()
	{
		writeLogs(" Beore Suite....This method will run first before all other methods");
	}
	
	@AfterSuite
	public static void afterSuite()
	{
		writeLogs(" After Suite....This method will run after all other methods at the end");
	}
	*/
	
	@BeforeTest
	public static void beforetest()
	{
		writeLogs(" Beore Test....This method will run before every testngxml test");
	}
	
	@AfterTest
	public static void aftertest()
	{
		writeLogs(" After Test....This method will run after every testngxml test");
	}
	
	

	@BeforeClass
	public static void beforeClass()
	{
		writeLogs(" Beore Class....This method will run before every class");
	}
	
	@AfterClass
	public static void afterClass()
	{
		writeLogs(" After Class....This method will run after every class");
	}
	
	
	// TestNG Listeners Methods.....

	public void onTestStart(ITestResult result) {
		extent.flush();		
		exLogger = extent.createTest(result.getName());
		writeLogs(" The test case by name "+result.getName() + " is getting executed....");
		
	}

	public void onTestSuccess(ITestResult result) {
		exLogger.log(Status.PASS, "The test case by name "+result.getName() + " is PASS");
		writeLogs(" The test case by name "+result.getName() + " is Passed");
		try {
			writeResultsToFile(result.getName(), "PASS");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onTestFailure(ITestResult result) {
		exLogger.log(Status.FAIL, "The test case by name "+result.getName() + " is Failed");
		writeLogs(" The test case by name "+result.getName() + " is Failed");
		try {
			writeResultsToFile(result.getName(), "Failed");
			captureScreenShot(result.getName());
			Throwable t = result.getThrowable();
			writeErrorLogs(t);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void onTestSkipped(ITestResult result) {
		
		
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		
		
	}

	public void onStart(ITestContext context) {
		
       File f = new File("./src/test/results/suiteresults.html");
		
		
		 reporter = new ExtentHtmlReporter(f);
		 extent = new ExtentReports();
		 extent.attachReporter(reporter);
		 exLogger = extent.createTest("extentReportDemo");
		
		writeLogs("************* This method will be executed in the begining**************");
		
		try {
			storeLocatData();
			storeTestData();
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		//writeLogs(locatorData);
		//writeLogs(testData);
		
		
	}

	public void onFinish(ITestContext context) {
		writeLogs("************* This method will be executed at the end **************");
		extent.flush();	
	}

	
	public  void storeLocatData() throws Exception
	{
		
		writeLogs(" Inisde the storeLocatorData Method");
		String locator = "";
		File file = null;

		
		file = new File("./src/test/data/locatordata.xlsx");
		
		FileInputStream fio = new FileInputStream(file);

		XSSFWorkbook workbook = new XSSFWorkbook(fio);

		XSSFSheet worksheet = workbook.getSheet("Sheet1");

		int rows = worksheet.getLastRowNum();
		// rows = rows+1;

		for (int x = 1; x <= rows; x++) {

			String page = worksheet.getRow(x).getCell(0).getStringCellValue();
			String loc = worksheet.getRow(x).getCell(1).getStringCellValue();
			locator = worksheet.getRow(x).getCell(2).getStringCellValue();
			
			//writeLogs(page+" "+loc +" "+ locator);

			locatorData.put(page+"#"+loc, locator);

		}
		workbook.close();

	}
	




	public  void storeTestData() throws Exception
	{
		writeLogs(" Inisde the storeTestData Method");
		String locator = "";
		File file = null;
	
		
		file = new File("./src/test/data/testdata.xlsx");
		
		FileInputStream fio = new FileInputStream(file);
	
		XSSFWorkbook workbook = new XSSFWorkbook(fio);
	
		XSSFSheet worksheet = workbook.getSheet("Sheet1");
	
		int rows = worksheet.getLastRowNum();
		// rows = rows+1;
	
		for (int x = 1; x <= rows; x++) {
	
			String page = worksheet.getRow(x).getCell(0).getStringCellValue();
			String loc = worksheet.getRow(x).getCell(1).getStringCellValue();
			locator = worksheet.getRow(x).getCell(2).getStringCellValue();
	
			
			//writeLogs(page+" "+loc +" "+ locator);
			
			
			testData.put(page+"#"+loc, locator);
	
		}
		workbook.close();
	
	}
	
	public static String getTestDataFromMap(String pageName,String elementName)
	
	{
		String value = "";
		String key = pageName+"#"+elementName;
		value = testData.get(key);		
		return value;
		
		
	}
	
	
	
public static String getlocatorDataFromMap(String pageName,String elementName)
	
	{
		String value = "";
		String key = pageName+"#"+elementName;
		value = locatorData.get(key);		
		return value;
		
		
	}


}