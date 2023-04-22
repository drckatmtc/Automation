package test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class FBSignUpExtentReport {
	WebDriver driver;
	ExtentSparkReporter htmlReporter;
	ExtentReports report;
	ExtentTest test;

	String fName = "Derrick";
	String lName = "Bragais";
	String phone = "+679123456789";
	String password = "TestPassword_12345";

	@BeforeSuite
	public void setup() {
		htmlReporter = new ExtentSparkReporter("FBExtentReport.html");
		report = new ExtentReports();
		report.attachReporter(htmlReporter);
		driver = new ChromeDriver();
	}

	@Test
	public void signUpTest() throws InterruptedException {

		test = report.createTest("Facebook Sign Up Test", "test to validate sign up ");
		test.log(Status.INFO, "Starting test case");
		driver.manage().window().maximize();
		driver.get("https://www.facebook.com/");
		Thread.sleep(1000);

		driver.findElement(By.xpath("//a[@data-testid='open-registration-form-button']")).click();
		Thread.sleep(3000);

//		driver.findElement(By.xpath("//input[@name='firstname']")).sendKeys(fName);

		WebElement fNameElement = driver.findElement(By.xpath("//input[@name='firstname']"));

		WebElement lNameElement = driver.findElement(By.xpath("//input[@name='lastname']"));
		lNameElement.sendKeys(lName);

		if (fNameElement.getAttribute("value").isEmpty()) {
			test.skip("First name is empty!");
		} else if (!lNameElement.getAttribute("value").isEmpty()) {
			test.pass("Lastname name has entered!");
		}

		WebElement phoneElement = driver.findElement(By.xpath("//input[@name='reg_email__']"));
		phoneElement.sendKeys(phone);
		if (phoneElement.getAttribute("value").startsWith("+639")) {
			test.pass("Phone number format is correct");
		} else {
			test.fail("Phone number format is incorrect");
		}

		driver.findElement(By.xpath("//input[@name='reg_passwd__']")).sendKeys(password);

		Select monthSelect = new Select(driver.findElement(By.xpath("//select[@name='birthday_month']")));
		monthSelect.selectByVisibleText("Apr");
		Select dateSelect = new Select(driver.findElement(By.xpath("//select[@name='birthday_day']")));
		dateSelect.selectByVisibleText("27");
		Select yearSelect = new Select(driver.findElement(By.xpath("//select[@name='birthday_year']")));
		yearSelect.selectByVisibleText("1997");
		test.pass("Birthdate has entered");

		driver.findElement(By.xpath("//label[normalize-space()='Custom']")).click();

		WebElement pronounElement = driver.findElement(By.xpath("//select[@name='preferred_pronoun']"));
		Select pronounSelect = new Select(pronounElement);
		pronounSelect.selectByVisibleText("They: \"Wish them a happy birthday!\"");
		if (pronounElement.getAttribute("value").isEmpty()) {
			test.fail("Select pronoun");
		} else {
			test.pass("Selected a pronoun");
		}

//		driver.findElement(By.xpath("//button[@name='websubmit']")).click(); // sign up button
		Thread.sleep(3000);
	}

	@AfterSuite
	public void tearDown() {
		driver.quit();
		test.info("Test completed");

		// write results into the file
		report.flush();
	}
}
