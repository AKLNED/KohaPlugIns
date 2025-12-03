<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="org.json.JSONArray, org.json.JSONObject" %>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>

<%
    JSONArray arr = (JSONArray) request.getAttribute("defaulters");
%>

<html lang="en">
<head>
<meta charset="UTF-8">
<title>EAKL - Patron PlugIn</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/consolidated-styles.css">

<% if (session.getAttribute("kohaUserid") == null) {
       response.sendRedirect(request.getContextPath()+"/kohaPluginLogin.jsp");
       return;
   }
%>
</head>

<body>

<%@ include file="/common/header.jsp" %>
<%@ include file="/common/searchbox.jsp" %>

<div class="main-layout">
	<%@ include file="/common/sidebar.jsp" %>

	<main class="content">
		<section class="forms-section">
			<h3>Defaulter List</h3>
			
			<form method="post" action="<%=request.getContextPath()%>/UnblockDefaulterServlet" class="forms-standard" id="UnblockRegForm">
			
	
						<div class="form-row"> <label for="studentId">Member ID/ QR Code:</label> <input
							type="text" id="studentId" name="studentId" required>
							<button type="submit">Allow Registration</button></div>
							<br>
						
									
			<div class="button-row">
<!-- 			<button type="submit">Allow Registration</button> -->
			<button type="button" onclick="window.location.href='<%= request.getContextPath() %>/ListBlockedDefaulterServlet'">&nbsp;
			Refresh Page &nbsp;</button>
			<button type="button" onclick="window.location.href='https://www.eakl.neduet.edu.pk:8001/cgi-bin/koha/circ/circulation-home.pl'">&nbsp;
			Return to Koha &nbsp;</button>
			</div>
		</form>
		</section>

		<section class="message-section">
		
		<% if (request.getAttribute("message") != null) { %>
		
		<div class="info">
							<strong><br>Result:
							<%= request.getAttribute("message") %></strong></div>
						
												
						<%}
					 
		else { %>
		<h3>Defaulter List</h3>
		
		<div>
			<table class="striped-table">
				<thead>
					<tr>
						<th>S No.</th>
						<th>Card Number</th>
						<th>Member Name</th>
						<th>Borrower No</th>
						<th>Update Date</th>
						<th>Status</th>
					</tr>
				</thead>
				<tbody>

				<%
					if (arr != null) {
						for (int i = 0; i < arr.length(); i++) {
							JSONObject o = arr.getJSONObject(i);
				%>
					<tr>
<%-- 						<td><%= o.optString("serial_no") %></td> --%>
						<td><%= i+1 %></td>							
						<td><%= o.optString("student_id") %>
<%-- 							<input type="hidden" name="student_id" value="<%= o.optString("student_id") %>"></td> --%>
						<td><%= o.optString("student_name") %>
<%-- 						<input type="hidden" name="student_name" value="<%= o.optString("student_name") %>"></td> --%>
						<td><%= o.optString("borrower_no") %>
<%-- 						<input type="hidden" name="borrower_no" value="<%= o.optString("borrower_no") %>"></td> --%>
						<td><%= o.optString("last_upd_dt") %></td>
						<td><%= o.optString("lib_def") %></td>
					</tr>
				<%
						}
					}
		}
				%>

				</tbody>
			</table>

			<br>
			</div>
		</section>
	</main>
</div>

<%@ include file="/common/footer.jsp" %>


<script>
	document.addEventListener("DOMContentLoaded", function() {
	    var userIdInput = document.getElementById("studentId");
	    //var userCategoryInput = document.getElementById("category");
	    var form = document.getElementById("UnblockRegForm");

	    function extractUserId(value) {
	        value = value.trim();
	        var userCategory = userCategoryInput ? userCategoryInput.value : "";

	        // EMP restriction
	        const baseUrl = "https://pl.neduet.edu.pk/qrapp/qrapp.jsp?";
	        /* if (userCategory === "EMP" && value.startsWith(baseUrl)) {
	            alert("Category and Input do not match.");
	            return null;
	        }
 */
	        // Direct ID
	        if (/^\d{3,7}$/.test(value)) {
	            return value;
	        }

	        	        
	        if (!value.startsWith(baseUrl)) {
	            // not the expected base URL
	            alert("Input does not match expected patterns.");
	            return null;
	        }

	        // must end with S or P
	        if (!(value.endsWith("S") || value.endsWith("P"))) {
	            alert('Input URL must end with S (UG) or P (PG).');
	            return null;
	        }

	        /* // category must match final character
	        if (value.endsWith("S") && userCategory !== "UG") {
	            alert('Category and Input do not match.');
	            return null;
	        }
	        if (value.endsWith("P") && userCategory !== "PG") {
	            alert('Category and Input do not match.');
	            return null;
	        } */

	        // get param=... (do NOT strip additional query params per your instruction)
	        const param = (value.split("param=")[1] || "");

	        // start from 4th character (1-based) -> index 3 (0-based)
	        const startIndex = 3;
	        const requiredLength = 7;

	        if (param.length < startIndex + requiredLength) {
	            alert('Param value too short to contain member ID.');
	            return null;
	        }

	        const candidate = param.substr(startIndex, requiredLength); // contiguous substring

	        if (!/^\d{7}$/.test(candidate)) {
	            alert('Invalid member ID format.');
	            return null;
	        }

	        return candidate;
	    }

	        //alert("Input does not match expected patterns.");
	        //return null;
	    //}

	    if (userIdInput) {
	        userIdInput.addEventListener("keydown", function(e) {
	            if (e.key === "Enter") {
	                let newValue = extractUserId(userIdInput.value);
	                if (newValue === null) {
	                    e.preventDefault();
	                    return false;
	                }
	                userIdInput.value = newValue;
	            }
	        });
	    }

	    if (form && userIdInput) {
	        form.addEventListener("submit", function(e) {
	            let newValue = extractUserId(userIdInput.value);
	            if (newValue === null) {
	                e.preventDefault();
	                return false;
	            }
	            userIdInput.value = newValue;
	        });
	    }
	});
</script>

</body>
</html>
