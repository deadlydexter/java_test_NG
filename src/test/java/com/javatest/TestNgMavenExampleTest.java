package com.javatest;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class TestNgMavenExampleTest {
	String test;
//	@Parameters({ "suite-param" })
//	@BeforeTest
//	public void prameterBeforeTest(String param) {
//        System.out.println("Test one suite param is: " + param);
//        test = param;
//    }
	
	@DataProvider(name="scenarios")
	public static Object[][] scenarios(){
		return new Object[][] {{"TEstOne"},{"TEstTwo"},{"TEstTHREE"},{"TEstfour"}};
	}
    @Test(dataProvider = "scenarios")
    public void prameterTestOne(String param) {
        System.out.println("Test one suite param is: " + param);
        System.out.println(test);
    }
    @Test
    public void oneMoreTest() {
        System.out.println("Test Two: This is a test in QA Env");
//        org.testng.Assert.fail("Test Failed !!");
    }
}
//run this from command prompt: mvn -Dtest=TestNgMavenExampleTest test