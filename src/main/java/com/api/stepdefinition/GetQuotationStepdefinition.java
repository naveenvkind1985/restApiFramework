package com.api.stepdefinition;

import com.api.utils.JsonReader;
import com.api.utils.TestContext;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import static org.junit.Assert.assertEquals;

public class GetQuotationStepdefinition {
	private TestContext context;
	private static final Logger LOG = LogManager.getLogger(GetQuotationStepdefinition.class);
	public GetQuotationStepdefinition(TestContext context) {
		this.context = context;
	}
	@Given("user has access to endpoint {string}")
	public void userHasAccessToEndpoint(String endpoint) {
		context.session.put("endpoint", endpoint);
	}

	@When("user creates a quotation from JSON file {string}")
	public void userCreatesAQuotationUsingDataFromJSONFile(String JSONFile) {
		context.response = context.requestSetup().body(JsonReader.getRequestBody(JSONFile))
			.when().post(context.session.get("endpoint").toString());
    ExtentCucumberAdapter.addTestStepLog("Test Response is as :- " + context.response.prettyPrint());
		//System.out.println("Response :- " + context.response);
	}
	@Then("user should get the response code {int}")
	public void userShouldGetTheResponseCode(Integer statusCode) {
		assertEquals(Long.valueOf(statusCode), Long.valueOf(context.response.getStatusCode()));
    ExtentCucumberAdapter.addTestStepLog("Test Stastu Code is as :- " + context.response.getStatusCode());
	}

}
