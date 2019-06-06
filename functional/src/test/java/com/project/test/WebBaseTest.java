package com.project.test;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.project.core.AppLib;

/**
 * This class serves as the base test for all tests as it opens up a connection
 * to the selenium grid and tears it down as well after tests have completed.
 * 
 * @author ekohanchi
 *
 */
public class WebBaseTest {
	protected WebDriver webDriver;
	protected String environment;
	protected String baseUrl;
	protected String seleniumGrid;
	protected AppLib appLib = new AppLib();

	@BeforeClass(alwaysRun = true)
	@Parameters({ "Environment", "AppHost", "SeleniumGrid" })
	public void setup(String env, @Optional("") String appHost, @Optional("") String selGrid) {
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability("name", this.getClass().getName().toString());
		try {
			environment = env;
			seleniumGrid = selGrid;
			baseUrl = appLib.getBaseUrl(environment, appHost);
			webDriver = new RemoteWebDriver(new URL(appLib.getSeleniumServer(seleniumGrid)), capabilities);
			webDriver.manage().window().maximize();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() {
		// Closing the browser and WebDriver
		webDriver.close();
		webDriver.quit();
	}

}
