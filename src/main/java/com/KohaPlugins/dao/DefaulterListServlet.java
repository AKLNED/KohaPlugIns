package com.KohaPlugins.dao;

import com.KohaPlugins.service.KohaPatronFunctionService;
import org.json.JSONArray;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/DefaulterListServlet")
public class DefaulterListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Session check — OPTIONAL if handled in JSP already
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("kohaUserid") == null) {
            resp.sendRedirect(req.getContextPath() + "/kohaPluginLogin.jsp");
            return;
        }

        KohaPatronFunctionService service = new KohaPatronFunctionService();

        try {
            JSONArray defaulters = service.fetchDefaultersAsJsonArray();
            req.setAttribute("defaulters", defaulters);

        } catch (SQLException e) {
            throw new ServletException("Error fetching defaulters", e);
        }

        RequestDispatcher rd = req.getRequestDispatcher("/patron/defaulterList.jsp");
        rd.forward(req, resp);
    }
}
