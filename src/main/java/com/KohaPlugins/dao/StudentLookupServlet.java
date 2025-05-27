package com.KohaPlugins.dao;

import com.KohaPlugins.service.KohaPatronService;
import com.KohaPlugins.service.OracleMemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
    	
    	/* Protects sensitive JSPs and servlets with a session check (e.g., check for an attribute like kohaUserid in session).
		If not present, redirect to your login page before any API call is attempted. */
    	HttpSession session = request.getSession(false);
    	if (session.getAttribute("kohaUserid") == null) {
			response.sendRedirect("kohaPluginLogin.jsp");
			return;
		}
    	
        String studentId = request.getParameter("studentId");
        String category = request.getParameter("category");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Step 1: Get student info from Oracle via the service
        	
            JSONObject oracleStudent = oracleService.getStudentInfoById(studentId, category);

            if (oracleStudent != null) {
                
            	//String studentID = oracleStudent.optString("STUDENTID");
                
                String firstname = oracleStudent.optString("FIRSTNAME");
                out.println("<p>oracleStudentName: " + firstname + "</p>");
                String surname = oracleStudent.optString("SURNAME");
                String address = oracleStudent.optString("ADDRESS");
                String address2 = oracleStudent.optString("ADDRESS2");
                String phone = oracleStudent.optString("PHONE");
                String mobile = oracleStudent.optString("MOBILE");
                String email = oracleStudent.optString("EMAIL");
                //String email = "naveen@neduet.edu.pk";
                String cardnumber = oracleStudent.optString("CARDNUMBER");
                String category_id = oracleStudent.optString("CATEGORYCODE");
                String library_id = oracleStudent.optString("BRANCHCODE");
                String dateenrolled = oracleStudent.optString("DATEENROLLED");
                String userid = oracleStudent.optString("USERID");
                String password = oracleStudent.optString("PASSWORD");
                String patronAttributes = oracleStudent.optString("PATRON_ATTRIBUTES");
                out.println("<p>oracleAttributes: " + patronAttributes + "</p>");

                // Extract rollNo from Oracle attributes
                
                String rollNo = extractFromOracleAttributes(patronAttributes, "PAT_NO");

                // Step 2: Check Koha for patron
                JSONObject kohaPatron = kohaService.getKohaPatronByCardNumber(studentId);

                if (kohaPatron != null) {
                	if ("EMP".equals(category)) {
                		request.setAttribute("status", "found_same");
                        request.setAttribute("studentId", studentId);
                	}
                	else {
                    int patronId = kohaPatron.optInt("patron_id");
                    JSONArray kohaExtendedAttributes = kohaService.getExtendedAttributes(patronId);
                    String kohaRollNo = extractFromKohaExtendedAttributes(kohaExtendedAttributes, "PAT_NO");
                 // Patron found in Koha
                    if (rollNo.equals(kohaRollNo)) {
                    	// Patron found in Koha
                    	request.setAttribute("status", "found_same");
                        request.setAttribute("studentId", studentId);
                        
                    } else {
                    	// Patron found in Koha with different extended attributes
                    	request.setAttribute("status", "found_different");
                        request.setAttribute("studentId", studentId);
                        request.setAttribute("patronAttributes", patronAttributes);
                        request.setAttribute("rollNo", rollNo);
                        request.setAttribute("kohaRollNo", kohaRollNo);
                    }
                    }
                } else {
                    // Step 3: Prompt user to insert new patron
                	if ((email != null && !email.contains("@cloud.neduet.edu.pk")) ) {
                	    request.setAttribute("status", "not_in_koha_invalid_email");
                	} else {
                	    request.setAttribute("status", "not_in_koha");
                	}
                	    request.setAttribute("studentId", studentId);
                	    request.setAttribute("firstname", firstname);
                	    request.setAttribute("surname", surname);
                	    request.setAttribute("address", address);
                	    request.setAttribute("address2", address2);
                	    request.setAttribute("phone", phone);
                	    request.setAttribute("mobile", mobile);
                	    request.setAttribute("email", email);
                	    request.setAttribute("cardnumber", cardnumber);
                	    request.setAttribute("category_id", category_id);
                	    request.setAttribute("library_id", library_id);
                	    request.setAttribute("dateenrolled", dateenrolled);
                	    request.setAttribute("userid", userid);
                	    request.setAttribute("password", password);
                	    request.setAttribute("patronAttributes", patronAttributes);
                	    request.setAttribute("rollNo", rollNo);
                	
                }
            } else {
            	request.setAttribute("status", "not_found");  
            	}
            
            request.getRequestDispatcher("/studentResult.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace(out);
            out.println("<p>Error processing student lookup: " + e.getMessage() + "</p>");
        }
    }
    
    // TWO DIFFERENT FUNCTIONS TO EXTRACT ROLL NOS FROM ORACLE AND KOHA DATABASES

    // FUNCTION 1: Extract key from Oracle attribute string
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

    // FUNCTION 2: Extract key from Koha extended attributes array
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