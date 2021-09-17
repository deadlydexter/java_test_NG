package com.javatest;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.testng.annotations.Test;

public class TestNgMavenSecondClass {

	@Test
	public void oneMoreTest() {
		
		Logger logger = Logger.getLogger(TestNgMavenSecondClass.class.getName());

		String logMessage = "Test Two: This is a test in QA Env";
		logger.log(Level.INFO, logMessage);
	}

}
