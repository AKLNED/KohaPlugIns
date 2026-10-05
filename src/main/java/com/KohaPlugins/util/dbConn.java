package com.KohaPlugins.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConn {

    // Method to connect to MySQL
    public static Connection getMySQLConnection() throws ClassNotFoundException, SQLException {
        // MySQL connection details
        //String url = "jdbc:mysql://192.168.14.241:3306/koha_library";  // Backup Koha Credentials for MySQL database details
        String url = "jdbc:mysql://192.168.14.237:3306/koha_library";  // Live Koha Credentials for MySQL database details
        //String user = "naveen";
        //String password = "Naveen123@";
        String user = "kohaplugin";
        String password = "Kohaplugin1!";

        // Load the MySQL driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Establish and return the MySQL connection
        return DriverManager.getConnection(url, user, password);
    }

    // Method to connect to Oracle
    public static Connection getOracleConnection() throws ClassNotFoundException, SQLException {
        // Oracle connection details
    	// Note: With ojdbc8, the driver class is "oracle.jdbc.OracleDriver"
        String url = "jdbc:oracle:thin:@192.168.14.199:1521:dev";  // Replace with Oracle database details
    	//String url = "jdbc:oracle:thin:@//192.168.14.199:1521/dev"; // for service name instead of SID
        String user = "siraj";
        String password = "ora13ned";
        

        // Load the Oracle driver
        Class.forName("oracle.jdbc.OracleDriver");

        // Establish and return the Oracle connection
        return DriverManager.getConnection(url, user, password);
    }
    
    
}
