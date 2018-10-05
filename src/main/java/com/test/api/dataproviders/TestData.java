package com.test.api.dataproviders;

import static io.restassured.RestAssured.baseURI;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import com.test.api.database.DBConnect;
import com.test.api.utils.ExcelReader;

/**
 * 
 * Performs Test Case setup and tearDown.
 * Uses TestNg DataProviders that read from Excel dataSheets
 * used for Test Cases.
 */
public class TestData {


    private String uri;
    private String dataSheetDirectory;
    private String JDBC_DRIVER;
    private String DB_STRING;

    protected String dataSheetName;
    protected Connection dbConnection;
    protected String responseJsonDirectory;
    protected String requestJsonDirectory;
    
    protected static final int RESPONSEOK = 200; 


    /**
     * Creates a new Extent Report.
     */
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {

        System.out.println("Starting Regression Test Suite");
        System.out.println("Starting Extent Report");
    }


    /**
     * Sets up a common Database Connection for each test class that extends from TestData
     * Database properties are to be specifies in config.properties
     * Starts Report Generation
     */
    @BeforeClass
    public void beforeClass() {
        loadProperties();
        if (!JDBC_DRIVER.isEmpty() && !DB_STRING.isEmpty())
            dbConnection = DBConnect.getJdbcConnection(JDBC_DRIVER, DB_STRING);
       // baseURI = uri;
        baseURI ="";
        if (dbConnection != null)
            System.out.println("Database Connection Opened");
        else
            System.out.println("Database Connection not created. All DB verifications will be skipped");
    }


    /**
     * Gets Data from an Excel sheet and used as data provider
     * for all test cases. dataSheetName must be specified in
     * the child class setup method
     */
    @DataProvider(name = "testData")
    public Object[][] getData() {

        return ExcelReader.read(dataSheetDirectory + dataSheetName);

    }


    /**
     * Closes the DB connection if it was opened.
     */

    @AfterClass
    public void afterClass() {
        try {
            if (dbConnection != null) {
                dbConnection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {

            System.out.println("Database connection could not be closed");
        }
    }


    /**
     * Loads the required properties from config.properties.
     */
    private void loadProperties() {
        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream("./config/properties/config.properties");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            JDBC_DRIVER = prop.getProperty("JDBC_DRIVER");
            if (JDBC_DRIVER == null) {
                System.out.println("JDBC Driver property not set");
                JDBC_DRIVER = "";
            }
            DB_STRING = prop.getProperty("DB_STRING");
            if (DB_STRING == null) {
                System.out.println("Database String property not set");
                DB_STRING = "";
            }
            uri = prop.getProperty("URI");
            if (uri == null) {
                System.out.println("Base URI not set");
                uri = "";
            }
            dataSheetDirectory = prop.getProperty("DATASHEET");
            if (dataSheetDirectory == null) {
                dataSheetDirectory = "";
                System.out.println("dataSheet directory not set");
            }
            responseJsonDirectory = prop.getProperty("RESPONSE_JSON");
            if (responseJsonDirectory == null) {
                responseJsonDirectory = "";
                System.out.println("Response JSON directory is not set");
            }
            requestJsonDirectory = prop.getProperty("REQUEST_JSON");
            if (requestJsonDirectory == null) {
                requestJsonDirectory = "";
                System.out.println("Request Json directory not set");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}