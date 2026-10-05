<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ page import="java.sql.*, com.KohaPlugins.util.dbConn" %>
<!DOCTYPE html>
<html>
<head>
    <title>MySQL Connection Example</title>
<link rel="stylesheet" href="css/consolidated-styles.css">
</head>
<body>
    <h1>Database Connection in JSP</h1>
    
    <%
        // Database credentials
        //String url = "jdbc:mysql://192.168.14.237:3306/koha_library";  // Replace with your MySQL URL and database name
        //String user = "naveen";  // Replace with your MySQL username
        //String password = "Naveen123@";  // Replace with your MySQL password

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Load MySQL JDBC driver
            //Class.forName("com.mysql.cj.jdbc.Driver");  // MySQL 8.x and newer versions
            
            // Get MySQL connection from the utility class
            conn = dbConn.getMySQLConnection();
            
            // Create a statement
            stmt = conn.createStatement();
            String query = "SELECT branchcode,branchname FROM branches";  // Replace with your SQL query
            rs = stmt.executeQuery(query);

            // Output the data from the ResultSet
            while (rs.next()) {
                out.println("Code: " + rs.getString("branchcode") + ", Name: " + rs.getString("branchname") + "<br>");
            }
        } catch (SQLException e) {
            out.println("SQL Exception: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            out.println("Class Not Found Exception: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                out.println("Error closing resources: " + e.getMessage());
            }
        }
    %>
</body>
</html>
