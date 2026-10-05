package com.KohaPlugins.service;

import java.sql.*;
import java.math.BigDecimal;

import com.KohaPlugins.util.dbConn;

public class KohaBiblioFunctionService {
    //private static final String JDBC_URL = "jdbc:mysql://192.168.14.241:3306/koha_library";
	//private static final String JDBC_URL = "jdbc:mysql://192.168.14.237:3306/koha_library";
    //private static final String JDBC_USER = "kohaplugin";
    //private static final String JDBC_PASSWORD = "Kohaplugin1!";
    //private static final String JDBC_USER = "naveen";
    //private static final String JDBC_PASSWORD = "Naveen123@";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found.", e);
        }
    }

    public static int calRecoveryPrice(String barcode, String year, BigDecimal cost) throws SQLException {
        try (//Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        		Connection conn = dbConn.getMySQLConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT calRecoveryPrice(?, ?, ?)")) {
        	
        	
            stmt.setString(1, barcode);
            stmt.setString(2, year);
            stmt.setBigDecimal(3, cost);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
            
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int calRentBbk(BigDecimal price) throws SQLException {
        try (//Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        		Connection conn = dbConn.getMySQLConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT calRentBbk(?)")) {
            stmt.setBigDecimal(1, price);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int policyRentBbk(String barcode) throws SQLException {
        try (//Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        		Connection conn = dbConn.getMySQLConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT policyRentBbk(?)")) {
            stmt.setString(1, barcode);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public static int lastSerialBarcode() throws SQLException {
        try (//Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        		Connection conn = dbConn.getMySQLConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT lastSerialBarcode()")) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public static int nextSerialBarcode() throws SQLException {
        try (//Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        		Connection conn = dbConn.getMySQLConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT nextSerialBarcode()")) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}