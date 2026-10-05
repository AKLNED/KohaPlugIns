package com.KohaPlugins.dao;

import com.KohaPlugins.service.KohaPatronFunctionService;
import com.KohaPlugins.service.OracleMemberService;
import com.KohaPlugins.util.dbConn;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;



@WebServlet("/UnblockDefaulterServlet")
public class UnblockDefaulterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private OracleMemberService oracleService = new OracleMemberService();
    private KohaPatronFunctionService mysqlService   = new KohaPatronFunctionService();
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("kohaUserid") == null) {
            resp.sendRedirect("/KohaPlugins/kohaPluginLogin.jsp");
            return;
        }

        String studentId = req.getParameter("studentId");
        String message = "";
        Connection oracleConn = null;

        try {
            // Step 1: Check if student is a defaulter in Oracle
            int isDefaulterSIS = oracleService.isDefaulterSIS(studentId);
          //for testing
          // isDefaulterSIS =1;
            if (isDefaulterSIS==0) {
                message = "This member is not Blocked for Registration";
                req.setAttribute("message", message);
                forwardToResult(req, resp);
                return;
            }

            // Step 2: Check if member has cleared dues in MySQL
            int isStillDefaulter = mysqlService.isDefaulterKoha(studentId);
            //for testing
            // isStillDefaulter =0;
            if (isStillDefaulter==1) {
                message = "This member has not cleared his/her dues";
                req.setAttribute("message", message);
                forwardToResult(req, resp);
                return;
            }

            // Step 3: Update defaulter flag in Oracle
            oracleConn = dbConn.getOracleConnection();
            oracleConn.setAutoCommit(false);

            int updateResult = oracleService.updateLibDefFlag(studentId, oracleConn);

            if (updateResult == 1) {
                oracleConn.commit();
                message = "Member has been successfully unblocked";
            } else {
                oracleConn.rollback();
                message = "Failed to unblock the member";
            }

        } catch (SQLException sqle) {
            message = "Database error: " + sqle.getMessage();
            try { if (oracleConn != null) oracleConn.rollback(); } catch (Exception ex) {}
            sqle.printStackTrace();

        } catch (Exception e) {
            message = "Error: " + e.getMessage();
            try { if (oracleConn != null) oracleConn.rollback(); } catch (Exception ex) {}
            e.printStackTrace();

        } finally {
            try { if (oracleConn != null) oracleConn.close(); } catch (Exception ex) {}
        }

        req.setAttribute("message", message);
        forwardToResult(req, resp);
    }

    private void forwardToResult(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        RequestDispatcher rd = req.getRequestDispatcher("/patron/unblockDefaulter.jsp");
        rd.forward(req, resp);
    }
}
