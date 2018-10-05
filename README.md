Welcome to the Selenium-JAVA-API-TDD-Framework
This project is created with one of the Open Source Automation tools such as Selenium, TestNG, Extent Report, Maven, and JAVA with TDD approach. 
Let’s get into a few details of this API project create on to tests to get weather using Open Weather API with the below URL
http://openweathermap.org/current.  But the sample API's which you see in the UI of this page won’t be working as its not having any API id's to fetch you any details.
To get the proper Endpoints of the sample API's one has to inspect the element and get the proper endpoints from the source file itself.
Approach
	This has been implemented with latest version of Selenium and Maven ensuring that all dependencies & plug-in will be installed in your local the moment this project is cloned in local system.
	Moreover , it has been designed with one of the very popular and latest design patterns "Page Object Model" with "Page factory" which brings out a flavor of well readability , re-usability and easy maintenance as if it goes for continuous development and integration down the line.
	Besides, this will explain that how to remove your all static data in your test files and keep into one file which is ".properties file" which should work as central object repository for all your test data and makes it easy to update/ modify your data with any future changes at one place rather in multiple files . 
	Last, but not least! Logs have been implemented across the project with Extent report which will make it feasible for debugging phase/troubleshooting when it fails.
Hoping that you got a high level glance about this Framework!

The Framework implemented with its relevant details is as below:
1)	Framework Structure :-
 
2)	 Src Main  
 
	Database: - It will help in connecting to the DB to fetch the Details to verify with the actual Responses.
	Data provider: - It will help in Passing the excel data to the Test cases
	Test cases: - The actual Test case Methods are called here to Do the API Call’s
	Wrappers :- Has include the Methods to Perform the API Calls’ like fetching the Status codes, response body, json values etc….
1.2)  Utils 
 
	Listeners: - Has the Methods to Start Test and Reporting the tests to Extent Reports
	Utils: -  Has Excel Reader files, Assertions, Mappers which can be used in generic for whole project
	Extent Report :- Has the details for Connections to the Logs and reporting purpose

	
1.3) Data
 
	Datasheets :- Has an excel file where the data will be passed to the test cases from this excel
	Request – Json : - Has the sample file of Json for put & post
	Response – Json : - has sample file of Json for comparing the actual responses

1.4) Config
 
	Properties : - User can use this to pass the Url when it’s the Constant
	XML : - 1) ApiExecution.xml is the file where user can run the scripts in testing suite
2) Extent-config.xml has the reporting structure of the extent report
 
 1.5) endpoint Excel sheet
 
	Test ID (Unique):- It should contain the Test case ID/Name which has to be unique to analyze in the Report.
	Endpoint: - Should contain the URL end points.
	Skip (Yes/No):- Let’s say, any specific Test cases shouldn’t be executed, it should be marked as Yes and vice-versa (Note: Yes will Skip the test and No will execute the test cases).
	Target URL: - It’s the Base URL of the API.
	HTTP Verb (IN CAPS) :- Should give the API calls like GET, PUT, POST, PATCH
  
Step's to Run :-
	System is not having any set ups as its based out of Maven and once the project is cloned, user has to run the Maven Install on the project.
	To execute the Scripts, Navigate to Config> xml> open the file with the name "apiExecution.xml"
	Right Click inside the xml select Run AS > Testng suite
Now, the execution of TCs is displayed in the console.
Result Analysis:-
User can see results in the folder Extent Reports
	Open the HTML file with system editor using right click open with system editor.
NOTE: - Intentionally made a Script Failed by sending the Invalid URI to ensure how the reporting looks for failed scripts.
