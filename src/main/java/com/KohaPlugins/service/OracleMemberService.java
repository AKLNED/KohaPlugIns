package com.KohaPlugins.service;

import com.KohaPlugins.util.dbConn;
import oracle.jdbc.OracleTypes;
import org.json.JSONObject;

import java.sql.*;

public class OracleMemberService {

    public JSONObject getStudentInfoById(String studentId) {
        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        JSONObject studentInfo = null;

        try {
            conn = dbConn.getOracleConnection();
            String function = "{ ? = call kohaPlugin.fetch_student_info(?)}";
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
}