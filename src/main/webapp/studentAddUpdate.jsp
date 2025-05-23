<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Engr Abul Kalam Library - PlugIns</title>
    <link rel="stylesheet" href="styles.css">
    <style type="text/css">
<!--
.style1 {color: #800000}
.style2 {color: #428bca}
.style3 {color: #80000; }
-->
.message {
    		color: blue;
    		font-weight: demibold;
    		margin-bottom: 1em;
		} 
    </style>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"></head>
<body>

    <%@ include file="header.jsp" %>

    <%@ include file="searchbox.jsp" %>

    <!-- Main Layout: Sidebar and Content -->
    <div class="main-layout">
        <!-- Sidebar Section -->
        <%@ include file="sidebar.jsp" %>

        <!-- Main Content Section -->
        <main class="content">
            <section class="new-arrivals">
            <div class="message">
                <h3 class="style3">Add / Update Patrons to Koha</h2>
                
                <% String message = (String)request.getAttribute("message"); %>
    <% if (message != null) { %>
        <div class="success"><br><%= message %></div>
    <% } %>
    <br>
    <!--  <h2>Student Information Lookup</h2>-->
    <form action="StudentLookupServlet" method="POST">
        
        <label for="category">Category:</label>
        <select name="category" id="category">
            <option value="UG">Undergraduate Student</option>
            <option value="PG">Postgraduate Student</option>
            <option value="EMP">Faculty/Employee</option>
            </select><br><br>
            
            <label for="studentId">Member ID:</label>
        <input type="text" id="studentId" name="studentId" required><br><br>
        
        <input type="submit" value=" Lookup ">
    </form>
            </div>
            </section>
            <section class="announcements"></section>
        </main>
</div>

        <%@ include file="footer.jsp" %>
</body>
</html>
