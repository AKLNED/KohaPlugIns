<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>KohaPlugIns Home</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/common/styles.css">
    <style>
        body {
            background: #f8f8f8;
        }
        .home-links {
            margin: 2em auto;
            max-width: 500px;
            padding: 2em;
            background: #fff;
            border-radius: 10px;
            text-align: center;
            box-shadow: 0 2px 8px rgba(128,0,0,0.05);
            border: 1px solid #eee;
        }
        .home-links h2 {
            margin-bottom: 1em;
            color: #800000;
            letter-spacing: 1px;
        }
        .button-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 1.2em;
            margin-top: 1.5em;
        }
        .home-links a {
            display: block;
            padding: 1.1em 0.5em;
            background: #5ca6d6; /* Koha blue */
            color: #fff; /* white text */
            text-decoration: none;
            border-radius: 6px;
            font-size: 1.1em;
            font-weight: bold;
            transition: background 0.2s, color 0.2s, border 0.2s;
            border: 2px solid #428bca;
            box-sizing: border-box;
        }
        .home-links a:hover {
            background: #fff;
            color: #800000;
            border-color: #800000;
        }
    </style>
</head>
<body>
    <%@ include file="/common/header.jsp" %>
    <div class="home-links">
        <h2>Welcome to Koha PlugIns</h2>
        <div class="button-grid">
            <a href="<%=request.getContextPath()%>/kohaPluginLogin.jsp?route=patron">
                Student Add/Update
            </a>
            <a href="<%=request.getContextPath()%>/kohaPluginLogin.jsp?route=qrcheckout">
                QR Code Check-Out
            </a>
            <a href="<%=request.getContextPath()%>/kohaPluginLogin.jsp?route=bbkrent">
                Book Bank Rent
            </a>
            <a href="<%=request.getContextPath()%>/kohaPluginLogin.jsp?route=pr">
                Price Recovery
            </a>
<%--             <a href="<%=request.getContextPath()%>/GenNextSBarcodeServlet"> --%>
            <a href="${pageContext.request.contextPath}/GenNextSBarcodeServlet">
                Generate Next Serial BarCode
            </a>
            <a href="#">
                Button 6
            </a>
        </div>
    </div>
</body>
</html>