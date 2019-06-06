package com.project.test;

import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.project.core.AppLib;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.proxy.CaptureType;

/**
 * This class serves as the base test for all tests as it opens up a connection
 * to the selenium grid and tears it down as well after tests have completed.
 * 
 * @author ekohanchi
 *
 */
public class NetworkBaseTest {
	protected WebDriver webDriver;
	protected String environment;
	protected String seleniumGrid;
	protected AppLib appLib = new AppLib();
	protected BrowserMobProxy proxy;

	@BeforeClass(alwaysRun = true)
	@Parameters({ "Environment", "SeleniumGrid" })
	public void setup(String env, @Optional("") String selGrid) {
		// start the proxy
		//proxy = new BrowserMobProxyServer();
		proxy = getProxyServer();
		
		// Get the Selenium proxy Object
		//Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
		Proxy seleniumProxy = getSeleniumProxy(proxy);
		
		// Configure it as a desired capability
		DesiredCapabilities capabilities = DesiredCapabilities.chrome(); // new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
		capabilities.setCapability("name", this.getClass().getName().toString());
		try {
			environment = env;
			seleniumGrid = selGrid;
			webDriver = new RemoteWebDriver(new URL(appLib.getSeleniumServer(seleniumGrid)), capabilities);
			webDriver.manage().window().maximize();
			proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
			proxy.newHar();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() {
		// Stopping the proxy
		proxy.stop();
		// Closing the browser and WebDriver
		webDriver.close();
		webDriver.quit();
	}
	
	private Proxy getSeleniumProxy(BrowserMobProxy proxyServer) {
		Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxyServer);
		try {
			String hostIp = Inet4Address.getLocalHost().getHostAddress();
			seleniumProxy.setHttpProxy(hostIp + ":" + proxyServer.getPort());
			seleniumProxy.setSslProxy(hostIp + ":" + proxyServer.getPort());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			Assert.fail("Invalid Host Address");
		}
		
		return seleniumProxy;
	}
	
	private BrowserMobProxy getProxyServer() {
		BrowserMobProxy proxy = new BrowserMobProxyServer();
		proxy.setTrustAllServers(true);
		proxy.start();
		return proxy;
	}
	
	/**
	 * This method will get all the Har Entries and put it within map for a given
	 * List<HarEntry> object passed in
	 * @param entries
	 * @return - HashMap<String, Integer> - The map for all the Har Entries
	 */
	public HashMap<String, Integer> getAllHarEntries(List<HarEntry> entries) {
		HashMap<String, Integer> harMap = new HashMap<String, Integer>();
		for(HarEntry entry : entries) {
			harMap.put(entry.getRequest().getUrl(), entry.getResponse().getStatus());
		}
		
		return harMap;
	}
	
	/**
	 * This method will get all the urls for the js Har Entries and put it within a list
	 * @param harMap
	 * @return - List<String> - The list of urls containing only js Har Entries
	 */
	public List<String> getJsHarEntries(HashMap<String, Integer> harMap) {
		List<String> jsHarList = new ArrayList<String>();
		for (Map.Entry<String, Integer> harEntry : harMap.entrySet()) {
			if (harEntry.getKey().contains(".js")) {
				//System.out.println(harEntry.getKey());
				jsHarList.add(harEntry.getKey());
			}
		}
		
		return jsHarList;
	}
	
	public boolean doesListContainValue(List<String> jsHarList, String value) {
		boolean found = false;
		for (String jsHar : jsHarList) {
			if(jsHar.contains(value)) {
				found = true;
				break;
			}
		}
		return found;
	}
	
	/**
	 * This method will display the contents of a given Map Object
	 * @param harMap
	 */
	public void displayMapData(HashMap<String, Integer> harMap) {
		for (Map.Entry<String, Integer> harEntry : harMap.entrySet()) {
			System.out.println(harEntry.getValue() + " : " + harEntry.getKey());
		}
	}

}
