package com.test.api.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 
 * @purpose - JDBC CONNECTION
 */

public class DBConnect {


    public static Connection getJdbcConnection(String JDBC_DRIVER, String DB_STRING) {

        Connection conn = null;
        
        try {

            // STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);
            
            // STEP 3: Open a connection
            conn = DriverManager.getConnection(DB_STRING);
            return conn;

        } catch (SQLException se) {
            // Handle errors for JDBC

            System.out.println("JDBC connection is broken");
            /*try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                System.out.println("Error closing jdbc connection");
            }*/
        } catch (Exception e) {
            // Handle errors for Class.forName
            System.out.println("Error reading Class.forName. Check if  database driver is included in classpath");
        }
        return null;
    }


}
