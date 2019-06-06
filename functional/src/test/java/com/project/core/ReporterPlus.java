package com.project.core;

import org.testng.Reporter;

public class ReporterPlus {
	
	public static void log(String input) {
		System.out.println(input);
		Reporter.log(input);
	}

}
