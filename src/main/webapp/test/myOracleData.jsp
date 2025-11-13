<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ page import="java.sql.*, com.KohaPlugins.util.dbConn" %>
<!DOCTYPE html>
<html>
<head>
    <title>Oracle 10g Connection Usage Example</title>
<link rel="stylesheet" href="css/consolidated-styles.css">
</head>
<body>
	
	
    <h1>Database Connection to Oracle 10g in JSP</h1>
    
    <%
    
    
        // Oracle database credentials
        //String url = "jdbc:oracle:thin:@192.168.14.199:1521:dev";  // Replace <IP_ADDRESS> with the Oracle server's IP
        //String user = "siraj";  // Replace with your Oracle database username
        //String password = "ora13ned";  // Replace with your Oracle database password

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Load Oracle JDBC driver
            //Class.forName("oracle.jdbc.driver.OracleDriver");

            // Get MySQL connection from the utility class
            conn = dbConn.getOracleConnection();
            
            

            // Create a statement object
            stmt = conn.createStatement();
            //String query = "SELECT student_id, initcap(father_name) \"surname\" ,student_name  FROM v_student_data where student_id =4703799 ";  // Replace with your SQL query
            String query = "SELECT initcap(father_name) \"surname\", student_name  , " +
                           "initcap(present_address) \"address\", initcap(permanent_address) \"address2\", " +
                           "CONTACT_NO \"phone\", MOBILE_NO \"mobile\", lower(gsuite_id) \"email\", " +
                           "student_id \"cardnumber\", DECODE(DISC_ABBREV,'AR','CC','DS','CC','BM','LEJ','TCE','TIEST','TCT','TIEST','AKL') \"branchcode\", " +
                           "DECODE(ACADEMIC_YEAR_ID, 1, 'UG1', 2,'UG2' , 3,'UG3' , 4, 'UG4', 5,'UG5' ) \"categorycode\", " +
                           "to_char(sysdate,'DD/MM/RRRR') \"dateenrolled\", STUDENT_ID \"userid\", '' \"password\", " +
                           "'BATCH:'||BATCH_NAME ||',AC_YEAR:'||ACADEMIC_YEAR_ID ||',DEPT:'||nvl(DEPT_ABBREV,'CSE-T') " +
                           "||',DISC:'||DISC_ABBREV ||',PAT_NO:'||roll_no||'/'||BATCH_NAME " +
                           "||',TYPE:'||DECODE(IS_ODD,0,to_char((2*ACADEMIC_YEAR_ID)), 1, to_char((2*ACADEMIC_YEAR_ID)-1) ) \"patron_attributes\" " +
                           "FROM V_STUDENT_DATA WHERE student_id = 	4703799 ";
            
           
            
            rs = stmt.executeQuery(query);

            // Output the data from the ResultSet
            while (rs.next()) {
                out.println("StudentID: " + rs.getString("cardnumber") + ", Name: " + rs.getString("student_name") + "<br>");
            }
        } catch (SQLException e) {
            out.println("SQL Exception: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            out.println("Class Not Found Exception: " + e.getMessage());
        } finally {
            // Close resources
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
