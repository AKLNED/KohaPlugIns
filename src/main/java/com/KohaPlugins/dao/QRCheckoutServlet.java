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

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("kohaUserid") == null) {
            response.sendRedirect(request.getContextPath() + "/kohaPluginLogin.jsp?route=qrcheckout");
            return;
        }

        String qrInput = request.getParameter("qrCode");
        String error = null;
        String memberId = null;

        if (qrInput != null && !qrInput.trim().isEmpty()) {
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
            }

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