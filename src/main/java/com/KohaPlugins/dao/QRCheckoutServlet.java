package com.KohaPlugins.dao;

import com.KohaPlugins.service.KohaPatronService;
import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/QRCheckoutServlet")
public class QRCheckoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    	//Session expiry check 
		
		  HttpSession session = request.getSession(false); if (session == null ||
		  session.getAttribute("kohaUserid") == null) {
		  response.sendRedirect(request.getContextPath() +
		  "/kohaPluginLogin.jsp?route=qrcheckout"); return; }
		 

        String qrInput = request.getParameter("qrCode");
        String error = null;
        String memberId = null;

        
        if (qrInput != null && !qrInput.trim().isEmpty()) {
            String value = qrInput.trim();

            
         // Accept short numeric codes (3-7 digits) as before
            if (value.matches("^\\d{3,7}$")) {
                memberId = value;
            } else {
                String baseUrl = "https://pl.neduet.edu.pk/qrapp/index.jsp?";
                // Require input to start with the base URL AND end with 'S' or 'P'
                if (value.startsWith(baseUrl)) {
                    // Regex matches ([?&])param=([^&]+)
                    // Group 2 directly captures the value of 'param' up to the next '&' or end of string
                    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("[?&]param=([^&]+)");
                    java.util.regex.Matcher matcher = pattern.matcher(value);

                    if (matcher.find()) {
                        String paramValue = matcher.group(1); // e.g. "1144903983A78127S"

                        // Check if param value itself ends with 'S' or 'P'
                        if (paramValue.endsWith("S") || paramValue.endsWith("P")) {
                            final int startIndex = 3; // 4th character (0-based index 3)
                            final int requiredLength = 7;

                            // Ensure paramValue has enough characters to extract the 7-digit candidate
                            if (paramValue.length() >= startIndex + requiredLength) {
                                String candidate = paramValue.substring(startIndex, startIndex + requiredLength);
                                // Candidate must be exactly 7 contiguous digits
                                if (candidate.matches("\\d{7}")) {
                                	// Prepend 'P' if paramValue ends with 'P', otherwise keep candidate as-is
                                    if (paramValue.endsWith("P")) {
                                        memberId = "P" + candidate; // e.g. "P4903983"
                                    } else {
                                        memberId = candidate;      // e.g. "4903983"
                                    }
                                } else {
                                    memberId = null;
                                }
                            } else {
                                memberId = null;
                            }
                        } else {
                            // paramValue does not end with 'S' or 'P'
                            memberId = null;
                        }
                    } else {
                        // 'param=' parameter not found in URL
                        memberId = null;
                    }
                } else {
                    // Input does not start with the base URL
                    memberId = null;
                }
            }
            
            // Accept short numeric codes (3-7 digits) as before
            /*if (value.matches("^\\d{3,7}$")) {
                memberId = value;
            } else {
                String baseUrl = "https://pl.neduet.edu.pk/qrapp/qrapp.jsp?";
                // Require input to start with the base URL AND end with 'S' or 'P'
                if (value.startsWith(baseUrl) && (value.endsWith("S") || value.endsWith("P"))) {
                    int p = value.indexOf("param=");
                    if (p >= 0) {
                        // Do NOT strip off other query params per your instruction
                        String param = value.substring(p + "param=".length());

                        final int startIndex = 3; // 4th character (1-based) -> index 3 (0-based)
                        final int requiredLength = 7;

                        // Must have at least startIndex + requiredLength characters
                        if (param.length() >= startIndex + requiredLength) {
                            String candidate = param.substring(startIndex, startIndex + requiredLength);
                            // candidate must be exactly 7 contiguous digits
                            if (candidate.matches("\\d{7}")) {
                                memberId = candidate;
                            } else {
                                memberId = null;
                            }
                        } else {
                            memberId = null;
                        }
                    } else {
                        memberId = null;
                    }
                } else {
                    // input is not a recognized URL (either wrong base or doesn't end with S/P) -> invalid
                    memberId = null;
                }
            }*/
            
            
			/*
			 } else { memberId = null; }
			 */
        

            if (memberId == null) {
                error = "Invalid QR code or Member ID format.";
            } else {
                KohaPatronService service = new KohaPatronService();
                JSONObject patronObj = service.getKohaPatronByCardNumber(memberId);
                if (patronObj == null) {
                    error = "No patron found for this Member ID.";
                } else {
                    int patronId = patronObj.optInt("patron_id", -1);
                    if (patronId == -1) {
                        error = "Unable to retrieve patron ID.";
                    } else {
                        // Success: open Koha page in new window via JS
                        request.setAttribute("patronId", patronId);
                        request.getRequestDispatcher("/circulation/qrCheckOut.jsp").forward(request, response);
                        return;
                    }
                }
            }
        } else {
            error = "QR code is required.";
        }

        // On error, forward with error message
        request.setAttribute("errorMsg", error);
        request.getRequestDispatcher("/circulation/qrCheckOut.jsp").forward(request, response);
    }

//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//        throws ServletException, IOException {
//        HttpSession session = request.getSession(false);
//        if (session == null || session.getAttribute("kohaUserid") == null) {
//            response.sendRedirect(request.getContextPath() + "/kohaPluginLogin.jsp?route=qrcheckout");
//            return;
//        }
//        request.getRequestDispatcher("/qrcheckout.jsp").forward(request, response);
//    }
}