
<%@ page import="com.KohaPlugins.util.AuthBasic"%>
<%@ page import="jakarta.servlet.http.HttpSession"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">

<title>Koha Plugin Login</title>
<link rel="stylesheet" href="styles.css">
<style type="text/css">
<!--
.style1 {
	color: #800000
}

.style2 {
	color: #428bca
}

.style3 {
	color: #80000;
}

-->
.message {
	color: blue;
	font-weight: demibold;
	margin-bottom: 1em;
}

        label {
            font-weight: bold;
            font-size: 1.2em;
        }
        input[type="text"], input[type="password"] {
            font-size: 1.2em;
            /*font-weight: bold;*/
        }
   
</style>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>

	<%@ include file="header.jsp"%>

	<%@ include file="searchbox.jsp"%>

	<!-- Main Layout: Sidebar and Content -->
	<div class="main-layout">
		<!-- Sidebar Section -->
		<%@ include file="sidebar.jsp"%>

		<!-- Main Content Section -->
		<main class="content">
			<section class="new-arrivals">
				<div class="message">


					<%
					String error = null;
					if ("POST".equalsIgnoreCase(request.getMethod())) {
						String userid = request.getParameter("userid");
						String password = request.getParameter("password");

						if (userid == null || userid.isEmpty() || password == null || password.isEmpty()) {
							error = "User ID and Password are required.";
						} else {
							// Set credentials in AuthBasic
							AuthBasic.setCredentials(userid, password);

							// Set session timeout to 15 minutes
							AuthBasic.setSessionTimeout(request);

							// Optional: Store username in session if needed
							//HttpSession session = request.getSession();
							session.setAttribute("kohaUserid", userid);

							// Forward to studentAddUpdate.jsp for user input
							response.sendRedirect("studentAddUpdate.jsp");
							return;
						}
					}
					%>
					<h3 class="style3">Welcome to Koha PlugIns</h3>
					<br>
					<%
					if (error != null) {
					%>
					<p style="color: red;"><%=error%></p>
					<%
					}
					%>
					<form method="post" action="kohaPluginLogin.jsp">
						<label for="userid">User ID:</label> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text"
							name="userid" id="userid" value="" required autocomplete="off" /><br />
						<br /> <label for="password">Password:</label> <input
							type="password" name="password" id="password" value="" required autocomplete="off" /><br />
						<br /> <input type="submit" value="&nbsp; Login &nbsp;" />
					</form>

				</div>
			</section>

		</main>
	</div>

	<%@ include file="footer.jsp"%>
</body>
</html>

