package com.test.extentreport;

import java.io.File;

import org.openqa.selenium.Platform;
import org.testng.IInvokedMethod;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * This class contains all the methods related to extent report configuration
 * 
 * @author HMarkam
 *
 */
public class ExtentManager {

	static ExtentReports extent;
	private static String testContext;

	/**
	 * Method to return extent report instance
	 * 
	 * @return extent
	 */
	public static ExtentReports getInstance() {
		return extent;
	}

	/**
	 * Method to return test context as a suite name This suite name will be
	 * displayed on extent report header per brand(Catherines, dressbarn, maurices,
	 * justice, lanebryant etc.)
	 * 
	 * @return testContext
	 */
	public static synchronized String getTestSuiteName() {
		return testContext;
	}

	/**
	 * Method to get suite name from test context and set to testContext
	 * 
	 * 
	 * @param ctx
	 */
	public static void getSuiteName(IInvokedMethod method) {
		testContext = method.getTestMethod().getTestClass().getXmlTest().getSuite().getName();
	}

	/**
	 * ----------------------------Extent Report
	 * configuration--------------------------------- This method will create an
	 * instance of extent report and set few properties for extent report
	 * 
	 * @param fileName
	 * @return extent
	 */
	public static synchronized ExtentReports createInstance(String extentHtmlFileName) {
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(extentHtmlFileName);
		htmlReporter.loadXMLConfig(new File(System.getProperty("user.dir") + "\\config\\xml\\extent-config.xml"));

		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);

		extent.setSystemInfo("OS", Platform.getCurrent().toString());
		extent.setSystemInfo("Host Name", "Naveen");
		extent.setSystemInfo("Environment", "QA");
		extent.setSystemInfo("User Name", System.getProperty("user.name"));

		htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.config().setDocumentTitle(getTestSuiteName() + " API Automation Status Report");
		htmlReporter.config().setReportName(getTestSuiteName() + " API Automation Status Report");
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		htmlReporter.config().setTheme(Theme.DARK);

		return extent;
	}

}
