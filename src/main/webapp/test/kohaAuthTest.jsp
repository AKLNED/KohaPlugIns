<%@ page import="com.KohaPlugins.util.AuthManager" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Koha Endpoint Authentication Test</title>
<link rel="stylesheet" href="css/consolidated-styles.css">
</head>
<body>
    <h2>Koha Endpoint Authentication Test</h2>
    <%
        String token = null;
        String error = null;
        long expiry = 0;
        try {
            token = AuthManager.getAccessToken();
            expiry = AuthManager.getTokenExpiry();
        } catch (Exception e) {
            error = e.getMessage();
        }
    %>
    <%
        if (error != null) {
    %>
        <p class="c_f479d19b">Error obtaining access token: <%= error %></p>
    <%
        } else {
    %>
        <p class="c_e24d296e">Access token obtained successfully!</p>
        <p><strong>Access Token:</strong> <code class="c_49a359d2"><%= token %></code></p>
        <p><strong>Token Expiry (ms since epoch):</strong> <%= expiry %></p>
        <p><strong>Token Expiry (date/time):</strong> <%= (new java.util.Date(expiry)).toString() %></p>
    <%
        }
    %>
</body>
</html>