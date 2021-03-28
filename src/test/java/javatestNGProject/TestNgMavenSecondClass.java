package javatestNGProject;

import org.testng.annotations.Test;

public class TestNgMavenSecondClass {

	 @Test
	    public void oneMoreTest() {
	        System.out.println("Test Two: This is a test in QA Env");
	    }
	 
}
//Running from command line:
//C:\Users\19378\eclipse-workspace\javaTestNGProject>mvn test -DsuiteXmlFile=testng_qa.xml