package com.project.test;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.project.core.AppLib;

public class ApiBaseTest {
	protected String environment;
	protected AppLib appLib = new AppLib();
	protected String apiHost;

	@BeforeClass(alwaysRun = true)
	@Parameters({ "Environment", "AppHost" })
	public void setup(String env, @Optional("") String appHost) {
		environment = env;
		apiHost = appLib.getApiHost(appHost, env);
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() {
		// close any open connections
	}
}
