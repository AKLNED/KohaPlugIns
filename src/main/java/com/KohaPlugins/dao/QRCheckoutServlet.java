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

        /*if (qrInput != null && !qrInput.trim().isEmpty()) {
            String value = qrInput.trim();
            String baseUrl = "https://pl.neduet.edu.pk/qrapp/qrapp.jsp?";
            if (value.matches("^\\d{3,7}$")) {
                memberId = value;
            } else if (value.startsWith(baseUrl)) {
                String param = value.contains("param=") ? value.split("param=")[1] : "";
                if (value.endsWith("S")) {
                    String[] parts = param.split("R");
                    if (parts.length > 1) {
                        memberId = parts[0];
                        if (memberId.length() > 7) {
                            memberId = memberId.substring(memberId.length() - 7);
                        }
                    }
                } else if (value.endsWith("P")) {
                    String[] parts = param.split("i");
                    if (parts.length > 1) {
                        memberId = parts[0];
                        if (memberId.length() > 7) {
                            memberId = memberId.substring(memberId.length() - 7);
                        }
                    }
                }
            }*/
        
        if (qrInput != null && !qrInput.trim().isEmpty()) {
            String value = qrInput.trim();

            // Accept short numeric codes (3-7 digits) as before
            if (value.matches("^\\d{3,7}$")) {
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
            }
			/*
			 * } else { memberId = null; }
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