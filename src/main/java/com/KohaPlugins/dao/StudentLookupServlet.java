package com.KohaPlugins.dao;

import com.KohaPlugins.service.KohaPatronService;
import com.KohaPlugins.util.dbConn;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import oracle.jdbc.OracleTypes;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/StudentLookupServlet")
public class StudentLookupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final KohaPatronService kohaService = new KohaPatronService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String studentId = request.getParameter("studentId");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;

        try {
            // Step 1: Establish database connection
            try {
                conn = dbConn.getOracleConnection();
            } catch (ClassNotFoundException e) {
                e.printStackTrace(out);
                out.println("<p>Error establishing database connection: " + e.getMessage() + "</p>");
                return;
            }

            // Step 2: Call the fetch_student_info function
            String function = "{ ? = call kohaPlugin.fetch_student_info(?)}";
            stmt = conn.prepareCall(function);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setString(2, studentId);
            stmt.execute();
            rs = (ResultSet) stmt.getObject(1);

            if (rs.next()) {
                String firstname = rs.getString("firstname");
                String surname = rs.getString("surname");
                String email = rs.getString("email");
                String categorycode = rs.getString("categorycode");
                String branchcode = rs.getString("branchcode");
                String patronAttributes = rs.getString("patron_attributes");

                // Extract rollNo from Oracle attributes
                String rollNo = extractFromOracleAttributes(patronAttributes, "PAT_NO");

                // Step 3: Check Koha for patron
                JSONObject kohaPatron = kohaService.getKohaPatronByCardNumber(studentId);

                if (kohaPatron != null) {
                    int patronId = kohaPatron.optInt("patron_id");
                    JSONArray kohaExtendedAttributes = kohaService.getExtendedAttributes(patronId);
                    String kohaRollNo = extractFromKohaExtendedAttributes(kohaExtendedAttributes, "PAT_NO");

                    if (rollNo.equals(kohaRollNo)) {
                        out.println("<p>The same patron with the same information exists in the library database.</p>");
                    } else {
                        out.println("<p>Different information found in Koha. Do you want to update?</p>");
                        out.println("<form action='UpdateKohaServlet' method='POST'>");
                        out.println("<p>Student ID: " + studentId + "</p>");
                        out.println("<input type='hidden' name='studentId' value='" + studentId + "'>");
                        out.println("<p>Patron Attributes: " + patronAttributes + "</p>");
                        out.println("<input type='hidden' name='patron_attributes' value='" + patronAttributes + "'>");
                        out.println("<p>SIS Roll No: " + rollNo + "</p>");
                        out.println("<p>Koha Roll No: " + kohaRollNo + "</p>");
                        out.println("<input type='submit' value='Update Koha'>");
                        out.println("</form>");
                    }
                } else {
                    // Step 4: Prompt user to insert new patron
                    out.println("<p>Student does not exist in the library database. Do you want to insert?</p>");
                    out.println("<form action='InsertKohaServlet' method='POST'>");
                    out.println("<p>Student ID: " + studentId + "</p>");
                    out.println("<input type='hidden' name='studentId' value='" + studentId + "'>");
                    out.println("<p>First name: " + firstname + "</p>");
                    out.println("<input type='hidden' name='firstname' value='" + firstname + "'>");
                    out.println("<p>Surname: " + surname + "</p>");
                    out.println("<input type='hidden' name='surname' value='" + surname + "'>");
                    out.println("<p>Email: " + email + "</p>");
                    out.println("<input type='hidden' name='email' value='" + email + "'>");
                    out.println("<p>Category: " + categorycode + "</p>");
                    out.println("<input type='hidden' name='category' value='" + categorycode + "'>");
                    out.println("<p>Branch: " + branchcode + "</p>");
                    out.println("<input type='hidden' name='branch' value='" + branchcode + "'>");
                    out.println("<p>Patron Attributes: " + patronAttributes + "</p>");
                    out.println("<input type='hidden' name='patron_attributes' value='" + patronAttributes + "'>");
                    out.println("<input type='submit' value='Insert into Koha'>");
                    out.println("</form>");
                }
            } else {
                out.println("<p>No data found for the given Student ID in Oracle.</p>");
            }
        } catch (SQLException e) {
            e.printStackTrace(out);
            out.println("<p>Error fetching student data: " + e.getMessage() + "</p>");
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace(out);
                out.println("<p>Error closing resources: " + e.getMessage() + "</p>");
            }
        }
    }

    // Extract key from Oracle attribute string
    private String extractFromOracleAttributes(String attributes, String key) {
        if (attributes == null) return "";
        String[] pairs = attributes.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2 && keyValue[0].trim().equalsIgnoreCase(key)) {
                return keyValue[1].trim();
            }
        }
        return "";
    }

    // Extract key from Koha extended attributes array
    private String extractFromKohaExtendedAttributes(JSONArray attributesArr, String key) {
        if (attributesArr == null) return "";
        for (int i = 0; i < attributesArr.length(); i++) {
            JSONObject attr = attributesArr.getJSONObject(i);
            if (attr.optString("type").equalsIgnoreCase(key)) {
                return attr.optString("value", "");
            }
        }
        return "";
    }
}