package com.KohaPlugins.dao;


import com.KohaPlugins.service.OracleMemberService;

import java.io.IOException;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.json.JSONArray;

@WebServlet("/ListBlockedDefaulterServlet")
public class ListBlockedDefaulterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private OracleMemberService oracleService = new OracleMemberService();
    
	 
     @Override
     protected void doGet(HttpServletRequest req, HttpServletResponse resp)
             throws ServletException, IOException {
    	 
    	 HttpSession session = req.getSession(false);
         if (session == null || session.getAttribute("kohaUserid") == null) {
             resp.sendRedirect("/KohaPlugins/kohaPluginLogin.jsp");
             return;
         }
         req.setAttribute("message", null);
         try {
             // Fetch JSON array of blocked defaulters from Oracle
             JSONArray defaulters = oracleService.fetchSISDefaultersAsJsonArray();

             // Attach to request so JSP can read it
             req.setAttribute("defaulters", defaulters);

         } catch (Exception e) {
             e.printStackTrace();
             req.setAttribute("defaulters", new JSONArray());
             req.setAttribute("message", "Unable to fetch defaulters list.");
         }

         // Forward to JSP
         RequestDispatcher rd = req.getRequestDispatcher("/patron/unblockDefaulter.jsp");
         rd.forward(req, resp);
     }

     @Override
     protected void doPost(HttpServletRequest req, HttpServletResponse resp)
             throws ServletException, IOException {
         doGet(req, resp);
     }
}
