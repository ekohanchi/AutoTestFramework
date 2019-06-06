package com.project.test.tech;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.project.test.WebBaseTest;

public class VerifySearchReturnsResults extends WebBaseTest {
	private By searchField = By.name("q");
	private By searchButton = By.xpath("//input[@value='Google Search'][1]");

	@Test(groups = { "full" })
	public void test() {
		String searchQuery = "technology";
		
		appLib.reportPreTestDetails(environment, baseUrl);

		webDriver.navigate().to(baseUrl);
		webDriver.findElement(searchField).sendKeys(searchQuery);
		webDriver.findElement(searchButton).click();
		String searchPageTitle = webDriver.getTitle();
		Assert.assertEquals(searchPageTitle, searchQuery + " - Google Search",
				"Page title of search page does not match expected value");
	}
}
