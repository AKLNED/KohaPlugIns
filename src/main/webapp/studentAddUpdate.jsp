<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Information</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
        }
        table, th, td {
            border: 1px solid black;
        }
        th, td {
            padding: 8px;
            text-align: left;
        }
        .success {
    		color: blue;
    		font-weight: bold;
    		margin-bottom: 1em;
		}
    </style>
</head>
<body>

	<% String message = (String)request.getAttribute("message"); %>
    <% if (message != null) { %>
        <div class="success"><%= message %></div>
    <% } %>
    
    <h2>Student Information Lookup</h2>
    <form action="StudentLookupServlet" method="POST">
        <label for="studentId">Student ID:</label>
        <input type="text" id="studentId" name="studentId" required><br><br>
        <label for="category">Category:</label>
        <select name="category" id="category">
            <option value="UG1">UG1</option>
            <option value="UG2">UG2</option>
            <option value="UG3">UG3</option>
            <option value="UG4">UG4</option>
            <option value="UG5">UG5</option>
        </select><br><br>
        <input type="submit" value="Search">
    </form>
</body>
</html>