package com.javatest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class RunSeleniumTests {
	private WebDriver driver;

	@BeforeClass
	public void setUp() {
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--headless");
		driver = new ChromeDriver(options);
		driver.navigate().to("https://www.saucedemo.com/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(120, TimeUnit.MILLISECONDS);
	}

	@Test
	public void userLogin() {
	    WebElement usernameTxt = driver.findElement(By.id("user-name"));
	    usernameTxt.sendKeys("standard_user");

	    WebElement passwordTxt = driver.findElement(By.id("password"));
	    passwordTxt.sendKeys("secret_sauce");

	    WebElement loginBtn = driver.findElement(By.id("login-button"));
	    loginBtn.click();

	    System.out.println("Current URL: " + driver.getCurrentUrl());
	    System.out.println("Current title: " + driver.getTitle());

	    Assert.assertTrue(
	        driver.getCurrentUrl().contains("inventory"),
	        "User was not redirected to the inventory page"
	    );

	    WebElement productsHeading = driver.findElement(By.className("title"));

	    Assert.assertEquals(
	        productsHeading.getText(),
	        "Products",
	        "Products heading was not displayed"
	    );
	}
	@AfterClass
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}