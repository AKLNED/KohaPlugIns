package com.KohaPlugins.service;

import com.KohaPlugins.util.dbConn;

import java.sql.*;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class KohaPatronFunctionService {
    //private static final String JDBC_URL = "jdbc:mysql://192.168.14.241:3306/koha_library";
	private static final String JDBC_URL = "jdbc:mysql://192.168.14.237:3306/koha_library";
    private static final String JDBC_USER = "kohaplugin";
    private static final String JDBC_PASSWORD = "Kohaplugin1!";
    //private static final String JDBC_USER = "naveen";
    //private static final String JDBC_PASSWORD = "Naveen123@";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found.", e);
        }
    }

    
  //--------------------------------------------------------------------
    //----------------------------------------------------------------------
    
    public JSONArray fetchDefaultersAsJsonArray() throws SQLException {
    	String sql = "SELECT list_defaulters_in_json()";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (!rs.next()) {
                return new JSONArray();
            }

            String jsonText = rs.getString(1);
            if (jsonText == null || jsonText.isEmpty()) {
                return new JSONArray();
            }

            JSONArray arr = new JSONArray(jsonText);

            // Convert JSONArray → List<JSONObject> for sorting
            List<JSONObject> list = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                list.add(arr.getJSONObject(i));
            }

            // Sort using your required ordering
            list.sort((a, b) -> {

                // ----- 1. Compare CAST(SUBSTR(cardnumber,1,2) AS UNSIGNED) DESC -----
                int aPrefix = parseTwoDigitPrefix(a.optString("cardnumber"));
                int bPrefix = parseTwoDigitPrefix(b.optString("cardnumber"));

                // DESC → reverse subtraction
                int cmp = Integer.compare(bPrefix, aPrefix);
                if (cmp != 0) return cmp;

                // ----- 2. Compare SUBSTR(pat_no,1,2) ASC -----
                String aPat = firstSix(a.optString("pat_no"));
                String bPat = firstSix(b.optString("pat_no"));

                return aPat.compareTo(bPat);
            });

            // Convert list back to JSONArray
            JSONArray sorted = new JSONArray();
            int serialNo = 1; // Start serial number from 1
            for (JSONObject obj : list) {
            	// Format DECIMAL field with 2 trailing zeros
                double amt = obj.optDouble("amountoutstanding", 0.0);
                String formattedAmt = String.format("%.2f", amt);
                obj.put("amountoutstanding", formattedAmt);
            	
             // Add serial number
                obj.put("serial_no", serialNo++);
                
            	sorted.put(obj);
            }

            return sorted;
        }
    }

    // Helper: extract first two chars + parse int
    private int parseTwoDigitPrefix(String s) {
        if (s == null || s.length() < 2) return 0;
        try {
            return Integer.parseInt(s.substring(0, 2));
        } catch (Exception e) {
            return 0;
        }
    }

    // Helper: extract first two chars (safe)
    private String firstSix(String s) {
        if (s == null) return "";
        return s.length() >= 6 ? s.substring(0, 6) : s;
    }
    
    
  //--------------------------------------------------------------------
    //----------------------------------------------------------------------
    
    public int isDefaulterKoha(String cardnumber) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = dbConn.getMySQLConnection();

            stmt = conn.prepareStatement("SELECT is_defaulter_koha(?)");
            stmt.setString(1, cardnumber);

            rs = stmt.executeQuery();

            if (rs.next()) {
                int val = rs.getInt(1);
                return 1;
            }

            return 0;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ex) {}
            try { if (stmt != null) stmt.close(); } catch (Exception ex) {}
            try { if (conn != null) conn.close(); } catch (Exception ex) {}
        }
    }

    
}