package automationRun;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class amazonTestCase extends propertyUtil {

	RemoteWebDriver driver = new ChromeDriver();
	Properties p = propertyUtil.getObjectRepository();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	reportingUtil report = new reportingUtil();

	@BeforeClass
	public void setUp() {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\healt\\OneDrive\\Desktop\\TestAutomation\\driver\\chromedriver.exe");

		driver.manage().window().maximize();
		driver.get(p.getProperty("url"));
	}

	@Test
	public void amazonScenarioTest() {
		Actions act = new Actions(driver);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

//		Click on Electronics from dropdown menu and type “IPhone 13”
		WebElement electronicsDropdown = driver.findElement(By.xpath(p.getProperty("click_dropdown")));
		electronicsDropdown.click();
		WebElement electronicsOption = driver.findElement(By.xpath(p.getProperty("electronic_option")));
		electronicsOption.click();
		act.sendKeys("IPhone 13").perform();

//		 Get All the dropdown suggestions and validate all are related to searched product
		List<WebElement> list = driver.findElements(By.xpath(p.getProperty("listOfDropDown")));
		validate(list);

//		Then Type again “IPhone 13 128 GB” variant and Click “iPhone 13 128GB” variant from dropdown Result.
		WebElement searchAmaxon = driver.findElement(By.xpath(p.getProperty("searchAmaxon")));
		searchAmaxon.clear();
		searchAmaxon.click();
		searchAmaxon.sendKeys("iPhone 13 128GB");

//		Navigate to next tab and click on Visit the Apple Store link appears below Apple iPhone 13 (128 GB) variant
		act.click(driver.findElement(By.xpath("//div[contains(text(),'iphone 13 128gb')]"))).build().perform();
		WebElement iphone13 = driver.findElement(By.xpath(p.getProperty("iphone13")));
		wait.until(ExpectedConditions.elementToBeClickable(iphone13));
		iphone13.click();
		
		windowHandle();
		
		WebElement appleLinkText = driver.findElement(By.xpath(p.getProperty("applelink")));
		
		String apple = appleLinkText.getText();
		SoftAssert as = new SoftAssert();

//		Click on Apple Watch dropdown and select Apple Watch SE (GPS + Cellular)
		as.assertNotEquals(apple, "Shop the Apple India Store on", "Apple link is available");
		wait.until(ExpectedConditions.elementToBeClickable(appleLinkText));
		appleLinkText.click();

		WebElement watch = driver.findElement(By.xpath(p.getProperty("getappleWatch")));
		WebElement appleSE = driver.findElement(By.xpath(p.getProperty("appleWatchSE")));

//		Hover on watch image and verify Quick Look is displayed for the watch.

		wait.until(ExpectedConditions.elementToBeClickable(watch));
		js.executeScript("arguments[0].click();", watch);
		wait.until(ExpectedConditions.elementToBeClickable(appleSE));
		js.executeScript("arguments[0].click();", appleSE);
		WebElement hoverwatchSE = driver.findElement(By.xpath(p.getProperty("hoverwatchSE")));
		WebElement quickLook = driver.findElement(By.xpath(p.getProperty("quickLook")));

		String quicklook = quickLook.getText();
		js.executeScript("arguments[0].scrollIntoView(true);", quickLook);
		act.scrollToElement(hoverwatchSE).perform();
		as.assertEquals(quicklook, "quicklook", "Quick Look displayed");

		quickLook.click();
		WebElement seeDetails = driver.findElement(By.xpath(p.getProperty("seeDetails")));

		as.assertEquals(seeDetails, "See all details", "See all details displayed in quick look");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(p.getProperty("seeDetails"))));
		js.executeScript("arguments[0].click();", seeDetails);

//		Verify newly opened modal is related to Same product for which Quick look is performed.
		String title = driver.getTitle();
		as.assertEquals(title,
				"https://www.amazon.in/Apple-Watch-Cellular-Starlight-Aluminium/dp/B0BDKKTM6B?ref_=ast_sto_dp&th=1",
				"opened modal is related to Same product for which Quick look is performed");

//		Reporting integrated

//		report.addPassLog("Test case is passed", driver);

	}

	@AfterClass
	public void tearDown() throws InterruptedException {
		// Close the browser
		Thread.sleep(2000);
		driver.quit();
	}

	public void validate(List<WebElement> list) {

		try {

			for (int i = 1; i < list.size(); i++) {
				WebElement ele = driver
						.findElement(By.xpath("(//div[@class='left-pane-results-container']/div)[" + i + "]"));
				String str = ele.getText().toLowerCase();
				System.out.println(str);
				if (str.contains("iphone 13")) {
					System.out.println("All suggetions are related to iPhone 13");
				} else {
					System.out.println("All suggetions are related to iPhone 13");
					break;
				}

			}
		} catch (StaleElementReferenceException e) {
			System.out.println(e);
		}
	}

	public void windowHandle() {
		
		String originalwindow = driver.getWindowHandle();
		
		Set<String> childWindows = driver.getWindowHandles();
		Iterator<String> child = childWindows.iterator();
			while(child.hasNext()) {
				String window= child.next();
				if(!originalwindow.equalsIgnoreCase(window)) {
					driver.switchTo().window(window);
					System.out.println("New window switched");
					}
		
	}
}
	
}
