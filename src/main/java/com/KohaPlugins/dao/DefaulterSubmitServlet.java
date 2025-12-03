package com.KohaPlugins.dao;

import com.KohaPlugins.service.OracleMemberService;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



import com.KohaPlugins.model.DefaulterRow;

@WebServlet("/DefaulterSubmitServlet")
public class DefaulterSubmitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	
    	/* Protects sensitive JSPs and servlets with a session check (e.g., check for an attribute like kohaUserid in session).
		If not present, redirect to your login page before any API call is attempted. */
    	HttpSession session = req.getSession(false);
    	if (session.getAttribute("kohaUserid") == null) {
			resp.sendRedirect("/KohaPlugins/kohaPluginLogin.jsp");
			return;
		}

    	// Receive arrays of fields
        String[] cardnumbers = req.getParameterValues("cardnumber");
        String[] patnos      = req.getParameterValues("pat_no");
        String[] firstnames  = req.getParameterValues("firstname");

        List<DefaulterRow> rows = new ArrayList<>();

        // Defensive check
        if (cardnumbers != null) {
            for (int i = 0; i < cardnumbers.length; i++) {
                rows.add(new DefaulterRow(
                        cardnumbers[i],
                        patnos[i],
                        firstnames[i]
                ));
            }
        }

		/*
		 * OracleMemberService oracleService = new OracleMemberService();
		 * 
		 * // Call the Oracle function int count = oracleService.updateDefaulters(rows);
		 * 
		 * req.setAttribute("message", count + " registration blocked !!");
		 * RequestDispatcher rd =
		 * req.getRequestDispatcher("/patron/defaulterResult.jsp"); rd.forward(req,
		 * resp);
		 */
        
        try {
            OracleMemberService oracleService = new OracleMemberService();

            // Call the Oracle function
            int count = oracleService.updateDefaulters(rows);

            req.setAttribute("message", count + " registration blocked !!");
     
        } catch (Exception e) {
            // Handle other exceptions (e.g., NullPointerException)
            e.printStackTrace();
            req.setAttribute("message", "Error processing request: " + e.getMessage());
        } finally {
            // Forward to result JSP regardless of success/failure
            RequestDispatcher rd = req.getRequestDispatcher("/patron/defaulterResult.jsp");
            rd.forward(req, resp);
        }
    }
}
