package com.project.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;

public class AppLib {
	private String seleniumServer = "http://localhost:4444/wd/hub";
	public static String ENV_PROD = "PROD";
	public static String ENV_QA = "QA";
	public static String ENV_DEV = "DEV";
	public static String ENV_LOCAL = "LOCAL";
	private static ConfigProperties configProperties = new ConfigProperties();

	public String getBaseUrl(String env, String url) {
		if (!allowableEnvs().contains(env)) {
			throw new RuntimeException(
					"Environment specified [ " + env + "] not supported as part of allowable enviornments");
		}

		String newBaseUrl;

		if (!env.equals(ENV_LOCAL)) {
			newBaseUrl = configProperties.getBaseSite();
		} else {
			newBaseUrl = url;
		}

		return newBaseUrl;
	}

	public String getApiHost(String apiHost, String env) {
		if (!allowableEnvs().contains(env)) {
			throw new RuntimeException(
					"Environment specified [ " + env + "] not supported as part of allowable enviornments");
		}

		String apiHostUrl = "";

		if (!env.equals(ENV_LOCAL)) {
			apiHostUrl = configProperties.getApiHost();
		} else {
			apiHostUrl = apiHost;
		}

		return apiHostUrl;
	}

	public static ArrayList<String> allowableEnvs() {
		ArrayList<String> allowedEnvs = new ArrayList<String>();
		allowedEnvs.add(ENV_PROD);
		allowedEnvs.add(ENV_QA);
		allowedEnvs.add(ENV_DEV);
		allowedEnvs.add(ENV_LOCAL);
		return allowedEnvs;
	}

	public String getSeleniumServer(String seleniumHost) {
		if (seleniumHost.isEmpty()) {
			return seleniumServer;
		} else {
			System.out.println("WARN: Default selenium server host overridden with: " + seleniumHost);
			return seleniumHost;
		}
	}

	public void reportPreTestDetails(String env, String baseUrl) {
		ReporterPlus.log("Running tests on environment: [" + env + "] - URL: " + baseUrl);
	}

	public void goToNewTab(WebDriver webDriver, int tabNum) {
		ArrayList<String> tabs2 = new ArrayList<String>(webDriver.getWindowHandles());
		ReporterPlus.log("Navigating to tab number: " + tabNum);
		webDriver.switchTo().window(tabs2.get(tabNum));
	}

	public String getValueForKey(String urlString, String key) {
		URL url;
		String queryValue = "";
		try {
			url = new URL(urlString);

			String query = url.getQuery();
			String[] params = query.split("&");
			String name = "";
			String value = "";
			Map<String, String> map = new HashMap<String, String>();
			for (String param : params) {
				name = param.split("=")[0];
				if (param.split("=").length < 2) {
					value = "";
				} else {
					value = param.split("=")[1];
				}
				map.put(name, value);
			}
			queryValue = map.get(key);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return queryValue;
	}
}
