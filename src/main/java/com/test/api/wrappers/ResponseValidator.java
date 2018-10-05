package com.test.api.wrappers;

import java.io.IOException;
import java.util.Map;

import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.test.api.utils.FileReaderUtil;
import com.test.api.utils.ParamMapper;
import com.test.api.utils.TestSoftAssert;
import com.test.extentreport.ExtentTestManager;

import io.restassured.response.Response;

/**
 * 
 * Response Validator for RestAssured Response objects
 */

public class ResponseValidator extends TestSoftAssert {

	String responseJsonDirectory;
	SoftAssert softAssert;
	ExtentTest test;

	public ResponseValidator(SoftAssert softAssert, String responseJsonDirectory) {
		this.responseJsonDirectory = responseJsonDirectory;
		this.test = ExtentTestManager.getTest();
		this.softAssert = softAssert;
	}

	/**
	 * Validates the status code of a RestAssured Response object. Takes two
	 * parameters, a Response Object and String value of expected status code
	 * Performs a soft assert to verify if staus code of Response and expected
	 * status code match validate() method must be called to perform the assertion.
	 * Ignores if expectedStatusCode is an empty String.
	 * 
	 * @throws IOException
	 */
	public ResponseValidator verifyStatusCode(Response response, String expectedStatusCode)
			throws IOException, Exception {
		if (!expectedStatusCode.isEmpty()) {
			try {

				/*
				 * Assert.assertEquals("" + response.getStatusCode(), expectedStatusCode);
				 * test.log(Status.PASS, "Status code matches " +
				 * expectedStatusCode);
				 */

				softAssertEquals(response.getStatusCode() + "", expectedStatusCode,
						"Actual Status Code is not same as Expected <br> Actual Status Code from the Response is not as :- "
								+ response.getStatusCode() + " <br> Expected Response Code is as :- "
								+ expectedStatusCode);

			} catch (AssertionError ae) {
				softAssert.fail(ae.getMessage());
				test.log(Status.FAIL, "Status Code: " + ae.getMessage());
			}
		}

		return this;
	}

	/**
	 * Validates the content type of a RestAssured Response object. Takes two
	 * parameters, a Response Object and String value of expected content type
	 * Performs a soft assert to verify if content type of Response and expected
	 * content type match validate() method must be called to perform the assertion.
	 * Ignores if expectedContentType is an empty String.
	 * 
	 * @throws Exception
	 * @throws IOException
	 */
	public ResponseValidator verifyContentType(Response response, String expectedContentType)
			throws IOException, Exception {
		if (!expectedContentType.isEmpty()) {
			try {

				softAssertEquals(response.getContentType().toLowerCase().trim(),
						expectedContentType.toLowerCase().trim(),
						"Actual content Type is not same as Expected <br> Actual Content Type is as :-  <br> "
								+ response.getContentType().toUpperCase() + " <br> Expected Content Type is as :- <br>"
								+ expectedContentType.toUpperCase());

			} catch (AssertionError ae) {
				softAssert.fail(ae.getMessage());
				test.log(Status.FAIL, "Content-Type : " + ae.getMessage());
			}
		}
		return this;
	}

	/**
	 * Validates the Error type, Code & Message of a RestAssured Response object.
	 * Takes two parameters, a Response Object and String value of expected content
	 * type Performs a soft assert to verify if content type of Response and
	 * expected content type match validate() method must be called to perform the
	 * assertion. Ignores if expectedContentType is an empty String.
	 * 
	 * @throws Exception
	 * @throws IOException
	 */
	public ResponseValidator verifyErrorDetails(Response response, String expectedStatusCode)
			throws IOException, Exception {
		try {

			softAssertEquals(expectedStatusCode, response.getStatusCode() + "",
					"Actual status code is not as same as exepcted <br> Actual Status Code is as :-  <br> "
							+ response.getStatusCode() + " <br> Expected Status Code is as :- <br>"
							+ expectedStatusCode);

			softAssertEquals(response.getStatusLine(), "HTTP/1.1 200 OK",
					"Actual Status Line is not same as Expected <br> Actual Status Line from resposne is as :- <br> "
							+ response.getStatusLine()
							+ "<br> Expected Status Line From Response is as <br> HTTP/1.1 200 OK");

			ExtentTestManager.getTest().log(Status.ERROR, "Error response :- " + response.getBody().asString());

		} catch (AssertionError ae) {
			softAssert.fail(ae.getMessage());
			test.log(Status.FAIL, "Content-Type : " + ae.getMessage());
		}
		return this;
	}

	/**
	 * Fetches the Response time and Body of a RestAssured Response object. Takes
	 * two parameters, a Response Object and String value of expected content type
	 * Performs a soft assert to verify if content type of Response and expected
	 * content type match validate() method must be called to perform the assertion.
	 * Ignores if expectedContentType is an empty String.
	 */
	public ResponseValidator fetchResponseBodyTime(Response response) {
		try {

			ExtentTestManager.getTest().log(Status.PASS,
					"Fetched Response Time is as :  <br>" + response.time() + " ms");

			ExtentTestManager.getTest().log(Status.PASS,
					"Fetched Response Body is as :  <br>" + response.getBody().asString());

		} catch (AssertionError ae) {
			softAssert.fail(ae.getMessage());
			test.log(Status.FAIL, "Content-Type : " + ae.getMessage());
		}
		return this;
	}

	/**
	 * Validates the body of a RestAssured Response object. Takes two parameters, a
	 * Response Object and String value of expected body If expectedBody starts with
	 * '{' it is treated as a JSON string. Otherwise searches for a JSON file with
	 * the same name in the path'jsonResponseDirectory'.
	 */
	public ResponseValidator verifyJsonBody(Response response, String expectedBody) {

		if (!expectedBody.isEmpty()) {
			expectedBody = expectedBody.trim();
			String expectedJson;
			if (expectedBody.startsWith("{")) {
				expectedJson = expectedBody;
			} else {
				// get from file
				try {

					expectedJson = new FileReaderUtil().readJsonFile(responseJsonDirectory + expectedBody);

				} catch (Exception e) {
					test.log(Status.WARNING,
							expectedBody + " not found. Skipping JSON Body validation");
					return this;
				}
			}

			String actualJson = response.getBody().asString();
			try {

				JSONAssert.assertEquals(expectedJson, actualJson, false);
				test.log(Status.PASS, "Expected and Actual JSON body Match");
			} catch (AssertionError ae) {
				softAssert.fail(ae.getMessage());
				test.log(Status.FAIL, "In Json Body " + ae.getMessage());
			} catch (JSONException e) {
				test.log(Status.WARNING, "Malformed JSON found " + e.getMessage());
			}
		}
		return this;
	}

	public ResponseValidator verifyJsonPath(Response response, String jsonPaths) {

		if (!jsonPaths.isEmpty()) {
			Map<String, String> expectedJsonPathMap;
			try {
				expectedJsonPathMap = new ParamMapper().map(jsonPaths);
			} catch (Exception e) {
				test.log(Status.WARNING,
						"Failed to parse Json Path expected values. Skipping verification");
				return this;
			}
			for (String key : expectedJsonPathMap.keySet()) {
				try {

					String actualValue = JsonPath.parse(response.getBody().asString()).read(key, String.class);
					String expectedValue = expectedJsonPathMap.get(key);

					try {
						Assert.assertEquals(actualValue, expectedValue);
						test.log(Status.PASS,
								"Expected and Actual values matches for element : " + key + "<br> Expected Value : "
										+ expectedValue + "<br> Actual Value : " + actualValue);

					} catch (AssertionError ae) {
						softAssert.fail(ae.getMessage());
						// test.log(Status.FAIL,"Json Path " + key + " :" +
						// ae.getLocalizedMessage());

						test.log(Status.FAIL,
								"Expected and Actual values do not match for element : " + key
										+ "<br> Expected Value : " + expectedValue + "<br> Actual Value : "
										+ actualValue);
					}

				} catch (PathNotFoundException e) {
					softAssert.fail(e.getMessage());
					test.log(Status.FAIL, "Json Path " + key + " not found in response body");
				}
			}
		}

		return this;
	}

}
