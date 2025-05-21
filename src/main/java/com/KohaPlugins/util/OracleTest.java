package com.KohaPlugins.util;
import java.sql.*;

public class OracleTest {
    public static void main(String[] args) throws Exception {
        Class.forName("oracle.jdbc.OracleDriver");
        Connection conn = DriverManager.getConnection(
            "jdbc:oracle:thin:@192.168.14.199:1521:dev", "siraj", "ora13ned"
        	//"jdbc:oracle:thin:@//192.168.14.199:1521/dev", "siraj","ora13ned"
        );
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM NLS_SESSION_PARAMETERS");
        while (rs.next()) {
            System.out.println(rs.getString(1) + " = " + rs.getString(2));
        }
        conn.close();
    }
}