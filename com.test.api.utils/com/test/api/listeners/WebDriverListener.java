package com.test.api.listeners;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.openqa.selenium.Platform;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.Status;
import com.test.api.utils.Constants;
import com.test.extentreport.ExtentManager;
import com.test.extentreport.ExtentTestManager;

public class WebDriverListener extends ExternalConfig implements IInvokedMethodListener, ITestListener {

	/**
	 * Prop object instance
	 */
	static Properties prop;

	/**
	 * Invoked after the test class is instantiated and before any configuration
	 * method is called 1. Method call to set the suite name before execution starts
	 * and set the brand name for extent report name 2. Loading the properties file
	 * at once, that can be used anywhere on classes.
	 */
	@Override
	public void onStart(ITestContext context) {

		if (getInstance() == null)
			prop = getProperties(Constants.BASE_CONFIG_FILE_KEY);
	}

	/**
	 * This method would be invoked before any beforeXX/AfterXX methods Condition:
	 * If method is beforeXX/AfterXX then create an instance of the driver and set
	 * the driver
	 * 
	 * @return
	 */
	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult iTestResult) {
		
		if (iTestResult.getMethod().isBeforeClassConfiguration()
				|| iTestResult.getMethod().isBeforeMethodConfiguration()) {
			
			if (ExtentManager.getInstance() == null) {
				ExtentManager.getSuiteName(method);

				// MAC or Windows Selection for excel path
				if (Platform.getCurrent().toString().equalsIgnoreCase("MAC")) {
					ExtentManager.createInstance(Constants.USER_DIRECTORY + "//extentreports//"
							+ ExtentManager.getTestSuiteName() + "AutomationReport" + ".html");
				} else if (Platform.getCurrent().toString().contains("WIN")) {
					ExtentManager.createInstance(Constants.USER_DIRECTORY + "\\extentreports\\"
							+ ExtentManager.getTestSuiteName() + "AutomationReport" + ".html");
				}
			}
		}
	}

	/**
	 * This method would be invoked after any beforeXX/AfterXX method Condition: If
	 * invoked method is test method then log the results in extent report and close
	 * the driver
	 */
	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult iTestresult) {
		//
	}

	/**
	 * Invoked after all the tests have run and all their Configuration methods have
	 * been called.
	 */
	@Override
	public void onFinish(ITestContext context) {
		ExtentManager.getInstance().flush();

		zipExtentReport();
	}

	/**
	 * Method to log test status for pass, fail, skipped, and info
	 * 
	 * @param iTestResult
	 * @throws Exception
	 */
	public void getresult(ITestResult iTestResult) throws Exception {
		try {
			if (iTestResult.getStatus() == ITestResult.SUCCESS) {
			} else if (iTestResult.getStatus() == ITestResult.SKIP) {
				ExtentTestManager.getTest().log(Status.SKIP, iTestResult.getMethod().getMethodName()
						+ " test is skipped and skip reason is:-" + iTestResult.getThrowable());
			} else if (iTestResult.getStatus() == ITestResult.FAILURE) {
				ExtentTestManager.getTest().log(Status.ERROR,
						iTestResult.getName() + " | Exception: " + iTestResult.getThrowable());
			} else if (iTestResult.getStatus() == ITestResult.STARTED) {
				ExtentTestManager.getTest().log(Status.INFO, iTestResult.getName() + " test is started");
			}
		} catch (Exception e) {
			throw(e);
		}
	}

	/**
	 * Invoked each time before a test will be invoked.
	 */
	@Override
	public void onTestStart(ITestResult iTestResult) {
		//
	}

	/**
	 * Invoked each time a test succeeds.
	 */
	@Override
	public void onTestSuccess(ITestResult iTestResult) {
		//
	}

	/**
	 * Invoked each time a test fails.
	 */
	@Override
	public void onTestFailure(ITestResult iTestResult) {
		//
	}

	/**
	 * Invoked each time a test is skipped
	 */
	@Override
	public void onTestSkipped(ITestResult iTestResult) {
		//
	}

	/**
	 * Invoked each time a method fails but has been annotated with
	 * successPercentage and this failure still keeps it within the success
	 * percentage requested.
	 */
	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
		//
	}

	public static void zipExtentReport() {
		try {

			String filePath = System.getProperty("user.dir") + "\\extentreports\\";
			File file = new File(filePath + ExtentManager.getTestSuiteName() + " API Automation Status Report.html");

			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
			formater.format(calendar.getTime());
			String zipFileName = filePath + "archives\\" + ExtentManager.getTestSuiteName() + " API Automation Status Report_"
					+ formater.format(calendar.getTime()) + ".zip";

			addToZipFile(file, zipFileName);

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public static void addToZipFile(File file, String zipFileName) throws IOException {
		FileOutputStream fos = new FileOutputStream(zipFileName);
		ZipOutputStream zos = new ZipOutputStream(fos);
		FileInputStream fis = new FileInputStream(file);
		try {
			ZipEntry zipEntry = new ZipEntry(file.getName());
			zos.putNextEntry(zipEntry);

			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) > 0) {
				zos.write(bytes, 0, length);
			}
			
			System.out.println(file.getCanonicalPath() + " is zipped to " + zipFileName);
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			zos.closeEntry();
			zos.close();
			fis.close();
			fos.close();
		}
	}
}