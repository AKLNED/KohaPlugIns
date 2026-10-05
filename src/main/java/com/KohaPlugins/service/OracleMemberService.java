package com.KohaPlugins.service;

import com.KohaPlugins.util.dbConn;
import com.KohaPlugins.model.DefaulterRow;

import oracle.jdbc.OracleTypes;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.util.List;
//import oracle.jdbc.OracleConnection;

public class OracleMemberService {

    public JSONObject getStudentInfoById(String studentId,String category) {
        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        JSONObject studentInfo = null;

        try {
            conn = dbConn.getOracleConnection();
            
//            String function = "{ ? = call kohaPlugin.fetch_student_info(?)}";
            String function="";
            
            if ("UG".equals(category)) {
            	function = "{ ? = call kohaPlugin.fetch_student_info(?) }";
            } else if ("PG".equals(category)) {
            	function = "{ ? = call kohaPlugin.fetch_pg_student_info(?) }";
            } else if ("EMP".equals(category)) {
            	function = "{ ? = call kohaPlugin.fetch_employee_info(?) }";
            }
            stmt = conn.prepareCall(function);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setString(2, studentId);
            stmt.execute();
            rs = (ResultSet) stmt.getObject(1);

            if (rs.next()) {
                studentInfo = new JSONObject();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();

                for (int i = 1; i <= columnCount; i++) {
                    String colName = rsmd.getColumnLabel(i);
                    Object value = rs.getObject(i);
                    studentInfo.put(colName, value != null ? value : JSONObject.NULL);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ex) {}
            try { if (stmt != null) stmt.close(); } catch (Exception ex) {}
            try { if (conn != null) conn.close(); } catch (Exception ex) {}
        }
        return studentInfo;
    }
    
    //--------------------------------------------------------------------
    //----------------------------------------------------------------------
    
    public int updateDefaulters(List<DefaulterRow> rows) {
        if (rows == null || rows.isEmpty()) return 0;

        Connection conn = null;
        CallableStatement cstmt = null;
        try {
            conn = dbConn.getOracleConnection(); // test connection - must return java.sql.Connection
         
            
           
            // Convert list → JSON string (correctly!)
            String jsonData = toJson(rows);

            CallableStatement stmt =
                conn.prepareCall("{ ? = call kohaPlugin.update_insert_defaulters(?) }");

            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setString(2, jsonData);

            stmt.execute();

            int count = stmt.getInt(1);


            return count;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            try { if (cstmt != null) cstmt.close(); } catch (Exception ex) {}
            try { if (conn != null) conn.close(); } catch (Exception ex) {}
        }
    }
    
    private String toJson(List<DefaulterRow> rows) {
        JSONArray arr = new JSONArray();
        for (DefaulterRow r : rows) {
            JSONObject o = new JSONObject();
            o.put("cardnumber", r.getCardnumber());
            o.put("pat_no", r.getborrower_no());
            o.put("firstname", r.getdef_name());
            arr.put(o);
        }
        return arr.toString();
    }

  //--------------------------------------------------------------------
    //----------------------------------------------------------------------
    
    
    public JSONArray fetchSISDefaultersAsJsonArray() {
        Connection conn = null;
        CallableStatement stmt = null;

        try {
            conn = dbConn.getOracleConnection();  // Oracle 10g connection

           
            stmt = conn.prepareCall("{ ? = call kohaPlugin.get_SIS_defaulter_list_json }");
            stmt.registerOutParameter(1, java.sql.Types.CLOB);

            stmt.execute();

            // Read CLOB
            Clob clob = stmt.getClob(1);
            if (clob == null) {
                return new JSONArray(); // return empty list instead of null
            }

            String jsonData = clob.getSubString(1, (int) clob.length());

            // Convert into JSON Array
            return new JSONArray(jsonData);

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return new JSONArray();  // return empty JSON array on DB failure

        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();  // return empty JSON array on generic error

        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception ex) {}
            try { if (conn != null) conn.close(); } catch (Exception ex) {}
        }
    }
    
    
    //--------------------------------------------------------------------
    //----------------------------------------------------------------------
    
    public int isDefaulterSIS(String studentId) {
        Connection conn = null;
        CallableStatement stmt = null;

        try {
            conn = dbConn.getOracleConnection();

            stmt = conn.prepareCall("{ ? = call kohaplugin.is_defaulter_sis(?) }");
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setString(2, studentId);

            stmt.execute();
            int result = stmt.getInt(1);

            return (result);

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception ex) {}
            try { if (conn != null) conn.close(); } catch (Exception ex) {}
        }
    }
  
    
    
    //--------------------------------------------------------------------
    //----------------------------------------------------------------------
    
    public int updateLibDefFlag(String studentId, Connection conn) {
        //String sql = "{ ? = call update_lib_def_flag(?) }";
        //Connection conn = null;
        CallableStatement cstmt = null;
        
        try { 
        	//conn = dbConn.getOracleConnection();
            cstmt = conn.prepareCall("{ ? = call kohaplugin.update_lib_def_flag(?) }");

            cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
            cstmt.setString(2, studentId);

            cstmt.execute();

            return cstmt.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    
    
    

}