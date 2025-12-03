<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<title>EAKL - Patron PlugIn</title>

<!-- <link rel="stylesheet" href="/KohaPlugins/common/styles.css"> -->

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/consolidated-styles.css">

<script type="text/javascript">

<!-- Protects sensitive JSPs and servlets with a session check (e.g., check for an attribute like kohaUserid in session).
If not present, redirect to your login page before any API call is attempted. -->

<%
if (session.getAttribute("kohaUserid") == null) {
	response.sendRedirect(request.getContextPath()+"/kohaPluginLogin.jsp?route=patron");
	return;
}
%>
<!-- Checks for response message from InsertKohaServlet in case of error in insertion -->
					<%
					String message = (String) request.getAttribute("message");
					%>
</script>

</head>
<body>

	<%@ include file="/common/header.jsp"%>

	<%@ include file="/common/searchbox.jsp"%>

	<!-- Main Layout: Sidebar and Content -->
	<div class="main-layout">
		<!-- Sidebar Section -->
		<%@ include file="/common/sidebar.jsp"%>

		<!-- Main Content Section -->
		<main class="content">
			<section class="forms-section">
				<div >
					<h3 >Add / Update Patrons to Koha</h3>
					<br>
					
					
					<!--  <h2>Student Information Lookup</h2>-->
					<form action="/KohaPlugins/StudentLookupServlet" method="POST" id="loginForm" class="forms-standard">

						<div class="form-row"><label for="category">Category:</label> <select name="category"
							id="category">
							<option value="UG">Undergraduate Student</option>
							<option value="PG">Postgraduate Student</option>
							<option value="EMP">Faculty/Employee</option>
						</select></div>
						<div class="form-row"> <label for="studentId">Member ID/ QR Code:</label> <input
							type="text" id="studentId" name="studentId" required></div></br>
						<div class="button-row"> <input type="submit" value=" Lookup "></div>
					</form>
				</div>
			</section>
			<section class="message-section">
			
			<%
					if (message != null) {
					%>
					<div class="error">
						<%=message%></div>
					<%
					}
					%>
			
			</section>
		</main>
	</div>

	<%@ include file="/common/footer.jsp"%>
	
	
	<script>
	document.addEventListener("DOMContentLoaded", function() {
	    var userIdInput = document.getElementById("studentId");
	    var userCategoryInput = document.getElementById("category");
	    var form = document.getElementById("loginForm");

	    function extractUserId(value) {
	        value = value.trim();
	        var userCategory = userCategoryInput ? userCategoryInput.value : "";

	        // EMP restriction
	        const baseUrl = "https://pl.neduet.edu.pk/qrapp/qrapp.jsp?";
	        if (userCategory === "EMP" && value.startsWith(baseUrl)) {
	            alert("Category and Input do not match.");
	            return null;
	        }

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

	        // category must match final character
	        if (value.endsWith("S") && userCategory !== "UG") {
	            alert('Category and Input do not match.');
	            return null;
	        }
	        if (value.endsWith("P") && userCategory !== "PG") {
	            alert('Category and Input do not match.');
	            return null;
	        }

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
