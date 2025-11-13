
<%@ page import="com.KohaPlugins.util.AuthBasic"%>
<%@ page import="jakarta.servlet.http.HttpSession"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<html lang="en">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<title>Koha Plugins Login Page</title>
<!-- <link rel="stylesheet" -->
<%-- 	href="<%=request.getContextPath()%>/common/styles.css"> --%>


<link rel="stylesheet" href="css/consolidated-styles.css">

<script type="text/javascript">

<%String error = null;
if ("POST".equalsIgnoreCase(request.getMethod())) {
	String userid = request.getParameter("userid");
	String password = request.getParameter("password");

	if (userid == null || userid.isEmpty() || password == null || password.isEmpty()) {
		error = "User ID and Password are required.";
	} else {
		// Set credentials in AuthBasic
		AuthBasic.setCredentials(userid, password);

		// Test credentials against Koha API (use a lightweight endpoint like /api/v1/patrons?limit=1)

		int responseCode = AuthBasic.validateKohaCredentials();
		boolean isAuthenticated = (responseCode == 200);

		if (isAuthenticated) {

			// Set session timeout to 15 minutes
			AuthBasic.setSessionTimeout(request);

			// Optional: Store username in session if needed
			//HttpSession session = request.getSession();
			session.setAttribute("kohaUserid", userid);

			// Forward to relevant page based on route parameter
			//response.sendRedirect(request.getContextPath() + "/patron/studentAddUpdate.jsp");
			String route = request.getParameter("route");
			//System.out.println("Route parameter: '" + route + "'");

			if (route != null) {
				if (route.equals("patron")) {
					response.sendRedirect(request.getContextPath() + "/patron/studentAddUpdate.jsp");
					return;
				} else if (route.equals("qrcheckout")) {
					response.sendRedirect(request.getContextPath() + "/circulation/qrCheckOut.jsp");
					return;
				} else if (route.equals("bbkrent")) {
					response.sendRedirect(request.getContextPath() + "/biblio/calculateRent.jsp");
					return;
				} else if (route.equals("pr")) {
					response.sendRedirect(request.getContextPath() + "/circulation/calculatePR.jsp");
					return;
				}
			}
			// Default behavior (if no route or unknown route)
			response.sendRedirect(request.getContextPath() + "/index.jsp");
			return;

		} else {
			//error = "Invalid Koha credentials. Please try again.";
					
			String message;
    switch (responseCode) {
        case 200:
            message = "✅ Connection successful. Koha credentials are valid.";
            break;
        case 401:
            message = "❌ Authentication failed. Please check your Koha username or password.";
            break;
        case 403:
            message = "⚠️ Access denied. Your account may not have permission to use this Plugin.";
            break;
        case 404:
            message = "🔍 API endpoint not found. Check the Koha server URL.";
            break;
        case 500:
            message = "💥 Koha server error. Try again later.";
            break;
        case -1:
            message = "🌐 Could not connect to Koha server. Please check the network or SSL settings.";
            break;
        case -2:
            message = "❗ Invalid API URL format.";
            break;
        default:
            message = "⚠️ Unexpected response code: " + responseCode;
            break;
    }

		}

	}
}
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
				<div>

					<h3>Welcome to Koha PlugIns</h3>


					<form method="post" action="kohaPluginLogin.jsp" id="loginForm"
						class="forms-standard">
						<input type="hidden" name="route"
							value="<%= request.getParameter("route") != null ? request.getParameter("route") : "" %>">
<br />
						<div class="form-row"><label for="userid">User ID:</label>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type="text" name="userid"
							id="userid" value="" required autocomplete="off" /></div>
						<div class="form-row"><label for="password">Password:</label> 
						&nbsp;<input type="password"
							name="password" id="password" value="" required
							autocomplete="off" /></div><br />  
						<div class="button-row"><input type="submit"
							value="&nbsp; Login &nbsp;" /></div>
					</form>

				</div>
			</section>
			<section class="message-section">


				<%					
					if (error != null) {
					%>
				<div class="error"><%=error%></div>

				<%
					}
					%>

			</section>
		</main>
	</div>
	<%@ include file="/common/footer.jsp"%>


</body>
</html>

