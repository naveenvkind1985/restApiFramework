
Feature: Get Quotation


  Scenario Outline: Get Quotation from endpoint
    Given user has access to endpoint "<endpoint>"
    When user creates a quotation from JSON file "<JSONFile>"
    Then user should get the response code 200

    Examples:
      | endpoint       | JSONFile         |
      | /IIRIS_ECRM_API_UAT/webapi/MotorApi/GetQuotation | GetQuotation.json |