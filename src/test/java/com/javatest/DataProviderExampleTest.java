package com.javatest;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DataProviderExampleTest {

    /*
     * The DataProvider supplies test data to the test method.
     *
     * Each inner Object[] represents one test execution.
     * This DataProvider contains four rows, so the test method
     * will execute four times.
     */
    @DataProvider(name = "testScenarios")
    public Object[][] provideTestScenarios() {

        return new Object[][]{
                {"Test Scenario One"},
                {"Test Scenario Two"},
                {"Test Scenario Three"},
                {"Test Scenario Four"}
        };
    }

    /*
     * The dataProvider value must match the name specified
     * in the @DataProvider annotation.
     *
     * The method parameter receives one value from each
     * DataProvider row.
     */
    @Test(dataProvider = "testScenarios")
    public void runTestScenario(String scenarioName) {

        System.out.println("Running test: " + scenarioName);
    }
    
    @Test
    public void verifyAddition() {
        int actualResult = 2 + 3;
        int expectedResult = 5;

        org.testng.Assert.assertEquals(actualResult, expectedResult);
    }

}
//run this from command prompt: mvn -Dtest=TestNgMavenExampleTest test