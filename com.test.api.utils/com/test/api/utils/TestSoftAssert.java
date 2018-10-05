package com.test.api.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.Status;
import com.test.extentreport.ExtentTestManager;

public class TestSoftAssert extends SoftAssert {

	List<String> messages = new ArrayList<>();

	@Override
	public void onAssertFailure(IAssert<?> assertCommand, AssertionError ex) {
		messages.add("onAssertFailure");
	}

	@Override
	public void assertAll() {
		try {
			messages.add("assertAll");
			super.assertAll();
		} catch (AssertionError e) {
			throw e;
		}
	}

	/**
	 * Assertion method to Soft Assert the validation and log the respective result.
	 * 
	 * @param condition,
	 *            failureMessage
	 * @throws IOException
	 * @throws Exception
	 */

	public synchronized void softAssertTrue(Boolean condition, String failureMessage) throws IOException, Exception {

		this.assertTrue(condition, failureMessage);
		if (this.messages.contains("onAssertFailure")) {
			ExtentTestManager.getTest().log(Status.FAIL, failureMessage + "<br> FAILED AT METHOD: "
					+ Thread.currentThread().getStackTrace()[2].getMethodName());
		} else {
			ExtentTestManager.getTest().log(Status.PASS, failureMessage.replace("not", ""));
		}

		messages.clear();
	}

	/**
	 * Assertion method to Soft Assert the validation and log the respective result.
	 * 
	 * @param condition,
	 *            failureMessage
	 * @throws IOException
	 * @throws Exception
	 */

	public synchronized void softAssertFalse(Boolean condition, String failureMessage) throws IOException, Exception {

		this.assertFalse(condition, failureMessage);
		if (this.messages.contains("onAssertFailure")) {
			ExtentTestManager.getTest().log(Status.FAIL, failureMessage + "<br> FAILED AT METHOD: "
					+ Thread.currentThread().getStackTrace()[2].getMethodName());
		} else {
			ExtentTestManager.getTest().log(Status.PASS, failureMessage.replace("not", ""));
		}

		messages.clear();
	}

	/**
	 * Assertion method to Soft Assert the validation and log the respective result.
	 * 
	 * @param condition,
	 *            failureMessage
	 * @throws IOException
	 * @throws Exception
	 */

	public synchronized void softAssertEquals(String actual, String expected, String failureMessage)
			throws IOException, Exception {

		this.assertEquals(actual, expected, failureMessage);
		if (this.messages.contains("onAssertFailure")) {
			ExtentTestManager.getTest().log(Status.FAIL, failureMessage + "<br> FAILED AT METHOD: "
					+ Thread.currentThread().getStackTrace()[2].getMethodName());
		} else {
			ExtentTestManager.getTest().log(Status.PASS, failureMessage.replace("not", ""));
		}

		messages.clear();
	}

	/**
	 * Assertion method to Soft Assert the validation and log the respective result.
	 * 
	 * @param condition,
	 *            failureMessage
	 * @throws IOException
	 * @throws Exception
	 */

	public synchronized void softAssertNotEquals(String actual, String expected, String failureMessage)
			throws IOException, Exception {

		this.assertNotEquals(actual, expected, failureMessage);
		if (this.messages.contains("onAssertFailure")) {
			ExtentTestManager.getTest().log(Status.FAIL, failureMessage + "<br> FAILED AT METHOD: "
					+ Thread.currentThread().getStackTrace()[2].getMethodName());
		} else {
			ExtentTestManager.getTest().log(Status.PASS, failureMessage.replace("not", ""));
		}

		messages.clear();
	}

	public void softAssertIntEquals(float actual, float expected, String failureMessage) throws Exception, Exception {
		// TODO Auto-generated method stub
		this.assertEquals(actual, expected, failureMessage);
		if (this.messages.contains("onAssertFailure")) {
			ExtentTestManager.getTest().log(Status.FAIL, failureMessage + "<br> FAILED AT METHOD: "
					+ Thread.currentThread().getStackTrace()[2].getMethodName());
		} else {
			ExtentTestManager.getTest().log(Status.PASS, failureMessage.replace("not", ""));
		}

		messages.clear();
	}
}
