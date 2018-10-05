package com.test.api.testcases;

import java.lang.reflect.Method;

import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.test.api.dataproviders.TestData;
import com.test.api.utils.TestSoftAssert;
import com.test.api.wrappers.RequestBuilder;
import com.test.api.wrappers.ResponseValidator;
import com.test.extentreport.ExtentTestManager;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class WeatherInfoServiceTests extends TestData {

	TestSoftAssert softAssert;
	ResponseValidator responseValidator;

	@BeforeClass
	public void setUp() {
		dataSheetName = "Endpoint.xlsx";
	}

	@BeforeMethod
	public void beforeMethod(Method method) {
		softAssert = new TestSoftAssert();
		responseValidator = new ResponseValidator(softAssert, responseJsonDirectory);
	}

	@Test(dataProvider = "testData", testName = "Tests to verify Product Info API.")
	public void InfoService(String testId, String endpoint, String skip, String targetUrl, String httpMethod,
			String formParams, String requestHeaders, String requestJsonBody, String expectedStatus,
			String expectedContentType, String expectedJsonBody, String jsonPath, String dbQuery,
			String expectedDBValues, Method method) throws Exception {

		try {
			if (skip.equalsIgnoreCase("yes")) {
				try {
					throw new SkipException("Skipped Test for " + testId + " :" + endpoint);
				} catch (Exception e) {
					throw e;
				}
			}

			ExtentTestManager.createTest(testId, testId);
			RequestSpecification request = new RequestBuilder(requestJsonDirectory).addFormParams(formParams)
					.addHeaders(requestHeaders).addRequestBody(requestJsonBody).build();

			Response response = request.when().request(httpMethod, targetUrl + endpoint);

			if (response.getStatusCode() == 200) {

				System.out.println(response.getBody().asString());

				responseValidator.verifyContentType(response, expectedContentType)
						.verifyStatusCode(response, expectedStatus).fetchResponseBodyTime(response);

			} else {
				responseValidator.verifyErrorDetails(response, expectedStatus);
				throw new AssertionError();
			}
		} catch (Exception e) {
			ExtentTestManager.getTest().log(Status.FAIL, "Fails to Fetch Repsonse from the API");
			throw (e);
		}

		softAssert.assertAll();
	}
}
