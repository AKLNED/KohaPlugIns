<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">


<title>EAKL - Patron PlugIn</title>
<!-- <link rel="stylesheet" href="common/styles.css"> -->


<link rel="stylesheet" href="<%=request.getContextPath()%>/css/consolidated-styles.css">
<script type="text/javascript">

<!-- Protects sensitive JSPs and servlets with a session check (e.g., check for an attribute like kohaUserid in session).
If not present, redirect to your login page before any API call is attempted. -->
<%
if (session.getAttribute("kohaUserid") == null) {
	response.sendRedirect(request.getContextPath()+"/kohaPluginLogin.jsp");
	return;
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
				
					<h3 >Stop Registration Update</h3>
									
							<div class="info">
							<strong><br>Result:
							<%= request.getAttribute("message") %></strong></div>
							
							<div class="button-row">
							<button type="button"
								onclick="window.location.href='http://192.168.14.231:8080/jasperserver/flow.html?_flowId=viewReportFlow&_flowId=viewReportFlow&ParentFolderUri=%2Freports%2FKoha_Reports%2FCirculation&reportUnit=%2Freports%2FKoha_Reports%2FCirculation%2FSemRegSuspended&standAlone=true&j_username=joeuser&j_password=joeuser&PAR_USER=Value'">
								&nbsp; View Report &nbsp;</button>
							<button type="button" onclick="window.location.href='https://www.eakl.neduet.edu.pk:8001/cgi-bin/koha/circ/circulation-home.pl'">
							&nbsp; Return to Koha &nbsp;</button></div>
								
						
				
			</section>
			<section class="message-section"></section>
		</main>
	</div>

	<%@ include file="/common/footer.jsp"%>
</body>
</html>



