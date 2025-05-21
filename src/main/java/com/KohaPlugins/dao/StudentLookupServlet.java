package com.KohaPlugins.dao;

import com.KohaPlugins.service.KohaPatronService;
import com.KohaPlugins.service.OracleMemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/StudentLookupServlet")
public class StudentLookupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final KohaPatronService kohaService = new KohaPatronService();
    private final OracleMemberService oracleService = new OracleMemberService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String studentId = request.getParameter("studentId");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Step 1: Get student info from Oracle via the service
            JSONObject oracleStudent = oracleService.getStudentInfoById(studentId);

            if (oracleStudent != null) {
                
            	// Print all information from Oracle database
                /*
                 * out.println("<h3>Oracle Student Information</h3>");
                out.println("<ol>");
                for (String key : oracleStudent.keySet()) {
                    out.println("<li><strong>" + key + ":</strong> " + oracleStudent.get(key) + "</li>");
                }
                out.println("</ol>");
                */

                String studentID = oracleStudent.optString("STUDENTID");
                //out.println("<p>oracleStudentID: " + studentID + "</p>");
                String firstname = oracleStudent.optString("FIRSTNAME");
                String surname = oracleStudent.optString("SURNAME");
                String address = oracleStudent.optString("ADDRESS");
                String address2 = oracleStudent.optString("ADDRESS2");
                String phone = oracleStudent.optString("PHONE");
                String mobile = oracleStudent.optString("MOBILE");
                String email = oracleStudent.optString("EMAIL");
                String cardnumber = oracleStudent.optString("CARDNUMBER");
                String category_id = oracleStudent.optString("CATEGORYCODE");
                String library_id = oracleStudent.optString("BRANCHCODE");
                String dateenrolled = oracleStudent.optString("DATEENROLLED");
                String userid = oracleStudent.optString("USERID");
                String password = oracleStudent.optString("PASSWORD");
                String patronAttributes = oracleStudent.optString("PATRON_ATTRIBUTES");

                // Extract rollNo from Oracle attributes
                String rollNo = extractFromOracleAttributes(patronAttributes, "PAT_NO");

                // Step 2: Check Koha for patron
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
                    // Step 3: Prompt user to insert new patron
                    out.println("<p>Student does not exist in the library database. Do you want to insert?</p>");
                    out.println("<form action='InsertKohaServlet' method='POST'>");
                    out.println("<input type='hidden' name='studentId' value='" + studentId + "'>");
                    out.println("<input type='hidden' name='firstname' value='" + firstname + "'>");
                    out.println("<input type='hidden' name='surname' value='" + surname + "'>");
                    out.println("<input type='hidden' name='address' value='" + address + "'>");
                    out.println("<input type='hidden' name='address2' value='" + address2 + "'>");
                    out.println("<input type='hidden' name='phone' value='" + phone + "'>");
                    out.println("<input type='hidden' name='mobile' value='" + mobile + "'>");
                    out.println("<input type='hidden' name='email' value='" + email + "'>");
                    out.println("<input type='hidden' name='cardnumber' value='" + cardnumber + "'>");
                    out.println("<input type='hidden' name='category' value='" + category_id + "'>");
                    out.println("<input type='hidden' name='branch' value='" + library_id + "'>");
                    out.println("<input type='hidden' name='dateenrolled' value='" + dateenrolled + "'>");
                    out.println("<input type='hidden' name='userid' value='" + userid + "'>");
                    out.println("<input type='hidden' name='password' value='" + password + "'>");
                    out.println("<input type='hidden' name='patron_attributes' value='" + patronAttributes + "'>");
                    // Print these values
                    out.println("<p>Student ID: " + studentId + "</p>");
                    out.println("<p>First name: " + firstname + "</p>");
                    out.println("<p>Surname: " + surname + "</p>");
                    out.println("<p>Email: " + email + "</p>");
                    out.println("<p>Category: " + category_id + "</p>");
                    out.println("<p>Branch: " + library_id + "</p>");
                    out.println("<p>Roll No: " + rollNo + "</p>");

                    out.println("<input type='submit' value='Insert into Koha'>");
                    out.println("</form>");
                }
            } else {
                out.println("<p>No data found for the given Student ID in Oracle.</p>");
            }
        } catch (Exception e) {
            e.printStackTrace(out);
            out.println("<p>Error processing student lookup: " + e.getMessage() + "</p>");
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