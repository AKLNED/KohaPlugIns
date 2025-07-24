<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String barcode = (String) request.getAttribute("barcode");
    String rentResult = (String) request.getAttribute("rentResult");
    String kohaJson = (String) request.getAttribute("kohaJson");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Book Details and Rent</title>
    <style>
        pre { background: #f7f7f7; border: 1px solid #ddd; padding: 10px; }
    </style>
</head>
<body>
    <h2>Book Details</h2>
    <table border="1">
        <tr><th>Barcode</th><td><%= barcode != null ? barcode : "Unknown" %></td></tr>
        <tr><th>Rent</th><td><%= rentResult != null ? rentResult : "Not calculated" %></td></tr>
    </table>
    <h3>Koha API Response</h3>
    <pre id="json"></pre>
    <script>
        var json = <%= kohaJson != null ? "\"" + kohaJson.replace("\"", "\\\"") + "\"" : "null" %>;
        if(json) {
            try {
                document.getElementById('json').textContent = JSON.stringify(JSON.parse(json), null, 2);
            } catch (e) {
                document.getElementById('json').textContent = json;
            }
        }
    </script>
    <p><a href="calculateRent.jsp">Back</a></p>
</body>
</html>