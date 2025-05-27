package com.KohaPlugins.dao;

import com.KohaPlugins.service.KohaPatronService;
//import com.KohaPlugins.util.AuthManager;
import com.KohaPlugins.util.AuthBasic;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.time.LocalDate;

@WebServlet("/InsertKohaServlet")
public class InsertKohaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final KohaPatronService kohaService = new KohaPatronService();

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
		
        // Read all form fields
        String cardnumber = request.getParameter("studentId"); // or "cardnumber"
        String surname = request.getParameter("surname");
        String firstname = request.getParameter("firstname");
        String address = request.getParameter("address");
        String address2 = request.getParameter("address2");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String mobile = request.getParameter("mobile");
        String library_id = request.getParameter("branch"); // or "library_id"
        String category_id = request.getParameter("category"); // or "category_id"
        String date_enrolled = LocalDate.now().toString(); // Or use request.getParameter("date_enrolled");
        String userid = request.getParameter("userid");
        String patronAttributes = request.getParameter("patron_attributes");

        // Compose JSON for Koha API
        JSONObject json = new JSONObject();
        json.put("patron_id", 0);
        json.put("cardnumber", cardnumber != null ? cardnumber : "");
        json.put("surname", surname != null ? surname : "");
        json.put("firstname", firstname != null ? firstname : "");
        json.put("address", address != null ? address : "");
        json.put("address2", address2 != null ? address2 : "");
        json.put("city", JSONObject.NULL); // required by API
        json.put("email", email != null ? email : "");
        json.put("phone", phone != null ? phone : "");
        json.put("mobile", mobile != null ? mobile : "");
        json.put("library_id", library_id != null ? library_id : "");
        json.put("category_id", category_id != null ? category_id : "");
        json.put("date_enrolled", date_enrolled);
        json.put("staff_notes", "Added using KohaPlugins");
        json.put("userid", userid != null ? userid : "");

        // Parse extended_attributes
        JSONArray extendedAttributesArray = new JSONArray();
        if (patronAttributes != null && !patronAttributes.trim().isEmpty()) {
            String[] pairs = patronAttributes.split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                if (keyValue.length == 2) {
                    JSONObject extAttr = new JSONObject();
                    extAttr.put("type", keyValue[0].trim());
                    extAttr.put("value", keyValue[1].trim());
                    extendedAttributesArray.put(extAttr);
                }
            }
        }
        json.put("extended_attributes", extendedAttributesArray);
        
        //PrintWriter out = response.getWriter();
        //out.println("<pre>" + json.toString(2) + "</pre>");
        
        // Prepare Koha API call
        String apiUrl = "http://seakl.neduet.edu.pk/api/v1/patrons";
        
        //String token = null;
        try {
            //token = AuthManager.getAccessToken();
            
        } catch (Exception e) {
            // Handle exception (e.g., log and send error response)
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("{\"error\": \"Failed to get access token: " + e.getMessage() + "\"}");
            return;
        }

        HttpURLConnection conn = null;
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");

        try {
        	
        	AuthBasic.setSessionTimeout(request); // session timeout
        	
            URI uri = new URI(apiUrl);
            conn = (HttpURLConnection) uri.toURL().openConnection();
            conn.setRequestMethod("POST");
          //  conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Authorization", AuthBasic.getBasicAuthHeader());
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Write JSON payload
            OutputStream os = conn.getOutputStream();
            os.write(json.toString().getBytes("UTF-8"));
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            InputStream is = (responseCode >= 200 && responseCode < 300) ?
                    conn.getInputStream() : conn.getErrorStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder apiResponse = new StringBuilder();
            while ((line = br.readLine()) != null) {
                apiResponse.append(line);
            }
            br.close();
            out.println(apiResponse.toString());
            response.setStatus(responseCode);
            
            // Output API response
            if (responseCode >= 200 && responseCode < 300) {
                // Success: Set message and redirect to patron page on Koha
                
            	JSONObject kohaPatron = kohaService.getKohaPatronByCardNumber(cardnumber);

                if (kohaPatron != null) {
                    int patronId = kohaPatron.optInt("patron_id");
                    String kohaUrl = "http://seakl.neduet.edu.pk:8001/cgi-bin/koha/members/moremember.pl?borrowernumber=" + patronId;
                    response.sendRedirect(kohaUrl);
                    return;
             } else {
                // Failure: Show error (optional)
                request.setAttribute("message", "Failed to insert record: " + apiResponse.toString());
                request.getRequestDispatcher("studentAddUpdate.jsp").forward(request, response);
                return;
            }
           }
        } catch (Exception e) {
        	request.setAttribute("message", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("studentAddUpdate.jsp").forward(request, response);
            return;
        } finally {
            if (conn != null) conn.disconnect();
            out.close();
        }
    }
}