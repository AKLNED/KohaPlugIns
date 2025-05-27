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

@WebServlet("/UpdateKohaServlet")
public class UpdateKohaServlet extends HttpServlet {
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
    	
        String studentId = request.getParameter("studentId");
        String patronAttributes = request.getParameter("patron_attributes");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (studentId == null || patronAttributes == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("{\"error\": \"Missing required parameters: studentId or patron_attributes\"}");
            return;
        }

        JSONObject patron = kohaService.getKohaPatronByCardNumber(studentId);
        if (patron == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.println("{\"error\": \"No patron found for the given studentId/cardnumber.\"}");
            return;
        }

        int patronId = patron.optInt("patron_id", -1);
        if (patronId == -1) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("{\"error\": \"Unable to determine patron_id for the given studentId.\"}");
            return;
        }

        // Parse attributes string into JSONArray
        JSONArray extendedAttributesArray = new JSONArray();
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

        String apiUrl = "http://seakl.neduet.edu.pk/api/v1/patrons/" + patronId + "/extended_attributes";

        //String token;
        try {
            //token = AuthManager.getAccessToken();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("{\"error\": \"Failed to get access token: " + e.getMessage() + "\"}");
            return;
        }

        HttpURLConnection conn = null;
        try {
        	
        	AuthBasic.setSessionTimeout(request); // session timeout
        	
        	URI uri = new URI(apiUrl);
            conn = (HttpURLConnection) uri.toURL().openConnection();
            conn.setRequestMethod("PUT");
            //conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Authorization", AuthBasic.getBasicAuthHeader());
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(extendedAttributesArray.toString().getBytes("UTF-8"));
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            InputStream is = (responseCode >= 200 && responseCode < 300) ?
                    conn.getInputStream() : conn.getErrorStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder apiResponse = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                apiResponse.append(line);
            }
            br.close();

            //out.println(apiResponse.toString());
            //response.setStatus(responseCode);
            // Output API response
            if (responseCode >= 200 && responseCode < 300) {
                // Success: Set message and redirect to patron page on Koha
                
            	//JSONObject kohaPatron = kohaService.getKohaPatronByCardNumber(cardnumber);

                //if (kohaPatron != null) {
                    //int patronId = kohaPatron.optInt("patron_id");
                    String kohaUrl = "http://seakl.neduet.edu.pk:8001/cgi-bin/koha/members/moremember.pl?borrowernumber=" + patronId;
                    response.sendRedirect(kohaUrl);
                    return;
             } 
            else {
                // Failure: Show error (optional)
                request.setAttribute("message", "Failed to insert record: " + apiResponse.toString());
                request.getRequestDispatcher("studentAddUpdate.jsp").forward(request, response);
                return;
            }
          // }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("{\"error\": \"An error occurred: " + e.getMessage() + "\"}");
        } finally {
            if (conn != null) conn.disconnect();
            out.close();
        }
    }
}