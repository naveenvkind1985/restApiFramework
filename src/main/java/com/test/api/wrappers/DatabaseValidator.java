package com.test.api.wrappers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.asserts.SoftAssert;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 
 * Response object DB Validator
 */
public class DatabaseValidator {


    private Connection dbConnection;
    private JsonParser jsonParser;
    private SoftAssert softAssert;

    public DatabaseValidator(SoftAssert softAssert,Connection dbConnection) {
        this.dbConnection = dbConnection;
        jsonParser = new JsonParser();
        this.softAssert =softAssert;
    }


    public DatabaseValidator verifyWithDB(String expectedJson, String sql) {

        if (dbConnection == null) {
            return this;
        }

        JsonObject dbJson;
        JsonObject expected;
        if (!expectedJson.isEmpty() && !sql.isEmpty()) {

            try {
                dbJson = getDBJson(sql);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return this;
            }
            try {
                expected = jsonParser.parse(expectedJson).getAsJsonObject();
            } catch (Exception e) {
                System.out.println("Could not parse the expected JSON. Could not verify with database");
                return this;
            }
            Map<String, String> expectedAttributes = jsonToMap(expected);
            for (String key : expectedAttributes.keySet()) {
                boolean hasKey = dbJson.has(key);
                softAssert.assertTrue(hasKey);
                System.out.println("Verified if "+key+" is present in the database");
                if (hasKey) {
                    softAssert.assertEquals(dbJson.get(key).getAsString(), expected.get(key).getAsString());
                    System.out.println("Verified if "+key+" is equal to "+expected.get(key).getAsString());
                }
            }
        }

        return this;
    }

    private JsonObject getDBJson(String sql) throws Exception {
        Statement statement = null;
        try {

            statement = dbConnection.createStatement();
            System.out.println("Created statement from jDbc connection");
            ResultSet rs = statement.executeQuery(sql);
            ResultSetMetaData rsMeta = rs.getMetaData();

            List<JsonObject> resList = new ArrayList<JsonObject>();
            int columnCnt = rsMeta.getColumnCount();
            List<String> columnNames = new ArrayList<String>();
            for (int i = 1; i <= columnCnt; i++) {
                columnNames.add(rsMeta.getColumnName(i));
            }

            while (rs.next()) { // convert each object to an human readable JSON object
                JsonObject obj = new JsonObject();
                for (int i = 1; i <= columnCnt; i++) {
                    String key = columnNames.get(i - 1);
                    String value = rs.getString(i);
                    obj.addProperty(key, value);
                }
                resList.add(obj);
            }
            if (resList.size() > 1) {
                statement.close();
                throw new Exception("Multiple results found when SQL query was executed. Did not perform Database validation ");
            }
            return resList.get(0);
        } catch (Exception e) {
            try {
                if (statement != null) {
                    statement.close();
                    System.out.println("Close statement");
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
            throw new Exception("Sql query did not return result. Could not verify with database");
        }

    }


    private Map<String, String> jsonToMap(JsonObject jsonObject) {
        Map<String, String> jsonMap = new HashMap<>();
        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();

        for (Map.Entry<String, JsonElement> entry : entrySet) {
            jsonMap.put(entry.getKey(), jsonObject.get(entry.getKey()).getAsString());
        }
        return jsonMap;
    }
}