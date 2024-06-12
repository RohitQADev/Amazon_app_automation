package automationRun;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;

public class reportingUtil {

	String date;
	LocalDateTime time = LocalDateTime.now();

	static ExtentReports rptExtent = new ExtentReports();
	static ExtentTest rpttest;
	static ExtentTest rpttest1;

	public static String capture(WebDriver driver) throws IOException {
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		File Dest = new File("./Reports//" +"//Screenshot" + "//TC_Screenshots" + System.currentTimeMillis() + ".png");
		String errflpath = Dest.getAbsolutePath();
		FileUtils.copyFile(scrFile, Dest);
		return errflpath;
	}

	public String getTodaysDate() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yy");
		String strDate = formatter.format(date);

		return strDate;
	}

	
	public void CreateReport(String filename) {
		File f = new File(System.getProperty("user.dir") + "//" + "./Reports//" + "//" + filename);
		if (f.exists() && !f.isDirectory()) {
			try {
				Path temp = Files.move(
						Paths.get(System.getProperty("user.dir") + "//" + "./Reports//" + "//" + filename),
						Paths.get(System.getProperty("user.dir") + "//" + "./Reports//" + "//Archive//" + filename
								+ time.toString().replace(":", "_") + ".html"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ExtentSparkReporter spark = new ExtentSparkReporter(
				System.getProperty("user.dir") + "//" + "./Reports//" + "//" + filename);
		spark.config().setTheme(Theme.STANDARD);
		spark.viewConfigurer().viewOrder().as(new ViewName[] { ViewName.DASHBOARD, ViewName.TEST }).apply();
		spark.filter().statusFilter();

		rptExtent.attachReporter(spark);
		rptExtent.setSystemInfo("Build", "30.0.1");

	}

	public void CreateTest(String testName) {
		rpttest = rptExtent.createTest(testName);
	}

	public void CreateNode(String nodeName) {
		rpttest1 = rpttest.createNode(nodeName);
	}

	public void addPassLog(String statement, RemoteWebDriver driver) {
		try {
			rpttest1.pass(statement)
					.pass(MediaEntityBuilder.createScreenCaptureFromPath(reportingUtil.capture(driver)).build());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addFailLog(String statement, RemoteWebDriver driver) {
		try {
			rpttest1.fail(statement)
					.pass(MediaEntityBuilder.createScreenCaptureFromPath(reportingUtil.capture(driver)).build());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void FlushReport() {
		rptExtent.flush();
	}

}
