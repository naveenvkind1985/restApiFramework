package com.test.api.wrappers;

import static io.restassured.RestAssured.given;

import com.aventstack.extentreports.Status;
import com.test.api.utils.FileReaderUtil;
import com.test.api.utils.ParamMapper;
import com.test.extentreport.ExtentTestManager;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * 
 * Request Builder
 */
public class RequestBuilder {

	private RequestSpecification specification;
	private ParamMapper paramMapper;
	private String requestJsonDir;

	public RequestBuilder(String requestJsonDir) {

		System.out.println("Building Request");

		this.requestJsonDir = requestJsonDir;
		paramMapper = new ParamMapper();
		specification = given();
	}

	public RequestSpecification build() {

		specification.log().all();
		ExtentTestManager.getTest().log(Status.INFO, "Built Request");
		return this.specification;
	}

	public RequestBuilder addFormParams(String formParams) {
		if (!formParams.isEmpty()) {
			specification.formParams(paramMapper.map(formParams));

		}
		return this;
	}

	public RequestBuilder addHeaders(String headers) {
		if (!headers.isEmpty()) {
			specification.headers(paramMapper.map(headers));

		}
		return this;
	}

	public RequestBuilder addRequestBody(String body) {
		if (!body.isEmpty()) {

			specification.contentType(ContentType.JSON);

			body = body.trim();
			if (body.startsWith("{")) {
				specification.body(body);

			} else {

				try {
					String requestJson = new FileReaderUtil().readJsonFile(requestJsonDir + body);
					specification.body(requestJson);

				} catch (Exception e) {
					ExtentTestManager.getTest().log(Status.WARNING,
							"Could not find JSON file for adding to Request Body");
					return this;
				}
			}

		}
		return this;
	}

}
