package com.KohaPlugins.util;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * Ready-to-use servlet for displaying Tomcat environment details and system variables.
 * Deploy on Tomcat 10+ (Jakarta EE namespace).
 * Access via: http://localhost:8080/YourWebApp/envdetails
 */
@WebServlet("/envdetails")
public class EnvDetailsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");

        // Print Java system properties
        resp.getWriter().println("==== System Properties ====");
        Properties props = System.getProperties();
        for (String key : props.stringPropertyNames()) {
            resp.getWriter().println(key + " = " + props.getProperty(key));
        }

        resp.getWriter().println();

        // Print environment variables
        resp.getWriter().println("==== Environment Variables ====");
        for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
            resp.getWriter().println(entry.getKey() + " = " + entry.getValue());
        }

        resp.getWriter().println();

        // Print servlet context info (Tomcat context parameters)
        resp.getWriter().println("==== Servlet Context Info ====");
        resp.getWriter().println("Context Path: " + getServletContext().getContextPath());
        resp.getWriter().println("Server Info: " + getServletContext().getServerInfo());
        resp.getWriter().println("Servlet API Version: " +
                getServletContext().getMajorVersion() + "." +
                getServletContext().getMinorVersion());
        resp.getWriter().println("file.encoding = " + System.getProperty("file.encoding"));
    }
}